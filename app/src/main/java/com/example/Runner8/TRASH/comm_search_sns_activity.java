package com.example.Runner8.TRASH;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;


public class comm_search_sns_activity extends AppCompatActivity {

    String[] searchItems = {"제목/내용","닉네임"};
    String[] sortItems = {"최신순","인기순","조회순"};

    Spinner searchSpinner, sortSpinner;
    RecyclerView listSearchResult;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1:

                String[] to = {"djskal3745@gmail.com"};
                String subject = "D-DIET_ 건의사항";
                String message = "";
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                email.putExtra(Intent.EXTRA_EMAIL, to);
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);
                email.setType("message/rfc822");
                startActivity(email);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activity);

        sortSpinner = findViewById(R.id.sortspinner);
        searchSpinner = findViewById(R.id.searchspinner);
        listSearchResult = findViewById(R.id.list_searchSNS);

        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, searchItems
        );
        searchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        searchSpinner.setAdapter(searchAdapter);

        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //    pick_item = searchItems[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            //    pick_item = searchItems[0];
            }
        });

        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, sortItems
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sortSpinner.setAdapter(sortAdapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //    pick_item = searchItems[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //    pick_item = searchItems[0];
            }
        });

    }
}
