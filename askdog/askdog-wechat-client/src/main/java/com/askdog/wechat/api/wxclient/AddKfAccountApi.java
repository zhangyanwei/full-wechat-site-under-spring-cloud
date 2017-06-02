package com.askdog.wechat.api.wxclient;

import com.askdog.wechat.api.ApiRequest;
import com.askdog.wechat.api.wxclient.model.KfAccount;
import com.askdog.wechat.api.wxclient.model.WxResult;

public interface AddKfAccountApi extends ApiRequest.ApiRequestWithBody<WxResult, KfAccount> {
}
