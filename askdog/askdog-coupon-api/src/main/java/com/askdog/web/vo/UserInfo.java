package com.askdog.web.vo;

import com.askdog.common.Out;
import com.askdog.model.entity.inner.user.UserType;
import com.askdog.service.bo.BasicInnerUser;
import com.askdog.web.configuration.userdetails.AdUserDetails;

public class UserInfo implements Out<UserInfo, AdUserDetails> {

    private Long id;
    private String name;
    private UserType type;
    private String avatar;

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

    @Override
    public UserInfo from(AdUserDetails details) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(details.getId());
        userInfo.setName(details.getUser().getNickname());

        BasicInnerUser innerUser = details.getUser();
        userInfo.setType(innerUser.getType());
        userInfo.setAvatar(innerUser.getAvatar());
        return userInfo;
    }
}
