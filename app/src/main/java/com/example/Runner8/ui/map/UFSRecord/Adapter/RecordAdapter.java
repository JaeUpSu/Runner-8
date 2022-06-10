package com.example.Runner8.ui.map.UFSRecord.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.map.UFSRecord.Adapter.Model.TopRecord;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private ArrayList<TopRecord> dataArrayList;
    private TopRecord topRecord;

    public RecordAdapter(ArrayList<TopRecord> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        Log.i("onCreateViewHolder", "onCreateViewHolder!!");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_record_top_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        topRecord = dataArrayList.get(position);

        Log.i("topRecord.getDate()", topRecord.getDate());
        holder.item_date.setText(topRecord.getDate());
        holder.item_nickName.setText(topRecord.getNickName());
        holder.item_time.setText(topRecord.getTime() + " 초");
        holder.item_ranking.setText(topRecord.getRanking() + "");

    }
    @Override
    public int getItemCount()
    {
        return dataArrayList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_ranking, item_nickName, item_time, item_date;

        public ViewHolder(View view)
        {
            super(view);
            item_ranking = view.findViewById(R.id.item_ranking);
            item_nickName = view.findViewById(R.id.item_nickName);
            item_time = view.findViewById(R.id.item_time);
            item_date = view.findViewById(R.id.item_date);

        }
    }
}