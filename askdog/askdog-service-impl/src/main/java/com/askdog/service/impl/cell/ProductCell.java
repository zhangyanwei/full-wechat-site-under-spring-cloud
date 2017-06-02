package com.askdog.service.impl.cell;

import com.askdog.dao.repository.ProductRepository;
import com.askdog.dao.repository.UserRepository;
import com.askdog.dao.repository.mongo.ProductAttributeRepository;
import com.askdog.dao.repository.mongo.ProductStatisticsRepository;
import com.askdog.dao.repository.mongo.VoteRepository;
import com.askdog.model.data.ProductAttribute;
import com.askdog.model.data.ProductStatistics;
import com.askdog.model.entity.Coupon;
import com.askdog.model.entity.Product;
import com.askdog.model.entity.User;
import com.askdog.model.security.Authority;
import com.askdog.service.bo.product.ProductDetail;
import com.askdog.service.bo.product.ProductDetailBasic;
import com.askdog.service.bo.product.ProductDetailStatistics;
import com.askdog.service.bo.product.ProductPicture;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.impl.cache.annotation.product.ProductBasicCache;
import com.askdog.service.impl.cache.annotation.product.ProductBasicCacheRefresh;
import com.askdog.service.impl.cache.annotation.product.ProductStatisticCache;
import com.askdog.service.impl.cache.annotation.product.ProductStatisticCacheRefresh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static com.askdog.common.utils.Conditions.checkState;
import static com.askdog.model.common.State.DELETED;
import static com.askdog.service.exception.NotFoundException.Error.PRODUCT;
import static com.askdog.service.exception.NotFoundException.Error.USER;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Component
public class ProductCell {

    @Autowired private VoteRepository voteRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private ProductStatisticsRepository productStatisticsRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductAttributeRepository productAttributeRepository;

    @ProductBasicCache
    public ProductDetailBasic getBasic(Long productId) {
        Product product = findExist(productId);
        ProductDetailBasic productDetailBasic = new ProductDetailBasic();
        productDetailBasic.setId(product.getId());
        productDetailBasic.setName(product.getName());
        productDetailBasic.setDescription(product.getDescription());
        productDetailBasic.setVideoId(product.getVideoId());
        productDetailBasic.setStoreId(product.getStore().getId());

        if (product.getCoupons() != null) {
            productDetailBasic.setCoupons(product.getCoupons().stream().map(Coupon::getId).collect(toSet()));
        }

        productDetailBasic.setState(product.getState());
        ProductAttribute productAttribute = productAttributeRepository.findByProductId(productId);

        if (productAttribute != null) {
            productDetailBasic.setPictures(productAttribute.getPictures().stream().map(trans()).collect(toList()));
        }

        productDetailBasic.setTags(product.getTags());
        productDetailBasic.setCoverId(product.getCoverId());
        productDetailBasic.setCreationTime(product.getCreationTime());
        productDetailBasic.setCreationUserId(product.getCreationUser().getId());
        productDetailBasic.setDeleted(product.getState() == DELETED);
        return productDetailBasic;
    }

    @ProductBasicCacheRefresh
    public ProductDetailBasic refreshBasicCache(Long productId) {
        return getBasic(productId);
    }

    @ProductStatisticCache
    public ProductDetailStatistics getStatistic(Long productId) {
        ProductStatistics statistics = productStatisticsRepository.findByProductId(productId).orElseGet(() -> {
            ProductStatistics productStatistics = new ProductStatistics();
            productStatistics.setProductId(productId);
            return productStatistics;
        });
        return new ProductDetailStatistics().from(statistics);
    }

    @ProductStatisticCacheRefresh
    public ProductDetailStatistics refreshStatisticCache(Long productId) {
        return getStatistic(productId);
    }

    public Product findExist(Long productId) {
        Product product = productRepository.findOne(productId);
        checkState(product != null, () -> new NotFoundException(PRODUCT));
        return product;
    }

    public ProductDetail fillInState(Long userId, ProductDetail productDetail) {
        if (userId == null) {
            return productDetail;
        }

        productDetail.setMine(mine(userId, productDetail));
        productDetail.setEditable(deletable(userId, productDetail));
        productDetail.setDeletable(deletable(userId, productDetail));

        voteRepository.findByUserAndTargetId(userId, productDetail.getId()).ifPresent(
                voteAction -> productDetail.setVote(voteAction.getDirection())
        );

        return productDetail;
    }

    private boolean isAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER));
        return user.getAuthorities() != null && user.getAuthorities().contains(Authority.Role.ADMIN);
    }

    private boolean mine(Long userId, ProductDetail productDetail) {
        return userId.equals(productDetail.getCreationUser().getId()) || userId.equals(productDetail.getStore().getOwner().getId());
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean deletable(Long userId, ProductDetail productDetail) {
        if (productDetail.isDeleted()) {
            return false;
        }

        boolean deletable = userId.equals(productDetail.getCreationUser().getId());
        deletable = deletable || userId.equals(productDetail.getStore().getOwner().getId());
        deletable = deletable || (productDetail.getStore().getAgent() != null
                && userId.equals(productDetail.getStore().getAgent().getOwner().getId()));
        deletable = deletable || isAdmin(userId);

        return deletable;
    }

    private Function<ProductAttribute.ProductPicture, ProductPicture> trans() {
        return productPicture -> {
            ProductPicture picture = new ProductPicture();
            picture.setName(productPicture.getName());
            picture.setLinkId(productPicture.getLinkId());
            return picture;
        };
    }
}
