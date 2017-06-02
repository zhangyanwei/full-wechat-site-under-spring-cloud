package com.askdog.service.bo.usercoupon;

import com.askdog.model.entity.inner.usercoupon.CouponState;

import java.io.Serializable;
import java.util.Date;

public class UserCouponBasic implements Serializable {

    private static final long serialVersionUID = -1019336219326816937L;

    private Long id;
    private Long userId;
    private Long couponId;
    private CouponState state;
    private Date creationTime;
    private Date useTime;
    private Date expireTime;
    private Long verificationUserId;
    private String orderId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public CouponState getState() {
        return state;
    }

    public void setState(CouponState state) {
        this.state = state;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Long getVerificationUserId() {
        return verificationUserId;
    }

    public void setVerificationUserId(Long verificationUserId) {
        this.verificationUserId = verificationUserId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
