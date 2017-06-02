package com.askdog.service.bo.usercoupon;

import com.askdog.model.entity.User;
import com.askdog.model.entity.inner.user.UserType;

public class UserBasic {

    private long userId;
    private String nickname;
    private String phoneNumber;
    private UserType type;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    UserBasic from(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.phoneNumber = user.getPhoneNumber();
        this.type = user.getType();
        return this;
    }

}
