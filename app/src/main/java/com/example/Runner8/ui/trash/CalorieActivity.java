package com.example.Runner8.ui.trash;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Runner8.R;
import com.example.Runner8.TRASH.FoodItem;
import com.example.Runner8.TRASH.Foods;
import com.example.Runner8.ui.F_H.calorieDictionary.Adapter.SearchAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CalorieActivity extends AppCompatActivity {

    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private Button button;
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<FoodItem> foodItems = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;
    Foods foods = new Foods();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_calorie_dictionary);

        editSearch =  findViewById(R.id.editSearch);
        listView =  findViewById(R.id.food_search_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        list = new ArrayList<>();

        InitialListToFoodDB();

        list.addAll(foods.getFood());

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list, this);

        // 리스트뷰에 아답터를 연결한다.

        listView.setAdapter(adapter);

        Log.i("adapterCount",Integer.toString(adapter.getCount()));

        // 키보드 앤터키를 찾기로 설정
        editSearch.setOnKeyListener((v, keyCode, event) -> {
            if((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER))
                return true;
            else{
                return false;
            }
        });

        // input 창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        // 클릭시 음식 상세정보 Activity 로 이동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView c = view.findViewById(R.id.label);
                String item = c.getText().toString();

                if(item != null){
                    Intent intent = new Intent(CalorieActivity.this, FoodDetailActivity.class);
                    intent.putExtra("FoodName",item);
                    startActivity(intent);
                    Log.i("item", item);
                }
            }
        });

    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.

        list.clear();

        list.addAll(foods.getContainFood(charText));

        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    // FoodList Update (FireStore)
    public void InitialListToFoodDB(){

        db.collection("Calorie").document("Food")
                .collection("Dictionary")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            int i=0;
                            for(QueryDocumentSnapshot document : task.getResult())
                                foods.addFoodData(document.getId());
                        }
                        else Log.d("QueryError", "Error getting documents: ", task.getException());
                    }
                });
    }

}
