package com.askdog.service.impl.review;

import com.askdog.dao.repository.mongo.ReviewRepository;
import com.askdog.messaging.review.ReviewMessage;
import com.askdog.messaging.review.ReviewMessageInputChannel;
import com.askdog.model.data.ReviewItem;
import com.askdog.model.data.ReviewItem.ReviewStatus;
import com.askdog.model.data.inner.NotificationType;
import com.askdog.service.NotificationService;
import com.askdog.service.bo.PureNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.askdog.messaging.review.ReviewMessageInputChannel.REVIEW;
import static com.askdog.model.common.EventType.EventTypeGroup;
import static com.askdog.model.data.ReviewItem.ReviewStatus.REJECT;
import static java.util.Collections.singletonList;

@EnableBinding(ReviewMessageInputChannel.class)
public class ReviewListener {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private NotificationService notificationService;

    @StreamListener(REVIEW)
    @Transactional
    public void handleReviewResult(ReviewMessage reviewMessage) {

        ReviewItem reviewItem = reviewMessage.getPayload();
        ReviewStatus reviewStatus = reviewItem.getReviewStatus();
        EventTypeGroup resourceType = reviewItem.getResourceType();
        List<Long> targetId = singletonList(reviewItem.getTargetId());

        // TODO update the review status.\
        sendNotification(reviewMessage);
    }

    private void sendNotification(ReviewMessage reviewMessage) {
        ReviewItem reviewItem = reviewMessage.getPayload();
        ReviewStatus reviewStatus = reviewItem.getReviewStatus();

        if (reviewStatus == REJECT) {
            PureNotification pureNotification = new PureNotification();
            pureNotification.setNotificationType(NotificationType.REVIEW);
            pureNotification.setLogId(reviewItem.getId());
            notificationService.save(pureNotification);
            return;
        }

        List<ReviewItem> items = reviewRepository.findTop2ByEventTypeAndTargetIdOrderByReviewDateDesc(reviewItem.getEventType(), reviewItem.getTargetId());

        if (items.size() > 1 && items.get(1).getReviewStatus() == REJECT) {
            PureNotification pureNotification = new PureNotification();
            pureNotification.setNotificationType(NotificationType.REVIEW);
            pureNotification.setLogId(reviewItem.getId());
            notificationService.save(pureNotification);
        }
    }

}
