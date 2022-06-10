package com.example.Runner8.ui.Parsing;

import java.util.HashMap;
import java.util.Map;

public class MinistryOfFoodAndDrugSafety {
    Map<String, String> tagName = new HashMap<>();
    Map<String, Boolean> bSetName = new HashMap<>();

    String NUM;            // 번호
    String FOOD_CD;        // 식품코드
    String DESC_KOR;       // 이름
    String SERVING_SIZE;   // 총 내용량
    String NUTR_CONT1;     // 열량(kcal)(1회 제공량)
    String NUTR_CONT2;     // 탄수화물(g)(1회 제공량)
    String NUTR_CONT3;     // 단백질(g)(1회 제공량)
    String NUTR_CONT4;     // 지방(g)(1회 제공량)
    String NUTR_CONT5;     // 당류(g)(1회 제공량)
    String NUTR_CONT6;     // 나트륨(mg)(1회 제공량)
    String NUTR_CONT7;     // 콜레스테롤(mg)(1회 제공량)
    String NUTR_CONT8;     // 포화지방산(mg)(1회 제공량)
    String NUTR_CONT9;    // 트랜스지방(mg)(1회 제공량)

    boolean bSet_NUM;            // 번호
    boolean bSet_FOOD_CD;        // 식품코드
    boolean bSet_DESC_KOR;       // 이름
    boolean bSet_SERVING_SIZE;   // 총 내용량
    boolean bSet_NUTR_CONT1;     // 열량(kcal)(1회 제공량)
    boolean bSet_NUTR_CONT2;     // 탄수화물(g)(1회 제공량)
    boolean bSet_NUTR_CONT3;     // 단백질(g)(1회 제공량)
    boolean bSet_NUTR_CONT4;     // 지방(g)(1회 제공량)
    boolean bSet_NUTR_CONT5;     // 당류(g)(1회 제공량)
    boolean bSet_NUTR_CONT6;     // 나트륨(mg)(1회 제공량)
    boolean bSet_NUTR_CONT7;     // 콜레스테롤(mg)(1회 제공량)
    boolean bSet_NUTR_CONT8;     // 포화지방산(mg)(1회 제공량)
    boolean bSet_NUTR_CONT9;

    public MinistryOfFoodAndDrugSafety(){

        tagName.put("NUM", "");
        tagName.put("FOOD_CD", "");
        tagName.put("DESC_KOR", "");
        tagName.put("SERVING_SIZE", "");
        tagName.put("NUTR_CONT1", "");
        tagName.put("NUTR_CONT2", "");
        tagName.put("NUTR_CONT3", "");
        tagName.put("NUTR_CONT4", "");
        tagName.put("NUTR_CONT5", "");
        tagName.put("NUTR_CONT6", "");
        tagName.put("NUTR_CONT7", "");
        tagName.put("NUTR_CONT8", "");
        tagName.put("NUTR_CONT9", "");

        bSetName.put("bSet_NUM", false);
        bSetName.put("bSet_FOOD_CD", false);
        bSetName.put("bSet_DESC_KOR", false);
        bSetName.put("bSet_SERVING_SIZE", false);
        bSetName.put("bSet_NUTR_CONT1", false);
        bSetName.put("bSet_NUTR_CONT2", false);
        bSetName.put("bSet_NUTR_CONT3", false);
        bSetName.put("bSet_NUTR_CONT4", false);
        bSetName.put("bSet_NUTR_CONT5", false);
        bSetName.put("bSet_NUTR_CONT6", false);
        bSetName.put("bSet_NUTR_CONT7", false);
        bSetName.put("bSet_NUTR_CONT8", false);
        bSetName.put("bSet_NUTR_CONT9", false);

    }

    public Boolean getbSetParsingData(String tagName){
        return bSetName.get(tagName);
    }

    public void bSetParsingData(String tagName, Boolean check){
        bSetName.put(tagName,check);
    }

    public String getParsingData(String tn){
        return tagName.get(tn);
    }

    public void setParsingData(String Name, String tn){ tagName.put(Name,tn); }

    public String changeName(String tagName){
        if(tagName.equals("NUM")) return "번호";
        else if(tagName.equals("FOOD_CD")) return "식품코드";
        else if(tagName.equals("DESC_KOR")) return "이름";
        else if(tagName.equals("SERVING_SIZE")) return "총내용량";
        else if(tagName.equals("NUTR_CONT1")) return "열량";
        else if(tagName.equals("NUTR_CONT2")) return "탄수화물";
        else if(tagName.equals("NUTR_CONT3")) return "단백질";
        else if(tagName.equals("NUTR_CONT4")) return "지방";
        else if(tagName.equals("NUTR_CONT5")) return "당류";
        else if(tagName.equals("NUTR_CONT6")) return "나르륨";
        else if(tagName.equals("NUTR_CONT7")) return "콜레스테롤";
        else if(tagName.equals("NUTR_CONT8")) return "포화지방산";
        else if(tagName.equals("NUTR_CONT9")) return "트랜스지방";
        else return "";
    }
}
