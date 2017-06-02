package com.askdog.messaging.index;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import static com.askdog.messaging.index.IndexMessageInputChannel.INDEX;

public interface IndexMessageOutputChannel {
    @Output(INDEX)
    MessageChannel output();
}