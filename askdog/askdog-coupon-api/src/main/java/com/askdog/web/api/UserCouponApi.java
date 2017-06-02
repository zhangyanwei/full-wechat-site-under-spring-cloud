package com.askdog.web.api;

import com.askdog.model.entity.inner.usercoupon.CouponState;
import com.askdog.service.UserCouponService;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.usercoupon.StoreBasic;
import com.askdog.service.bo.usercoupon.UserCouponPageDetail;
import com.askdog.service.exception.ServiceException;
import com.askdog.web.configuration.userdetails.AdUserDetails;
import com.askdog.web.vo.UserCouponRequest;
import com.askdog.wechat.api.wxclient.WxClient;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.model.TemplateNotice;
import com.askdog.wechat.configuration.WxGainNoticeConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static com.askdog.common.utils.Variables.variables;
import static com.askdog.wechat.api.wxclient.model.TemplateNotice.DataValue;
import static com.google.common.collect.ImmutableMap.of;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/usercoupons")
public class UserCouponApi {

    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private WxGainNoticeConfiguration wxConfig;

    @Autowired
    private WxClient wxClient;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = GET)
    public PagedData<UserCouponPageDetail> getUserCoupons(@AuthenticationPrincipal AdUserDetails user,
                                                          @RequestParam(value = "state", required = false) CouponState couponState,
                                                          @PageableDefault(sort = "creationTime", direction = DESC) Pageable pageable) throws ServiceException {
        return userCouponService.getUserCoupons(user.getId(), couponState, pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = POST)
    public UserCouponPageDetail gain(@AuthenticationPrincipal AdUserDetails user, @NotNull @RequestBody UserCouponRequest userCouponRequest) {
        UserCouponPageDetail userCouponPageDetail = userCouponService.gain(user.getId(), userCouponRequest.getCouponId());
        //couponNotice(user, userCouponPageDetail);
        return userCouponPageDetail;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = PUT)
    public UserCouponPageDetail upgradeUserCoupon(@AuthenticationPrincipal AdUserDetails user, @NotNull @RequestBody UserCouponRequest userCouponRequest) {
        UserCouponPageDetail userCouponPageDetail = userCouponService.upgradeUserCoupon(user.getId(), userCouponRequest.getCouponId());
        //couponNotice(user, userCouponPageDetail);
        return userCouponPageDetail;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{id}", method = GET)
    public UserCouponPageDetail getUserCouponDetail(@AuthenticationPrincipal AdUserDetails user, @PathVariable("id") Long userCouponId) {
        return userCouponService.getPageDetail(user.getId(), userCouponId);
    }

    private void couponNotice(AdUserDetails user, UserCouponPageDetail userCouponPageDetail) {
        try {
            String openId = user.getDetails().get("openid");
            StoreBasic storeBasic = userCouponPageDetail.getStoreBasic();
            long targetId = userCouponPageDetail.getId();

            Map<String, DataValue> data = new HashMap<>();
            data.put("first", new DataValue("尊敬的" + user.getExternalUser().getNickname() + ": 优惠券已经放入您的账户中，点击\"优惠券\"即可查看。 ", "#000000"));
            data.put("store", new DataValue(storeBasic.getName(), "#173177"));
            data.put("tel", new DataValue(storeBasic.getPhone(), "#173177"));
            data.put("address", new DataValue(storeBasic.getAddress(), "#173177"));
            data.put("expireTime", new DataValue(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(userCouponPageDetail.getExpireTime()), "#173177"));
            data.put("discount", new DataValue("￥" + userCouponPageDetail.getRule(), "#173177"));
            data.put("remark", new DataValue("使用说明：最终解释权归商户所有。为商户点赞，让我们服务更好。", "#000000"));

            TemplateNotice templateNotice = new TemplateNotice.Builder()
                    .toUser(openId)
                    .targetId(userCouponPageDetail.getId())
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
