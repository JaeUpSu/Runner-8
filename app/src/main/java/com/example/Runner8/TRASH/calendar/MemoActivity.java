package com.example.Runner8.TRASH.calendar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Runner8.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    ListView listView;
    TextView memoCount, textDay;
    ImageButton backBtn, newBtn;
    MemoAdapter memoAdapter = new MemoAdapter();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Memo> memoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        // Get Layout's id
        backBtn = findViewById(R.id.main_btn_back);
        newBtn = findViewById(R.id.main_btn_add_text);
        memoCount = findViewById(R.id.main_tv_count);
        textDay = findViewById(R.id.day);
        listView = findViewById(R.id.main_lv_memo_list);

        // (Firebase) 사용자 정보 얻기 위한 객체
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Intent Data
        selectDate cDate = (selectDate) getIntent().getSerializableExtra("ClickDate");       // Date = Year+Month+Day

        // 메모장에 날짜 확인 텍스트
        textDay.setText(cDate.getYear() + "년 " + cDate.getMonth() + "월 " + cDate.getDay() + "일");

        // 유저의 해당 날짜 메모데이터 가져오기
        db.collection("Users").document(user.getUid())
                .collection("Calendar").document(cDate.getDate())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        // Document 정보 가져오기
                        DocumentSnapshot document = task.getResult();

                        // document 정보가 존재하면 아래 실행
                        if(document.exists()){
                            List list = (List) document.getData().get("list");
                            if(list.size() != 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    HashMap map = (HashMap) list.get(i);
                                    Memo m = new Memo(map.get("title").toString(), map.get("content").toString());
                                    memoArrayList.add(m);
                                }
                            }
                            // 리스트 뷰 연결
                            memoAdapter = new MemoAdapter(memoArrayList, getApplicationContext());
                            memoAdapter.notifyDataSetChanged();
                            listView.setAdapter(memoAdapter);
                            // 메모 수 표시
                            memoCount.setText("메모 수   :  " + Integer.toString(memoAdapter.getCount()));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        // Edit Memo
        listView.setOnItemClickListener((parent, view, position, id) -> {

            Memo vo =(Memo)parent.getAdapter().getItem(position);  //리스트 뷰의 포지션 가져옴.
            String t_title = vo.getTitle();
            String c_contents =vo.getContents();

            Intent intent = new Intent(MemoActivity.this, EditActivity.class);
            intent.putExtra("title",t_title);
            intent.putExtra("contents",c_contents);
            intent.putExtra("ClickDate", cDate);

            startActivity(intent);
            finish();
        });

        // Delete Memo (LongClick)
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Log.i("position", Integer.toString(position));
            Memo vo =(Memo)parent.getAdapter().getItem(position);  //리스트 뷰의 포지션 가져옴.
            final String t_title = vo.getTitle();
            String t_content = vo.getContents();

            AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);

            builder.setPositiveButton("Yes", (dialog, which) -> {

                Map<String, Object> map = new HashMap<>();

                map.put("title", t_title);
                map.put("content", t_content);

                db.collection("Users").document(user.getUid())
                        .collection("Calendar").document(cDate.getDate())
                        .update("list", FieldValue.arrayRemove(map));

                Intent intent = new Intent(MemoActivity.this, MemoActivity.class);
                intent.putExtra("ClickDate", cDate);
                intent.putExtra("UserUid", user.getUid());
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            builder.setMessage("Do you want to delete this memo?");
            builder.setTitle("Delete Notification");
            builder.show();
            return true;
        });

        // Back
        backBtn.setOnClickListener(v -> {
            finish();
        });

        // Detail Activity (Add Memo)
        newBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MemoActivity.this, DetailActivity.class);
            intent.putExtra("ClickDate", cDate);
            intent.putExtra("UserUid", user.getUid());
            startActivity(intent);
            finish();
        });
    }
}
