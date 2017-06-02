package com.askdog.messaging.review;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import static com.askdog.messaging.review.StateChangeMessageInputChannel.STATE;

public interface StateChangeMessageOutputChannel {
    @Output(STATE)
    MessageChannel output();
}