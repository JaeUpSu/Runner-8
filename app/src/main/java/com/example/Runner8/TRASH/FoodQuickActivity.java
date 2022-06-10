package com.example.Runner8.TRASH;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.F_H.calorie.Adapter.FoodQuickAdapter;
import com.example.Runner8.ui.F_H.calorie.Adapter.Model.FoodData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class FoodQuickActivity extends AppCompatActivity {

    RecyclerView view_quickFood;
    ArrayList<FoodData> list_quickFood;
    FoodQuickAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodquick);

        java.lang.String food_class = getIntent().getStringExtra("class");

        view_quickFood = findViewById(R.id.list_FoodQuick);
        view_quickFood.setLayoutManager(new LinearLayoutManager(FoodQuickActivity.this));

        list_quickFood = new ArrayList<>();
        // setSample();
        adapter = new FoodQuickAdapter(list_quickFood);
        view_quickFood.setAdapter(adapter);
        for (FoodData data : list_quickFood){
            Log.i("data", data.getName());
            Log.i("src",data.getImg_src()+"");
        }

        getFoodData(food_class);

        adapter.setOnItemClickListener((holder, v, position) -> {
            FoodData foodData = list_quickFood.get(position);

            Intent intent = new Intent(this, FoodDetailActivity.class);
            intent.putExtra("food_data", (Serializable) foodData);
            startActivity(intent);
        });


    }

    public void getFoodData(java.lang.String food_class){

        db.collection("Calorie").document("Food")
                .collection(food_class)
                .get()
                .addOnCompleteListener(task -> {
                    for(QueryDocumentSnapshot document : task.getResult()){
                        FoodData foodData = new FoodData();
                        Log.i("id", document.getId());
                        foodData.setName(document.getId());
                        foodData.setF_class(food_class);
                        // foodData.setKcal(Double.valueOf(document.get("kcal").toString()));
                        // foodData.setCar(Double.valueOf(document.get("Car").toString()));
                        // foodData.setContent(Double.valueOf(document.get("content").toString()));
                        // foodData.setFat(Double.valueOf(document.get("Fat").toString()));
                        // foodData.setPro(Double.valueOf(document.get("Pro").toString()));
                        // foodData.setKcal(Double.valueOf(document.get("열량").toString()));
                        list_quickFood.add(foodData);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void setSample(){



        java.lang.String[] name = new java.lang.String[]{"소갈비찜", "짜장면", "족 발", "라 면", "삼겹살", "떡볶이"};
        Double[] kcal = new Double[]{300.0, 230.0, 250.0, 313.0, 240.0, 400.0};

        /*
        Integer[] src = new Integer[]{R.drawable.foodex,R.drawable.foodex2,R.drawable.foodex3,
                    R.drawable.foodex4,R.drawable.foodex5,R.drawable.foodex6};

         */

        int i = 0;

        for (java.lang.String food : name){
            FoodData data = new FoodData();
            data.setName(food);
            // data.setImg_src(src[i]);
            data.setKcal(kcal[i]);
            list_quickFood.add(data);
            i++;
        }

    }
}