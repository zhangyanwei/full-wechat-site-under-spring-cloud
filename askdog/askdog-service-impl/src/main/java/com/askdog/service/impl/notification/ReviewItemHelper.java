package com.askdog.service.impl.notification;

import com.askdog.model.data.ReviewItem;
import com.askdog.service.bo.review.ReviewNotificationContentRo;
import com.askdog.service.helper.EventHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewItemHelper {

    @Autowired
    private EventHelper eventHelper;

    public ReviewNotificationContentRo convert(ReviewItem reviewItem) {
        ReviewNotificationContentRo content = new ReviewNotificationContentRo();
        content.setReviewEvent(eventHelper.convert(reviewItem.getPerformerId(), reviewItem.getEventType(), reviewItem.getTargetId()));
        content.setReviewStatus(reviewItem.getReviewStatus());
        content.setRejectType(reviewItem.getRejectType());
        content.setReviewNote(reviewItem.getReviewNote());
        content.setReviewDate(reviewItem.getReviewDate());
        return content;
    }

}
