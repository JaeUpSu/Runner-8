package com.example.Runner8.ui.map.Adapter.Model;

import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;

public class MapData {

    String address, course_name, courseTime, courseDist, index, kcal, time, uid, like, nickName;
    String start_lat;
    String start_long;
    String finish_lat;
    String finish_long;
    // Boolean isLike;

    ArrayList <LatLng> geoList;
    ArrayList <Integer> timeList;

    public void setGeoList(ArrayList<LatLng> geoList) { this.geoList = geoList; }
    public void setTimeList(ArrayList<Integer> timeList) { this.timeList = timeList; }
    public void setCourse_name(String course_name) { this.course_name = course_name; }

    public void setAddress(String address) { this.address = address; }
    // public void setLike(Boolean like) { isLike = like; }

    public void setCourseDist(String courseDist) { this.courseDist = courseDist; }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public ArrayList<LatLng> getGeoList() { return geoList; }
    public ArrayList<Integer> getTimeList() { return timeList; }
    public String getCourse_name() { return course_name;}
    public String getAddress() { return address; }
    // public Boolean getLike() { return isLike; }
    public String getCourseDist() {return courseDist; }
    public String getCourseTime() { return courseTime; }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setLike(String like) {
        this.like = like;
    }
    public String getLike(){
        return like;
    }

    public String getStart_lat() {
        return start_lat;
    }

    public String getStart_long() {
        return start_long;
    }

    public void setStart_lat(String start_lat) {
        this.start_lat = start_lat;
    }

    public void setStart_long(String start_long) {
        this.start_long = start_long;
    }

    public String getFinish_lat() {
        return finish_lat;
    }

    public String getFinish_long() {
        return finish_long;
    }

    public void setFinish_long(String finish_long) {
        this.finish_long = finish_long;
    }

    public void setFinish_lat(String finish_lat) {
        this.finish_lat = finish_lat;
    }
}
