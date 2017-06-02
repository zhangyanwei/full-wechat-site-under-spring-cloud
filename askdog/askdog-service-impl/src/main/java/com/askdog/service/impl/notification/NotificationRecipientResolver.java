package com.askdog.service.impl.notification;

import com.askdog.model.common.EventType.EventTypeGroup;
import com.askdog.model.data.EventLog;
import com.askdog.model.data.ReviewItem;
import com.askdog.model.data.inner.NotificationType;
import com.askdog.service.EventLogService;
import com.askdog.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.askdog.model.data.inner.NotificationType.EVENT;
import static com.askdog.model.data.inner.NotificationType.REVIEW;

@Component
public class NotificationRecipientResolver {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private EventLogService eventLogService;

    public Long resolve(NotificationType notificationType, String logId) {

        if (notificationType == EVENT) {
            EventLog eventLog = eventLogService.findById(logId);
            Long targetId = eventLog.getTargetId();
            EventTypeGroup eventTypeGroup = eventLog.getEventType().getEventTypeGroup();
            return getRecipient(eventTypeGroup, targetId);
        }

        if (notificationType == REVIEW) {
            ReviewItem reviewItem = reviewService.get(logId);
            Long targetId = reviewItem.getTargetId();
            EventTypeGroup eventTypeGroup = reviewItem.getEventType().getEventTypeGroup();
            return getRecipient(eventTypeGroup, targetId);
        }

        return null;
    }

    private Long getRecipient(EventTypeGroup eventTypeGroup, Long targetId) {
        // TODO find the notification target.
        return null;
    }
}