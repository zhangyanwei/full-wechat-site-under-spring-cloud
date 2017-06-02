package com.askdog.model.data.inner.location;


import com.askdog.model.data.Target;
import com.askdog.model.data.inner.location.LocationDescription.Geometry;
import org.springframework.util.Assert;

import java.util.Date;

public class LocationRecord extends Target {

    private String ip;
    private Date creationDate;
    private LocationProvider provider;
    private LocationDescription description;
    private Geometry geometry;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public LocationProvider getProvider() {
        return provider;
    }

    public void setProvider(LocationProvider provider) {
        this.provider = provider;
    }

    public LocationDescription getDescription() {
        return description;
    }

    public void setDescription(LocationDescription description) {
        this.description = description;
    }

    public Geometry getGeometry() {
        Assert.notNull(this.description);
        if (this.geometry == null) {
            this.geometry = new Geometry(this.description.coordinates());
        }
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

}
