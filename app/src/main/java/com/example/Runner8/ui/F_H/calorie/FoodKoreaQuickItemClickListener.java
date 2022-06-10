package com.example.Runner8.ui.F_H.calorie;

import android.view.View;

import com.example.Runner8.ui.F_H.calorie.Adapter.FoodKoreaQuickAdapter;

public interface FoodKoreaQuickItemClickListener {
    void onItemClick(FoodKoreaQuickAdapter.ViewHolder holder, View v , int position);
}
