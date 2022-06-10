package com.example.Runner8.TRASH.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Runner8.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);

        // Get Layout's Id
        final EditText title = findViewById(R.id.detail_et_title);
        final EditText contents = findViewById(R.id.detail_et_contents);
        ImageButton save_btn = findViewById(R.id.detail_ib_save);
        ImageButton back_btn = findViewById(R.id.detail_ib_back);

        // (Firebase) 사용자 정보 얻기 위한 객체
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // getIntent
        selectDate cDate = (selectDate) getIntent().getSerializableExtra("ClickDate");

        // save_btn 을 누르면 데이터 베이스에 들어간다.
        save_btn.setOnClickListener(v -> {

            // 텍스트 가져오기
            String titleStr = title.getText().toString();
            String contentsStr = contents.getText().toString();

            // Memo Data Mapping
            Map<String, Object> map = new HashMap<>();
            map.put("title", titleStr);
            map.put("content", contentsStr);

            DocumentReference docRef = db.collection("Users").document(user.getUid())
                    .collection("Calendar").document(cDate.getDate());

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    // document 없을 시 추가
                    if(!document.exists()){
                        Map<String,Object> data = new HashMap<>();
                        docRef.set(data);
                    }
                    // Memo Data 추가
                    docRef.update("list", FieldValue.arrayUnion(map));
                }
            });

            // 저장 된다면 toast 메세지 같은 거 띄우기
            Toast.makeText(getApplicationContext(),"save well.",Toast.LENGTH_SHORT).show();

        });
        // cancel_btn 을 누르면 이 액티비티 끝내고 main 화면 돌아간다.
        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, MemoActivity.class);
            intent.putExtra("ClickDate", cDate);
            startActivity(intent);
            finish();
        });
    }
}