package com.example.Runner8.ui.Graph;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.Runner8.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;

public class Drawer_Graph {

    LineChart chart;
    LineData lineData;
    Today_Date today_date;
    int period_num, first_Year, first_Month, first_Day;
    String period, firstDate, unable_period, unablePeriod_num;
    boolean able = false;
    private String  pick_period_name;
    private String[] pick_period;
    ArrayList<String> labels;
    ArrayList<Entry> initial_lineEntries;

    GraphPeriod graphPeriod;

    Activity activity;

    String datasetLabel = " ( 일 월 화 수 목 금 토 )";

    private String[] week = {"","Sun","Mon","Tue","Wed","Tur",
            "Fri","Sat"};
    private String[] month = {"","1주차","2주차","3주차","4주차"};
    private String[] year = {"","1분기","2분기","3분기","4분기"};

    public Drawer_Graph(Activity activity, LineChart chart){
        this.chart = chart;
        this.today_date = new Today_Date();
        this.period = "일주일";
        this.period_num = 7;
        this.today_date.setNow();
        this.activity = activity;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setPeriod_num(int period_num) {
        this.period_num = period_num;
    }

    public String getPeriod() {
        return period;
    }

    public String getUnable_period() {
        return unable_period;
    }

    public String getUnablePeriod_num() {
        return unablePeriod_num;
    }

    public boolean getAble(){
        return able;
    }


    public void Initial_Drawer_Graph(DatePickerDialog dialog){
        // 임시 초기 Entry 설정 클래스
        graphPeriod = new GraphPeriod(period, today_date.getFormat_date());
        initial_lineEntries = graphPeriod.getDataSets2();
        this.labels = graphPeriod.getPick_GraphLabels();

        // setAxis();
        setDate_min(dialog);
        setDate_max(dialog);
        setBtn_calOnOff();
        setLegend();

        graph_expression(initial_lineEntries);
    }

    public void setPeriod_Pick(){
        if(this.period.equals("일주일"))
        {
            this.pick_period_name = "week";
            this.datasetLabel = " ( 일 월 화 수 목 금 토 )";

        }else if(period.equals("한달"))

        {
            this.pick_period_name = "month";
            this.datasetLabel = " ( 1주차 2주차 3주차 4주차 )";
        }else{
            this.pick_period_name = "year";
            this.datasetLabel = " ( 1분기 2분기 3분기 4분기 )";
        }
    }

    public void setting_GraphLabels() {

        setPeriod_Pick();
        this.labels = new ArrayList<>();

        for (int i = 0; i < pick_period.length; i++){
            this.labels.add(pick_period[i]);
        }

    }

    // 데이터를 입력한 첫날
    public void setFirstDate(String date){
        Log.i("setFirstDate", date);
        if(!date.equals("")) {
            firstDate = date;
            first_Year = Integer.parseInt(date.split("[.]")[0]);
            first_Month = Integer.parseInt(date.split("[.]")[1]) - 1;
            first_Day = Integer.parseInt(date.split("[.]")[2]);
        }
    }

    public void setBtn_calOnOff(){

        today_date.setNow();
        Log.i("today_date.getMonth()", today_date.getMonth() - 1 +" ");
        Log.i("period", period);

        int first = first_Day + (first_Month+1) * 100 + first_Year * 10000;
        int today = today_date.getDay() + today_date.getMonth() * 100 + today_date.getYear() * 10000;
        Calendar calendar = Calendar.getInstance();

        if( period.equals("일주일"))   // 첫입력이 일주일채 안될때 주차가 안바뀐상태에서 검색 false
        {
            if(today - first < 7){
                calendar.set(first_Year, first_Month, first_Day);
                int day1 = calendar.get(Calendar.DAY_OF_WEEK);

                calendar.set(today_date.getYear(), today_date.getMonth()-1, today_date.getDay());

                int day2 = calendar.get(Calendar.DAY_OF_WEEK);

                if(day2 >= day1) {
                    able = false;
                    unable_period = "일주일";
                    unablePeriod_num = String.valueOf(8-day2)+"일 뒤에";
                }
                else
                    able = true;
            }else
                able = true;
        }       // 같은 해인데 같은 달인경우 false
        else if(period.equals("한달")){
            Log.i("today_date.getMonth()-1", first_Month + "  " + (today_date.getMonth()-1) + "");
            if(first_Year == today_date.getYear() && first_Month == today_date.getMonth()-1)
            {
                able = false; unable_period = "한달";
                unablePeriod_num= String.valueOf(first_Month +2) + "월 부터";
                Log.i("CHECK", "able = false");
            }
            else{
                able = true;
                Log.i("CHECK", "able = true");
            }
        }else if(period.equals("일년"))
            if(first_Year == today_date.getYear())
            {able = false; unable_period = "일년"; unablePeriod_num= String.valueOf(first_Year +1) + "년 부터";}
            else
                able = true;

        Log.i("setBtn_calOnOff_CHECK", able + unable_period + unablePeriod_num);
        Log.i("ABLE", String.valueOf(able));
    }

    public void setDate_min(DatePickerDialog dialog){
        Calendar calendar = Calendar.getInstance();

        calendar.set(first_Year,first_Month,first_Day);
        dialog.getDatePicker().setMinDate(calendar.getTime().getTime());
    }

    public void setDate_max(DatePickerDialog dialog){
        Calendar calendar = Calendar.getInstance();
        String resultDate = "";

        if (period == "일주일")
        {
            Log.i("1","여기");
            calendar.set(today_date.getYear(), today_date.getMonth()-1, today_date.getDay());
            dialog.getDatePicker().setMaxDate(calendar.getTime().getTime());
        }
        //  today Date - 7
        else if(period == "한달")
        {
            Log.i("2",String.valueOf(first_Month)+ String.valueOf(today_date.getMonth()));
            int month_finalday = 0;
            calendar.set(today_date.getYear(), today_date.getMonth()-2, 1);
            month_finalday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(today_date.getYear(), today_date.getMonth()-2, month_finalday);
            dialog.getDatePicker().setMaxDate(calendar.getTime().getTime());
        }       //  today Month - 1
        else
        {
            Log.i("3","저어어어기");
            if(first_Year == today_date.getYear() - 1){
                calendar.set(today_date.getYear() -1, 11, 31);
            }
            else if(first_Year < today_date.getYear() - 1){
                calendar.set(today_date.getYear() -1, 1, 1);
            }
            dialog.getDatePicker().setMaxDate(calendar.getTime().getTime());
        }       //  today Year - 1

    }

    public void setLegend(){
        /*
        chart.getDescription().setEnabled(false); // No description
        chart.setTouchEnabled(false);
        chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        chart.getLegend().setXOffset(10f);
        chart.getLegend().setYOffset(4f);
        chart.getLegend().setTextSize(14f);


         */

        chart.setBackgroundColor(Color.WHITE);
        chart.getDescription().setEnabled(false);

        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // force pinch zoom along both axis
        chart.setPinchZoom(true);

        // draw points over time
        chart.animateX(1000);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setTextSize(15);
        l.setYOffset(20);
        l.setXOffset(10);

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);
    }
    public void setAxis(float limit) {
        float y_alpha = 2;
        int count = 0;
        float tmp_limit = limit;
        float new_limit = limit;
        while(tmp_limit > 10){
            count += 1;
            tmp_limit /= 10;
        }
        while(count > 0){
            y_alpha *= 10;
            count -= 1;
        }
        new_limit += y_alpha;
        Log.i("limit", y_alpha + limit + " ");

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();
            xAxis.setDrawLabels(false);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();
            yAxis.setTextSize(13);
            yAxis.setXOffset(10);

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);


            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);


            // axis range
            yAxis.setAxisMaximum(new_limit);              // limit 에 맞춘 추가 값.
            yAxis.setAxisMinimum(0f);

        }
        String limit_name = "Limit";
        if(limit == 300) limit_name = "Goal";

        {   // Health -> Goal , Food -> Limit
            LimitLine ll1 = new LimitLine(limit, limit_name);
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(12f);
            ll1.setTypeface(Typeface.DEFAULT);

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true);

            // add limit lines
            yAxis.addLimitLine(ll1);
        }


        chart.invalidate();
    }

    public void graph_expression(ArrayList<Entry> lineEntries){
        //BarDataSet dataSet1 = new BarDataSet(barEntries1, "HEALTH");

        LineDataSet lineDataSet = new LineDataSet(lineEntries,datasetLabel);
        // lineDataSet.setValueTextSize(14.5f);
        // lineDataSet.setColor(Color.GREEN);
        // lineDataSet.setDrawFilled(true);
        // lineDataSet.setFillAlpha(55);
        // lineDataSet.setLineWidth(2f);
        // draw dashed line
        lineDataSet.enableDashedLine(10f, 5f, 0f);

        //
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setDrawCircleHole(false);
        // customize legend entry
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        lineDataSet.setFormSize(15.f);

        // text size of values
        lineDataSet.setValueTextSize(14f);

        // draw selection line as dashed
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);

        // set the filled area
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillFormatter((dataSet, dataProvider) -> chart.getAxisLeft().getAxisMinimum());

        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.fade_green);
            lineDataSet.setFillDrawable(drawable);
        } else {
            lineDataSet.setFillColor(Color.BLACK);
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet);
        lineData = new LineData(lineDataSets);
        ////dataSet1.setValueTextSize(13.5f);

        //barData = new BarData(dataSet1);
        chart.setData(lineData);
        chart.invalidate();
    }

}
