package com.askdog.wechat.api.wxclient;

import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.model.OauthAccessToken;

public interface Oauth2Api {
    OauthAccessToken exchange(String code) throws WxClientException;
}
