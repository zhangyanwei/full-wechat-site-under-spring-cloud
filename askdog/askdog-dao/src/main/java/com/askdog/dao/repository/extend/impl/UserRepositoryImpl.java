package com.askdog.dao.repository.extend.impl;

import com.askdog.dao.repository.extend.UserStatisticsRepository;
import com.askdog.model.entity.inner.user.UserStatus;
import com.askdog.model.entity.partial.TimeBasedStatistics;
import com.askdog.model.entity.partial.UserProviderStatistic;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

public class UserRepositoryImpl implements UserStatisticsRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Long countByUserStatus(UserStatus userStatus) {
        return ((BigInteger) entityManager.createNativeQuery("SELECT count(*) FROM mc_user WHERE user_statuses LIKE '%" + userStatus.name() + "%'").getSingleResult()).longValueExact();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UserProviderStatistic> userProviders() {
        Query nativeQuery = entityManager.createNamedQuery("UserProviderDistribution");
        return nativeQuery.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TimeBasedStatistics> userRegistrationStatistics(String unit, String interval) {
        Query nativeQuery = entityManager.createNativeQuery(
                "SELECT date_trunc('" + unit + "', join_u_eu.u_registration_time) AS day , count(*) AS user_count " +
                        "FROM (" +
                        "   SELECT" +
                        "       u.id," +
                        "       CASE" +
                        "           WHEN u.registration_time IS NULL THEN eu.registration_time" +
                        "           ELSE u.registration_time" +
                        "       END" +
                        "       AS u_registration_time" +
                        "   FROM mc_user u LEFT JOIN mc_external_user eu ON u.id = eu.bind_user" +
                        ") AS join_u_eu " +
                        "WHERE join_u_eu.u_registration_time > now() - interval '" + interval + "' " +
                        "GROUP BY 1 " +
                        "ORDER BY 1",
                "UserRegistrationInWeek"
        );
        return nativeQuery.getResultList();
    }
}
