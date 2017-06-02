package com.askdog.wechat.api.wxpay;

import java.util.Map;

public interface WxPayClient {
    UnifiedorderApi unifiedorder();
    String sign(Map<String, ?> map);
    String sign(Map<String, ?> map, boolean app);
    TransferApi transfer();
}
