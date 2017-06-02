package com.askdog.model.entity.builder;

import com.askdog.model.entity.User;
import com.askdog.model.entity.inner.user.UserStatus;
import com.askdog.model.entity.inner.user.UserType;

import java.util.Date;

public final class UserBuilder {

    private String nickname;
    private String email;
    private String phoneNumber;
    private String password;
    private UserType type;
    private Date registrationTime;

    public static UserBuilder userBuilder() {
        return new UserBuilder();
    }

    public UserBuilder nickname(String name) {
        this.nickname = name;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder type(UserType type) {
        this.type = type;
        return this;
    }

    public UserBuilder registrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
        return this;
    }

    public User build() {
        User user = new User();
        user.setNickname(this.nickname);
        user.setEmail(this.email);
        user.setPhoneNumber(this.phoneNumber);
        user.setPassword(this.password);
        user.setType(this.type);
        user.setRegistrationTime(this.registrationTime);
        user.addStatus(UserStatus.MAIL_CONFIRMED);
        return user;
    }

}
