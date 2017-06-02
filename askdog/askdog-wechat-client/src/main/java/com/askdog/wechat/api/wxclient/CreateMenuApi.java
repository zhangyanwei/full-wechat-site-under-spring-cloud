package com.askdog.wechat.api.wxclient;

import com.askdog.wechat.api.ApiRequest;
import com.askdog.wechat.api.wxclient.model.Menu;
import com.askdog.wechat.api.wxclient.model.WxResult;

public interface CreateMenuApi extends ApiRequest.ApiRequestWithBody<WxResult, Menu> {
}
