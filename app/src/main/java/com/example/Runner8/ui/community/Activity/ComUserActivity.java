package com.example.Runner8.ui.community.Activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Runner8.R;
import com.example.Runner8.ui.community.Adapter.Board.BoardAdapter;
import com.example.Runner8.ui.community.Adapter.Board.model.BoardModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ComUserActivity extends AppCompatActivity {

    ImageView img_user;
    TextView tv_name, tv_wrttenBy, tv_commentCount, tv_likeCount;
    Button btn_writtenByRecord, btn_commentRecord;

    //
    RecyclerView recyclerView;

    // Valuable
    String uid;
    ArrayList<BoardModel> boardModels = new ArrayList<>();

    // Class
    BoardAdapter boardAdapter;

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.written_by);

        img_user = findViewById(R.id.img_user);
        tv_name = findViewById(R.id.tv_name);
        tv_wrttenBy = findViewById(R.id.tv_wrttenBy);
        tv_commentCount = findViewById(R.id.tv_commentCount);
        tv_likeCount = findViewById(R.id.tv_likeCount);
        btn_writtenByRecord = findViewById(R.id.btn_writtenByRecord);
        btn_commentRecord = findViewById(R.id.btn_commentRecord);
        recyclerView = findViewById(R.id.list_myRecord);

        uid = getIntent().getStringExtra("uid");

        btn_writtenByRecord.setOnClickListener(v -> {

        });

        btn_commentRecord.setOnClickListener(v -> {

        });


        InitialView();
    }

    public void InitialView(){

        boardAdapter = new BoardAdapter(this, boardModels);
        boardAdapter.setCom_user_activity(true);
        recyclerView.setAdapter(boardAdapter);

        DocumentReference dr_user = db.collection("Users").document(uid);

        // profile, nickName
        dr_user.collection("Profile").document("diet_profile")
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot document = task.getResult();

                    tv_name.setText(document.get("nickName").toString());
                    // profile

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        img_user.setClipToOutline(true);
                    }
                    Glide.with(getApplicationContext())
                            .load(document.get("user_img").toString())
                            .into(img_user);
                });

        tv_wrttenBy.setText("");
        tv_commentCount.setText("");
        tv_likeCount.setText("");

        // listView

        dr_user.collection("Comm")
                .get()
                .addOnCompleteListener(task -> {
                    for(QueryDocumentSnapshot document : task.getResult()){

                    }
                });

    }
}
