package com.example.Runner8.ui.summary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.Runner8.R;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.example.Runner8.SingleTon.Top_Data;
import com.example.Runner8.ui.Graph.DateRange;
import com.example.Runner8.ui.Graph.Today_Date;
import com.example.Runner8.ui.map.SingleTon.MapSingleTon;
import com.example.Runner8.ui.summary.Model.Top_count_model;
import com.example.Runner8.ui.summary.Model.Top_kcal_model;
import com.example.Runner8.ui.summary.Model.Top_pro_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;

import me.grantland.widget.AutofitTextView;


public class AnalysticsFragment extends Fragment {


    ImageView whereImg;
    TextView whereTxt, kg_summary, goal_kg_summary;

    // RUN
    TextView total_count_run, total_distance_run, total_kcal_run, whereEndPoint;
    NumberProgressBar whereProgress;

    //
    TextView txtMonth_manyday;

    //
    TextView avg_speed_summary;
    TextView minus_avg_kcal_summary;

    //
    TextView none_data_date;

    // TOP
    AutofitTextView freqFood_top3, highKcal_top3, LowKcal_top3, highProtein_top3;

    //
    ImageView img_week_health_kcal, img_week_food_kcal;
    TextView week_food_kcal, week_health_kcal, week_health_kcal_ratio, week_food_kcal_ratio;

    // Class
    Today_Date today_date = new Today_Date();
    DateRange dateRange = new DateRange();

    //
    int st_num = 0, final_num = 0, size = 0, i = 0;
    double current_food_avg = 0;
    double current_health_avg = 0;
    double food_result = 0;
    double health_result = 0;

    double food_ratio = 0;
    double health_ratio = 0;

    String[] day_of_month = new String[]{"일", "월", "화", "수",
            "목", "금", "토"};

    // Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DocumentReference dr_summary = db.collection("Users").document(user.getUid())
            .collection("user_data").document("summary");

