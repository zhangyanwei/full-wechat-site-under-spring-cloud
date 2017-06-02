package com.askdog.wechat.module.auth;

import com.askdog.common.exception.WrappedRuntimeException;
import com.askdog.wechat.api.wxclient.WxClient;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.model.Ticket;
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
public class TicketStore {

    private static final Logger logger = LoggerFactory.getLogger(TicketStore.class);

    private static final String WECHAT_TICKET_KEY = "wechat.token.ticket";

    @Autowired
    private StringRedisTemplate template;

    @Autowired
    private WxClient client;

    @Value("${wx.client.name:coupon}")
    private String clientName;

    private String tokenKey;

    @PostConstruct
    private void init() {
        tokenKey = WECHAT_TICKET_KEY + "." + clientName;
    }

    public String get() {

        ValueOperations<String, String> opsForValue = template.opsForValue();
        if (template.hasKey(tokenKey)) {
            return opsForValue.get(tokenKey);
        }

        logger.info("refreshing ticket token ...");
        try {
            Ticket ticket = client.ticket().request();
            opsForValue.set(tokenKey, ticket.getTicket(), ticket.getExpiresIn(), SECONDS);

            logger.info("refreshed ticket token ...");
            return ticket.getTicket();
        } catch (WxClientException e) {
            logger.error("can not refresh the wechat ticket", e);
            throw new WrappedRuntimeException(e);
        }
    }

}
