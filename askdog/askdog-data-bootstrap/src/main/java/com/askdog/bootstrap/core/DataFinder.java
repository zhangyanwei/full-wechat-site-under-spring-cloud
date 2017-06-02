package com.askdog.bootstrap.core;

import com.askdog.model.entity.User;

import java.util.Optional;

public interface DataFinder {
    Optional<User> findUser(String identifier);
}
