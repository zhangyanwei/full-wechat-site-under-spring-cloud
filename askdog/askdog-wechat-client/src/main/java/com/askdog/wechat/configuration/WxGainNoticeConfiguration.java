package com.askdog.wechat.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxGainNoticeConfiguration {

    @Value("${wx.template-message.gain-coupon.templateId}")
    private String templateId;

    @Value("${wx.template-message.gain-coupon.topColor}")
    private String topColor;

    @Value("${wx.template-message.gain-coupon.url}")
    private String url;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTopColor() {
        return topColor;
    }

    public void setTopColor(String topColor) {
        this.topColor = topColor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
