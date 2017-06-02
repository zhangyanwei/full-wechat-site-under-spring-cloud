package com.askdog.wechat.api.wxpay;

import com.askdog.wechat.api.ApiRequest;
import com.askdog.wechat.api.wxpay.model.PureUnifiedOrder;
import com.askdog.wechat.api.wxpay.model.UnifiedOrderResult;

public interface UnifiedorderApi extends ApiRequest.ApiRequestWithBody<UnifiedOrderResult, PureUnifiedOrder> {
}
