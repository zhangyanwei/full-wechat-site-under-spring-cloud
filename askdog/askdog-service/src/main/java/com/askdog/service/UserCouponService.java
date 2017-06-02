package com.askdog.service;

import com.askdog.model.entity.inner.usercoupon.CouponState;
import com.askdog.service.bo.admin.dashboard.CouponStatistics;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.usercoupon.UserCouponDetail;
import com.askdog.service.bo.usercoupon.UserCouponHistory;
import com.askdog.service.bo.usercoupon.UserCouponPageDetail;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient("service")
@RequestMapping("/service/coupon")
public interface UserCouponService {

    @Nonnull
    @RequestMapping(value = "/usercoupon", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    UserCouponPageDetail gain(@NotNull @RequestParam("userId") long userId,
                              @NotNull @RequestParam("couponId") long couponId);

    @Nonnull
    @RequestMapping(value = "/usercoupon", method = PUT, produces = APPLICATION_JSON_UTF8_VALUE)
    UserCouponPageDetail upgradeUserCoupon(@NotNull @RequestParam("userId") long userId,
                                           @NotNull @RequestParam("couponId") long couponId);

    @RequestMapping(value = "/usercoupon/{id}", method = DELETE)
    void deleteUserCoupon(@RequestParam("userId") long userId,
                          @PathVariable("id") long id);

    @Nonnull
    @RequestMapping(value = "/user/{id}/detail", method = GET)
    UserCouponDetail getDetail(@RequestParam(value = "userId", required = false) Long userId, @PathVariable("id") Long id);

    @Nonnull
    @RequestMapping(value = "/user/{id}/page_detail", method = GET)
    UserCouponPageDetail getPageDetail(@RequestParam("userId") long userId, @PathVariable("id") long id);

    @Nonnull
    @RequestMapping(value = "/user", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    PagedData<UserCouponPageDetail> getUserCoupons(@RequestParam(name = "userId") Long userId,
                                                   @RequestParam(value = "state", required = false) CouponState couponState,
                                                   @RequestBody() Pageable pageable);

    @RequestMapping(value = "/statistic", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    CouponStatistics couponStatistic();

    @Nonnull
    @RequestMapping(value = "/search", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    PagedData<UserCouponHistory> search(@RequestParam(name = "userId") Long userId,
                                        @RequestParam(name = "storeId") Long storeId,
                                        @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DATE_TIME) Date from,
                                        @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DATE_TIME) Date to,
                                        @RequestParam(name = "verifier", required = false) Long verifier,
                                        @RequestParam(name = "posId", required = false) String posId,
                                        @RequestBody Pageable pageable);
}
