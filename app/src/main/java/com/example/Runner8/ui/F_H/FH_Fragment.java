package com.example.Runner8.ui.F_H;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.example.Runner8.ui.F_H.Cart.NewCart.FoodCartActivity;
import com.example.Runner8.ui.Graph.GraphActivity;
import com.example.Runner8.ui.F_H.calorie.Adapter.FoodAdapter;
import com.example.Runner8.ui.F_H.calorie.Adapter.Model.FoodData;
import com.example.Runner8.ui.F_H.calorie.Activity.FoodRepActivity;
import com.example.Runner8.ui.F_H.calorie.Activity.FoodSearchActivity;
import com.example.Runner8.ui.F_H.calorie.SingleTon.CalorieSingleTon;
import com.example.Runner8.ui.F_H.health.Adapter.HealthDialogAdapter;
import com.example.Runner8.ui.F_H.health.Adapter.HealthListAdapter;
import com.example.Runner8.ui.F_H.health.Adapter.Model.HealthData;
import com.example.Runner8.ui.F_H.health.Exercise;
import com.example.Runner8.ui.F_H.health.HealthListDialogue;
import com.example.Runner8.ui.F_H.health.MET_DATA;
import com.example.Runner8.ui.F_H.health.SingleTon.HealthSingleTon;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FH_Fragment extends Fragment
        implements View.OnClickListener {

    ConstraintLayout foodLayout, healthLayout;
    RecyclerView foodList1, foodList2, healthList, healthDialogList;
    ImageButton btn_food, btn_foodCart, btn_foodGraph;
    ImageButton btn_foodSearch, btn_foodZero;
    CircularProgressIndicator foodKcalProgress, healthKcalProgress;

    FrameLayout direct_KcalInput;

    FoodAdapter foodAdapter1, foodAdapter2;
    HealthListAdapter healthAdapter;
    ArrayList<FoodData> foodDataList_1, foodDataList_2;
    java.lang.String[] foodName;
    Integer[] foodSrc;
    java.lang.String[] healthName;
    Integer[] healthSrc;

    private FloatingActionButton fab_direct;
    private FABRevealLayout fabRevealLayout;
    private Button ibtn_return;
    private LinearLayout foodLinear, healthLinear;
    private TextView titleText, okText, cancelText;
    private TextView goal_foodKcal, txt_foodKcal, txt_healthKcal;

    // CHECK
    private java.lang.String fh_check = "음식/food";
    private java.lang.String direct_fh_check = "음식/food";
    private boolean directly_check = false;

    // Calculate
    private TextInputEditText txt_directName, txt_directKcal, txt_directPro, txt_directCar;


    // Food
    ImageButton btn_health;

    // Class
    Exercise exercise = new Exercise();

    // health
    private TextInputEditText txt_healthTime;
    private AppCompatButton btn_inputTime, btn_input;
    private double[] met_num = {};
    private HealthData current_healthData, input_healthData;
    private MaterialAutoCompleteTextView txt_calKcal;
    private ImageButton btn_healthList, btn_healthZero, btn_healthGraph;
    private ArrayList<HealthData> healthDataList = new ArrayList<>();
    private HealthDialogAdapter healthDialogAdapter;

    // Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // Reference

    DocumentReference dr_user = db.collection("Users").document(user.getUid());

    CollectionReference cr_health_today = dr_user.collection("Health").document("today")
            .collection("list");

    CollectionReference cr_food_today = dr_user.collection("FoodCart");


    public static FH_Fragment newInstance(int num){
        FH_Fragment fragment = new FH_Fragment();
        Bundle args = new Bundle();
        args.putInt("num",num);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fh, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foodKcalProgress = view.findViewById(R.id.foodKcalProgress);
        healthKcalProgress = view.findViewById(R.id.healthKcalProgress);
        foodList1 = view.findViewById(R.id.foodList1);
        foodList2 = view.findViewById(R.id.foodList2);
        btn_food = view.findViewById(R.id.btn_food);
        foodLayout = view.findViewById(R.id.foodLayout);
        healthList = view.findViewById(R.id.healthList);
        btn_health = view.findViewById(R.id.btn_health);
        healthLayout = view.findViewById(R.id.healthLayout);
        btn_healthList = view.findViewById(R.id.btn_healthList);
        btn_foodCart = view.findViewById(R.id.btn_foodCart);
        btn_foodGraph = view.findViewById(R.id.btn_foodGraph);
        btn_healthGraph = view.findViewById(R.id.btn_healthGraph);
        btn_foodSearch = view.findViewById(R.id.btn_foodSearch);
        btn_foodZero = view.findViewById(R.id.btn_foodZero);
        btn_healthZero = view.findViewById(R.id.btn_healthZero);
        txt_healthKcal = view.findViewById(R.id.txt_healthKcal);
        txt_foodKcal = view.findViewById(R.id.txt_foodKcal);
        goal_foodKcal = view.findViewById(R.id.goal_foodKcal);
        btn_inputTime = view.findViewById(R.id.btn_inputTime);
        txt_healthTime = view.findViewById(R.id.txt_healthTime);
        txt_calKcal = view.findViewById(R.id.txt_calKcal);
        btn_input = view.findViewById(R.id.btn_input);

        direct_KcalInput = view.findViewById(R.id.direct_KcalInput);
        txt_directPro = view.findViewById(R.id.txt_directPro);
        txt_directCar = view.findViewById(R.id.txt_directCar);

        onCreateFood();
        onCreateHealth();

        findViews();
        configureFABReveal();


        setOnclickListener();

        setNoneClickDirectFoodUI();

        fab_direct.setBackgroundColor(Color.TRANSPARENT);
    }
    public void setOnclickListener(){
        btn_healthList.setOnClickListener(this);
        btn_food.setOnClickListener(this);
        btn_health.setOnClickListener(this);
        btn_foodZero.setOnClickListener(this);
        btn_healthZero.setOnClickListener(this);
        btn_healthGraph.setOnClickListener(this);
        btn_foodSearch.setOnClickListener(this);
        btn_foodCart.setOnClickListener(this);
        btn_foodGraph.setOnClickListener(this);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onCreateFood(){

        foodName = new java.lang.String[]{ "한식","일식","중식","양식","분식","패스트푸드","음료수","카페,디저트"};
        foodSrc = new Integer[]{R.drawable.koreafood, R.drawable.japanfood, R.drawable.chinafood
                , R.drawable.steak , R.drawable.snackfood, R.drawable.hamburger, R.drawable.drink
                , R.drawable.cafe};

        foodDataList_1 = new ArrayList<>();
        foodDataList_2 = new ArrayList<>();

        int idx = 0;
        for (java.lang.String name : foodName){
            FoodData data = new FoodData();
            data.setName(name);
            data.setImg_src(foodSrc[idx]);
            if(idx < 4) foodDataList_1.add(data);
            else foodDataList_2.add(data);
            idx++;
        }

        Log.i("foodname", foodDataList_1.get(2).getName()+ foodDataList_2.get(1).getName());
        foodAdapter1 = new FoodAdapter(foodDataList_1);
        foodAdapter2 = new FoodAdapter(foodDataList_2);

        foodList1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        foodList1.setAdapter(foodAdapter1);
        foodList2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        foodList2.setAdapter(foodAdapter2);

        // progress
        double mean = Double.valueOf(Sub_bundle.getInstance().getApp_amount());

        foodKcalProgress.setMax((int) Math.round(mean));

        goal_foodKcal.setText((int) Math.round(mean) + " kcal \n (적정)");

        foodAdapter1.setOnItemClickListener((holder, v, position) -> {
            FoodData data = foodAdapter1.getData(position);
            Intent intent = new Intent(getContext(), FoodRepActivity.class);
            Sub_bundle.getInstance().setRep(data.getName());
            startActivity(intent);
        });

        foodAdapter2.setOnItemClickListener((holder, v, position) -> {
            FoodData data = foodAdapter2.getData(position);
            Intent intent = new Intent(getContext(), FoodRepActivity.class);
            Sub_bundle.getInstance().setRep(data.getName());
            startActivity(intent);
        });

    }

    public void onCreateHealth(){

        MET_DATA met_data = new MET_DATA();
        healthName = new java.lang.String[]{"걷 기","조 깅","사이클","웨이트","수 영","등 산","축 구","농 구","야 구"};
        healthSrc = new Integer[]{R.drawable.walk_selector, R.drawable.running_selector
                , R.drawable.bicycle_selector, R.drawable.dumbbell_selector
                , R.drawable.swimmer_selector, R.drawable.hike_selector, R.drawable.football_selector
                , R.drawable.basketball_selector, R.drawable.baseball_selector };

        //
        double health_kcal = Double.valueOf(Sub_bundle.getInstance().getTotal_health_kcal());
        healthKcalProgress.setMax(300);
        healthKcalProgress.setProgress((int) Math.round(health_kcal));
        txt_healthKcal.setText((int) Math.round(health_kcal) + " kcal\n (현재)");

        int idx = 0;
        for (java.lang.String name : healthName){
            HealthData data = new HealthData();
            data.setName(name);
            data.setImg_src(healthSrc[idx]);
            data.setMet_num(met_data.getMet_num(healthName[idx]));
            healthDataList.add(data);
            idx++;
        }
        healthAdapter = new HealthListAdapter(healthDataList);

        healthList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        healthList.setAdapter(healthAdapter);

        healthAdapter.setOnClickListener((holder, v, position) -> {
            current_healthData = healthAdapter.getData(position);
            int i = 0;
            for (HealthData data :healthDataList){
                if (i != position){
                    data.setBtnChecked(false);
                }
                i++;
            }
            healthDataList.get(position).setBtnChecked(true);
            healthAdapter.notifyDataSetChanged();
        });

        // 시간 입력 -> 칼로리
        btn_inputTime.setOnClickListener(v -> {

            if(current_healthData != null) {
                if (!txt_healthTime.getText().toString().equals("")) {
                    input_healthData = current_healthData;
                    int time = Integer.valueOf(txt_healthTime.getText().toString());
                    double met_num = current_healthData.getMet_num();
                    double result = 5 * met_num * 3.5 *
                            Double.valueOf(Sub_bundle.getInstance().getKg()) * time / 1000;

                    input_healthData.setTime(time);
                    input_healthData.setKcal(result);

                    txt_calKcal.setText(Math.round(result * 100) / 100 + "Kcal");
                }
                else Toast.makeText(getContext(), "시간을 입력해주세요!!", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getContext(), "운동을 선택해주세요!!", Toast.LENGTH_SHORT).show();
        });

        // 저장
        btn_input.setOnClickListener(v -> {
            if(txt_calKcal.getText() != null) ADD_HEALTH_DATA(input_healthData);
            else Toast.makeText(getContext(), "칼로리를 계산해 주세요!!", Toast.LENGTH_SHORT).show();
        });


    }

    private void findViews() {
        fabRevealLayout = getView().findViewById(R.id.fab_reveal_layout);
        titleText = getView().findViewById(R.id.txt_directTitle);
        txt_directKcal = getView().findViewById(R.id.txt_directKcal);
        txt_directName = getView().findViewById(R.id.txt_directName);
        ibtn_return = getView().findViewById(R.id.ibtn_return);
        // okText = getView().findViewById(R.id.btn_DirectOK);
        cancelText = getView().findViewById(R.id.btn_DirectCancel);

        fab_direct = getView().findViewById(R.id.fab_direct);
        foodLinear = getView().findViewById(R.id.foodInformation);
        healthLinear = getView().findViewById(R.id.healthInformation);
    }

    private void configureFABReveal() {
        fabRevealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabRevealLayout, View mainView) {
                showMainViewItems();
            }

            @Override
            public void onSecondaryViewAppeared(final FABRevealLayout fabRevealLayout, View secondaryView) {
                showSecondaryViewItems();

                Log.i("ViewAppeared", "check!!");

                cancelText.setOnClickListener(view -> fabRevealLayout.revealMainView());
                //prepareBackTransition(fabRevealLayout);
                ibtn_return.setOnClickListener(v -> {

                    java.lang.String name = "";
                    double kcal = 0.0;

                    if(txt_directName.getText() != null) {
                        name = txt_directName.getText().toString();
                        if(txt_directKcal.getText() != null) {
                            kcal = Double.valueOf(txt_directKcal.getText().toString());
                        }
                        else {
                            Toast.makeText(getContext(), "kcal 를 입력해주세요!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "이름을 입력해주세요!!", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if(fh_check.equals("음식/food")){
                        // 음식 추가

                        FoodData foodData = new FoodData();
                        foodData.setName(name);
                        foodData.setKcal(kcal);
                        if (!txt_directPro.getText().toString().isEmpty()) {
                            double pro = Double.valueOf(txt_directPro.getText().toString());
                            foodData.setPro(pro);
                        }

                        if (!txt_directCar.getText().toString().isEmpty()) {
                            double car = Double.valueOf(txt_directCar.getText().toString());
                            foodData.setCar(car);
                        }

                        ADD_FOOD_DATA(foodData);

                    }
                    else{
                        Log.i("health_direct", "check!!");

                        // 운동 추가
                        HealthData healthData = new HealthData();
                        healthData.setName(name);
                        healthData.setKcal(Double.valueOf(kcal));
                        healthData.setTime(-999);
                        healthData.setImg_src(exercise.getImageSource("Direct"));

                        ADD_HEALTH_DATA(healthData);

                        /*
                        Map<String, Object> map = new HashMap<>();

                        // total
                        double total_kcal = Double.valueOf(txt_healthKcal.getText().toString());
                        total_kcal += kcal;

                        // singleTon
                        HealthSingleTon.getInstance().addArray_healthData(healthData);

                        // progress bar
                        healthKcalProgress.setProgress((int) Math.round(total_kcal));

                        // db
                        map.put("TotalHealthKcalOfDay", total_kcal);
                        dr_user.update(map);


                         */
                    }

                    directly_check = false;
                });

                directly_check = true;
            }
        });
    }
    public void ADD_FOOD_DATA(FoodData foodData){

        Map<java.lang.String, Object> map = new HashMap<>();

        boolean equal_check = false;

        for(int i = 0; i< CalorieSingleTon.getInstance().getFoodArrayList().size(); i++){
            FoodData new_data = CalorieSingleTon.getInstance().getFoodArrayList().get(i);
            java.lang.String food_name = new_data.getName();
            if(food_name.equals(foodData.getName())){

                int count = foodData.getNum();
                new_data.setNum(++count);
                map.put("Count", count);
                CalorieSingleTon.getInstance().setFoodArrayList(i, new_data);

                cr_food_today.document(food_name).update(map);
                equal_check = true;
                break;
            }
        }
        if(!equal_check){

            foodData.setNum(1);

            map.put("Count", foodData.getNum());
            map.put("name", foodData.getName());
            map.put("Eng", foodData.getKcal());

            if(foodData.getPro() != -1) map.put("Pro", foodData.getPro());
            if(foodData.getPro() != -1) map.put("Car", foodData.getCar());

            cr_food_today.document(foodData.getName()).set(map);
        }

        // total
        double total_kcal = Double.valueOf(txt_foodKcal.getText().toString().split("[ ]")[0]);
        total_kcal += foodData.getKcal();

        Sub_bundle.getInstance().setTotal_food_kcal(java.lang.String.valueOf(total_kcal));

        // progress bar
        foodKcalProgress.setProgress((int) Math.round(total_kcal));

        // setText
        txt_foodKcal.setText(total_kcal + " kcal \n (현재)");

        // db
        Map<java.lang.String, Object> total_map = new HashMap<>();
        total_map.put("TotalFoodKcalOfDay", total_kcal);
        dr_user.update(total_map);

        Toast.makeText(getContext(), "저장되었습니다!!", Toast.LENGTH_SHORT).show();

    }

    public void ADD_HEALTH_DATA(HealthData healthData){

        Map<java.lang.String, Object> map = new HashMap<>();

        boolean equal_check = false;

        for(int i=0;i<HealthSingleTon.getInstance().getArray_healthData().size();i++){
            HealthData new_data = HealthSingleTon.getInstance().getArray_healthData().get(i);
            java.lang.String health_name = new_data.getName();

            // 추가했다면 시간과 칼로리를 더해줌
            if(health_name.equals(healthData.getName())){
                if(!directly_check) {
                    new_data.setTime(new_data.getTime() + healthData.getTime());
                    map.put("time", new_data.getTime());
                }
                new_data.setKcal(new_data.getKcal() + healthData.getKcal());
                HealthSingleTon.getInstance().getArray_healthData().set(i, new_data);
                map.put("kcal", new_data.getKcal());

                cr_health_today.document(healthData.getName()).update(map);
                equal_check = true;
                Log.i("equal_check", "true");
                break;
            }
        }
        // 추가 안했었다면 그냥 추가
        if(!equal_check) {
            HealthSingleTon.getInstance().getArray_healthData().add(healthData);

            map.put("name", healthData.getName());
            map.put("kcal", healthData.getKcal());

            if(!directly_check) {
                map.put("time", healthData.getTime());
                map.put("imgSrc", healthData.getImg_src());
            }
            else{
                map.put("time", -999);
                map.put("imgSrc", exercise.getImageSource("Direct"));
            }

            cr_health_today.document(healthData.getName()).set(map);
        }

        // total
        double total_kcal = Double.valueOf(txt_healthKcal.getText().toString().split("[ ]")[0]);
        total_kcal += healthData.getKcal();

        Sub_bundle.getInstance().setTotal_health_kcal(java.lang.String.valueOf(total_kcal));

        // progress bar
        healthKcalProgress.setProgress((int) Math.round(total_kcal));

        // setText
        txt_healthKcal.setText(total_kcal + " kcal \n (현재)");

        // db
        Map<java.lang.String, Object> total_map = new HashMap<>();
        total_map.put("TotalHealthKcalOfDay", total_kcal);
        dr_user.update(total_map);

        Toast.makeText(getContext(), "저장되었습니다!!", Toast.LENGTH_SHORT).show();
    }

    private void showMainViewItems() {

        scale(foodKcalProgress, 0);
        scale(foodLinear, 100);
        scale(healthKcalProgress, 150);
        scale(healthLinear, 100);
    }

    private void showSecondaryViewItems() {
        scale(titleText, 50);
        scale(txt_directName, 150);
        scale(txt_directKcal, 150);
        scale(ibtn_return, 350);
        // scale(okText, 450);
        scale(cancelText, 450);
    }

    private void scale(View view, long delay){
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(500)
                .setStartDelay(delay)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    private void prepareBackTransition(final FABRevealLayout fabRevealLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabRevealLayout.revealMainView();
            }
        }, 5000);
    }

    public void setClickDirectFoodUI(){

        //Food
        // 위
        txt_directPro.setVisibility(View.VISIBLE);
        txt_directPro.setY(150);
        txt_directPro.setX(370);
        txt_directName.setY(150);
        txt_directName.setX(60);

        // 아래
        txt_directCar.setVisibility(View.VISIBLE);
        txt_directCar.setY(270);
        txt_directCar.setX(370);
        txt_directKcal.setY(270);
        txt_directKcal.setX(60);

        //계산버튼
        ibtn_return.setY(190);
        ibtn_return.setX(680);
    }
    public void setClickDirectHealthUI(){

        // 위
        txt_directPro.setVisibility(View.INVISIBLE);
        txt_directName.setY(150);
        txt_directName.setX(190);

        // 아래
        txt_directCar.setVisibility(View.INVISIBLE);
        txt_directKcal.setY(270);
        txt_directKcal.setX(190);

        //계산버튼
        ibtn_return.setY(190);
        ibtn_return.setX(490);
    }
    public void setNoneClickDirectFoodUI(){
        // Food
        // 위
        txt_directPro.setVisibility(View.VISIBLE);
        txt_directPro.setY(0);
        txt_directPro.setX(15);
        txt_directName.setY(0);
        txt_directName.setX(10);

        // 아래
        txt_directCar.setVisibility(View.VISIBLE);
        txt_directCar.setY(10);
        txt_directCar.setX(15);
        txt_directKcal.setY(10);
        txt_directKcal.setX(10);

        //계산버튼
        ibtn_return.setY(12);
        ibtn_return.setX(15);

    }
    public void setNoneClickDirectHealthUI(){
        // Food
        // 위
        txt_directPro.setVisibility(View.INVISIBLE);
        // txt_directName.setY(0);
        txt_directName.setX(130);

        // 아래
        txt_directCar.setVisibility(View.INVISIBLE);
        // txt_directKcal.setY(10);
        txt_directKcal.setX(130);

        //계산버튼
        ibtn_return.setY(12);
        ibtn_return.setX(-190);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_foodSearch:
                Intent intent = new Intent(getContext(), FoodSearchActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_food:
                foodLayout.setVisibility(View.VISIBLE);
                healthLayout.setVisibility(View.INVISIBLE);
                fh_check = "음식/food";
                if(directly_check) setClickDirectFoodUI();
                else setNoneClickDirectFoodUI();

                break;
            case R.id.btn_health:
                foodLayout.setVisibility(View.INVISIBLE);
                healthLayout.setVisibility(View.VISIBLE);
                fh_check = "health";
                if(directly_check) setClickDirectHealthUI();
                else setNoneClickDirectHealthUI();

                break;

            case R.id.btn_healthList:
                DialogFragment dialogue = new HealthListDialogue(HealthSingleTon.getInstance().getArray_healthData());
                dialogue.show(getChildFragmentManager(),"Dialog");
                break;
            case R.id.btn_healthZero:

                ZeroHealthKcalSetting();

                break;
            case R.id.btn_foodZero:
                ZeroFoodKcalSetting();
                break;

            case R.id.btn_foodCart:
                Intent cart_intent = new Intent(getContext(), FoodCartActivity.class);
                startActivity(cart_intent);
                break;

            case R.id.btn_foodGraph:
                Intent intent_foodGraph = new Intent(getContext(), GraphActivity.class);
                intent_foodGraph.putExtra("kcal_name", "food_kcal");
                startActivity(intent_foodGraph);
                break;

            case R.id.btn_healthGraph:
                Intent intent_graph = new Intent(getContext(), GraphActivity.class);
                intent_graph.putExtra("kcal_name", "health_kcal");
                startActivity(intent_graph);
                break;
        }
    }
    public void ZeroFoodKcalSetting(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("확인")
                .setMessage(" 오늘의 칼로리는 0 Kcal 입니다. \n 장바구니에 모든 데이터가 사라집니다.")
                .setNegativeButton("저  장", (dialog, which) -> {

                    dr_user.get()
                            .addOnCompleteListener(task -> {
                                dr_user.update("TotalFoodKcalOfDay", 0);
                                Sub_bundle.getInstance().setTotal_food_kcal("0");

                                onResume();
                            });

                    dr_user.collection("FoodCart")
                            .get()
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot docSnap : task.getResult()){
                                        dr_user.collection("FoodCart").document(docSnap.getId())
                                                .delete();
                                    }
                                }
                            });
                })
                .setPositiveButton("취 소", (dialog, which) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void ZeroHealthKcalSetting(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("확인")
                .setMessage(" 오늘의 칼로리는 0 Kcal 입니다. \n 운동리스트에 모든 데이터가 사라집니다.")
                .setNegativeButton("저  장", (dialog, which) -> {

                    dr_user.get().addOnCompleteListener(task -> {
                        dr_user.update("TotalHealthKcalOfDay", 0);
                        Sub_bundle.getInstance().setTotal_food_kcal("0");

                        onResume();
                    });

                    dr_user.collection("Health").document("today")
                            .collection("list")
                            .get()
                            .addOnCompleteListener(task -> {
                                int size = task.getResult().size();

                                if(size != 0){
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        dr_user.collection("Health").document("today")
                                                .collection("list").document(document.getId()).delete();
                                    }
                                }

                                // progress bar
                                healthKcalProgress.setProgress(0);

                                // setText
                                txt_healthKcal.setText(0 + " kcal \n (현재)");

                                // singleTon
                                HealthSingleTon.getInstance().clearArray_healthData();


                                Toast.makeText(getContext(), "0 KCAL 가 되었어요!!", Toast.LENGTH_SHORT).show();
                            });
                })
                .setPositiveButton("취 소", (dialog, which) -> {
                    dialog.dismiss();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        double food_mean = Double.valueOf(Sub_bundle.getInstance().getApp_amount());
        double food_kcal = Double.valueOf(Sub_bundle.getInstance().getTotal_food_kcal());

        goal_foodKcal.setText((int) Math.round(food_mean) + " kcal \n (적정)");
        txt_foodKcal.setText((int) Math.round(food_kcal) + " kcal \n (현재)");

        foodKcalProgress.setProgress((int) Math.round(food_kcal));
    }
}
