package com.example.Runner8.ui.F_H.Cart;

public class CartItem {

    String foodName;
    String foodKcal;
    String foodCount;

    public CartItem(String foodName, String foodKcal, String foodCount){
        this.foodName = foodName;
        this.foodKcal = foodKcal;
        this.foodCount = foodCount;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodKcal() {
        return foodKcal;
    }

    public String getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(String foodCount) {
        this.foodCount = foodCount;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodKcal(String foodKcal) {
        this.foodKcal = foodKcal;
    }
}
