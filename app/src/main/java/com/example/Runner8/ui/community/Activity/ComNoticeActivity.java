package com.example.Runner8.ui.community.Activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.Runner8.R;
import com.example.Runner8.SingleTon.ScreenChangeDetect;
import com.example.Runner8.ui.community.Adapter.Notice.model.NoticeModel;

public class ComNoticeActivity extends AppCompatActivity {

    ImageView img_user;
    TextView tv_boardName, tv_title, tv_boardDate, tv_boardViewCount, tv_boardContent;
    Toolbar toolbar;

    //
    NoticeModel noticeModel;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_pickboard);

        img_user = findViewById(R.id.img_user);
        tv_boardName = findViewById(R.id.tv_boardName);
        tv_title = findViewById(R.id.tv_title);
        tv_boardDate = findViewById(R.id.tv_boardDate);
        tv_boardViewCount = findViewById(R.id.tv_boardViewCount);
        tv_boardContent = findViewById(R.id.tv_boardContent);
        toolbar = findViewById(R.id.notice_board_toolbar);

        // toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("공지게시판");

        //
        getIntentData();

        //
        InitialView();

    }
    public void getIntentData(){
        noticeModel = (NoticeModel) getIntent().getSerializableExtra("NoticeModel");
    }
    public void InitialView(){

        Glide.with(getApplicationContext())
                .load(noticeModel.getProfile())
                .into(img_user);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            img_user.setClipToOutline(true);
        }

        tv_boardName.setText("Admin");
        tv_title.setText(noticeModel.getTitle());
        tv_boardDate.setText(noticeModel.getTimeValue());
        tv_boardViewCount.setText("조회수 " + noticeModel.getViews());
        tv_boardContent.setText(noticeModel.getContent());

        //
        ScreenChangeDetect.getInstance().setNoticePickBoardToNotice(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ScreenChangeDetect.getInstance().setNoticeModel(noticeModel);
    }


}
