package com.example.Runner8.ui.F_H.health.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.F_H.health.Adapter.Model.HealthData;
import com.example.Runner8.ui.F_H.health.InterFace.HealthItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HealthListAdapter extends RecyclerView.Adapter<HealthListAdapter.ViewHolder>
        implements HealthItemClickListener {

    HealthData data;
    private ArrayList<HealthData> dataArrayList;
    HealthItemClickListener listener;

    public HealthListAdapter(ArrayList<HealthData> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.healthlistitem, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HealthListAdapter.ViewHolder holder, int position) {
        data = dataArrayList.get(position);
        String name = data.getName();
        Integer src = data.getImg_src();
        Boolean checked = data.getBtnChecked();

        getItemViewType(position);
        holder.button.setBackgroundResource(src);
        holder.button.setChecked(checked);

        if (checked)
            holder.layout.setBackground(ContextCompat.getDrawable(holder.layout.getContext()
                    ,R.drawable.board_content3));
        else
            holder.layout.setBackground(ContextCompat.getDrawable(holder.layout.getContext()
                    ,R.color.white));

        holder.textView.setText(name);
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public void setOnClickListener(HealthItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View v, int position) {
        if(listener != null){
            listener.onItemClick(holder,v,position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ToggleButton button;
        TextView textView;
        LinearLayout layout;
        @SuppressLint("ResourceAsColor")

        public ViewHolder(@NonNull @NotNull View view, HealthItemClickListener listener) {
            super(view);
            layout = view.findViewById(R.id.item_Layout);
            button = view.findViewById(R.id.btn_health);
            textView = view.findViewById(R.id.txt_health);

            button.setClipToOutline(true);
            button.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null){
                    listener.onItemClick(ViewHolder.this, v, position);
                }
            });
        }
        public ToggleButton getButton() {
            return button;
        }
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public HealthData getData(int position){ return dataArrayList.get(position); }

}
