package com.askdog.web.configuration.oauth2.clients;

import com.askdog.web.configuration.handler.WebAuthenticationSuccessHandler;
import com.askdog.wechat.api.wxclient.WxClient;
import com.askdog.wechat.oauth2.WxOAuth2RestTemplate;
import com.askdog.wechat.oauth2.WxResourceTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.RememberMeServices;

public class WechatClientResources extends DefaultClientResources {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Autowired
    private WebAuthenticationSuccessHandler webAuthenticationSuccessHandler;

    @Autowired
    private WxClient wxClient;

    @Autowired
    @Qualifier("oauth2")
    private RememberMeServices rememberMeServices;

    @Override
    public OAuth2ClientAuthenticationProcessingFilter filter(String path) {
        OAuth2ClientAuthenticationProcessingFilter filter = new AdOAuth2ClientAuthenticationProcessingFilter(path);
        filter.setRestTemplate(new WxOAuth2RestTemplate(getClient(), oAuth2ClientContext));
        filter.setTokenServices(new WxResourceTokenServices(wxClient, filter.restTemplate, getClient()));
        filter.setRememberMeServices(rememberMeServices);
        filter.setAuthenticationSuccessHandler(webAuthenticationSuccessHandler);
        return filter;
    }

}