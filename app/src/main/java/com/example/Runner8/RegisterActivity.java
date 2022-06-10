package com.example.Runner8;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Runner8.SingleTon.LoginSingleTon;
import com.example.Runner8.ui.Graph.Today_Date;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText mEmailText, mPasswordText, mPasswordCheckText, mNameText;
    private TextInputEditText newid_inputedit, newpassword_inputedit, nick_inputedit, newpassword_check_inputedit;

    private TextInputEditText age_inputedit, height_inputedit, kg_inputedit, goal_inputedit;
    private Button btn_join;
    private ToggleButton btn_man, btn_woman;

    //
    Today_Date today_date = new Today_Date();

    //

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        btn_join = findViewById(R.id.btn_join);
        newid_inputedit = findViewById(R.id.newid_inputedit);
        newpassword_inputedit = findViewById(R.id.newpassword_inputedit);
        newpassword_check_inputedit = findViewById(R.id.newpassword_check_inputedit);
        nick_inputedit = findViewById(R.id.nick_inputedit);
        btn_man = findViewById(R.id.btn_man);
        btn_woman = findViewById(R.id.btn_woman);
        height_inputedit = findViewById(R.id.height_inputedit);
        kg_inputedit = findViewById(R.id.kg_inputedit);
        goal_inputedit = findViewById(R.id.goal_inputedit);
        age_inputedit = findViewById(R.id.age_inputedit);

        // TOGGLE
        btn_man.setOnClickListener(v -> {
            btn_man.setChecked(true);
            btn_woman.setChecked(false);
        });
        btn_woman.setOnClickListener(v -> {
            btn_woman.setChecked(true);
            btn_man.setChecked(false);
        });

        //
        storage = FirebaseStorage.getInstance();

        btn_join.setOnClickListener(v -> {
            String email = newid_inputedit.getText().toString().trim();
            String pwd = newpassword_inputedit.getText().toString().trim();
            String pwdCheck = newpassword_check_inputedit.getText().toString().trim();

            Log.i("User","email : " + email + "  pwd : " + pwd);

            if(email == null){
                Toast.makeText(RegisterActivity.this,"이메일을 입력하세요!!",Toast.LENGTH_SHORT).show();
            }
            else if(pwd == null){
                Toast.makeText(RegisterActivity.this,"비밀번호를 입력하세요!!",Toast.LENGTH_SHORT).show();
            }
            else{
                if(!pwd.equals(pwdCheck)){
                    Toast.makeText(RegisterActivity.this,"비밀번호가 틀렸습니다!!",Toast.LENGTH_SHORT).show();
                }
                else{

                    Log.d("REG","등록 버튼 " + email + " , " + pwd);
                    ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);

                    mDialog.setMessage("가입중입니다...");
                    mDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(RegisterActivity.this, task -> {

                                    if(task.isSuccessful()){

                                        DB_Initialize(task.getResult());
                                        LoginSingleTon.getInstance().setRegister_check(true);
                                        finish();
                                        Toast.makeText(RegisterActivity.this, "회원 가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this,"이미 존재하는 아이디 입니다.",Toast.LENGTH_LONG).show();
                                        mDialog.dismiss();
                                        return;
                                    }

                                mDialog.dismiss();
                            });
                }
            }
        });
    }

    public void DB_Initialize(AuthResult authResult){

        DocumentReference dr_myCourses = db.collection("Users").document(authResult.getUser().getUid())
                .collection("Map").document("MyCourses");

        DocumentReference dr_dietProfile = db.collection("Users").document(authResult.getUser().getUid())
                .collection("Profile").document("diet_profile");

        DocumentReference dr_recentlySearched =
                db.collection("Users").document(authResult.getUser().getUid())
                        .collection("Community").document("RecentlySearched");

        StorageReference storageReference = storage.getReferenceFromUrl("gs://menu-96dd8.appspot.com");
        StorageReference pathReference;
        pathReference = storageReference.child("profile.png");

        Map<String, Object> profile = new HashMap<>();
        String age = age_inputedit.getText().toString().trim();
        String height = height_inputedit.getText().toString().trim();
        String kg = kg_inputedit.getText().toString().trim();
        String goal = goal_inputedit.getText().toString().trim();
        String sex = "";
        String nickName = nick_inputedit.getText().toString().trim();
        Double mean_kcal = getAppAmount(sex, Integer.valueOf(age),
                Double.valueOf(kg), Double.valueOf(height));

        if(btn_man.isChecked()) sex = "man";
        else sex = "woman";

        profile.put("age", age);
        profile.put("height", height);
        profile.put("nickName", nickName);
        profile.put("kg", kg);
        profile.put("g_w", goal);
        profile.put("sex", sex);
        profile.put("mean", mean_kcal);
        profile.put("memo_final_count", 0);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> join_data = new HashMap<>();
        Map<String, Object> recently = new HashMap<>();
        Map<String, Object> user_data = new HashMap<>();
        Map<String, Object> my_courses = new HashMap<>();
        Map<String, Object> sound_setting = new HashMap<>();
        Map<String, Object> summary_total_map = new HashMap<>();
        Map<String, Object> com_recently = new HashMap<>();
        Map<String, Object> summary_week_data = new HashMap<>();
        Map<String, Object> week_check = new HashMap<>();

        // map.put("data_check", false);

        today_date.setNow();
        Log.i("getFormat_date()", today_date.getFormat_date());

        join_data.put("join_date", today_date.getFormat_date());
        join_data.put("TotalFoodKcalOfDay", "");
        join_data.put("TotalHealthKcalOfDay", "");
        join_data.put("first_data_date", "");
        join_data.put("comm_count", 0);
        join_data.put("auto_check", false);

        com_recently.put("final_index", 0);

        recently.put("flag", true);
        recently.put("true", today_date.getFormat_date());
        recently.put("false", "");
        recently.put("month_update_date", "");
        recently.put("week_update_date", "");

        // user_data.put("day", today_date.getDay());

        my_courses.put("final_index", 0);
        my_courses.put("total_count", 0);

        week_check.put("data_check", false);

        sound_setting.put("all", true);
        sound_setting.put("start", true);
        sound_setting.put("arrive", true);
        sound_setting.put("quarter", true);
        sound_setting.put("distance", true);

        summary_total_map.put("total_count", 0);
        summary_total_map.put("total_distance", 0);
        summary_total_map.put("total_kcal", 0);
        summary_total_map.put("total_avg_speed", 0);
        summary_total_map.put("total_avg_diet_kcal", 0);

        summary_week_data.put("prev_week_food_kcal", "");
        summary_week_data.put("prev_week_health_kcal", "");
        summary_week_data.put("current_week_food_kcal", "");
        summary_week_data.put("current_week_health_kcal", "");

        Log.i("UID", authResult.getUser().getUid());

        DocumentReference user = db.collection("Users").document(authResult.getUser().getUid());

        // 프로필의 데이터가 존재하는지 확인하기 위한 변수 설정
        //  user.collection("Profile").document("diet_profile").set(map);

        // 가입 날짜 저장
        user.set(join_data);

        // 최근 날짜 초기화
        user.collection("recently_data").document("recently_value")
                .set(recently);

        // food, health kcal 초기화
        user.collection("user_data").document(String.valueOf(today_date.getYear()))
                .collection(String.valueOf(today_date.getMonth())).document(String.valueOf(today_date.getDay()))
                .set(user_data);

        dr_myCourses.set(my_courses);

        db.collection("Users").document(authResult.getUser().getUid())
                .collection("Map").document("Sound").set(sound_setting);

        db.collection("Users").document(authResult.getUser().getUid())
                .collection("Map").document("TOTAL").set(summary_total_map);

        db.collection("Users").document(authResult.getUser().getUid())
                .collection("user_data").document("summary").set(summary_week_data);

        for(int i=1;i<=7;i++) {
            week_check.put("day_of_week", i);
            db.collection("Users").document(authResult.getUser().getUid())
                    .collection("user_data").document("week_check")
                    .collection("week").document(String.valueOf(i))
                    .set(week_check);
        }

        dr_recentlySearched.set(com_recently);

        pathReference.getDownloadUrl().addOnSuccessListener(uri -> {

            profile.put("user_img", uri.toString());
            dr_dietProfile.set(profile);
        });

    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        // 함수는 옵션 메뉴(Option Menu)에서 특정 Menu Item 을 선택하였을 때 호출되는 함수입니다.
        // 매개변수로 선택 된 MenuItem 의 객체가 넘어옵니다.

        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, loginActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public double getAppAmount(String sex, int age, double kg, double height){

        double result = 0;

        if(sex.equals("man")){
            result = 66.47 + (13.75 * kg) + (5 * height) - (6.76 * age);
        }
        else{
            result = 655.1 + (9.56 * kg) + (1.85 * height) - (4.68 * age);
        }

        return result;
    }
}
