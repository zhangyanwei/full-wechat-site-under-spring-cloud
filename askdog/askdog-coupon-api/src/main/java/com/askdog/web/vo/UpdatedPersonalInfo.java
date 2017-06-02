package com.askdog.web.vo;

import com.askdog.common.In;
import com.askdog.model.data.UserAttribute;
import com.askdog.model.data.UserAttribute.Address;
import com.askdog.model.data.UserAttribute.Gender;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

public class UpdatedPersonalInfo implements In<UserAttribute> {

    private  String name;

    private String phone;

    private Gender gender;

    @Valid
    private Address address;

    @Pattern(regexp = ".{0,37}")
    private String occupation;

    @Pattern(regexp = ".{0,37}")
    private String major;

    @Pattern(regexp = ".{0,37}")
    private String school;

    @Pattern(regexp = "[\\s\\S]{0,100}")
    private String signature;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public UserAttribute convert() {
        UserAttribute userAttribute = new UserAttribute();
        userAttribute.setRealName(this.name);
        userAttribute.setPhone(this.phone);
        userAttribute.setGender(this.gender);
        userAttribute.setAddress(this.address);
        userAttribute.setOccupation(this.occupation);
        userAttribute.setSchool(this.school);
        userAttribute.setMajor(this.major);
        userAttribute.setSignature(this.signature);
        return userAttribute;
    }

}
