package com.askdog.wechat.module.respond;

import com.askdog.wechat.api.wxclient.exception.WxClientException;

public class RespondException extends Exception {

    public RespondException(WxClientException cause) {
        super(cause);
    }
}
