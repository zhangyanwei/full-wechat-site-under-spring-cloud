package com.askdog.web.vo;

import com.askdog.common.In;
import com.askdog.model.entity.User;
import com.askdog.model.entity.inner.user.UserType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.askdog.common.RegexPattern.*;

public class PhoneUser implements In<User> {

    @NotNull
    @Pattern(regexp = REGEX_NICK_NAME)
    private String nickname;

    @NotNull
    @Pattern(regexp = REGEX_USER_PASSWORD)
    private String password;

    @Pattern(regexp = REGEX_PHONE)
    private String phone;

    @Pattern(regexp = REGEX_IDENTIFYING_CODE)
    private String code;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String identifyingCode) {
        this.code = identifyingCode;
    }

    @Override
    public User convert() {
        User user = new User();
        user.setNickname(this.nickname);
        user.setPassword(this.password);
        user.setPhoneNumber(this.phone);
        user.setType(UserType.REGISTERED);
        return user;
    }
}
