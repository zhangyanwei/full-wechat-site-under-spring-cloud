package com.askdog.service.bo.admin.dashboard;

import com.askdog.model.entity.partial.TimeBasedStatistics;

import java.util.List;

public class StoreStatistics {

    private Long totalStoreCount;
    private List<TimeBasedStatistics> storeRegistrationTrend;

    public Long getTotalStoreCount() {
        return totalStoreCount;
    }

    public void setTotalStoreCount(Long totalStoreCount) {
        this.totalStoreCount = totalStoreCount;
    }

    public List<TimeBasedStatistics> getStoreRegistrationTrend() {
        return storeRegistrationTrend;
    }

    public void setStoreRegistrationTrend(List<TimeBasedStatistics> storeRegistrationTrend) {
        this.storeRegistrationTrend = storeRegistrationTrend;
    }
}
