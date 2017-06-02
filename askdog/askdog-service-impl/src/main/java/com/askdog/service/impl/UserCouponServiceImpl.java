package com.askdog.service.impl;

import com.askdog.dao.repository.UserCouponRepository;
import com.askdog.dao.repository.extend.CouponStatisticsRepository;
import com.askdog.dao.repository.mongo.StoreSettingRepository;
import com.askdog.model.entity.*;
import com.askdog.model.entity.inner.usercoupon.CouponState;
import com.askdog.model.entity.partial.CouponTimeAndStateStatistics;
import com.askdog.model.entity.partial.TimeBasedStatistics;
import com.askdog.service.*;
import com.askdog.service.bo.admin.dashboard.CouponStatistics;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.coupon.CouponDetail;
import com.askdog.service.bo.usercoupon.UserCouponBasic;
import com.askdog.service.bo.usercoupon.UserCouponDetail;
import com.askdog.service.bo.usercoupon.UserCouponHistory;
import com.askdog.service.bo.usercoupon.UserCouponPageDetail;
import com.askdog.service.exception.ConflictException;
import com.askdog.service.exception.ConflictException.Error;
import com.askdog.service.exception.IllegalArgumentException;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.impl.cell.*;
import com.askdog.service.impl.configuration.CouponExpireConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static com.askdog.common.utils.Conditions.checkState;
import static com.askdog.dao.repository.UserCouponRepository.UserCouponSpecs.*;
import static com.askdog.model.entity.inner.coupon.CouponType.FORWARDED;
import static com.askdog.model.entity.inner.usercoupon.CouponState.*;
import static com.askdog.service.bo.common.PagedDataUtils.rePage;
import static com.askdog.service.exception.IllegalArgumentException.Error.INVALID_FORWARD_COUPON;
import static com.askdog.service.exception.NotFoundException.Error.COUPON;
import static com.askdog.service.exception.NotFoundException.Error.USER_COUPON;
import static com.askdog.service.utils.StatisticsUtils.*;
import static java.time.LocalDateTime.now;
import static java.time.ZoneId.systemDefault;
import static org.springframework.data.jpa.domain.Specifications.where;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@RestController
@EnableConfigurationProperties(CouponExpireConfig.class)
public class UserCouponServiceImpl implements UserCouponService {

    @Autowired private UserCell userCell;
    @Autowired private UserService userService;
    @Autowired private UserCouponRepository userCouponRepository;
    @Autowired private UserCouponCell userCouponCell;
    @Autowired private StoreService storeService;
    @Autowired private CouponCell couponCell;
    @Autowired private CouponService couponService;
    @Autowired private CouponStatisticsRepository couponStatisticsRepository;
    @Autowired private CouponExpireConfig couponExpireConfig;
    @Autowired private StoreSettingRepository storeSettingRepository;

    @Nonnull
    @Override
    public UserCouponDetail getDetail(@RequestParam(value = "userId", required = false) Long userId, @PathVariable("id") Long id) {
        UserCouponBasic userCouponBasic = userCouponCell.getBasic(id);
        CouponDetail couponDetail = couponService.getDetail(userCouponBasic.getCouponId());
        Long storeId = couponDetail.getStore().getId();

        UserCouponDetail userCouponDetail = new UserCouponDetail();
        userCouponDetail.setId(userCouponBasic.getId());
        userCouponDetail.setCreationTime(userCouponBasic.getCreationTime());
        userCouponDetail.setCouponState(userCouponBasic.getState());
        userCouponDetail.setUseTime(userCouponBasic.getUseTime());
        userCouponDetail.setCouponDetail(couponDetail);
        userCouponDetail.setUserDetail(userService.findDetail(userCouponBasic.getUserId()));
        userCouponDetail.setStoreDetail(storeService.findDetailWithState(userId, storeId, true));
        userCouponDetail.setExpireTime(userCouponBasic.getExpireTime());

        if (userCouponBasic.getVerificationUserId() != null) {
            userCouponDetail.setVerificationUser(userService.findDetail(userCouponBasic.getVerificationUserId()));
        }

        userCouponDetail.setOrderId(userCouponBasic.getOrderId());
        storeSettingRepository.findByStoreId(storeId).ifPresent((setting) -> {
            userCouponDetail.setRequireOrderId(setting.isRequirePosId());
        });

        ZonedDateTime expireTime = userCouponBasic.getExpireTime().toInstant().atZone(systemDefault());
        if (userCouponBasic.getState() == NOT_USED && now().isAfter(expireTime.toLocalDateTime())) {
            userCouponDetail.setCouponState(EXPIRED);
        }
        return userCouponDetail;
    }

    @Nonnull
    @Override
    public UserCouponPageDetail getPageDetail(@RequestParam("userId") long userId, @PathVariable("id") long id) {
        return new UserCouponPageDetail().from(getDetail(userId, id));
    }

    @Nonnull
    @Override
    public UserCouponPageDetail gain(@NotNull @RequestParam("userId") long userId,
                                     @NotNull @RequestParam("couponId") long couponId) {
        return createUserCoupon(userId, couponId, NOT_USED);
    }

