package com.example.Runner8.ui.map.Adapter;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.map.Adapter.Model.MapData;
import com.example.Runner8.ui.map.ItemClickListener;
import com.example.Runner8.ui.map.ItemLongClickListener;

import java.util.ArrayList;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder>
        implements ItemClickListener, ItemLongClickListener {

    private ArrayList<MapData> dataArrayList;
    ItemClickListener listener;
    ItemLongClickListener longClick_listener;
    MapData data;
    String current_filter_name;

    public MapAdapter(ArrayList<MapData> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        Log.i("onCreateViewHolder", "onCreateViewHolder!!");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mapcourselistitem, parent, false);

        return new ViewHolder(view,this, longClick_listener);
    }


    public void setOnItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position){
        if(listener != null){
            listener.onItemClick(holder,view,position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("onBindViewHolder", "onBindViewHolder!!");

        data = dataArrayList.get(position);
        String name = data.getCourse_name();
        String dist = "";

        if(data.getCourseDist() != null) dist = String.valueOf(Math.round(
                Double.valueOf(data.getCourseDist()) / 1000 * 100) / 100.0);
        String kcal = data.getKcal();

        getItemViewType(position);

        if (position == 0) {
            if (name.equals("RUN MY")) holder.textView.setText("RUN MY");
            holder.button.setBackgroundDrawable(
                    ContextCompat.getDrawable(holder.button.getContext(), R.drawable.runner_selector));
        } else {
            if (current_filter_name.equals("kcal")) {
                kcal += " \nkcal";
                holder.button.setTextOff(kcal);
                holder.button.setTextOn(kcal);
            } else {
                dist += " \nkm";
                holder.button.setTextOff(dist);
                holder.button.setTextOn(dist);

            }
            holder.textView.setText(name);
        }
        holder.button.setChecked(false);
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

    @Override
    public void onItemLongClick(ViewHolder holder, View v, int position) {
        if(listener != null){
            longClick_listener.onItemLongClick(holder,v,position);
        }
    }

    public void setLongClick_listener(ItemLongClickListener longClick_listener) {
        this.longClick_listener = longClick_listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ToggleButton button;
        TextView textView;

        public ViewHolder(View view, final ItemClickListener listener,
                          final ItemLongClickListener longClickListener)
        {
            super(view);

            Log.i("ViewHolder", "ViewHolder!!");
            button = view.findViewById(R.id.img_mapcousre);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                button.setClipToOutline(true);
            }
            textView = view.findViewById(R.id.tv_mapcourse);

            button.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if(longClickListener != null)
                    longClickListener.onItemLongClick(ViewHolder.this,v,position);

                return true;
            });

            button.setOnClickListener(v -> {
                Log.i("setOnClickListener", "setOnClickListener");
                int position = getAdapterPosition();
                if (listener != null)
                    listener.onItemClick(ViewHolder.this,v,position);
                else Log.i("listener", "listener is null");
            });
        }
        public void setItem(MapData item){ textView.setText(item.getCourse_name()); }

        public ToggleButton getButton() {
            return button;
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public void addItem(MapData item){ dataArrayList.add(item); }
    public void setItems(ArrayList<MapData> items){ this.dataArrayList = items; }
    public MapData getData(int position){ return dataArrayList.get(position); }
    public void setItem(int position, MapData item){ dataArrayList.set(position,item); }

    public ArrayList<MapData> getDataArrayList() {
        return dataArrayList;
    }
    public void setFirstButton(int position){
        // 위치를 가져왔다
        // 위치에 맞는 버튼을 가져와야지
    }

    public void setCurrent_filter_name(String current_filter_name) {
        this.current_filter_name = current_filter_name;
    }
    public String getChoseUnit(int position){
        if (current_filter_name.equals("kcal")) {
            data = dataArrayList.get(position);
            return data.getKcal() + "\nkcal";
        }
        else{
            data = dataArrayList.get(position);
            String distance = String.valueOf(Math.round(
                    Double.valueOf(data.getCourseDist()) / 1000 * 100) / 100.0);

            return distance + "\nkm";
        }
    }
    public void button(){

    }
}