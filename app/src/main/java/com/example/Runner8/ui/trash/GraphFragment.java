package com.example.Runner8.ui.trash;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Runner8.R;
import com.example.Runner8.ui.Graph.GraphPeriod;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GraphFragment extends Fragment {

    // Store instance variables
    private int period_num, today_Year, today_Month, today_Day;
    private String period, date, graphType ="" ,firstDate = "", unable_period, unablePeriod_num;
    private Integer first_Year = 0, first_Month = 0, first_Day = 0;
    private boolean able = false;
    ArrayList<BarEntry> barEntries1, barEntries2;
    ArrayList<Entry> lineEntries1, lineEntries2;
    ArrayList<String> labels;

    TextView tvdate;
    ImageButton btn_cal, btn_line;
    Button btn_period;
    LineChart chart;
    LineData lineData;

    DatePickerDialog dialog;
    DatePickerDialog.OnDateSetListener callbackMethod;

    // newInstance constructor for creating fragment with arguments

    // Store instance variables ba
    // sed on arguments passed

    public GraphFragment(){}

    public static GraphFragment newInstance(int num){
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putInt("num",num);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables ba
    // sed on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        int frag_num = getArguments().getInt("num", 0);
        period = "일주일"; period_num = 7;
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_food_graph,container,false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState){
        super.onViewCreated(view,savedInstancdState);

        btn_cal = view.findViewById(R.id.btn_calendal);
        btn_period = view.findViewById(R.id.btn_pick_period);
        tvdate = view.findViewById(R.id.tv_pick_date1);
        chart = view.findViewById(R.id.plus_chart);

        Date today = new Date();
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat month = new SimpleDateFormat("MM");
        SimpleDateFormat day = new SimpleDateFormat("dd");

        //barEntries1 = new ArrayList<>();
        //barEntries2 = new ArrayList<>();
        lineEntries1 = new ArrayList<>();
        labels = new ArrayList<>();

        today_Year = Integer.parseInt(year.format(today));
        today_Month = Integer.parseInt(month.format(today));
        today_Day = 4;//Integer.parseInt(day.format(today));
        date = String.valueOf(today_Year)+"."+String.valueOf(today_Month)+"."+String.valueOf(today_Day);

        setFirstDate("2021.07.02");         /////// 첫 데이터 입력하면 첫 날짜만 따로 등록
        /// data 입력하는  달 - 1 해야 할려는 값으로 나옴
        dialog = new DatePickerDialog(getContext(),
                callbackMethod, today_Year, today_Month, today_Day);
        Log.i("hereerer" , String.valueOf(first_Month) + String.valueOf(today_Month));
        GraphPeriod GP2 = new GraphPeriod(period, date);
        //barEntries1 = GP2.getDataSets1();
        lineEntries1 = GP2.getDataSets2();
        labels = GP2.getPick_GraphLabels();
        setAxis();

        btn_period.setText(period);
        btn_period.setOnClickListener(v1 -> {
            final PopupMenu popupMenu = new PopupMenu(getContext(), v1);
            popupMenu.getMenuInflater().inflate(R.menu.graph_period_sel,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.action_menu1){
                    btn_period.setText("일주일"); period = "일주일";
                    period_num = 7;
                }else if (menuItem.getItemId() == R.id.action_menu2){
                    btn_period.setText("한달"); period = "한달";
                    period_num = 4;
                }else {
                    btn_period.setText("일년"); period ="일년";
                    period_num = 4;
                }
                setDate_min();        // set MIN date
                setDate_max();        // set MAX date
                setBtn_calOnOff();    // able : true or false
                GraphPeriod GP1 = new GraphPeriod(period, date);
                //barEntries1 = GP1.getDataSets1();
                lineEntries1 = GP1.getDataSets2();
                labels = GP1.getPick_GraphLabels();
                LineDataSet lineDataSet = new LineDataSet(lineEntries1,"HEALTH");
                ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
                lineDataSets.add(lineDataSet);
                lineData = new LineData(lineDataSets);setAxis();
                chart.notifyDataSetChanged();
                chart.invalidate();
                Log.i("HERE",labels.get(0)+labels.get(1)+labels.get(2)+labels.get(3));
                return false;
            });
            popupMenu.show();
        });
        //date = String.valueOf(pickedYear)+"."+String.valueOf(pickedMonth)+"."+String.valueOf(pickedDay);
        tvdate.setText(String.valueOf(today_Year)+"."+String.valueOf(today_Month)+"."+String.valueOf(today_Day));

        setDate_min();        // set MIN date
        setDate_max();        // set MAX date
        setBtn_calOnOff();    // able : true or false
        Log.i("real","hear");
        btn_cal.setOnClickListener(v12 -> {
            if(able) {
                onClickHandler(view);
                this.initializeDateTV();
            } else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("그래프 데이터 부족");
                builder.setMessage(unable_period+" 기한에서 그래프를 이룰 데이터가" +
                        " 부족합니다. " + unablePeriod_num + " 사용가능합니다.");
                builder.setPositiveButton("확 인",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        this.initializeDateTV();
        setLegend();

        //BarDataSet dataSet1 = new BarDataSet(barEntries1, "HEALTH");
        LineDataSet lineDataSet = new LineDataSet(lineEntries1,"HEALTH");
        lineDataSet.setValueTextSize(14.5f);
        lineDataSet.setColor(Color.GREEN);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillAlpha(55);
        lineDataSet.setLineWidth(2f);

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet);
        lineData = new LineData(lineDataSets);
        ////dataSet1.setValueTextSize(13.5f);

        //barData = new BarData(dataSet1);

        chart.setData(lineData);
        chart.invalidate();

    }
    // 데이터를 입력한 첫날
    private void setFirstDate(String date){
        firstDate = date;
        first_Year = Integer.parseInt(date.split("[.]")[0]);
        first_Month = Integer.parseInt(date.split("[.]")[1]) - 1;
        first_Day = Integer.parseInt(date.split("[.]")[2]);
    }

    private void setBtn_calOnOff(){
        int first = first_Day + (first_Month+1) * 100 + first_Year * 10000;
        int today = today_Day + today_Month * 100 + today_Year * 10000;
        Calendar calendar = Calendar.getInstance();

        if( period =="일주일")   // 첫입력이 일주일채 안될때 주차가 안바뀐상태에서 검색 false
        {
            if(today - first < 7){
                calendar.set(first_Year, first_Month, first_Day);
                int day1 = calendar.get(Calendar.DAY_OF_WEEK);
                Log.i("여기임", calendar.getTime().toString() +String.valueOf(day1));
                calendar.set(today_Year, today_Month-1, today_Day);
                int day2 = calendar.get(Calendar.DAY_OF_WEEK);

                Log.i("여기임", calendar.getTime().toString() + String.valueOf(day2));

                if(day2 >= day1) {
                    able = false; unable_period = "일주일";
                    unablePeriod_num = String.valueOf(8-day2)+"일 뒤에";
                }else { able = true;}
            }else { able = true;}
        }       // 같은 해인데 같은 달인경우 false
        else if(period == "한달"){
            if(first_Year == today_Year && first_Month == today_Month-1)
            {able = false; unable_period = "한달";
                unablePeriod_num= String.valueOf(first_Month +2) + "월 부터";}
            else
                able = true;
        }else if(period == "일년")
            if(first_Year == today_Year)
            {able = false; unable_period = "일년"; unablePeriod_num= String.valueOf(first_Year +1) + "년 부터";}
            else
                able = true;
    }

    private void setDate_min(){
        Calendar calendar = Calendar.getInstance();

        calendar.set(first_Year,first_Month,first_Day);
        dialog.getDatePicker().setMinDate(calendar.getTime().getTime());
    }
    private void setDate_max(){
        Calendar calendar = Calendar.getInstance();

        if (period == "일주일")
        {
            Log.i("1","여기");
            calendar.set(today_Year, today_Month-1, today_Day);
            dialog.getDatePicker().setMaxDate(calendar.getTime().getTime());
        }       //  today Date - 7
        else if(period == "한달")
        {
            Log.i("2",String.valueOf(first_Month)+ String.valueOf(today_Month));
            int month_finalday = 0;
            calendar.set(today_Year, today_Month-2, 1);
            month_finalday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(today_Year, today_Month-2, month_finalday);
            dialog.getDatePicker().setMaxDate(calendar.getTime().getTime());
        }       //  today Month - 1
        else
        {
            Log.i("3","저어어어기");
            calendar.set(today_Year-1, 11, 31);
            dialog.getDatePicker().setMaxDate(calendar.getTime().getTime());
        }       //  today Year - 1
    }
    private void setLegend(){
        chart.getDescription().setEnabled(false); // No description
        chart.setTouchEnabled(false);
        chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        chart.getLegend().setXOffset(10f);
        chart.getLegend().setYOffset(4f);
        chart.getLegend().setTextSize(14f);
    }
    private void setAxis() {
        // set the x axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // Set the x axis to be displayed at the bottom, the default is at the top
        xAxis.setDrawGridLines(false); // Set this to true to draw grid lines for this axis.
        xAxis.setTextSize(12f);
        xAxis.setLabelCount(period_num);

        xAxis.setValueFormatter((value, axis) -> {
            if ((int) value < labels.size()) {
                return labels.get((int)value);
            } else {
                return "";
            }
        });
        // Set the format of the value displayed on the x-axis
        xAxis.setYOffset(15); // Set the offset of the label to the x axis, the vertical direction

        // Set the y-axis, there are two y-axis, left and right
        YAxis yAxis_right = chart.getAxisRight();
        yAxis_right.setDrawLabels(false);
        yAxis_right.setAxisMaximum(1200f);  // Set the maximum value of the y axis
        yAxis_right.setAxisMinimum(0f);  // Set the minimum value of the y axis
        yAxis_right.setEnabled(false);  // Do not display the right y axis
        YAxis yAxis_left = chart.getAxisLeft();
        yAxis_left.setDrawGridLines(false);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setTextSize(14f); // Set the label size of the y axis
        chart.invalidate();
    }
    public void initializeDateTV(){
        callbackMethod = (view, year, month, dayOfMonth) -> {
            date = year +"."+month+"."+dayOfMonth;
            tvdate.setText(year + "." + month + "." + dayOfMonth);
        };
    }
    public void onClickHandler(View view){
        dialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
