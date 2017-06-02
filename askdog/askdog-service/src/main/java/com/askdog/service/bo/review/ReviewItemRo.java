package com.askdog.service.bo.review;

import com.askdog.service.bo.system.event.EventRo;

import java.io.Serializable;

public class ReviewItemRo implements Serializable {

    private static final long serialVersionUID = -1246401957446927258L;

    private String reviewId;
    private EventRo event;
    private Object detail;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public EventRo getEvent() {
        return event;
    }

    public void setEvent(EventRo event) {
        this.event = event;
    }

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }
}
