package com.askdog.service.bo.store;

import java.io.Serializable;

public class ContactsUserDetail implements Serializable {

    private static final long serialVersionUID = 987938879358694775L;
    private String name;
    private String phone;
    private String telephone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
