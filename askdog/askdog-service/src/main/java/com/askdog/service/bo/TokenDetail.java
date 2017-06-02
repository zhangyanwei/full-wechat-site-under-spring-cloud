package com.askdog.service.bo;

public class TokenDetail {

    private String key;
    private long timeout;

    public TokenDetail() {
    }

    public TokenDetail(String key, long timeout) {
        this.key = key;
        this.timeout = timeout;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

}
