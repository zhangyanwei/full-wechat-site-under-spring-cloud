package com.askdog.model.data;

import com.askdog.model.data.inner.ReportType;

public class Report extends Actions.Action {

    private ReportType type;
    private String message;

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