    @Nonnull
    @Override
    public UserCouponPageDetail upgradeUserCoupon(@NotNull @RequestParam("userId") long userId,
                                                  @NotNull @RequestParam("couponId") long couponId) {
        Coupon coupon = couponCell.findExist(couponId);
        checkState(coupon.getType() == FORWARDED, () -> new IllegalArgumentException(INVALID_FORWARD_COUPON));

        return createUserCoupon(userId, couponId, NOT_USED);
    }

    @Override
    public void deleteUserCoupon(@RequestParam("userId") long userId, @PathVariable("id") long userCouponId) {
        UserCouponBasic userCouponBasic = userCouponCell.getBasic(userCouponId);
        //TODO instead by deletable of detail & logic deletion
        checkState(userCouponBasic.getUserId().equals(userId), () -> new NotFoundException(USER_COUPON));
        userCouponRepository.deleteById(userCouponId);
    }

    @Nonnull
    @Override
    public PagedData<UserCouponPageDetail> getUserCoupons(@RequestParam(name = "userId") Long userId,
                                                          @RequestParam(value = "state", required = false) CouponState couponState,
                                                          @RequestBody() Pageable pageable) {
        Page<UserCoupon> userCoupons;
        if (couponState == null) {
            userCoupons = userCouponRepository.findByUser_IdOrderByStateAsc(userId, pageable);
        } else if (couponState == EXPIRED) {
            userCoupons = userCouponRepository.findByUser_IdAndExpireTimeBefore(userId, new Date(), pageable);
        } else {
            userCoupons = userCouponRepository.findByUser_IdAndStateAndExpireTimeAfter(userId, couponState, new Date(), pageable);
        }

        return rePage(userCoupons, pageable,
                coupon -> new UserCouponPageDetail().from(getDetail(userId, coupon.getId())));
    }

    @Override
    public CouponStatistics couponStatistic() {
        CouponStatistics couponStatistics = new CouponStatistics();
        couponStatistics.setTotalCouponCount(userCouponRepository.countCreatedCoupon());
        List<CouponTimeAndStateStatistics> couponUnionStatistics = couponStatisticsRepository.couponGainUnionStatistics("day", "1 years");
        couponStatistics.setCouponCreationTrend(overlyingForCoupon(union(couponUnionStatistics)));
        return couponStatistics;
    }

    @Nonnull
    @Override
    public PagedData<UserCouponHistory> search(@RequestParam(name = "userId") Long userId,
                                               @RequestParam(name = "storeId") Long storeId,
                                               @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DATE_TIME) Date from,
                                               @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DATE_TIME) Date to,
                                               @RequestParam(name = "verifier", required = false) Long verifier,
                                               @RequestParam(name = "posId", required = false) String posId,
                                               @RequestBody Pageable pageable) {

        Specifications<UserCoupon> specifications = where(state(USED)).and(storeId(storeId));
        if (from != null || to != null) {
            specifications = specifications.and(useTime(from, to));
        }
        if (verifier != null) {
            specifications = specifications.and(verificationUser(verifier));
        }
        if (posId != null) {
            specifications = specifications.and(orderId(posId));
        }

        Page<UserCoupon> userCoupons = userCouponRepository.findAll(specifications, pageable);

        return rePage(userCoupons, pageable, coupon -> {
            UserCouponHistory history = new UserCouponHistory();
            history.setUserCouponId(coupon.getId());
            history.setOrderId(coupon.getOrderId());
            history.setOwner(userCell.findDetail(coupon.getUser().getId()).getNickname());

            User verificationUser = coupon.getVerificationUser();
            if (verificationUser != null) {
                history.setVerifier(userCell.findDetail(verificationUser.getId()).getNickname());
            }
            history.setVerifyTime(coupon.getUseTime());
            history.setRule(coupon.getCoupon().getRule());
            history.setCouponState(USED);
            return history;
        });
    }

    private UserCouponPageDetail createUserCoupon(Long userId, Long couponId, CouponState state) {
        Optional<UserCoupon> notUsedCoupon = userCouponCell.findNotUsedUserCoupon(userId, couponId);
        checkState(!notUsedCoupon.isPresent(), () -> new ConflictException(Error.USER_COUPON));

        User user = userCell.findExists(userId);
        Coupon coupon = couponCell.findExist(couponId);
        Store store = coupon.getStore();
        Long expirePeriod = coupon.getConsumeExpiresPeriod() == null ? TimeUnit.DAYS.toMillis(couponExpireConfig.getValue()) : coupon.getConsumeExpiresPeriod();
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUser(user);
        userCoupon.setCoupon(coupon);
        userCoupon.setState(state);
        userCoupon.setCreationTime(new Date());
        userCoupon.setStore(store);

        long expireTime = new Date().getTime() + expirePeriod;
        long expireTimeTo = userCoupon.getCoupon().getExpireTimeTo().getTime();
        userCoupon.setExpireTime(new Date(expireTime < expireTimeTo ? expireTime : expireTimeTo));

        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);

        return new UserCouponPageDetail().from(getDetail(user.getId(), savedUserCoupon.getId()));
    }

    private Coupon get(List<Coupon> coupons, Predicate<Coupon> predicate) {
        Assert.notNull(coupons);
        return coupons.stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(COUPON));
    }

    private void getCompleteResult(List<TimeBasedStatistics> list, Long beforeCount) {
        Long firstCount = list.get(0).getCount();
        list.get(0).setCount(beforeCount + firstCount);
    }

}
