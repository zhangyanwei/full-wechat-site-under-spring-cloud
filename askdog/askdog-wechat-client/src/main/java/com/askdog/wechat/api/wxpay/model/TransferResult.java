package com.askdog.wechat.api.wxpay.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TransferResult extends ReturnValue {

    public enum ResultCode {
        SUCCESS, FAIL
    }

    private String muchAppid;
    private String muchid;
    private String deviceInfo;
    private String nonceStr;
    private ResultCode resultCode;
    private String partnerTradeNo;
    private String paymentNo;

    // TODO this maybe not right, that because the tecent just returned the formatted time string without the timezone, so we need process it.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:MM:ss")
    private Date paymentTime;

    public String getMuchAppid() {
        return muchAppid;
    }

    public void setMuchAppid(String muchAppid) {
        this.muchAppid = muchAppid;
    }

    public String getMuchid() {
        return muchid;
    }

    public void setMuchid(String muchid) {
        this.muchid = muchid;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }
}
