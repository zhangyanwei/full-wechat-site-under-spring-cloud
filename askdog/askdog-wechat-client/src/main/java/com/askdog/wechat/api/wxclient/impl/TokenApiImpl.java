package com.askdog.wechat.api.wxclient.impl;

import com.askdog.wechat.api.wxclient.TokenApi;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.model.AccessToken;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.askdog.wechat.api.ApiRequestBuilder.buildGetRequest;

class TokenApiImpl implements TokenApi {

    private static final String API_PATH = "/token";

    private URI uri;

    TokenApiImpl(String baseUrl, String appId, String secret) {
        uri = UriComponentsBuilder.fromUriString(baseUrl)
                .path(API_PATH)
                .queryParam("grant_type", "client_credential")
                .queryParam("appid", appId)
                .queryParam("secret", secret)
                .build()
                .toUri();
    }

    @Override
    public AccessToken request() throws WxClientException {
        ApiRequestWithoutBody<AccessToken> apiExchange = buildGetRequest(uri, AccessToken.class);
        return apiExchange.request();
    }
}
