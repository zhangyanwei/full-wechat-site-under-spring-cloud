package com.askdog.dao.repository.mongo.extention;

import com.askdog.model.entity.partial.TimeBasedStatistics;

import java.util.List;

public interface ExtendedEventLogRepository {
    List<TimeBasedStatistics> activeUserStatistics();
}
