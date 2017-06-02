package com.askdog.service.bo;

import com.askdog.common.Out;
import com.askdog.model.data.UserAttribute;
import com.askdog.model.data.UserAttribute.Gender;
import com.askdog.model.common.State;
import com.askdog.model.entity.inner.user.UserTag;
import com.askdog.model.entity.inner.user.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.EnumSet;

import static com.askdog.model.data.UserAttribute.Address;
import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class UserDetail implements Out<UserDetail, DecoratedUser>, Serializable {

    private static final long serialVersionUID = 628375547402545660L;

    @JsonFormat(shape = STRING)
    private Long id;
    private String username;
    private UserType type;
    private String nickname;
    private String name;
    private String phone;
    private String basePhone;
    private String mail;
    private String avatar;
    private State state;

    private Gender gender;
    private Address address;
    private String occupation;
    private String major;
    private String school;
    private String signature;
    private EnumSet<UserTag> tags;

    private boolean deleted;

    private boolean perfected;

    private boolean isAdmin;

    public Long getId() {
        return id;
    }

    public UserDetail setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public String getBasePhone() {
        return basePhone;
    }

    public void setBasePhone(String basePhone) {
        this.basePhone = basePhone;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isPerfected() {
        return perfected;
    }

    public void setPerfected(boolean perfected) {
        this.perfected = perfected;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public UserDetail from(DecoratedUser entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.type = entity.getType();
        this.nickname = entity.getNickname();
        this.name = entity.getName();
        this.mail = entity.getEmail();
        this.basePhone = entity.getPhoneNumber();
        this.avatar = entity.getAvatarUrl();
        this.tags = entity.getUserTags() == null ? EnumSet.noneOf(UserTag.class) : entity.getUserTags();
        this.state = entity.getState();
        return this;
    }

    public BasicUser toBasic() {
        BasicUser basicUser = new BasicUser();
        basicUser.setId(this.getId());
        basicUser.setNickname(this.getNickname());
        basicUser.setAvatar(this.getAvatar());
        basicUser.setTags(this.getTags());
        basicUser.setGender(this.getGender());
        basicUser.setSignature(this.getSignature());
        return basicUser;
    }

    public UserDetail setAttributes(@Nonnull UserAttribute attributes) {
        this.setGender(attributes.getGender());
        this.setPhone(attributes.getPhone());
        this.setAddress(attributes.getAddress());
        this.setOccupation(attributes.getOccupation());
        this.setSchool(attributes.getSchool());
        this.setMajor(attributes.getMajor());
        this.setSignature(attributes.getSignature());
        return this;
    }
}
