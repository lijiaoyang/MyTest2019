package com.example.mytest2019;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Date:2019/3/26
 * Time:22:33
 * author:jiaoyang
 **/
public class CurrentTime {
    Calendar cal;
    String year;
    String month;
    String day;
    String hour;
    String minute;
    String second;
    String my_time_1;
    String my_time_2;

    protected void GetTime() {
        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        year = String.valueOf(cal.get(Calendar.YEAR));
        month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        day = String.valueOf(cal.get(Calendar.DATE));
        if (cal.get(Calendar.AM_PM) == 0)
            hour = String.valueOf(cal.get(Calendar.HOUR));
        else
            hour = String.valueOf(cal.get(Calendar.HOUR) + 12);
        minute = String.valueOf(cal.get(Calendar.MINUTE));
        second = String.valueOf(cal.get(Calendar.SECOND));

        my_time_1 = year + "-" + month + "-" + day;
        my_time_2 = hour + "-" + minute + "-" + second;
    }
}
