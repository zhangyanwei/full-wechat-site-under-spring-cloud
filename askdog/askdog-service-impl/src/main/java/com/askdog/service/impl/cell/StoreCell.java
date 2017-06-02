package com.askdog.service.impl.cell;

import com.askdog.dao.repository.EventRepository;
import com.askdog.dao.repository.ProductRepository;
import com.askdog.dao.repository.StoreEmployeeRepository;
import com.askdog.dao.repository.StoreRepository;
import com.askdog.dao.repository.mongo.StoreAttributeRepository;
import com.askdog.model.common.State;
import com.askdog.model.data.StoreAttribute;
import com.askdog.model.entity.Event;
import com.askdog.model.entity.Product;
import com.askdog.model.entity.Product.ProductTags;
import com.askdog.model.entity.Store;
import com.askdog.model.entity.StoreEmployee;
import com.askdog.service.bo.AgentDetail;
import com.askdog.service.bo.StoreDetail;
import com.askdog.service.bo.UserDetail;
import com.askdog.service.bo.common.Location;
import com.askdog.service.bo.store.ContactsUserDetail;
import com.askdog.service.bo.user.StoreEmployeeDetail;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.impl.cache.annotation.store.StoreBasicCache;
import com.askdog.service.impl.cache.annotation.store.StoreBasicCacheRefresh;
import com.askdog.service.impl.cache.annotation.store.StoresOwnedCache;
import com.askdog.service.impl.cache.annotation.store.StoresOwnedCacheRefresh;
import com.askdog.service.impl.storage.StorageRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static com.askdog.model.common.State.DELETED;
import static com.askdog.model.entity.StoreEmployee.EmployeeRole.CASHIER;
import static com.askdog.service.exception.NotFoundException.Error.STORE;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toList;

@Component
@Transactional
public class StoreCell {

    @Autowired private UserCell userCell;
    @Autowired private StoreRepository storeRepository;
    @Autowired private StorageRecorder storageRecorder;
    @Autowired private ProductRepository productRepository;
    @Autowired private StoreAttributeRepository storeAttributeRepository;
    @Autowired private StoreEmployeeRepository storeEmployeeRepository;
    @Autowired private EventRepository eventRepository;

    @Nonnull
    @StoreBasicCache
    public StoreDetail findDetail(long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NotFoundException(STORE));
        StoreAttribute storeAttribute = storeAttributeRepository.findByStoreId(storeId);

        StoreDetail storeDetail = new StoreDetail();
        storeDetail.setId(store.getId());
        storeDetail.setName(store.getName());
        storeDetail.setDescription(store.getDescription());
        storeDetail.setAddress(store.getAddress());
        storeDetail.setPhone(store.getPhone());
        storeDetail.setCreationTime(store.getCreationTime());
        storeDetail.setDeleted(store.getState() == State.DELETED);
        storeDetail.setSpecialProductId(getSpecialProductId(storeId, Product.ProductTags.SPECIAL));

        ContactsUserDetail contactsUserDetail = new ContactsUserDetail();
        contactsUserDetail.setName(store.getContactsName());
        contactsUserDetail.setPhone(store.getContactsPhone());
        if (storeAttribute != null) {
            contactsUserDetail.setTelephone(storeAttribute.getTelephone());
        }
        storeDetail.setContactsUserDetail(contactsUserDetail);

        UserDetail userDetail = userCell.findDetail(store.getOwner().getId());
        storeDetail.setOwner(userDetail);

        if (null != store.getAgent()) {
            AgentDetail agentDetail = new AgentDetail();
            agentDetail.setId(store.getAgent().getId());
            storeDetail.setAgent(agentDetail);
        }

        Long coverImageId = store.getCover();
        if (null != coverImageId) {
            storeDetail.setCoverImage(storageRecorder.getResource(coverImageId).getDescription().getResourceUrl());
        }

        if (storeAttribute != null) {
            if (storeAttribute.getGeo() != null) {
                Location location = new Location();
                location.setLat(storeAttribute.getGeo().getY());
                location.setLng(storeAttribute.getGeo().getX());
                storeDetail.setLocation(location);
            }

            storeDetail.setType(storeAttribute.getType());
            storeDetail.setCpc(storeAttribute.getCpc());
            storeDetail.setBusinessHours(storeAttribute.getBusinessHours());
        }

