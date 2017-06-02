package com.askdog.coupon.store.web;

import com.askdog.coupon.store.web.excelsheet.ExcelViewBuilder;
import com.askdog.oauth.client.vo.UserInfo;
import com.askdog.service.UserCouponService;
import com.askdog.service.UserService;
import com.askdog.service.bo.BasicInnerUser;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.usercoupon.UserCouponHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.joda.LocalDateTimeParser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/usercoupons")
public class UserCouponApi {

    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN', 'ROLE_STORE_EMPLOYEE')")
    @RequestMapping(path = "/history", method = GET)
    public PagedData<UserCouponHistory> usercoupons(@AuthenticationPrincipal UserInfo userInfo,
                                                    @RequestParam(name = "storeId") Long storeId,
                                                    @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyyMMddHHmm") Date from,
                                                    @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyyMMddHHmm") Date to,
                                                    @RequestParam(name = "verifier", required = false) Long verifier,
                                                    @RequestParam(name = "posId", required = false) String posId,
                                                    @PageableDefault(sort = "useTime", direction = DESC) Pageable pageable) {
        return userCouponService.search(userInfo.getId(), storeId, from, to, verifier, posId, pageable);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN', 'ROLE_STORE_EMPLOYEE')")
    @RequestMapping(value = "/history/excel", method = GET)
    public ModelAndView usercouponsExcel(@AuthenticationPrincipal UserInfo userInfo,
                                         @RequestParam(name = "storeId") Long storeId,
                                         @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyyMMddHHmm") Date from,
                                         @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyyMMddHHmm") Date to,
                                         @RequestParam(name = "verifier", required = false) Long verifier,
                                         @RequestParam(name = "posId", required = false) String posId,
                                         @PageableDefault(sort = "useTime", direction = DESC, value = Integer.MAX_VALUE) Pageable pageable) {
        Map<String, Object> model = new HashMap<>();
        List<UserCouponHistory> userCouponHistories = usercoupons(userInfo, storeId, from, to, verifier, posId, pageable).getResult();
        String verifierName = "";
        if (verifier != null) {
            Optional<BasicInnerUser> user = userService.findById(verifier);
            if (user.isPresent()) verifierName = user.get().getNickname();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        if (from != null) model.put("from", simpleDateFormat.format(from));
        if (to != null) model.put("to", simpleDateFormat.format(to));
        /**the key "sheetList" in inject object model type must be List<UserCouponHistory>*/
        model.put("sheetList", userCouponHistories);
        model.put("verifier", verifierName);
        model.put("posId", posId);
        ExcelViewBuilder excelViewBuilder = new ExcelViewBuilder();
        return new ModelAndView(excelViewBuilder, model);
    }
}
