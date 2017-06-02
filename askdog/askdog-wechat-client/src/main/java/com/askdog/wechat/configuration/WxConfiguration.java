package com.askdog.wechat.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxConfiguration {

    @Value("${wx.client.clientId}")
    private String appId;

    @Value("${wx.client.clientSecret}")
    private String appSecret;

    @Value("${wx.base-url:https://api.weixin.qq.com/cgi-bin}")
    private String baseUrl;

    @Value("${wx.token:token}")
    private String token;

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getToken() {
        return token;
    }

}
