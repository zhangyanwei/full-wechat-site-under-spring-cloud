package com.askdog.service.impl;

import com.askdog.dao.repository.*;
import com.askdog.dao.repository.mongo.AddressCodeRepository;
import com.askdog.dao.repository.mongo.StoreAttributeRepository;
import com.askdog.dao.repository.mongo.StoreSettingRepository;
import com.askdog.model.data.AddressCode;
import com.askdog.model.data.StoreAttribute;
import com.askdog.model.data.StoreSetting;
import com.askdog.model.entity.Agent;
import com.askdog.model.entity.Store;
import com.askdog.model.entity.StoreEmployee;
import com.askdog.model.entity.User;
import com.askdog.model.entity.partial.TimeBasedStatistics;
import com.askdog.service.AgentService;
import com.askdog.service.ProductService;
import com.askdog.service.StoreService;
import com.askdog.service.UserService;
import com.askdog.service.bo.BasicUser;
import com.askdog.service.bo.TokenDetail;
import com.askdog.service.bo.addresscode.AddressCodePageDetail;
import com.askdog.service.bo.addresscode.PureAddressCode;
import com.askdog.service.bo.admin.dashboard.StoreStatistics;
import com.askdog.service.bo.common.ListedWrapper;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.store.AmendedStore;
import com.askdog.service.bo.store.PureStore;
import com.askdog.service.bo.store.PureStore.Location;
import com.askdog.service.bo.store.StoreDetail;
import com.askdog.service.bo.store.StoreHome;
import com.askdog.service.bo.user.EmployeeUpdate;
import com.askdog.service.bo.user.StoreEmployeeDetail;
import com.askdog.service.exception.ConflictException;
import com.askdog.service.exception.ForbiddenException;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.impl.cache.annotation.store.CityListCache;
import com.askdog.service.impl.cell.StoreCell;
import com.askdog.service.impl.cell.UserCell;
import com.askdog.service.impl.event.TriggerEvent;
import com.askdog.service.impl.location.tencent.LocationResponseBody;
import com.askdog.service.impl.location.tencent.PlaceLocationDescription;
import com.askdog.service.impl.storage.StorageRecorder;
import com.askdog.service.location.LocationAgent;
import com.askdog.service.location.Provider;
import com.askdog.service.location.ResponseBody;
import com.askdog.web.common.async.AsyncCaller;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoPage;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.askdog.common.utils.Conditions.checkState;
import static com.askdog.common.utils.Json.readValue;
import static com.askdog.model.common.EventType.BIND_STORE;
import static com.askdog.model.common.State.DELETED;
import static com.askdog.model.common.State.OK;
import static com.askdog.model.data.inner.location.LocationProvider.TENCENT_MAP;
import static com.askdog.model.entity.StoreEmployee.EmployeeRole.CASHIER;
import static com.askdog.model.entity.builder.StoreBuilder.storeBuilder;
import static com.askdog.model.security.Authority.Role.STORE_ADMIN;
import static com.askdog.service.bo.common.ListedWrapperUtils.reListedPage;
import static com.askdog.service.bo.common.PagedDataUtils.rePage;
import static com.askdog.service.exception.ForbiddenException.Error.*;
import static com.askdog.service.exception.NotFoundException.Error.EMPLOYEE;
import static com.askdog.service.exception.NotFoundException.Error.STORE;
import static com.askdog.service.utils.StatisticsUtils.overlying;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.String.valueOf;
import static java.util.EnumSet.of;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.springframework.util.StringUtils.isEmpty;


@RestController
public class StoreServiceImpl implements StoreService {

    private final static String EMPLOYEE_BIND_TOKEN = "employee.bind";

    @Provider(TENCENT_MAP)
    @Autowired private LocationAgent locationAgent;
    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private UserCell userCell;
    @Autowired private StoreCell storeCell;
    @Autowired private StoreRepository storeRepository;
    @Autowired private StoreAttributeRepository storeAttributeRepository;
    @Autowired private StoreSettingRepository storeSettingRepository;
    @Autowired private StoreEmployeeRepository storeEmployeeRepository;
    @Autowired private AgentService agentService;
    @Autowired private AgentRepository agentRepository;
    @Autowired private ProductService productService;
    @Autowired private AsyncCaller asyncCaller;
    @Autowired private AddressCodeRepository addressCodeRepository;
    @Autowired private TokenRepository tokenRepository;
    @Autowired private StorageRecorder storageRecorder;

