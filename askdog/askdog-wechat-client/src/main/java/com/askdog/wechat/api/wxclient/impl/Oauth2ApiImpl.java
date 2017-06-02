package com.askdog.wechat.api.wxclient.impl;

import com.askdog.wechat.api.ApiRequest;
import com.askdog.wechat.api.wxclient.Oauth2Api;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.model.OauthAccessToken;

import java.net.URI;

import static com.askdog.wechat.api.ApiRequestBuilder.buildGetRequest;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

public class Oauth2ApiImpl implements Oauth2Api {

    private URI uri;

    Oauth2ApiImpl(String accessTokenUrl, String appId, String secret) {
        this.uri = fromUriString(accessTokenUrl)
                .queryParam("appid", appId)
                .queryParam("secret", secret)
                .queryParam("grant_type", "authorization_code")
                .build()
                .toUri();
    }

    @Override
    public OauthAccessToken exchange(String code) throws WxClientException {
        URI accessTokenUri = fromUri(uri).queryParam("code", code).build().toUri();
        ApiRequest.ApiRequestWithoutBody<OauthAccessToken> apiExchange = buildGetRequest(accessTokenUri, OauthAccessToken.class);
        return apiExchange.request();
    }
}
