package com.example.Runner8.ui.trash;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class Fragment_Food_2nd extends Fragment {

    public Fragment_Food_2nd(){}

    public static Fragment_Food_2nd newInstance(int num){
        Fragment_Food_2nd fragment = new Fragment_Food_2nd();
        Bundle args = new Bundle();
        args.putInt("num",num);
        fragment.setArguments(args);
        return fragment;
    }
}