    @Nonnull
    @Override
    public com.askdog.service.bo.StoreDetail findDetail(@PathVariable("storeId") Long storeId,
                                                        @RequestParam(name = "enableDeleted", defaultValue = "false") boolean enableDeleted) {
        com.askdog.service.bo.StoreDetail storeDetail = storeCell.findDetail(storeId);
        checkState(enableDeleted || !storeDetail.isDeleted(), () -> new NotFoundException(STORE));
        storeDetail.setOwner(userService.findDetail(storeDetail.getOwner().getId()));
        if (null != storeDetail.getAgent()) {
            storeDetail.setAgent(agentService.findDetail(storeDetail.getAgent().getId(), enableDeleted));
        }
        return storeDetail;
    }

    @Nonnull
    @Override
    public com.askdog.service.bo.StoreDetail findDetailWithState(@RequestParam(value = "userId", required = false) Long userId,
                                                                 @PathVariable("storeId") long storeId,
                                                                 @RequestParam(name = "enableDeleted", defaultValue = "false") boolean enableDeleted) {
        com.askdog.service.bo.StoreDetail storeDetail = findDetail(storeId, enableDeleted);
        if (userId != null) {
            storeCell.fillInState(userId, storeDetail);
        }
        return storeDetail;
    }

    @Nonnull
    @Override
    public StoreDetail findPageDetail(@RequestParam(value = "userId", required = false) Long userId,
                                      @PathVariable("storeId") long storeId) {
        //Long productId = storeCell.getSpecialProductId(storeId, Product.ProductTags.SPECIAL);
        com.askdog.service.bo.StoreDetail storeDetailCell = findDetail(storeId, false);
        Long productId = storeDetailCell.getSpecialProductId();
        StoreDetail storeDetail = new StoreDetail().from(storeDetailCell);

        if (productId != null) {
            storeDetail.setSpecialProduct(productService.getPageDetail(userId, productId));
        }
        return storeDetail;
    }

    @Nonnull
    @Override
    public StoreDetail findPageDetailWithState(@RequestParam("userId") long userId, @PathVariable("storeId") long storeId) {
        return new StoreDetail().from(findDetailWithState(userId, storeId, false));
    }

    @Nonnull
    @Override
    public PagedData<StoreHome> getStores(@RequestParam("ip") @Nonnull String ip,
                                          @RequestParam(name = "lat", required = false) Double lat,
                                          @RequestParam(name = "lng", required = false) Double lng,
                                          @RequestParam(name = "ad_code", required = false) String adCode,
                                          @RequestBody Pageable pageable) {

        if (isEmpty(adCode) && lat != null && lng != null) {
            ResponseBody responseBody = locationAgent.analysisAddress(lat, lng);
            if (responseBody.isSuccess()) {
                PlaceLocationDescription locationDescription = readValue(responseBody.getData(), PlaceLocationDescription.class);
                if (locationDescription != null) {
                    adCode = locationDescription.getAddressInfo().getAddressCode().substring(0, 4);
                }
            }
        }

        Page<StoreAttribute> storeAttributes;

        if (isNotEmpty(adCode) && lat != null && lng != null) {
            Point point = new Point(lng, lat);
            GeoPage<StoreAttribute> geoResults = storeRepository.findStore(truncAdCode(adCode), point, pageable);

            return rePage(geoResults, pageable, geoResult -> {
                StoreHome storeHome = new StoreHome();
                storeHome.from(findPageDetail(null, geoResult.getContent().getStoreId()));
                storeHome.setDistance(geoResult.getDistance().getValue());
                return storeHome;
            });

        } else if (isNotEmpty(adCode) && !"00".equals(adCode)) {
            storeAttributes = storeAttributeRepository.findByAdCodeStartingWithAndStateNotIn(truncAdCode(adCode), newHashSet(DELETED), pageable);

        } else {
            storeAttributes = storeAttributeRepository.findByStateNotIn(newHashSet(DELETED), pageable);
        }

        return rePage(storeAttributes, pageable, storeAttribute -> {
            StoreHome storeHome = new StoreHome();
            storeHome.from(findPageDetail(null, storeAttribute.getStoreId()));
            storeHome.setDistance(-1);
            return storeHome;
        });
    }

    @Nonnull
    @Override
    public PagedData<StoreDetail> getStoreByRole(@RequestParam("userId") long userId,
                                                 @RequestBody Pageable pageable) {
        Agent agent = agentRepository.findByOwner_IdAndStateNotIn(userId, newHashSet(DELETED)).orElse(null);

        Page<Store> stores = userCell.findDetail(userId).isAdmin()
                ? storeRepository.findAllByStateNotIn(newHashSet(DELETED), pageable)
                : storeRepository.findByAgentAndStateNotIn(agent, newHashSet(DELETED), pageable);

        return rePage(stores, pageable, store -> new StoreDetail().from(storeCell.findDetail(store.getId())));
    }

