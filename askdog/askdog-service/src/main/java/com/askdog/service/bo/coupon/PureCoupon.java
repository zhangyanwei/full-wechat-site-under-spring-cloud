package com.askdog.service.bo.coupon;

import com.askdog.model.entity.inner.coupon.ConsumeExpires;
import com.askdog.model.entity.inner.coupon.CouponType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class PureCoupon {

    @NotNull
    @Size(max = 20)
    private String name;

    @NotNull
    private String rule;

    @NotNull
    private CouponType type;

    @NotNull
    private Long storeId;

    private ConsumeExpires consumeExpires;

    @NotNull
    private Date expireTimeFrom;

    @NotNull
    private Date expireTimeTo;

    private String couponDescription;

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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

    public ConsumeExpires getConsumeExpires() {
        return consumeExpires;
    }

    public void setConsumeExpires(ConsumeExpires consumeExpires) {
        this.consumeExpires = consumeExpires;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }

}
