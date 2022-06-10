package com.example.Runner8.ui.map.UFSRecord;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.map.Courses;
import com.example.Runner8.ui.map.UFSRecord.Adapter.Model.TopRecord;
import com.example.Runner8.ui.map.UFSRecord.Adapter.RecordAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MapUFSRecordActivity extends AppCompatActivity {

    TextView my_record_time;
    TextView my_record_avg_time;
    TextView my_record_count;
    TextView tv_check_data;
    TextView course_dist;
    TextView course_kcal;
    TextView course_normalrecord;

    Toolbar toolbar;

    RecyclerView recyclerView;
    RecordAdapter recordAdapter;
    TopRecord topRecord;
    ArrayList<TopRecord> topRecords = new ArrayList<>();

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DocumentReference dr_myRecord = db.collection("Users").document(user.getUid())
            .collection("Map").document("RAN")
            .collection("Courses").document(Courses.getInstance().getPick_course_index());

    //
    CollectionReference cr_totally_mapCourses = db.collection("Map").document("User_Courses")
            .collection("Courses");

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_ufs_record);

        my_record_time = findViewById(R.id.my_record_time);
        my_record_avg_time = findViewById(R.id.my_record_avg_time);
        my_record_count = findViewById(R.id.my_record_count);
        tv_check_data = findViewById(R.id.tv_check_data);
        recyclerView = findViewById(R.id.record_recyclerView);
        toolbar = findViewById(R.id.map_ufs_toolbar);
        course_dist = findViewById(R.id.course_dist);
        course_kcal = findViewById(R.id.course_kcal);
        course_normalrecord = findViewById(R.id.course_normalrecord);

        // toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        InitialCourseData();

        recordAdapter = new RecordAdapter(topRecords);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(recordAdapter);

        InitialTop20();
    }

    public void InitialCourseData() {

        dr_myRecord.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                Log.i("check!!", "check!!");
                String time = document.get("time").toString();
                String avg_time = document.get("avg_time").toString();
                String record_count = document.get("finish_count").toString();

                my_record_count.setText("총 달린 횟수   " + record_count + " 번");
                my_record_avg_time.setText("보통   " +avg_time + " 초");
                my_record_time.setText("BEST   " + time + " 초");
            } else {

            }
        });

        cr_totally_mapCourses.document(Courses.getInstance().getPick_course_index())
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot document = task.getResult();

                    double distance = Double.valueOf(document.get("total_distance").toString());
                    double kcal = Double.valueOf(document.get("kcal").toString());
                    double avg_time = Double.valueOf(document.get("total_avg_time").toString());

                    distance = Math.round(distance * 100) / 100;
                    kcal = Math.round(kcal * 100) / 100;

                    course_dist.setText("Distance    " + distance + " m");
                    course_kcal.setText("Kcal    " + kcal + " Kcal");
                    course_normalrecord.setText("보통 기록    " + (int) Math.round(avg_time) + " 초");

                });

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
