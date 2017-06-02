package com.askdog.web.vo;

import com.askdog.common.Out;
import com.askdog.model.data.UserAttribute.Address;
import com.askdog.model.entity.inner.user.UserTag;
import com.askdog.service.bo.UserDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;

import static com.askdog.model.data.UserAttribute.Gender;
import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class PersonalInfo implements Out<PersonalInfo, UserDetail> {

    @JsonFormat(shape = STRING)
    private Long id;
    private String name;
    private String mail;
    private String avatar;
    private Gender gender;
    private String phone;
    private Address address;
    private String occupation;
    private String major;
    private String school;
    private String signature;
    private EnumSet<UserTag> tags;

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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public EnumSet<UserTag> getTags() {
        return tags;
    }

    public void setTags(EnumSet<UserTag> tags) {
        this.tags = tags;
    }

    @Override
    public PersonalInfo from(UserDetail userDetail) {
        this.id = userDetail.getId();
        this.name = userDetail.getNickname();
        this.mail = userDetail.getMail();
        this.avatar = userDetail.getAvatar();
        this.gender = userDetail.getGender();
        this.phone = userDetail.getPhone();
        this.address = userDetail.getAddress();
        this.occupation = userDetail.getOccupation();
        this.major = userDetail.getMajor();
        this.school = userDetail.getSchool();
        this.signature = userDetail.getSignature();
        this.tags = userDetail.getTags();
        return this;
    }
}
