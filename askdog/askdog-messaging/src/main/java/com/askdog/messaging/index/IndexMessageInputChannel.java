package com.askdog.messaging.index;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface IndexMessageInputChannel {
    String INDEX = "index";

    @Input(INDEX)
    SubscribableChannel input();
}