package com.askdog.model.data;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "ad_user_attribute ")
public class UserAttribute extends Base implements Serializable {

    private static final long serialVersionUID = -3163187605126868852L;

    private Long userId;
    private String realName;
    private Gender gender;
    private String phone;
    private Address address;
    private String occupation;
    private String school;
    private String major;
    private String signature;
    private boolean isUpdateUserBasicInfo = true;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean isUpdateUserBasicInfo() {
        return isUpdateUserBasicInfo;
    }

    public void setUpdateUserBasicInfo(boolean updateUserBasicInfo) {
        isUpdateUserBasicInfo = updateUserBasicInfo;
    }

    public enum Gender {
        MALE, FEMALE
    }

    public static class Address implements Serializable {

        private static final long serialVersionUID = -674513753585233476L;

        // TODO: 16-6-11 not in user'profile basic info
        private String province;
        private String city;

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
    }

}
