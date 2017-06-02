package com.askdog.sync;

import com.askdog.messaging.index.IndexMessage;
import com.askdog.messaging.index.IndexMessageInputChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import static com.askdog.messaging.index.IndexMessageInputChannel.INDEX;

@EnableBinding(IndexMessageInputChannel.class)
public class DataEventReceiver {

    private static final Logger logger = LoggerFactory.getLogger(DataEventReceiver.class);

    @StreamListener(INDEX)
    public void receive(IndexMessage indexMessage) {

    }

}
