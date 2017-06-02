package com.askdog.dao.repository.extend.impl;

import com.askdog.dao.repository.extend.ExtendedLocationRecordRepository;
import com.askdog.model.data.UserLocation;
import com.askdog.model.data.UserResidence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.util.List;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class UserResidenceRepositoryImpl implements ExtendedLocationRecordRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    private void init() {
        mongoTemplate.indexOps(UserLocation.class).ensureIndex(new GeospatialIndex("geometry").typed(GeoSpatialIndexType.GEO_2DSPHERE));
        mongoTemplate.indexOps(UserLocation.class).ensureIndex(new Index("creationDate", DESC).expire(365, DAYS));
        mongoTemplate.indexOps(UserResidence.class).ensureIndex(new GeospatialIndex("geometry").typed(GeoSpatialIndexType.GEO_2DSPHERE));
        mongoTemplate.indexOps(UserResidence.class).ensureIndex(new Index("creationDate", DESC).expire(7, DAYS));
    }

    @Override
    @Nonnull
    public List<UserLocation> findNearbyUsers(@Nonnull Long userId, @Nonnull Double lat, @Nonnull Double lng, double nearbyDistance, long nearbyLimit) {
        return mongoTemplate.aggregate(
                newAggregation(
                        geoNear(NearQuery.near(new Point(lng, lat)).maxDistance(new Distance(nearbyDistance, Metrics.KILOMETERS)).spherical(true), "geometry"),
                        match(Criteria.where("target").ne(userId)),
                        group("target"),
                        limit(nearbyLimit)
                ),
                "userLocation",
                UserLocation.class).getMappedResults();
    }


    @Override
    @Nonnull
    public List<UserResidence> findUserLocation(@Nonnull Long userId, long recentLimit) {
        return mongoTemplate.aggregate(
                newAggregation(
                        match(Criteria.where("target").is(userId)),
                        sort(DESC, "creationDate"),
                        limit(recentLimit)
                ),
                "userLocation",
                UserResidence.class).getMappedResults();
    }

    @Override
    @Nonnull
    public UserResidence countNearbyPoints(@Nonnull Long userId, @Nonnull Double lat, @Nonnull Double lng, double rateDistance) {
        return mongoTemplate.aggregate(
                newAggregation(
                        geoNear(NearQuery.near(new Point(lng, lat)).maxDistance(new Distance(rateDistance, Metrics.KILOMETERS)).spherical(true), "geometry"),
                        match(Criteria.where("target").is(userId)),
                        group("target").count().as("rate"),
                        project("rate")
                ),
                "userLocation",
                UserResidence.class).getUniqueMappedResult();

    }
}
