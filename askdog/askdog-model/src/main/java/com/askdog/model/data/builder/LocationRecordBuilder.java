package com.askdog.model.data.builder;

import com.askdog.model.data.UserLocation;
import com.askdog.model.data.UserResidence;
import com.askdog.model.data.inner.EntityType;
import com.askdog.model.data.inner.location.LocationDescription;
import com.askdog.model.data.inner.location.LocationProvider;
import com.askdog.model.data.inner.location.LocationRecord;

import javax.validation.constraints.NotNull;
import java.util.Date;

public final class LocationRecordBuilder {

    private LocationProvider provider;
    @NotNull
    private EntityType targetType;
    @NotNull
    private Long target;
    private String ip;
    private LocationDescription description;
    private Date creationDate;

    public Long getTarget() {
        return target;
    }

    public LocationRecordBuilder setTarget(Long target) {
        this.target = target;
        return this;
    }

    public EntityType getTargetType() {
        return targetType;
    }

    public LocationRecordBuilder setTargetType(EntityType targetType) {
        this.targetType = targetType;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public LocationRecordBuilder setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public LocationRecordBuilder setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public LocationProvider getProvider() {
        return provider;
    }

    public LocationRecordBuilder setProvider(LocationProvider provider) {
        this.provider = provider;
        return this;
    }

    public LocationDescription getDescription() {
        return description;
    }

    public LocationRecordBuilder setDescription(LocationDescription description) {
        this.description = description;
        return this;
    }


    public LocationRecord build() {
        LocationRecord record = new LocationRecord();
        record.setProvider(this.provider);
        record.setTargetType(this.targetType);
        record.setTargetId(this.target);
        record.setIp(this.ip);
        record.setDescription(this.description);
        record.setCreationDate(new Date());
        record.getGeometry();
        return record;
    }

    public UserLocation buildUserLocation() {
        UserLocation record = new UserLocation();
        record.setProvider(this.provider);
        record.setTargetType(this.targetType);
        record.setTargetId(this.target);
        record.setIp(this.ip);
        record.setDescription(this.description);
        record.setCreationDate(new Date());
        record.getGeometry();
        return record;
    }

    public UserResidence bulidUserResidence(double rate, @NotNull Date loginAt) {
        UserResidence record = new UserResidence();
        record.setProvider(this.provider);
        record.setTargetType(this.targetType);
        record.setTargetId(this.target);
        record.setIp(this.ip);
        record.setDescription(this.description);
        record.setCreationDate(new Date());
        record.setRate(rate);
        record.setLoginAt(loginAt);
        record.getGeometry();
        return record;
    }

    public static LocationRecordBuilder locationRecordBuilder() {
        return new LocationRecordBuilder();
    }

}
