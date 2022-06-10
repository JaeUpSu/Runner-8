package com.example.Runner8.ui.Graph;

public class Pick_Date {

    String year, month, day;
    String date;

    public Pick_Date(int year, int month, int day){
        this.year = String.valueOf(year);
        this.month = String.valueOf(month);
        this.day = String.valueOf(day);
        this.date = (year)+"."+(month)+"."+(day);
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getDate() {
        return date;
    }

    public String get_setTextDate(){
        return (year)+"."+(month + 1)+"."+(day);
    }
}
