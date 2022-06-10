package com.example.Runner8.ui.community.Adapter.Notice.model;

import java.io.Serializable;

public class NoticeModel implements Serializable {
    private static final long serialVersionUID = 2L;

    private String profile;
    private String nickName;
    private String date;
    private String title;
    private String content;
    private String views;
    private String timeValue;
    private String index;
    private String uid;
    private String array_position;


    public String getDate() {
        return date;
    }

    public String getNickName() {
        return nickName;
    }

    public String getTimeValue() {
        return timeValue;
    }

    public String getUid() {
        return uid;
    }

    public String getIndex() {
        return index;
    }

    public String getProfile() {
        return profile;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getViews() {
        return views;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getArray_position() {
        return array_position;
    }

    public void setArray_position(String array_position) {
        this.array_position = array_position;
    }
}