    public static AnalysticsFragment newInstance(int num){
        AnalysticsFragment fragment = new AnalysticsFragment();
        Bundle args = new Bundle();
        args.putInt("num",num);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analytics, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        whereImg = view.findViewById(R.id.whereImg);
        whereTxt = view.findViewById(R.id.whereTxt);

        //
        kg_summary = view.findViewById(R.id.kg_summary);
        goal_kg_summary = view.findViewById(R.id.goal_kg_summary);
        //
        total_count_run = view.findViewById(R.id.total_count_run);
        total_distance_run = view.findViewById(R.id.total_distance_run);
        total_kcal_run = view.findViewById(R.id.total_kcal_run);
        whereEndPoint = view.findViewById(R.id.whereEndPoint);
        whereProgress = view.findViewById(R.id.whereProgress);

        //
        week_food_kcal_ratio = view.findViewById(R.id.week_food_kcal_ratio);
        week_health_kcal_ratio = view.findViewById(R.id.week_health_kcal_ratio);
        img_week_health_kcal = view.findViewById(R.id.img_week_health_kcal);
        img_week_food_kcal = view.findViewById(R.id.img_week_food_kcal);
        week_food_kcal = view.findViewById(R.id.week_food_kcal);
        week_health_kcal = view.findViewById(R.id.week_health_kcal);

        //
        txtMonth_manyday = view.findViewById(R.id.txtMonth_manyday);

        //
        avg_speed_summary = view.findViewById(R.id.avg_speed_summary);
        minus_avg_kcal_summary = view.findViewById(R.id.minus_avg_kcal_summary);

        //
        freqFood_top3 = view.findViewById(R.id.freqFood_top3);
        highKcal_top3 = view.findViewById(R.id.highKcal_top3);
        LowKcal_top3 = view.findViewById(R.id.LowKcal_top3);
        highProtein_top3 = view.findViewById(R.id.highProtein_top3);

        //
        none_data_date = view.findViewById(R.id.none_data_date);


        db.collection("Users").document(user.getUid())
                .collection("Profile").document("diet_profile")
                .get()
                .addOnCompleteListener(task3 -> {
                    DocumentSnapshot document2 = task3.getResult();

                    String g_w = document2.get("g_w").toString();
                    String kg = document2.get("kg").toString();

                    kg_summary.setText(kg);
                    goal_kg_summary.setText(g_w);

                });


        // RUN
        int total_count = MapSingleTon.getInstance().getTOTAL_COUNT();
        total_count_run.setText(total_count + " 번");
        double distance = MapSingleTon.getInstance().getTOTAL_DISTANCE();
        double km_distance = Math.round(distance / 1000 * 100) / 100.0;
        total_distance_run.setText((Math.round(distance / 1000 * 100) / 100.0) + " km");
        double total_kcal = MapSingleTon.getInstance().getTOTAL_KCAL();
        total_kcal_run.setText(Math.round(total_kcal * 100) / 100.0 + " kcal");

        setChangeWhereImg(km_distance);

        // DAY OF THE WEEK LAST MONTH
        if(Sub_bundle.getInstance().getRecently_month() != null) {
            txtMonth_manyday.setText(Sub_bundle.getInstance().getDayOfTheWeek_lastMonth() +
                    "요일 " + "( " +
                    (Sub_bundle.getInstance().getAvg_DayOfTheWeek_lastMonth()) + " Kcal )");
        }
        else txtMonth_manyday.setText(" ...");

        double avg_speed = MapSingleTon.getInstance().getTOTAL_AVG_SPEED();
        avg_speed_summary.setText(Math.round(avg_speed * 100) / 100.0 + " m/s");
        minus_avg_kcal_summary.setText("- " + Math.round(total_kcal / total_count * 100) / 100.0 + " kcal");

        // TOP
        getTop3_count();
        getTop3_high_kcal();
        getTop3_pro();
        getTop3_row_kcal();

        // 많이 먹은 요일

        /*
        //
        current_week_data();

        // 입력 안한 요일
        getNone_data_date();

         */

        new_current_week_data();

        new_prev_week_data();

        new_none_data_date();
    }



    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setChangeWhereImg(Double dist){
        double standard = 0;
        if ( dist <= 450 ){
            whereTxt.setText(" 제주도 ");
            whereImg.setImageResource(R.drawable.jeju);
            standard = 450;
            whereEndPoint.setText("450 km");
        }else if ( dist <= 578.3 ){
            whereTxt.setText(" 대한민국 ");
            whereImg.setImageResource(R.drawable.korea);
            standard = 578.3;
            whereEndPoint.setText("578.3 km");
        }else if ( dist <= 1021 ){
            whereTxt.setText(" 일 본 ");
            whereImg.setImageResource(R.drawable.japan);
            standard = 1021;
            whereEndPoint.setText("1021 km");
        }else if ( dist <= 2025 ){
            whereTxt.setText(" 중 국 ");
            whereImg.setImageResource(R.drawable.china);
            standard = 2025;
            whereEndPoint.setText("2025 km");
        }else if ( dist <= 3054 ){
            whereTxt.setText(" 러시아 ");
            whereImg.setImageResource(R.drawable.russia);
            standard = 3054;
            whereEndPoint.setText("3054 km");
        }else if ( dist <= 4985 ){
            whereTxt.setText(" 인 도 ");
            whereImg.setImageResource(R.drawable.india);
            standard = 4985;
            whereEndPoint.setText("4985 km");
        }else if ( dist <= 8371 ){
            whereTxt.setText(" 독 일 ");
            whereImg.setImageResource(R.drawable.germany);
            standard = 8371;
            whereEndPoint.setText("8371 km");
        }else if ( dist <= 8655 ){
            whereTxt.setText(" 영 국 ");
            whereImg.setImageResource(R.drawable.uk);
            standard = 8655;
            whereEndPoint.setText("8655 km");
        }else if ( dist <= 10628 ){
            whereTxt.setText(" 미 국 ");
            whereImg.setImageResource(R.drawable.usa);
            standard = 10628;
            whereEndPoint.setText("10628 km");
        }else if ( dist <= 36787 ){
            whereTxt.setText(" 세계일주 ");
            whereImg.setImageResource(R.drawable.earth);
            standard = 36787;
        }
        whereEndPoint.setText(standard + " km");
        whereProgress.setMax((int) Math.round(standard));
        whereProgress.setProgress((int) Math.round(dist));
    }

