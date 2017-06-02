package com.askdog.service.bo.admin.dashboard;

public class BasicStatistics {

    private Long userCount;
    private Long wechatFollowerCount;
    private Long ipAddressCount;

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public Long getWechatFollowerCount() {
        return wechatFollowerCount;
    }

    public void setWechatFollowerCount(Long wechatFollowerCount) {
        this.wechatFollowerCount = wechatFollowerCount;
    }

    public Long getIpAddressCount() {
        return ipAddressCount;
    }

    public void setIpAddressCount(Long ipAddressCount) {
        this.ipAddressCount = ipAddressCount;
    }
}
