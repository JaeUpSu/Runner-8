package com.example.Runner8.TRASH.calendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Runner8.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private selectDate cDate;
    private String got_title, got_contents;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_detail);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // 유저 정보
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Get Intent Data
        Intent intent = getIntent();
        got_title = intent.getStringExtra("title");
        got_contents = intent.getStringExtra("contents");
        cDate = (selectDate) intent.getSerializableExtra("ClickDate");

        // Get Layout Data
        final EditText title = findViewById(R.id.detail_et_title);
        final EditText contents = findViewById(R.id.detail_et_contents);
        ImageButton save_btn = findViewById(R.id.detail_ib_save);
        ImageButton back_btn = findViewById(R.id.detail_ib_back);

        // 선택한 메모내용 출력
        title.setText(got_title);
        contents.setText(got_contents);


        // 저장 누를 시
        save_btn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this)
                    .setTitle("Notification")
                    .setMessage("The note has been saved well :)")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String final_title = title.getText().toString();
                            String final_contents = contents.getText().toString();

                            // 수정한 입력 값
                            Map<String,Object> map = new HashMap<>();
                            map.put("title",final_title);
                            map.put("content", final_contents);

                            // 선택했던 입력 값
                            Map<String, Object> oriMemo = new HashMap<>();
                            oriMemo.put("title",got_title);
                            oriMemo.put("content", got_contents);

                            // Document 참조 객체
                            DocumentReference docRef = db.collection("Users").document(user.getUid())
                                    .collection("Calendar").document(cDate.getDate());

                            // 선택 리스트 삭제
                            docRef.update("list",FieldValue.arrayRemove(oriMemo));

                            // 새로운 리스트 추가
                            docRef.update("list",FieldValue.arrayUnion(map));

                            // 이전 액티비티로 이동
                            Intent eIntent = new Intent(EditActivity.this, MemoActivity.class);
                            eIntent.putExtra("ClickDate", cDate);
                            startActivity(eIntent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog msgDlg = builder.create();
            msgDlg.show();
        });
        back_btn.setOnClickListener(v -> {
            // 메인으로 다시
            Intent intentE = new Intent(EditActivity.this, MemoActivity.class);
            intentE.putExtra("ClickDate", cDate);
            startActivity(intentE);
            finish();
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
