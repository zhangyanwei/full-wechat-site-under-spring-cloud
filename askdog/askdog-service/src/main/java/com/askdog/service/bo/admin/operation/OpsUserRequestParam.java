package com.askdog.service.bo.admin.operation;

import javax.validation.constraints.NotNull;

public class OpsUserRequestParam extends BasicRequestParam {

    @NotNull
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OpsUserRequestParam(){}

    public OpsUserRequestParam(String index, String type, Double score, String email){
        this.email = email;
        super.setIndex(index);
        super.setType(type);
        super.setScore(score);
    }
}
