package com.example.Runner8.ui.F_H.calorie;

import com.example.Runner8.R;

import java.util.ArrayList;

public class KoreaFoodData {
    ArrayList<Integer> meat = new ArrayList<>();
    ArrayList<Integer> rice = new ArrayList<>();
    ArrayList<Integer> seafood = new ArrayList<>();
    ArrayList<Integer> soup = new ArrayList<>();
    ArrayList<Integer> flour = new ArrayList<>();

    public KoreaFoodData(){
        this.meat.add(R.drawable.samgyeobsal);
        this.meat.add(R.drawable.darkgalbi);
        this.meat.add(R.drawable.porkgalbi);
        this.meat.add(R.drawable.sogalbi);
        this.meat.add(R.drawable.sogogi);
        this.meat.add(R.drawable.porkfoot);
        this.meat.add(R.drawable.bossam);
        this.meat.add(R.drawable.zzimdark);
        this.meat.add(R.drawable.gobchang);
        this.meat.add(R.drawable.darkbal);
        this.meat.add(R.drawable.yookhae);

        this.seafood.add(R.drawable.jogaegui);
        this.seafood.add(R.drawable.janeogui);
        this.seafood.add(R.drawable.ggotgaezzim);
        this.seafood.add(R.drawable.hae);
        this.seafood.add(R.drawable.godeungeogui);
        this.seafood.add(R.drawable.ggonchigui);
        this.seafood.add(R.drawable.yangnyeomgaezang);
        this.seafood.add(R.drawable.ganzanggaezang);

        this.rice.add(R.drawable.bob);
        this.rice.add(R.drawable.jobgokbob);
        this.rice.add(R.drawable.hatban);
        this.rice.add(R.drawable.yookhaebibimbob);
        this.rice.add(R.drawable.dolsot);
        this.rice.add(R.drawable.jook);

        this.flour.add(R.drawable.kalguksu);
        this.flour.add(R.drawable.suzaebi);
        this.flour.add(R.drawable.mulnaengmyeon);
        this.flour.add(R.drawable.bibimnaengmeyon);
        this.flour.add(R.drawable.makguksu);
        this.flour.add(R.drawable.koreapancake);

        this.soup.add(R.drawable.soondubuzzigae);
        this.soup.add(R.drawable.kimchizzigae);
        this.soup.add(R.drawable.dangzanguk);
        this.soup.add(R.drawable.budaezzigae);
        this.soup.add(R.drawable.miyukguk);
        this.soup.add(R.drawable.yookgaezang);
        this.soup.add(R.drawable.gamzatang);
        this.soup.add(R.drawable.sundaeguk);
        this.soup.add(R.drawable.suleongtang);
        this.soup.add(R.drawable.maeuntang);
        this.soup.add(R.drawable.samgyetang);
        this.soup.add(R.drawable.tteokguk);
        this.soup.add(R.drawable.manduguk);
        this.soup.add(R.drawable.sagolgomtang);
    }

    public ArrayList<Integer> getKoreaFoodData(String className){
        if(className.equals("고기")){
            return meat;
        }
        else if(className.equals("밥")){
            return rice;
        }
        else if(className.equals("해산물")){
            return seafood;
        }
        else if(className.equals("국,찌개")){
            return soup;
        }
        else if(className.equals("밀가루")){
            return flour;
        }
        return meat;
    }
}
