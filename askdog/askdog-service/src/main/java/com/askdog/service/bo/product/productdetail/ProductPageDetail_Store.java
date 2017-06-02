package com.askdog.service.bo.product.productdetail;

import com.askdog.common.Out;
import com.askdog.service.bo.StoreDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class ProductPageDetail_Store implements Out<ProductPageDetail_Store, StoreDetail> {

    @JsonFormat(shape = STRING)
    private Long id;
    private String name;
    private String address;
    private String phone;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override public ProductPageDetail_Store from(StoreDetail storeDetail) {
        this.id = storeDetail.getId();
        this.name = storeDetail.getName();
        this.address = storeDetail.getAddress();
        this.phone = storeDetail.getPhone();
        return this;
    }
}
