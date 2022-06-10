package com.example.Runner8.TRASH;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.map.Courses;
import com.example.Runner8.ui.map.UFSRecord.Adapter.Model.TopRecord;
import com.example.Runner8.ui.map.UFSRecord.Adapter.RecordAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopRecordFragment extends Fragment {

    RecyclerView recyclerView;
    RecordAdapter recordAdapter;
    TopRecord topRecord;
    ArrayList<TopRecord> topRecords = new ArrayList<>();

    //
    TextView tv_check_data;
    TextView my_record_time;
    TextView my_record_avg_time;
    TextView my_record_count;

    //
    String course_index;

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //
    CollectionReference cr_totally_mapCourses = db.collection("Map").document("User_Courses")
            .collection("Courses");

    public TopRecordFragment() {
        // Required empty public constructor
    }

    public static TopRecordFragment newInstance(int num) {
        TopRecordFragment fragment = new TopRecordFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.record_recyclerView);
        tv_check_data = view.findViewById(R.id.tv_check_data);

        recordAdapter = new RecordAdapter(topRecords);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(recordAdapter);

        InitialTop20();

    }

    public void InitialTop20(){
        Log.i("Courses.getInstance()", Courses.getInstance().getPick_course_index());

        cr_totally_mapCourses.document(Courses.getInstance().getPick_course_index())
                .collection("TOP20")
                .orderBy("total_time")
                .get()
                .addOnCompleteListener(task -> {

                    int rank = 1;

                    if(task.getResult().size() == 0) {
                        // 데이터 없음 표시
                        tv_check_data.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                    else{
                        tv_check_data.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            topRecord = new TopRecord();
                            Log.i("date", document.get("date").toString());

                            topRecord.setDate(document.get("date").toString());
                            topRecord.setNickName(document.get("nickName").toString());
                            topRecord.setRanking(rank);
                            topRecord.setTime(document.get("total_time").toString());
                            topRecords.add(topRecord);
                            recordAdapter.notifyItemInserted(rank - 1);
                            rank++;
                        }
                    }
                });
    }
}