package com.askdog.dao.repository.mongo;

import com.askdog.model.data.ProductAttribute;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductAttributeRepository extends MongoRepository<ProductAttribute, String> {
    ProductAttribute findByProductId(Long storeId);
}
