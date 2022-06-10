package com.example.Runner8.ui.map;

import android.view.View;

import com.example.Runner8.ui.map.Adapter.MapAdapter;

public interface ItemClickListener {
    void onItemClick(MapAdapter.ViewHolder holder, View v , int position);
}
