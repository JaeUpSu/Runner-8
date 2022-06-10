package com.example.Runner8.SingleTon;

import java.util.HashMap;
import java.util.Map;

public class Sub_bundle {

    String rep;
    String foodName;
    String total_food_kcal;
    String total_health_kcal;
    String app_amount;
    String goal_weight;
    String kg;
    String act;
    String height;
    String first_date;
    String MaxKcal;
    String MaxKcalFood;
    String MinKcal;
    String MinKcalFood;
    String DayOfTheWeek_lastMonth;
    String avg_DayOfTheWeek_lastMonth;
    String recently_month;
    String pro_img_url;
    String nickName;
    String total_comm_index;
    String user_comm_index;
    String sex;
    String age;
    String memo_count;

    int backStackCount = 0;
    Map<String, Object> drawable = new HashMap<>();
    Map<String, Object> name = new HashMap<>();
    Map<String, Object> num = new HashMap<>();
    Map<String, Object> prev_summary_freq_food = new HashMap<>();
    Map<String, Object> today_summary_freq_food = new HashMap<>();


    private static final Sub_bundle SUBBUNDLE_INSTANCE = new Sub_bundle();

    public static synchronized Sub_bundle getInstance(){
        return SUBBUNDLE_INSTANCE;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getTotal_health_kcal() {
        return total_health_kcal;
    }

    public void setTotal_health_kcal(String total_health_kcal) {
        this.total_health_kcal = total_health_kcal;
    }

    public String getTotal_food_kcal() {
        return total_food_kcal;
    }

    public void setTotal_food_kcal(String total_food_kcal) {
        this.total_food_kcal = total_food_kcal;
    }

    public String getApp_amount() {
        return app_amount;
    }

    public void setApp_amount(String app_amount) {
        this.app_amount = app_amount;
    }

    public String getGoal_weight() {
        return goal_weight;
    }

    public void setGoal_weight(String goal_weight) {
        this.goal_weight = goal_weight;
    }

    public String getKg() {
        return kg;
    }

    public void setKg(String kg) {
        this.kg = kg;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Map<String, Object> getDrawable() {
        return drawable;
    }

    public void setDrawable(Map<String, Object> drawable) {
        this.drawable.putAll(drawable);
    }

    public Map<String, Object> getName() {
        return name;
    }

    public void setName(Map<String, Object> name) {
        this.name.putAll(name);
    }

    public Map<String, Object> getNum() {
        return num;
    }

    public void setNum(Map<String, Object> num) {
        this.num.putAll(num);
    }

    public String getFirst_date() {
        return first_date;
    }

    public void setFirst_date(String first_date) {
        this.first_date = first_date;
    }

    public Map<String, Object> getPrev_summary_freq_food() {
        return prev_summary_freq_food;
    }

    public void setPrev_summary_freq_food(Map<String, Object> prev_summary_freq_food) {
        this.prev_summary_freq_food.putAll(prev_summary_freq_food);
        this.today_summary_freq_food.putAll(today_summary_freq_food);
    }

    public void addPrev_summary_freq_food(String foodName, int count){
        this.prev_summary_freq_food.put(foodName, count);
    }

    public Map<String, Object> getToday_summary_freq_food() {
        return today_summary_freq_food;
    }

    public void setToday_summary_freq_food(Map<String, Object> today_summary_freq_food) {
        this.today_summary_freq_food.putAll(today_summary_freq_food);
    }

    public void addToday_summary_freq_food(String foodName, int count){
        this.prev_summary_freq_food.put(foodName, count);
    }

    public String getMaxKcal() {
        return MaxKcal;
    }

    public void setMaxKcal(String maxKcal) {
        MaxKcal = maxKcal;
    }

    public String getMinKcal() {
        return MinKcal;
    }

    public void setMinKcal(String minKcal) {
        MinKcal = minKcal;
    }

    public String getMaxKcalFood() {
        return MaxKcalFood;
    }

    public void setMaxKcalFood(String maxKcalFood) {
        MaxKcalFood = maxKcalFood;
    }

    public String getMinKcalFood() {
        return MinKcalFood;
    }

    public void setMinKcalFood(String minKcalFood) {
        MinKcalFood = minKcalFood;
    }

    public String getDayOfTheWeek_lastMonth() {
        return DayOfTheWeek_lastMonth;
    }

    public void setDayOfTheWeek_lastMonth(String dayOfTheWeek_lastMonth) {
        DayOfTheWeek_lastMonth = dayOfTheWeek_lastMonth;
    }

    public String getAvg_DayOfTheWeek_lastMonth() {
        return avg_DayOfTheWeek_lastMonth;
    }

    public void setAvg_DayOfTheWeek_lastMonth(String avg_DayOfTheWeek_lastMonth) {
        this.avg_DayOfTheWeek_lastMonth = avg_DayOfTheWeek_lastMonth;
    }

    public String getRecently_month() {
        return recently_month;
    }

    public void setRecently_month(String recently_month) {
        this.recently_month = recently_month;
    }

    public String getPro_img_url() {
        return pro_img_url;
    }

    public void setPro_img_url(String pro_img_url) {
        this.pro_img_url = pro_img_url;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTotal_comm_index() {
        return total_comm_index;
    }

    public void setTotal_comm_index(String total_comm_index) {
        this.total_comm_index = total_comm_index;
    }

    public String getUser_comm_index() {
        return user_comm_index;
    }

    public void setUser_comm_index(String user_comm_index) {
        this.user_comm_index = user_comm_index;
    }
    public void PlusBackStackCount(){
        this.backStackCount++;
    }

    public int getBackStackCount() {
        return backStackCount;
    }
    public void clearBackStackCount(){
        this.backStackCount = 0;
    }
    public void MinusBackStackCount(){
        this.backStackCount--;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    public void setMemo_count(String memo_count) {
        this.memo_count = memo_count;
    }

    public String getMemo_count() {
        return memo_count;
    }
}
