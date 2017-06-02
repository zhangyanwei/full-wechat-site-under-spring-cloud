package com.askdog.wechat.api.wxclient.impl;

import com.askdog.wechat.api.ApiRequest;
import com.askdog.wechat.api.wxclient.CreateMenuApi;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.model.Menu;
import com.askdog.wechat.api.wxclient.model.WxResult;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.askdog.wechat.api.ApiRequestBuilder.buildPostRequest;

class CreateMenuApiImpl implements CreateMenuApi {

    private static final String API_PATH = "/menu/create";

    private URI uri;

    CreateMenuApiImpl(String accessToken, String baseUrl) {
        uri = UriComponentsBuilder.fromUriString(baseUrl)
                .path(API_PATH)
                .queryParam("access_token", accessToken)
                .build()
                .toUri();
    }

    @Override
    public WxResult request(Menu body) throws WxClientException {
        ApiRequest.ApiRequestWithBody<WxResult, Menu> apiExchange = buildPostRequest(uri, WxResult.class);
        return apiExchange.request(body);
    }
}
