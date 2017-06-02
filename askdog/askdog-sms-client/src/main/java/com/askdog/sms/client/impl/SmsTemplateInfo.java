package com.askdog.sms.client.impl;

import com.askdog.model.data.Base;

import java.io.Serializable;
import java.util.Date;

public class SmsTemplateInfo extends Base implements Serializable {
    private static final long serialVersionUID = -3163187605126868855L;

    private String name;
    private String templateID;
    private String language;
    private Date creationTime;
    private Date lastModifyTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateID() {
        return templateID;
    }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}
