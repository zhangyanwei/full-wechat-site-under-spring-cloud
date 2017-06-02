package com.askdog.service.bo;

import com.askdog.model.entity.inner.user.UserStatus;
import com.askdog.model.entity.inner.user.UserType;
import com.askdog.model.security.Authority;

import java.io.Serializable;
import java.util.EnumSet;

public class BasicInnerUser implements Serializable {

    private static final long serialVersionUID = -7906184063602029210L;

    private Long id;
    private String username;
    private String nickname;
    private String mail;
    private String phone;
    private String password;
    private UserType type;
    private String avatar;
    private long noticeCount;
    private EnumSet<UserStatus> userStatuses;
    private EnumSet<Authority.Role> authorities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public EnumSet<UserStatus> getUserStatuses() {
        return userStatuses;
    }

    public void setUserStatuses(EnumSet<UserStatus> userStatuses) {
        this.userStatuses = userStatuses;
    }

    public boolean hasStatus(UserStatus userStatus) {
        return userStatuses != null && userStatuses.contains(userStatus);
    }

    public EnumSet<Authority.Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(EnumSet<Authority.Role> authorities) {
        this.authorities = authorities;
    }

    public String[] authorities() {
        EnumSet<Authority.Role> roles = getAuthorities();
        if (roles == null) {
            roles = EnumSet.noneOf(Authority.Role.class);
        }

        roles.add(Authority.Role.USER);
        return roles.stream().map(Authority.Role::authority).toArray(String[]::new);
    }
}
