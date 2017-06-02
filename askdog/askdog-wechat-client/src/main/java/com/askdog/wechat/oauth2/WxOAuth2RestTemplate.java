package com.askdog.wechat.oauth2;

import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;

import java.util.Collections;

public class WxOAuth2RestTemplate extends org.springframework.security.oauth2.client.OAuth2RestTemplate {

    public WxOAuth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
        super(resource, context);
        setAccessTokenProvider(new AccessTokenProviderChain(Collections.<AccessTokenProvider>singletonList(
                new WxAuthorizationCodeAccessTokenProvider()
        )));
    }
}