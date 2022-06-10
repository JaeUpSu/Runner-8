package com.example.Runner8.ui.map.SingleTon;

import com.example.Runner8.ui.map.QuarterOfCourses;

import java.util.ArrayList;

public class CourseQuarterPointsST {

    ArrayList<String> documentId = new ArrayList<>();
    ArrayList<QuarterOfCourses> quarterOfCourses = new ArrayList<>();
    ArrayList<Double> quarter_Lat = new ArrayList<>();
    ArrayList<Double> quarter_Long = new ArrayList<>();

    private static final CourseQuarterPointsST COURSE_QUARTER_POINTS_ST_INSTANCE = new CourseQuarterPointsST();

    public static synchronized CourseQuarterPointsST getInstance() {return COURSE_QUARTER_POINTS_ST_INSTANCE;}

    public void addDocumentId(String id){
        this.documentId.add(id);
    }
    public void addQuarter_Lat(double lat){
        this.quarter_Lat.add(lat);
    }
    public void addQuarter_Long(double Long){
        this.quarter_Lat.add(Long);
    }

    public ArrayList<Double> getQuarter_Lat() {
        return quarter_Lat;
    }

    public ArrayList<Double> getQuarter_Long() {
        return quarter_Long;
    }

    public ArrayList<String> getDocumentId() {
        return documentId;
    }
    public void clearDocumentId(){
        this.documentId.clear();
    }
    public void clearQuarter_lat(){
        this.quarter_Lat.clear();
    }
    public void clearQuarter_Long(){
        this.quarter_Long.clear();
    }
}
