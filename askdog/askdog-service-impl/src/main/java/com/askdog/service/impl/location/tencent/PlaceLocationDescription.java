package com.askdog.service.impl.location.tencent;

import com.askdog.model.data.inner.location.LocationDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.elasticsearch.common.Strings;
import org.springframework.util.Assert;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceLocationDescription extends LocationDescription {

    private Location location;
    private String title;
    private String address;
    private String category;

    @JsonProperty("ad_info")
    private AddressInfo addressInfo;

    private FormattedAddresses formattedAddresses;

    @JsonProperty("_distance")
    private Float distance;

    @JsonProperty("_dir_desc")
    private String direction;

    @Override
    public Double[] coordinates() {
        Assert.notNull(this.location);
        return new Double[]{this.location.getLng(), this.location.getLat()};
    }

    @Override
    public String addressRecommend() {
        return formattedAddresses == null ? address : (Strings.isNullOrEmpty(formattedAddresses.getRecommend()) ? address : formattedAddresses.getRecommend());
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public FormattedAddresses getFormattedAddresses() {
        return formattedAddresses;
    }

    public void setFormattedAddresses(FormattedAddresses formattedAddresses) {
        this.formattedAddresses = formattedAddresses;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressInfo {

        @JsonProperty("adcode")
        private String addressCode;
        private String name;
        private Location location;
        private String nation;
        private String province;
        private String city;
        private String district;

        public String getAddressCode() {
            return addressCode;
        }

        public void setAddressCode(String addressCode) {
            this.addressCode = addressCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FormattedAddresses {

        private String recommend;
        private String rough;

        public String getRecommend() {
            return recommend;
        }

        public void setRecommend(String recommend) {
            this.recommend = recommend;
        }

        public String getRough() {
            return rough;
        }

        public void setRough(String rough) {
            this.rough = rough;
        }
    }
}
