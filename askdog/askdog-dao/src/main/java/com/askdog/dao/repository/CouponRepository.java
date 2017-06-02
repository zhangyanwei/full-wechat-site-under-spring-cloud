package com.askdog.dao.repository;


import com.askdog.model.entity.Coupon;
import com.askdog.model.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CouponRepository extends CrudRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {
    Optional<Coupon> findById(Long couponId);
    Page<Coupon> findByStore_Id(Long storeId, Pageable pageable);
}
