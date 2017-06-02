package com.askdog.wechat.api.wxpay.model;

import com.askdog.wechat.api.WxObject;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("xml")
public class ReturnValue implements WxObject {

    public enum Code {
        SUCCESS, FAIL
    }

    private Code returnCode;
    private String returnMsg;
    private String errCode;
    private String errCodeDes;

    public Code getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Code returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }
}
