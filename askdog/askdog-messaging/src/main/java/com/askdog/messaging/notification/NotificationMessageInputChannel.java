package com.askdog.messaging.notification;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface NotificationMessageInputChannel {
    String NOTIFICATION = "notification";

    @Input(NOTIFICATION)
    SubscribableChannel input();
}