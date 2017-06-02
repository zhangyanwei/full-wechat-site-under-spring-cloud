package com.askdog.service.utils;

import com.askdog.model.entity.partial.CouponTimeAndStateStatistics;
import com.askdog.model.entity.partial.CouponTimeBasedStatistics;
import com.askdog.model.entity.partial.TimeBasedStatistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StatisticsUtils {

    public static List<TimeBasedStatistics> overlying(List<TimeBasedStatistics> statisticsList) {
        if (statisticsList.size() <= 1) return statisticsList;
        for (int i = 1; i < statisticsList.size(); i++) {
            Long currentCount = statisticsList.get(i).getCount();
            currentCount = currentCount + statisticsList.get(i - 1).getCount();
            statisticsList.get(i).setCount(currentCount);
        }
        return statisticsList;
    }

    public static List<CouponTimeBasedStatistics> overlyingForCoupon(List<CouponTimeBasedStatistics> statisticsList) {
        if (statisticsList.size() <= 1) return statisticsList;

        for (int i = 1; i < statisticsList.size(); i++) {
            Long currentCount = statisticsList.get(i).getCount();
            currentCount = currentCount + statisticsList.get(i - 1).getCount();
            statisticsList.get(i).setCount(currentCount);

            Long currentUsedCount = statisticsList.get(i).getUsedCount();
            currentUsedCount = currentUsedCount + statisticsList.get(i - 1).getUsedCount();
            statisticsList.get(i).setUsedCount(currentUsedCount);
        }
        return statisticsList;
    }

    public static List<CouponTimeBasedStatistics> union(List<CouponTimeAndStateStatistics> countList) {
        List<CouponTimeBasedStatistics> resultList = new ArrayList<>();
        CouponTimeAndStateStatistics last;
        boolean initialization = false;
        if (countList.size() == 0) {
            resultList.add(new CouponTimeBasedStatistics(new Date(), 0L, 0L));
            return resultList;
        }
        Iterator<CouponTimeAndStateStatistics> iterator = countList.iterator();
        last = iterator.next();
        if (!iterator.hasNext()) {
            resultList.add(new CouponTimeBasedStatistics(last.getTime(), last.getCount(), 0L));
            return resultList;
        }
        while (iterator.hasNext()) {
            if (initialization) last = iterator.next();
            if (!iterator.hasNext()) {
                resultList.add(new CouponTimeBasedStatistics(last.getTime(), last.getCount(), 0L));
                return resultList;
            }
            CouponTimeAndStateStatistics current = iterator.next();
            if (last.getTime().equals(current.getTime())) {
                resultList.add(new CouponTimeBasedStatistics(last.getTime(), last.getCount(), current.getCount()));
                initialization = true;
            } else {
                resultList.add(new CouponTimeBasedStatistics(last.getTime(), last.getCount(), 0L));
                last = current;
                initialization = false;
            }
        }
        return resultList;
    }
}
