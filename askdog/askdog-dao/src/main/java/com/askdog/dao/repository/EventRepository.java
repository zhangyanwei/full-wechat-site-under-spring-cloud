package com.askdog.dao.repository;

import com.askdog.model.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface EventRepository extends PagingAndSortingRepository<Event, Long> {
    Page<Event> findByStore_Id(Long storeId, Pageable pageable);
    List<Event> findAllByStore_Id(Long storeId);
}
