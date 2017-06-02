package com.askdog.wechat.api.wxclient.impl;

import com.askdog.wechat.api.wxclient.AddKfAccountApi;
import com.askdog.wechat.api.ApiRequest;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.model.KfAccount;
import com.askdog.wechat.api.wxclient.model.WxResult;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.askdog.wechat.api.ApiRequestBuilder.buildPostRequest;

class AddKfAccountApiImpl implements AddKfAccountApi {

    private static final String API_PATH = "/customservice/kfaccount/add";

    private URI uri;

    AddKfAccountApiImpl(String accessToken, String baseUrl) {
        uri = UriComponentsBuilder.fromUriString(baseUrl)
                .replacePath(API_PATH)
                .queryParam("access_token", accessToken)
                .build()
                .toUri();
    }

    @Override
    public WxResult request(KfAccount body) throws WxClientException {
        ApiRequest.ApiRequestWithBody<WxResult, KfAccount> apiExchange = buildPostRequest(uri, WxResult.class);
        return apiExchange.request(body);
    }

}
