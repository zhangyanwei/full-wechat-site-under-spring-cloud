package com.askdog.wechat.module.respond.impl;

import com.askdog.wechat.module.respond.MessagePassiveResponder;
import com.askdog.wechat.module.respond.MessageResponderType;
import com.askdog.wechat.module.respond.RespondException;
import com.askdog.wechat.module.respond.model.WxEventMessage;
import com.askdog.wechat.module.respond.model.WxMessage;
import com.askdog.wechat.module.respond.model.WxTextMessage;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.askdog.wechat.module.respond.model.WxMessageType.EVENT;
import static com.askdog.wechat.module.respond.model.WxMessageType.TEXT;
import static com.google.common.base.Joiner.on;

@Component
@MessageResponderType(EVENT)
public class WxEventMessageResponder implements MessagePassiveResponder {

    @Override
    public WxMessage respond(WxMessage message) throws RespondException {

        WxEventMessage eventMessage = (WxEventMessage) message;
        WxTextMessage responseMessage = new WxTextMessage();
        responseMessage.setFromUserName(eventMessage.getToUserName());
        responseMessage.setToUserName(eventMessage.getFromUserName());
        responseMessage.setCreateTime(new Date());
        responseMessage.setMessageType(TEXT);

        switch (eventMessage.getEvent()) {
            case SUBSCRIBE:
                responseMessage.setContent("欢迎关注惠券，在这里可以免费领取商家优惠券，转发商家视频更可获取翻倍优惠低值券，更多优惠正在加入中，请持续关注惠券！\n惠券，让美味更优惠！");
                break;
            default:
                responseMessage.setContent("");
                break;
        }

        return responseMessage;
    }
}
