package com.askdog.service.impl;

import com.askdog.dao.repository.CouponRepository;
import com.askdog.dao.repository.StoreRepository;
import com.askdog.dao.repository.UserCouponRepository;
import com.askdog.model.entity.Coupon;
import com.askdog.model.entity.Store;
import com.askdog.model.entity.UserCoupon;
import com.askdog.model.entity.inner.coupon.CouponType;
import com.askdog.model.entity.inner.coupon.ExpiresUnit;
import com.askdog.model.entity.inner.usercoupon.CouponState;
import com.askdog.service.CouponService;
import com.askdog.service.StoreService;
import com.askdog.service.UserCouponService;
import com.askdog.service.UserService;
import com.askdog.service.bo.CouponStatisticsDetail;
import com.askdog.service.bo.UserDetail;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.coupon.CouponDetail;
import com.askdog.service.bo.coupon.CouponDetailBasic;
import com.askdog.service.bo.coupon.CouponPageDetail;
import com.askdog.service.bo.coupon.PureCoupon;
import com.askdog.service.bo.usercoupon.UserCouponConsumeDetail;
import com.askdog.service.bo.usercoupon.UserCouponDetail;
import com.askdog.service.exception.ForbiddenException;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.impl.cell.CouponCell;
import com.askdog.service.impl.cell.UserCell;
import com.askdog.service.impl.cell.UserCouponCell;
import com.askdog.service.impl.configuration.CouponExpireConfig;
import com.askdog.service.impl.event.TriggerEvent;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.askdog.common.utils.Conditions.checkState;
import static com.askdog.model.common.EventType.VERIFY_USER_COUPON;
import static com.askdog.model.common.State.DELETED;
import static com.askdog.model.entity.inner.coupon.CouponType.FORWARDED;
import static com.askdog.model.entity.inner.coupon.CouponType.NORMAL;
import static com.askdog.model.entity.inner.usercoupon.CouponState.*;
import static com.askdog.service.bo.common.PagedDataUtils.rePage;
import static com.askdog.service.exception.ForbiddenException.Error.*;
import static com.askdog.service.exception.NotFoundException.Error.STORE;
import static com.google.common.collect.Sets.newHashSet;


