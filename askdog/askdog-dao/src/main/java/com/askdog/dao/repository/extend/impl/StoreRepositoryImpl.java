package com.askdog.dao.repository.extend.impl;

import com.askdog.dao.repository.extend.StoreRepositoryExtension;
import com.askdog.model.data.StoreAttribute;
import com.askdog.model.data.UserLocation;
import com.askdog.model.entity.partial.TimeBasedStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoPage;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static com.askdog.model.common.State.OK;
import static org.springframework.data.mongodb.core.index.GeoSpatialIndexType.GEO_2DSPHERE;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.NearQuery.near;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.util.Assert.notNull;

public class StoreRepositoryImpl implements StoreRepositoryExtension {

    @Autowired private EntityManager entityManager;
    @Autowired private MongoTemplate mongoTemplate;

    @PostConstruct
    public void init() {
        mongoTemplate.indexOps(UserLocation.class).ensureIndex(new GeospatialIndex("geo").typed(GEO_2DSPHERE));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TimeBasedStatistics> storeRegistrationStatistics(String unit, String interval) {
        Query nativeQuery = entityManager.createNativeQuery(
                "SELECT date_trunc('" + unit + "', join_u_eu.u_creation_time) AS day , count(*) AS store_count " +
                        "FROM (" +
                        "   SELECT" +
                        "       u.id," +
                        "       CASE" +
                        "           WHEN u.creation_time IS NULL THEN eu.registration_time" +
                        "           ELSE u.creation_time" +
                        "       END" +
                        "       AS u_creation_time" +
                        "   FROM mc_store u LEFT JOIN mc_external_user eu ON u.id = eu.bind_user" +
                        ") AS join_u_eu " +
                        "WHERE join_u_eu.u_creation_time > now() - interval '" + interval + "' " +
                        "GROUP BY 1 " +
                        "ORDER BY 1",
                "StoreCreationInWeek"
        );

        return nativeQuery.getResultList();
    }

    @Override
    public GeoPage<StoreAttribute> findStore(String cityCode, Point point, Pageable pageable) {
        notNull(cityCode);

        Criteria criteria = where("state").is(OK);

        if (!"00".equals(cityCode)) {
            criteria.and("adCode").regex("^" + cityCode);
        }

        org.springframework.data.mongodb.core.query.Query query = query(criteria);
        long count = mongoTemplate.count(query, StoreAttribute.class);
        return new GeoPage<>(
                mongoTemplate.geoNear(
                        near(point)
                                .spherical(false)
                                .inKilometers()
                                .query(query.with(pageable)),
                        StoreAttribute.class
                ),
                pageable,
                count
        );
    }

}
