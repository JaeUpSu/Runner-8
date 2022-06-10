package com.example.Runner8.TRASH;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Runner8.R;

public class MapSearchActivity extends AppCompatActivity {

    ImageButton btn_back;
    EditText et_searchContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapsearch);

        et_searchContent = findViewById(R.id.et_mapsearch);
        btn_back = findViewById(R.id.searchbtn_back);

        btn_back.setOnClickListener(v -> {
            finish();
        });
    }
}
