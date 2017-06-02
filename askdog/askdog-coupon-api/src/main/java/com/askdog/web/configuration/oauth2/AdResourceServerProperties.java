package com.askdog.web.configuration.oauth2;

public class AdResourceServerProperties {

    private String userIdUri;
    private String userInfoUri;

    String getUserIdUri() {
        return userIdUri;
    }

    public void setUserIdUri(String userIdUri) {
        this.userIdUri = userIdUri;
    }

    public String getUserInfoUri() {
        return userInfoUri;
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }
}