@RestController
@EnableConfigurationProperties(CouponExpireConfig.class)
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;
    @Autowired
    private CouponCell couponCell;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private UserCouponCell userCouponCell;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private CouponExpireConfig couponExpireConfig;
    @Autowired private UserCell userCell;

    @Nonnull
    @Override
    public CouponDetail getDetail(@PathVariable("id") long id) {
        CouponDetailBasic couponDetailBasic = couponCell.getBasic(id);
        UserDetail userDetail = couponDetailBasic.getUpdateUserId() == null ? null : userService.findDetail(couponDetailBasic.getUpdateUserId());
        CouponDetail couponDetail = new CouponDetail();
        couponDetail.setId(couponDetailBasic.getId());
        couponDetail.setCreationTime(couponDetailBasic.getCreationTime());
        couponDetail.setLastUpdateTime(couponDetailBasic.getLastUpdateTime());
        couponDetail.setName(couponDetailBasic.getName());
        couponDetail.setCouponRule(couponDetailBasic.getCouponRule());
        couponDetail.setType(couponDetailBasic.getType());
        couponDetail.setUpdateUser(userDetail);
        couponDetail.setConsumeExpires(couponDetailBasic.getConsumeExpires());
        couponDetail.setStore(storeService.findDetail(couponDetailBasic.getStoreId(), true));
        couponDetail.setExpireTimeFrom(couponDetailBasic.getExpireTimeFrom());
        couponDetail.setExpireTimeTo(couponDetailBasic.getExpireTimeTo());
        couponDetail.setCouponDescription(couponDetailBasic.getCouponDescription());
        return couponDetail;
    }

    @Nonnull
    @Override
    public CouponPageDetail create(@RequestParam(name = "userId") Long userId, @RequestBody PureCoupon pureCoupon) {
        Long storeId = pureCoupon.getStoreId();
        checkState(userCell.findDetail(userId).isAdmin() || storeRepository.findByIdAndOwner_IdAndStateNotIn(storeId, userId, newHashSet(DELETED)).isPresent(),
                () -> new ForbiddenException(CREATE_COUPONS));
        Coupon coupon = new Coupon();
        coupon.setName(pureCoupon.getName());
        coupon.setStore(storeRepository.findById(storeId).orElseThrow(() -> new NotFoundException(STORE)));
        coupon.setRule(pureCoupon.getRule());
        coupon.setType(pureCoupon.getType());
        coupon.setCreationTime(new Date());
        coupon.setCouponDescription(pureCoupon.getCouponDescription());
        coupon.setConsumeExpiresPeriod(pureCoupon.getConsumeExpires() != null
                ? pureCoupon.getConsumeExpires().toMillis()
                : defaultExpiry());
        coupon.setExpireTimeFrom(pureCoupon.getExpireTimeFrom());
        coupon.setExpireTimeTo(pureCoupon.getExpireTimeTo());
        Coupon savedCoupon = couponRepository.save(coupon);
        return new CouponPageDetail().from(getDetail(savedCoupon.getId()));
    }

    @Override
    public void deleteCoupon(@PathVariable("id") long couponId, @RequestParam("userId") long userId) {
        boolean enableDelete = false;
        Optional<Coupon> coupon = couponRepository.findById(couponId);
        if (coupon.isPresent()) {
            Store store = coupon.get().getStore();
            enableDelete = store.getOwner().getId() == userId || store.getAgent().getOwner().getId() == userId;
        }
        checkState(enableDelete, () -> new ForbiddenException(DELETE_COUPON));
        couponRepository.delete(couponId);
        couponCell.refreshCouponDetailBasic(couponId);
    }

    @Nonnull
    @Override
    @TriggerEvent({@TriggerEvent.TriggerEventItem(performer = "userId", eventType = VERIFY_USER_COUPON, target = "userCouponId")})
    public UserCouponConsumeDetail consume(@RequestParam("userId") long userId, @PathVariable("id") long userCouponId,
                                           @RequestParam(value = "order_id", required = false) String orderId) {
        UserCouponDetail userCouponDetail = userCouponService.getDetail(userId, userCouponId);
        checkState(userCouponDetail.getCouponState() != USED, () -> new ForbiddenException(HAS_USED));
        checkState(userCouponDetail.getCouponState() != EXPIRED, () -> new ForbiddenException(EXPIRE));
        checkState(userCouponDetail.getStoreDetail().isConsumable(), () -> new ForbiddenException(CONSUME_COUPON));

        UserCoupon userCoupon = userCouponCell.findExist(userCouponId);
        userCoupon.setState(USED);
        userCoupon.setVerificationUser(userCell.findExists(userId));
        userCoupon.setOrderId(orderId);
        userCoupon.setUseTime(new Date());

        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);

        return new UserCouponConsumeDetail().from(savedUserCoupon);
    }

    @Nonnull
    @Override
    public PagedData<CouponStatisticsDetail> getCoupons(@RequestParam("userId") long userId,
                                                        @RequestParam("storeId") long storeId,
                                                        @RequestBody Pageable pageable) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NotFoundException(STORE));
        if (!userCell.isAdmin(userId)) {
            checkState(store.getOwner().getId() == userId, () -> new ForbiddenException(COUPON_lIST));
        }
        Page<Coupon> coupons = couponRepository.findByStore_Id(storeId, pageable);
        List<UserCoupon> userCoupons = userCouponRepository.findByCoupon_IdIn(coupons.getContent().stream().map(Coupon::getId).collect(Collectors.toList()));
        Map<Long, Map<CouponState, Long>> couponStatisticsMap = userCoupons.stream()
                .collect(Collectors.groupingBy(each -> each.getCoupon().getId(), Collectors.groupingBy(UserCoupon::getState, Collectors.counting())));

        return rePage(coupons, pageable, coupon -> {
            CouponStatisticsDetail couponStatisticsDetail = new CouponStatisticsDetail().from(coupon);
            Map<CouponState, Long> couponStateCountMap = couponStatisticsMap.get(coupon.getId());
            if (couponStateCountMap != null) {
                couponStateCountMap.putIfAbsent(USED, 0L);
                couponStateCountMap.putIfAbsent(NOT_USED, 0L);
                couponStatisticsDetail.setUseCount(couponStateCountMap.get(USED));
                couponStatisticsDetail.setNotUseCount(couponStateCountMap.get(NOT_USED));
            }
            return couponStatisticsDetail;
        });
    }

    @Nonnull
    @Override
    public Map<CouponType, Map<CouponState, Long>> getStoreStatistics(@RequestParam("userId") long userId,
                                                                      @RequestParam("storeId") long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NotFoundException(STORE));
        if (!userCell.isAdmin(userId)) {
            checkState(store.getOwner().getId() == userId, () -> new ForbiddenException(COUPON_lIST));
        }
        List<UserCoupon> userCouponList = userCouponRepository.findByStore_Id(storeId);
        Map<CouponType, Map<CouponState, Long>> storeStatisticsMap = userCouponList.stream()
                .collect(Collectors.groupingBy(each -> each.getCoupon().getType(), Collectors.groupingBy(UserCoupon::getState, Collectors.counting())));
        storeStatisticsMap.keySet().stream()
                .forEach(each -> {
                    Map<CouponState, Long> map = storeStatisticsMap.get(each);
                    map.putIfAbsent(USED, 0L);
                    map.putIfAbsent(NOT_USED, 0L);
                });
        storeStatisticsMap.putIfAbsent(NORMAL, ImmutableMap.of(USED, 0L, NOT_USED, 0L));
        storeStatisticsMap.putIfAbsent(FORWARDED, ImmutableMap.of(USED, 0L, NOT_USED, 0L));
        return storeStatisticsMap;
    }

    private long defaultExpiry() {
        Long value = couponExpireConfig.getValue();
        String unit = couponExpireConfig.getUnit();
        return TimeUnit.DAYS.toMillis(value * ExpiresUnit.toDay(ExpiresUnit.valueOf(unit)));
    }
}
