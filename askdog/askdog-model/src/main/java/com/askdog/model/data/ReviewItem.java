package com.askdog.model.data;

import com.askdog.model.common.EventType;
import com.askdog.model.common.EventType.EventTypeGroup;
import com.askdog.model.data.inner.ReportType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

import static com.askdog.model.data.ReviewItem.ReviewStatus.REVIEWING;

@Document(collection = "mc_review")
public class ReviewItem extends Base implements Serializable {

    private static final long serialVersionUID = 496405988941916532L;

    private Long performerId;
    private EventType eventType;
    private Long targetId;
    private Date eventDate = new Date();
    private EventTypeGroup resourceType;
    private ReviewStatus reviewStatus = REVIEWING;
    private ReportType rejectType;
    private String reviewNote;
    private Long reviewUserId;
    private Date reviewDate;

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public EventTypeGroup getResourceType() {
        return resourceType;
    }

    public void setResourceType(EventTypeGroup resourceType) {
        this.resourceType = resourceType;
    }

    public ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ReviewStatus reviewStatus) {
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

    public Long getReviewUserId() {
        return reviewUserId;
    }

    public void setReviewUserId(Long reviewUserId) {
        this.reviewUserId = reviewUserId;
    }

    public enum ReviewStatus {
        IGNORE, REJECT, ACCEPT, REVIEWING
    }
}
