package com.askdog.messaging.review;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ReviewMessageInputChannel {
    String REVIEW = "review";

    @Input(REVIEW)
    SubscribableChannel input();
}