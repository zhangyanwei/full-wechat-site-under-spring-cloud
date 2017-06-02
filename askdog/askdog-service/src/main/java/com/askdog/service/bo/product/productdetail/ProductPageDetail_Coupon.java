package com.askdog.service.bo.product.productdetail;


import com.askdog.common.Out;
import com.askdog.model.entity.inner.coupon.CouponType;
import com.askdog.service.bo.coupon.CouponDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class ProductPageDetail_Coupon implements Out<ProductPageDetail_Coupon, CouponDetail> {

    @JsonFormat(shape = STRING)
    private Long id;
    private String name;
    private String rule;
    private CouponType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public ProductPageDetail_Coupon from(CouponDetail couponDetail) {
        this.id = couponDetail.getId();
        this.name = couponDetail.getName();
        this.rule = couponDetail.getCouponRule();
        this.type = couponDetail.getType();
        return this;
    }

}
