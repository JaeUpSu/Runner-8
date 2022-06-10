package com.example.Runner8.TRASH.calendar;

import java.io.Serializable;

public class selectDate implements Serializable {
    private static final long serialVersionUID = 1L;

    private String year, month, day;

    public selectDate(String[] date){
        this.year = date[5];
        this.month = date[1];
        this.day = date[2];
    }
    public String getYear() {
        return this.year;
    }
    public String getMonth(){
        if(this.month.equals("Jan")) this.month = "1";
        else if(this.month.equals("Feb")) this.month = "2";
        else if(this.month.equals("Mar")) this.month = "3";
        else if(this.month.equals("Apr")) this.month = "4";
        else if(this.month.equals("May")) this.month = "5";
        else if(this.month.equals("Jun")) this.month = "6";
        else if(this.month.equals("Jul")) this.month = "7";
        else if(this.month.equals("Aug")) this.month = "8";
        else if(this.month.equals("Sep")) this.month = "9";
        else if(this.month.equals("Oct")) this.month = "10";
        else if(this.month.equals("Nov")) this.month = "11";
        else if(this.month.equals("Dec")) this.month = "12";
        return this.month;
    }
    public String getDay(){
        return this.day;
    }
    public String getDate(){
        return this.year + getMonth() + this.getDay();
    }
}
