package com.askdog.service.impl.mail;

import com.askdog.model.entity.User;

import java.io.Serializable;
import java.util.Map;

public interface Mail {

    void send(User user, Map<String, Serializable> data);

    default void send(User user) {
        send(user, null);
    }
}
