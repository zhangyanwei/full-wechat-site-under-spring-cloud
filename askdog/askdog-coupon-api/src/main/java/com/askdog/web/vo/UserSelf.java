package com.askdog.web.vo;

import com.askdog.common.Out;
import com.askdog.model.entity.inner.user.UserTag;
import com.askdog.model.entity.inner.user.UserType;
import com.askdog.service.bo.UserDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class UserSelf implements Out<UserSelf, UserDetail> {

    @JsonFormat(shape = STRING)
    private Long id;
    private String name;
    private String mail;
    private String phone;
    private String basePhone;
    private UserType type;
    private String avatar;
    private long noticeCount;
    private EnumSet<UserTag> tags;
    private boolean subscribed;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBasePhone() {
        return basePhone;
    }

    public void setBasePhone(String basePhone) {
        this.basePhone = basePhone;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getNoticeCount() {
        return noticeCount;
    }

    public void setNoticeCount(long noticeCount) {
        this.noticeCount = noticeCount;
    }

    public EnumSet<UserTag> getTags() {
        return tags;
    }

    public void setTags(EnumSet<UserTag> tags) {
        this.tags = tags;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    @Override
    public UserSelf from(UserDetail user) {
        this.id = user.getId();
        this.name = user.getNickname();
        this.mail = user.getMail();
        this.phone = user.getPhone();
        this.basePhone = user.getBasePhone();
        this.type = user.getType();
        this.avatar = user.getAvatar();
        this.tags = user.getTags();
        return this;
    }
}
