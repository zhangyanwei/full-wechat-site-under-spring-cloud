package com.askdog.model.entity;

import com.askdog.model.entity.inner.coupon.CouponType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "mc_coupon")
public class Coupon extends Base {
    private static final long serialVersionUID = -435752115977668793L;

    @NotNull
    @Column(name = "name")
    @Size(max = 20)
    private String name;

    @NotNull
    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @NotNull
    @Column(name = "coupon_rule")
    private String rule;

    @NotNull
    @Enumerated(STRING)
    @Column(name = "coupon_type")
    private CouponType type;

    @Column(name = "creation_time")
    private Date creationTime;

    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "last_update_user")
    private User lastUpdateUser;

    @ManyToMany(mappedBy = "coupons", targetEntity = Product.class, fetch = LAZY)
    private List<Product> products;

    @Column(name = "consume_expires_period")
    private Long consumeExpiresPeriod;

    @NotNull
    @Column(name = "expire_time_from")
    private Date expireTimeFrom;

    @NotNull
    @Column(name = "expire_time_To")
    private Date expireTimeTo;

    @Column(name = "coupon_description")
    private String couponDescription;

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

    public User getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(User lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getConsumeExpiresPeriod() {
        return consumeExpiresPeriod;
    }

    public void setConsumeExpiresPeriod(Long consumeExpiresPeriod) {
        this.consumeExpiresPeriod = consumeExpiresPeriod;
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
