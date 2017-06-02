package com.askdog.wechat.api.wxpay;

import com.askdog.wechat.api.ApiRequest;
import com.askdog.wechat.api.wxpay.model.TransferRequest;
import com.askdog.wechat.api.wxpay.model.TransferResult;

public interface TransferApi extends ApiRequest.ApiRequestWithBody<TransferResult, TransferRequest> {
}
