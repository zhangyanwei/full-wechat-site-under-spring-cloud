package com.askdog.service.impl;

import com.askdog.messaging.event.Event;
import com.askdog.messaging.event.EventMessage;
import com.askdog.messaging.event.EventMessageSender;
import com.askdog.messaging.notification.NotificationMessage;
import com.askdog.messaging.notification.NotificationMessageSender;
import com.askdog.messaging.review.ReviewMessage;
import com.askdog.messaging.review.ReviewMessageSender;
import com.askdog.model.business.Notification;
import com.askdog.model.common.EventType;
import com.askdog.model.data.ReviewItem;
import com.askdog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageServiceImpl implements MessageService {

    @Autowired
    private EventMessageSender eventMessageSender;

    @Autowired
    private NotificationMessageSender notificationMessageSender;

    @Autowired
    private ReviewMessageSender reviewMessageSender;

    @Override
    public void sendEventMessage(@RequestParam(value = "performerId", required = false) Long performerId,
                                 @RequestParam(value = "eventType", required = false) EventType eventType,
                                 @RequestParam(value = "targetId", required = false) Long targetId) {
        Event event = new Event();
        event.setPerformerId(performerId);
        event.setEventType(eventType);
        event.setTargetId(targetId);

        EventMessage eventMessage = new EventMessage();
        eventMessage.setPayload(event);

        eventMessageSender.send(eventMessage);
    }

    @Override
    public void sendNotificationMessage(@RequestBody Notification notification) {
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setPayload(notification);
        notificationMessageSender.send(notificationMessage);
    }

    @Override
    public void sendReviewMessage(@RequestBody ReviewItem reviewItem) {
        ReviewMessage reviewMessage = new ReviewMessage();
        reviewMessage.setPayload(reviewItem);
        reviewMessageSender.send(reviewMessage);
    }
}
