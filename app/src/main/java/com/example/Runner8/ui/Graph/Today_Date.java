package com.example.Runner8.ui.Graph;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Today_Date {

    Date today;
    SimpleDateFormat format_year, format_month,
            format_day, format_hour, format_minute, format_second;
    int year, month, day, hour, minute, second;
    long now;
    String format_date, format_time;

    public Today_Date(){
    }

    public void setNow(){
        now = System.currentTimeMillis();

        this.today = new Date(now);
        this.format_year = new SimpleDateFormat("yyyy");
        this.format_month = new SimpleDateFormat("MM");
        this.format_day = new SimpleDateFormat("dd");
        this.format_hour = new SimpleDateFormat("hh");
        this.format_minute = new SimpleDateFormat("mm");
        this.format_second = new SimpleDateFormat("ss");

        this.year = Integer.parseInt(format_year.format(today));
        this.month = Integer.parseInt(format_month.format(today));
        this.day = Integer.parseInt(format_day.format(today));
        this.hour = Integer.parseInt(format_hour.format(today));
        this.minute = Integer.parseInt(format_minute.format(today));
        this.second = Integer.parseInt(format_second.format(today));

        this.format_date = (year)+"."+(month)+"."+(day);
        this.format_time = (hour)+":"+(minute)+":"+(second);
    }
    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getFormat_date() {
        return format_date;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public String getFormat_time() {
        return format_time;
    }
}
