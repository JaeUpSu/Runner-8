package com.example.Runner8;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.Runner8.ui.map.SingleTon.MapSingleTon;
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

public class TestActivity extends AppCompatActivity {

    LineChart map_chart;
    LineData lineData;
    ArrayList<Entry> entries = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);

        map_chart = findViewById(R.id.map_chart);

        setLegend();


        int i=0;
        for(String data: MapSingleTon.getInstance().getSample_altitude()){
            entries.add(new Entry(i,Float.valueOf(data)));
            i++;
        }

        graph_expression(entries);
        setAxis(22);
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

        map_chart.setBackgroundColor(Color.WHITE);
        map_chart.getDescription().setEnabled(false);

        map_chart.setDragEnabled(true);
        map_chart.setScaleEnabled(true);

        // force pinch zoom along both axis
        map_chart.setPinchZoom(true);

        // draw points over time
        map_chart.animateX(1000);

        // get the legend (only possible after setting data)
        Legend l = map_chart.getLegend();
        l.setTextSize(15);
        l.setYOffset(20);
        l.setXOffset(10);

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);
    }
    public void setAxis(float limit) {

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = map_chart.getXAxis();
            xAxis.setDrawLabels(false);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = map_chart.getAxisLeft();
            yAxis.setTextSize(13);
            yAxis.setXOffset(10);

            // disable dual axis (only use LEFT axis)
            map_chart.getAxisRight().setEnabled(false);


            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);


            // axis range
            yAxis.setAxisMaximum(500);              // limit 에 맞춘 추가 값.
            yAxis.setAxisMinimum(0f);

        }

        {   // Health -> Goal , Food -> Limit
            LimitLine ll1 = new LimitLine(limit, "Real");
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


        map_chart.invalidate();
    }

    public void graph_expression(ArrayList<Entry> lineEntries){
        //BarDataSet dataSet1 = new BarDataSet(barEntries1, "HEALTH");
        String datasetLabel = "";


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
        lineDataSet.setFillFormatter((dataSet, dataProvider) -> map_chart.getAxisLeft().getAxisMinimum());

        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_green);
            lineDataSet.setFillDrawable(drawable);
        } else {
            lineDataSet.setFillColor(Color.BLACK);
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet);
        lineData = new LineData(lineDataSets);
        ////dataSet1.setValueTextSize(13.5f);

        //barData = new BarData(dataSet1);
        map_chart.setData(lineData);
        map_chart.invalidate();
    }
}
