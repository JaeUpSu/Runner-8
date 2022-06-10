package com.example.Runner8.ui.F_H.Cart.NewCart;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
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

import java.util.ArrayList;

public class FoodCartAdapter extends RecyclerView.Adapter<FoodCartAdapter.ViewHolder>
        implements CartItemClickListener {

    FoodData data;
    private ArrayList<FoodData> dataArrayList;
    CartItemClickListener listener;
    int size, count;
    int[] img;

    public FoodCartAdapter(ArrayList<FoodData> dataArrayList) {
        this.dataArrayList = dataArrayList;
        this.img = new int[]{R.drawable.cook_hat, R.drawable.dinner, R.drawable.restaurant, R.drawable.chef};
        this.size = img.length;
        this.count = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todayfoodlist_item, parent, false);
        return new ViewHolder(view,listener);
    }
    public void setOnItemClickListener(CartItemClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onCartItemClick(ViewHolder holder, View view, int position){
        if(listener != null){
            listener.onCartItemClick(holder,view,position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        data = dataArrayList.get(position);
        java.lang.String name = data.getName();
        Double kcal = data.getKcal();
        // Integer src = data.getImg_src();
        Integer num = data.getNum();
        Boolean checked = data.getChecked();

        Log.i("cart", name + kcal);

        if(size != count) holder.img.setImageResource(img[count++]);
        holder.txt_name.setText(name);
        holder.txt_num.setText(num + "");
        holder.txt_kcal.setText(kcal * num + "\nKcal");

        getItemViewType(position);

        if (checked)
            holder.layout.setBackgroundColor(Color.WHITE);
        else
            holder.layout.setBackgroundColor(Color.LTGRAY);

    }
    @Override
    public int getItemCount()
    {
        return dataArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txt_num, txt_kcal, txt_name;
        LinearLayout layout;
        @SuppressLint("ResourceAsColor")
        public ViewHolder(View view, final CartItemClickListener listener)
        {
            super(view);
            layout = view.findViewById(R.id.item_CartLayout);
            txt_name = view.findViewById(R.id.txt_foodName);
            txt_num = view.findViewById(R.id.txt_foodNum);
            txt_kcal = view.findViewById(R.id.txt_foodKcal);
            img = view.findViewById(R.id.img_food);

            img.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null)
                    listener.onCartItemClick(ViewHolder.this,v,position);
            });
        }

        public LinearLayout getLayout() { return layout; }
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public FoodData getData(int position){ return dataArrayList.get(position); }
}