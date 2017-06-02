package com.askdog.service.bo.addresscode;

import javax.validation.constraints.NotNull;

public class PureAddressCode {

    @NotNull
    private String code;
    @NotNull
    private String name;
    @NotNull
    private String parent;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

}
