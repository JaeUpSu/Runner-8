package com.example.Runner8.ui.F_H.calorie;

import com.example.Runner8.R;

import java.util.ArrayList;

public class RepData {

    ArrayList<Integer> snackBar = new ArrayList<>();
    ArrayList<Integer> fastFood = new ArrayList<>();
    ArrayList<Integer> koreaFood = new ArrayList<>();
    ArrayList<Integer> chineseFood = new ArrayList<>();
    ArrayList<Integer> japaneseFood = new ArrayList<>();
    ArrayList<Integer> cafeDesert = new ArrayList<>();
    ArrayList<Integer> form = new ArrayList<>();
    ArrayList<Integer> drink = new ArrayList<>();

    public RepData(){

        this.drink.add(R.drawable.beer);
        this.drink.add(R.drawable.cider);
        this.drink.add(R.drawable.soju);
        this.drink.add(R.drawable.yangju);
        this.drink.add(R.drawable.wine);
        this.drink.add(R.drawable.zerocola);
        this.drink.add(R.drawable.cokecola);

        this.form.add(R.drawable.creampasta);
        this.form.add(R.drawable.tomatopasta);
        this.form.add(R.drawable.oilpasta);
        this.form.add(R.drawable.gazdong);
        this.form.add(R.drawable.salad);
        this.form.add(R.drawable.gorgonzola);
        this.form.add(R.drawable.bread);
        this.form.add(R.drawable.steak);

        this.japaneseFood.add(R.drawable.icememil);
        this.japaneseFood.add(R.drawable.garaake);
        this.japaneseFood.add(R.drawable.donkaz);
        this.japaneseFood.add(R.drawable.gazdong);
        this.japaneseFood.add(R.drawable.ramen);
        this.japaneseFood.add(R.drawable.kyudon);
        this.japaneseFood.add(R.drawable.salmondupbab);
        this.japaneseFood.add(R.drawable.occonomiyaki);
        this.japaneseFood.add(R.drawable.udon);
        this.japaneseFood.add(R.drawable.chobab);
        this.japaneseFood.add(R.drawable.hamburgersteak);

        this.fastFood.add(R.drawable.yangnyeomchiken);
        this.fastFood.add(R.drawable.friedchicken);
        this.fastFood.add(R.drawable.frenchfired);
        this.fastFood.add(R.drawable.spagetti);
        this.fastFood.add(R.drawable.cheeseball);
        this.fastFood.add(R.drawable.pizza);
        this.fastFood.add(R.drawable.hamburger);

        this.snackBar.add(R.drawable.chamchikimbab);
        this.snackBar.add(R.drawable.donggaskimbab);
        this.snackBar.add(R.drawable.kimbab);
        this.snackBar.add(R.drawable.cheesekimbab);
        this.snackBar.add(R.drawable.trianglekimbab);
        this.snackBar.add(R.drawable.ojingeofried);
        this.snackBar.add(R.drawable.vegetablefried);
        this.snackBar.add(R.drawable.sweetpotatofried);
        this.snackBar.add(R.drawable.kimmarifried);
        this.snackBar.add(R.drawable.chikenggochi);
        this.snackBar.add(R.drawable.tteokbokki);
        this.snackBar.add(R.drawable.ramyeon);
        this.snackBar.add(R.drawable.jjapageti);
        this.snackBar.add(R.drawable.sotteok);
        this.snackBar.add(R.drawable.soondae);
        this.snackBar.add(R.drawable.fishcake);
        this.snackBar.add(R.drawable.chikenmayo);
        this.snackBar.add(R.drawable.chamchimayo);
        this.snackBar.add(R.drawable.spammayo);

        this.chineseFood.add(R.drawable.maratang);
        this.chineseFood.add(R.drawable.gochujabche);
        this.chineseFood.add(R.drawable.yangjangpy);
        this.chineseFood.add(R.drawable.yanggochi);
        this.chineseFood.add(R.drawable.gunmandu);
        this.chineseFood.add(R.drawable.gganpungi);
        this.chineseFood.add(R.drawable.jjajangbokembab);
        this.chineseFood.add(R.drawable.bokemjjambbong);
        this.chineseFood.add(R.drawable.jjajangmyeon);
        this.chineseFood.add(R.drawable.jjambbong);
        this.chineseFood.add(R.drawable.tangsuyook);

        this.cafeDesert.add(R.drawable.creamcake);
        this.cafeDesert.add(R.drawable.chococake);
        this.cafeDesert.add(R.drawable.latte);
        this.cafeDesert.add(R.drawable.macaron);
        this.cafeDesert.add(R.drawable.bubbletea);
        this.cafeDesert.add(R.drawable.smoothie);
        this.cafeDesert.add(R.drawable.americano);
        this.cafeDesert.add(R.drawable.icecream);
        this.cafeDesert.add(R.drawable.ade);
        this.cafeDesert.add(R.drawable.waffle);
        this.cafeDesert.add(R.drawable.potbingsu);
        this.cafeDesert.add(R.drawable.sandwich);


    }

    public ArrayList<Integer> getFoodList(String foodClass){
        if(foodClass.equals("분식")) return snackBar;
        else if (foodClass.equals("패스트푸드")) return fastFood;
        else if (foodClass.equals("음료")) return drink;
        else if (foodClass.equals("양식")) return form;
        else if (foodClass.equals("중식")) return chineseFood;
        else if (foodClass.equals("일식")) return japaneseFood;
        else if (foodClass.equals("카페,디저트")) return cafeDesert;
        else if (foodClass.equals("한식")) return koreaFood;
        return snackBar;
    }

}
