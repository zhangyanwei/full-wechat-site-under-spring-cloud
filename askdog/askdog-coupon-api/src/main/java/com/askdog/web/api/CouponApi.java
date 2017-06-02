package com.askdog.web.api;


import com.askdog.model.entity.Product;
import com.askdog.service.CouponService;
import com.askdog.service.bo.usercoupon.StoreBasic;
import com.askdog.service.bo.usercoupon.UserCouponConsumeDetail;
import com.askdog.service.bo.usercoupon.UserCouponPageDetail;
import com.askdog.web.configuration.userdetails.AdUserDetails;
import com.askdog.wechat.api.wxclient.WxClient;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.model.TemplateNotice;
import com.askdog.wechat.configuration.WxVerificationNoticeConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static com.askdog.common.utils.Variables.variables;
import static com.google.common.collect.ImmutableMap.of;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/api/coupon")
public class CouponApi {

    @Autowired private CouponService couponService;
    @Autowired private WxClient wxClient;
    @Autowired private WxVerificationNoticeConfiguration wxConfig;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{id}", method = PUT)
    public UserCouponConsumeDetail consume(@AuthenticationPrincipal AdUserDetails adUserDetails,
                                           @PathVariable("id") long id,
                                           @RequestParam(value = "order_id", required = false) String orderId) {
        UserCouponConsumeDetail userCouponConsumeDetail = couponService.consume(adUserDetails.getId(), id, orderId);
        //couponNotice(adUserDetails, userCouponConsumeDetail);
        return userCouponConsumeDetail;
    }

    private void couponNotice(AdUserDetails user, UserCouponConsumeDetail userCouponConsumeDetail) {
        try {
            String openId = user.getDetails().get("openid");
            long targetId = userCouponConsumeDetail.getId();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

            Map<String, TemplateNotice.DataValue> data = new HashMap<>();
            data.put("first", new TemplateNotice.DataValue("您好，您今日的订单已经核销！", "#000000"));
            data.put("keyword1", new TemplateNotice.DataValue(String.valueOf(userCouponConsumeDetail.getId()), "#173177"));
            data.put("keyword2", new TemplateNotice.DataValue(simpleDateFormat.format(userCouponConsumeDetail.getUsageTime()), "#173177"));
            data.put("keyword3", new TemplateNotice.DataValue(user.getExternalUser().getNickname(), "#173177"));
            data.put("remark", new TemplateNotice.DataValue("请确认！", "#000000"));

            TemplateNotice templateNotice = new TemplateNotice.Builder()
                    .toUser(openId)
                    .targetId(targetId)
                    .data(data)
                    .templateId(wxConfig.getTemplateId())
                    .topColor(wxConfig.getTopColor())
                    .url(variables(of("targetId", targetId)).pattern("\\{(\\w+)\\}").replace(wxConfig.getUrl()))
                    .build();

            wxClient.couponNotice().request(templateNotice);
        } catch (WxClientException e) {
            e.printStackTrace();
        }
    }

}
