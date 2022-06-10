package com.example.Runner8.ui.trash;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Runner8.R;

public class FragmentFourth extends Fragment {
    // Store instance variables
    private int frag_num;
    TextView data_t;

    public FragmentFourth(){

    }

    // newInstance constructor for creating fragment with arguments
    public static FragmentFourth newInstance(int num){
        FragmentFourth fragment = new FragmentFourth();
        Bundle args = new Bundle();
        args.putInt("num",num);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables ba
    // sed on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        frag_num = getArguments().getInt("num",0);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_4p,container,false);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState){
        super.onViewCreated(view,savedInstancdState);
        data_t = view.findViewById(R.id.tvName4);
        data_t.setText("Page " + frag_num);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
