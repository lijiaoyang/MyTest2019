package com.example.mytest2019;

import java.util.Calendar;
import java.util.TimeZone;

public class CurrentTime {
    public Calendar cal;
    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;
    public String my_time;

    public int getHour() {
        if (cal.get(Calendar.AM_PM) == 0)
            hour = cal.get(Calendar.HOUR);
        else
            hour = cal.get(Calendar.HOUR) + 12;
        return hour;
    }

    public String getMy_time() {
        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        year = cal.get(Calendar.YEAR)-2000;
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DATE);

        if(month>9 && day >9)
            my_time = year + "-" +month + "-" + day;
        else if (month>9 && day <=9)
            my_time = year + "-" +month + "-0" + day;
        else if (month<=9 && day >9)
            my_time = year + "-" +"0" + month + "-" + day;
        else if (month<=9 && day <=9)
            my_time = year + "-" +"0" + month + "-0" + day;
        return my_time;
    }
    //判断整点
    public int JudgeClock(){
        minute = cal.get(Calendar.MINUTE);
        second = cal.get(Calendar.SECOND);
        if (minute==0 && second==0){
            return 1;
        }else
            return 0;
    }

}
