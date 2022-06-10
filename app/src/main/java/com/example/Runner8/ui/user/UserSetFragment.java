package com.example.Runner8.ui.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.Runner8.R;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserSetFragment extends DialogFragment  {
    private EditText goal_w, height, weight;
    private RadioButton option1,option2,option3,option4;
    private UserSetListener listener;

    private static final String ARGUMENT_TITLE = "My Imformation";

    private String title;

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public Dialog onCreateDialog(@NonNull Bundle saveInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog, null);

        goal_w = view.findViewById(R.id.goal_w);
        height = view.findViewById(R.id.height);
        weight = view.findViewById(R.id.weight);

        // 숫자키패드
        goal_w.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        height.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        weight.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        //
        builder.setView(view)
                .setTitle("My Imformation")
                .setNegativeButton("취  소", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setPositiveButton("저  장", (dialog, which) -> {

                    if(goal_w.getText().toString().length() == 0)
                        Toast.makeText(getContext(),"목표 몸무게 입력하세요!!",Toast.LENGTH_SHORT).show();
                    else if(height.getText().toString().length() == 0)
                        Toast.makeText(getContext(),"키 입력하세요!!",Toast.LENGTH_SHORT).show();
                    else if(weight.getText().toString().length() == 0)
                        Toast.makeText(getContext(),"몸무게 입력하세요!!",Toast.LENGTH_SHORT).show();
                    else {

                        Double goal_weight = Double.valueOf(Math.round(Double.parseDouble(goal_w.getText().toString()) * 10) / 10.0);
                        Double height_ = Math.round(Double.parseDouble(height.getText().toString()) * 10) / 10.0;
                        Double kg = Double.valueOf(Math.round(Double.parseDouble(weight.getText().toString()) * 10) / 10.0);
                        Double mean_kcal = getAppAmount(Sub_bundle.getInstance().getSex(),
                                Integer.valueOf(Sub_bundle.getInstance().getAge()), kg, height_);
                        Integer mean = Integer.parseInt(String.valueOf(Math.round(mean_kcal)));

                        Map<String, Object> map = new HashMap<>();
                        map.put("g_w", goal_weight);
                        map.put("mean", mean);
                        map.put("kg", kg);
                        map.put("height", height_);

                        db.collection("Users").document(user.getUid())
                                .collection("Profile").document("diet_profile")
                                .update(map);

                        Sub_bundle.getInstance().setHeight(String.valueOf(height_));
                        Sub_bundle.getInstance().setKg(String.valueOf(kg));
                        Sub_bundle.getInstance().setGoal_weight(String.valueOf(goal_weight));
                        Sub_bundle.getInstance().setApp_amount(String.valueOf(mean));

                        //ViewGroup container = null;
                        UserFragment userFragment = new UserFragment();
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment, userFragment).commit();
                    }
                });

        return builder.create();

    }

    public interface UserSetListener{
        void applyTexts(Integer mean_kcal, Double kg, Double height, String act , Double goal);
    }

    public double getAppAmount(String sex, int age, double kg, double height){

        double result = 0;

        if(sex.equals("man")){
            result = 66.47 + (13.75 * kg) + (5 * height) - (6.76 * age);
        }
        else{
            result = 655.1 + (9.56 * kg) + (1.85 * height) - (4.68 * age);
        }

        return result;
    }
}

