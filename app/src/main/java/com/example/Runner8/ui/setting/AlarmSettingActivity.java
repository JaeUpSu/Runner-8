package com.example.Runner8.ui.setting;

import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.Runner8.R;

import java.util.Calendar;

public class AlarmSettingActivity extends AppCompatActivity {

    private Calendar calendar;
    private SharedPreferences prefs;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm_setting);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.alarm_setting, new SettingFragment()).commit();

        Toolbar toolbar = findViewById(R.id.alarm_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("설정");

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
}
