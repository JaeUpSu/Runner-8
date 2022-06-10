package com.example.Runner8.TRASH;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.Runner8.R;
import com.example.Runner8.ui.Graph.GraphActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HealthFragment extends Fragment {
    // Store instance variables
    private int frag_num;
    TextView data_t;

    private Object mData;

    Button del_btn,add_btn,ok_btn, result_btn;
    TextView met_result, picked_name, mettv, health_imgText1,health_imgText2,health_imgText3;
    EditText health_time;
    ImageButton bookmark;
    ListView kcal_list;
    ToggleButton btn1,btn2,btn3;

    int met_health_time = 0;
    int position_p = -1;
    double met_kcal= 0;
    double met_num = 0;
    double kg = 0;
    boolean btn_able = true;

    String[] met_health_3pick = {};
    Integer[] met_drawable_3pick = {};
    double[] met_num_3pick = {};

    String[] get_drawable_list = {};
    String[] get_health_list = {};
    String[] get_num_list = {};

    ArrayList<String> name_arr;
    ArrayList<String> kcal_arr;
    ArrayList<Integer> draw_arr;

    //
    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    CollectionReference pick = db.collection("Users").document(user.getUid())
            .collection("Pick");

    FloatingActionButton floatingActionButton;

    public HealthFragment(){

    }

    public static HealthFragment newInstance(int num){
        HealthFragment healthFragment = new HealthFragment();
        Bundle args = new Bundle();
        args.putInt("number",num);
        healthFragment.setArguments(args);
        return healthFragment;
    }
    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// data_t = view.findViewById(R.id.tvName1);
        //  data_t.setText("Page " + frag_num);

        Log.i("Fragment", "HealthFragment onCreateView!!");

        View v = inflater.inflate(R.layout.fragment_health,container,false);

        kcal_list = v.findViewById(R.id.kcal_list);
        //bookmark = view.findViewById(R.id.bookMark);
        result_btn = v.findViewById(R.id.result_health_btn);
        del_btn = v.findViewById(R.id.delbtn);
        add_btn = v.findViewById(R.id.addbtn);
        health_time = v.findViewById(R.id.health_time);
        met_result = v.findViewById(R.id.met_result);
        mettv = v.findViewById(R.id.mettv);
        btn1 = v.findViewById(R.id.pick_btn1);
        btn2 = v.findViewById(R.id.pick_btn2);
        btn3 = v.findViewById(R.id.pick_btn3);
        picked_name = v.findViewById(R.id.health_name_pick);
        bookmark = v.findViewById(R.id.bookMark);
        floatingActionButton = v.findViewById(R.id.health_graph);

        health_imgText1 = v.findViewById(R.id.health_img1);
        health_imgText2 = v.findViewById(R.id.health_img2);
        health_imgText3 = v.findViewById(R.id.health_img3);

        // 선택된 3가지 운동 (이름, met, drawable)
        met_health_3pick = new String[]{"자전거타기","웨이트","조깅"};
        met_num_3pick = new double[]{3.0,6.0,7.0};
        met_drawable_3pick = new Integer[]{R.drawable.btn1_selector,R.drawable.btn7_selector,R.drawable.btn10_selector};

        // 숫자키패드
        health_time.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        name_arr = new ArrayList<>();
        kcal_arr = new ArrayList<>();
        draw_arr = new ArrayList<>();
        CustomList adapter = new CustomList(getActivity());
        kcal_list.setAdapter(adapter);

        //
        floatingActionButton.setOnClickListener(v12 -> {
            Intent intent = new Intent(getContext(), GraphActivity.class);
            intent.putExtra("kcal_name", "health_kcal");
            intent.putExtra("none_data", "none_health_data");
            startActivity(intent);
        });

        //
        bookmark.setOnClickListener(v1 -> {
            Intent intent = new Intent(getContext(), BookMarkActivity.class);
            startActivity(intent);
        });

        // UserData 초기화
        db.collection("Users").document(user.getUid())
                .collection("Profile").document("diet_profile")
                .get()
                .addOnCompleteListener(task -> kg = Double.valueOf(task.getResult().get("kg").toString()));

        btn1.setOnClickListener(view -> {
            boolean on = ((ToggleButton) view).isChecked();
            if (on) {
                if (btn_able) {
                    mettv.setText("0");
                    met_num = met_num_3pick[0];
                    picked_name.setText(met_health_3pick[0]);
                    Log.i("1", met_drawable_3pick[0] + " / " + met_num_3pick[0] + " / " + met_health_3pick[0]);
                    btn2.setChecked(false);
                    btn3.setChecked(false);
                }
            } else {
                Log.i("1", Boolean.toString(btn1.isChecked()));
                picked_name.setText("(운동명)");
            }
        });
        btn2.setOnClickListener(view -> {
            boolean on = ((ToggleButton) view).isChecked();
            if (on) {
                if (btn_able) {
                    mettv.setText("0");
                    met_num = met_num_3pick[1];
                    picked_name.setText(met_health_3pick[1]);
                    Log.i("2",met_drawable_3pick[1] +" / " + met_num_3pick[1] + " / "+ met_health_3pick[1]);
                    btn1.setChecked(false);
                    btn3.setChecked(false);
                }
            } else {
                Log.i("2", Boolean.toString(btn2.isChecked()));
                picked_name.setText("(운동명)");
            }
        });
        btn3.setOnClickListener(view -> {
            boolean on = ((ToggleButton) view).isChecked();
            if (on) {
                if (btn_able) {
                    mettv.setText("0");
                    met_num = met_num_3pick[2];
                    picked_name.setText(met_health_3pick[2]);
                    Log.i("3",met_drawable_3pick[2] +" / " + met_num_3pick[2] + " / "+ met_health_3pick[2]);
                    btn2.setChecked(false);
                    btn1.setChecked(false);
                }
            } else {
                Log.i("3", Boolean.toString(btn3.isChecked()));
                picked_name.setText("(운동명)");
            }
        });

        result_btn.setOnClickListener(this::onClick);

        add_btn.setOnClickListener(view -> {
            if(mettv.getText().toString() !="kg 미입력!" && mettv.getText().toString() != "버튼 미클릭!")
            {
                if(picked_name.getText().toString() != "(운동명)") {
                    int count = adapter.getCount() + 1;
                    kcal_arr.add(mettv.getText().toString());
                    name_arr.add( "  " + picked_name.getText().toString());
                    adapter.notifyDataSetChanged();
                    met_kcal += Math.round((Double.parseDouble(mettv.getText().toString()) * 100) / 100);
                    met_result.setText(Double.toString(met_kcal));


                }else {
                    Toast.makeText(getContext(),"운동을 선택해주세요!",Toast.LENGTH_SHORT).show();
                }
            }

        });
        kcal_list.setOnItemClickListener((parent, view1, position, id) -> {
            position_p = position;
            Log.i("클릭", Integer.toString(position));
            adapter.notifyDataSetChanged();
        });
        del_btn.setOnClickListener(view -> {
            int count;
            count = adapter.getCount();
            if (count > 0) {
                if (position_p > -1 && position_p < count) {
                    double minus_kcal = Double.parseDouble(kcal_arr.get(position_p));
                    met_kcal -= Math.round(minus_kcal);
                    met_result.setText(Double.toString(met_kcal));

                    name_arr.remove(position_p);
                    kcal_arr.remove(position_p);
                    position_p = -1;
                    kcal_list.clearChoices();
                }
            } else {
                met_kcal = 0;
                met_result.setText("0");
            }
            adapter.notifyDataSetChanged();
            position_p = -1;

        });

        // BookMarkData 초기화
        pick.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : task.getResult()){
                    if(document.getId().equals("Drawable")){
                        for(int i=0;i<3;i++)
                            met_drawable_3pick[i] = Integer.parseInt(document.get(String.valueOf(i)).toString());
                    }
                    else if(document.getId().equals("Num")){
                        for(int i=0;i<3;i++)
                            met_num_3pick[i] = Double.valueOf(document.get(String.valueOf(i)).toString());
                    }
                    else if(document.getId().equals("Name")){
                        for(int i=0;i<3;i++)
                            met_health_3pick[i] = document.get(String.valueOf(i)).toString();
                    }

                    // health_img_set
                    btn1.setBackground(ContextCompat.getDrawable(getActivity(),met_drawable_3pick[0]));
                    btn2.setBackground(ContextCompat.getDrawable(getActivity(),met_drawable_3pick[1]));
                    btn3.setBackground(ContextCompat.getDrawable(getActivity(),met_drawable_3pick[2]));

                    // health_imgText_set
                    health_imgText1.setText(met_health_3pick[0]);
                    health_imgText2.setText(met_health_3pick[1]);
                    health_imgText3.setText(met_health_3pick[2]);

                    Log.i("met_drawable_3pick", met_drawable_3pick[0].toString());
                }
            }
        });

        Log.i("LifeCycle", "HealthFragment" + "onCreateView");

        return v;
    }
    @SuppressLint("DefaultLocale")
    public String met_kcal(int time, double met_num, double kg){
        return String.format("%.2f",5 * met_num * 3.5 * kg * time / 1000);
    }

    private void onClick(View v) {
        if (health_time.getText().toString().length() > 0)
        {
            met_health_time = Integer.parseInt(health_time.getText().toString());
            
            // kg = ((MainActivity) getActivity()).get_kg(); 메인에서 가져오는 get_kg

            //  met으로 칼로리 계산
            if(kg == 0)
                mettv.setText("kg 미입력!");
            else if(met_num == 0)
                mettv.setText("버튼 미클릭!");
            else if(met_health_time > 0)
                mettv.setText(met_kcal(met_health_time, met_num, kg));}

    }

    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;

        public CustomList(Activity context){
            super(context, R.layout.kcal_item,name_arr);
            this.context = context;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent){
            LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(R.layout.kcal_item,null,true);
            TextView nametv = v.findViewById(R.id.kcal_health);
            TextView kcaltv = v.findViewById(R.id.health_kcal_result);

            nametv.setText(name_arr.get(position));
            kcaltv.setText(kcal_arr.get(position) + " k c a l");
            return v;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        // BookMarkData 초기화
        pick.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        if(document.getId().equals("Drawable")){
                            for(int i=0;i<3;i++)
                                met_drawable_3pick[i] = Integer.parseInt(document.get(String.valueOf(i)).toString());
                        }
                        else if(document.getId().equals("Num")){
                            for(int i=0;i<3;i++)
                                met_num_3pick[i] = Double.valueOf(document.get(String.valueOf(i)).toString());
                        }
                        else if(document.getId().equals("Name")){
                            for(int i=0;i<3;i++)
                                met_health_3pick[i] = document.get(String.valueOf(i)).toString();
                        }

                        // health_img_set
                        btn1.setBackground(ContextCompat.getDrawable(getActivity(),met_drawable_3pick[0]));
                        btn2.setBackground(ContextCompat.getDrawable(getActivity(),met_drawable_3pick[1]));
                        btn3.setBackground(ContextCompat.getDrawable(getActivity(),met_drawable_3pick[2]));

                        // health_imgText_set
                        health_imgText1.setText(met_health_3pick[0]);
                        health_imgText2.setText(met_health_3pick[1]);
                        health_imgText3.setText(met_health_3pick[2]);

                        Log.i("met_drawable_3pick", met_drawable_3pick[0].toString());
                    }
                }
            }
        });
    }
}