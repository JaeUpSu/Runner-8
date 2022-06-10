package com.example.Runner8.ui.map.Guide;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.Runner8.R;
import com.example.Runner8.ui.map.SingleTon.MapSingleTon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class GuideSoundDialogue extends DialogFragment {


    TextView ok , cancel;
    Switch sound_all, sound_start, sound_arrive, sound_quarter, sound_distance;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.guidesound_dialog, null);

        sound_all = view.findViewById(R.id.sound_all);
        sound_start = view.findViewById(R.id.sound_start);
        sound_arrive = view.findViewById(R.id.sound_arrive);
        sound_quarter = view.findViewById(R.id.sound_quarter);
        sound_distance = view.findViewById(R.id.sound_distance);
        ok = view.findViewById(R.id.soundOk);
        cancel = view.findViewById(R.id.soundCancel);

        sound_all.setChecked(MapSingleTon.getInstance().isAll_check());
        sound_start.setChecked(MapSingleTon.getInstance().isStart_check());
        sound_arrive.setChecked(MapSingleTon.getInstance().isArrive_check());
        sound_quarter.setChecked(MapSingleTon.getInstance().isQuarter_check());
        sound_distance.setChecked(MapSingleTon.getInstance().isDistance_check());

        sound_all.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sound_start.setChecked(true);
                sound_arrive.setChecked(true);
                sound_quarter.setChecked(true);
                sound_distance.setChecked(true);
            } else {
                sound_start.setChecked(false);
                sound_arrive.setChecked(false);
                sound_quarter.setChecked(false);
                sound_distance.setChecked(false);
            }
        });

        ok.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();

            map.put("all", sound_all.isChecked());
            map.put("start", sound_start.isChecked());
            map.put("arrive", sound_arrive.isChecked());
            map.put("quarter", sound_quarter.isChecked());
            map.put("distance", sound_distance.isChecked());

            MapSingleTon.getInstance().setArrive_check(sound_arrive.isChecked());
            MapSingleTon.getInstance().setStart_check(sound_start.isChecked());
            MapSingleTon.getInstance().setQuarter_check(sound_quarter.isChecked());
            MapSingleTon.getInstance().setDistance_check(sound_distance.isChecked());
            MapSingleTon.getInstance().setAll_check(sound_all.isChecked());

            db.collection("Users").document(user.getUid())
                    .collection("Map").document("Sound").set(map);

            dismiss();
        });
        cancel.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }
}
