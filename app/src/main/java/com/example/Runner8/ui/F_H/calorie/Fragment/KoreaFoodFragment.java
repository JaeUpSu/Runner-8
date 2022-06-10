package com.example.Runner8.ui.F_H.calorie.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.F_H.calorie.Adapter.FoodQuickAdapter;
import com.example.Runner8.ui.F_H.calorie.Adapter.Model.FoodData;
import com.example.Runner8.ui.F_H.calorie.KoreaFoodData;
import com.example.Runner8.ui.F_H.calorie.SingleTon.CalorieSingleTon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class KoreaFoodFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private NavController navController;

    String foodClassName;

    RecyclerView view_quickFood;
    ArrayList<FoodData> list_quickFood;
    FoodQuickAdapter adapter;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        Log.i("LIFECYCILE", "onCreateView");
        navController = Navigation.findNavController(getActivity(), R.id.nav_food_rep_fragment);

        return inflater.inflate(R.layout.activity_korea_foodquick, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view_quickFood = view.findViewById(R.id.list_korea_FoodQuick);

        view_quickFood.setLayoutManager(new LinearLayoutManager(getContext()));

        // setSample();
        list_quickFood = new ArrayList<>();
        adapter = new FoodQuickAdapter(list_quickFood);
        view_quickFood.setAdapter(adapter);

        adapter.setOnItemClickListener((holder, v, position) -> {
            FoodData foodData = list_quickFood.get(position);

            CalorieSingleTon.getInstance().setFoodData(foodData);
            navController.navigate(R.id.nav_food_detail);

        });

        foodClassName = CalorieSingleTon.getInstance().getKoreaFoodName();
        KoreaFoodData koreaFoodData = new KoreaFoodData();

        db.collection("Calorie").document("Food")
                .collection("한식").document(foodClassName)
                .collection("data")
                .orderBy("index")
                .get()
                .addOnCompleteListener(task -> {

                    int i=0;

                    for(QueryDocumentSnapshot document : task.getResult()){
                        FoodData foodData = new FoodData();
                        Log.i("id", document.getId());
                        Log.i("content", document.get("content").toString());

                        foodData.setName(document.getId());
                        foodData.setF_class("한식, " + foodClassName);
                        foodData.setKcal(Double.valueOf(document.get("Eng").toString()));
                        foodData.setImg_src(koreaFoodData.getKoreaFoodData(foodClassName).get(i++));
                        foodData.setCar(Double.valueOf(document.get("Car").toString()));
                        foodData.setContent(document.get("content").toString());
                        foodData.setFat(Double.valueOf(document.get("Fat").toString()));
                        foodData.setPro(Double.valueOf(document.get("Pro").toString()));

                        list_quickFood.add(foodData);
                    }
                    adapter.notifyDataSetChanged();
                });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
