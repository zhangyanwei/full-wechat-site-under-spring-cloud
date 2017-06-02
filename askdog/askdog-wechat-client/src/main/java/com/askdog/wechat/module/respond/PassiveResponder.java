package com.askdog.wechat.module.respond;

import com.askdog.wechat.module.respond.model.WxMessage;

public interface PassiveResponder {
    WxMessage respond(WxMessage message) throws RespondException;
}
