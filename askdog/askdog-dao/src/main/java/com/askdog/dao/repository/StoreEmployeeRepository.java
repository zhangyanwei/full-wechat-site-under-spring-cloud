package com.askdog.dao.repository;

import com.askdog.model.entity.StoreEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface StoreEmployeeRepository extends PagingAndSortingRepository<StoreEmployee, Long> {
    Page<StoreEmployee> findByStoreId(Long storeId, Pageable pageable);

    Optional<StoreEmployee> findByStoreIdAndUserId(Long storeId, Long userId);

    int deleteByStoreIdAndUserId(Long storeId, Long userId);
}
