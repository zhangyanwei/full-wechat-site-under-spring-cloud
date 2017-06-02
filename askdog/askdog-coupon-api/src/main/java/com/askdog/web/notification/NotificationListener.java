package com.askdog.web.notification;

import com.askdog.messaging.notification.NotificationMessage;
import com.askdog.messaging.notification.NotificationMessageInputChannel;
import com.askdog.model.business.Notification;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static com.askdog.common.utils.Json.writeValueAsString;
import static com.askdog.messaging.notification.NotificationMessageInputChannel.NOTIFICATION;
import static org.slf4j.LoggerFactory.getLogger;

@EnableBinding(NotificationMessageInputChannel.class)
public class NotificationListener {
    private static final Logger logger = getLogger(NotificationListener.class);

    @Autowired private SimpMessagingTemplate template;

    @StreamListener(NOTIFICATION)
    public void handle(NotificationMessage notificationMessage) {
        Notification notification = notificationMessage.getPayload();
        logger.info("[Notification] : {}", writeValueAsString(notification));

        if (notification.getRecipient() == null) {
            template.convertAndSend("/topic/notification", notification);
        } else {
            template.convertAndSendToUser(notification.getRecipient().toString(), "/topic/notification", notification);
        }

    }
}