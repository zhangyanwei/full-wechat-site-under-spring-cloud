package com.askdog.service.impl.cell;

import com.askdog.dao.repository.UserCouponRepository;
import com.askdog.model.entity.UserCoupon;
import com.askdog.service.bo.usercoupon.UserCouponBasic;
import com.askdog.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

import static com.askdog.model.entity.inner.usercoupon.CouponState.NOT_USED;
import static com.askdog.service.exception.NotFoundException.Error.USER_COUPON;

@Component
public class UserCouponCell {

    @Autowired
    private UserCouponRepository userCouponRepository;

    public UserCouponBasic getBasic(Long userCouponId) {
        UserCoupon userCoupon = findExist(userCouponId);
        UserCouponBasic userCouponBasic = new UserCouponBasic();
        userCouponBasic.setId(userCouponId);
        userCouponBasic.setCreationTime(userCoupon.getCreationTime());
        userCouponBasic.setUseTime(userCoupon.getUseTime());
        userCouponBasic.setCouponId(userCoupon.getCoupon().getId());
        userCouponBasic.setState(userCoupon.getState());
        userCouponBasic.setUserId(userCoupon.getUser().getId());
        userCouponBasic.setExpireTime(userCoupon.getExpireTime());

        if (userCoupon.getVerificationUser() != null) {
            userCouponBasic.setVerificationUserId(userCoupon.getVerificationUser().getId());
        }

        userCouponBasic.setOrderId(userCoupon.getOrderId());

        return userCouponBasic;
    }

    public Optional<UserCoupon> findNotUsedUserCoupon(Long userId, Long couponId) {
        return userCouponRepository.findByUser_IdAndCoupon_IdAndStateAndExpireTimeAfter(userId, couponId, NOT_USED, new Date());
    }

    public UserCoupon findExist(Long userCouponId) {
        return userCouponRepository.findById(userCouponId).orElseThrow(() -> new NotFoundException(USER_COUPON));
    }

}
