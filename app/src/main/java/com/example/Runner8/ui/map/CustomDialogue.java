package com.example.Runner8.ui.map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.Runner8.R;
import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;

public class CustomDialogue extends DialogFragment {

    private static final String MY_INFO = "COURSE";

    TextView tv_courseRegister, tv_courseCancel, tvBtn_resume;
    EditText edit_courseName;
    CheckBox anony_CheckBox;


    CustomDialogueListener customDialogueListener;
    String getMyInfo;
    String name;
    String realname;
    Boolean onlyMeChecked;
    Boolean okCancel;
    ArrayList<LatLng> geoList;

    public CustomDialogue(ArrayList<LatLng> geoList, String name){
        this.geoList = geoList; this.realname = name;
    }

    interface CustomDialogueListener{
        void onPositiveClicked(String name, ArrayList<LatLng> geoList);
        void onNegativeClicked();
        void onResumeClicked();
    }
    public void setDialogListener(CustomDialogueListener listener){
        this.customDialogueListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogue_courseregister, null);

        edit_courseName = view.findViewById(R.id.edit_courseName);
        tv_courseCancel= view.findViewById(R.id.tvBtn_courseCancel);
        tv_courseRegister = view.findViewById(R.id.tvBtn_courseRegist);
        tvBtn_resume = view.findViewById(R.id.tvBtn_resume);
        // anony_CheckBox = view.findViewById(R.id.anonymousBox);

        name = realname;

        /*
        anony_CheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) name = "익명의 길";
            else name = realname;
        });

         */

        tv_courseRegister.setOnClickListener(v -> {
            if(edit_courseName.getText().toString().length() >= 1)
                name = edit_courseName.getText().toString();
            customDialogueListener.onPositiveClicked(name,geoList);
            getDialog().dismiss();
        });
        tv_courseCancel.setOnClickListener(v -> {
            customDialogueListener.onNegativeClicked();
            getDialog().dismiss();
        });

        tvBtn_resume.setOnClickListener(v -> {
            customDialogueListener.onResumeClicked();
            getDialog().dismiss();
        });

        builder.setView(view);
        return builder.create();
    }

}
