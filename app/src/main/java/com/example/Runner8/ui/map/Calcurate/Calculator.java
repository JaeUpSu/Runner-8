package com.example.Runner8.ui.map.Calcurate;

import com.example.Runner8.SingleTon.Sub_bundle;
import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;

public class Calculator {


    public Double dist_num(ArrayList<LatLng> llList) {

        return llList.get(llList.size() - 2).toLatLng().distanceTo(llList.get(llList.size() - 1).toLatLng());
    }

    public String dist_result(double num) {
        String result = "";

        if (num >= 1000) {
            if ((int) (num % 1000) == 0)
                result = (int) (num / 1000) + "km";
            else
                result = (int) (num / 1000) + "km " + (int) (num % 1000) + "m";
        } else
            result = (int) (num % 1000) + "m";

        return result;
    }

    public Double dist_point(ArrayList<LatLng> llList) {

        double sum = 0;
        int i = 1;
        for (LatLng latLng : llList) {
            if (llList.size() == i) break;
            sum += latLng.distanceTo(llList.get(i));
            i++;
        }
        return sum;
    }

    public String time_result(double dist) {
        Integer time = 1 * (int) (dist / 100) - (int) (dist / 1600);
        String result = time + " 분";

        return result;
    }

    public double kcal_result(int time) {

        double result;
        double kg = Double.valueOf(Sub_bundle.getInstance().getKg());
        result = Math.round((5 * 4 * 3.5 * kg * time / 1000) * 100) / 100.0;

        return result;
    }

    public String format_time(long second){

        String result="";
        int minute = 0;
        int hour = 0;

        while(second > 60){
            minute++;
            while(minute > 60){
             hour++;
             minute -= 60;
            }
            second -= 60;
        }
        if(hour > 0) result = hour + "시간" + minute + "분" + second + "초";
        else if(minute > 0) result = minute + "분" + second + "초";
        else result = second + "초";

        return result;
    }

    public String format_time_num(long second){

        String result="";
        int minute = 0;
        int hour = 0;

        while(second > 60){
            minute++;
            while(minute > 60){
                hour++;
                minute -= 60;
            }
            second -= 60;
        }
        if(hour > 0) result = hour + " : " + minute + " : " + second + " : ";
        else if(minute > 0) result = minute + " : " + second + " : ";
        else result = second + " : ";

        return result;
    }
}
