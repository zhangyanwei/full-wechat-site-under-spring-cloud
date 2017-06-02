package com.askdog.dao.repository.mongo;

import com.askdog.model.data.StoreSetting;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StoreSettingRepository extends MongoRepository<StoreSetting, String> {
    Optional<StoreSetting> findByStoreId(Long storeId);
}
