package com.askdog.dao.repository.mongo;

import com.askdog.model.data.UserLocation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserLocationRepository extends MongoRepository<UserLocation, String> {
}
