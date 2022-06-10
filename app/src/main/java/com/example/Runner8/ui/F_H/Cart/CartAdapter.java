package com.example.Runner8.ui.F_H.Cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Runner8.R;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {

    ArrayList<CartItem> cartItems;
    Context mContext;

    TextView tvFoodName, tvFoodKcal;
    ImageView plus, minus;
    EditText item_count;

    public CartAdapter(ArrayList<CartItem> cartItems, Context context){
        this.cartItems = cartItems;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View itemView = inflater.inflate(R.layout.item_cart,parent,false);


        tvFoodName = itemView.findViewById(R.id.cart_item_name);
        tvFoodKcal = itemView.findViewById(R.id.cart_item_kcal);
        item_count = itemView.findViewById(R.id.cart_item_count);

        // Text Initial
        tvFoodName.setText(cartItems.get(position).foodName);
        tvFoodKcal.setText(cartItems.get(position).foodKcal);
        item_count.setText(cartItems.get(position).foodCount);

        plus = itemView.findViewById(R.id.cart_item_plus);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(cartItems.get(position).foodCount);
                String PlusCount = Integer.toString(++count);
                item_count.setText(PlusCount);
                cartItems.get(position).setFoodCount(PlusCount);
            }
        });

        return itemView;
    }


}