    @Nonnull
    @Override
    public List<Long> findOwnedStores(@PathVariable("userId") long userId) {
        return storeCell.findOwnedStores(userId);
    }

    @Nonnull
    @Override
    public StoreDetail create(@PathVariable("userId") long userId,
                              @Nonnull @Valid @RequestBody PureStore pureStore) {
        User bindUser = userCell.findExists(pureStore.getUserId());
        Agent agent = agentRepository.findByOwner_IdAndStateNotIn(userId, newHashSet(DELETED)).orElse(null);

        if (pureStore.getCoverImageLinkId() != null) {
            storageRecorder.assertValid(pureStore.getCoverImageLinkId());
        }

        Store store = storeBuilder()
                .name(pureStore.getName())
                .description(pureStore.getDescription())
                .address(pureStore.getAddress())
                .phone(pureStore.getPhone())
                .cover(pureStore.getCoverImageLinkId())
                .owner(bindUser)
                .agent(agent)
                .contactsName(pureStore.getPureContactsUser() == null ? null : pureStore.getPureContactsUser().getName())
                .contactsPhone(pureStore.getPureContactsUser() == null ? null : pureStore.getPureContactsUser().getPhone())
                .build();

        StoreAttribute storeAttribute = new StoreAttribute();
        storeAttribute.setState(OK);
        storeAttribute.setCreationTime(store.getCreationTime());
        storeAttribute.setStoreId(store.getId());

        Location location = pureStore.getLocation();
        GeoJsonPoint geo = new GeoJsonPoint(new Point(location.getLng(), location.getLat()));
        storeAttribute.setGeo(geo);

        storeAttribute.setType(pureStore.getType());
        storeAttribute.setCpc(pureStore.getCpc());
        storeAttribute.setBusinessHours(pureStore.getBusinessHours());
        //storeAttribute.setTelephone(pureStore.getPureContactsUser().getTelephone());

        storeAttributeRepository.save(storeAttribute);

        Store savedStore = storeRepository.save(store);
        storeCell.refreshOwnedStoresCache(bindUser.getId());

        asyncCaller.asyncCall(() -> parseAdCode(savedStore.getId(), location));

        return findPageDetail(userId, savedStore.getId());
    }

    @Nonnull
    @Override
    public StoreDetail update(@PathVariable("userId") long userId,
                              @PathVariable("storeId") long storeId,
                              @Nonnull @Valid @RequestBody AmendedStore amendedStore) {
        com.askdog.service.bo.StoreDetail storeDetail = findDetailWithState(userId, storeId, false);
        checkState(storeDetail.isEditable(), () -> new ForbiddenException(ForbiddenException.Error.UPDATE_STORE));

        Store store = storeCell.findExist(storeId);

        if (amendedStore.getCoverImageLinkId() != null) {
            storageRecorder.assertValid(amendedStore.getCoverImageLinkId());
            store.setCover(amendedStore.getCoverImageLinkId());
        }

        if (!Strings.isNullOrEmpty(amendedStore.getName())) {
            store.setName(amendedStore.getName());
        }

        if (amendedStore.getDescription() != null) {
            store.setDescription(amendedStore.getDescription());
        }

        if (!Strings.isNullOrEmpty(amendedStore.getAddress())) {
            store.setAddress(amendedStore.getAddress());
        }

        if (!Strings.isNullOrEmpty(amendedStore.getPhone())) {
            store.setPhone(amendedStore.getPhone());
        }

        if (amendedStore.getPureContactsUser() != null) {
            store.setContactsName(amendedStore.getPureContactsUser().getName());
            store.setContactsPhone(amendedStore.getPureContactsUser().getPhone());
        }

        StoreAttribute storeAttribute = storeAttributeRepository.findByStoreId(storeId);

        if (storeAttribute == null) {
            storeAttribute = new StoreAttribute();
            storeAttribute.setStoreId(storeId);
        }

        Location location = amendedStore.getLocation();
        if (location != null) {
            GeoJsonPoint geo = new GeoJsonPoint(new Point(location.getLng(), location.getLat()));
            storeAttribute.setGeo(geo);
        }

        if (amendedStore.getType() != null) {
            storeAttribute.setType(amendedStore.getType());
        }

        if (amendedStore.getCpc() != null) {
            storeAttribute.setCpc(amendedStore.getCpc());
        }

        if (amendedStore.getBusinessHours() != null) {
            storeAttribute.setBusinessHours(amendedStore.getBusinessHours());
        }

//        if (amendedStore.getPureContactsUser() != null && amendedStore.getPureContactsUser().getTelephone() != null) {
//            storeAttribute.setTelephone(amendedStore.getPureContactsUser().getTelephone());
//        }

        storeAttributeRepository.save(storeAttribute);

        if (location != null) {
            parseAdCode(storeId, location);
        }

        storeRepository.save(store);
        storeCell.refreshBasicCache(storeId);

        return findPageDetail(userId, storeId);
    }

