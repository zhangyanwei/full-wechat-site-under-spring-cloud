package com.askdog.service.bo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.askdog.common.RegexPattern.REGEX_NICK_NAME;
import static com.askdog.common.RegexPattern.REGEX_USER_NAME;
import static com.askdog.common.RegexPattern.REGEX_USER_PASSWORD;

public class SimpleUserCreation {

    @NotNull
    @Pattern(regexp = REGEX_USER_NAME)
    private String username;

    @NotNull
    @Pattern(regexp = REGEX_NICK_NAME)
    private String nickname;

    @NotNull
    @Pattern(regexp = REGEX_USER_PASSWORD)
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
