package com.example.Runner8.ui.community.Adapter.Search;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.community.Adapter.Search.Model.RecentlyWord;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecentlyWordAdapter extends RecyclerView.Adapter<RecentlyWordAdapter.ViewHolder>{
    
    ArrayList<RecentlyWord> word_list;

    public RecentlyWordAdapter(ArrayList<RecentlyWord> word_list){
        this.word_list = word_list;
    }
    
    @NonNull
    @NotNull
    @Override
    public RecentlyWordAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recently_word_list,parent,false);
        return new RecentlyWordAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecentlyWordAdapter.ViewHolder holder, int position) {
        RecentlyWord recentlyWord = word_list.get(position);

        Log.i("tv_word", recentlyWord.getWord());

        holder.tv_word.setText(recentlyWord.getWord());
    }

    @Override
    public int getItemCount() {
        return this.word_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        
        TextView tv_word;

        public ViewHolder(@NonNull @NotNull View view) {
            super(view);
            tv_word = view.findViewById(R.id.tv_recently_word);
            
        }
    }
}
