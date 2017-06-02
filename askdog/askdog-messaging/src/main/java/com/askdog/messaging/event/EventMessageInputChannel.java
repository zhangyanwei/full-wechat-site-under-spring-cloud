package com.askdog.messaging.event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.simp.annotation.SubscribeMapping;

public interface EventMessageInputChannel {
    String EVENT = "event";

    @Input(EVENT)
    SubscribableChannel input();
}