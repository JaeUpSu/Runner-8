package com.example.Runner8.ui.F_H.calorie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodClassification {


    Map<String, String[]> classification;
    String[] snackBar;
    String[] form;
    String[] japaneseFood;
    List foodName;

    public FoodClassification(){
        this.classification = new HashMap<>();
        this.foodName = new ArrayList();
        this.snackBar = new String[]{"김말이","김밥","김치볶음밥","닭꼬치",
        "떡볶이","라면","만두튀김","삼각김밥","소떡소떡","순대","어묵"};
        this.form = new String[]{"고르곤졸라","로제파스타","빵",
        "스테이크","토마토파스타"};
        this.japaneseFood = new String[]{"돈까스","돈까스덮밥","라멘","모밀",
        "소고기덮밥","연어덮밥","오꼬노미야끼"};


        this.classification.put("분식",snackBar);
    }

    public String[] getSnackBar(String cf) {
        return classification.get(cf);
    }

    public int getFoodNameSize(){
        return this.foodName.size();
    }

    public void setFoodName(List foodName) {
        this.foodName.addAll(foodName);
    }

    public Object getFoodName(int index) {
        return foodName.get(index);
    }

    public List getFoodName() {
        return foodName;
    }
}
