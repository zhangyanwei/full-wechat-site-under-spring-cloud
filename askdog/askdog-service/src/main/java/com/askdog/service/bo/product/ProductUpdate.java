package com.askdog.service.bo.product;

import com.askdog.model.entity.Product.ProductTags;

import javax.validation.constraints.Size;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class ProductUpdate {

    private Long id;

    @Size(min = 1, max = 15)
    private String name;

    @Size(max = 50000)
    private String description;

    private Long videoId;

    private Set<Long> coupons;

    private Long coverId;

    private List<ProductPicture> pictures;

    private EnumSet<ProductTags> tags;

    public Long getId() {
        return id;
    }

    public ProductUpdate setId(Long id) {
        this.id = id;
        return this;
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

    public Set<Long> getCoupons() {
        return coupons;
    }

    public void setCoupons(Set<Long> coupons) {
        this.coupons = coupons;
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

    public EnumSet<ProductTags> getTags() {
        return tags;
    }

    public void setTags(EnumSet<ProductTags> tags) {
        this.tags = tags;
    }
}
