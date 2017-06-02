package com.askdog.service.bo.product;

import com.askdog.model.common.State;
import com.askdog.model.entity.Product;

import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class ProductDetailBasic implements Serializable {

    private static final long serialVersionUID = 565451029547339449L;

    private Long id;
    private String name;
    private String description;
    private Long videoId;
    private Long storeId;
    private Set<Long> coupons;
    private State state;
    private EnumSet<Product.ProductTags> tags;
    private Long coverId;
    private List<ProductPicture> pictures;
    private Date creationTime;
    private Long creationUserId;
    private boolean deleted;

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

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Set<Long> getCoupons() {
        return coupons;
    }

    public void setCoupons(Set<Long> coupons) {
        this.coupons = coupons;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public EnumSet<Product.ProductTags> getTags() {
        return tags;
    }

    public void setTags(EnumSet<Product.ProductTags> tags) {
        this.tags = tags;
    }

    public Long getCoverId() {
        return coverId;
    }

    public void setCoverId(Long coverId) {
        this.coverId = coverId;
    }

    public List<ProductPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<ProductPicture> pictures) {
        this.pictures = pictures;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Long getCreationUserId() {
        return creationUserId;
    }

    public void setCreationUserId(Long creationUserId) {
        this.creationUserId = creationUserId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
