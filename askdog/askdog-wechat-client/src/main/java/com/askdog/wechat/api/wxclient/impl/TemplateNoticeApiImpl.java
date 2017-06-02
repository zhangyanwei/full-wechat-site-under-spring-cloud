package com.askdog.wechat.api.wxclient.impl;

import com.askdog.wechat.api.ApiRequest;
import com.askdog.wechat.api.wxclient.TemplateNoticeApi;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.model.TemplateNotice;
import com.askdog.wechat.api.wxclient.model.WxResult;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.askdog.wechat.api.ApiRequestBuilder.buildPostRequest;

class TemplateNoticeApiImpl implements TemplateNoticeApi {

    private static final String API_PATH = "/message/template/send";

    private URI uri;

    TemplateNoticeApiImpl(String accessToken, String baseUrl) {
        uri = UriComponentsBuilder.fromUriString(baseUrl)
                .path(API_PATH)
                .queryParam("access_token", accessToken)
                .build()
                .toUri();
    }

    @Override
    public WxResult request(TemplateNotice body) throws WxClientException {
        ApiRequest.ApiRequestWithBody<WxResult, TemplateNotice> apiExchange = buildPostRequest(uri, WxResult.class);
        return apiExchange.request(body);
    }
}
