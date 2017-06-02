package com.askdog.coupon.store.web.vo;

import com.askdog.common.Out;
import com.askdog.service.bo.UserDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class UserBasic implements Out<UserBasic, UserDetail> {

    @JsonFormat(shape = STRING)
    private Long id;
    private String username;
    private String nickname;

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

    @Override
    public UserBasic from(UserDetail entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.nickname = entity.getNickname();
        return this;
    }
}
