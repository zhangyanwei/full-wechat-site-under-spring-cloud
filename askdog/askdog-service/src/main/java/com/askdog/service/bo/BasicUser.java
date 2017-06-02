package com.askdog.service.bo;

import com.askdog.model.data.UserAttribute;
import com.askdog.model.entity.inner.user.UserTag;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.EnumSet;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class BasicUser implements Serializable {

    private static final long serialVersionUID = 8169328852529687491L;

    @JsonFormat(shape = STRING)
    private Long id;
    private String nickname;
    private String avatar;
    private UserAttribute.Gender gender;
    private EnumSet<UserTag> tags;
    private String signature;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public UserAttribute.Gender getGender() {
        return gender;
    }

    public void setGender(UserAttribute.Gender gender) {
        this.gender = gender;
    }

    public EnumSet<UserTag> getTags() {
        return tags;
    }

    public void setTags(EnumSet<UserTag> tags) {
        this.tags = tags;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}
