package com.askdog.dao.repository.extend;

import com.askdog.model.entity.partial.CouponTimeAndStateStatistics;

import java.util.List;

public interface CouponStatisticsRepository {

    List<CouponTimeAndStateStatistics> couponGainUnionStatistics(String unit, String interval);

}
