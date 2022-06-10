package com.example.Runner8.ui.Graph;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

public class GraphPeriod {
    private String period, date;
    private String  pick_period_name;
    private String[] pick_period;
    Integer date_month = 06, date_year = 2021;

    private String[] week = {"","Sun","Mon","Tue","Wed","Tur",
                             "Fri","Sat"};
    private String[] month = {"","1주차","2주차","3주차","4주차"};
    private String[] year = {"","1분기","2분기","3분기","4분기"};

    private Integer[] month_num = {31,28,31,30,31,30,31,31,30,31,30,31};

    private Integer[] data_week = new Integer[8];
    private Integer[] data_month = new Integer[5];
    private Integer[] data_year = new Integer[5];

    int st_num;
    int final_num;

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public GraphPeriod(){ }
    //      1번째 뷰페이저에 쓰일 이중 그래프 + 잔여그래프
    //      Food , Health 막대그래프 + 꺾은선그래프
    public GraphPeriod(String period, String date){
            setPeriod(period);
            setDate(date);
            setperiod_Pick();
    }
    public void setDate(String date){
        this.date =date;
        String[] date_num = date.split("[.]");
//        Log.i("herehrer",String.valueOf(date_num[0]) + String.valueOf(date_num[1]));
        this.date_year = Integer.parseInt(String.valueOf(date_num[0]));
        this.date_month = Integer.parseInt(String.valueOf(date_num[1]));
    }
    public void setPeriod(String period){
        this.period = period;
    }
    public void setperiod_Pick(){
       if(this.period == "일주일")
       {
           this.pick_period_name = "week";
           this.pick_period = week;

       }else if(period == "한달")

       {
           this.pick_period_name = "month";
           this.pick_period = month;
       }else{
               this.pick_period_name = "year";
               this.pick_period = year;
       }
    }

    public String getDate() {
        return date;
    }

    public String getPeriod() {
        return period;
    }

    public ArrayList<String> getPick_GraphLabels() {

        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < pick_period.length; i++){
            labels.add(pick_period[i]);
        }
        return labels;
    }

    public String[] getPick_period() {
        return pick_period;
    }

    public String getPick_period_name() {
        return pick_period_name;
    }

    public Integer[] getMonth_num(){
        ArrayList<Integer> num = new ArrayList<>();
        Integer[] data = {0,0,0,0};
        Random random = new Random();
        int bound = 3000;
        Log.i("tojtojto", String.valueOf(date_month) + String.valueOf(date_year));


        // 랜덤 데이터 뽑아내기

        if(date_month == 2)
            if(date_year % 4 !=0)
                for(int i =0; i < 28; i++)
                    num.add(i, random.nextInt(bound));
            else
                for(int i =0; i < 29; i++)
                    num.add(i, random.nextInt(bound));
        else if(date_month == 4 || date_month == 6 || date_month == 9 || date_month == 11 )
            for(int i =0; i < 30; i++)
                num.add(i, random.nextInt(bound));
        else
            for(int i =0; i < 31; i++)
                num.add(i, random.nextInt(bound));


        Log.i("dssddsdsds", String.valueOf(num.get(2)) + String.valueOf(num.get(3)));
        for (int i = 0; i < 7; i++)
        {
            data[0] += num.get(i);       // 0 ~ 6
            data[1] += num.get(i+7);   // 7 ~ 13
            data[2] += num.get(i+14);  // 14 ~ 20
        }
        for (int i = 21; i < num.size(); i++)
            data[3] += num.get(i); // 21 ~

        return data;
    }
    public Integer[] getYear_num(){
        ArrayList<Integer> data = new ArrayList<>();
        Integer[] num = {0,0,0,0};
        Random random = new Random();
        int bound = 3000;

        if(date_year % 4 !=0) {
            for (int i = 0; i < 365; i++)
                data.add(i, random.nextInt(bound));
            for (int i = 0; i < 90; i++){
                num[0] += data.get(i); // 0~89
                num[1] += data.get(i+90); // 90~179 + 180
                num[2] += data.get(i+181); // 181~270 + 271 + 272
                num[3] += data.get(i+273); // 273~362 + 363 + 364
            }
            num[0] = num[0] / 90;
            num[1] = (num[1] + data.get(180)) / 91;
            num[2] = (num[2] + data.get(271) + data.get(272)) / 92;
            num[3] = (num[3] + data.get(363) + data.get(364)) / 92;
        }else {
            for (int i = 0; i < 366; i++)
                data.add(i, random.nextInt(bound));
            for (int i = 0; i < 91; i++){
                num[0] += data.get(i); // 0~90
                num[1] += data.get(i+91); // 91~181
                num[2] += data.get(i+182); // 182~272 + 273
                num[3] += data.get(i+274); // 274~364 + 365
            }
            num[0] = num[0] / 91;
            num[1] = (num[1] + data.get(180)) / 91;
            num[2] = (num[2] + data.get(273)) / 92;
            num[3] = (num[3] + data.get(365)) / 92;
        }

        return num;
    }
    // 평균
    public ArrayList<Entry> getDataSets2(){
        ArrayList<Entry> entries = new ArrayList<>();
        Random random = new Random();
        int bound = 3000;
        Log.i("THISTHISTHSI", pick_period_name);

        entries.add(new Entry(0,0));
        if(pick_period_name == "week")
            for (int i = 1; i <= pick_period.length-1; i++){
                data_week[i] = random.nextInt(bound);
                entries.add(new Entry(i,data_week[i]));
            }
        else if(pick_period_name == "month"){
            data_month = getMonth_num();

            for (int i = 1; i <= pick_period.length-2; i++)
                entries.add(new Entry(i,data_month[i]/7));
            entries.add(new Entry(4,data_month[3]/(month_num[date_month-1]-21)));
        }else {
            data_year = getYear_num();
            for (int i = 1; i <= pick_period.length-2; i ++) {
                Log.i("pick_period", pick_period[i]);
                entries.add(new Entry(i, data_year[i]));
            }
        }

        return entries;
    }

    /*          막대그래프
    public ArrayList<BarEntry> getDataSets1(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        Random random = new Random();
        Integer[] num = new Integer[4];
        int bound = 3000;
        Log.i("THISTHISTHSI", pick_period_name);

        if(pick_period_name == "week")
            for (int i = 0; i < pick_period.length; i++){
                 data_week[i] = random.nextInt(bound);
                 entries.add(new BarEntry(i,data_week[i]));
            }
        // 1,2,3,4 주차로 평균내는 구간
        else if(pick_period_name == "month"){
            data_month = getMonth_num();

            for (int i = 0; i < pick_period.length-1; i++)
                entries.add(new BarEntry(i,data_month[i]/7));

            // date_month 의 총 일수
            int date_month_amount = month_num[date_month-1];
            entries.add(new BarEntry(3,data_month[3]/(date_month_amount-21)));
        }else {
            data_year = getYear_num();
            for (int i = 0; i < 4; i ++)
                entries.add(new BarEntry(i, data_year[i]));
        }

        return entries;
    }

     */



}
