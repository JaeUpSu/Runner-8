package com.example.Runner8.TRASH;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.Runner8.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRecordFragment extends Fragment {

    public MyRecordFragment() {
        // Required empty public constructor
    }

    public static MyRecordFragment newInstance(int num) {
        MyRecordFragment fragment = new MyRecordFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_record, container, false);
    }
}