package com.askdog.sync.index.processor;

import com.askdog.messaging.index.Index;

public interface EventProcessor<E extends Index> {

    String INDEX_NAME = "askdog";

    void process(E event);
}
