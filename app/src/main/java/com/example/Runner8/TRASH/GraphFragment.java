package com.example.Runner8.TRASH;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GraphFragment extends Fragment  {

    TextView txt_title;
    ImageButton btn_datePick;
    LineChart chart;
    NiceSpinner spinnerDate;

    String type_Graph = "health";
    String period = "week";
    String datasetLabel = "last week ( 일 월 화 수 목 금 토 )";
    Integer count = 0;

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.graph_frag, container, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_title = view.findViewById(R.id.txt_graphTitle);
        chart = view.findViewById(R.id.chart);
        spinnerDate = view.findViewById(R.id.spinner_graph);
        btn_datePick = view.findViewById(R.id.btn_date_pick);


        List<String> dataset = new LinkedList<>(Arrays.asList("일주일", "한 달", "일 년"));
        spinnerDate.attachDataSource(dataset);

        spinnerDate.setOnSpinnerItemSelectedListener((parent, view1, position, id) -> {
            switch (position){
                case 1 : period = "month";
                        datasetLabel = "last month ( 1주차 2주차 3주차 4주차 )";
                        break;
                case 2 : period = "year";
                        datasetLabel = "last year ( 1분기 2분기 3분기 4분기 )";
                        break;
                default: period = "week";
                        datasetLabel = "last week ( 일 월 화 수 목 금 토 )";
                        break;
            }
        });

        btn_datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  date picker
            }
        });

        if (type_Graph.equals("음식/food"))
            foodGraph();
        else if (type_Graph.equals("health"))
            healthGraph();

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
    // data ( 값 , UI ) Settings
    private void setData(int count) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            float val = (float) (Math.random() * 180) - 30;
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {

            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, datasetLabel);

            set1.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(4f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(14f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter((dataSet, dataProvider) -> chart.getAxisLeft().getAxisMinimum());

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                // Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
                // set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
    }

    public void foodGraph(){
        txt_title.setText("FOOD");

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
            yAxis.setAxisMaximum(200f);
            yAxis.setAxisMinimum(-50f);

        }

        {   // Health -> Goal , Food -> Limit
            LimitLine ll1 = new LimitLine(150f, "Limit");
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

        // add data
        if (period.equals("week")) setData(7);
        else setData(4);
    }
    public void healthGraph(){
        txt_title.setText("HEALTH");
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
            yAxis.setAxisMaximum(200f);
            yAxis.setAxisMinimum(-50f);

        }

        {   // Health -> Goal , Food -> Limit
            LimitLine ll1 = new LimitLine(150f, "Goal");
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

        // add data
        if (period.equals("week")) setData(7);
        else setData(4);

    }
    public void setType_Graph(String type_graph){
        this.type_Graph = type_graph;
    }
}
