package com.example.Runner8.ui.Graph;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.Runner8.R;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    // Store instance variables
    private int period_num, st_num, final_num, month_average = 0,
            month_count=0;
    private String period, todayDate, graphType ="" ,firstDate = "", unable_period, unablePeriod_num;
    private Integer first_Year = 0, first_Month = 0, first_Day = 0;
    private boolean first_check = false;

    //
    DateRange dateRange = new DateRange();

    // Array
    ArrayList<Entry> lineEntries1, period_lineEntries;
    ArrayList<String> labels;

    // View
    TextView txt_dataGraph, noDataDay, avg_kcal ,txt_graphTitle;
    ImageButton btn_date_pick, btn_line;
    Button btn_period;
    LineChart week_chart, month_chart, year_chart;
    LineData lineData;
    Toolbar toolbar;
    LinearLayout linearLayout;
    LinearLayout graph_linear;
    NiceSpinner spinner_graph;

    FrameLayout graph_no_data;

    // Dialog
    DatePickerDialog dialog;

    // Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // Class
    Today_Date today_date = new Today_Date();
    Pick_Date pick_date;
    Drawer_Graph drawer_graph;
    GraphPeriod graphPeriod;

    // Variable
    String kcal_name;
    String none_data;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_frag);

        // 초기 설정
        period = "일주일"; period_num = 7;

        btn_date_pick = findViewById(R.id.btn_date_pick);
        btn_period = findViewById(R.id.btn_pick_period);
        txt_dataGraph = findViewById(R.id.txt_dataGraph);
        week_chart = findViewById(R.id.chart);
        month_chart = findViewById(R.id.chart);
        year_chart = findViewById(R.id.chart);
        avg_kcal = findViewById(R.id.avg_kcal);
        noDataDay = findViewById(R.id.noDataDay);
        graph_linear = findViewById(R.id.graph_linear);
        spinner_graph = findViewById(R.id.spinner_graph);
        txt_graphTitle = findViewById(R.id.txt_graphTitle);
        graph_no_data = findViewById(R.id.graph_no_data);

        kcal_name = getIntent().getStringExtra("kcal_name");

        Log.i("kcal_name", kcal_name);
        if(kcal_name.equals("food_kcal")) txt_graphTitle.setText("FOOD");
        else txt_graphTitle.setText("HEALTH");

        //barEntries1 = new ArrayList<>();
        //barEntries2 = new ArrayList<>();
        lineEntries1 = new ArrayList<>();
        labels = new ArrayList<>();

        // "." 으로 포맷된 오늘 date
        today_date.setNow();
        todayDate = today_date.getFormat_date();

        //date = String.valueOf(pickedYear)+"."+String.valueOf(pickedMonth)+"."+String.valueOf(pickedDay);
        txt_dataGraph.setText(todayDate);

        // ToolBar
        toolbar = findViewById(R.id.graph_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // View 객체 가져오기
        View view = getLayoutInflater().inflate(R.layout.activity_food_graph,null,false);

        // btn_period.setText(period);
        List<String> dataset = new LinkedList<>(Arrays.asList("일주일", "한 달", "일 년"));
        spinner_graph.attachDataSource(dataset);
        spinner_graph.setOnSpinnerItemSelectedListener((parent, view1, position, id) -> {
            Log.i("position", position + "");
            switch (position){
                case 0 :
                    // btn_period.setText("일주일");
                    period = "일주일";
                    period_num = 7;

                    drawer_graph = new Drawer_Graph(this, week_chart);
                    ReSetting_Graph();
                    break;
                case 1 :
                    // btn_period.setText("한달");
                    period = "한달";
                    period_num = 4;

                    drawer_graph = new Drawer_Graph(this, month_chart);
                    ReSetting_Graph();

                    break;
                case 2:
                    // btn_period.setText("일년");
                    period ="일년";
                    period_num = 4;

                    drawer_graph = new Drawer_Graph(this, year_chart);
                    ReSetting_Graph();
                    break;
            }
        });

        /*
        btn_period.setOnClickListener(v1 -> {
            final PopupMenu popupMenu = new PopupMenu(this, v1);
            popupMenu.getMenuInflater().inflate(R.menu.graph_period_sel,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {

                if (menuItem.getItemId() == R.id.action_menu1) {
                    btn_period.setText("일주일");
                    period = "일주일";
                    period_num = 7;

                    drawer_graph = new Drawer_Graph(week_chart);
                    ReSetting_Graph();
                }else if (menuItem.getItemId() == R.id.action_menu2){
                    btn_period.setText("한달");
                    period = "한달";
                    period_num = 4;

                    drawer_graph = new Drawer_Graph(month_chart);
                    ReSetting_Graph();
                }else {
                    btn_period.setText("일년");
                    period ="일년";
                    period_num = 4;

                    drawer_graph = new Drawer_Graph(year_chart);
                    ReSetting_Graph();
                }
                return false;
            });

            popupMenu.show();
        });

         */
        btn_date_pick.setOnClickListener(v12 -> {
            if(drawer_graph.getAble()) {
                Log.i("btn_calendar", "CHECK!!");
                onClickHandler(view);

            } else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("그래프 데이터 부족");
                builder.setMessage(drawer_graph.getUnable_period()+" 기한에서 그래프를 이룰 데이터가" +
                        " 부족합니다. " + drawer_graph.getUnablePeriod_num() + " 사용가능합니다.");
                builder.setPositiveButton("확 인",null);
                AlertDialog dialog = builder.create();

                dialog.show();
            }
        });

        /// data 입력하는  달 - 1 해야 할려는 값으로 나옴
        dialog = new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {

            pick_date = new Pick_Date(year, month, dayOfMonth);
            graphPeriod = new GraphPeriod(period, pick_date.get_setTextDate());

            txt_dataGraph.setText(pick_date.get_setTextDate());

            if(period.equals("일주일")){

                // st_num, final_num 셋팅
                week_st_final_Day_Setting(year, month, dayOfMonth);
                // Drawer
                week_Drawer_Graph(pick_date.getYear(), String.valueOf(Integer.valueOf(pick_date.getMonth()) + 1));
                // SetText
                txt_dataGraph.setText((year) + "." + (month+1) + "." + st_num + " ~ " + year + "." + (month+1) + "." + final_num);
            }
            else if(period.equals("한달")){

                // st_num, final_num 셋팅 필요없음

                // Drawer
                month_Drawer_Graph(pick_date.getYear(), String.valueOf(Integer.valueOf(pick_date.getMonth()) + 1));
                // SetText
                txt_dataGraph.setText((year) + " 년 " + (month+1) + " 월");
            }
            else{
                // st_num, final_num 셋팅 필요없음
                year_Drawer_Graph();
                // SetText
                txt_dataGraph.setText((year) + " 년");
            }
            Log.i("onDateSet", todayDate);

            // 오늘 날짜 기준으로 Dialog 가 켜짐
        }, today_date.getYear(), today_date.getMonth(), today_date.getDay());

        // 초기 그래프 그리기
        new_Initial_Drawer_Graph();
    }

    public void new_Initial_Drawer_Graph(){

        drawer_graph = new Drawer_Graph(this, week_chart);

        first_check = true;

        // 첫 데이터가 들어간 날짜, Period(일주일, 한달, 일년) 와 x축 표현 갯수,
        // Dialog 에 보여질 날짜셋팅(setData_min, max), Dialog 켜짐 여부
        ReSetting_Graph();

        // st_num, final_num 셋팅
        week_st_final_Day_Setting(today_date.getYear(),
                today_date.getMonth() - 1, today_date.getDay() - 7);

        // 오늘 날짜를 기준으로 일주일 데이터를 초기 그래프로 선정
        // week_Drawer_Graph(String.valueOf(today_date.getYear()),
                // String.valueOf(today_date.getMonth()));

        week_Drawer_Graph(String.valueOf(today_date.getYear()),
                String.valueOf(today_date.getMonth()));

        txt_dataGraph.setText((today_date.getYear()) + "." +
                (today_date.getMonth()) + "." + st_num + " ~ "
                + today_date.getYear() + "." + (today_date.getMonth()) + "." + final_num);
    }

    public void week_Drawer_Graph(String year, String month){

        Calendar calendar = Calendar.getInstance();

        Log.i("STNUMFINALNUM", st_num + "" +  final_num);

        CollectionReference choseMonth = db.collection("Users").document(user.getUid())
                .collection("user_data").document(year)
                .collection(month);

        choseMonth.whereGreaterThanOrEqualTo("day", st_num)
                .whereLessThanOrEqualTo("day", final_num)
                .get()
                .addOnCompleteListener(task -> {

                    int st_day_of_week = 0, final_day_of_week = 0, count = 0, sum = 0, average = 0, point = 0;

                    //
                    int check_point = st_num;

                    int task_size = task.getResult().size();

                    String[] day_of_month = new String[]{"일", "월", "화", "수",
                            "목", "금", "토"};
                    String Sum_day_of_month = "";

                    period_lineEntries = new ArrayList<>();
                    period_lineEntries.add(new Entry(0, 0));

                    // None_Click Date
                    calendar.set(Integer.valueOf(year),
                            Integer.valueOf(month) - 1, Integer.valueOf(st_num));
                    st_day_of_week = calendar.get(Calendar.DAY_OF_WEEK);

                    if(st_num == 1 && st_day_of_week > 1){
                        for(int j=1;j<st_day_of_week;j++){
                            period_lineEntries.add(new Entry(j, 0));
                            Log.i("J", String.valueOf(j));
                            count++;
                            point++;
                        }
                    }
                    //
                    if(task.getResult().size() == 0) {
                        while(check_point != final_num){
                            period_lineEntries.add(new Entry(st_day_of_week++,
                                    0));
                            Sum_day_of_month += day_of_month[point] + " ";
                            check_point++;
                            count++;
                            point++;
                        }
                        if(check_point == final_num){
                            period_lineEntries.add(new Entry(st_day_of_week++,
                                    0));
                            Sum_day_of_month += day_of_month[point] + " ";
                        }
                    }

                    else {
                        Log.i("check_point", String.valueOf(check_point));
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Log.i("DOCUMENT (ID)", document.getId());
                            Log.i("DOCUMENT (food_kcal)", document.getData().get(kcal_name).toString());

                            int kcal = (int) Math.round(Double.valueOf(document.get(kcal_name).toString()));

                            while (check_point != Integer.valueOf(document.getId())) {
                                Log.i("check_point", String.valueOf(check_point));
                                Log.i("point", point + "");
                                period_lineEntries.add(new Entry(st_day_of_week++,
                                        0));
                                Sum_day_of_month += day_of_month[point] + " ";
                                check_point++;
                                point++;
                                count++;
                            }
                            period_lineEntries.add(new Entry(st_day_of_week++,
                                    kcal));
                            sum += kcal;
                            point++;
                            check_point++;
                        }
                        if(check_point < final_num){
                            while(check_point != final_num){
                                Sum_day_of_month += day_of_month[point] + " ";
                                period_lineEntries.add(new Entry(st_day_of_week++,
                                        0));
                                count++;
                                check_point++;
                                point++;
                            }
                        }

                        if(check_point == final_num) {
                            period_lineEntries.add(new Entry(st_day_of_week++,
                                    0));
                            count++;
                            Sum_day_of_month += day_of_month[point] + " ";
                        }
                    }

                    // None_Click Date
                    calendar.set(Integer.valueOf(year),
                            Integer.valueOf(month) - 1, Integer.valueOf(final_num));
                    final_day_of_week = calendar.get(Calendar.DAY_OF_WEEK);

                    if(final_num == calendar.getActualMaximum(Calendar.DAY_OF_MONTH) && final_day_of_week < 7){
                        for(int k = final_day_of_week + 1; k <= 7; k++){
                            period_lineEntries.add(new Entry(k, 0));
                            count++;
                            Log.i("K", String.valueOf(k));
                        }
                    }

                    // AVERAGE
                    average = sum / (7 - count);
                    Log.i("AVERAGE", (sum) + " " +  (count) + " " + (average));

                    // SetText
                    noDataDay.setText(Sum_day_of_month);
                    avg_kcal.setText(String.valueOf(average) + " Kcal");

                    Drawer_Graph();
                });
    }

    public void month_Drawer_Graph(String year, String month){

        Log.i("Date", year + " " + month);

        month_average = 0;

        db.collection("Users").document(user.getUid())
                .collection("user_data").document(year)
                .collection(month)
                .orderBy("day")
                .get()
                .addOnCompleteListener(task -> {
                    Log.i("month", month + "");
                    Log.i("size", task.getResult().size() + "");

                    Integer[] count = {0, 0, 0, 0};
                    Integer[] sum = {0, 0, 0, 0};
                    Integer[] average = {0, 0, 0, 0};
                    Integer[] week_count = {7, 7, 7, 0};
                    int entry_index = 1, month_count = 0;
                    int check_point = 1;

                    period_lineEntries = new ArrayList<>();
                    period_lineEntries.add(new Entry(0, 0));

                    if(task.getResult().size() == 0){
                        // FrameLayout 사용하기
                        graph_no_data.setVisibility(View.VISIBLE);
                        graph_linear.setVisibility(View.INVISIBLE);
                    }
                    else{
                        // FrameLayout 사용하기
                        graph_no_data.setVisibility(View.INVISIBLE);
                        graph_linear.setVisibility(View.VISIBLE);

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            int id = Integer.valueOf(document.getId());

                            Log.i("ID", String.valueOf(id));

                            if(id <= 7){
                                while(check_point != Integer.valueOf(document.getId())){
                                    check_point++;
                                    count[0]++;
                                }

                                int tem_kcal = (int) Math.round(Double.valueOf(document.get(kcal_name).toString()));
                                sum[0] += tem_kcal;

                                Log.i("SUM", String.valueOf(sum[0]));
                                check_point++;
                            }
                            else if(id <= 14){
                                while(check_point != Integer.valueOf(document.getId())){
                                    check_point++;
                                    count[1]++;
                                }

                                int tem_kcal = (int) Math.round(Double.valueOf(document.get(kcal_name).toString()));
                                sum[1] += tem_kcal;
                                Log.i("check_point", String.valueOf(check_point));
                                check_point++;
                                Log.i("count", String.valueOf(count[1]));

                            }
                            else if(id <= 21){
                                while(check_point != Integer.valueOf(document.getId())){
                                    check_point++;
                                    count[2]++;
                                }

                                int tem_kcal = (int) Math.round(Double.valueOf(document.get(kcal_name).toString()));
                                sum[2] += tem_kcal;
                                Log.i("check_point", String.valueOf(check_point));
                                check_point++;
                                Log.i("count", String.valueOf(count[1]));
                            }
                            else{
                                while(check_point != Integer.valueOf(document.getId())){
                                    check_point++;
                                    count[3]++;
                                    week_count[3]++;
                                }

                                int tem_kcal = (int) Math.round(Double.valueOf(document.get(kcal_name).toString()));
                                sum[3] += tem_kcal;
                                week_count[3]++;

                                Log.i("check_point", String.valueOf(check_point));
                                check_point++;
                                Log.i("count", String.valueOf(count[1]));
                            }
                        }

                        for(int i=0;i<4;i++){
                            Log.i("COUNT",  (week_count[i])+ " " +  (count[i]));
                            if(count[i] == week_count[i]) {
                                average[i] = 0;
                                continue;
                            }

                            average[i] = sum[i] / week_count[i] - count[i];
                            period_lineEntries.add(new Entry(entry_index++,
                                    average[i]));

                            month_average += average[i];
                            month_count += count[i];
                        }

                        //
                        noDataDay.setText( " 총 " + month_count + " 일");
                        avg_kcal.setText((month_average / 4) + " Kcal");

                        Drawer_Graph();
                    }
                });
    }

    public void year_Drawer_Graph(){



        db.collection("Users").document(user.getUid())
                .collection("user_data").document("yearData")
                .collection(pick_date.getYear())
                .orderBy("month")
                .get()
                .addOnCompleteListener(task -> {

                    int first_day_year = Integer.valueOf(Sub_bundle.getInstance()
                            .getFirst_date().split("[.]")[0]);
                    int first_day_month = Integer.valueOf(Sub_bundle.getInstance()
                            .getFirst_date().split("[.]")[1]);

                    int[] month_avg_data = new int[13];
                    int[] quarter_avg_data = new int[4];
                    int i=1, final_month_count = 0, cutLine, year_avg_data;

                    period_lineEntries = new ArrayList<>();
                    period_lineEntries.add(new Entry(0, 0));

                    while( i < first_day_month && first_day_year == Integer.valueOf(pick_date.getYear())){
                        month_avg_data[i] = 0;
                        i++;
                    }

                    cutLine = i;

                    for(QueryDocumentSnapshot document : task.getResult()){
                        month_avg_data[Integer.valueOf(document.getId())] =
                                Integer.valueOf(document.get("average").toString());
                        final_month_count += Integer.valueOf(document.get("count").toString());
                    }

                    int l = 1;
                    while(l<=3){
                        quarter_avg_data[0] += month_avg_data[l];
                        quarter_avg_data[1] += month_avg_data[l + 3];
                        quarter_avg_data[2] += month_avg_data[l + 6];
                        quarter_avg_data[3] += month_avg_data[l + 9];
                        l++;
                    }

                    for(int j=0;j<4;j++) quarter_avg_data[j] /= 4;

                    if(cutLine<=3){
                        year_avg_data = (quarter_avg_data[0] + quarter_avg_data[1] +
                                quarter_avg_data[2] + quarter_avg_data[3]) / 4;
                    }
                    else if(cutLine<=6){
                        year_avg_data = (quarter_avg_data[1] +
                                quarter_avg_data[2] + quarter_avg_data[3]) / 3;
                    } else if (cutLine <= 9)
                        year_avg_data = (quarter_avg_data[2] + quarter_avg_data[3]) / 2;
                    else year_avg_data = quarter_avg_data[3];

                    for(int k=1;k<5;k++){
                        period_lineEntries.add(new Entry(k,
                                quarter_avg_data[k-1]));
                    }

                    noDataDay.setText( " 총 " + final_month_count + " 일");
                    avg_kcal.setText((year_avg_data) + " Kcal");

                    Log.i("LABELS", drawer_graph.getPeriod());

                    Drawer_Graph();
                });
    }

    // 첫 데이터가 들어간 날짜, Period(일주일, 한달, 일년) 와 x축 표현 갯수,
    // Dialog 에 보여질 날짜셋팅(setData_min, max), Dialog 켜짐 여부
    public void ReSetting_Graph() {

        Log.i("ReSetting_Graph", "ReSetting_Graph");
        Log.i("getFirst_date()", Sub_bundle.getInstance().getFirst_date());

        today_date.setNow();
        boolean none_data_check = dateRange.check_this_week(
                today_date.getFormat_date(), Sub_bundle.getInstance().getFirst_date());

        Log.i("none_data_check", none_data_check + "");
        if (none_data_check) {

            // FrameLayout 사용하기
            graph_no_data.setVisibility(View.VISIBLE);
            graph_linear.setVisibility(View.INVISIBLE);

        } else {

            graph_no_data.setVisibility(View.INVISIBLE);
            graph_linear.setVisibility(View.VISIBLE);

            Log.i("ReSetting_Graph, period", period);

            drawer_graph.setFirstDate(Sub_bundle.getInstance().getFirst_date());
            drawer_graph.setPeriod(period);
            drawer_graph.setPeriod_num(period_num);
            drawer_graph.setDate_min(dialog);
            drawer_graph.setDate_max(dialog);
            drawer_graph.setPeriod_Pick();
            drawer_graph.setBtn_calOnOff();
        }
    }

    public void week_st_final_Day_Setting(int pick_year, int pick_month, int pick_day){

        if(pick_day < 0) pick_day = 1;

        Calendar calendar = Calendar.getInstance();
        calendar.set(pick_year, pick_month, pick_day);

        int pick_day_of_week = calendar.get(Calendar.DAY_OF_WEEK);

        Log.i("PICK", "pick_day" + pick_day + "pick_month" + pick_month + "pick_day_of_week" + pick_day_of_week);

        st_num = (Integer.valueOf(pick_day)- pick_day_of_week) + 1;
        final_num = Integer.valueOf(pick_day) + (7 - pick_day_of_week);

        Log.i("NUM", (st_num) + " " +  (final_num));

        if(pick_day < pick_day_of_week)
            st_num = 1;

        int month_final_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(pick_year, pick_month, month_final_day);
        int final_day_of_week = calendar.get(Calendar.DAY_OF_WEEK);

        if(final_num > month_final_day && final_day_of_week != 7)
            final_num = month_final_day;

        Log.i("month_final_day", String.valueOf(month_final_day));
    }

    public void Drawer_Graph(){
        // Drawer Graph
        // drawer_graph.setting_GraphLabels();
        if(kcal_name.equals("food_kcal")) drawer_graph.setAxis(Float.valueOf(Sub_bundle.getInstance().getApp_amount()));
        else {
            Log.i("health_checkk", "checkcekce");
            drawer_graph.setAxis(300);
        }

        drawer_graph.setLegend();
        drawer_graph.graph_expression(period_lineEntries);
    }

    public void onClickHandler(View view){
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}