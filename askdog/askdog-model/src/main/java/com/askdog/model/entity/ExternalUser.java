package com.askdog.model.entity;

import com.askdog.model.converter.MapConverter;
import com.askdog.model.entity.inner.user.UserProvider;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

@Entity
@Table(name = "mc_external_user")
public class ExternalUser extends Base {

    private static final long serialVersionUID = 702492398661752105L;

    @Column(name = "external_user_id", nullable = false, updatable = false)
    private String externalUserId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "avatar_url")
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, updatable = false)
    private UserProvider provider;

    @Convert(converter = MapConverter.class)
    @Column(name = "details", length = 4000)
    private Map<String, String> details;

    @JoinColumn(name="bind_user")
    @ManyToOne(optional = false, cascade = {PERSIST, MERGE})
    private User user;

    @Column(name = "registration_time")
    private Date registrationTime;

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
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

    public UserProvider getProvider() {
        return provider;
    }

    public void setProvider(UserProvider provider) {
        this.provider = provider;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }
}