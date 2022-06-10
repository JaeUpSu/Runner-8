package com.example.Runner8.ui.F_H.calorieDictionary.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.F_H.calorieDictionary.Adapter.Model.SearchModel;
import com.example.Runner8.ui.F_H.calorieDictionary.CalorieDictionaryFragment;
import com.example.Runner8.ui.F_H.calorieDictionary.OnSearchItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchAdapter2 extends RecyclerView.Adapter<SearchAdapter2.ViewHolder>
        implements OnSearchItemClickListener {

    SearchModel searchModel;
    ArrayList<SearchModel> searchModels;
    private NavController navController;

    Activity activity;
    CalorieDictionaryFragment fragment;

    OnSearchItemClickListener listener;

    public SearchAdapter2(Activity activity, ArrayList<SearchModel> searchModels){
        this.searchModels = searchModels;
        this.activity = activity;

        navController = Navigation.findNavController(activity, R.id.nav_food_search_fragment);
    }

    @NonNull
    @NotNull
    @Override
    public SearchAdapter2.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_listview,parent,false);
        return new SearchAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchAdapter2.ViewHolder holder, int position) {
        searchModel = searchModels.get(position);

        holder.label.setText(searchModel.getDESC_KOR());


    }

    @Override
    public int getItemCount() {
        return searchModels.size();
    }

    public void setListener(OnSearchItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSearchItemClick(SearchAdapter2.ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onSearchItemClick(holder, view, position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView label;

        public ViewHolder(@NonNull @NotNull View view) {
            super(view);

            label = view.findViewById(R.id.label);

            view.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    if (listener != null) {
                        listener.onSearchItemClick(ViewHolder.this, v, pos);
                    }
                }
            });
        }
    }
}
