package com.askdog.service.bo.usercoupon;


import com.askdog.model.entity.inner.coupon.CouponType;
import com.askdog.model.entity.inner.usercoupon.CouponState;
import com.askdog.service.bo.coupon.CouponDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class UserCouponPageDetail {

    @JsonFormat(shape = STRING)
    private long id;
    private String name;
    private Date receiveTime;
    private String rule;
    private CouponState couponState;
    private CouponType type;
    private StoreBasic storeBasic;
    private Date expireTime;
    private Date useTime;
    private String couponDescription;
    private boolean requireOrderId;

    public UserCouponPageDetail from(UserCouponDetail userCouponDetail) {
        CouponDetail couponDetail = userCouponDetail.getCouponDetail();
        this.id = userCouponDetail.getId();
        this.name = couponDetail.getName();
        this.receiveTime = userCouponDetail.getCreationTime();
        this.rule = couponDetail.getCouponRule();
        this.couponState = userCouponDetail.getCouponState();
        this.type = couponDetail.getType();
        this.storeBasic = new StoreBasic().from(userCouponDetail.getStoreDetail());
        this.expireTime = userCouponDetail.getExpireTime();
        this.useTime = userCouponDetail.getUseTime();
        this.couponDescription = couponDetail.getCouponDescription();
        this.requireOrderId = userCouponDetail.isRequireOrderId();
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public CouponState getCouponState() {
        return couponState;
    }

    public void setCouponState(CouponState couponState) {
        this.couponState = couponState;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public StoreBasic getStoreBasic() {
        return storeBasic;
    }

    public void setStoreBasic(StoreBasic storeBasic) {
        this.storeBasic = storeBasic;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }

    public boolean isRequireOrderId() {
        return requireOrderId;
    }

    public void setRequireOrderId(boolean requireOrderId) {
        this.requireOrderId = requireOrderId;
    }
}
