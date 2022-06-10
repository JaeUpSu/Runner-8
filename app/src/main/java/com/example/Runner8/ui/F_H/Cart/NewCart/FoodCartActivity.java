package com.example.Runner8.ui.F_H.Cart.NewCart;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.example.Runner8.ui.F_H.calorie.Adapter.Model.FoodData;
import com.example.Runner8.ui.F_H.calorie.SingleTon.CalorieSingleTon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.dionsegijn.steppertouch.StepperTouch;

public class FoodCartActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    RecyclerView foodList;
    StepperTouch numBtn;
    TextView txt_totalKcal, txt_totalMeal, txt_totalProtein, txt_cartTitle;
    ImageButton btn_delete;

    FoodCartAdapter foodCartAdapter;
    Boolean pick_flag = false;
    Integer pick_pos, pick_num;

    double total_kcal;
    double total_pro;
    double total_car;
    int food_count;

    java.lang.String foodName = "";
    List list = new ArrayList();

    CollectionReference docRef = db.collection("Users")
            .document(user.getUid()).collection("FoodCart");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_cart_frag);

        foodList = findViewById(R.id.foodCartList);
        numBtn = findViewById(R.id.stepperTouch);
        txt_totalKcal = findViewById(R.id.txt_totalKcal);
        txt_totalMeal = findViewById(R.id.txt_totalMeal);
        txt_totalProtein = findViewById(R.id.txt_totalProtein);
        btn_delete = findViewById(R.id.btn_delete);
        txt_cartTitle = findViewById(R.id.txt_cartTitle);

        pick_pos = -1;
        sampleDataArr();


        foodCartAdapter = new FoodCartAdapter(CalorieSingleTon.getInstance().getFoodArrayList());
        foodList.setLayoutManager(new LinearLayoutManager(this
                , LinearLayoutManager.VERTICAL, false));
        foodList.setAdapter(foodCartAdapter);

        foodCartAdapter.setOnItemClickListener((holder, v, position) -> {

            setCheckedDataChange(position);

            if (!pick_pos.equals(-1)){
                numBtn.setCount(CalorieSingleTon.getInstance().getFoodArrayList().get(pick_pos).getNum());
                numBtn.setMinValue(1);
                numBtn.setSideTapEnabled(true);
            }

        });

        numBtn.addStepCallback((i, b) -> {
            Log.i("addStepCallback", "addStepCallback");
            FoodData pick_food = CalorieSingleTon.getInstance().getFoodArrayList().get(pick_pos);

            double pick_kcal = pick_food.getKcal();
            double pick_pro = pick_food.getPro();
            double pick_car = pick_food.getCar();

            Map<java.lang.String, Object> map = new HashMap<>();

            if(pick_num < numBtn.getCount()){

                total_kcal += pick_kcal;
                total_pro += pick_pro;
                total_car += pick_car;
                pick_num += 1;

            }
            else if(pick_num > numBtn.getCount()){
                total_kcal -= pick_kcal;
                total_pro -= pick_pro;
                total_car -= pick_car;
                pick_num -= 1;
            }

            map.put("Count", pick_num);
            docRef.document(pick_food.getName()).update(map);

            // 총 칼로리
            Map<java.lang.String, Object> total_map = new HashMap<>();

            total_map.put("TotalFoodKcalOfDay", total_kcal);
            Sub_bundle.getInstance().setTotal_food_kcal(java.lang.String.valueOf(total_kcal));

            db.collection("Users").document(user.getUid()).update(total_map);

            numBtn.allowNegative(pick_num > numBtn.getMinValue());
            pick_food.setNum(numBtn.getCount());

            foodCartAdapter.notifyDataSetChanged();

            // setText
            txt_totalKcal.setText("Total Kcal : " + Math.round(total_kcal));
            txt_totalProtein.setText("Total Protein : " + Math.round(total_pro));
            txt_totalMeal.setText("( 총 탄수화물 : " + Math.round(total_car) + " )");
        });
        btn_delete.setOnClickListener(v -> onDelete(pick_pos));

        //////////////////////////////////////////////////////////////////////////////////////////
    }

    public void sampleDataArr(){

        CalorieSingleTon.getInstance().getFoodArrayList().clear();

        // 데이터 받아오기
        docRef.get().addOnCompleteListener(task -> {


            if(task.isSuccessful()){

                food_count = task.getResult().size();
                total_kcal = 0.0;
                total_pro = 0.0;
                total_car = 0.0;

                for(QueryDocumentSnapshot docSnap : task.getResult()){

                    int count = Integer.valueOf(docSnap.get("Count").toString());
                    double kcal = Double.valueOf(docSnap.get("Eng").toString());

                    double pro = 0, car = 0;
                    if(docSnap.get("Pro") != null){
                        pro = Double.valueOf(docSnap.get("Pro").toString());
                    }
                    if(docSnap.get("Car") != null){
                        car = Double.valueOf(docSnap.get("Car").toString());
                    }

                    FoodData foodData = new FoodData();
                    foodData.setName(docSnap.get("name").toString());
                    foodData.setNum(count);
                    foodData.setKcal(kcal);
                    total_kcal += count * kcal;
                    total_pro += count * pro;
                    total_car += count * car;

                    CalorieSingleTon.getInstance().getFoodArrayList().add(foodData);
                }
                txt_totalKcal.setText("Total Kcal : " + Math.round(total_kcal));
                txt_totalProtein.setText("Total Protein : " + Math.round(total_pro));
                txt_totalMeal.setText("( 총 탄수화물 : " + Math.round(total_car) + " )");
                txt_cartTitle.setText(food_count + " Foods in Cart");

                foodCartAdapter.notifyDataSetChanged();
            }
        });

    }

    public void setCheckedDataChange(int position){

        // pick_flag - 뭐라도 선택이 되어있을때 true
        if (pick_flag){
            if (!CalorieSingleTon.getInstance().getFoodArrayList().get(position).getChecked()){
                CalorieSingleTon.getInstance().getFoodArrayList().get(position).setChecked(true);
                pick_pos = -1;
                pick_flag = false;
                numBtn.setVisibility(View.INVISIBLE);
                btn_delete.setVisibility(View.INVISIBLE);
            }
            else {
                CalorieSingleTon.getInstance().getFoodArrayList().get(pick_pos).setChecked(true);
                CalorieSingleTon.getInstance().getFoodArrayList().get(position).setChecked(false);
                pick_pos = position;
                numBtn.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
                pick_num = CalorieSingleTon.getInstance().getFoodArrayList().get(position).getNum();
            }
        }else{
            pick_pos = position;
            pick_flag =true;
            CalorieSingleTon.getInstance().getFoodArrayList().get(position).setChecked(false);
            numBtn.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(View.VISIBLE);
            pick_num = CalorieSingleTon.getInstance().getFoodArrayList().get(position).getNum();
        }
        foodCartAdapter.notifyDataSetChanged();
    }
    public void onDelete(int position) {

        FoodData pick_food = CalorieSingleTon.getInstance().getFoodArrayList().get(pick_pos);

        int count = pick_food.getNum();
        double pick_kcal = pick_food.getKcal();
        double pick_pro = pick_food.getPro();
        double pick_car = pick_food.getCar();
        total_kcal -= count * pick_kcal;
        total_pro -= count * pick_pro;
        total_car -= count * pick_car;
        food_count -= 1;

        // setText
        txt_totalKcal.setText("Total Kcal : " + Math.round(total_kcal));
        txt_totalProtein.setText("Total Protein : " + Math.round(total_pro));
        txt_totalMeal.setText("( 총 탄수화물 : " + Math.round(total_car) + " )");
        txt_cartTitle.setText(food_count + " Foods in Cart");

        // singleTon
        Sub_bundle.getInstance().setTotal_food_kcal(java.lang.String.valueOf(total_kcal));

        // db
        docRef.document(pick_food.getName()).delete();


        CalorieSingleTon.getInstance().removeFoodArrayList(position);

        pick_flag = false;
        numBtn.setVisibility(View.INVISIBLE);
        btn_delete.setVisibility(View.INVISIBLE);
        foodCartAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
