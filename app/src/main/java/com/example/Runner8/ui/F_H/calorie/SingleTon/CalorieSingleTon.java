package com.example.Runner8.ui.F_H.calorie.SingleTon;

import com.example.Runner8.ui.F_H.calorie.Adapter.Model.FoodData;
import com.example.Runner8.ui.F_H.calorie.Adapter.Model.KoreaClass;

import java.util.ArrayList;

public class CalorieSingleTon {

    FoodData foodData;
    ArrayList<FoodData> foodArrayList = new ArrayList<>();
    ArrayList<KoreaClass> koreaClasses = new ArrayList<>();
    String koreaFoodName;

    private static final CalorieSingleTon CALORIE_SINGLE_TON_INSTANCE = new CalorieSingleTon();

    public static synchronized CalorieSingleTon getInstance(){
        return CALORIE_SINGLE_TON_INSTANCE;
    }

    public void setFoodData(FoodData foodData) {
        this.foodData = foodData;
    }

    public FoodData getFoodData() {
        return foodData;
    }

    public void setFoodArrayList(ArrayList<FoodData> foodArrayList) {
        this.foodArrayList = foodArrayList;
    }
    public void addFoodArrayList(FoodData foodData){
        this.foodArrayList.add(foodData);
    }
    public void clearFoodArrayList(){
        this.foodArrayList.clear();
    }

    public ArrayList<FoodData> getFoodArrayList() {
        return foodArrayList;
    }
    public void removeFoodArrayList(int position){
        this.foodArrayList.remove(position);
    }
    public void setFoodArrayList(int position, FoodData foodData){
        this.foodArrayList.set(position, foodData);
    }

    public void setKoreaClasses(ArrayList<KoreaClass> koreaClasses) {
        this.koreaClasses = koreaClasses;
    }

    public ArrayList<KoreaClass> getKoreaClasses() {
        return koreaClasses;
    }

    public void addKoreaClass(KoreaClass koreaClass){
        this.koreaClasses.add(koreaClass);
    }

    public void setKoreaFoodName(String koreaFoodName) {
        this.koreaFoodName = koreaFoodName;
    }

    public String getKoreaFoodName() {
        return koreaFoodName;
    }
}
