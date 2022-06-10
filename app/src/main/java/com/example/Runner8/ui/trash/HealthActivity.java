package com.example.Runner8.ui.trash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Runner8.MainActivity;
import com.example.Runner8.R;

public class HealthActivity extends AppCompatActivity {

    Button save_btn, cancel_btn, ok_btn;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.health_activity);

        ok_btn = findViewById(R.id.ok_health_btn);

        ok_btn.setOnClickListener(v -> {
            Intent intent = new Intent(HealthActivity.this, MainActivity.class);
            finish();   //자꾸 쌓이는 것 방지.
            startActivity(intent);
        });
    }
}
