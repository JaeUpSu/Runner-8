package com.example.Runner8;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Runner8.SingleTon.LoginSingleTon;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.example.Runner8.SingleTon.Top_Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    UserUpdate userUpdate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initial_load);

        if(user != null) {
            userUpdate = new UserUpdate();
            InitialSingleTon();
            LoadingStart();
        }
        else{
            Log.i("Splash InExist User", "check!!");
            Intent intent = new Intent(getApplicationContext(), loginActivity.class);
            startActivity(intent);
        }
    }
    public void LoadingStart(){

        Handler handler = new Handler();

        if(LoginSingleTon.getInstance().isLogin_finish_check()) {
            userUpdate.startUpdate(user);

            handler.postDelayed(() -> {
                Log.i("Splash InExist User", "check!!");

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }, 2000);
        }
        else{
            db.collection("Users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        Boolean auto_check = Boolean.valueOf(document.get("auto_check").toString());
                        if (auto_check) {
                            Log.i("startUpdate", "startUpdate");
                            userUpdate.startUpdate(user);

                            handler.postDelayed(() -> {
                                Log.i("Splash InExist User", "check!!");

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);

                            }, 2000);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                            startActivity(intent);
                        }
                    });
        }
    }
    public void InitialSingleTon(){
        // TOP3
        Top_Data.getInstance().clearTop_count();
        Top_Data.getInstance().clearTop_high_kcal();
        Top_Data.getInstance().clearTop_high_pro();
        Top_Data.getInstance().clearTop_row_kcal();

        // 많이 먹은 요일
        Sub_bundle.getInstance().setDayOfTheWeek_lastMonth(null);
        Sub_bundle.getInstance().setAvg_DayOfTheWeek_lastMonth(null);
        Sub_bundle.getInstance().setRecently_month(null);

        //
    }
}
