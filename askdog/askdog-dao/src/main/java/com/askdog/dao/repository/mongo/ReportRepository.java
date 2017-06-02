package com.askdog.dao.repository.mongo;

import com.askdog.model.data.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, String> {
}
