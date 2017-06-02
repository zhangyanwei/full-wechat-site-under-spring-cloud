package com.askdog.model.entity;

import com.askdog.model.common.State;
import com.askdog.model.converter.ProductTagsSetConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import static com.askdog.model.common.State.OK;
import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "mc_product")
public class Product extends Base {

    private static final long serialVersionUID = 3647694472065078721L;

    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "name", length = 15)
    private String name;

    @Size(max = 50000)
    @Column(name = "description", length = 50000)
    private String description;

    @Column(name = "video_id")
    private Long videoId;

    @NotNull
    @Enumerated(STRING)
    @Column(name = "state")
    private State state = OK;

    @NotNull
    @Column(name = "creation_time")
    private Date creationTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private Store store;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "creation_user", nullable = false, updatable = false)
    private User creationUser;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, targetEntity = Coupon.class)
    @JoinTable(
            name = "mc_product_coupon",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "coupon_id", referencedColumnName = "id")}
    )
    @Size(max = 2)
    private List<Coupon> coupons;

    @Column(name = "cover_id")
    private Long coverId;

    @Convert(converter = ProductTagsSetConverter.class)
    @Column(name = "tags")
    private EnumSet<ProductTags> tags;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public User getCreationUser() {
        return creationUser;
    }

    public void setCreationUser(User creationUser) {
        this.creationUser = creationUser;
    }

    public Long getCoverId() {
        return coverId;
    }

    public void setCoverId(Long coverId) {
        this.coverId = coverId;
    }

    public EnumSet<ProductTags> getTags() {
        return tags;
    }

    public void setTags(EnumSet<ProductTags> tags) {
        this.tags = tags;
    }

    public enum ProductTags {
        SPECIAL
    }

}
