package com.askdog.model.entity.inner.coupon;

import java.util.EnumSet;
import java.util.Set;

public enum ExpiresUnit {
    DAY, WEEK, MONTH;

    final static long WEEK_PARA = 7;
    final static long MONTH_PARA = 30;
    final static long DAY_PARA = 1;

    public static Set getAllElements() {
        return EnumSet.allOf(ExpiresUnit.class);
    }

    public static long toDay(ExpiresUnit unit) {

        switch (unit) {
            case WEEK:
                return WEEK_PARA;
            case MONTH:
                return MONTH_PARA;
            default:
                return DAY_PARA;
        }
    }
}
