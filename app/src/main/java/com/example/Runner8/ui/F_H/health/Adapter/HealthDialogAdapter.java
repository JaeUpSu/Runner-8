package com.example.Runner8.ui.F_H.health.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.F_H.health.Adapter.Model.HealthData;
import com.example.Runner8.ui.F_H.health.InterFace.HealthDialogItemLongClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HealthDialogAdapter extends RecyclerView.Adapter<HealthDialogAdapter.ViewHolder>
        implements HealthDialogItemLongClickListener {

    HealthData data;
    private ArrayList<HealthData> dataArrayList;
    private HealthDialogItemLongClickListener listener;

    public HealthDialogAdapter(ArrayList<HealthData> healthData){
        this.dataArrayList = healthData;
    }

    @NonNull
    @NotNull
    @Override
    public HealthDialogAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todayhealthlist_item, parent, false);

        return new HealthDialogAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        data = dataArrayList.get(position);

        Log.i("src", data.getName() + "");

        Integer src = data.getImg_src();
        Integer time;

        if(data.getTime() != -999) {
            time = data.getTime();
            holder.txt_time.setText(time + " min");
        }
        else holder.txt_time.setText(" ...");

        String name = data.getName();
        Double kcal = data.getKcal();

        holder.img.setImageResource(src);
        holder.txt_name.setText(name);
        holder.txt_kcal.setText(kcal + "\nKcal");

        getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public void setOnLongClickListener(HealthDialogItemLongClickListener listener) {
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
        TextView txt_time, txt_kcal, txt_name;
        @SuppressLint("ResourceAsColor")
        public ViewHolder(View view, HealthDialogItemLongClickListener listener)
        {
            super(view);
            txt_name = view.findViewById(R.id.txt_healthName);
            txt_time = view.findViewById(R.id.txt_healthTime);
            txt_kcal = view.findViewById(R.id.txt_healthKcal);
            img = view.findViewById(R.id.img_health);

            view.setOnLongClickListener(v -> {
                int position = getAdapterPosition();



                if(listener != null){
                    listener.onItemClick(ViewHolder.this, view, position);
                    return true;
                }

                return false;
            });

        }
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public HealthData getData(int position){ return dataArrayList.get(position); }
}
