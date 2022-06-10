package com.example.Runner8.ui.map;

import java.util.ArrayList;

public class QuarterOfCourses {


    String id;
    ArrayList<Double> quarter_Lat = new ArrayList<>();
    ArrayList<Double> quarter_Long = new ArrayList<>();

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Double> getQuarter_Long() {
        return quarter_Long;
    }

    public ArrayList<Double> getQuarter_Lat() {
        return quarter_Lat;
    }

    public String getId() {
        return id;
    }

    public void setQuarter_Lat(ArrayList<Double> quarter_Lat) {
        this.quarter_Lat = quarter_Lat;
    }

    public void setQuarter_Long(ArrayList<Double> quarter_Long) {
        this.quarter_Long = quarter_Long;
    }
    public void addQuarter_Lat(double lat){
        this.quarter_Lat.add(lat);
    }
    public void addQuarter_Long(double Long){
        this.quarter_Long.add(Long);
    }

    public void clearQuarter_lat(){
        this.quarter_Lat.clear();
    }
    public void clearQuarter_Long(){
        this.quarter_Long.clear();
    }
}
