package com.example.Runner8.ui.F_H.calorie.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.F_H.calorie.Adapter.Model.FoodData;
import com.example.Runner8.ui.F_H.calorie.FoodItemClickListener;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>
        implements FoodItemClickListener {

    FoodData data;
    private ArrayList<FoodData> dataArrayList;
    FoodItemClickListener listener;

    public FoodAdapter(ArrayList<FoodData> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodlistitem, parent, false);
        return new ViewHolder(view,this);
    }
    public void setOnItemClickListener(FoodItemClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onItemClick(ViewHolder holder, View view, int position){
        if(listener != null){
            listener.onItemClick(holder,view,position);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        data = dataArrayList.get(position);
        java.lang.String name = data.getName();
        Integer src = data.getImg_src();

        getItemViewType(position);
        holder.button.setBackgroundDrawable(ContextCompat.getDrawable(holder.button.getContext(),src));
        holder.textView.setText(name);

    }
    @Override
    public int getItemCount()
    {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton button;
        TextView textView;
        LinearLayout layout;
        @SuppressLint("ResourceAsColor")
        public ViewHolder(View view, final FoodItemClickListener listener)
        {
            super(view);
            layout = view.findViewById(R.id.item_Layout);
            button = view.findViewById(R.id.btn_food);
            textView = view.findViewById(R.id.txt_food);

            button.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (listener != null)
                        listener.onItemClick(ViewHolder.this,v,position);
            });
        }
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public FoodData getData(int position){ return dataArrayList.get(position); }
}