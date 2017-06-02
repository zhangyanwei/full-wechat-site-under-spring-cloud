package com.askdog.service;

import com.askdog.model.entity.inner.coupon.CouponType;
import com.askdog.model.entity.inner.usercoupon.CouponState;
import com.askdog.service.bo.CouponStatisticsDetail;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.coupon.CouponDetail;
import com.askdog.service.bo.coupon.CouponPageDetail;
import com.askdog.service.bo.coupon.PureCoupon;
import com.askdog.service.bo.usercoupon.UserCouponConsumeDetail;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient("service")
@RequestMapping("/service")
public interface CouponService {

    @Nonnull
    @RequestMapping(value = "/coupon", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    CouponPageDetail create(@RequestParam(name = "userId") Long userId, @RequestBody PureCoupon pureCoupon);

    @RequestMapping(value = "/coupon/{id}", method = DELETE)
    void deleteCoupon(@PathVariable("id") long id, @RequestParam("userId") long userId);

    @Nonnull
    @RequestMapping(value = "/coupon/{id}", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    CouponDetail getDetail(@PathVariable("id") long id);

    @Nonnull
    @RequestMapping(value = "/user/{id}", method = PUT)
    UserCouponConsumeDetail consume(@RequestParam("userId") long userId, @PathVariable("id") long id,
                                    @RequestParam(value="order_id", required = false) String orderId);

    @Nonnull
    @RequestMapping(value = "/coupons", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    PagedData<CouponStatisticsDetail> getCoupons(@RequestParam("userId") long userId,
                                                 @RequestParam("storeId") long storeId,
                                                 @RequestBody Pageable pageable);

    @Nonnull
    @RequestMapping(value = "/coupon/statistics/store", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    Map<CouponType, Map<CouponState, Long>> getStoreStatistics(@RequestParam("userId") long userId,
                                                               @RequestParam("storeId") long storeId);
}
