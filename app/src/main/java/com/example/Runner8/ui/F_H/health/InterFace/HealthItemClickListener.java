package com.example.Runner8.ui.F_H.health.InterFace;

import android.view.View;

import com.example.Runner8.ui.F_H.health.Adapter.HealthListAdapter;

public interface HealthItemClickListener {
    void onItemClick(HealthListAdapter.ViewHolder holder, View v, int position);
}
