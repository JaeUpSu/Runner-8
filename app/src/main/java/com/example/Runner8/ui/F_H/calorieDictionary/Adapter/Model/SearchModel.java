package com.example.Runner8.ui.F_H.calorieDictionary.Adapter.Model;

import java.io.Serializable;

public class SearchModel implements Serializable {
    private static final long serialVersionUID = 3L;

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

    public void setDESC_KOR(String DESC_KOR) {
        this.DESC_KOR = DESC_KOR;
    }

    public void setFOOD_CD(String FOOD_CD) {
        this.FOOD_CD = FOOD_CD;
    }

    public void setNUM(String NUM) {
        this.NUM = NUM;
    }

    public void setNUTR_CONT1(String NUTR_CONT1) {
        this.NUTR_CONT1 = NUTR_CONT1;
    }

    public void setNUTR_CONT2(String NUTR_CONT2) {
        this.NUTR_CONT2 = NUTR_CONT2;
    }

    public void setNUTR_CONT3(String NUTR_CONT3) {
        this.NUTR_CONT3 = NUTR_CONT3;
    }

    public void setNUTR_CONT4(String NUTR_CONT4) {
        this.NUTR_CONT4 = NUTR_CONT4;
    }

    public void setNUTR_CONT5(String NUTR_CONT5) {
        this.NUTR_CONT5 = NUTR_CONT5;
    }

    public void setNUTR_CONT6(String NUTR_CONT6) {
        this.NUTR_CONT6 = NUTR_CONT6;
    }

    public void setNUTR_CONT7(String NUTR_CONT7) {
        this.NUTR_CONT7 = NUTR_CONT7;
    }

    public void setNUTR_CONT8(String NUTR_CONT8) {
        this.NUTR_CONT8 = NUTR_CONT8;
    }

    public void setNUTR_CONT9(String NUTR_CONT9) {
        this.NUTR_CONT9 = NUTR_CONT9;
    }

    public void setSERVING_SIZE(String SERVING_SIZE) {
        this.SERVING_SIZE = SERVING_SIZE;
    }

    public String getDESC_KOR() {
        return DESC_KOR;
    }

    public String getFOOD_CD() {
        return FOOD_CD;
    }

    public String getNUM() {
        return NUM;
    }

    public String getNUTR_CONT1() {
        return NUTR_CONT1;
    }

    public String getNUTR_CONT2() {
        return NUTR_CONT2;
    }

    public String getNUTR_CONT3() {
        return NUTR_CONT3;
    }

    public String getNUTR_CONT4() {
        return NUTR_CONT4;
    }

    public String getNUTR_CONT5() {
        return NUTR_CONT5;
    }

    public String getNUTR_CONT6() {
        return NUTR_CONT6;
    }

    public String getNUTR_CONT7() {
        return NUTR_CONT7;
    }

    public String getNUTR_CONT8() {
        return NUTR_CONT8;
    }

    public String getNUTR_CONT9() {
        return NUTR_CONT9;
    }

    public String getSERVING_SIZE() {
        return SERVING_SIZE;
    }
}
