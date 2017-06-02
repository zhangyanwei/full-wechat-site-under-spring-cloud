package com.askdog.dao.repository;

import com.askdog.dao.repository.extend.StoreRepositoryExtension;
import com.askdog.model.entity.Agent;
import com.askdog.model.entity.Store;
import com.askdog.model.common.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StoreRepository extends CrudRepository<Store, Long>, StoreRepositoryExtension, JpaSpecificationExecutor<Store> {

    List<Store> findByOwner_IdAndStateNotIn(Long ownerId, Set<State> states);

    Optional<Store> findById(Long storeId);

    Optional<Store> findByIdAndStateNotIn(Long storeId, Set<State> states);

    Page<Store> findByAgentAndStateNotIn(Agent agent, Set<State> states, Pageable pageable);

    Optional<Store> findByIdAndOwner_IdAndStateNotIn(Long storeId, Long userId, Set<State> states);

    Page<Store> findAllByStateNotIn(Set<State> states, Pageable pageable);

    @Query(value = "SELECT count(*) FROM mc_store ", nativeQuery = true)
    Long countRegisteredStore();

    @Query(value = "SELECT count(*) FROM mc_store WHERE creation_time <= ?1", nativeQuery = true)
    Long getCountOutOfQuery(Date date);
}
