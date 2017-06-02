package com.askdog.dao.repository.extend.impl;

import com.askdog.dao.repository.extend.CouponStatisticsRepository;
import com.askdog.model.entity.partial.CouponTimeAndStateStatistics;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class CouponRepositoryImpl implements CouponStatisticsRepository {

    @Autowired
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<CouponTimeAndStateStatistics> couponGainUnionStatistics(String unit, String interval) {
        Query nativeQuery = entityManager.createNativeQuery(
                "SELECT date_trunc('" + unit + "', join_u_eu.u_creation_time) AS day, count(*) AS coupon_count, join_u_eu.usage_state AS usage_state" +
                        "    FROM (" +
                        "        SELECT " +
                        "            u.id, " +
                        "            CASE " +
                        "                WHEN u.creation_time ISNULL THEN eu.registration_time \n" +
                        "                ELSE u.creation_time\n" +
                        "            END" +
                        "            AS u_creation_time," +
                        "            u.state AS usage_state" +
                        "        FROM mc_user_coupon u LEFT JOIN mc_external_user eu ON u.id = eu.bind_user" +
                        "    ) AS join_u_eu" +
                        "    WHERE join_u_eu.u_creation_time > now()- INTERVAL '" + interval + "' " +
                        "    GROUP BY day,usage_state" +
                        "    ORDER BY day,usage_state",
                "CouponCreationInWeek"
        );

        return nativeQuery.getResultList();
    }
}
