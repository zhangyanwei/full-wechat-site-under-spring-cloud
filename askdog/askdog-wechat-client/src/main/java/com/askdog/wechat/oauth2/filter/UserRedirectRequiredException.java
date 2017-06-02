package com.askdog.wechat.oauth2.filter;

import java.util.Map;

public class UserRedirectRequiredException extends org.springframework.security.oauth2.client.resource.UserRedirectRequiredException {

    private final String fragment;

    public UserRedirectRequiredException(String redirectUri, Map<String, String> requestParams, String fragment) {
        super(redirectUri, requestParams);
        this.fragment = fragment;
    }

    public String getFragment() {
        return fragment;
    }
}
