package com.askdog.coupon.store.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class AuthenticationUser {

    private Long id;
    private String name;
    private String avatar;
    private String[] authorities;

    @JsonFormat(shape = STRING)
    private List<Long> storeIds;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }

    public List<Long> getStoreIds() {
        return storeIds;
    }

    public void setStoreIds(List<Long> storeIds) {
        this.storeIds = storeIds;
    }
}
