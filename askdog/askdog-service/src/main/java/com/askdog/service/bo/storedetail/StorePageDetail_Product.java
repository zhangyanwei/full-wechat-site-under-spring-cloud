package com.askdog.service.bo.storedetail;

import com.askdog.common.Out;
import com.askdog.model.entity.Product;
import com.askdog.service.bo.product.ProductDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class StorePageDetail_Product implements Out<StorePageDetail_Product, ProductDetail> {

    @JsonFormat(shape = STRING)
    private Long id;
    private String name;
    private StorePageDetail_Video video;
    private EnumSet<Product.ProductTags> tags;
    private String coverUrl;

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


    public StorePageDetail_Video getVideo() {
        return video;
    }

    public void setVideo(StorePageDetail_Video video) {
        this.video = video;
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

    @Override
    public StorePageDetail_Product from(ProductDetail entity) {
        StorePageDetail_Product productStoreList = new StorePageDetail_Product();
        productStoreList.setId(entity.getId());
        productStoreList.setName(entity.getName());
        if (entity.getVideo() != null) {
            productStoreList.setVideo(new StorePageDetail_Video().from(entity.getVideo()));
        }

        productStoreList.setTags(entity.getTags());
        productStoreList.setCoverUrl(entity.getCoverUrl());

        return productStoreList;
    }
}
