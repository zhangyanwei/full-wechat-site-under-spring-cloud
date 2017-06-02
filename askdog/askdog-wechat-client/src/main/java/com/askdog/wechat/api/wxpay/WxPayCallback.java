package com.askdog.wechat.api.wxpay;

import com.askdog.wechat.api.wxpay.model.ReturnValue;

import java.util.Map;

public interface WxPayCallback {
    ReturnValue call(Map<String, String> payResultDetails);
}
