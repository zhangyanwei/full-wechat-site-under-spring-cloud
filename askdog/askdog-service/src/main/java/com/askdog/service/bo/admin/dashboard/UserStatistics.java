package com.askdog.service.bo.admin.dashboard;

import com.askdog.model.entity.partial.TimeBasedStatistics;
import com.askdog.model.entity.partial.UserProviderStatistic;

import java.util.List;

public class UserStatistics {

    private Long totalUserCount;
    private Long totalRegistrationConfirmedUserCount;
    private List<UserProviderStatistic> userDistribution;
    private List<TimeBasedStatistics> userRegistrationTrend;
    private List<TimeBasedStatistics> userActiveTrend;

    public Long getTotalUserCount() {
        return totalUserCount;
    }

    public void setTotalUserCount(Long totalUserCount) {
        this.totalUserCount = totalUserCount;
    }

    public Long getTotalRegistrationConfirmedUserCount() {
        return totalRegistrationConfirmedUserCount;
    }

    public void setTotalRegistrationConfirmedUserCount(Long totalRegistrationConfirmedUserCount) {
        this.totalRegistrationConfirmedUserCount = totalRegistrationConfirmedUserCount;
    }

    public List<UserProviderStatistic> getUserDistribution() {
        return userDistribution;
    }

    public void setUserDistribution(List<UserProviderStatistic> userDistribution) {
        this.userDistribution = userDistribution;
    }

    public List<TimeBasedStatistics> getUserRegistrationTrend() {
        return userRegistrationTrend;
    }

    public void setUserRegistrationTrend(List<TimeBasedStatistics> userRegistrationTrend) {
        this.userRegistrationTrend = userRegistrationTrend;
    }

    public List<TimeBasedStatistics> getUserActiveTrend() {
        return userActiveTrend;
    }

    public void setUserActiveTrend(List<TimeBasedStatistics> userActiveTrend) {
        this.userActiveTrend = userActiveTrend;
    }
}
