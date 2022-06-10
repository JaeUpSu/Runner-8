package com.example.Runner8.ui.summary;

public class SummarySingleTon {

    String current_week_food_kcal = "";
    String current_week_health_kcal = "";
    String prev_week_food_kcal = "";
    String prev_week_health_kcal = "";

    private static final SummarySingleTon SUMMARY_SINGLE_TON_INSTANCE = new SummarySingleTon();

    public static synchronized SummarySingleTon getInstance(){
        return SUMMARY_SINGLE_TON_INSTANCE;
    }

    public String getCurrent_week_food_kcal() {
        return current_week_food_kcal;
    }

    public String getCurrent_week_health_kcal() {
        return current_week_health_kcal;
    }

    public String getPrev_week_food_kcal() {
        return prev_week_food_kcal;
    }

    public String getPrev_week_health_kcal() {
        return prev_week_health_kcal;
    }

    public void setCurrent_week_food_kcal(String current_week_food_kcal) {
        this.current_week_food_kcal = current_week_food_kcal;
    }

    public void setCurrent_week_health_kcal(String current_week_health_kcal) {
        this.current_week_health_kcal = current_week_health_kcal;
    }

    public void setPrev_week_food_kcal(String prev_week_food_kcal) {
        this.prev_week_food_kcal = prev_week_food_kcal;
    }

    public void setPrev_week_health_kcal(String prev_week_health_kcal) {
        this.prev_week_health_kcal = prev_week_health_kcal;
    }
}
