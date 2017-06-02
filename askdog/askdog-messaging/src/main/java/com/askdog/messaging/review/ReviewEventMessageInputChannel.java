package com.askdog.messaging.review;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ReviewEventMessageInputChannel {
    String REVIEW_EVENT = "review_event";

    @Input(REVIEW_EVENT)
    SubscribableChannel input();
}