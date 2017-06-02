package com.askdog.model.data;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mc_address_code")
public class AddressCode extends Base{

    private static final long serialVersionUID = -8116444292477871203L;

    private String code;
    private String name;
    private String parent;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
