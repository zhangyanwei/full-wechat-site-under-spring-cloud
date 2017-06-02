package com.askdog.dao.repository.mongo;

import com.askdog.model.data.AddressCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AddressCodeRepository extends MongoRepository<AddressCode, String> {

    List<AddressCode> findAll();

}
