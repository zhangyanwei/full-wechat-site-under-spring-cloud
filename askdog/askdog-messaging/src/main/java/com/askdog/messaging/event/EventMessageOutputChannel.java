package com.askdog.messaging.event;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import static com.askdog.messaging.event.EventMessageInputChannel.EVENT;

public interface EventMessageOutputChannel {
    @Output(EVENT)
    MessageChannel output();
}