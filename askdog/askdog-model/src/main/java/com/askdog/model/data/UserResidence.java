package com.askdog.model.data;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;
import java.util.Date;

@Document(collection = "ad_user_residence")
public class UserResidence extends UserLocation {

    private static final long serialVersionUID = -7077871078807831966L;

    private double rate;

    private Date loginAt;

    @Nonnull
    public Double getLng() {
        return this.getGeometry().getCoordinates()[0];
    }

    @Nonnull
    public Double getLat() {
        return this.getGeometry().getCoordinates()[1];
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Date getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(Date loginAt) {
        this.loginAt = loginAt;
    }
}
