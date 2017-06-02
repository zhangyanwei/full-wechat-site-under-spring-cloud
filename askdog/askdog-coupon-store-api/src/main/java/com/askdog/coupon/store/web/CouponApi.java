package com.askdog.coupon.store.web;

import com.askdog.model.entity.inner.usercoupon.CouponState;
import com.askdog.model.entity.inner.coupon.CouponType;
import com.askdog.oauth.client.vo.UserInfo;
import com.askdog.service.CouponService;
import com.askdog.service.bo.*;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.coupon.CouponPageDetail;
import com.askdog.service.bo.coupon.PureCoupon;
import com.askdog.service.bo.usercoupon.UserCouponConsumeDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/coupon")
public class CouponApi {

    @Autowired
    private CouponService couponService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN', 'ROLE_STORE_EMPLOYEE')")
    @RequestMapping(value = "/{id}", method = PUT)
    public UserCouponConsumeDetail consume(@AuthenticationPrincipal UserInfo userInfo, @PathVariable("id") long id,
                                           @RequestParam(value = "order_id", required = false)  String orderId) {
        return couponService.consume(userInfo.getId(), id, orderId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN', 'ROLE_STORE_EMPLOYEE')")
    @RequestMapping(method = POST)
    public CouponPageDetail create(@AuthenticationPrincipal UserInfo userInfo, @Validated @RequestBody() PureCoupon pureCoupon) {
        return couponService.create(userInfo.getId(), pureCoupon);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN', 'ROLE_STORE_EMPLOYEE')")
    @RequestMapping(value = "/{id}", method = DELETE)
    public void delete(@AuthenticationPrincipal UserInfo userInfo, @PathVariable("id") long couponId) {
        couponService.deleteCoupon(userInfo.getId(), couponId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN', 'ROLE_STORE_EMPLOYEE')")
    @RequestMapping(value = "/statistics", method = GET)
    public Map<CouponType, Map<CouponState, Long>> storeStatistics(@AuthenticationPrincipal UserInfo userInfo, @RequestParam("storeId") long storeId) {
        return couponService.getStoreStatistics(userInfo.getId(), storeId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN', 'ROLE_STORE_EMPLOYEE')")
    @RequestMapping(method = GET)
    public PagedData<CouponStatisticsDetail> coupons(@AuthenticationPrincipal UserInfo userInfo,
                                                     @RequestParam("storeId") long storeId,
                                                     @PageableDefault(sort = "creationTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return couponService.getCoupons(userInfo.getId(), storeId, pageable);
    }

}
