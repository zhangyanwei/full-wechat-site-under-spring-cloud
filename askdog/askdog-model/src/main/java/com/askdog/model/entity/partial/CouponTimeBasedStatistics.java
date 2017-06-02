package com.askdog.model.entity.partial;

import java.util.Date;

public class CouponTimeBasedStatistics extends TimeBasedStatistics {

    private Long usedCount;

    public CouponTimeBasedStatistics() {
    }

    public CouponTimeBasedStatistics(Date time, Long count, Long usedCount) {
        super(time, count);
        this.usedCount = usedCount;
    }

    public Long getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Long usedCount) {
        this.usedCount = usedCount;
    }
}
