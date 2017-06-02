package com.askdog.service.bo;

import java.io.Serializable;
import java.util.Map;

public class BasicExternalUser extends BindAccount implements Serializable {

    private static final long serialVersionUID = 7321768292498703303L;

    private Map<String, String> details;
    private BasicInnerUser innerUser;

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    public BasicInnerUser getInnerUser() {
        return innerUser;
    }

    public void setInnerUser(BasicInnerUser innerUser) {
        this.innerUser = innerUser;
    }
}
