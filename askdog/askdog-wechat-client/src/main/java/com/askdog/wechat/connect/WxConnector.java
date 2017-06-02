package com.askdog.wechat.connect;

import com.askdog.wechat.api.wxclient.WxClient;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.model.KfAccount;
import com.askdog.wechat.api.wxclient.model.Menu;
import com.askdog.wechat.api.wxclient.model.WxResult;
import com.askdog.wechat.api.wxpay.WxPayCallback;
import com.askdog.wechat.api.wxpay.model.ReturnValue;
import com.askdog.wechat.configuration.WxConfiguration;
import com.askdog.wechat.module.auth.TicketStore;
import com.askdog.wechat.module.respond.CentrePassiveResponder;
import com.askdog.wechat.module.respond.RespondException;
import com.askdog.wechat.module.respond.model.WxMessage;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Ordering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.hash.Hashing.sha1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.TEXT_XML_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/wechat")
public class WxConnector {

    @Autowired
    private WxConfiguration configuration;

    @Autowired
    private CentrePassiveResponder responder;

    @Autowired
    private WxClient client;

    @Autowired(required = false)
    private WxPayCallback wxPayCallback;

    @Autowired
    private TicketStore ticketStore;

    @RequestMapping(method = GET)
    public String validate(
            @RequestParam(name = "signature") String signature,
            @RequestParam(name = "timestamp") String timestamp,
            @RequestParam(name = "nonce") String nonce,
            @RequestParam(name = "echostr") String echostr) {
        List<String> parameters = newArrayList(configuration.getToken(), timestamp, nonce);
        parameters.sort(Ordering.natural());

        String imploded = Joiner.on("").join(parameters);
        String hashed = sha1().hashString(imploded, Charsets.UTF_8).toString();
        if (hashed.equals(signature)) {
            return echostr;
        }

        return null;
    }

    @RequestMapping(method = POST, consumes = TEXT_XML_VALUE, produces = TEXT_XML_VALUE)
    public WxMessage receive(@RequestBody WxMessage message) throws RespondException {
        return responder.respond(message);
    }

    @RequestMapping(path = "/pay/result", method = POST, consumes = TEXT_XML_VALUE, produces = TEXT_XML_VALUE)
    public ReturnValue paid(@RequestBody Map<String, String> payResultDetails) throws WxClientException {
        return wxPayCallback.call(payResultDetails);
    }

    @RequestMapping(path = "/jsapi/config", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    public JsapiConfig getJsapiConfig(@RequestBody String pageUrl) {

        JsapiConfig jsapiConfig = new JsapiConfig();
        jsapiConfig.setAppId(configuration.getAppId());
        jsapiConfig.setTimestamp(new Date());
        jsapiConfig.setNonceStr(UUID.randomUUID().toString());

        String imploded = Joiner.on('&').withKeyValueSeparator("=")
                .join(of(
                        "jsapi_ticket", ticketStore.get(),
                        "noncestr", jsapiConfig.getNonceStr(),
                        "timestamp", jsapiConfig.getTimestamp().getTime(),
                        "url", pageUrl
                ));

        jsapiConfig.setSignature(sha1().hashString(imploded, Charsets.UTF_8).toString());
        return jsapiConfig;
    }

    @RequestMapping(path = "/menu/create", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public WxResult createMenu(@RequestBody Menu menu) throws WxClientException {
        return client.createMenu().request(menu);
    }

    @RequestMapping(path = "/kfaccount", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public WxResult addServiceAccount(@RequestBody KfAccount kfAccount) throws WxClientException {
        return client.addKfAccount().request(kfAccount);
    }

}
