package com.example.Runner8.TRASH;

import java.util.ArrayList;
import java.util.List;

public class Foods {

    List<String> food = new ArrayList<>();
    List<String> foodKcal = new ArrayList<>();
    List<String> containFood = new ArrayList<>();

    public void addFoodData(String name){
        food.add(name);
    }

    public List getFood(){
        return food;
    }

    public List getFoodKcacl(){
        return foodKcal;
    }

    public String getFood(int index){
        return food.get(index);
    }

    public List getContainFood(String string){
        int count=0;
        containFood.clear();

        for(String foodName : food){
            if((foodName.indexOf(string) == 0 ) && count < 10){
                containFood.add(foodName);
                count++;
            }
        }
        return containFood;
    }

}
