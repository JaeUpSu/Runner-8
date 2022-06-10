package com.example.Runner8.ui.F_H.calorie.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.Runner8.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FoodSearchActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    NavController navController;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_food_search);
        bottomNavigationView = findViewById(R.id.bottomNavigationView2);

        navController = Navigation.findNavController(this, R.id.nav_food_search_fragment);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> Log.i("check", "bottomNavigationViewCHECK"));

        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
