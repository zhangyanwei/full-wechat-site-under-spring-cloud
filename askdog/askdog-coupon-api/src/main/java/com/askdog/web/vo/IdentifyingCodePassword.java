package com.askdog.web.vo;

import com.askdog.common.RegexPattern;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.askdog.common.RegexPattern.REGEX_USER_PASSWORD;

public class IdentifyingCodePassword {

    @NotNull
    @Pattern(regexp = RegexPattern.REGEX_PHONE)
    private String phone;

    @NotNull
    private String code;

    @NotNull
    @Pattern(regexp = REGEX_USER_PASSWORD)
    private String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
