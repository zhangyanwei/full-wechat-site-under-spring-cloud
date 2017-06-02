package com.askdog.service.bo.coupon;

import com.askdog.model.entity.inner.coupon.CouponType;
import com.askdog.service.bo.StoreDetail;
import com.askdog.service.bo.UserDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class CouponDetail {

    @JsonFormat(shape = STRING)
    private Long id;
    private Date creationTime;
    private Date lastUpdateTime;
    private String name;
    private String couponRule;
    private CouponType type;
    private UserDetail updateUser;
    private StoreDetail store;
    private Long consumeExpires;
    private Date expireTimeFrom;
    private Date expireTimeTo;
    private String couponDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCouponRule() {
        return couponRule;
    }

    public void setCouponRule(String couponRule) {
        this.couponRule = couponRule;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public UserDetail getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(UserDetail updateUser) {
        this.updateUser = updateUser;
    }

    public StoreDetail getStore() {
        return store;
    }

    public void setStore(StoreDetail store) {
        this.store = store;
    }

    public Long getConsumeExpires() {
        return consumeExpires;
    }

    public void setConsumeExpires(Long consumeExpires) {
        this.consumeExpires = consumeExpires;
    }

    public Date getExpireTimeFrom() {
        return expireTimeFrom;
    }

    public void setExpireTimeFrom(Date expireTimeFrom) {
        this.expireTimeFrom = expireTimeFrom;
    }

    public Date getExpireTimeTo() {
        return expireTimeTo;
    }

    public void setExpireTimeTo(Date expireTimeTo) {
        this.expireTimeTo = expireTimeTo;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }
}
