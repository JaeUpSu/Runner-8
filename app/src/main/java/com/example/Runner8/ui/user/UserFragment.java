package com.example.Runner8.ui.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.Runner8.R;
import com.example.Runner8.SingleTon.ProfileSingleTon;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.example.Runner8.ui.setting.AlarmSettingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserFragment extends Fragment {

    private final int GALLERY_CODE = 10;

    TextView tvNickname,tvHeight, tvKg, tvGoal, tvAct, nickName, age_user, sex_user;
    EditText editMemo;
    ImageView imgUser;
    ImageButton setting_alarm;
    Button btnEdit, btnWrite;
    ListView listMemo;
    LinearLayout test_linear;
    private FirebaseStorage storage;

    String data;

    ArrayList<String> memo_arr;

    BottomNavigationView bottomNavigationView;
    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    CollectionReference cr_memo = db.collection("Users").document(user.getUid())
            .collection("Profile").document("diet_profile").collection("Memo");

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile,container,false);

        tvGoal = v.findViewById(R.id.goal_user);
        tvKg = v.findViewById(R.id.kg_user);
        tvHeight = v.findViewById(R.id.height_user);
        tvNickname = v.findViewById(R.id.user_nickname);
        editMemo = v.findViewById(R.id.memo_user);
        imgUser = v.findViewById(R.id.img_user);
        setting_alarm = v.findViewById(R.id.setting_alarm);
        btnEdit = v.findViewById(R.id.user_btn);
        btnWrite = v.findViewById(R.id.memo_btn);
        listMemo = v.findViewById(R.id.memolist_user);
        test_linear = v.findViewById(R.id.test_linear);
        nickName = v.findViewById(R.id.user_nickname);
        age_user = v.findViewById(R.id.age_user);
        sex_user = v.findViewById(R.id.sex_user);


        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgUser.setClipToOutline(true);
        }

        storage = FirebaseStorage.getInstance();

        CustomList adapter = new CustomList(getActivity());
        listMemo.setAdapter(adapter);

        // Alarm Setting
        setting_alarm.setOnClickListener(v12 -> {

            Intent intent = new Intent(getContext(), AlarmSettingActivity.class);
            startActivity(intent);

        });

        btnEdit.setOnClickListener(view -> {
            UserSetFragment userSetFragment = new UserSetFragment();
            userSetFragment.show(getParentFragmentManager(),"Dialog");
        });

        btnWrite.setOnClickListener(v1 -> {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> total_count = new HashMap<>();

            String content = editMemo.getText().toString().trim();
            int index = ProfileSingleTon.getInstance().getMemo_final_count();

            map.put("content", content);
            data = content;

            ProfileSingleTon.getInstance().addMemo_array(content);
            ProfileSingleTon.getInstance().setMemo_final_count(++index);
            adapter.notifyDataSetChanged();

            cr_memo.document(String.valueOf(index)).set(map);

            total_count.put("memo_final_count", index);
            db.collection("Users").document(user.getUid())
                    .collection("Profile").document("diet_profile").update(total_count);
        });

        listMemo.setOnItemLongClickListener((parent, view, position, id) -> {
            int count;
            count = adapter.getCount();
            String content = adapter.getItem(position);

            Log.i("position", position + "");
            Log.i("content", content + "");

            if (count > 0) {
                if (position > -1 && position < count) {
                    ProfileSingleTon.getInstance().removeMemo_array(position);

                    listMemo.clearChoices();

                    Log.i("클릭", Integer.toString(position));
                    adapter.notifyDataSetChanged();


                    // if(data.equals(content)) Log.i("check", "check1!!");
                    // Log.i("content", content);
                    cr_memo.whereEqualTo("content", content)
                            .get()
                            .addOnCompleteListener(task -> {
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    cr_memo.document(document.getId()).delete();
                                }
                            });
                }
            }
            return false;
        });

        Glide.with(getActivity())
                .load(Sub_bundle.getInstance().getPro_img_url())
                .into(imgUser);

        imgUser.setOnClickListener(v13 -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, GALLERY_CODE);
        });

        //
        tvHeight.setText(Sub_bundle.getInstance().getHeight());
        tvKg.setText(Sub_bundle.getInstance().getKg());
        tvGoal.setText(Sub_bundle.getInstance().getGoal_weight());
        nickName.setText(Sub_bundle.getInstance().getNickName());

        if(Sub_bundle.getInstance().getSex().equals("man"))
            sex_user.setText("남자");
        else
            sex_user.setText("여자");
        age_user.setText(Sub_bundle.getInstance().getAge());

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK){
            Toast.makeText(getActivity(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(requestCode == GALLERY_CODE){
            Uri file = data.getData();
            StorageReference storageRef = storage.getReference();
            // child 안에는 이미지가 저장될 경로를 적는다.
            StorageReference riversRef = storageRef.child("user1.png");
            UploadTask uploadTask = riversRef.putFile(file);

            try{
                Glide.with(getActivity()).load(file).into(imgUser);

            }catch (Exception e){
                e.printStackTrace();
            }

            DocumentReference document = db.collection("Users").document(user.getUid())
                    .collection("Profile").document("diet_profile");

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://menu-96dd8.appspot.com");
            StorageReference pathReference;

            pathReference = storageReference.child("user1.png");

            pathReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Log.i("Uri", uri.toString());
                Map<String, Object> map = new HashMap<>();
                map.put("user_img", uri.toString());
                Sub_bundle.getInstance().setPro_img_url(uri.toString());
                document.update(map);
            });

        }

    }

    public class CustomList extends ArrayAdapter<String> {
        private final Activity context;

        public CustomList(Activity context){
            super(context, R.layout.user_memo_item, ProfileSingleTon.getInstance().getMemo_array());
            this.context = context;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent){
            LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(R.layout.user_memo_item,null,true);

            TextView tvMemoUser = v.findViewById(R.id.memoItem_user);
            tvMemoUser.setTextColor(Color.BLACK);

            tvMemoUser.setText(ProfileSingleTon.getInstance().getMemo_array().get(position));
            return v;
        }
    }
}

