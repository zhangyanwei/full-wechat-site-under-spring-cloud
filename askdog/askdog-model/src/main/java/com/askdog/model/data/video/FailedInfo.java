package com.askdog.model.data.video;

import java.io.Serializable;

public class FailedInfo implements Serializable {

    private static final long serialVersionUID = 3869396431045972753L;

    private FailedType type;
    private String code;
    private String message;

    public FailedType getType() {
        return type;
    }

    public void setType(FailedType type) {
        this.type = type;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public FailedInfo(FailedType type, String code, String message) {
        this.type = type;
        this.code = code;
        this.message = message;
    }

    public enum FailedType {
        RESOURCE, TRANSCODE, SNAPSHOT
    }
}