package com.example.Runner8.ui.community.Activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.Runner8.R;
import com.example.Runner8.SingleTon.ScreenChangeDetect;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.example.Runner8.ui.Graph.Today_Date;
import com.example.Runner8.ui.community.Adapter.Board.model.BoardModel;
import com.example.Runner8.ui.community.TimeValue;
import com.example.Runner8.ui.community.singleTon.TotalCounts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UploadBoardActivity extends AppCompatActivity {

    private final int GALLERY_CODE = 10;
    private final String ADMIN = "JpPFT0etXLaBlhEZCbsddu9ZKhK2";

    ImageView img_user;
    EditText et_title, et_content;
    TextView tv_ok, tv_cancel, tv_name;
    ImageButton btn_img;
    ToggleButton tbtn_notice;
    Toolbar toolbar;

    // Valuable
    String img_url = "";
    int user_endBoard_count;
    boolean tbtn_notice_check = false;

    //
    Today_Date today_date = new Today_Date();
    TimeValue timeValue;

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage storage;
    //

    CollectionReference cr_user_comm = db.collection("Users").document(user.getUid())
            .collection("Comm");

    DocumentReference dr_user = db.collection("Users").document(user.getUid());

    DocumentReference dr_total_comm = db.collection("Community").document("board");

    DocumentReference dr_notice_comm = db.collection("Community").document("noticeBoard");

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_board);

        img_user = findViewById(R.id.img_user);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        tv_ok = findViewById(R.id.tv_ok);
        tv_cancel = findViewById(R.id.tv_cancel);
        btn_img = findViewById(R.id.btn_img);
        tv_name = findViewById(R.id.tv_name);
        tbtn_notice = findViewById(R.id.tbtn_notice);
        toolbar = findViewById(R.id.upload_board_toolbar);

        // toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("게시물 업로드");

        storage = FirebaseStorage.getInstance();

        // 게시물 업로드
        tv_ok.setOnClickListener(v -> {

            today_date.setNow();

            String final_title = et_title.getText().toString();
            String final_et_content = et_content.getText().toString();

            // 공지 업로드 체크
            if(tbtn_notice_check){

                dr_notice_comm.get().addOnCompleteListener(task -> {
                    DocumentSnapshot document = task.getResult();
                    int final_index = Integer.valueOf(document.get("final_index").toString());
                    int count = Integer.valueOf(document.get("count").toString());
                    final_index += 1;
                    count += 1;

                    Map<String, Object> notice_board = new HashMap<>();
                    Map<String, Object> notice_comm_count = new HashMap<>();

                    notice_board.put("index", final_index);
                    notice_board.put("title", final_title);
                    notice_board.put("content", final_et_content);
                    notice_board.put("date", today_date.getFormat_date());
                    notice_board.put("time", today_date.getFormat_time());
                    notice_board.put("views", 0);

                    notice_comm_count.put("final_index", final_index);
                    notice_comm_count.put("count", count);
                    Sub_bundle.getInstance().setTotal_comm_index(String.valueOf(final_index));
                    TotalCounts.getInstance().setBoard_count(String.valueOf(count));

                    dr_notice_comm.update(notice_comm_count);

                    dr_notice_comm.collection("item").document(String.valueOf(final_index))
                            .set(notice_board);

                    finish();
                });
            }
            else {
                // total comm
                dr_total_comm.get().addOnCompleteListener(task2 -> {

                    DocumentSnapshot document = task2.getResult();
                    BoardModel boardModel = new BoardModel();

                    int board_final_index = Integer.valueOf(document.get("comm_count").toString());
                    int board_count = Integer.valueOf(document.get("board_count").toString());
                    Log.i("comm_count", String.valueOf(board_final_index));
                    board_final_index++;
                    board_count++;

                    timeValue = new TimeValue(today_date.getFormat_date(), today_date.getFormat_time());
                    String time_value = timeValue.getTimeValue();

                    Map<String, Object> comm_board = new HashMap<>();
                    Map<String, Object> total_comm_count = new HashMap<>();

                    comm_board.put("index", board_final_index);
                    comm_board.put("writer_index", user_endBoard_count);
                    comm_board.put("title", final_title);
                    comm_board.put("content", final_et_content);
                    comm_board.put("date", today_date.getFormat_date());
                    comm_board.put("time", today_date.getFormat_time());
                    comm_board.put("img", img_url);
                    comm_board.put("uid", user.getUid());
                    comm_board.put("up_count", 0);
                    comm_board.put("views", 0);
                    comm_board.put("comment_count", 0);
                    comm_board.put("comment_final_index", 0);

                    boardModel.setBoard_index(String.valueOf(board_final_index));
                    boardModel.setWriter_index(String.valueOf(user_endBoard_count));
                    boardModel.setTitle(final_title);
                    boardModel.setContent(final_et_content);
                    boardModel.setTimeValue(time_value);
                    boardModel.setImg(img_url);
                    boardModel.setUid(user.getUid());
                    boardModel.setUp_count("0");
                    boardModel.setViews("0");
                    boardModel.setComment_count("0");
                    boardModel.setComment_final_index("0");

                    Log.i("comm_count", String.valueOf(board_final_index));
                    total_comm_count.put("comm_count", board_final_index);
                    total_comm_count.put("board_count", board_count);
                    Sub_bundle.getInstance().setTotal_comm_index(String.valueOf(board_final_index));
                    TotalCounts.getInstance().setBoard_count(String.valueOf(board_count));

                    dr_total_comm.update(total_comm_count);

                    // 게시물 날짜
                    db.collection("Community").document("board")
                            .collection("item").document(String.valueOf(board_final_index))
                            .set(comm_board);

                    //
                    ScreenChangeDetect.getInstance().setBoardModel(boardModel);
                    ScreenChangeDetect.getInstance().setUploadBoardToComHome(true);

                    finish();
                });
            }
        });

        // 사진 업로드
        btn_img.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, GALLERY_CODE);
        });

        // 취소 버튼
        tv_cancel.setOnClickListener(v -> finish());

        //
        initialView();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void initialView(){
        boolean editCheck = getIntent().getBooleanExtra("edit", false);

        // edit board
        if(editCheck){
            // Total Comm
            String board_index = getIntent().getStringExtra("board_index");
            DocumentReference dr_writer_board = dr_total_comm.collection("item").document(board_index);

            dr_writer_board.get().addOnCompleteListener(task -> {
                DocumentSnapshot document = task.getResult();

                String title = document.get("title").toString();
                String content = document.get("content").toString();
                String img = document.get("img").toString();

                et_title.setText(title);
                et_content.setText(content);

                tv_ok.setText("수정");
                getSupportActionBar().setTitle("게시물 수정");

                // 이미지 추가

            });

            tv_ok.setOnClickListener(v -> {

                // total comm
                Map<String, Object> edit_board = new HashMap<>();

                String new_title = et_title.getText().toString();
                String new_content = et_content.getText().toString();
                // String new_img;

                edit_board.put("title", new_title);
                edit_board.put("content", new_content);

                dr_writer_board.update(edit_board);

                // user comm
                cr_user_comm.document().update(edit_board);

                finish();
            });
        }
        // set profile, nickName
        Glide.with(getApplicationContext())
                .load(Sub_bundle.getInstance().getPro_img_url())
                .into(img_user);

        tv_name.setText(Sub_bundle.getInstance().getNickName());

        // 둥글게
        GradientDrawable drawable = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = (GradientDrawable) getApplicationContext().getDrawable(R.drawable.shape);
        }

        img_user.setBackground(drawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            img_user.setClipToOutline(true);
        }

        if(user.getUid().equals(ADMIN)){

            tbtn_notice.setVisibility(View.VISIBLE);

            tbtn_notice.setOnClickListener(v -> {
                 if(tbtn_notice.isChecked()) {
                     Log.i("tbtn_notice", "check!!");

                     tbtn_notice_check = true;
                 }
                 else
                     tbtn_notice_check = false;
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE){
            Uri file = data.getData();
            StorageReference storageRef = storage.getReference();
            // child 안에는 이미지가 저장될 경로를 적는다.
            StorageReference riversRef = storageRef.child("board.png");
            UploadTask uploadTask = riversRef.putFile(file);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(UploadBoardActivity.this,
                        "사진이 정상적으로 업로드 되었습니다.", Toast.LENGTH_LONG).show();
            });

            StorageReference storageReference = storage.getReferenceFromUrl("gs://menu-96dd8.appspot.com");
            StorageReference pathReference;

            pathReference = storageReference.child("board.png");

            // 딜레이 필요함

            pathReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Log.i("Uri", uri.toString());
                img_url = uri.toString();
            });
        }

        Log.i("img_url", img_url);
    }

}
