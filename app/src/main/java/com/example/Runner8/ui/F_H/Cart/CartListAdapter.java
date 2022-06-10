package com.example.Runner8.ui.F_H.Cart;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private ArrayList<CartItem> cartItems = null;

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView item_foodName, item_foodKcal, totalKcal;
        EditText item_foodCount;
        ImageButton item_plus, item_minus;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            item_foodName = itemView.findViewById(R.id.cart_item_name);
            item_foodKcal = itemView.findViewById(R.id.cart_item_kcal);
            item_foodCount = itemView.findViewById(R.id.cart_item_count);
            item_plus = itemView.findViewById(R.id.cart_item_plus);
            item_minus = itemView.findViewById(R.id.cart_item_minus);

            itemView.setOnClickListener(v -> v.setBackgroundColor(Color.GRAY));
        }
    }

    public CartListAdapter(ArrayList<CartItem> list){
        this.cartItems = list;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_cart, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        // Text
        holder.item_foodName.setText(cartItems.get(position).foodName);

        double init_kcal = Double.parseDouble(cartItems.get(position).foodKcal)
                * Integer.parseInt(cartItems.get(position).foodCount);
        holder.item_foodKcal.setText(String.valueOf(Math.round(init_kcal)));
        holder.item_foodCount.setText(cartItems.get(position).foodCount);

        holder.item_plus.setTag(position);

        holder.item_plus.setOnClickListener(v -> {
            // int btnPosition = (int) v.getTag();
            int count = Integer.parseInt(String.valueOf(holder.item_foodCount.getText()));
            double totalKcal = Double.valueOf(String.valueOf(holder.item_foodKcal.getText()));
            double f_kcal = Math.round(Double.parseDouble(cartItems.get(position).getFoodKcal()));
            String newCount = Integer.toString(++count);

            holder.item_foodCount.setText(newCount);
            cartItems.get(position).setFoodCount(newCount);
            holder.item_foodKcal.setText(Double.toString(Math.round(totalKcal + f_kcal)));
        });

        holder.item_minus.setTag(position);
        holder.item_minus.setOnClickListener(v -> {
            // int btnPosition = (int) v.getTag();
            int count = Integer.parseInt(String.valueOf(holder.item_foodCount.getText()));
            double totalKcal = Double.valueOf(String.valueOf(holder.item_foodKcal.getText()));

            if(count == 1) return;
            else {
                String newCount = Integer.toString(--count);
                double f_kcal = Math.round(Double.parseDouble(cartItems.get(position).getFoodKcal()));

                holder.item_foodCount.setText(newCount);
                cartItems.get(position).setFoodCount(newCount);
                holder.item_foodKcal.setText(Double.toString(Math.round(totalKcal - f_kcal)));
            }
        });

    }
    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
