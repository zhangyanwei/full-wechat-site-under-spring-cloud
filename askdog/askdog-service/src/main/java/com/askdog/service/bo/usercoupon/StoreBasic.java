package com.askdog.service.bo.usercoupon;


import com.askdog.service.bo.StoreDetail;
import com.askdog.service.bo.common.Location;
import com.fasterxml.jackson.annotation.JsonFormat;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class StoreBasic {

    @JsonFormat(shape = STRING)
    private long id;
    private String name;
    private String address;
    private String phone;
    private Location location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public StoreBasic from(StoreDetail storeDetail) {
        this.id = storeDetail.getId();
        this.name = storeDetail.getName();
        this.address = storeDetail.getAddress();
        this.phone = storeDetail.getPhone();
        this.location = storeDetail.getLocation();
        return this;
    }
}
