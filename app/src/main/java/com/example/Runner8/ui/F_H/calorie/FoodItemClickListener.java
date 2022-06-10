package com.example.Runner8.ui.F_H.calorie;

import android.view.View;

import com.example.Runner8.ui.F_H.calorie.Adapter.FoodAdapter;

public interface FoodItemClickListener {
    void onItemClick(FoodAdapter.ViewHolder holder, View v , int position);
}
