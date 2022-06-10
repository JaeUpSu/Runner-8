package com.example.Runner8.ui.GetUserData;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Runner8.MainActivity;
import com.example.Runner8.R;
import com.example.Runner8.ui.Graph.Today_Date;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class UserDataFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //
    Today_Date today_date = new Today_Date();

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage storage;

    //
    private EditText nickName, user_tall, user_weight, user_weight_goal;
    private Button save;
    private Spinner spinner;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String pick_item = "";
    private String[] items = {"앉아서 주로 활동","규칙적인 활동",
            "조금 활발한 활동", "육체노동 등 높은 신체활동"};
    private String[] nameOfItems = {"낮음", "보통", "활발", "매우 활발"};
    private int point;

    public UserDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserDataFragment newInstance(String param1, String param2) {
        UserDataFragment fragment = new UserDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nickName = view.findViewById(R.id.nickName);
        user_tall = view.findViewById(R.id.user_tall);
        user_weight = view.findViewById(R.id.user_weight);
        user_weight_goal = view.findViewById(R.id.user_weight_goal);
        save = view.findViewById(R.id.save);
        spinner = view.findViewById(R.id.user_activity_spinner);

        storage = FirebaseStorage.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity().getBaseContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pick_item = items[position];
                point = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                point = 0;
                pick_item = items[0];
            }
        });

        save.setOnClickListener(v -> {

            StorageReference storageReference = storage.getReferenceFromUrl("gs://menu-96dd8.appspot.com");
            StorageReference pathReference;

            pathReference = storageReference.child("profile.png");

            pathReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String get_nickName = nickName.getText().toString();
                int get_user_tall = Integer.valueOf(user_tall.getText().toString());
                int get_user_weight = Integer.valueOf(user_weight.getText().toString());
                int get_user_weight_goal = Integer.valueOf(user_weight_goal.getText().toString());

                Double mean_kcal = (get_user_tall - 100) * 0.9
                        * 30;

                Map<String, Object> user_data = new HashMap<>();
                Map<String, Object> first_date = new HashMap<>();

                user_data.put("nickName", get_nickName);
                user_data.put("height", get_user_tall);
                user_data.put("kg", get_user_weight);
                user_data.put("g_w", get_user_weight_goal);
                user_data.put("act", nameOfItems[point]);
                user_data.put("mean", mean_kcal);

                Log.i("Uri", uri.toString());
                user_data.put("user_img", uri.toString());
                user_data.put("data_check", true);

                db.collection("Users").document(user.getUid())
                        .collection("Profile").document("diet_profile")
                        .update(user_data);

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

                Toast.makeText(getContext(),"환영합니다!!", Toast.LENGTH_LONG).show();
            });
        });
    }
}