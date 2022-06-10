package com.example.Runner8.ui.community;

import com.example.Runner8.ui.Graph.Today_Date;

import java.util.Calendar;

public class TimeValue {

    String date;
    String time;
    String timeValue;

    int max_date_cal;
    int max_feb;

    int[] int_date;
    int[] int_time;

    Calendar date_cal;
    Calendar time_cal;
    Calendar feb_date_cal;
    Today_Date today_date = new Today_Date();

    public TimeValue(String date, String time){
        this.date = date;
        this.time = time;

        int_date = new int[3];
        int_time = new int[3];

        for(int i=0; i<3; i++){
            int_date[i] = Integer.valueOf(date.split("[.]")[i]);
            int_time[i] = Integer.valueOf(time.split("[:]")[i]);
        }
    }

    public void InitialCalendarObject(){
        date_cal = Calendar.getInstance();
        time_cal = Calendar.getInstance();
        feb_date_cal = Calendar.getInstance();

        date_cal.set(int_date[0], int_date[1] - 1, int_date[2]);
        feb_date_cal.set(int_date[0], 1, 1);

        max_date_cal = date_cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        max_feb = feb_date_cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public String getTimeValue(){
        InitialCalendarObject();

        today_date.setNow();

        // 검토 요망
        if (int_date[0] == today_date.getYear()) {
            if (int_date[1] == 1 && today_date.getMonth() - int_date[1] == 2) {
                int time_value = max_date_cal - int_date[2] + max_feb + today_date.getDay();
                if (time_value < 30)
                    timeValue = time_value + "일전";
                 else
                    timeValue = date;
            } else if (today_date.getMonth() - int_date[1] == 1) {
                int time_value = max_date_cal - int_date[2] + today_date.getDay();
                if (time_value < 30)
                    timeValue = time_value + "일전";
                 else
                    timeValue = date;

            } else if (today_date.getMonth() == int_date[1]) {
                // 방금 전 vs 0 일전
                int time_value = today_date.getDay() - int_date[2];
                timeValue = time_value + "일전";
            } else {
                timeValue = date;
            }
        } else if (today_date.getYear() - int_date[0] == 1
                && int_date[1] == 12) {
            int time_value = max_date_cal - int_date[2] + today_date.getDay();
            if (time_value < 30) {
                timeValue = time_value + "일전";
            } else {
                timeValue = date;
            }
        } else {
            timeValue = date;
        }
        return this.timeValue;
    }
}
