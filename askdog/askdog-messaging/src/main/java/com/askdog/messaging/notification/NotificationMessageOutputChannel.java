package com.askdog.messaging.notification;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import static com.askdog.messaging.notification.NotificationMessageInputChannel.NOTIFICATION;

public interface NotificationMessageOutputChannel {
    @Output(NOTIFICATION)
    MessageChannel output();
}