package com.example.Runner8.ui.F_H.calorie.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.Runner8.R;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.example.Runner8.ui.F_H.Cart.NewCart.FoodCartActivity;
import com.example.Runner8.ui.F_H.calorie.Adapter.Model.FoodData;
import com.example.Runner8.ui.F_H.calorie.SingleTon.CalorieSingleTon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

public class FoodDetailFragment extends Fragment {

    //
    TextView fooddataname, kcaldata, proteindata, fatdata, cycdata, foodStyle, day1_citeria;
    ImageButton detail_cart2;
    LinearLayout high_protein, row_kcal;

    ImageView img_highprotein, img_lowkcal;

    FoodData foodData;

    AppCompatButton btneat_fooddata;

    //
    double TotalFoodKcalOfDay = 0.0;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        Log.i("FragmentLifeCycle", "FoodDetailFragment onAttach!!");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("FragmentLifeCycle", "FoodDetailFragment onDetach!!");
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Log.i("FragmentLifeCycle", "FoodDetailFragment onCreateView!!");

        View view = inflater.inflate(R.layout.foodquickitem_data, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("FragmentLifeCycle", "FoodDetailFragment onViewCreated!!");

        fooddataname = view.findViewById(R.id.fooddataname);
        cycdata = view.findViewById(R.id.cycdata);
        fatdata = view.findViewById(R.id.fatdata);
        proteindata = view.findViewById(R.id.proteindata);
        kcaldata = view.findViewById(R.id.kcaldata);
        foodStyle = view.findViewById(R.id.foodStyle);
        day1_citeria = view.findViewById(R.id.day1_citeria);
        btneat_fooddata = view.findViewById(R.id.btneat_fooddata);
        detail_cart2 = view.findViewById(R.id.detail_cart2);

        row_kcal = view.findViewById(R.id.row_kcal);
        high_protein = view.findViewById(R.id.high_protein);

        foodData = CalorieSingleTon.getInstance().getFoodData();

        btneat_fooddata.setOnClickListener(v -> {

            Map<java.lang.String,Object> map = new HashMap<>();

            map.put("Eng",foodData.getKcal());
            map.put("Pro", foodData.getPro());
            map.put("Fat", foodData.getFat());
            map.put("name", foodData.getName());
            map.put("Car", foodData.getCar());
            map.put("content", foodData.getContent());

            // 해당 음식의 데이터
            DocumentReference docRef_foodName = db.collection("Users").document(user.getUid())
                    .collection("FoodCart").document(foodData.getName());

            // Summary 에 들어갈 데이터
            DocumentReference docRef_summary = db.collection("Users").document(user.getUid())
                    .collection("Summary").document(foodData.getName());

            // 지금까지 먹은 총 먹은 Kcal 가져오기
            if(Sub_bundle.getInstance().getTotal_food_kcal().equals(""))
                TotalFoodKcalOfDay = 0.0;
            else
                TotalFoodKcalOfDay = Double.valueOf(Sub_bundle.getInstance().getTotal_food_kcal());

            db.collection("Users").document(user.getUid())
                    .collection("FoodCart")
                    .whereEqualTo("name", foodData.getName())
                    .get().addOnCompleteListener(task -> {

                        if(task.getResult().size() != 0){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                int count = Integer.parseInt(document.getData().get("Count").toString());
                                map.put("Count",++count);
                                break;
                            }
                        }
                        else{
                            map.put("Count",1);
                        }

                        docRef_foodName.set(map);

                        // 지금까지 먹은 열량 업데이트
                        Map<java.lang.String, Object> totalKcal = new HashMap<>();
                        TotalFoodKcalOfDay += Double.valueOf(foodData.getKcal());
                        totalKcal.put("TotalFoodKcalOfDay", TotalFoodKcalOfDay);

                        db.collection("Users").document(user.getUid())
                                .update(totalKcal);

                        // SingleTon
                        Sub_bundle.getInstance().setTotal_food_kcal(java.lang.String.valueOf(TotalFoodKcalOfDay));

                        Toast.makeText(getContext(), "장바구니에 추가되었습니다.!!", Toast.LENGTH_SHORT).show();
            });
        });
        detail_cart2.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), FoodCartActivity.class);
            startActivity(intent);
        });

        setView();

    }
    public void setView(){

        java.lang.String content = foodData.getContent();
        double protein = foodData.getPro();
        double kcal = foodData.getKcal();

        Log.i("foodData.getName()", foodData.getName());
        fooddataname.setText(foodData.getName());
        day1_citeria.setText(" 1인분 " + java.lang.String.valueOf(foodData.getContent()));
        cycdata.setText(java.lang.String.valueOf(foodData.getCar()));
        fatdata.setText(java.lang.String.valueOf(foodData.getFat()));
        proteindata.setText(java.lang.String.valueOf(foodData.getPro()));
        kcaldata.setText(java.lang.String.valueOf(foodData.getKcal()));
        cycdata.setText(java.lang.String.valueOf(foodData.getCar()));

        if(foodData.getF_class() != null) foodStyle.setText(foodData.getF_class());


        try{
            if(content.matches(".*[ㄱ-ㅎ ㅏ-ㅣ가-힣]+.*")){

            }
            else{
                // 고단백
                // 일일 영양소 기준치 55 , 100 당 기준치의 20%
                if( 55 * 0.2 < 100 / Double.valueOf(content) * protein){
                    high_protein.setVisibility(View.VISIBLE);
                }else{
                    high_protein.setVisibility(View.INVISIBLE);
                }

                // 저칼로리
                // 100g 당 40kcal 미만
                if( 40 > 100 / Double.valueOf(content) * kcal){
                    row_kcal.setVisibility(View.VISIBLE);
                }
                else{
                    row_kcal.setVisibility(View.INVISIBLE);
                }
            }
        }catch (PatternSyntaxException e){
            System.err.println("An Exception Occured");
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("FragmentLifeCycle", "FoodDetailFragment onActivityResult!!");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("FragmentLifeCycle", "FoodDetailFragment onStart!!");
    }

    @Override
    public void onResume() {
        super.onResume();

        FragmentActivity activity = getActivity();
        /*
        if (activity != null){
            ((FoodRepActivity) activity).getSupportActionBar().setTitle("식품 영양 정보");
        }

         */

        Log.i("FragmentLifeCycle", "FoodDetailFragment onResume!!");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("FragmentLifeCycle", "FoodDetailFragment onPause!!");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("FragmentLifeCycle", "FoodDetailFragment onStop!!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("FragmentLifeCycle", "FoodDetailFragment onDestroy!!");
    }
}

