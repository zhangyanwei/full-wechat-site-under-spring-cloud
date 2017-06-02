package com.askdog.dao.repository.mongo;

import com.askdog.dao.repository.mongo.extention.ProductStatisticsRepositoryExtension;
import com.askdog.model.data.ProductStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductStatisticsRepository extends MongoRepository<ProductStatistics, String>, ProductStatisticsRepositoryExtension {
    Optional<ProductStatistics> findByProductId(Long productId);
}