        List<Event> events = eventRepository.findAllByStore_Id(storeId);
        storeDetail.setEvents(events.stream().map(event -> {
            StoreDetail.Event basic = new StoreDetail.Event();
            basic.setId(event.getId());
            basic.setName(event.getName());
            return basic;
        }).collect(toList()));

        return storeDetail;
    }

    @Nonnull
    public StoreDetail fillInState(Long userId, StoreDetail storeDetail) {
        storeDetail.setMine(isMine(userId, storeDetail));
        storeDetail.setDeletable(isDeletable(userId, storeDetail));
        storeDetail.setEditable(isEditable(userId, storeDetail));
        storeDetail.setManageable(isManageable(userId, storeDetail));
        storeDetail.setConsumable(isConsumable(userId, storeDetail));
        return storeDetail;
    }

    @StoreBasicCacheRefresh
    public StoreDetail refreshBasicCache(long storeId) {
        return findDetail(storeId);
    }

    @Nonnull
    @StoresOwnedCache
    public List<Long> findOwnedStores(long userId) {
        List<Store> stores = storeRepository.findByOwner_IdAndStateNotIn(userId, newHashSet(DELETED));
        return stores.stream().map(Store::getId).collect(toList());
    }

    @StoresOwnedCacheRefresh
    public List<Long> refreshOwnedStoresCache(long userId) {
        List<Store> stores = storeRepository.findByOwner_IdAndStateNotIn(userId, newHashSet(DELETED));
        return stores.stream().map(Store::getId).collect(toList());
    }

    public StoreEmployeeDetail getStoreEmployeeDetail(StoreEmployee employee) {
        StoreEmployeeDetail storeEmployeeDetail = new StoreEmployeeDetail().from(userCell.findDetail(employee.getUser().getId()));
        storeEmployeeDetail.setRoles(employee.getRoles());
        storeEmployeeDetail.setNote(employee.getNote());
        return storeEmployeeDetail;
    }

    public Long getSpecialProductId(Long storeId, ProductTags productTags) {
        List<Product> products = productRepository.findByStore_IdAndStateNotIn(storeId, newHashSet(DELETED));
        if (products != null) {
            Optional<Long> productId = products.stream()
                    .sorted(Comparator.comparing(Product::getCreationTime).reversed())
                    .filter(each -> {
                        EnumSet<ProductTags> tagSet = each.getTags();
                        return tagSet != null && tagSet.contains(productTags);
                    })
                    .map(Product::getId)
                    .findFirst();
            return productId.isPresent() ? productId.get() : null;
        }
        return null;
    }

    public Store findExist(Long storeId) {
        return storeRepository.findByIdAndStateNotIn(storeId, newHashSet(DELETED)).orElseThrow(() -> new NotFoundException(STORE));
    }

    private boolean isMine(long userId, @Nonnull StoreDetail storeDetail) {
        return storeDetail.getOwner().getId().equals(userId);
    }

    private boolean isManageable(long userId, @Nonnull StoreDetail storeDetail) {
        return isMine(userId, storeDetail)
                || userCell.findDetail(userId).isAdmin()
                || (null != storeDetail.getAgent() && storeDetail.getAgent().getOwner().getId().equals(userId));
    }

    private boolean isConsumable(long userId, @Nonnull StoreDetail storeDetail) {
        if (isManageable(userId, storeDetail)) {
            return true;
        }

        StoreEmployee employee = storeEmployeeRepository.findByStoreIdAndUserId(storeDetail.getId(), userId).orElse(null);
        return employee != null && employee.getRoles().stream().filter(employeeRole -> employeeRole.equals(CASHIER)).findFirst().isPresent();
    }

    private boolean isDeletable(long userId, @Nonnull StoreDetail storeDetail) {
        return !storeDetail.isDeleted() && isManageable(userId, storeDetail);
    }

    private boolean isEditable(long userId, @Nonnull StoreDetail storeDetail) {
        return !storeDetail.isDeleted() && isManageable(userId, storeDetail);
    }
}
