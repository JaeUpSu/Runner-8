package com.example.Runner8.TRASH;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Runner8.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookMarkActivity extends AppCompatActivity {

    ListView list;
    Button btn;

    ArrayList<String> met_health_list;
    ArrayList<String> met_num_list;
    ArrayList<String> met_drawable_list;

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String[] met_health = new String[]{"자전거타기", "체조","골프", "속보","배드민턴","야구","웨이트","농구","에어로빅", "조깅","축구","테니스","스키","등산","사이클","런닝","수영", "유도","태권도","계단오르기"};
    double[] met_num  = new double[]{3.0,3.5,3.5,4.0,4.5,5.0,6.0,6.0,6.5,7.0,7.0,7.0,7.0,7.5, 8.0,8.0,8.0,10.0,10.0,15.0};
    Integer[] met_drawable  = new Integer[]{R.drawable.bicycle, R.drawable.gymnestics,
            R.drawable.golf,R.drawable.fastwalk,R.drawable.badminton,
            R.drawable.baseball, R.drawable.weighttraining,R.drawable.basketball,
            R.drawable.aerobics, R.drawable.jogging,R.drawable.soccer,R.drawable.tennis,R.drawable.ski,
            R.drawable.mountain ,R.drawable.cycle,R.drawable.running,R.drawable.swimming,
            R.drawable.judo,R.drawable.taekwondo,R.drawable.stairclimb};
    Integer[] met_selector = new Integer[]{R.drawable.btn1_selector, R.drawable.btn2_selector,
            R.drawable.heart_selector2,R.drawable.btn4_selector,R.drawable.btn5_selector,
            R.drawable.btn6_selector, R.drawable.btn7_selector,R.drawable.btn8_selector,
            R.drawable.btn9_selector, R.drawable.btn10_selector,R.drawable.btn11_selector,R.drawable.btn12_selector,R.drawable.btn13_selector,
            R.drawable.btn14_selector ,R.drawable.btn15_selector,R.drawable.btn16_selector,R.drawable.btn17_selector,
            R.drawable.btn18_selector,R.drawable.btn19_selector,R.drawable.btn20_selector};

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_health_pick);

        btn = findViewById(R.id.pick_ok_btn);
        list = findViewById(R.id.pickList);

        CustomList Adapter = new CustomList(BookMarkActivity.this);
        list.setAdapter(Adapter);

        met_health_list = new ArrayList<>();
        met_num_list = new ArrayList<>();
        met_drawable_list = new ArrayList<>();

        // Intent intent = new Intent(PickActivity.this,MainActivity.class);
        String[] num_array = {"","",""};
        String[] health_array = {"","",""};
        String[] drawable_array = {"","",""};

        // 저장
        btn.setOnClickListener((View view) -> {
            if(met_health_list.size() == 3) {

                Map<String, Object> drawable = new HashMap<>();
                Map<String, Object> num = new HashMap<>();
                Map<String, Object> name = new HashMap<>();

                for (int i = 0; i < 3; i ++){
                    num_array[i] = met_num_list.get(i);
                    health_array[i] = met_health_list.get(i);
                    drawable_array[i] = met_drawable_list.get(i);
                    Log.i("pick", met_health_list.get(i) + met_num_list.get(i) + met_drawable_list.get(i));
                }

                List list = new ArrayList();

                for (int i = 0; i < num_array.length; i++) {
                    num.put(String.valueOf(i), num_array[i]);
                    drawable.put(String.valueOf(i), drawable_array[i]);
                    name.put(String.valueOf(i), health_array[i]);
                }

                CollectionReference pick = db.collection("Users").document(user.getUid())
                        .collection("Pick");

                pick.document("Name").set(name);
                pick.document("Num").set(num);
                pick.document("Drawable").set(drawable);

                Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show();

                finish();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("운동");
                builder.setMessage("3개 선택 필수!!");
                builder.setPositiveButton("확 인",null);
                AlertDialog dialog = builder.create();
                dialog.show();

                Toast.makeText(this,"3개 선택 필수!!",Toast.LENGTH_SHORT).show();
            }

        });


    }

    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;

        public CustomList(Activity context) {
            super(context, R.layout.health_list_item, met_health);
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            LayoutInflater inflater = context.getLayoutInflater();
            @SuppressLint({"ViewHolder", "InflateParams"})
            View rowView = inflater.inflate(R.layout.health_list_item,null,true);
            ImageView img = rowView.findViewById(R.id.imageView);
            TextView name = rowView.findViewById(R.id.health_name);
            TextView met = rowView.findViewById(R.id.met_level);
            ToggleButton pickbtn = rowView.findViewById(R.id.togglepick);

            name.setText(met_health[position]);
            met.setText(String.valueOf(met_num[position]));
            img.setImageResource(met_drawable[position]);

            Log.i("CustomListCheck", met_health[position]);

            pickbtn.setOnClickListener(v -> {
                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    if (met_health_list.size() < 3){
                        met_health_list.add(met_health[position]);
                        met_num_list.add(String.valueOf(met_num[position]));
                        met_drawable_list.add(String.valueOf(met_selector[position]));
                    }
                    else{
                        pickbtn.setChecked(false);
                        Toast.makeText(getContext(),"3개만 등록 가능",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    met_health_list.remove(met_health[position]);
                    met_num_list.remove(String.valueOf(met_num[position]));
                    met_drawable_list.remove(String.valueOf(met_selector[position]));
                }
            });

            return rowView;
        }
    }
}
