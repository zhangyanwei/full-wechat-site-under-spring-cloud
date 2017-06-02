package com.askdog.wechat.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "wx-pay", name = "callback-url")
public class WxPayConfiguration {

    @Value("${wx-pay.callback-url}")
    private String callbackUrl;

    @Value("${wx-pay.unifiedorder-url}")
    private String unifiedorderUrl;

    @Value("${wx-pay.transfer-url}")
    private String transferUrl;

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public String getUnifiedorderUrl() {
        return unifiedorderUrl;
    }

    public String getTransferUrl() {
        return transferUrl;
    }

    @Bean
    @ConfigurationProperties(prefix = "wx-pay.web")
    @ConditionalOnProperty(prefix = "wx-pay.web", name = "mchid")
    public MchConfiguration getWeb() {
        return new MchConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = "wx-pay.app")
    @ConditionalOnProperty(prefix = "wx-pay.app", name = "mchid")
    public MchConfiguration getApp() {
        return new MchConfiguration();
    }

    public static class MchConfiguration {

        private String certificationFile;
        private String appid;
        private String mchid;
        private String key;

        public String getCertificationFile() {
            return certificationFile;
        }

        public void setCertificationFile(String certificationFile) {
            this.certificationFile = certificationFile;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getMchid() {
            return mchid;
        }

        public void setMchid(String mchid) {
            this.mchid = mchid;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
