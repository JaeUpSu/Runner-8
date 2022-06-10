package com.example.Runner8.ui.map.UFSRecord.Adapter.Model;

public class TopRecord {
    int ranking;
    String nickName;
    String Time;
    String Date;

    public String getNickName() {
        return nickName;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public int getRanking() {
        return ranking;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
}

