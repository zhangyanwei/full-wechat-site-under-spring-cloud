package com.askdog.service.impl.notification;

import com.askdog.model.business.Notification;
import com.askdog.model.data.EventLog;
import com.askdog.model.data.OriginalNotification;
import com.askdog.model.data.ReviewItem;
import com.askdog.model.data.inner.NotificationType;
import com.askdog.service.EventLogService;
import com.askdog.service.ReviewService;
import com.askdog.service.helper.EventHelper;
import com.askdog.service.impl.cache.annotation.notification.NotificationCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.askdog.model.data.inner.NotificationType.EVENT;
import static com.askdog.model.data.inner.NotificationType.REVIEW;

@Component
public class NotificationHelper {

    @Autowired
    private EventHelper eventHelper;

    @Autowired
    private EventLogService eventLogService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewItemHelper reviewItemHelper;

    @NotificationCache
    public Notification convert(OriginalNotification originalNotification) {

        NotificationType notificationType = originalNotification.getNotificationType();

        Notification notification = new Notification();
        notification.setNotificationType(notificationType);
        notification.setId(originalNotification.getId());
        notification.setRecipient(originalNotification.getRecipient());
        notification.setRead(originalNotification.isRead());
        notification.setCreateDate(originalNotification.getCreateDate());

        if (notificationType == EVENT) {
            EventLog eventLog = eventLogService.findById(originalNotification.getLogId());
            notification.setContent(eventHelper.convert(eventLog.getPerformerId(), eventLog.getEventType(), eventLog.getTargetId()));

        } else if (notificationType == REVIEW) {
            ReviewItem reviewItem = reviewService.get(originalNotification.getLogId());
            notification.setContent(reviewItemHelper.convert(reviewItem));
        }

        return notification;
    }
}