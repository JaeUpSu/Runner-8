package com.example.Runner8.ui.map.Guide;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.Runner8.R;


public class GuideDialogue extends DialogFragment {


    TextView tv_guideApply, tv_guideCancel, tv_guideClear;
    EditText edit_me,edit_opp,editmeOpp1,editmeOpp2,editoppMe1,editoppMe2;
    ToggleButton tgbtn_sound;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.guide_fragment, null);

        edit_me = view.findViewById(R.id.edit_myLocation);
        edit_opp = view.findViewById(R.id.edit_oppLocation);
        editoppMe2 = view.findViewById(R.id.edit_myPrecedeAfter);
        editoppMe1 = view.findViewById(R.id.edit_myPrecedeBefore);
        editmeOpp1 = view.findViewById(R.id.edit_oppPrecedeBefore);
        editmeOpp2 = view.findViewById(R.id.edit_oppPrecedeAfter);
        tv_guideApply = view.findViewById(R.id.tvBtn_guideApply);
        tv_guideCancel = view.findViewById(R.id.tvBtn_guideCancel);
        tv_guideClear = view.findViewById(R.id.tvBtn_guideClear);

        tv_guideApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //tv_guideCancel, tv_guideClear

        builder.setView(view);
        return builder.create();
    }

}
