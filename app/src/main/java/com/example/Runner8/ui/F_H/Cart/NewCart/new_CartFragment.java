package com.example.Runner8.ui.F_H.Cart.NewCart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.F_H.calorie.Adapter.Model.FoodData;

import java.util.ArrayList;

import nl.dionsegijn.steppertouch.StepperTouch;


public class new_CartFragment extends Fragment {

    RecyclerView foodList;
    StepperTouch numBtn;
    TextView totalKcalTxt, totalMealTxt, totalProteinTxt;
    ImageButton btn_delete;

    FoodCartAdapter foodCartAdapter;
    ArrayList<FoodData> dataArrayList;
    Boolean pick_flag = false;
    Integer pick_pos, pick_num;
    Double total_kcal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.food_cart_frag, container, false);
    }

    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foodList = view.findViewById(R.id.foodCartList);
        numBtn = view.findViewById(R.id.stepperTouch);
        totalKcalTxt = view.findViewById(R.id.txt_totalKcal);
        totalMealTxt = view.findViewById(R.id.txt_totalMeal);
        totalProteinTxt = view.findViewById(R.id.txt_totalProtein);
        btn_delete = view.findViewById(R.id.btn_delete);

        pick_pos = -1;
        total_kcal = 0.0;
        dataArrayList = new ArrayList<>();
        sampleDataArr();

        totalKcalTxt.setText("Total Kcal : "+total_kcal);
        foodCartAdapter = new FoodCartAdapter(dataArrayList);
        foodList.setLayoutManager(new LinearLayoutManager(getActivity()
                , LinearLayoutManager.VERTICAL, false));
        foodList.setAdapter(foodCartAdapter);

        foodCartAdapter.setOnItemClickListener((holder, v, position) -> {

            setCheckedDataChange(position);

            if (!pick_pos.equals(-1)){
                numBtn.setCount(dataArrayList.get(pick_pos).getNum());
                numBtn.setMinValue(1);
                numBtn.setSideTapEnabled(true);
            }
        });
        /*
        totalKcalTxt.setOnClickListener(view1 -> getParentFragmentManager().beginTransaction()
                .replace(R.id.navHostFragment,new FH_Fragment()).commit());

         */
        numBtn.addStepCallback((i, b) -> {
            numBtn.allowNegative(pick_num > numBtn.getMinValue());
            dataArrayList.get(pick_pos).setNum(numBtn.getCount());
            foodCartAdapter.notifyDataSetChanged();
        });
        btn_delete.setOnClickListener(view12 -> onDelete(pick_pos));
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void sampleDataArr(){
        Integer[] src_arr = new Integer[]{
                R.drawable.koreafood, R.drawable.japanfood, R.drawable.steak
        };
        java.lang.String[] name_arr = new java.lang.String[]{
                "된장찌개", "초 밥", "스테이크"
        };
        Double[] kcal_arr = new Double[]{
                201.4,90.1,303.2
        };
        Integer[] num_arr = new Integer[]{
                1, 2, 1
        };

        for (int i =0; i<3; i++){
            FoodData data = new FoodData();
            data.setImg_src(src_arr[i]);
            data.setKcal(kcal_arr[i]);
            data.setName(name_arr[i]);
            data.setNum(num_arr[i]);

            total_kcal += (kcal_arr[i] * num_arr[i]);

            dataArrayList.add(data);
        }
    }
    public void setCheckedDataChange(int position){

        // pick_flag - 뭐라도 선택이 되어있을때 true
        if (pick_flag){
            if (!dataArrayList.get(position).getChecked()){
                dataArrayList.get(position).setChecked(true);
                pick_pos = -1;
                pick_flag = false;
                numBtn.setVisibility(View.INVISIBLE);
                btn_delete.setVisibility(View.INVISIBLE);
            }
            else {
                dataArrayList.get(pick_pos).setChecked(true);
                dataArrayList.get(position).setChecked(false);
                pick_pos = position;
                numBtn.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
                pick_num = dataArrayList.get(position).getNum();
            }
        }else{
            pick_pos = position;
            pick_flag =true;
            dataArrayList.get(position).setChecked(false);
            numBtn.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(View.VISIBLE);
            pick_num = dataArrayList.get(position).getNum();
        }
        foodCartAdapter.notifyDataSetChanged();
    }
    public void onDelete(int position) {
        dataArrayList.remove(position);
        pick_flag = false;
        numBtn.setVisibility(View.INVISIBLE);
        btn_delete.setVisibility(View.INVISIBLE);
        foodCartAdapter.notifyDataSetChanged();
    }
}
