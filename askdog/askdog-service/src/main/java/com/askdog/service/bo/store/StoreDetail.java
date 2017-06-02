package com.askdog.service.bo.store;

import com.askdog.common.Out;
import com.askdog.service.bo.BasicUser;
import com.askdog.service.bo.common.Location;
import com.askdog.service.bo.event.EventDetail;
import com.askdog.service.bo.product.productdetail.ProductPageDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class StoreDetail implements Out<StoreDetail, com.askdog.service.bo.StoreDetail> {

    @JsonFormat(shape = STRING)
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private BasicUser owner;
    private Location location;
    private String type;
    private Float cpc;
    private String businessHours;
    private Date creationTime;
    private String coverImage;
    private ContactsUserDetail contactsUserDetail;
    private ProductPageDetail specialProduct;
    private List<com.askdog.service.bo.StoreDetail.Event> events;

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

    public BasicUser getOwner() {
        return owner;
    }

    public void setOwner(BasicUser owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public ContactsUserDetail getContactsUserDetail() {
        return contactsUserDetail;
    }

    public void setContactsUserDetail(ContactsUserDetail contactsUserDetail) {
        this.contactsUserDetail = contactsUserDetail;
    }

    public ProductPageDetail getSpecialProduct() {
        return specialProduct;
    }

    public void setSpecialProduct(ProductPageDetail specialProduct) {
        this.specialProduct = specialProduct;
    }

    public List<com.askdog.service.bo.StoreDetail.Event> getEvents() {
        return events;
    }

    public void setEvents(List<com.askdog.service.bo.StoreDetail.Event> events) {
        this.events = events;
    }

    @Override
    public StoreDetail from(com.askdog.service.bo.StoreDetail storeDetail) {
        this.id = storeDetail.getId();
        this.name = storeDetail.getName();
        this.description = storeDetail.getDescription();
        this.address = storeDetail.getAddress();
        this.phone = storeDetail.getPhone();
        this.owner = storeDetail.getOwner().toBasic();
        this.location = storeDetail.getLocation();
        this.type = storeDetail.getType();
        this.cpc = storeDetail.getCpc();
        this.businessHours = storeDetail.getBusinessHours();
        this.creationTime = storeDetail.getCreationTime();
        this.coverImage = storeDetail.getCoverImage();
        this.contactsUserDetail = storeDetail.getContactsUserDetail();
        this.events = storeDetail.getEvents();
        return this;
    }
}
