package com.askdog.model.entity;

import com.askdog.model.entity.inner.usercoupon.CouponState;
import com.askdog.model.entity.partial.CouponTimeAndStateStatistics;
import com.askdog.model.entity.partial.CouponTimeBasedStatistics;
import com.askdog.model.entity.partial.TimeBasedStatistics;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "mc_user_coupon")
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "CouponCreationInWeek",
                classes = {
                        @ConstructorResult(
                                targetClass = CouponTimeAndStateStatistics.class,
                                columns = {
                                        @ColumnResult(name = "day", type = Date.class),
                                        @ColumnResult(name = "coupon_count", type = Long.class),
                                        @ColumnResult(name = "usage_state", type = String.class)
                                })
                }
        )
})
public class UserCoupon extends Base {

    private static final long serialVersionUID = -9103770339156446578L;

    @NotNull
    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @NotNull
    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "coupon_id", nullable = false, updatable = false)
    private Coupon coupon;

    @NotNull
    @Enumerated(STRING)
    @Column(name = "state")
    private CouponState state;

    @NotNull
    @Column(name = "creation_time")
    private Date creationTime;

    @NotNull
    @Column(name = "expire_time")
    private Date expireTime;

    @Column(name = "use_time")
    private Date useTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "verification_user_id")
    private User verificationUser;

    @Column(name = "order_id")
    private String orderId;

    @NotNull
    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private Store store;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
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

    public User getVerificationUser() {
        return verificationUser;
    }

    public void setVerificationUser(User verificationUser) {
        this.verificationUser = verificationUser;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
