package com.askdog.service.impl.location;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties("askdog.location")
@ConditionalOnProperty(prefix = "askdog.location", name = "key")
public class LocationConfiguration {

    @NotNull
    private String baseUrl;

    @NotNull
    private String key;

    @NotNull
    private String ipLocation;

    @NotNull
    private String geoLocation;

    private double nearbyDistance;

    private long nearbyLimit;

    private long rencentLimit;

    private double rateDistance;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIpLocation() {
        return ipLocation;
    }

    public void setIpLocation(String ipLocation) {
        this.ipLocation = ipLocation;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public double getNearbyDistance() {
        return nearbyDistance;
    }

    public void setNearbyDistance(double nearbyDistance) {
        this.nearbyDistance = nearbyDistance;
    }

    public long getNearbyLimit() {
        return nearbyLimit;
    }

    public void setNearbyLimit(long nearbyLimit) {
        this.nearbyLimit = nearbyLimit;
    }

    public long getRencentLimit() {
        return rencentLimit;
    }

    public void setRencentLimit(long rencentLimit) {
        this.rencentLimit = rencentLimit;
    }

    public double getRateDistance() {
        return rateDistance;
    }

    public void setRateDistance(double rateDistance) {
        this.rateDistance = rateDistance;
    }
}
