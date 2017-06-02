package com.askdog.service.bo.review;

import com.askdog.model.data.ReviewItem.ReviewStatus;
import com.askdog.model.data.inner.ReportType;

public class PureReviewItem {

    private ReviewStatus status;
    private ReportType rejectType;
    private String note;

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public ReportType getRejectType() {
        return rejectType;
    }

    public void setRejectType(ReportType rejectType) {
        this.rejectType = rejectType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
