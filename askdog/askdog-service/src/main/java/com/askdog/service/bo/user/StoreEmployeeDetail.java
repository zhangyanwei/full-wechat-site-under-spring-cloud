package com.askdog.service.bo.user;

import com.askdog.common.Out;
import com.askdog.model.entity.StoreEmployee.EmployeeRole;
import com.askdog.service.bo.UserDetail;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class StoreEmployeeDetail implements Out<StoreEmployeeDetail, UserDetail> {

    @JsonFormat(shape = STRING)
    private Long id;
    private String nickname;
    private String avatar;
    private EnumSet<EmployeeRole> roles;
    private String note;

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

    public EnumSet<EmployeeRole> getRoles() {
        return roles;
    }

    public void setRoles(EnumSet<EmployeeRole> roles) {
        this.roles = roles;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public StoreEmployeeDetail from(UserDetail entity) {
        this.id = entity.getId();
        this.nickname = entity.getNickname();
        this.avatar = entity.getAvatar();
        return this;
    }
}
