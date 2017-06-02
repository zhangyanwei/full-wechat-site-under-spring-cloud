package com.askdog.dao.repository.mongo;

import com.askdog.model.data.UserResidence;
import com.askdog.dao.repository.extend.ExtendedLocationRecordRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserResidenceRepository extends MongoRepository<UserResidence, String>, ExtendedLocationRecordRepository {
    Optional<UserResidence> findByTargetId(Long target);
}
