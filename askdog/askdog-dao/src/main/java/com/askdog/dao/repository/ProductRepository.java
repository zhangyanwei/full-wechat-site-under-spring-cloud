package com.askdog.dao.repository;

import com.askdog.model.entity.Product;
import com.askdog.model.common.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    Page<Product> findByStore_IdAndStateNotIn(Long storeId, Set<State> states, Pageable pageable);
    Page<Product> findByStore_Id(Long storeId, Pageable pageable);

    List<Product> findByStore_IdAndStateNotIn(Long storeId, Set<State> states);
    Optional<Product> findByIdAndStateNotIn(Long id, Set<State> states);
}
