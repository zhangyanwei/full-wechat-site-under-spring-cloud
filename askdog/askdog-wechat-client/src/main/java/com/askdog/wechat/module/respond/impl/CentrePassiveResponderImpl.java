package com.askdog.wechat.module.respond.impl;

import com.askdog.wechat.module.respond.CentrePassiveResponder;
import com.askdog.wechat.module.respond.MessagePassiveResponder;
import com.askdog.wechat.module.respond.MessageResponderType;
import com.askdog.wechat.module.respond.RespondException;
import com.askdog.wechat.module.respond.model.WxMessage;
import com.askdog.wechat.module.respond.model.WxMessageType;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Component
public class CentrePassiveResponderImpl implements CentrePassiveResponder {

    private EnumMap<WxMessageType, MessagePassiveResponder> messagePassiveResponders;

    @Autowired
    public CentrePassiveResponderImpl(List<MessagePassiveResponder> responders) {
        this.messagePassiveResponders = Maps.newEnumMap(WxMessageType.class);
        for (MessagePassiveResponder responder : responders) {
            MessageResponderType responderType = responder.getClass().getAnnotation(MessageResponderType.class);
            if (responderType != null) {
                this.messagePassiveResponders.put(responderType.value(), responder);
            }
        }
    }

    @Override
    public WxMessage respond(WxMessage message) throws RespondException {
        WxMessageType messageType = message.getMessageType();
        checkArgument(messageType != null);

        MessagePassiveResponder responder = messagePassiveResponders.get(messageType);
        return responder.respond(message);
    }
}
