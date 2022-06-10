package com.example.Runner8.ui.summary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.Runner8.R;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.example.Runner8.ui.Graph.Today_Date;
import com.example.Runner8.ui.summary.home.HomeAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Fragment_Summary_home extends Fragment implements SensorEventListener {

    private TextView DayOfTheWeek_lastMonth, thisWeek_mostFoodKcal, thisWeek_leastFoodKcal,
            goal_kgtv, current_kgtv, last_avg_foodHealthKcal, prev_month_freq_food_ex, prev_max_kcalFood,
            prev_min_kcalFood, text_DayOfTheWeek_lastMonth, food_health, tv_steps;

    // Variable

    int steps = 0;              // 현재 걸음 수
    int counterSteps = 0;       // 리스너가 등록되고 난 후의 step count
    boolean able = false;
    Double maxKcal = 0.0;
    Double minKcal = 0.0;
    String maxKcalFood = "";
    String minKcalFood = "";

    // Class
    Today_Date today_date = new Today_Date();
    HomeAdapter homeAdapter;

    // Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    Map<String, Object> cart_map;
    Map<String, Object> freq_map;
    Map<String, Object> sum_map;

    //
    SensorManager sensorManager;
    Sensor stepCountSensor;
    ProgressBar progressBar;

    String[] cart_data;

    ViewPager2 viewPager;
    FragmentStateAdapter pagerAdapter;

    BottomNavigationView bottomNavigationView;

    public Fragment_Summary_home(){}

    public static Fragment_Summary_home newInstance(int num){
        Fragment_Summary_home fragment_summary_home = new Fragment_Summary_home();
        Bundle bundle = new Bundle();
        bundle.putInt("number", num);
        fragment_summary_home.setArguments(bundle);
        return fragment_summary_home;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            int num = getArguments().getInt("number");
        }
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("onCreateView", "onCreateView!!");
        return inflater.inflate(R.layout.fragment_summary,container,false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState){
        super.onViewCreated(view,savedInstancdState);

        DayOfTheWeek_lastMonth = view.findViewById(R.id.DayOfTheWeek_lastMonth);
        thisWeek_mostFoodKcal = view.findViewById(R.id.most_food_kcal);
        thisWeek_leastFoodKcal = view.findViewById(R.id.least_food_kcal);
        goal_kgtv = view.findViewById(R.id.goal_kgtv);
        current_kgtv = view.findViewById(R.id.current_kgtv);
        viewPager = view.findViewById(R.id.view_pager);
        last_avg_foodHealthKcal = view.findViewById(R.id.last_avg_foodHealthKcal);
        prev_month_freq_food_ex = view.findViewById(R.id.prev_month_freq_food_ex);
        prev_max_kcalFood = view.findViewById(R.id.prev_max_kcalFood);
        prev_min_kcalFood = view.findViewById(R.id.prev_min_kcalFood);
        text_DayOfTheWeek_lastMonth = view.findViewById(R.id.text_DayOfTheWeek_lastMonth);
        food_health = view.findViewById(R.id.food_health);
        tv_steps = view.findViewById(R.id.steps);

        // limit_summary();

        db.collection("Users").document(user.getUid())
                .collection("Profile").document("diet_profile")
                .get()
                .addOnCompleteListener(task3 -> {
                    DocumentSnapshot document2 = task3.getResult();

                    current_kgtv.setText(document2.get("kg").toString() + "Kg");
                    goal_kgtv.setText(document2.get("g_w").toString() + "Kg");

                });

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        // 디바이스에 걸음 센서의 존재 여부 체크
        if (stepCountSensor == null) {
            Toast.makeText(getContext(), "No Step Sensor", Toast.LENGTH_SHORT).show();
        }

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);


        /*
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            Sub_bundle.getInstance().PlusBackStackCount();
            Log.i("item.getItemId()", Sub_bundle.getInstance().getBackStackCount() + "");
            switch (item.getItemId()) {
                case R.id.nav_home:
                    navController.navigate(R.id.nav_home);
                    break;
                case R.id.nav_community:

                    MaterialElevationScale exit_scale = new MaterialElevationScale(false);
                    exit_scale.setDuration(1100011);
                    setExitTransition(exit_scale);

                    MaterialElevationScale reenter_scale = new MaterialElevationScale(true);
                    reenter_scale.setDuration(1100011);
                    setReenterTransition(reenter_scale);

                    String transtionName = "share_you";
                    FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                            .addSharedElement(bottomNavigationView, transtionName).build();

                    NavDirections directions = UserFragmentDirections.actionNavSettingToNavCommunity();
                    Navigation.findNavController(v).navigate(directions, extras);

                    navController.navigate(R.id.nav_community);
                    break;
                case R.id.nav_map:
                    navController.navigate(R.id.nav_map);
                    break;
                case R.id.nav_setting:
                    navController.navigate(R.id.nav_setting);
                    break;
            }
            return true;


        });

         */



    }

    public void limit_summary(){

        DocumentReference c_r = db.collection("Users").document(user.getUid());

        c_r.get().addOnCompleteListener(task -> {

            DocumentSnapshot document = task.getResult();
            String db_first_dd = task.getResult().get("first_data_date").toString();

            Log.i("SUB_getIns", db_first_dd);


            if(!db_first_dd.equals("")) {

                int first_date;
                first_date = 100*Integer.valueOf(db_first_dd.split("[.]")[0])
                        + Integer.valueOf(db_first_dd.split("[.]")[1]);

                int sum_today_date = 100 * today_date.getYear() + today_date.getMonth();

                if(sum_today_date - first_date < 1) none_data_summary_print();
                else{
                    db.collection("Users").document(user.getUid())
                            .collection("user_data").document("summary")
                            .get()
                            .addOnCompleteListener(task2 -> {
                                DocumentSnapshot document2 = task2.getResult();

                                Sub_bundle.getInstance().setMaxKcalFood(document2.get("maxKcalFood").toString());
                                Sub_bundle.getInstance().setMinKcalFood(document2.get("minKcalFood").toString());
                                Sub_bundle.getInstance().setMaxKcal(document2.get("maxKcal").toString());
                                Sub_bundle.getInstance().setMinKcal(document2.get("minKcal").toString());
                                Sub_bundle.getInstance().setDayOfTheWeek_lastMonth(
                                        document2.get("DayOfTheWeek_lastMonth").toString());
                                Sub_bundle.getInstance().setAvg_DayOfTheWeek_lastMonth(
                                        document2.get("avg_DayOfTheWeek_lastMonth").toString());

                                thisWeek_mostFoodKcal.setText("2500" +
                                        "       ( " + Sub_bundle.getInstance().getMaxKcal() + "Kcal )");

                                thisWeek_leastFoodKcal.setText("1800" +
                                        "       ( " + Sub_bundle.getInstance().getMinKcal() + "Kcal )");

                                DayOfTheWeek_lastMonth.setText(Sub_bundle.getInstance().getDayOfTheWeek_lastMonth() +
                                        "요일   "  + "( 평균 " +
                                        (Sub_bundle.getInstance().getAvg_DayOfTheWeek_lastMonth()) + " Kcal )");

                                prev_month_freq_food_ex.setText("ㆍ  지난 " + document2.get("recently_month").toString()
                                        + "월 자주 먹는 음식");

                                text_DayOfTheWeek_lastMonth.setText("ㆍ  지난 " + document2.get("recently_month").toString()
                                        + "월 가장 많이 먹은 요일");
                            });
                }
            }
            else{
                none_data_summary_print();
            }
        });
    }

    public void none_data_summary_print(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("데이터 부족");
        builder.setMessage("Summary 를 구성할 데이터가 부족합니다. \n 데이터를 입력한 후 다음달 1일부터 " +
                "확인 가능합니다.");
        builder.setPositiveButton("확 인", (dialog, which) -> {
            // 다른 페이지로 넘어가는 방법을 알아내시오.
            // 데이터가 없는 것은 메인에서 확인 가능

            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();

        dialog.show();

        last_avg_foodHealthKcal.setText("");
        prev_month_freq_food_ex.setText("To Be Continue....");
        prev_max_kcalFood.setText("");
        prev_min_kcalFood.setText("");
        text_DayOfTheWeek_lastMonth.setText("");

        thisWeek_mostFoodKcal.setText("");
        thisWeek_leastFoodKcal.setText("");
        DayOfTheWeek_lastMonth.setText("");
        food_health.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
        db.collection("Users").document(user.getUid())
                .collection("user_data").document(String.valueOf(today_date.getYear()))
                .collection(String.valueOf(today_date.getMonth() - 1))
                .orderBy("day")
                .get()
                .addOnCompleteListener(task -> {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(today_date.getYear(), today_date.getMonth() - 2,
                            1);

                    int startDay_dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    int max_DayOfTheWeek = startDay_dayOfTheWeek;

                    Log.i("CEHCK_s_dayOfTheWeek", String.valueOf(startDay_dayOfTheWeek));

                    int[] dayOfTheWeek = new int[]{0, 0, 0, 0, 0, 0, 0};
                    int[] count = new int[]{0, 0, 0, 0, 0, 0, 0};

                    int avg = 0, max = 0, day_of_the_week = 0;
                    String[] name_dayOfTheWeek = new String[]{"일", "월", "화", "수",
                            "목", "금", "토"};

                    for(QueryDocumentSnapshot document : task.getResult()){
                        int foodKcal = Integer.valueOf(document.get("food_kcal").toString());

                        Log.i("dayOfTheWeek_max_check", dayOfTheWeek[startDay_dayOfTheWeek] + " " +
                                dayOfTheWeek[max_DayOfTheWeek]);

                        dayOfTheWeek[startDay_dayOfTheWeek] += foodKcal;
                        count[startDay_dayOfTheWeek] += 1;

                        startDay_dayOfTheWeek++;
                        if(startDay_dayOfTheWeek > 6) {
                            startDay_dayOfTheWeek %= 7;
                        }

                        Log.i("food_kcal", document.get("food_kcal").toString());
                        Log.i("startDay_dayOfTheWeek", String.valueOf(startDay_dayOfTheWeek));
                        Log.i("max_DayOfTheWeek", (name_dayOfTheWeek[max_DayOfTheWeek]));
                    }

                    max = dayOfTheWeek[0] / count[0];

                    for(int i=1;i<7;i++){
                        avg = dayOfTheWeek[i] / count[i];
                        if(max < avg) {
                            max = avg;
                            day_of_the_week = i;
                        }
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("avg_DayOfTheWeek_lastMonth", max);
                    map.put("DayOfTheWeek_lastMonth", name_dayOfTheWeek[day_of_the_week]);

                    db.collection("Users").document(user.getUid())
                            .collection("user_data").document("summary")
                            .update(map);

                    Log.i("startDay_dayOfTheWeek", String.valueOf(startDay_dayOfTheWeek));

                    DayOfTheWeek_lastMonth.setText(name_dayOfTheWeek[day_of_the_week] + "요일   " +
                            "( 평균 " + String.valueOf(max) + " Kcal )");
                });
*/
        /*


        // 최대 , 최소 칼로리 음식 구하는 코드

        db.collection("Users").document(user.getUid())
                .collection("FoodCart")
                .get()
                .addOnCompleteListener(task -> {

                    int count = 0;

                    for(QueryDocumentSnapshot document : task.getResult()){

                        Double Eng = Double.valueOf(document.get("Eng").toString());

                        if(count == 0){
                            maxKcalFood = document.getId();
                            maxKcal = Eng;
                            minKcalFood = document.getId();
                            minKcal = Eng;

                            Log.i("CHECK1", document.getId() + Eng);
                            count++;
                            continue;
                        }

                        if(maxKcal < Eng){
                            maxKcal = Eng;
                            maxKcalFood = document.getId();
                        }

                        if(minKcal > Eng){
                            minKcal = Eng;
                            minKcalFood = document.getId();
                        }

                        Log.i("CHECK2", document.getId() + Eng);
                    }

                    Map<String, Object> map = new HashMap<>();

                    DocumentReference summary = db.collection("Users").document(user.getUid())
                            .collection("user_data").document("summary");

                    summary.get()
                            .addOnCompleteListener(task1 -> {

                                double max_kcal = Double.valueOf(task1.getResult().get("maxKcal").toString());
                                double min_kcal = Double.valueOf(task1.getResult().get("minKcal").toString());

                                if(max_kcal < maxKcal){
                                    map.put("maxKcalFood", maxKcalFood);
                                    map.put("maxKcal", maxKcal);
                                }
                                if(min_kcal > minKcal){
                                    map.put("minKcalFood", minKcalFood);
                                    map.put("minKcal", minKcal);
                                }
                                summary.update(map);
                            });
                });

         */


        /*

        db.collection("Users").document(user.getUid())
                .collection("FoodCart")
                .get()
                .addOnCompleteListener(task -> {

                    cart_map = new HashMap<>();

                    for(QueryDocumentSnapshot document : task.getResult()){

                        CollectionReference collectionReference = db.collection("Users").document(user.getUid())
                                .collection("user_data").document("summary")
                                .collection("frequently_food");

                        collectionReference
                                .whereEqualTo("FoodName", document.getId())
                                .get()
                                .addOnCompleteListener(task1 -> {

                                    Map<String, Object> map = new HashMap<>();

                                    if(task1.isSuccessful()){

                                        int count = 0;
                                        for(QueryDocumentSnapshot document2 : task1.getResult()){
                                            count = Integer.valueOf(document2.get("Count").toString());
                                        }

                                        map.put("Count", count +
                                                Integer.valueOf(document.get("Count").toString()));

                                        collectionReference.document(document.getId())
                                                .update(map);
                                    }
                                    else{

                                        map.put("FoodName", document.getId());
                                        map.put("Count", document.get("Count"));

                                        collectionReference.document(document.getId())
                                                .set(map);
                                    }
                                });
                        // cart_map.put(document.getId(), document.get("Count"));
                    }
                });

         */
    }

    @Override
    public void onStop() {
        super.onStop();
        if(sensorManager!=null){
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 걸음 센서 이벤트 발생시
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            if(counterSteps < 1){
                counterSteps = (int) event.values[0];
            }
            steps = (int) event.values[0] - counterSteps;

            tv_steps.setText("현재 걸음 수 " + steps);
            Log.i("log: ", "New step detected by STEP_COUNTER sensor. Total step count: " + steps );
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStart() {
        super.onStart();

        if(stepCountSensor !=null){
            //센서의 속도 설정
            sensorManager.registerListener(this,
                    stepCountSensor,SensorManager.SENSOR_DELAY_GAME);
        }

    }
}