    @Override
    @Transactional
    public void delete(@PathVariable("userId") long userId,
                       @PathVariable("storeId") long storeId) {
        com.askdog.service.bo.StoreDetail storeDetail = findDetailWithState(userId, storeId, false);
        checkState(storeDetail.isDeletable(), () -> new ForbiddenException(ForbiddenException.Error.DELETE_STORE));

        Store store = storeCell.findExist(storeId);
        store.setState(DELETED);
        storeRepository.save(store);

        StoreAttribute storeAttribute = storeAttributeRepository.findByStoreId(storeId);
        if (storeAttribute != null) {
            storeAttribute.setState(DELETED);
            storeAttributeRepository.save(storeAttribute);
        }

        storeCell.refreshBasicCache(storeId);
        storeCell.refreshOwnedStoresCache(storeDetail.getOwner().getId());

        //TODO potential bug while one user has multiple stores
        User user = userCell.findExists(store.getOwner().getId());
        if (null != user.getAuthorities()) {
            user.getAuthorities().remove(STORE_ADMIN);
        }
        userRepository.save(user);
    }

    @Override
    public StoreSetting getSetting(@PathVariable("storeId") long storeId) {
        return storeSettingRepository.findByStoreId(storeId).orElseGet(StoreSetting::new);
    }

    @Override
    public void updateSetting(@PathVariable("storeId") long storeId, @RequestBody StoreSetting setting) {
        storeSettingRepository.findByStoreId(storeId).ifPresent((saved) -> {
            setting.setId(saved.getId());
        });

        setting.setStoreId(storeId);
        storeSettingRepository.save(setting);
    }

    @Override
    public TokenDetail employeeBindToken(@Nonnull @RequestParam("userId") Long userId, @Nonnull @PathVariable("storeId") Long storeId) {

        com.askdog.service.bo.StoreDetail storeDetail = findDetailWithState(userId, storeId, false);
        checkState(storeDetail.isManageable(), () -> new ForbiddenException(ADD_EMPLOYEE));

        Map<String, String> tokenDetail = Maps.newHashMap();
        tokenDetail.put("storeId", valueOf(storeId));

        long timeout = 10;
        TimeUnit timeUnit = TimeUnit.MINUTES;
        String token = tokenRepository.claimToken(valueOf(userId), EMPLOYEE_BIND_TOKEN, tokenDetail, timeout, timeUnit);
        return new TokenDetail(token, timeUnit.toMillis(timeout));
    }

    @Override
    public PagedData<StoreEmployeeDetail> getEmployees(@Nonnull @RequestParam("userId") Long userId,
                                                       @Nonnull @PathVariable("storeId") Long storeId,
                                                       @RequestBody Pageable pageable) {
        com.askdog.service.bo.StoreDetail storeDetail = findDetailWithState(userId, storeId, false);
        checkState(storeDetail.isManageable(), () -> new ForbiddenException(LIST_EMPLOYEE));

        Page<StoreEmployee> employees = storeEmployeeRepository.findByStoreId(storeId, pageable);

        return rePage(employees, pageable, employee -> storeCell.getStoreEmployeeDetail(employee));
    }

    @Override
    @TriggerEvent({@TriggerEvent.TriggerEventItem(performer = "userId", eventType = BIND_STORE, target = "storeId")})
    public StoreEmployeeDetail addEmployee(@Nonnull @RequestParam("token") String token,
                                           @Nonnull @RequestParam("userId") Long userId,
                                           @Nonnull @PathVariable("storeId") Long storeId) {

        checkState(isNotEmpty(token) && tokenRepository.isTokenValidate(EMPLOYEE_BIND_TOKEN, token),
                () -> new ForbiddenException(INVALID_TOKEN));

        checkState(!storeEmployeeRepository.findByStoreIdAndUserId(storeId, userId).isPresent(),
                () -> new ConflictException(ConflictException.Error.ADD_EMPLOYEE));

        Map<String, String> tokenPayload = tokenRepository.redeemToken(EMPLOYEE_BIND_TOKEN, token);
        Long tokenStoreId = Long.valueOf(tokenPayload.get("storeId"));

        checkState(storeId.equals(tokenStoreId), () -> new ForbiddenException(INVALID_TOKEN));


        Store store = storeCell.findExist(storeId);
        User employee = userCell.findExists(userId);

        StoreEmployee storeEmployee = new StoreEmployee();
        storeEmployee.setStore(store);
        storeEmployee.setUser(employee);
        storeEmployee.setRoles(of(CASHIER));
        storeEmployee.setCreationTime(new Date());

        StoreEmployee savedEmployee = storeEmployeeRepository.save(storeEmployee);
        return storeCell.getStoreEmployeeDetail(savedEmployee);
    }