    public void getTop3_count(){
        int size = Top_Data.getInstance().getTop_count().size();
        String result = "";

        if(size != 0){
            for(int i=0;i<size;i++){
                Top_count_model top_count_model = Top_Data.getInstance().getTop_count().get(i);
                if(i == size - 1) result += " " + top_count_model.getFood_name() + " (" + top_count_model.getFood_count() + ")";
                else result += " " + top_count_model.getFood_name() + " (" + top_count_model.getFood_count() + ") \n";
            }
        }
        else result = "  ....";

        freqFood_top3.setText(result);
    }
    public void getTop3_high_kcal(){
        int size = Top_Data.getInstance().getTop_high_kcal().size();
        String result = "";
        Log.i("size", size + "");

        if(size != 0){
            for(int i=0;i<size;i++){
                Top_kcal_model top_kcal_model = Top_Data.getInstance().getTop_high_kcal().get(i);
                result += " " + top_kcal_model.getFood_name() + " \n" + "  +" + top_kcal_model.getKcal() + " \n";
            }
        }
        else result = "  ....";

        Log.i("reseult", result);

        highKcal_top3.setText(result);
    }
    public void getTop3_pro(){
        int size = Top_Data.getInstance().getTop_high_pro().size();
        String result = "";

        if(size != 0){
            for(int i=0;i<size;i++){
                Top_pro_model top_pro_model = Top_Data.getInstance().getTop_high_pro().get(i);
                result += " " + top_pro_model.getFood_name() + " \n" + "  +" + top_pro_model.getPro() +" \n";
            }
        }
        else result = "  ....";

        highProtein_top3.setText(result);
    }
    public void getTop3_row_kcal(){
        int size = Top_Data.getInstance().getTop_row_kcal().size();
        String result = "";

        if(size != 0){
            for(int i=0;i<size;i++){
                Top_kcal_model top_kcal_model = Top_Data.getInstance().getTop_row_kcal().get(i);
                result += " " + top_kcal_model.getFood_name() + " \n" + "  +" + top_kcal_model.getKcal() + " \n";
            }
        }
        else result = "  ....";

        LowKcal_top3.setText(result);
    }

    public void new_current_week_data(){

        if(SummarySingleTon.getInstance().getCurrent_week_food_kcal().equals("")){
            week_food_kcal.setText(" ...");
            week_health_kcal.setText(" ...");
        }
        else {
            current_food_avg = Double.valueOf(SummarySingleTon.getInstance().getCurrent_week_food_kcal());
            current_health_avg = Double.valueOf(SummarySingleTon.getInstance().getCurrent_week_health_kcal());

            week_food_kcal.setText("+ " + Math.round(current_food_avg) + " KCAL");
            week_health_kcal.setText("+ " + Math.round(current_health_avg) + " KCAL");
        }
    }

    public void new_prev_week_data(){
        if(SummarySingleTon.getInstance().getPrev_week_food_kcal().equals("")){
            week_food_kcal_ratio.setText(" ...");
            week_health_kcal_ratio.setText(" ...");
        }
        else{

            double food_average = 0;
            double health_average = 0;

            food_average = Double.valueOf(SummarySingleTon.getInstance().getPrev_week_food_kcal());
            health_average = Double.valueOf(SummarySingleTon.getInstance().getPrev_week_health_kcal());

            food_result = current_food_avg - food_average;
            health_result = current_health_avg - health_average;

            food_ratio = Math.round((current_food_avg / food_average - 1) * 100 * 10) / 10;
            health_ratio = Math.round((current_health_avg / health_average - 1) * 100 * 10) / 10;


            if (food_result < 0) {
                week_food_kcal_ratio.setText(food_ratio + " %");
                img_week_food_kcal.setImageResource(R.drawable.down_graph);
            } else {
                week_food_kcal_ratio.setText("+ " + food_ratio + " %");
                img_week_food_kcal.setImageResource(R.drawable.up_graph);
            }

            if (health_result < 0) {
                Log.i("health_result", health_ratio + "");
                week_health_kcal_ratio.setText(health_ratio + " %");
                img_week_health_kcal.setImageResource(R.drawable.down_graph);
            } else {
                week_health_kcal_ratio.setText("+ " + health_ratio + " %");
                img_week_health_kcal.setImageResource(R.drawable.up_graph);
            }


        }
    }

