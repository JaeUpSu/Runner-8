package com.example.Runner8;

import android.os.AsyncTask;
import android.util.Log;

import com.example.Runner8.SingleTon.ProfileSingleTon;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.example.Runner8.SingleTon.Top_Data;
import com.example.Runner8.ui.F_H.calorie.Adapter.Model.KoreaClass;
import com.example.Runner8.ui.F_H.calorie.SingleTon.CalorieSingleTon;
import com.example.Runner8.ui.F_H.health.Adapter.Model.HealthData;
import com.example.Runner8.ui.F_H.health.SingleTon.HealthSingleTon;
import com.example.Runner8.ui.Graph.Today_Date;
import com.example.Runner8.ui.community.singleTon.TotalCounts;
import com.example.Runner8.ui.map.QuarterOfCourses;
import com.example.Runner8.ui.map.SingleTon.MapSingleTon;
import com.example.Runner8.ui.summary.SummarySingleTon;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserUpdate {

    boolean update_check = false;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;

    // FireStore Reference
    DocumentReference dr_recently;
    CollectionReference cr_cart;
    DocumentReference dr_summary;
    DocumentReference dr_user;
    DocumentReference dr_total_comm;
    CollectionReference cr_health_today;

    Query top3_count_query;
    Query top3_pro_query;

    Today_Date today_date = new Today_Date();

    int[] prev_date;
    int update_month;
    //
    String total_food_kcal, total_health_kcal, app_amount, first_date;
    Double maxKcal = 0.0;
    Double minKcal = 0.0;
    String maxKcalFood = "";
    String minKcalFood = "";

    public void startUpdate(FirebaseUser user){
        this.user = user;
        Log.i("user", user.getUid());

        // FireStore Reference
        dr_recently = db.collection("Users").document(user.getUid())
                .collection("recently_data").document("recently_value");

        cr_cart = db.collection("Users").document(user.getUid())
                .collection("FoodCart");

        dr_summary = db.collection("Users").document(user.getUid())
                .collection("user_data").document("summary");

        dr_user = db.collection("Users").document(user.getUid());

        dr_total_comm = db.collection("Community").document("board");

        cr_health_today = db.collection("Users").document(user.getUid())
                .collection("Health").document("today").collection("list");

        top3_count_query = dr_summary.collection("month_cart")
                .orderBy("Count", Query.Direction.DESCENDING).limit(3);

        top3_pro_query = dr_summary.collection("month_cart")
                .orderBy("Pro", Query.Direction.DESCENDING).limit(3);

        update_check = true;
        new singleTon().execute();
    }

    private class singleTon extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            Log.i("SingleTon", "check!!");
            InitialDB();

            // 하루 총 칼로리, 처음 입력 날, 회원가입 날
            dr_user.get().addOnCompleteListener(task -> {

                total_food_kcal = task.getResult().get("TotalFoodKcalOfDay").toString();
                total_health_kcal = task.getResult().get("TotalHealthKcalOfDay").toString();
                first_date = task.getResult().get("first_data_date").toString();

                if(total_food_kcal.equals("")) Sub_bundle.getInstance().setTotal_food_kcal("0.0");
                else Sub_bundle.getInstance().setTotal_food_kcal(total_food_kcal);
                Sub_bundle.getInstance().setFirst_date(first_date);
                if(total_health_kcal.equals("")) Sub_bundle.getInstance().setTotal_health_kcal("0.0");
                else Sub_bundle.getInstance().setTotal_health_kcal(total_health_kcal);
                Sub_bundle.getInstance().setUser_comm_index(task.getResult().get("comm_count").toString());
                Log.i("TOTALKCAL", Sub_bundle.getInstance().getTotal_food_kcal());
            });

            // 프로필 데이터
            dr_user.collection("Profile").document("diet_profile")
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();

                        Sub_bundle.getInstance().setGoal_weight(document.get("g_w").toString());
                        Sub_bundle.getInstance().setSex(document.get("sex").toString());
                        Sub_bundle.getInstance().setKg(document.get("kg").toString());
                        Sub_bundle.getInstance().setHeight(document.get("height").toString());
                        Sub_bundle.getInstance().setPro_img_url(document.get("user_img").toString());
                        Sub_bundle.getInstance().setNickName(document.get("nickName").toString());
                        Sub_bundle.getInstance().setAge(document.get("age").toString());
                        Sub_bundle.getInstance().setApp_amount(document.get("mean").toString());
                        ProfileSingleTon.getInstance().setMemo_final_count(
                                Integer.valueOf(document.get("memo_final_count").toString()));
                    });

            // 메모 데이터
            dr_user.collection("Profile").document("diet_profile")
                    .collection("Memo")
                    .get()
                    .addOnCompleteListener(task -> {

                        if(task.getResult().size() != 0) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String content = document.get("content").toString();
                                ProfileSingleTon.getInstance().addMemo_array(content);
                            }
                        }
                    });

            // 커뮤니티
            dr_total_comm.get().addOnCompleteListener(task -> {
                DocumentSnapshot document = task.getResult();

                Sub_bundle.getInstance().setTotal_comm_index(document.get("comm_count").toString());
            });

            db.collection("Community").document("board")
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        TotalCounts.getInstance().setBoard_count(document.get("board_count").toString());
                    });

            // TOP3
            Top_Data.getInstance().getTop3_data();

            CollectionReference cr_totally_mapCourses = db.collection("Map").document("User_Courses")
                    .collection("Courses");

            // QUARTER_POINT OF COURSES
            cr_totally_mapCourses
                    .get()
                    .addOnCompleteListener(task -> {

                        for (QueryDocumentSnapshot document : task.getResult()){

                            cr_totally_mapCourses.document(document.getId())
                                    .collection("quarters")
                                    .orderBy("index")
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        QuarterOfCourses quarterOfCourses = new QuarterOfCourses();
                                        quarterOfCourses.setId(document.getId());
                                        // Log.i("QUARTER document.getId()", document.getId());

                                        if(task1.getResult().size() != 0) {
                                            // Log.i("QUARTER getResult().size()", task1.getResult().size() + "");

                                            for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                                double Lat = Double.valueOf(document1.get("lat").toString());
                                                double Long = Double.valueOf(document1.get("long").toString());

                                                quarterOfCourses.addQuarter_Lat(Lat);
                                                quarterOfCourses.addQuarter_Long(Long);

                                                // Log.i("QUARTER Lat", Lat + "");
                                            }
                                            if(document.getId().equals("36")){
                                                Log.i("quarterOfCourses", quarterOfCourses.getQuarter_Lat().size() + "");

                                                Log.i("quarterOfCourses", quarterOfCourses.getQuarter_Lat().get(0) + "");
                                                Log.i("quarterOfCourses", quarterOfCourses.getQuarter_Lat().get(1) + "");
                                            }
                                        }
                                        MapSingleTon.getInstance().addQuarterOfCourses(quarterOfCourses);
                                    });
                        }
                    });

            // SUMMARY RUN
            db.collection("Users").document(user.getUid())
                    .collection("Map").document("TOTAL")
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        MapSingleTon.getInstance().setTOTAL_COUNT(Integer.valueOf(document.get("total_count").toString()));
                        MapSingleTon.getInstance().setTOTAL_DISTANCE(Double.valueOf(document.get("total_distance").toString()));
                        MapSingleTon.getInstance().setTOTAL_KCAL(Double.valueOf(document.get("total_kcal").toString()));
                        MapSingleTon.getInstance().setTOTAL_AVG_SPEED(Double.valueOf(document.get("total_avg_speed").toString()));
                        Log.i("SUMMARY RUN", document.get("total_count").toString());
                    });

            dr_user.collection("user_data").document("summary")
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();

                        if(document.getData().isEmpty()){
                            return;
                        }
                        else{
                            SummarySingleTon.getInstance().setCurrent_week_food_kcal(document.get("current_week_food_kcal").toString());
                            SummarySingleTon.getInstance().setCurrent_week_health_kcal(document.get("current_week_health_kcal").toString());
                            SummarySingleTon.getInstance().setPrev_week_food_kcal(document.get("prev_week_food_kcal").toString());
                            SummarySingleTon.getInstance().setPrev_week_health_kcal(document.get("prev_week_health_kcal").toString());
                        }
                    });


            // SUMMARY FOOD
            dr_summary.get().addOnCompleteListener(task -> {
                DocumentSnapshot document = task.getResult();

                if(document.exists()) {
                    if (document.getData().size() != 0) {

                        String avg_DayOfTheWeek_lastMonth = "";
                        String DayOfTheWeek_lastMonth = "";
                        String recently_month = "";

                        if(document.get("avg_DayOfTheWeek_lastMonth") != null){
                            avg_DayOfTheWeek_lastMonth = document.get("avg_DayOfTheWeek_lastMonth").toString();
                            Sub_bundle.getInstance().setAvg_DayOfTheWeek_lastMonth(avg_DayOfTheWeek_lastMonth);
                        }

                        if(document.get("DayOfTheWeek_lastMonth") != null){
                            DayOfTheWeek_lastMonth = document.get("DayOfTheWeek_lastMonth").toString();
                            Sub_bundle.getInstance().setDayOfTheWeek_lastMonth(DayOfTheWeek_lastMonth);
                        }

                        if(document.get("recently_month") != null){
                            recently_month = document.get("recently_month").toString();
                            Sub_bundle.getInstance().setRecently_month(recently_month);
                        }

                    }
                }
            });

            // HEALTH
            cr_health_today.get().addOnCompleteListener(task -> {

                if(task.isSuccessful()) {
                    int size = task.getResult().size();
                    if( size != 0){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            HealthData healthData = new HealthData();
                            healthData.setName(document.getId());
                            healthData.setKcal(Double.valueOf(document.get("kcal").toString()));
                            healthData.setTime(Integer.valueOf(document.get("time").toString()));
                            healthData.setImg_src(Integer.valueOf(document.get("imgSrc").toString()));
                            HealthSingleTon.getInstance().addArray_healthData(healthData);
                        }
                    }
                }
            });

            koreaFoodClass();

            return null;
        }
    }

    public void koreaFoodClass(){

        ArrayList<KoreaClass> koreaClasses = new ArrayList<>();

        KoreaClass koreaClass = new KoreaClass();
        koreaClass.setName("고기");
        koreaClass.setImg(R.drawable.meat);
        koreaClasses.add(koreaClass);

        KoreaClass koreaClass1 = new KoreaClass();
        koreaClass1.setName("국,찌개");
        koreaClass1.setImg(R.drawable.soup);
        koreaClasses.add(koreaClass1);

        KoreaClass koreaClass2 = new KoreaClass();
        koreaClass2.setName("밀가루");
        koreaClass2.setImg(R.drawable.flour);
        koreaClasses.add(koreaClass2);

        KoreaClass koreaClass3 = new KoreaClass();
        koreaClass3.setName("밥");
        koreaClass3.setImg(R.drawable.rice);
        koreaClasses.add(koreaClass3);

        KoreaClass koreaClass4 = new KoreaClass();
        koreaClass4.setName("해산물");
        koreaClass4.setImg(R.drawable.seafood);
        koreaClasses.add(koreaClass4);

        CalorieSingleTon.getInstance().setKoreaClasses(koreaClasses);
    }

    public void InitialDB() {

        dr_user.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();

            String first_data_date = document.get("first_data_date").toString();
            today_date.setNow();

            if (first_data_date.equals("")) {
                String totalFood = document.get("TotalFoodKcalOfDay").toString();
                String totalHealth = document.get("TotalHealthKcalOfDay").toString();

                if (totalFood.equals("") || totalHealth.equals("")) return;
                else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("first_data_date", today_date.getFormat_date());
                    Sub_bundle.getInstance().setFirst_date(today_date.getFormat_date());
                    dr_user.update(map);
                }
            } else {
                initial_recently_data();
                updateWeek();
            }
        });
    }

    public void OneDay_top3_month_food() {
        Log.i("OneDay_top3_month_food", "OneDay_top3_month_food");

        CollectionReference cr_monthCart = dr_summary.collection("month_cart");

        cr_cart.get()
                .addOnCompleteListener(task2 -> {

                    if (task2.getResult().size() == 0) return;
                    else {
                        Map<String, Object> month_cart = new HashMap<>();

                        cr_monthCart.get()
                                .addOnCompleteListener(task -> {
                                    for (QueryDocumentSnapshot document : task.getResult())
                                        month_cart.put(document.getId(), document.get("Count"));

                                    for (QueryDocumentSnapshot document : task2.getResult()) {
                                        Map<String, Object> map = new HashMap<>();

                                        double Eng = Double.valueOf(document.get("Eng").toString());
                                        double Pro = 0.0;
                                        double Car = 0.0;

                                        // double Fat = Double.valueOf(document.get("Fat").toString());

                                        if (month_cart.containsKey(document.getId())) {
                                            Log.i("containsKey", document.getId());
                                            int count = Integer.valueOf(String.valueOf(month_cart.get(document.getId())));
                                            count += Integer.valueOf(String.valueOf(document.get("Count")));
                                            map.put("Count", count);
                                            cr_monthCart.document(document.getId()).update(map);
                                        } else {
                                            if(document.get("Pro") != null) {
                                                Pro = Double.valueOf(document.get("Pro").toString());
                                                map.put("Pro", Pro);
                                            }
                                            if(document.get("Car") != null) {
                                                Car = Double.valueOf(document.get("Car").toString());
                                                map.put("Car", Car);
                                            }

                                            Log.i("not containsKey", document.getId());
                                            map.put("Eng", Eng);
                                            // map.put("Fat", Fat);
                                            map.put("Count", Integer.valueOf(document.get("Count").toString()));
                                            cr_monthCart.document(document.getId()).set(map);
                                        }

                                        cr_cart.document(document.getId()).delete();
                                    }


                                });
                    }
                });

    }

    public void OneDay_health_initial(){
        cr_health_today.get().addOnCompleteListener(task -> {
            for(QueryDocumentSnapshot document : task.getResult())
                cr_health_today.document(document.getId()).delete();
        });
    }

    public void initial_recently_data() {

        // 최근에 입력된 날짜를 불러올 참조객체
        dr_recently.get().addOnCompleteListener(task -> {

            DocumentSnapshot document = task.getResult();
            today_date.setNow();

            // 현재 flag
            boolean flag = Boolean.valueOf(document.get("flag").toString());
            String value = String.valueOf(flag);
            // 표현되고 있는 데이터
            int[] month_update_date = new int[3];
            // 최근 날짜를 넣을 배열
            prev_date = new int[3];
            // 오늘 날짜
            int today_year = today_date.getYear();
            int today_month = today_date.getMonth();
            int today_day = today_date.getDay();

            Log.i("today_day", today_day + "");

            // Map
            Map<String, Object> map = new HashMap<>();

            Log.i("recently_value", document.get(value).toString().split("[.]")[2]);

            // 날짜 데이터 받기
            for (int i = 0; i < 3; i++) {
                // 최근 날짜 할당
                prev_date[i] = Integer.valueOf(document.get(value).toString().split("[.]")[i]);
            }

            // 현재 년, 월이 최근 날짜와 다를 경우 최근 날짜를 초기화
            // 년, 월, 일 중 하나라도 다른 값이 있다면
            if (prev_date[0] != today_year || prev_date[1] != today_month ||
                    prev_date[2] != today_day) {
                if (flag) {
                    map.put("false", today_year + "." + today_month + "." + today_day);
                    map.put("flag", false);
                } else {
                    map.put("true", today_year + "." + today_month + "." + today_day);
                    map.put("flag", true);
                }

                // 오늘 날짜 업데이트
                dr_recently.update(map);

                // 최근 날짜의 하루용 데이터들 업데이트
                // Total data
                // Week data check
                Update_data_for_the_day();

                // health data initial
                OneDay_health_initial();

                // 년, 월 중 하나라도 다른 값이 있다면
                //      -> 년, 월이 둘다 같다면 조건에 만족하지 못함. (같은 달이라는 뜻)
                if (prev_date[0] != today_year || prev_date[1] != today_month) {

                    // 업데이트 순서
                    // 1. TOP3 데이터 초기화
                    // 2. TOP3 업데이트
                    // 3. month_cart 초기화
                    // 4  month_cart 업데이트

                    // TOP3 초기화
                    TOP3_initial();

                    // TOP3 데이터 업데이트
                    update_Top3();

                    //
                    Month_cart_initial();

                    // 특정 달이 몇일 까지인지 확인하기 위한 Calendar 객체 초기화
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(prev_date[0], prev_date[1], 1);
                    // 특정 달의 마지막 일
                    String max_day = String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

                    // 특정 달에 데이터가 입력되었는지 확인하기 위한 참조객체
                    DocumentReference check_data = db.collection("Users").document(user.getUid())
                            .collection("user_data").document("yearData");


                    update_month = prev_date[1];
                    updateMonth();

                    /*
                    // 최근 날짜의 데이터가 입력되었는지 확인
                    check_data.collection(String.valueOf(prev_date[0]))
                            .document(String.valueOf(prev_date[1]))
                            .get()
                            .addOnCompleteListener(task1 -> {

                                //  데이터가 있다면
                                if (!task1.getResult().get("count").toString().equals(max_day)) {

                            // show_date 출력
                            Sub_bundle.getInstance()
                                    .setRecently_month(String.valueOf(show_date[1]));

                                    Map<String, Object> new_show = new HashMap<>();
                                    new_show.put("month_update_date", prev_date[0] + "." + prev_date[1]
                                            + "." + prev_date[2]);
                                    dr_recently.update(new_show);

                                    update_month = prev_date[1];

                                    updateMonth();

                                    //  데이터가 없으면 updateMonth() 를 해줄 필요가 없음

                                } else {

                            // prev_date 출력
                            Sub_bundle.getInstance()
                                    .setRecently_month(String.valueOf(prev_date[1]));

                                }
                            });


                     */
                }
                // top3_food_month
                OneDay_top3_month_food();
            }
        });
    }
    public void Week_data_initial(){
        Map<String, Object> map = new HashMap<>();
        map.put("prev_week_food_kcal", "");
        map.put("prev_week_health_kcal", "");
        map.put("current_week_food_kcal", "");
        map.put("current_week_health_kcal", "");
        dr_user.collection("user_data").document("summary")
                .update(map);

        SummarySingleTon.getInstance().setCurrent_week_food_kcal("");
        SummarySingleTon.getInstance().setCurrent_week_health_kcal("");
        SummarySingleTon.getInstance().setPrev_week_food_kcal("");
        SummarySingleTon.getInstance().setPrev_week_health_kcal("");

        week_check_data_initial();

    }
    public void week_check_data_initial(){
        Log.i("week_check_data_initial", "week_check_data_initial");
        dr_user.collection("user_data").document("week_check")
                .collection("week")
                .get()
                .addOnCompleteListener(task -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("data_check", false);
                    for(QueryDocumentSnapshot document : task.getResult()){
                        dr_user.collection("user_data").document("week_check")
                                .collection("week").document(document.getId()).update(map);
                    }
                });
    }

    public boolean check_next_week(){

        // Solution : 오늘 날짜를 기준으로 저번주에 해당하는 범위안에 prev_date 가 존재하는지 확인한다.
        // Problem 1
        //      - 저번주를 확인하기 위해 today_day - 7 을 할 경우 해당 값이 음수가 되었을 때 (check_day)
        //          > 아예 전 달로 넘어간 경우
        //          > 이번달 첫번째 주인 경우
        //
        // Problem 2
        //      - prev_date 가 이번주라면 아무것도 하지 않음

        // Problem 3
        //      - Week_data_initial() : prev_date 가 이번주와 저번주가 아니면 초기화 하는 작업
        //

        // 순서
        // 1. prev_date 가 이번주인지 확인, 이번주면 return false (아무것도 안함)
        // 2. 오늘이 이번달의 첫번째 주인 경우 -> 전 달만 확인함
        // 3. check day(day - 7) 가 0보다 작거나 같은지 확인
        // 4. 0보다 클 때 st_num 이 음수인지 확인
        // 구한 prev_st_des, current_st_des 범위에 prev_date 가 있는지 확인

        Calendar calendar = Calendar.getInstance();

        boolean result = false;

        int today_year = today_date.getYear();
        int today_month = today_date.getMonth();
        int today_day = today_date.getDay();

        int prev_year = prev_date[0];
        int prev_month = prev_date[1];
        int prev_day = prev_date[2];

        int check_day = today_day - 7;          // 저번주를 확인하기 위한 임시 day 값
        int day_of_week = 0;
        int max_day = 0;
        int prev_check_st = 0;
        int prev_check_des = 0;
        int check_st = 0;
        int check_des = 0;

        // 달의 첫 번째 주인 경우에 사용될 데이터들
        calendar.set(today_year, today_month - 1, 1);
        int tmp_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        int tmp = 7 - tmp_of_week;

        // 오늘 기준으로 사용될 데이터
        calendar.set(today_year, today_month - 1, today_day);
        day_of_week = calendar.get(Calendar.DAY_OF_WEEK);

        // 저번주가 전 달로 넘어갈 때 사용될 데이터
        calendar.set(today_year, today_month - 2, 1);
        max_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Log.i("week_check_today", today_year + "  " + today_month + "  " + today_day);
        Log.i("week_check_prevDay", prev_year + "  " + prev_month + "  " + prev_day);

        // prev_date 가 이번주인 경우 확인
        if (today_year == prev_year && prev_month == today_month
                && today_day - day_of_week + 1 <= prev_day && prev_day <= today_day) {
            Log.i("this_week_check", "check!!");
            return false;
        }
        //
        else {
            // 이번달의 첫번째 주인 경우
            if (today_day - day_of_week < 0) {
                // 전 달만 보면 되기 때문에 확인 후 return

                int real_day = max_day + check_day;
                calendar.set(today_year, today_month - 2, real_day);

                day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
                check_st = real_day - day_of_week + 1;
                check_des = real_day + (7 - day_of_week);

                if (prev_year == today_year && prev_year == today_month - 1
                        && check_st <= prev_day && prev_day <= check_des)
                    return true;

                else Week_data_initial();
            /*
            if (today_year == prev_year && prev_month == today_month - 1
                    && (max_day - tmp_of_week) + 2 <= prev_day && prev_day <= max_day)
                return false;

             */
            }

            // day - 7 이 0보다 작거나 같은 경우 (전 주가 이번달의 첫번째 주임)
            if (check_day <= 0) {

                // prev
                prev_check_st = (max_day - tmp_of_week) + 2;
                prev_check_des = max_day;

                // current
                calendar.set(today_year, today_month - 1, 1);
                check_st = 1;
                check_des = 1 + (7 - tmp_of_week);

            } else {
                calendar.set(today_year, today_month - 1, check_day);

                /////////////////////////////////////////////////////////////////////////////////////////////
                //      1. 입력받은 날짜를 기준으로 왼쪽 오른쪽 끝 값을 구함
                //  저번주 인지 확인하는거잖아
                day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
                check_st = check_day - day_of_week + 1;
                check_des = check_day + (7 - day_of_week);

                if (check_st < 1) {
                    calendar.set(today_year, today_month - 2, 1);
                    check_st = 1;

                    day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
                    max_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    prev_check_st = (max_day - day_of_week) + 2;
                    prev_check_des = max_day;
                }
            }

            if (today_year == prev_year && today_month == prev_month) {
                if (prev_check_st <= prev_day && prev_check_des >= prev_day)
                    return true;
                else if (check_st <= prev_day && check_des >= prev_day) return true;
                else return false;
            } else return false;
        }

        // true -> 주가 지났다는 소리
        //      - current_week -> prev_week
        //      - current_week = ""
        // false -> 주가 안지났다느 소리
        //      - current_week -> update 해주면끝
    }

    public void TOP3_initial(){
        Log.i("TOP3_initial", "TOP3_initial");
        dr_summary.collection("TOP_DATA")
                .get()
                .addOnCompleteListener(task -> {
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.i("TOP3_initial document", document.getId());
                        dr_summary.collection("TOP_DATA").document(document.getId())
                                .collection("data")
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    for(QueryDocumentSnapshot document1 : task1.getResult()){
                                        Log.i("TOP3_initial document1", "document1.getId()");
                                        dr_summary.collection("TOP_DATA").document(document1.getId()).delete();
                                    }
                                });
                    }
                });
    }

    public void Month_cart_initial(){
        Log.i("Month_cart_initial", "Month_cart_initial");
        dr_summary.collection("month_cart")
                .get()
                .addOnCompleteListener(task -> {
                    for(QueryDocumentSnapshot document : task.getResult()){
                        dr_summary.collection("month_cart").document(document.getId()).delete();
                    }
                });
    }

    public void update_Top3(){
        Log.i("update_Top3", "update_Top3");
        top3_count_query.get().addOnCompleteListener(task -> {
            // 자주 먹은 음식 top3
            Top_Data.getInstance().Top_Data_Update("count", task);
        });
        top3_pro_query.get().addOnCompleteListener(task -> {
            // 고단백질 음식 top3
            Top_Data.getInstance().Top_Data_Update("pro", task);
        });

        // 저칼로리 음식 top3
        Top_Data.getInstance().Top3_kcal_month_food(Query.Direction.ASCENDING);
        // 고칼로리 음식 top3
        Top_Data.getInstance().Top3_kcal_month_food(Query.Direction.DESCENDING);
    }

    public void Update_data_for_the_day() {

        Log.i("Update_data_for_the_day", "Update_data_for_the_day");
        Log.i("prev_date", prev_date[0] + prev_date[1] + prev_date[2] + "");

        DocumentReference prev_data = dr_user.collection("user_data")
                .document(String.valueOf(prev_date[0])).collection(String.valueOf(prev_date[1]))
                .document(String.valueOf(prev_date[2]));

        Calendar calendar = Calendar.getInstance();
        calendar.set(prev_date[0], prev_date[1] - 1, prev_date[2]);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);

        DocumentReference dr_week_check = dr_user.collection("user_data")
                .document("week_check").collection("week")
                .document(String.valueOf(day_of_week - 1));

        dr_user.get().addOnCompleteListener(task2 -> {
            DocumentSnapshot document2 = task2.getResult();
            Map<String, Object> prev_date_data = new HashMap<>();
            Map<String, Object> week_check = new HashMap<>();
            String totalKcal = document2.get("TotalFoodKcalOfDay").toString();
            String totalHealth = document2.get("TotalHealthKcalOfDay").toString();

            Log.i("totalKcal",totalKcal );
            Sub_bundle.getInstance().setTotal_food_kcal("0.0");
            Sub_bundle.getInstance().setTotal_health_kcal("0.0");

            if (!totalKcal.equals("") && !totalHealth.equals("")) {
                Log.i("OneDayUserData", "check!!");

                prev_date_data.put("day", prev_date[2]);
                prev_date_data.put("food_kcal", totalKcal);
                prev_date_data.put("health_kcal", totalHealth);
                prev_data.set(prev_date_data);

                //
                week_check.put("data_check", true);
                dr_week_check.update(week_check);

                dr_user.collection("user_data").document("summary")
                        .get()
                        .addOnCompleteListener(task -> {
                            DocumentSnapshot document = task.getResult();

                            String current_week_food_kcal = document.get("current_week_food_kcal").toString();
                            String current_week_health_kcal = document.get("current_week_health_kcal").toString();
                            Double fk = 0.0;
                            Double hk = 0.0;
                            Double t_fk = Double.valueOf(totalKcal);
                            Double t_hk = Double.valueOf(totalHealth);

                            Map<String, Object> c_data = new HashMap<>();

                            if(!current_week_food_kcal.equals("")){
                                fk = Double.valueOf(current_week_food_kcal);
                                hk = Double.valueOf(current_week_health_kcal);

                                fk = (fk + t_fk) / 2;
                                hk = (hk + t_hk) / 2;
                            }
                            else {
                                fk = t_fk;
                                hk = t_hk;
                            }
                            Log.i("current_week_food_kcal", fk + "");

                            c_data.put("current_week_food_kcal", fk);
                            c_data.put("current_week_health_kcal", hk);
                            dr_user.collection("user_data").document("summary").update(c_data);

                            SummarySingleTon.getInstance().setCurrent_week_food_kcal(String.valueOf(fk));
                            SummarySingleTon.getInstance().setCurrent_week_health_kcal(String.valueOf(hk));

                            // 주가 달라졌을 때
                            if(check_next_week()) {
                                week_kcal_data();
                                week_check_data_initial();
                            }

                            Map<String, Object> total_data = new HashMap<>();
                            total_data.put("TotalFoodKcalOfDay", "");
                            total_data.put("TotalHealthKcalOfDay", "");
                            dr_user.update(total_data);

                        });

            }
            else{
                Map<String, Object> total_data = new HashMap<>();
                total_data.put("TotalFoodKcalOfDay", "");
                total_data.put("TotalHealthKcalOfDay", "");
                dr_user.update(total_data);

                //
                week_check.put("data_check", false);
                dr_week_check.update(week_check);

                // 주가 달라졌을 때
                if(check_next_week()) {
                    week_kcal_data();
                    week_check_data_initial();
                }
            }

        });




        /*

        // 최대 , 최소 칼로리 음식 구하는 코드
        cr_cart.get().addOnCompleteListener(task -> {

            Map<String, Object> month_cart = new HashMap<>();
            int count = 0;
            int dayOfWeek = 0;
            calendar.set(prev_date[0], prev_date[1] - 1, prev_date[2]);
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            try {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    Double Eng = Double.valueOf(document.get("Eng").toString());
                    if (count == 0) {
                        maxKcalFood = document.getId();
                        maxKcal = Eng;
                        minKcalFood = document.getId();
                        minKcal = Eng;
                        // Log.i("CHECK1", document.getId() + Eng);
                        count++;
                        continue;
                    }
                    if (maxKcal < Eng) {
                        maxKcal = Eng;
                        maxKcalFood = document.getId();
                    }
                    if (minKcal > Eng) {
                        minKcal = Eng;
                        minKcalFood = document.getId();
                    }
                    // Log.i("CHECK2", document.getId() + Eng);

                    month_cart.put(document.getId(), document.get("Count"));
                }

                Map<String, Object> map = new HashMap<>();
                map.put("minKcalFood", minKcalFood);
                map.put("minKcal", minKcal);
                map.put("maxKcalFood", maxKcalFood);
                map.put("maxKcal", maxKcal);

                // minKcalFood, maxKcalFood 하루용 데이터 업데이트
                dr_summary.collection("week_cart").document(String.valueOf(dayOfWeek))
                        .update(map);

            } catch (Exception e) {
                Log.i("Food Cart", "None Data");
            }
        });

         */
    }

    public void week_kcal_data(){
        // current -> prev
        // current = 0

        dr_user.collection("user_data").document("summary")
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot document = task.getResult();

                    String current_week_food_kcal = document.get("current_week_food_kcal").toString();
                    String current_week_health_kcal = document.get("current_week_health_kcal").toString();

                    Map<String, Object> week_data = new HashMap<>();
                    week_data.put("prev_week_food_kcal", current_week_food_kcal);
                    week_data.put("prev_week_health_kcal", current_week_health_kcal);
                    week_data.put("current_week_food_kcal", "");
                    week_data.put("current_week_health_kcal", "");
                    dr_user.collection("user_data").document("summary").update(week_data);

                    SummarySingleTon.getInstance().setCurrent_week_food_kcal("");
                    SummarySingleTon.getInstance().setCurrent_week_health_kcal("");
                    SummarySingleTon.getInstance().setPrev_week_food_kcal(current_week_food_kcal);
                    SummarySingleTon.getInstance().setPrev_week_health_kcal(current_week_health_kcal);
                });

        week_check_data_initial();
    }

    public void updateWeek() {

        dr_recently.get().addOnCompleteListener(task -> {

            int[] prev_date = new int[3];
            // 날짜를 십진수로 표현
            int num_prev_date = 0;
            int num_today_date = 0;
            // 요일 값을 저장할 변수
            int today_day_of_week = 0;
            int prev_day_of_week = 0;

            // week_recently_date 가 입력되지 않았을 때 오늘 기준 저번주의 날짜로 셋팅한다.
            if (task.getResult()
                    .get("week_update_date").toString().equals("")) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                Date date = calendar.getTime();
                String format = new SimpleDateFormat("yyyy.MM.dd").format(date);
                for (int i = 2; i >= 0; i--) {
                    prev_date[i] = Integer.valueOf(format.split("[.]")[i]);
                    num_prev_date += prev_date[i] * Math.pow(100, i);
                }
            }
            // 아니면 원래 week_recently_date 값 셋팅
            else {
                for (int i = 2; i >= 0; i--) {
                    prev_date[i] = Integer.valueOf(task.getResult()
                            .get("week_update_date").toString().split("[.]")[i]);
                    num_prev_date += prev_date[i] * Math.pow(100, i);
                }
            }

            // Calendar 객체
            Calendar today_calendar = Calendar.getInstance();
            Calendar prev_calendar = Calendar.getInstance();
            // Calendar 객체 setting
            today_calendar.set(today_date.getYear(), today_date.getMonth(), today_date.getDay());
            prev_calendar.set(prev_date[0], prev_date[1] - 1, prev_date[2]);
            // Day of week 얻기
            today_day_of_week = today_calendar.get(Calendar.DAY_OF_WEEK);
            prev_day_of_week = prev_calendar.get(Calendar.DAY_OF_WEEK);

            num_today_date = today_date.getYear() * 10000 + today_date.getMonth() * 100
                    + today_date.getDay();

            if (num_prev_date - prev_day_of_week < num_today_date - 7) {
                if (num_today_date <= num_prev_date + 7 + 7 - prev_day_of_week) {

                    // 최대 칼로리 음식 구하기
                    dr_summary.collection("week_cart")
                            .orderBy("maxKcal", Query.Direction.DESCENDING)
                            .limit(1)
                            .get()
                            .addOnCompleteListener(task12 -> {
                                Map<String, Object> maxKcal = new HashMap<>();
                                for (QueryDocumentSnapshot document : task12.getResult()) {
                                    maxKcal.put("maxKcal", document.get("maxKcal"));
                                    maxKcal.put("maxKcalFood", document.get("maxKcalFood"));
                                }
                                dr_summary.update(maxKcal);
                            });

                    // 최소 칼로리 음식 구하기
                    dr_summary.collection("week_cart")
                            .orderBy("minKcal")
                            .limit(1)
                            .get()
                            .addOnCompleteListener(task12 -> {
                                Map<String, Object> minKcal = new HashMap<>();
                                for (QueryDocumentSnapshot document : task12.getResult()) {
                                    minKcal.put("minKcal", document.get("minKcal"));
                                    minKcal.put("minKcalFood", document.get("minKcalFood"));
                                }
                                dr_summary.update(minKcal);
                            });



                    // food - health 적정량 칼로리 구하기
                    db.collection("Users").document(user.getUid())
                            .collection("user_data").document(String.valueOf(today_date.getYear()))
                            .collection(String.valueOf(today_date.getMonth()))
                            .whereGreaterThan("day", num_prev_date - prev_day_of_week)
                            .whereLessThanOrEqualTo("day", num_prev_date + 7 + 7 - prev_day_of_week)
                            .get()
                            .addOnCompleteListener(task1 -> {
                                int app_amount = 0;
                                Map<String, Object> appropriate = new HashMap<>();
                                for (QueryDocumentSnapshot document : task1.getResult()) {
                                    if (!(Boolean.valueOf(document.get("none_food_data").toString()) ||
                                            Boolean.valueOf(document.get("none_health_data").toString()))) {

                                        // 음수가 될 때는??
                                        app_amount += Integer.valueOf(document.get("food_kcal").toString())
                                                - Integer.valueOf(document.get("food_kcal").toString());
                                    }
                                }
                                appropriate.put("app_amount", app_amount / 7);
                                dr_summary.update(appropriate);
                            });
                } else {
                    // Summary Initialize
                    summaryInitialize();
                }
                WeekCartInitialize();
            }
            Log.i("num_prev_date", String.valueOf(num_prev_date));
        });
    }

    public void updateMonth() {
        Log.i("updateMonth", "updateMonth");

        db.collection("Users").document(user.getUid())
                .collection("user_data").document(String.valueOf(prev_date[0]))
                .collection(String.valueOf(prev_date[1]))
                .orderBy("day")
                .get()
                .addOnCompleteListener(task -> {

                    Calendar calendar = Calendar.getInstance();
                    int startDay_dayOfTheWeek;

                    // Log.i("CEHCK_s_dayOfTheWeek", String.valueOf(startDay_dayOfTheWeek));

                    double[] dayOfTheWeek = new double[]{0, 0, 0, 0, 0, 0, 0};
                    int[] count = new int[]{0, 0, 0, 0, 0, 0, 0};

                    int day_of_the_week = 0;
                    double avg = 0, max = 0;
                    String[] name_dayOfTheWeek = new String[]{"일", "월", "화", "수",
                            "목", "금", "토"};

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        int day = Integer.valueOf(document.get("day").toString());
                        double foodKcal = Double.valueOf(document.get("food_kcal").toString());

                        calendar.set(prev_date[0], prev_date[1] - 1,
                                day);
                        startDay_dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

                        Log.i("dayOfTheWeek_max_check", dayOfTheWeek[startDay_dayOfTheWeek] + "");

                        dayOfTheWeek[startDay_dayOfTheWeek] += foodKcal;
                        count[startDay_dayOfTheWeek] += 1;

                        startDay_dayOfTheWeek++;
                        if (startDay_dayOfTheWeek > 6) {
                            startDay_dayOfTheWeek %= 7;
                        }
                        Log.i("food_kcal", document.get("food_kcal").toString());
                        Log.i("startDay_dayOfTheWeek", String.valueOf(startDay_dayOfTheWeek));
                    }

                    max = dayOfTheWeek[0] / count[0];

                    for (int i = 1; i < 7; i++) {
                        if(count[i] != 0) {
                            avg = dayOfTheWeek[i] / count[i];
                            if (max < avg) {
                                max = avg;
                                day_of_the_week = i;
                            }
                        }
                    }
                    Log.i("max", max + "");
                    Log.i("DayOfTheWeek_lastMonth", name_dayOfTheWeek[day_of_the_week] + "");
                    Log.i("recently_month", update_month + "");
                    Log.i("avg", avg + "");

                    Map<String, Object> map = new HashMap<>();
                    map.put("avg_DayOfTheWeek_lastMonth", max);
                    map.put("DayOfTheWeek_lastMonth", name_dayOfTheWeek[day_of_the_week]);
                    map.put("recently_month", update_month);

                    Sub_bundle.getInstance().setAvg_DayOfTheWeek_lastMonth(String.valueOf(max));
                    Sub_bundle.getInstance().setDayOfTheWeek_lastMonth(name_dayOfTheWeek[day_of_the_week]);
                    Sub_bundle.getInstance().setRecently_month(String.valueOf(update_month));

                    dr_summary.update(map);
                });

    }

    public void summaryInitialize() {
        // Summary Initialize
        Map<String, Object> summaryInitialize = new HashMap<>();
        summaryInitialize.put("maxKcal", "");
        summaryInitialize.put("minKcal", "");
        summaryInitialize.put("maxKcalFood", "");
        summaryInitialize.put("minKcalFood", "");
        db.collection("Users").document(user.getUid())
                .collection("user_data").document("summary")
                .update(summaryInitialize);
    }

    public void WeekCartInitialize() {

        Map<String, Object> map = new HashMap<>();

        for (int i = 1; i <= 7; i++) {
            dr_summary.collection("week_cart")
                    .document(String.valueOf(i))
                    .set(map);
        }
    }

}
