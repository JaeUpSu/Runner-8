package com.example.Runner8.ui.map.Guide;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Runner8.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GuideFragment extends Fragment {


    TextView tv_guideApply, tv_guideCancel, tv_guideClear;
    EditText edit_me,edit_opp,editmeOpp1,editmeOpp2,editoppMe1,editoppMe2;
   // ToggleButton tgbtn_sound;
    FloatingActionButton tgbtn_sound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guide_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        edit_me = view.findViewById(R.id.edit_myLocation);
        edit_opp = view.findViewById(R.id.edit_oppLocation);
        editoppMe2 = view.findViewById(R.id.edit_myPrecedeAfter);
        editoppMe1 = view.findViewById(R.id.edit_myPrecedeBefore);
        editmeOpp1 = view.findViewById(R.id.edit_oppPrecedeBefore);
        editmeOpp2 = view.findViewById(R.id.edit_oppPrecedeAfter);
        tv_guideApply = view.findViewById(R.id.tvBtn_guideApply);
        tv_guideCancel = view.findViewById(R.id.tvBtn_guideCancel);
        tv_guideClear = view.findViewById(R.id.tvBtn_guideClear);
        tgbtn_sound = view.findViewById(R.id.tbtn_sound);

        GuideSoundDialogue dialogue = new GuideSoundDialogue();


        tgbtn_sound.setOnClickListener(v -> dialogue.show(getParentFragmentManager(),"Dialog"));

        tv_guideApply.setOnClickListener(v -> {
            // + 저장코드
            Intent intent = new Intent(getActivity(), GuideActivity.class);
            startActivity(intent);
        });

        tv_guideCancel.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GuideActivity.class);
            startActivity(intent);
        });

        tv_guideClear.setOnClickListener(v -> clearStr());
    }

    public void clearStr(){
        edit_me.setText("도착지점으로 부터 {내위치} 남았습니다!");
        edit_opp.setText("상대가 도착지점으로 부터 {상대위치} 남았습니다!");
        editmeOpp1.setText("상대가 바로 뒤에 있습니다!");
        editmeOpp2.setText("상대가 나를 추월했습니다!");
        editoppMe1.setText("상대가 바로 앞에 있습니다!");
        editoppMe2.setText("상대를 추월했습니다!");
    }

    public String getEdit_me() {
        return "도착지점으로 부터 {내위치} 남았습니다!";
    }

    public String getEdit_opp() {
        return "상대가 도착지점으로 부터 {상대위치} 남았습니다!";
    }

    public String getEditmeOpp1() {
        return "상대가 바로 뒤에 있습니다!";
    }

    public String getEditmeOpp2() { return "상대가 나를 추월했습니다!"; }

    public String getEditoppMe1() {
        return "상대가 바로 앞에 있습니다!";
    }

    public String getEditoppMe2() {
        return "상대를 추월했습니다!";
    }
}