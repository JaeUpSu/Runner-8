package com.example.Runner8;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.Runner8.SingleTon.Sub_bundle;
import com.example.Runner8.SingleTon.Top_Data;
import com.example.Runner8.ui.F_H.calorieDictionary.CalorieDictionaryFragment;
import com.example.Runner8.ui.Graph.Today_Date;
import com.example.Runner8.ui.Parsing.parsing;
import com.example.Runner8.ui.community.onBackPressedListener;
import com.example.Runner8.ui.map.Foreground;
import com.example.Runner8.ui.summary.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener, SharedPreferences.OnSharedPreferenceChangeListener {

    // 앱 상단 왼쪽의 탐색버튼을 관리할 객체
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    parsing ps = new parsing();

    //
    SensorManager sensorManager;
    Sensor stepCountSensor;

    //
    private FirebaseStorage storage;

    //
    String providerId;

    //
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    //
    HomeFragment homeFragment;
    CalorieDictionaryFragment calorieDictionaryFragment;

    //
    String total_food_kcal, total_health_kcal, app_amount, first_date;
    Double maxKcal = 0.0;
    Double minKcal = 0.0;
    String maxKcalFood = "";
    String minKcalFood = "";
    int i = 1;
    int currentSteps = 0;     // 현재 걸음
    int update_month;
    int[] prev_date;
    int backStackCount = 0;
    private long backKeyPressedTime = 0;

    // Class
    Today_Date today_date = new Today_Date();

    //
    onBackPressedListener listener;

    // FireStore Reference
    DocumentReference dr_recently = db.collection("Users").document(user.getUid())
            .collection("recently_data").document("recently_value");

    CollectionReference cr_cart = db.collection("Users").document(user.getUid())
            .collection("FoodCart");

    DocumentReference dr_summary = db.collection("Users").document(user.getUid())
            .collection("user_data").document("summary");

    DocumentReference dr_user = db.collection("Users").document(user.getUid());

    DocumentReference dr_total_comm = db.collection("Community").document("board");

    CollectionReference cr_health_today = db.collection("Users").document(user.getUid())
            .collection("Health").document("today").collection("list");

    // TOP3
    Query top3_count_query = dr_summary.collection("month_cart").orderBy("count", Query.Direction.DESCENDING);
    Query top3_pro_query = dr_summary.collection("month_cart").orderBy("pro", Query.Direction.DESCENDING);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // FirebaseAnalytics 초기화
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // content_main.xml 의 fragment 객체 생성 <navigation controller>
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            Sub_bundle.getInstance().PlusBackStackCount();

            Log.i("item.getItemId()", item.getItemId() + "\n");
            Log.i("item.getItemId()", Sub_bundle.getInstance().getBackStackCount() + "");
            switch (item.getItemId()){
                case R.id.nav_home:
                    navController.navigate(R.id.nav_home);

                    break;
                case R.id.nav_community:
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
        // initial_DB
        // InitialDB();

        // SingleTon Setting
        // new singleTon().execute();

        //
        Foreground.init(getApplication());

        CollectionReference cr_myCourses = db.collection("Users").document(user.getUid())
                .collection("Map").document("MyCourses").collection("courses");

        CollectionReference cr_totally_mapCourses = db.collection("Map").document("User_Courses")
                .collection("Courses");

        CollectionReference dr_user_data = db.collection("Users").document(user.getUid())
                .collection("user_data");

        CollectionReference dr_food = db.collection("Calorie").document("Food")
                .collection("한식").document("해산물").collection("data");


        AssetManager am = getResources().getAssets() ;
        InputStream is = null ;
        StringBuilder sb = new StringBuilder("");
        byte buf[] = new byte[1024] ;
        int limit = 4;
        int count = 1;
        int new_count = 0;
        int food_count = 0;
        int c = 1;
        int hg = 0;
        String text = "" ;

        ArrayList<Map> data = new ArrayList<>();

        /*
        for(int i = 1;i<32;i++) {

            Random random = new Random();

            // food_kcal 범위 : 1000 ~ 3000

            // health_kcal : 0~400

            int food_kcal = random.nextInt(2000) + 1001;
            int health_kcal = random.nextInt(400);

            Map<String, Object> map = new HashMap<>();
            map.put("day", i);
            map.put("food_kcal", food_kcal);
            map.put("health_kcal", health_kcal);

            db.collection("Users").document(user.getUid())
                    .collection("user_data").document("2021")
                    .collection("10").document(String.valueOf(i)).set(map);

        }


         */


        /*
        try {
            is = am.open("음식/food_data.txt") ;

            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, "euc-kr");
                BufferedReader br = new BufferedReader(isr);

                String read;

                Map<String, Object> map = new HashMap<>();
                ArrayList<String> cal = new ArrayList<>();
                cal.add("Eng");
                cal.add("Fat");
                cal.add("Car");
                cal.add("Pro");

                while((read = br.readLine()) != null) {
                    if(read.equals("")) continue;

                    if(count % 2 == 0) {
                        if(count == 8){
                            map.put(cal.get(new_count), read.replace("g", ""));
                            count = 1;
                            Log.i("data.get(2).get(\"Pro\")" + c, map.get("Pro").toString());

                            data.add(map);
                            new_count = 0;
                            c++;
                            map = new HashMap<>();
                            continue;
                        }
                        map.put(cal.get(new_count), read.replace("g", ""));
                        Log.i("read.replace(\"g\", \"\")", read.replace("g", ""));
                        new_count++;
                        count++;
                    }
                    else {
                        count++;
                        continue;
                    }
                }

                Log.i("ASSET " + count++, text);
            }

            is.close() ;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (is != null) {
            try {
                is.close() ;
            } catch (Exception e) {
                e.printStackTrace() ;
            }
        }


         */

        /*
        for(Map map : data){
            Log.i("mapmapmapmapmap", map.get("Eng").toString());
        }
         */

/*
        dr_food.orderBy("index").get().addOnCompleteListener(task -> {

            int size = task.getResult().size();
            int i = 112;
            Log.i("size", size + "" + data.size());

            for(QueryDocumentSnapshot document : task.getResult()){
                dr_food.document(document.getId()).update(data.get(i++));
            }
        });


 */



        /*
        int n = 5;
        int[] lost = new int[] {2, 4, 5};
        int[] reserve = new int[] {1, 3};

        ArrayList<Integer> tmp_lost = new ArrayList<Integer>();


        for(int i=0;i<lost.length;i++){
            tmp_lost.add(lost[i]);
        }

        for(int i=0;i<reserve.length;i++){

            for(int j=0;j<tmp_lost.size();j++){
                if(tmp_lost.get(i) + 1 == lost[j] || tmp_lost.get(i) - 1 == lost[j]){
                    tmp_lost.remove(i);
                }
            }
        }

        int result = n - tmp_lost.size();

        Log.i("RESULT", result + "");

 */

/*
        CollectionReference cr_courses = db.collection("Map").document("User_Courses")
                .collection("Courses");

        cr_courses.get().addOnCompleteListener(task -> {
            for(QueryDocumentSnapshot document : task.getResult()){
                if(!document.getData().containsKey("total_avg_time")){
                    Map<String, Object> map = new HashMap<>();
                    String total_time = document.get("total_time").toString();

                    map.put("total_avg_time", Double.valueOf(total_time));
                    cr_courses.document(document.getId()).update(map);
                }
            }
        });

 */

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 걸음 센서 이벤트 발생시
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            if(event.values[0]==1.0f){
                // 센서 이벤트가 발생할때 마다 걸음수 증가
                currentSteps++;
                Log.i("onSensorChanged", String.valueOf(currentSteps));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public boolean checkBeforeFile(File file){
        if (file.exists()) {
            if (file.isFile() && file.canRead()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        // Log.i("onBackPressed", "BACK!!");

        if(Sub_bundle.getInstance().getBackStackCount() == 0){
            ActivityCompat.finishAffinity(this);
        }
        else Sub_bundle.getInstance().MinusBackStackCount();
        super.onBackPressed();

    }

    public void setOnBackPressedListener(onBackPressedListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 해당 함수는 액티비티(Activity)가 시작될 때 호출되는 함수로 액티비티 Life Cycle 내에서 단 한 번만 호출되기 때문에 이 안에서 MenuItem 생성과 초기화를 하면 됩니다.
        // 해당 함수에서는 Menu Inflater 를 통하여 XML Menu 리소스에 정의된 내용을 파싱 하여 Menu 객체를 생성하고 추가를 하게 됩니다.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 함수는 옵션 메뉴(Option Menu)에서 특정 Menu Item 을 선택하였을 때 호출되는 함수입니다.
        // 매개변수로 선택 된 MenuItem 의 객체가 넘어옵니다.
        // FirebaseAuth

        switch (item.getItemId()) {
            case R.id.action_logout:
                Map<String, Object> map = new HashMap<>();
                map.put("auto_check", false);
                db.collection("Users").document(user.getUid()).update(map);

                InitialSingleTon();

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                startActivity(intent);

                Toast.makeText(MainActivity.this, "로그 아웃", Toast.LENGTH_LONG).show();

                InitialSingleTon();

                finish();
                break;

            case R.id.home:
                Toast.makeText(getApplicationContext(), "뒤로가기", Toast.LENGTH_LONG).show();
                finish();

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    public void InitialSingleTon(){
        // TOP3
        Top_Data.getInstance().clearTop_count();
        Top_Data.getInstance().clearTop_high_kcal();
        Top_Data.getInstance().clearTop_high_pro();
        Top_Data.getInstance().clearTop_row_kcal();

        // 많이 먹은 요일
        Sub_bundle.getInstance().setDayOfTheWeek_lastMonth(null);
        Sub_bundle.getInstance().setAvg_DayOfTheWeek_lastMonth(null);
        Sub_bundle.getInstance().setRecently_month(null);

        //
    }

}