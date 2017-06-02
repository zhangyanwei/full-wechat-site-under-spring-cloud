package com.askdog.wechat.api.wxclient.exception;

import com.askdog.common.exception.AbstractException;
import com.askdog.wechat.api.wxclient.model.WxResult;

public class WxClientException extends AbstractException {

    public enum Error {
        PAY_ERROR
    }

    public WxClientException(Error code) {
        super(code);
    }

    public WxClientException(Error code, String message) {
        super(code, message);
    }

    public WxClientException(WxResult result) {
        super(Integer.toString(result.getCode()), result.getMessage());
    }

    @Override
    protected String messageResourceBaseName() {
        return "exception.client";
    }

    @Override
    protected String moduleName() {
        return "WX";
    }
}
