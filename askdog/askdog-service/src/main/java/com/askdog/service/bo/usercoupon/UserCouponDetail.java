package com.askdog.service.bo.usercoupon;

import com.askdog.model.entity.inner.usercoupon.CouponState;
import com.askdog.service.bo.StoreDetail;
import com.askdog.service.bo.UserDetail;
import com.askdog.service.bo.coupon.CouponDetail;
import com.askdog.service.bo.product.ProductDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class UserCouponDetail {

    @JsonFormat(shape = STRING)
    private long id;
    private Date creationTime;
    private CouponState couponState;
    private Date useTime;
    private CouponDetail couponDetail;
    private UserDetail userDetail;
    private StoreDetail storeDetail;
    private Date expireTime;
    private UserDetail verificationUser;
    private String orderId;
    private boolean requireOrderId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public CouponState getCouponState() {
        return couponState;
    }

    public void setCouponState(CouponState couponState) {
        this.couponState = couponState;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public CouponDetail getCouponDetail() {
        return couponDetail;
    }

    public void setCouponDetail(CouponDetail couponDetail) {
        this.couponDetail = couponDetail;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public StoreDetail getStoreDetail() {
        return storeDetail;
    }

    public void setStoreDetail(StoreDetail storeDetail) {
        this.storeDetail = storeDetail;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public UserDetail getVerificationUser() {
        return verificationUser;
    }

    public void setVerificationUser(UserDetail verificationUser) {
        this.verificationUser = verificationUser;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isRequireOrderId() {
        return requireOrderId;
    }

    public void setRequireOrderId(boolean requireOrderId) {
        this.requireOrderId = requireOrderId;
    }
}
