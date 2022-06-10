package com.example.Runner8.TRASH;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.Runner8.R;
import com.example.Runner8.ui.map.Adapter.Model.MapData;

public class MarkerDialogue extends DialogFragment {

    TextView tv_markerName, tv_markerAddress, tv_markerSubway, tv_markerBus, tv_markerInform1,
            tv_markerInform2;
    ToggleButton btn_likeMarker;
    MapData markerData = null;
    boolean isLike = false;  // DB에서 받아옴

    public MarkerDialogue(MapData mapData){
        markerData = mapData;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogue_markerpick, null);

        tv_markerName = view.findViewById(R.id.tv_Markername);
        tv_markerAddress = view.findViewById(R.id.tv_Markeraddress);
        tv_markerSubway = view.findViewById(R.id.tv_Markersubway);
        tv_markerBus = view.findViewById(R.id.tv_Markerbus);
        tv_markerInform1 = view.findViewById(R.id.tv_Markerinform1);
        tv_markerInform2 = view.findViewById(R.id.tv_Markerinform2);
        btn_likeMarker = view.findViewById(R.id.btn_likeMarker);

        setTV();

        btn_stateChange(isLike);
        btn_likeMarker.setOnClickListener(v -> {
            isLike = !isLike;
            btn_stateChange(isLike);
        });

        builder.setView(view).setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
        .setPositiveButton("Go", (dialog, which) -> {
            dialog.dismiss();
            // 해당 지역 확대 코스표시 or 안내 Map ?ㅇ?
        });

        return builder.create();
    }
    public void setTV(){
        tv_markerName.setText(markerData.getCourse_name());
        tv_markerAddress.setText(markerData.getAddress());
    }
    public void btn_stateChange(boolean like){
        if(like == true) btn_likeMarker.setChecked(true);
        else btn_likeMarker.setChecked(false);
    }
}
