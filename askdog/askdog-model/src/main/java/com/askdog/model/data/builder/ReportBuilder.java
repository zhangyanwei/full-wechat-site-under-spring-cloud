package com.askdog.model.data.builder;

import com.askdog.model.data.Report;
import com.askdog.model.data.inner.ReportType;

public final class ReportBuilder extends ActionBuilder<ReportBuilder, Report> {

    private ReportType type;
    private String message;

    public ReportBuilder type(ReportType type) {
        this.type = type;
        return this;
    }

    public ReportBuilder message(String message) {
        this.message = message;
        return this;
    }

    public Report build() {
        Report report = new Report();
        report.setType(this.type);
        report.setMessage(this.message);
        return build(report);
    }

    public static ReportBuilder reportBuilder() {
        return new ReportBuilder();
    }

}
