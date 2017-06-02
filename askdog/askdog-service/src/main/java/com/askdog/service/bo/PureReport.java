package com.askdog.service.bo;

import com.askdog.model.data.inner.ReportType;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class PureReport {

    @NotNull
    private ReportType type;

    @Pattern(regexp = ".{0,50}")
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
