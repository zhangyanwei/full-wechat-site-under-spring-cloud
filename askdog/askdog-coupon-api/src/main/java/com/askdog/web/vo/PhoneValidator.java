package com.askdog.web.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.askdog.common.RegexPattern.REGEX_PHONE;

public class PhoneValidator {

    @NotNull
    @Pattern(regexp = REGEX_PHONE)
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
