package com.askdog.dao.repository.mongo.extention.impl;

import com.askdog.dao.repository.mongo.extention.ExtendedEventLogRepository;
import com.askdog.model.entity.partial.TimeBasedStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.askdog.model.common.EventType.USER_LOGIN;
import static java.time.ZoneId.systemDefault;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Fields.UNDERSCORE_ID;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class EventLogRepositoryImpl implements ExtendedEventLogRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<TimeBasedStatistics> activeUserStatistics() {
        List<Map> results = mongoTemplate.aggregate(
                newAggregation(
                        match(where("eventType").is(USER_LOGIN.name())),
                        project()
                                .andExpression("year(eventDate)").as("year")
                                .andExpression("month(eventDate)").as("month")
                                .andExpression("dayOfMonth(eventDate)").as("day")
                                .andInclude("performerId"),
                        group(fields().and("year").and("month").and("day"))
                                .addToSet("performerId").as("userId"),
                        unwind("userId"),
                        group(UNDERSCORE_ID)
                                .count().as("count")
                ),
                "mc_event_log", Map.class).getMappedResults();

        return results.stream().map(values -> {
            Integer year = (Integer) values.get("year");
            Integer month = (Integer) values.get("month");
            Integer day = (Integer) values.get("day");
            Integer count = (Integer) values.get("count");

            LocalDate localDate = LocalDate.of(year, month, day);
            Date date = Date.from(localDate.atStartOfDay().atZone(systemDefault()).toInstant());
            return new TimeBasedStatistics(date, count.longValue());
        }).collect(Collectors.toList());
    }
}
