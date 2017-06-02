package com.askdog.model.data.inner.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public abstract class LocationDescription {

    public abstract Double[] coordinates();

    public abstract String addressRecommend();

    public static class Geometry {

        private static final String MONGO_GEO_TYPE = "Point";

        private String type;

        private Double[] coordinates;

        public Geometry(Double[] coordinates) {
            this.type = MONGO_GEO_TYPE;
            this.coordinates = coordinates;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Double[] getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(Double[] coordinates) {
            this.coordinates = coordinates;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {

        private Double lat;
        private Double lng;

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }
    }

}
