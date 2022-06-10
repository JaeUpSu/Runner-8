package com.example.Runner8.ui.trash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Runner8.R;
import com.example.Runner8.ui.F_H.Cart.NewCart.FoodCartActivity;
import com.example.Runner8.ui.F_H.calorieDictionary.FoodData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FoodDetailActivity extends AppCompatActivity {

    Map<String, String> map  = new HashMap<>();

    TextView textView, kcal, protein, fat, sugars, carbohydrate, total, naturim, HighProtein;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FloatingActionButton cart;

    String FoodName="", total_kcal;

    FoodData foodData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_food_detail);

        // View 객체설정
        textView =  findViewById(R.id.이름);
        kcal = findViewById(R.id.열량);
        protein = findViewById(R.id.단백질);
        fat = findViewById(R.id.지방);
        sugars = findViewById(R.id.당류);
        carbohydrate = findViewById(R.id.탄수화물);
        total = findViewById(R.id.총내용량);
        naturim = findViewById(R.id.나트륨);
        HighProtein = findViewById(R.id.HighProtein);
        cart = findViewById(R.id.cart);

        // (Firebase) 사용자 정보 얻기 위한 객체
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // string-array
        String[] array = getResources().getStringArray(R.array.checkSpecialFood);

        // Intent 데이터 얻기
        Intent intent = getIntent();
        FoodName = intent.getStringExtra("FoodName");

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> map = new HashMap<>();

                map.put("Eng",foodData.getKcal());

                for(int i=0;i<foodData.getFoodDataSize();i++){
                    map.put(foodData.getFoodDataString()[i], foodData.getFoodData()[i]);
                }

                DocumentReference docRef = db.collection("Users").document(user.getUid())
                        .collection("FoodCart").document(FoodName);

                DocumentReference total_kcal_of_day = db.collection("Users").document(user.getUid());

                total_kcal_of_day.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                total_kcal = task.getResult().get("TotalKcalOfDay").toString();
                            }
                        });

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        if(!document.exists()){
                            Map<String,Object> data = new HashMap<>();
                            docRef.set(data);
                            map.put("Count",1);
                            docRef.update(map);
                        }
                        else{
                            int count = Integer.parseInt(document.getData().get("Count").toString());
                            map.put("Count",++count);
                            docRef.update(map);
                        }

                        Double sum = Double.valueOf(total_kcal);
                        sum += Double.valueOf(foodData.getKcal());
                        total_kcal_of_day.set(String.valueOf(sum));

                        Intent intent = new Intent(FoodDetailActivity.this, FoodCartActivity.class);
                        intent.putExtra("Map", (Serializable) map);
                        startActivity(intent);

                        Toast.makeText(FoodDetailActivity.this, "장바구니에 추가하셨습니다!!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



        // Layout 표현
        db.collection("Calorie").document("Food")
                .collection("Dictionary").document(FoodName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.getResult() != null){

                            DocumentSnapshot ds = task.getResult();
                            Map<String, Object> FoodDetail = ds.getData();
                            Map<String, String> OneDayBaseLine = new HashMap<>();

                            Log.i("FoodDetail", FoodDetail.toString());

                            foodData = new FoodData(Double.parseDouble(FoodDetail.get("열량").toString()),
                                    FoodDetail.get("단백질").toString(), FoodDetail.get("지방").toString(),
                                    FoodDetail.get("당류").toString(), FoodDetail.get("탄수화물").toString(),
                                    FoodDetail.get("나르륨").toString());

                            OneDayBaseLine.putAll(foodData.OneDayBaseLine());

                            if(foodData.checkHighProtein())
                                HighProtein.setTextColor(getResources().getColor(R.color.highProtein));

                            textView.setText(FoodDetail.get("이름").toString());
                            total.setText(FoodDetail.get("총내용량").toString());
                            sugars.setText(FoodDetail.get("당류").toString() + "  " + OneDayBaseLine.get("Tsg") + "%");
                            kcal.setText(FoodDetail.get("열량").toString());
                            protein.setText(FoodDetail.get("단백질").toString() + "  " + OneDayBaseLine.get("Pro") + "%");
                            fat.setText(FoodDetail.get("지방").toString() +"  " + OneDayBaseLine.get("Fat") + "%");
                            carbohydrate.setText(FoodDetail.get("탄수화물").toString() + "  " + OneDayBaseLine.get("Car") + "%");
                            naturim.setText(FoodDetail.get("나르륨").toString() + "  " + OneDayBaseLine.get("Na") + "%");
                        }
                    }
                });

    }
}