    @Override
    public StoreEmployeeDetail updateEmployee(@Nonnull @RequestParam("userId") Long userId,
                                              @Nonnull @PathVariable("storeId") Long storeId,
                                              @Nonnull @RequestParam("employeeId") Long employeeId,
                                              @RequestBody EmployeeUpdate employeeUpdate) {
        com.askdog.service.bo.StoreDetail storeDetail = findDetailWithState(userId, storeId, false);
        checkState(storeDetail.isManageable(), () -> new ForbiddenException(UPDATE_EMPLOYEE));

        StoreEmployee employee = storeEmployeeRepository.findByStoreIdAndUserId(storeId, employeeId)
                .orElseThrow(() -> new NotFoundException(EMPLOYEE));

        employee.setNote(employeeUpdate.getNote());

        StoreEmployee savedEmployee = storeEmployeeRepository.save(employee);
        return storeCell.getStoreEmployeeDetail(savedEmployee);
    }

    @Override
    @Transactional
    public void removeEmployee(@Nonnull @RequestParam("userId") Long userId,
                               @Nonnull @PathVariable("storeId") Long storeId,
                               @Nonnull @PathVariable("employeeId") Long employeeId) {
        com.askdog.service.bo.StoreDetail storeDetail = findDetailWithState(userId, storeId, false);
        checkState(storeDetail.isManageable(), () -> new ForbiddenException(REMOVE_EMPLOYEE));

        storeEmployeeRepository.deleteByStoreIdAndUserId(storeId, employeeId);
    }

    @Override
    public StoreStatistics storeStatistic() {
        StoreStatistics storeStatistics = new StoreStatistics();
        storeStatistics.setTotalStoreCount(storeRepository.countRegisteredStore());
        List<TimeBasedStatistics> storeStatistic = storeRepository.storeRegistrationStatistics("day", "1 years");
        if (storeStatistic.size() != 0) {
            Long beforeCount = storeRepository.getCountOutOfQuery(storeStatistic.get(0).getTime());
            Long firstCount = storeStatistic.get(0).getCount();
            storeStatistic.get(0).setCount(firstCount + beforeCount);
        }
        storeStatistics.setStoreRegistrationTrend(overlying(storeStatistic));
        return storeStatistics;
    }

    @Override
    public List<BasicUser> search(@PathVariable("key") String key) {
        List<User> users = userRepository.search(key);
        return users.stream().map(user -> userCell.findDetail(user.getId()).toBasic()).collect(toList());
    }

    @Override
    @CityListCache
    public ListedWrapper<AddressCodePageDetail> getCities() {
        List<AddressCode> addressCodes = addressCodeRepository.findAll();
        return reListedPage(addressCodes, each -> new AddressCodePageDetail().from(each));
    }

    @Override
    public void createCites(@Valid @RequestBody PureAddressCode pureAddressCode) {
        AddressCode addressCode = new AddressCode();
        addressCode.setName(pureAddressCode.getName());
        addressCode.setCode(pureAddressCode.getCode());
        addressCode.setParent(pureAddressCode.getParent());
        addressCodeRepository.save(addressCode);
    }

    private void parseAdCode(Long storeId, Location location) {
        asyncCaller.asyncCall(() -> {
            LocationResponseBody responseBody = locationAgent.analysisAddress(location.getLat(), location.getLng());
            PlaceLocationDescription description = readValue(responseBody.getData(), PlaceLocationDescription.class);
            if (description != null) {
                StoreAttribute storeAttribute = storeAttributeRepository.findByStoreId(storeId);
                storeAttribute.setAdCode(description.getAddressInfo().getAddressCode());
                storeAttributeRepository.save(storeAttribute);
            }
        });
    }

    private String truncAdCode(String adCode) {
        if ("00".equals(adCode)) {
            return adCode;
        }

        if (adCode.endsWith("0")) {
            return truncAdCode(adCode.substring(0, adCode.length() - 1));
        }

        return adCode;
    }

}
