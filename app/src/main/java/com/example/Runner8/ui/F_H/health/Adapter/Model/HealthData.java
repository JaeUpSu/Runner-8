package com.example.Runner8.ui.F_H.health.Adapter.Model;


public class HealthData {

    String name;
    Integer img_src, time;
    Double kcal;
    Double met_num;
    Boolean btnChecked = false;

    public void setName(String name) { this.name = name; }
    public String getName() { return name;}

    public Integer getImg_src() { return img_src; }
    public void setImg_src(Integer img_src) { this.img_src = img_src; }

    public Boolean getBtnChecked() { return btnChecked; }
    public void setBtnChecked(Boolean btnChecked) { this.btnChecked = btnChecked; }

    public void setTime(Integer time) { this.time = time; }
    public Integer getTime() { return time; }

    public void setKcal(Double kcal) { this.kcal = kcal; }
    public Double getKcal() { return kcal; }

    public void setMet_num(Double met_num) {
        this.met_num = met_num;
    }

    public Double getMet_num() {
        return met_num;
    }
}
