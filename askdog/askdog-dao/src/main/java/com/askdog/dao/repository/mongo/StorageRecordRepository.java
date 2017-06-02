package com.askdog.dao.repository.mongo;

import com.askdog.model.data.StorageRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StorageRecordRepository extends MongoRepository<StorageRecord, Long> {
    StorageRecord findByResourceId(Long linkId);
}
