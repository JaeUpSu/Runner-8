package com.example.Runner8.ui.F_H.calorieDictionary;

import java.util.HashMap;
import java.util.Map;

public class FoodData {

    /*
    String name;                    // 이름
    String kcal;                    // 열량         (Eng) Energy
    String protein;                 // 단백질       (Pro)
    String fat;                     // 지방         (Fat)
    String cholesterol;             // 콜레스테롤    (CHOLE)
    String transFat;                // 트랜스지방    (TFA) trans fatty acid
    String saturatedFattyAcids;     // 포화지방산    (SFA) Saturate Fatty acid
    String natrium;                 // 나트륨       (Na)
    String sugars;                  // 당류         (Tsg)
    String totalContent;            // 총 내용량
    String carbohydrate;            // 탄수화물     (Car)
    */

    Double[] OneDayBaseLine;
    Map<String, String> percent;
    String[] FoodData;
    String[] FoodDataString;
    String FoodName;
    double Eng;

    public FoodData(double Eng, String Pro, String Fat,
                    String Tsg, String Car, String Na){

        this.Eng = Eng;
        FoodData = new String[]{Car, Tsg, Pro, Fat, Na};
        OneDayBaseLine = new Double[]{324.0,100.0,55.0,54.0,2000.0};
        FoodDataString = new String[]{"Car", "Tsg", "Pro", "Fat", "Na", "name"};
    }

    public Map OneDayBaseLine(){

        percent = new HashMap<>();
        int tmp;

        for(int i=0;i<FoodData.length;i++){
            if(FoodData[i].trim().isEmpty()){
                percent.put(FoodDataString[i],"0");
                continue;
            }
            tmp = (int) Math.round(Double.parseDouble(FoodData[i]) / OneDayBaseLine[i] * 100);
            percent.put(FoodDataString[i],Integer.toString(tmp));
        }

        return percent;
    }

    public boolean checkHighProtein(){

        if(55 * 5/100 <= 100 / Eng * Double.parseDouble(FoodData[2])) return true;
        return false;
    }
    public int getFoodDataSize(){
        return FoodData.length;
    }

    public String getKcal(){
        return Double.toString(Eng);
    }

    public String[] getFoodData() {
        return FoodData;
    }

    public String[] getFoodDataString() {
        return FoodDataString;
    }

    
}
