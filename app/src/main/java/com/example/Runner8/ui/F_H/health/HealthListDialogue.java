package com.example.Runner8.ui.F_H.health;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.F_H.health.Adapter.HealthDialogAdapter;
import com.example.Runner8.ui.F_H.health.Adapter.Model.HealthData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HealthListDialogue extends DialogFragment {

    RecyclerView healthList;
    ArrayList<HealthData> healthArrList;
    HealthData healthData;
    HealthDialogAdapter healthDialogAdapter;
    TextView btn_healthListSave, txtok;


    // Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // Reference

    CollectionReference cr_health_today = db.collection("Users").document(user.getUid())
            .collection("Health").document("today").collection("list");


    public HealthListDialogue(ArrayList<HealthData> healthDataList){
        this.healthArrList = healthDataList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_health, null);

        healthList = view.findViewById(R.id.list_todayhealth);
        txtok = view.findViewById(R.id.btn_healthListOk);
        btn_healthListSave = view.findViewById(R.id.btn_healthListSave);

        txtok.setOnClickListener(view1 -> dismiss());
        btn_healthListSave.setOnClickListener(v -> {

        });

        healthDialogAdapter = new HealthDialogAdapter(healthArrList);

        healthList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        healthList.setAdapter(healthDialogAdapter);


        // 샘플 데이터
        // sampleDataArr();
        builder.setView(view);

        return builder.create();
    }

    public RecyclerView getHealthList() {
        return healthList;
    }

    public void sampleDataArr(){
        Integer[] src_arr = new Integer[]{
                R.drawable.swimmer, R.drawable.hike, R.drawable.bicycle
        };
        String[] name_arr = new String[]{
          "수 영", "등 산", "사이클"
        };
        Double[] kcal_arr = new Double[]{
          201.4,303.2,90.1
        };
        Integer[] time_arr = new Integer[]{
          104, 203, 40
        };

        for (int i =0; i<3; i++){
            HealthData data = new HealthData();
            data.setImg_src(src_arr[i]);
            data.setKcal(kcal_arr[i]);
            data.setName(name_arr[i]);
            data.setTime(time_arr[i]);

            healthArrList.add(data);
        }
    }
}
