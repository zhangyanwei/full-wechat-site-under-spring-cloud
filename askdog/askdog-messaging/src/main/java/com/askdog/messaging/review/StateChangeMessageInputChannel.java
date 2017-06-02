package com.askdog.messaging.review;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface StateChangeMessageInputChannel {
    String STATE = "stage";

    @Input(STATE)
    SubscribableChannel input();
}