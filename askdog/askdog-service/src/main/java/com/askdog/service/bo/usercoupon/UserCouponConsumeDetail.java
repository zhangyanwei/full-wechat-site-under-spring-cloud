package com.askdog.service.bo.usercoupon;


import com.askdog.model.entity.Coupon;
import com.askdog.model.entity.UserCoupon;
import com.askdog.model.entity.inner.usercoupon.CouponState;
import com.askdog.model.entity.inner.coupon.CouponType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class UserCouponConsumeDetail implements Serializable{

    private static final long serialVersionUID = 7799442348832996346L;
    @JsonFormat(shape = STRING)
    private long id;
    private String name;
    private Date receiveTime;
    private Date usageTime;
    private String rule;
    private CouponState couponState;
    private CouponType type;
    private UserBasic userBasic;

    public UserCouponConsumeDetail from(UserCoupon userCoupon){
        Coupon coupon = userCoupon.getCoupon();
        this.id = userCoupon.getId();
        this.name = coupon.getName();
        this.receiveTime = userCoupon.getCreationTime();
        this.rule = coupon.getRule();
        this.couponState = userCoupon.getState();
        this.usageTime = userCoupon.getUseTime();
        this.type = coupon.getType();
        this.userBasic = new UserBasic().from(userCoupon.getUser());
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

    public Date getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(Date usageTime) {
        this.usageTime = usageTime;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public UserBasic getUserBasic() {
        return userBasic;
    }

    public void setUserBasic(UserBasic userBasic) {
        this.userBasic = userBasic;
    }

}
