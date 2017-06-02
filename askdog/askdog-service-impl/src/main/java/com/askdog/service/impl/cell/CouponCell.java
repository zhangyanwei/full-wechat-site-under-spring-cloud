package com.askdog.service.impl.cell;

import com.askdog.dao.repository.CouponRepository;
import com.askdog.model.entity.Coupon;
import com.askdog.service.bo.coupon.CouponDetailBasic;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.impl.cache.annotation.coupon.CouponBasicCache;
import com.askdog.service.impl.cache.annotation.coupon.CouponBasicCacheRefresh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.askdog.service.exception.NotFoundException.Error.COUPON;

@Component
public class CouponCell {

    @Autowired private CouponRepository couponRepository;

    @CouponBasicCache
    public CouponDetailBasic getBasic(Long couponId) {
        Coupon coupon = findExist(couponId);
        Long lastUpdateUserId = coupon.getLastUpdateUser() == null ? null : coupon.getLastUpdateUser().getId();
        CouponDetailBasic couponDetailBasic = new CouponDetailBasic();
        couponDetailBasic.setId(coupon.getId());
        couponDetailBasic.setCreationTime(coupon.getCreationTime());
        couponDetailBasic.setLastUpdateTime(coupon.getLastUpdateTime());
        couponDetailBasic.setName(coupon.getName());
        couponDetailBasic.setCouponRule(coupon.getRule());
        couponDetailBasic.setType(coupon.getType());
        couponDetailBasic.setUpdateUserId(lastUpdateUserId);
        couponDetailBasic.setStoreId(coupon.getStore().getId());
        couponDetailBasic.setConsumeExpires(coupon.getConsumeExpiresPeriod());
        couponDetailBasic.setExpireTimeFrom(coupon.getExpireTimeFrom());
        couponDetailBasic.setExpireTimeTo(coupon.getExpireTimeTo());
        couponDetailBasic.setCouponDescription(coupon.getCouponDescription());
        return couponDetailBasic;
    }

    @CouponBasicCacheRefresh
    public CouponDetailBasic refreshCouponDetailBasic(Long couponId) {
        return getBasic(couponId);
    }

    public Coupon findExist(Long couponId) {
        return couponRepository.findById(couponId).orElseThrow(() -> new NotFoundException(COUPON));
    }

}
