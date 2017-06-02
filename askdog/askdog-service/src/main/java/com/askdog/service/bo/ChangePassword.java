package com.askdog.service.bo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.askdog.common.RegexPattern.REGEX_USER_PASSWORD;

public class ChangePassword {

    @NotNull
    @Pattern(regexp = REGEX_USER_PASSWORD)
    private String originPassword;

    @NotNull
    @Pattern(regexp = REGEX_USER_PASSWORD)
    private String newPassword;

    public String getOriginPassword() {
        return originPassword;
    }

    public void setOriginPassword(String originPassword) {
        this.originPassword = originPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
