package com.askdog.service.bo.product;

import com.askdog.model.common.State;
import com.askdog.model.data.inner.VoteDirection;
import com.askdog.model.data.video.Video;
import com.askdog.model.entity.Product;
import com.askdog.service.bo.StoreDetail;
import com.askdog.service.bo.UserDetail;
import com.askdog.service.bo.coupon.CouponDetail;

import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import static com.askdog.model.data.inner.VoteDirection.NONE;

public class ProductDetail implements Serializable {

    private static final long serialVersionUID = 637235769930597877L;

    private Long id;
    private String name;
    private String description;
    private Video video;
    private StoreDetail store;
    private List<CouponDetail> coupons;
    private State state;
    private EnumSet<Product.ProductTags> tags;
    private List<ProductPictureDetail> pictures;
    private String coverUrl;
    private Date creationTime;
    private UserDetail creationUser;
    private boolean mine;
    private boolean editable;
    private boolean deletable;
    private boolean deleted;
    private VoteDirection vote = NONE;

    private ProductDetailStatistics statistics;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public StoreDetail getStore() {
        return store;
    }

    public void setStore(StoreDetail store) {
        this.store = store;
    }

    public List<CouponDetail> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponDetail> coupons) {
        this.coupons = coupons;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<ProductPictureDetail> getPictures() {
        return pictures;
    }

    public void setPictures(List<ProductPictureDetail> pictures) {
        this.pictures = pictures;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public EnumSet<Product.ProductTags> getTags() {
        return tags;
    }

    public void setTags(EnumSet<Product.ProductTags> tags) {
        this.tags = tags;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public UserDetail getCreationUser() {
        return creationUser;
    }

    public void setCreationUser(UserDetail creationUser) {
        this.creationUser = creationUser;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public VoteDirection getVote() {
        return vote;
    }

    public void setVote(VoteDirection vote) {
        this.vote = vote;
    }

    public ProductDetailStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(ProductDetailStatistics statistics) {
        this.statistics = statistics;
    }
}
