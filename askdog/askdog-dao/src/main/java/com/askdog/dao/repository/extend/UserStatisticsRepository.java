package com.askdog.dao.repository.extend;

import com.askdog.model.entity.inner.user.UserStatus;
import com.askdog.model.entity.partial.TimeBasedStatistics;
import com.askdog.model.entity.partial.UserProviderStatistic;

import java.util.List;

public interface UserStatisticsRepository {

    Long countByUserStatus(UserStatus userStatus);
    List<UserProviderStatistic> userProviders();
    List<TimeBasedStatistics> userRegistrationStatistics(String unit, String interval);

}
