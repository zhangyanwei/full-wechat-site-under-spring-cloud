package com.askdog.dao.repository;

import com.askdog.model.entity.Agent;
import com.askdog.model.common.State;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface AgentRepository extends CrudRepository<Agent, Long>, JpaSpecificationExecutor<Agent> {
    Optional<Agent> findById(Long id);

    Optional<Agent> findByIdAndStateNotIn(Long agentId, Set<State> states);

    Optional<Agent> findByOwner_IdAndStateNotIn(Long userId, Set<State> states);
}
