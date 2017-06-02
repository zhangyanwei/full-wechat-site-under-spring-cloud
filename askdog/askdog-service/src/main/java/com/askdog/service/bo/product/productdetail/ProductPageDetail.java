package com.askdog.service.bo.product.productdetail;

import com.askdog.common.Out;
import com.askdog.model.data.inner.VoteDirection;
import com.askdog.model.data.video.Video;
import com.askdog.model.entity.Product;
import com.askdog.service.bo.product.ProductDetail;
import com.askdog.service.bo.product.ProductDetailStatistics;
import com.askdog.service.bo.product.ProductPictureDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;
import java.util.List;

import static com.askdog.model.data.inner.VoteDirection.NONE;
import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static java.util.stream.Collectors.toList;

public class ProductPageDetail implements Out<ProductPageDetail, ProductDetail> {

    @JsonFormat(shape = STRING)
    private Long id;
    private String name;
    private String description;
    private Video video;
    private ProductPageDetail_Store store;
    private List<ProductPageDetail_Coupon> coupons;
    private boolean deleted;
    private EnumSet<Product.ProductTags> tags;
    private String coverUrl;
    private List<ProductPictureDetail> pictures;

    private boolean mine;
    private boolean editable;
    private boolean deletable;
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

    public ProductPageDetail_Store getStore() {
        return store;
    }

    public void setStore(ProductPageDetail_Store store) {
        this.store = store;
    }

    public List<ProductPageDetail_Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<ProductPageDetail_Coupon> coupons) {
        this.coupons = coupons;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public EnumSet<Product.ProductTags> getTags() {
        return tags;
    }

    public void setTags(EnumSet<Product.ProductTags> tags) {
        this.tags = tags;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public List<ProductPictureDetail> getPictures() {
        return pictures;
    }

    public void setPictures(List<ProductPictureDetail> pictures) {
        this.pictures = pictures;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
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

    @Override
    public ProductPageDetail from(ProductDetail entity) {
        ProductPageDetail productPageDetail = new ProductPageDetail();
        productPageDetail.setId(entity.getId());
        productPageDetail.setName(entity.getName());
        productPageDetail.setDescription(entity.getDescription());
        productPageDetail.setVideo(entity.getVideo());
        productPageDetail.setStore(new ProductPageDetail_Store().from(entity.getStore()));
        if (entity.getCoupons() != null) {
            productPageDetail.setCoupons(entity.getCoupons().stream().map(
                    couponDetail -> new ProductPageDetail_Coupon().from(couponDetail)).collect(toList()));
        }
        productPageDetail.setDeleted(entity.isDeleted());
        productPageDetail.setTags(entity.getTags());
        productPageDetail.setCoverUrl(entity.getCoverUrl());
        productPageDetail.setPictures(entity.getPictures());
        productPageDetail.setMine(entity.isMine());
        productPageDetail.setEditable(entity.isEditable());
        productPageDetail.setDeletable(entity.isDeletable());
        productPageDetail.setVote(entity.getVote());
        productPageDetail.setStatistics(entity.getStatistics());
        return productPageDetail;
    }
}
