package com.example.Runner8.ui.F_H.calorie;

import android.view.View;

import com.example.Runner8.ui.F_H.calorie.Adapter.FoodQuickAdapter;

public interface FoodQuickItemClickListener {
    void onItemClick(FoodQuickAdapter.ViewHolder holder, View v , int position);
}
