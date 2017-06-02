package com.askdog.dao.repository;


import com.askdog.model.entity.UserCoupon;
import com.askdog.model.entity.UserCoupon_;
import com.askdog.model.entity.inner.usercoupon.CouponState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public interface UserCouponRepository extends CrudRepository<UserCoupon, Long>, JpaSpecificationExecutor<UserCoupon> {

    Page<UserCoupon> findByUser_IdAndStateAndExpireTimeAfter(long userId, CouponState couponState, Date now, Pageable pageable);

    @Query(value = "SELECT c FROM UserCoupon c INNER JOIN c.user_id u WHERE u.id = :userId ORDER BY c.state DESC , c.creation_time", nativeQuery = true)
    List<UserCoupon> findByUser_Id(@Param(value = "userId") long userId);

    Optional<UserCoupon> findById(long id);

    Optional<UserCoupon> findByUser_IdAndCoupon_IdAndStateAndExpireTimeAfter(long userId, long couponId, CouponState state, Date time);

    Page<UserCoupon> findByUser_IdOrderByStateAsc(long userId, Pageable pageable);

    List<UserCoupon> findByStore_Id(Long storeId);

    List<UserCoupon> findByCoupon_IdIn(List<Long> couponIdList);

    void deleteById(long couponId);

    @Query(value = "SELECT count(*) FROM mc_user_coupon ", nativeQuery = true)
    Long countCreatedCoupon();

    Page<UserCoupon> findByUser_IdAndExpireTimeBefore(Long userId, Date date, Pageable pageable);

    Page<UserCoupon> findAll(Pageable pageable);

    Page<UserCoupon> findAll(Specification<UserCoupon> spec, Pageable pageable);

    @Query(value = "SELECT count(*) AS coupon_count FROM mc_user_coupon WHERE creation_time <= ?1", nativeQuery = true)
    Long getCountOutOfQuery(Date date);

    @Query(value = "SELECT count(*) AS coupon_count FROM mc_user_coupon WHERE creation_time <= ?1 AND state = 'USED'", nativeQuery = true)
    Long getUsedCountOutOfQuery(Date date);

    class UserCouponSpecs {

        public static Specification<UserCoupon> useTime(Date from, Date to) {
            Instant fromTime = from != null ? from.toInstant().truncatedTo(ChronoUnit.MINUTES) : null;
            Instant toTime = to != null ? to.toInstant().truncatedTo(ChronoUnit.MINUTES) : null;
            return useTime(fromTime, toTime);
        }

        public static Specification<UserCoupon> state(CouponState state) {
            return (root, query, builder) -> builder.equal(root.get(UserCoupon_.state), state);
        }

        public static Specification<UserCoupon> verificationUser(Long verifier) {
            return (root, query, builder) -> builder.equal(root.get(UserCoupon_.verificationUser), verifier);
        }

        public static Specification<UserCoupon> orderId(String orderId) {
            return (root, query, builder) -> builder.equal(root.get(UserCoupon_.orderId), orderId);
        }

        public static Specification<UserCoupon> storeId(Long storeId) {
            return (root, query, builder) -> builder.equal(root.get(UserCoupon_.store), storeId);
        }

        public static Specification<UserCoupon> couponIn(Collection<Long> coupons) {
            return (root, query, builder) -> root.get(UserCoupon_.coupon).in(coupons);
        }

        private static Specification<UserCoupon> useTime(Instant from, Instant to) {
            checkArgument(from != null || to != null, "'from' and 'to' cannot both be null");
            if (from == null) {
                return (root, query, builder) -> builder.lessThanOrEqualTo(root.get(UserCoupon_.useTime), Date.from(to));
            }
            if (to == null) {
                return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get(UserCoupon_.useTime), Date.from(from));
            }
            return (root, query, builder) -> builder.and(
                    builder.greaterThanOrEqualTo(root.get(UserCoupon_.useTime), Date.from(from)),
                    builder.lessThanOrEqualTo(root.get(UserCoupon_.useTime), Date.from(to))
            );
        }
    }

}
