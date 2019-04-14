package com.example.mytest2019;

import java.util.Calendar;
import java.util.TimeZone;

public class CurrentTime {
    private Calendar cal;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private String my_time_1;
    private String my_time_2;

    protected void GetTime() {
        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        year = cal.get(Calendar.YEAR)-2000;
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DATE);
        if (cal.get(Calendar.AM_PM) == 0)
            hour = cal.get(Calendar.HOUR);
        else
            hour = cal.get(Calendar.HOUR) + 12;
        minute = cal.get(Calendar.MINUTE);
        second = cal.get(Calendar.SECOND);

        my_time_1 = year + "-" + month + "-" + day;
        my_time_2 = hour + "-" + minute + "-" + second;
    }
}
