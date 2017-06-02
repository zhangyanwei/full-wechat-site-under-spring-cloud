package com.askdog.web.vo;

import com.askdog.common.RegexPattern;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.askdog.common.RegexPattern.REGEX_USER_PASSWORD;

public class TokenPassword {

    @NotNull
    @Pattern(regexp = RegexPattern.REGEX_MAIL)
    private String mail;

    @NotNull
    private String token;

    @NotNull
    @Pattern(regexp = REGEX_USER_PASSWORD)
    private String password;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
