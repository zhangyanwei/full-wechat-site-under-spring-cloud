package com.askdog.service.bo.addresscode;

import com.askdog.model.data.AddressCode;

import java.io.Serializable;

public class AddressCodePageDetail implements Serializable {

    private static final long serialVersionUID = 909525749199182699L;

    private String code;
    private String name;

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

    public AddressCodePageDetail from(AddressCode addressCode) {
        this.code = addressCode.getCode();
        this.name = addressCode.getName();
        return this;
    }
}
