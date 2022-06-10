package com.example.Runner8.ui.F_H.calorie.Adapter.Model;


import java.io.Serializable;

public class FoodData implements Serializable {

    private static final long serialVersionUID = 10L;

    java.lang.String name;
    Double kcal;
    Double fat;
    double car = -1;
    java.lang.String content;
    double pro = -1;
    java.lang.String f_class;
    Integer img_src, num;
    Boolean checked = true;

    public void setName(java.lang.String name) { this.name = name; }
    public java.lang.String getName() { return name;}

    public Integer getImg_src() { return img_src; }
    public void setImg_src(Integer img_src) { this.img_src = img_src; }

    public Double getKcal() { return kcal; }
    public void setKcal(Double kcal) { this.kcal = kcal; }

    public Integer getNum() { return num; }
    public void setNum(Integer num) { this.num = num; }

    public Boolean getChecked() { return checked; }
    public void setChecked(Boolean checked) { this.checked = checked; }

    public double getCar() {
        return car;
    }

    public java.lang.String getContent() {
        return content;
    }

    public Double getFat() {
        return fat;
    }

    public void setContent(java.lang.String content) {
        this.content = content;
    }

    public void setCar(double car) {
        this.car = car;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public void setPro(double pro) {
        this.pro = pro;
    }

    public double getPro() {
        return pro;
    }

    public void setF_class(java.lang.String f_class) {
        this.f_class = f_class;
    }

    public java.lang.String getF_class() {
        return f_class;
    }
}
