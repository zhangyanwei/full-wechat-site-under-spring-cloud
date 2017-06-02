package com.askdog.messaging.notification;

import com.askdog.messaging.core.DefaultMessageSender;
import org.springframework.messaging.MessageChannel;

public class NotificationMessageSender extends DefaultMessageSender<NotificationMessage> {
    public NotificationMessageSender(MessageChannel messageChannel) {
        super(messageChannel);
    }
}