package com.askdog.wechat.api.wxclient.impl;

import com.askdog.wechat.api.wxclient.UserInfoApi;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.model.UserInfo;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.askdog.wechat.api.ApiRequestBuilder.buildGetRequest;

class UserInfoApiImpl implements UserInfoApi {

    private static final String API_PATH = "/cgi-bin/user/info";
    private static final String SNS_API_PATH = "/sns/userinfo";

    private URI uri;

    UserInfoApiImpl(String accessToken, String baseUrl, String openid) {
        this(accessToken, baseUrl, openid, false);
    }

    UserInfoApiImpl(String accessToken, String baseUrl, String openid, boolean sns) {
        uri = UriComponentsBuilder.fromUriString(baseUrl)
                .replacePath(sns ? SNS_API_PATH : API_PATH)
                .queryParam("access_token", accessToken)
                .queryParam("openid", openid)
                .queryParam("lang", "zh_CN")
                .build()
                .toUri();
    }

    @Override
    public UserInfo request() throws WxClientException {
        ApiRequestWithoutBody<UserInfo> apiExchange = buildGetRequest(uri, UserInfo.class);
        return apiExchange.request();
    }
}
