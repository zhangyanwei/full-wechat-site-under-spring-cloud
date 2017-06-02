package com.askdog.model.entity.partial;

import java.util.Date;

public class CouponTimeAndStateStatistics extends TimeBasedStatistics {

    private String state;

    public CouponTimeAndStateStatistics() {}

    public CouponTimeAndStateStatistics(Date time, Long count, String state) {
        super(time, count);
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
