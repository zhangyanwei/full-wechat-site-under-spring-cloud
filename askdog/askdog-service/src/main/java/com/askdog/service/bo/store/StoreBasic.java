package com.askdog.service.bo.store;

import com.askdog.common.Out;
import com.askdog.service.bo.common.Location;
import com.askdog.service.bo.product.productdetail.ProductPageDetail_Coupon;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static java.util.stream.Collectors.toList;

class StoreBasic implements Out<StoreBasic, StoreDetail> {

    @JsonFormat(shape = STRING)
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String coverImage;
    private String type;
    private Float cpc;
    private Location location;
    private List<ProductPageDetail_Coupon> coupons;
    private List<String> events;

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

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getCpc() {
        return cpc;
    }

    public void setCpc(Float cpc) {
        this.cpc = cpc;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<ProductPageDetail_Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<ProductPageDetail_Coupon> coupons) {
        this.coupons = coupons;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    @Override
    public StoreBasic from(StoreDetail storeDetail) {
        this.id = storeDetail.getId();
        this.name = storeDetail.getName();
        this.address = storeDetail.getAddress();
        this.phone = storeDetail.getPhone();
        this.coverImage = storeDetail.getCoverImage();
        this.type = storeDetail.getType();
        this.cpc = storeDetail.getCpc();
        this.location = storeDetail.getLocation();

        if(storeDetail.getSpecialProduct() != null) {
            this.coupons = storeDetail.getSpecialProduct().getCoupons();
        }

        List<com.askdog.service.bo.StoreDetail.Event> events = storeDetail.getEvents();
        if (events != null && events.size() > 0) {
            this.events = events.stream().map(com.askdog.service.bo.StoreDetail.Event::getName).collect(toList());
        }

        return this;
    }
}
