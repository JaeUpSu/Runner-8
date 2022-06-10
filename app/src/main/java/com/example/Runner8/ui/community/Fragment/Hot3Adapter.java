package com.example.Runner8.ui.community.Fragment;

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
import com.example.Runner8.ui.community.Adapter.Board.model.BoardModel;

import java.util.ArrayList;

public class Hot3Adapter extends RecyclerView.Adapter<Hot3Adapter.ViewHolder>
        implements Hot3ItemClickListener {

    BoardData data;
    private ArrayList<BoardData> dataArrayList;
    Hot3ItemClickListener listener;
    BoardModel boardModel;
    ArrayList<BoardModel> boardModels;

    public Hot3Adapter(ArrayList<BoardData> dataArrayList, ArrayList<BoardModel> boardModels) {
        this.dataArrayList = dataArrayList;
        this.boardModels = boardModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boarditem, parent, false);
        return new ViewHolder(view,this);
    }
    public void setOnItemClickListener(Hot3ItemClickListener listener){
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
        String name = data.getTitle();
        Integer src = data.getImg_src();
        int comment_count = data.getComment();
        int up_count = data.getUp();

        Log.i("adapter",src+"");

        getItemViewType(position);
        holder.img.setImageResource(src);
        holder.title.setText(name);

        holder.left.setColorFilter(Color.WHITE);
        holder.right.setColorFilter(Color.WHITE);

        holder.comment.setText("+ " + comment_count);
        holder.up.setText(String.valueOf(up_count));

        if (position == dataArrayList.size()-1){
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.INVISIBLE);
        }else if (position == 0){
            holder.left.setVisibility(View.INVISIBLE);
            holder.right.setVisibility(View.VISIBLE);
        }else{
            holder.left.setVisibility(View.VISIBLE);
            holder.right.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public int getItemCount()
    {
        return dataArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img, left, right;
        TextView title, up, comment;
        LinearLayout item;
        @SuppressLint("ResourceAsColor")
        public ViewHolder(View view, final Hot3ItemClickListener listener)
        {
            super(view);
            item = view.findViewById(R.id.quickitem);
            title = view.findViewById(R.id.quick_foodname);
            img = view.findViewById(R.id.quick_foodsrc);
            left = view.findViewById(R.id.leftBtn);
            right = view.findViewById(R.id.rightBtn);
            up = view.findViewById(R.id.quick_foodkcal);
            comment = view.findViewById(R.id.comment);

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
    public BoardData getData(int position){ return dataArrayList.get(position); }
}