package com.example.Runner8.ui.F_H.calorie.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.F_H.calorie.Adapter.Model.FoodData;
import com.example.Runner8.ui.F_H.calorie.FoodQuickItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FoodQuickAdapter extends RecyclerView.Adapter<FoodQuickAdapter.ViewHolder>
        implements FoodQuickItemClickListener{

    FoodData data;
    private ArrayList<FoodData> dataArrayList;
    FoodQuickItemClickListener listener;

    public FoodQuickAdapter(ArrayList<FoodData> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public FoodQuickAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodquick__item, parent, false);
        return new FoodQuickAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FoodQuickAdapter.ViewHolder holder, int position) {

        data = dataArrayList.get(position);
        java.lang.String name = data.getName();
        double kcal = data.getKcal();
        Integer src = data.getImg_src();

        // Log.i("adapter",src+"");

        getItemViewType(position);
        holder.img.setImageResource(src);
        holder.name.setText(name);
        holder.kcal.setText(Math.round(kcal)+" Kcal");

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public void setOnItemClickListener(FoodQuickItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View v, int position) {
        if(listener != null){
            listener.onItemClick(holder,v,position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, kcal;
        LinearLayout item;


        public ViewHolder(@NonNull @NotNull View view, FoodQuickItemClickListener listener) {
            super(view);

            item = view.findViewById(R.id.quickitem);
            name = view.findViewById(R.id.quick_foodname);
            kcal = view.findViewById(R.id.quick_foodkcal);
            img = view.findViewById(R.id.quick_foodsrc);

            item.setOnClickListener(v -> {
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
