package com.askdog.model.entity.inner.coupon;

import java.util.concurrent.TimeUnit;

public class ConsumeExpires {

    private long value;
    // DAY or WEEK or MONTH
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long toMillis() {
        ExpiresUnit expiresUnit = ExpiresUnit.valueOf(this.unit);
        this.value = this.value * ExpiresUnit.toDay(expiresUnit);
        return TimeUnit.DAYS.toMillis(value);
    }
}
