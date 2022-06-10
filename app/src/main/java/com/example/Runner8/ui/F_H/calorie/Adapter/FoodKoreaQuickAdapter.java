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
import com.example.Runner8.ui.F_H.calorie.Adapter.Model.KoreaClass;
import com.example.Runner8.ui.F_H.calorie.FoodKoreaQuickItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FoodKoreaQuickAdapter extends RecyclerView.Adapter<FoodKoreaQuickAdapter.ViewHolder>
        implements FoodKoreaQuickItemClickListener {

    KoreaClass data;
    private ArrayList<KoreaClass> dataArrayList;
    FoodKoreaQuickItemClickListener listener;

    public FoodKoreaQuickAdapter(ArrayList<KoreaClass> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public FoodKoreaQuickAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodquick__korea_item,
                parent, false);
        return new FoodKoreaQuickAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FoodKoreaQuickAdapter.ViewHolder holder, int position) {

        data = dataArrayList.get(position);
        java.lang.String name = data.getName();
        Integer src = data.getImg();

        // Log.i("adapter",src+"");

        getItemViewType(position);
        holder.img.setImageResource(src);
        holder.name.setText(name);

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public void setOnItemClickListener(FoodKoreaQuickItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(FoodKoreaQuickAdapter.ViewHolder holder, View v, int position) {
        if(listener != null){
            listener.onItemClick(holder,v,position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;
        LinearLayout item;


        public ViewHolder(@NonNull @NotNull View view, FoodKoreaQuickItemClickListener listener) {
            super(view);

            item = view.findViewById(R.id.korea_quickitem);
            name = view.findViewById(R.id.korea_quick_foodname);
            img = view.findViewById(R.id.korea_quick_foodsrc);

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
}
