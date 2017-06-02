package com.askdog.wechat.api.wxclient.impl;

import com.askdog.wechat.api.wxclient.TicketApi;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.model.Ticket;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.askdog.wechat.api.ApiRequestBuilder.buildGetRequest;

class TicketApiImpl implements TicketApi {

    private static final String API_PATH = "/ticket/getticket";

    private URI uri;

    TicketApiImpl(String accessToken, String baseUrl) {
        uri = UriComponentsBuilder.fromUriString(baseUrl)
                .path(API_PATH)
                .queryParam("access_token", accessToken)
                .queryParam("type", "jsapi")
                .build()
                .toUri();
    }

    @Override
    public Ticket request() throws WxClientException {
        ApiRequestWithoutBody<Ticket> apiExchange = buildGetRequest(uri, Ticket.class);
        return apiExchange.request();
    }
}
