package com.example.Runner8.ui.trash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.Runner8.R;


public class HomeDialog extends DialogFragment {

    private EditText editText;
    private NameInputListener listener;

    private EditText goal_w, height, weight;
    private CheckBox option1,option2,option3,option4;

    public String goal_weight ;
    public Double mean_kcal ;

    private  String act_num;

    private Fragment fragment;

    public static HomeDialog newInstance(NameInputListener listener){
        HomeDialog fragment = new HomeDialog();
        fragment.listener = listener;
        return fragment;
    }

    public interface NameInputListener{
        void onNameInputComplete(String name);
    }

    public HomeDialog(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_dialog, container, false);

        Bundle args = getArguments();
        String value = args.getString("key");

        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("home");

        return view;
    }

}
