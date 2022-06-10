package com.example.Runner8.ui.map.SingleTon;

import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;

public class CoursePointsSingleTon {

    ArrayList<LatLng> latLngs = new ArrayList<>();
    LatLng start_latLng;

    private static final CoursePointsSingleTon COURSE_POINTS_SINGLE_TON_INSTANCE = new CoursePointsSingleTon();

    public static synchronized CoursePointsSingleTon getInstance() {return COURSE_POINTS_SINGLE_TON_INSTANCE;}

    public ArrayList<LatLng> getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(ArrayList<LatLng> latLngs) {
        this.latLngs = latLngs;
    }

    public void setStart_latLng(LatLng start_latLng) {
        this.start_latLng = start_latLng;
    }

    public LatLng getStart_latLng() {
        return start_latLng;
    }
    public void addLatLngs(LatLng latLng){
        this.latLngs.add(latLng);
    }

}
