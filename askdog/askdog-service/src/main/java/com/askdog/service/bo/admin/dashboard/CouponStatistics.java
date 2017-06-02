package com.askdog.service.bo.admin.dashboard;

import com.askdog.model.entity.partial.CouponTimeBasedStatistics;

import java.util.List;

public class CouponStatistics {

    private Long totalCouponCount;
    private List<CouponTimeBasedStatistics> couponCreationTrend;

    public Long getTotalCouponCount() {
        return totalCouponCount;
    }

    public void setTotalCouponCount(Long totalCouponCount) {
        this.totalCouponCount = totalCouponCount;
    }

    public List<CouponTimeBasedStatistics> getCouponCreationTrend() {
        return couponCreationTrend;
    }

    public void setCouponCreationTrend(List<CouponTimeBasedStatistics> couponCreationTrend) {
        this.couponCreationTrend = couponCreationTrend;
    }
}
