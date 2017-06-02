package com.askdog.model.entity.partial;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeBasedStatistics {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Date time;
    private Long count;

    public TimeBasedStatistics() {}

    public TimeBasedStatistics(String time, Long count) throws ParseException {
        this.time = DATE_FORMATTER.parse(time);
        this.count = count;
    }

    public TimeBasedStatistics(Date time, Long count) {
        this.time = time;
        this.count = count;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
