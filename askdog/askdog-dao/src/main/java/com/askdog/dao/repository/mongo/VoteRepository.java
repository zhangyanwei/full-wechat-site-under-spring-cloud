package com.askdog.dao.repository.mongo;

import com.askdog.model.data.Actions.VoteAction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VoteRepository extends MongoRepository<VoteAction, String> {
    Optional<VoteAction> findByUserAndTargetId(Long userId, Long targetId);

    long deleteByUserAndTargetId(Long userId, Long target);
}