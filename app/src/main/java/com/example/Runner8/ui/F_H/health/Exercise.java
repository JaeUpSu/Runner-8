package com.example.Runner8.ui.F_H.health;

import com.example.Runner8.R;

public class Exercise {

    String[] healthName = new String[]{"걷 기","조 깅","사이클","웨이트","수 영","등 산","축 구","농 구","야 구"};
    Integer[] healthSrc = new Integer[]{
        R.drawable.walk_selector, R.drawable.running_selector
            , R.drawable.bicycle_selector, R.drawable.dumbbell_selector
            , R.drawable.swimmer_selector, R.drawable.hike_selector, R.drawable.football_selector
            , R.drawable.basketball_selector, R.drawable.baseball_selector };

    double[] met_num = new double[]{3.0,3.5,3.5,4.0,4.5,5.0,6.0,6.0,6.5,7.0,7.0,7.0,7.0,7.5,
            8.0,8.0,8.0,10.0,10.0,15.0};

    public int getImageSource(String exercise){

        int result = 0;
        switch (exercise){
            case "걷 기":
                result = R.drawable.walk_selector;
                break;
            case "조 깅":
                result = R.drawable.running_selector;
                break;
            case "사이클":
                result = R.drawable.bicycle_selector;
                break;
            case "웨이트":
                result = R.drawable.dumbbell_selector;
                break;
            case "수 영":
                result = R.drawable.swimmer_selector;
                break;
            case "등 산":
                result = R.drawable.hike_selector;
                break;
            case "축 구":
                result = R.drawable.football_selector;
                break;
            case "농 구":
                result = R.drawable.basketball_selector;
                break;
            case "야 구":
                result = R.drawable.baseball_selector;
            case "Direct":
                result = R.drawable.health;
                break;


        }
        return result;
    }
    public double getMetNum(String exercise){
        double result=0.0;
        switch (exercise){
            case "걷 기":
                result = R.drawable.walk_selector;
                break;
            case "조 깅":
                result = R.drawable.running_selector;
                break;
            case "사이클":
                result = R.drawable.bicycle_selector;
                break;
            case "웨이트":
                result = R.drawable.dumbbell_selector;
                break;
            case "수 영":
                result = R.drawable.swimmer_selector;
                break;
            case "등 산":
                result = R.drawable.hike_selector;
                break;
            case "축 구":
                result = R.drawable.football_selector;
                break;
            case "농 구":
                result = R.drawable.basketball_selector;
                break;
            case "야 구":
                result = R.drawable.baseball_selector;
                break;


        }
        return result;
    }
}
