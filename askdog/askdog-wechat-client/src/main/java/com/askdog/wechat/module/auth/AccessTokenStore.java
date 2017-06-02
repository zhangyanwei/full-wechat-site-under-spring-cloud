package com.askdog.wechat.module.auth;

import com.askdog.common.exception.WrappedRuntimeException;
import com.askdog.wechat.api.wxclient.WxClient;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.model.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class AccessTokenStore {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenStore.class);

    private static final String WECHAT_ACCESS_TOKEN_KEY = "wechat.token.access_token";

    @Autowired
    private StringRedisTemplate template;

    @Autowired
    private WxClient client;

    @Value("${wx.client.name:coupon}")
    private String clientName;

    private String tokenKey;

    @PostConstruct
    private void init() {
        tokenKey = WECHAT_ACCESS_TOKEN_KEY + "." + clientName;
    }

    public String get() {

        ValueOperations<String, String> opsForValue = template.opsForValue();
        if (template.hasKey(tokenKey)) {
            return opsForValue.get(tokenKey);
        }

        logger.info("refreshing access token ...");
        try {
            AccessToken accessToken = client.token().request();
            opsForValue.set(tokenKey, accessToken.getAccessToken(), accessToken.getExpiresIn(), SECONDS);

            logger.info("refreshed access token ...");
            return accessToken.getAccessToken();
        } catch (WxClientException e) {
            logger.error("can not refresh the wechat access token", e);
            throw new WrappedRuntimeException(e);
        }
    }

}