    public void new_none_data_date(){

        if(Sub_bundle.getInstance().getFirst_date().equals("")){
            none_data_date.setText(" ...");
        }
        else {

            Today_Date today_date = new Today_Date();
            today_date.setNow();

            int first_year = Integer.valueOf(Sub_bundle.getInstance().getFirst_date().split("[.]")[0]);
            int first_month = Integer.valueOf(Sub_bundle.getInstance().getFirst_date().split("[.]")[1]);
            int first_day = Integer.valueOf(Sub_bundle.getInstance().getFirst_date().split("[.]")[2]);

            int today_year = Integer.valueOf(today_date.getYear());
            int today_month = Integer.valueOf(today_date.getMonth());
            int today_day = Integer.valueOf(today_date.getDay());

            int first_value = first_year * 10000 + first_month * 100 * first_day;
            int today_value = today_year * 10000 + today_month * 100 * today_day;

            Calendar calendar = Calendar.getInstance();
            calendar.set(today_year, today_month - 1, today_day);
            int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);

            if(first_value < today_value - (day_of_week + 7) + 1){
                db.collection("Users").document(user.getUid())
                        .collection("user_data").document("week_check")
                        .collection("week")
                        .orderBy("day_of_week")
                        .get()
                        .addOnCompleteListener(task -> {


                            String[] day_of_month = new String[]{"일", "월", "화", "수",
                                    "목", "금", "토"};
                            String result = "";

                            int k = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                boolean data_check = Boolean.valueOf(document.get("data_check").toString());
                                if (data_check) {

                                } else {
                                    result += day_of_month[k] + " ";
                                }
                                k++;
                            }

                            if (result.equals("")) none_data_date.setText(" ...");
                            else none_data_date.setText(result);

                        });
            }
            else{
                if(first_value <= today_value - day_of_week){
                    calendar.set(first_year, first_month - 1, first_day);
                    day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
                    i = day_of_week - 1;

                    db.collection("Users").document(user.getUid())
                            .collection("user_data").document("week_check")
                            .collection("week")
                            .whereGreaterThanOrEqualTo("day_of_week", day_of_week)
                            .get()
                            .addOnCompleteListener(task -> {


                                String result = "";

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    boolean data_check = Boolean.valueOf(document.get("data_check").toString());
                                    if (data_check) {

                                    } else {
                                        result += day_of_month[i] + " ";
                                    }
                                    i++;
                                }

                                if (result.equals("")) none_data_date.setText(" ...");
                                else none_data_date.setText(result);

                            });
                }
                else none_data_date.setText(" ...");
            }
        }
    }

    /*
    public void current_week_data(){
        today_date.setNow();
        int year = today_date.getYear();
        int month = today_date.getMonth();
        int day = today_date.getDay();

        dateRange.getWeekRange(year, month, day);
        int[] range = dateRange.getWeekRange(year, month, day);
        st_num = range[0];
        final_num = range[1];

        Log.i("current_week_data range", st_num + "  " + final_num);


        if(dateRange.isData_check()) {

            if(dateRange.getPrev_st() != 0){

            }
            else if(dateRange.getAfter_st() != 0){

            }
            else{

            }

            CollectionReference choseMonth = db.collection("Users").document(user.getUid())
                    .collection("user_data").document(String.valueOf(year))
                    .collection(String.valueOf(month));

            choseMonth.whereGreaterThanOrEqualTo("day", st_num)
                    .whereLessThanOrEqualTo("day", final_num)
                    .get()
                    .addOnCompleteListener(task -> {

                        int check_point = st_num;
                        double food_sum = 0, health_sum = 0;
                        double food_average = 0, health_average = 0;
                        int size = task.getResult().size();

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            // Log.i("current_week document.getId()", document.getId());
                            while (check_point != Integer.valueOf(document.getId())) {
                                Log.i("check_point", String.valueOf(check_point));
                                check_point++;
                            }
                            food_sum += Double.valueOf(document.get("food_kcal").toString());
                            health_sum += Double.valueOf(document.get("health_kcal").toString());
                            check_point++;
                        }
                        Log.i("food_sum", food_sum + "");

                        if (size == 0) {
                            // AVERAGE
                            food_average = 0;
                            health_average = 0;
                        } else if (size == 7) {
                            // AVERAGE
                            food_average = food_sum / (7 - size);
                            health_average = health_sum / (7 - size);
                        } else {
                            // AVERAGE
                            food_average = food_sum / size;
                            health_average = health_sum / size;
                        }
                        current_food_avg = food_average;
                        current_health_avg = health_average;

                        week_food_kcal.setText("+ " + Math.round(current_food_avg) + " KCAL");
                        week_health_kcal.setText("+ " + Math.round(current_health_avg) + " KCAL");

                        Log.i("current_food_avg", current_food_avg + "");
                    });
        }
        else{
            week_food_kcal.setText(" ...");
            week_health_kcal.setText(" ...");
        }


    }

    public void getNone_data_date() {

        today_date.setNow();
        Calendar calendar = Calendar.getInstance();

        int year = today_date.getYear();
        int month = today_date.getMonth();
        int day = today_date.getDay();

        // Calendar Set 을 할 때는 month - 1 해야 원래 값이 나옴
        calendar.set(year, month - 1, day);

        int TodayDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Log.i("TodayDayOfWeek", TodayDayOfWeek + "");

        int[] range = dateRange.getWeekRange(year, month, day - 7);
        st_num = range[0];
        final_num = range[1];

        Log.i("stfinalnum", st_num + "  " + final_num );

        if(dateRange.isData_check()) {
            int first_year = Integer.valueOf(Sub_bundle.getInstance().getFirst_date().split("[.]")[0]);
            int first_month = Integer.valueOf(Sub_bundle.getInstance().getFirst_date().split("[.]")[1]);
            int first_day = Integer.valueOf(Sub_bundle.getInstance().getFirst_date().split("[.]")[2]);

            int[] current_range = dateRange.getWeekRange(year, month, day);
            int currentOfWeek_value = year * 10000 + month * 100 + current_range[0];
            int first_value = first_year * 10000 + first_month * 100 + first_day;

            Log.i("first_value", first_value + "");
            Log.i("currentOfWeek_value", currentOfWeek_value + "");

            // first date 가 이번주 보다 전 이어야 함.
            if (first_value < currentOfWeek_value) {
                CollectionReference choseMonth = db.collection("Users").document(user.getUid())
                        .collection("user_data").document(String.valueOf(year))
                        .collection(String.valueOf(month));

                choseMonth.whereGreaterThanOrEqualTo("day", st_num)
                        .whereLessThanOrEqualTo("day", final_num)
                        .get()
                        .addOnCompleteListener(task -> {

                            calendar.set(year, month - 1, st_num);
                            int point = calendar.get(Calendar.DAY_OF_WEEK) - 1;

                            size = task.getResult().size();
                            int check_point = st_num;
                            int count = 0;
                            int food_sum = 0, health_sum = 0;
                            int food_average = 0, health_average = 0;
                            String Sum_day_of_month = "";
                            String[] day_of_month = new String[]{"일", "월", "화", "수",
                                    "목", "금", "토"};

                            Log.i("size", size + "");

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.i("document.getId()", document.getId());

                                while (check_point != Integer.valueOf(document.getId())) {
                                    Log.i("check_point", String.valueOf(check_point));
                                    Log.i("point", String.valueOf(point));
                                    Sum_day_of_month += day_of_month[point] + " ";
                                    check_point++;
                                    point++;
                                    count++;
                                }

                                food_sum += Double.valueOf(document.get("food_kcal").toString());
                                health_sum += Double.valueOf(document.get("health_kcal").toString());
                                point++;
                                check_point++;

                            }
                            Log.i("prev food_sum", food_sum + "");

                            // 데이터가 없는 경우 확인 해 줌
                            if (check_point < final_num) {
                                while (check_point != final_num) {
                                    Sum_day_of_month += day_of_month[point] + " ";
                                    check_point++;
                                    point++;
                                }
                            }

                            // final_num 인 document 가 없을 때
                            if (check_point == final_num)
                                Sum_day_of_month += day_of_month[point] + " ";

                            if (count == 7) {
                                // AVERAGE
                                food_average = 0;
                                health_average = 0;
                                none_data_date.setText(" ALL..");
                            } else if (count == 0) {
                                if (size == 0) none_data_date.setText(" ...");
                                else {
                                    // AVERAGE
                                    food_average = food_sum / size;               // 총 데이터 개수에서 빼야 함
                                    health_average = health_sum / size;
                                }
                            } else {
                                // AVERAGE
                                food_average = food_sum / size;
                                health_average = health_sum / size;
                            }

                            none_data_date.setText(Sum_day_of_month);

                            Log.i("prev food_sum", food_average + "");

                            Log.i("current_food_avg", current_food_avg + "");
                            Log.i("current_health_avg", current_health_avg + "  " + health_average);

                            food_result = current_food_avg - food_average;
                            health_result = current_health_avg - health_average;

                            food_ratio = Math.round((current_food_avg / food_average - 1) * 100 * 10) / 10;
                            health_ratio = Math.round((current_health_avg / health_average - 1) * 100 * 10) / 10;

                            if (size != 0) {

                                if (food_result < 0) {
                                    week_food_kcal_ratio.setText(food_ratio + " %");
                                    img_week_food_kcal.setImageResource(R.drawable.down_graph);
                                } else {
                                    week_food_kcal_ratio.setText("+ " + food_ratio + " %");
                                    img_week_food_kcal.setImageResource(R.drawable.up_graph);
                                }

                                if (health_result < 0) {
                                    Log.i("health_result", health_ratio + "");
                                    week_health_kcal_ratio.setText(health_ratio + " %");
                                    img_week_health_kcal.setImageResource(R.drawable.down_graph);
                                } else {
                                    week_health_kcal_ratio.setText("+ " + health_ratio + " %");
                                    img_week_health_kcal.setImageResource(R.drawable.up_graph);
                                }
                            }
                        });

            } else none_data_date.setText(" ...");
        }
        else{
            none_data_date.setText(" ...");
        }

    }

     */
}
