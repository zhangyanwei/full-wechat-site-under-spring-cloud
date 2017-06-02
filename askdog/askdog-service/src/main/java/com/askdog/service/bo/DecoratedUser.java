package com.askdog.service.bo;

import com.askdog.model.entity.User;
import com.askdog.model.common.State;
import com.askdog.model.entity.inner.user.UserStatus;
import com.askdog.model.entity.inner.user.UserTag;
import com.askdog.model.entity.inner.user.UserType;
import com.askdog.model.security.Authority;

import java.util.EnumSet;

public class DecoratedUser {

    private String avatarUrl;
    private String nickname;
    private User user;

    public DecoratedUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return user.getId();
    }

    public String getName() {
        return user.getName();
    }

    public String getUsername() {
        return user.getName();
    }

    public String getPhoneNumber() {
        return user.getPhoneNumber();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public UserType getType() {
        return user.getType();
    }

    public EnumSet<UserStatus> getUserStatuses() {
        return user.getUserStatuses();
    }

    public EnumSet<Authority.Role> getAuthorities() {
        return user.getAuthorities();
    }

    public EnumSet<UserTag> getUserTags() {
        return user.getUserTags();
    }

    public Long getAvatar() {
        return user.getAvatar();
    }

    public State getState() {
        return user.getState();
    }

    // dynamic attributes
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}