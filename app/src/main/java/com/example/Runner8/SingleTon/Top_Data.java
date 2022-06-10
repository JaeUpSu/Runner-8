package com.example.Runner8.SingleTon;

import android.util.Log;

import com.example.Runner8.ui.summary.Model.Top_count_model;
import com.example.Runner8.ui.summary.Model.Top_kcal_model;
import com.example.Runner8.ui.summary.Model.Top_pro_model;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Top_Data {

    ArrayList<Top_count_model> top_count = new ArrayList<>();
    ArrayList<Top_pro_model> top_high_pro = new ArrayList<>();
    ArrayList<Top_kcal_model> top_high_kcal = new ArrayList<>();
    ArrayList<Top_kcal_model> top_row_kcal = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DocumentReference dr_summary = db.collection("Users").document(user.getUid())
            .collection("user_data").document("summary");


    private static final Top_Data TOP_DATA_INSTANCE = new Top_Data();

    public static synchronized Top_Data getInstance(){
        return TOP_DATA_INSTANCE;
    }

    public ArrayList<Top_count_model> getTop_count() {
        return top_count;
    }
    public void addTop_count(Top_count_model top_count_model){
        this.top_count.add(top_count_model);
    }
    public void clearTop_count(){
        this.top_high_pro.clear();
    }

    public ArrayList<Top_pro_model> getTop_high_pro() {
        return top_high_pro;
    }
    public void addTop_high_pro(Top_pro_model top_pro_model){
        this.top_high_pro.add(top_pro_model);
    }
    public void clearTop_high_pro(){
        this.top_high_pro.clear();
    }

    public ArrayList<Top_kcal_model> getTop_high_kcal() {
        return top_high_kcal;
    }
    public void addTop_high_kcal(Top_kcal_model top_kcal){
        this.top_high_kcal.add(top_kcal);
    }
    public void clearTop_high_kcal(){
        this.top_high_kcal.clear();
    }

    public ArrayList<Top_kcal_model> getTop_row_kcal() {
        return top_row_kcal;
    }
    public void addTop_row_kcal(Top_kcal_model top_kcal){
        this.top_row_kcal.add(top_kcal);
    }
    public void clearTop_row_kcal(){
        this.top_row_kcal.clear();
    }

    public void Top_Data_Update(String classification, Task<QuerySnapshot> task){

        int count = 1;

        switch (classification){
            case "count":
                Top_Data.getInstance().clearTop_count();

                if(task.getResult().size() != 0) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Map<String, Object> map = new HashMap<>();
                        Top_count_model top_count_model = new Top_count_model();

                        int food_count = Integer.valueOf(document.get("Count").toString());

                        map.put("name", document.getId());
                        map.put("Count", food_count);

                        top_count_model.setFood_count(food_count);
                        top_count_model.setFood_name(document.getId());

                        Top_Data.getInstance().addTop_count(top_count_model);

                        dr_summary.collection("TOP_DATA").document("count")
                                .collection("data").document(String.valueOf(count)).set(map);
                        count++;
                    }
                }
                break;
            case "pro":
                Log.i("pro", "pro");
                Top_Data.getInstance().clearTop_high_pro();

                if(task.getResult().size() != 0) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Map<String, Object> map = new HashMap<>();
                        Top_pro_model top_pro_model = new Top_pro_model();

                        double food_pro = Double.valueOf(document.get("Pro").toString());

                        map.put("name", document.getId());
                        map.put("Pro", food_pro);

                        Log.i("PRO", food_pro + "");

                        top_pro_model.setPro(food_pro);
                        top_pro_model.setFood_name(document.getId());

                        Top_Data.getInstance().addTop_high_pro(top_pro_model);

                        dr_summary.collection("TOP_DATA").document("pro")
                                .collection("data").document(String.valueOf(count)).set(map);
                        count++;
                    }
                }
                break;

        }
    }
    public void Top3_kcal_month_food(Query.Direction query){

        boolean high_check;
        String input_documentID;
        if(query == Query.Direction.ASCENDING) {
            high_check = false;
            input_documentID = "row_kcal";
        }
        else {
            high_check = true;
            input_documentID = "high_kcal";
        };

        dr_summary.collection("month_cart")
                .orderBy("Eng", query)
                .limit(3)
                .get()
                .addOnCompleteListener(task -> {
                    int count = 1;

                    if(high_check) Top_Data.getInstance().clearTop_high_kcal();
                    else Top_Data.getInstance().clearTop_row_kcal();

                    if(task.getResult().size() != 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Map<String, Object> map = new HashMap<>();
                            Top_kcal_model top_kcal = new Top_kcal_model();

                            String name = document.getId();
                            double kcal = Double.valueOf(document.get("Eng").toString());

                            map.put("name", document.getId());
                            map.put("Eng", kcal);

                            top_kcal.setFood_name(name);
                            top_kcal.setKcal(kcal);

                            if(high_check) Top_Data.getInstance().addTop_high_kcal(top_kcal);
                            else Top_Data.getInstance().addTop_row_kcal(top_kcal);

                            dr_summary.collection("TOP_DATA").document(input_documentID)
                                    .collection("data").document(String.valueOf(count)).set(map);
                            count++;
                        }
                    }
                });
    }
    public void getTop3_data(){
        getTop3_count_month_food();
        getTop3_pro_month_food();
        getTop3_high_kcal_month_food();
        getTop3_row_kcal_month_food();
    }

    public void getTop3_count_month_food() {
        dr_summary.collection("TOP_DATA").document("count")
                .collection("data")
                .get()
                .addOnCompleteListener(task -> {

                    if (task.getResult().size() != 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Top_count_model top_count_model = new Top_count_model();

                            int count = Integer.valueOf(document.get("Count").toString());
                            String name = document.get("name").toString();

                            top_count_model.setFood_count(count);
                            top_count_model.setFood_name(name);

                            addTop_count(top_count_model);
                        }
                    }
                });
    }
    public void getTop3_pro_month_food() {
        dr_summary.collection("TOP_DATA").document("pro")
                .collection("data")
                .get()
                .addOnCompleteListener(task -> {

                    if (task.getResult().size() != 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Top_pro_model top_pro_model = new Top_pro_model();

                            double pro = Double.valueOf(document.get("Pro").toString());
                            String name = document.get("name").toString();

                            top_pro_model.setPro(pro);
                            top_pro_model.setFood_name(name);

                            addTop_high_pro(top_pro_model);
                        }
                    }
                });
    }

    public void getTop3_high_kcal_month_food() {
        dr_summary.collection("TOP_DATA").document("high_kcal")
                .collection("data")
                .get()
                .addOnCompleteListener(task -> {

                    if (task.getResult().size() != 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Top_kcal_model top_kcal_model = new Top_kcal_model();

                            double kcal = Double.valueOf(document.get("Eng").toString());
                            String name = document.get("name").toString();

                            top_kcal_model.setKcal(kcal);
                            top_kcal_model.setFood_name(name);

                            addTop_high_kcal(top_kcal_model);
                        }
                    }
                });
    }
    public void getTop3_row_kcal_month_food() {
        dr_summary.collection("TOP_DATA").document("row_kcal")
                .collection("data")
                .get()
                .addOnCompleteListener(task -> {

                    if (task.getResult().size() != 0) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Top_kcal_model top_row_model = new Top_kcal_model();

                            double kcal = Double.valueOf(document.get("Eng").toString());
                            String name = document.get("name").toString();

                            top_row_model.setKcal(kcal);
                            top_row_model.setFood_name(name);

                            addTop_row_kcal(top_row_model);
                        }
                    }
                });
    }

}
