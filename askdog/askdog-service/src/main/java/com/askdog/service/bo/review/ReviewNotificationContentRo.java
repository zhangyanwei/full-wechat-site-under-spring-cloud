package com.askdog.service.bo.review;

import com.askdog.model.data.ReviewItem;
import com.askdog.model.data.inner.ReportType;
import com.askdog.service.bo.system.event.EventRo;

import java.io.Serializable;
import java.util.Date;

import static com.askdog.model.data.ReviewItem.ReviewStatus.REVIEWING;

public class ReviewNotificationContentRo implements Serializable {

    private static final long serialVersionUID = 161085186078837069L;

    private EventRo reviewEvent;
    private ReviewItem.ReviewStatus reviewStatus = REVIEWING;
    private ReportType rejectType;
    private String reviewNote;
    private Date reviewDate;

    public EventRo getReviewEvent() {
        return reviewEvent;
    }

    public void setReviewEvent(EventRo reviewEvent) {
        this.reviewEvent = reviewEvent;
    }

    public ReviewItem.ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ReviewItem.ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public ReportType getRejectType() {
        return rejectType;
    }

    public void setRejectType(ReportType rejectType) {
        this.rejectType = rejectType;
    }

    public String getReviewNote() {
        return reviewNote;
    }

    public void setReviewNote(String reviewNote) {
        this.reviewNote = reviewNote;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
}
