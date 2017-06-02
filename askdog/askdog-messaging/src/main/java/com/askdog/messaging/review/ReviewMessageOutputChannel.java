package com.askdog.messaging.review;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import static com.askdog.messaging.review.ReviewMessageInputChannel.REVIEW;

public interface ReviewMessageOutputChannel {
    @Output(REVIEW)
    MessageChannel output();
}