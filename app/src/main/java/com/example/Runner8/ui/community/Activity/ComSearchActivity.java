package com.example.Runner8.ui.community.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.ui.Graph.Today_Date;
import com.example.Runner8.ui.community.Adapter.Board.model.BoardModel;
import com.example.Runner8.ui.community.Adapter.Search.ComSearchAdapter;
import com.example.Runner8.ui.community.Adapter.Search.Model.RecentlyWord;
import com.example.Runner8.ui.community.Adapter.Search.RecentlyWordAdapter;
import com.example.Runner8.ui.community.TimeValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComSearchActivity extends AppCompatActivity {

    String[] items = {"제목/내용","닉네임"};
    String pick_item = "";
    BoardModel data;
    RecentlyWord recentlyWord;
    boolean recently_list_check = false;

    //
    ComSearchAdapter comSearchAdapter;
    RecentlyWordAdapter recentlyWordAdapter;

    //
    ArrayList<BoardModel> dataArrayList = new ArrayList<>();        // 출력용
    ArrayList<BoardModel> tmpArrayList = new ArrayList<>();         // 저장용
    ArrayList<BoardModel> containArrayList = new ArrayList<>();     // 찾는용
    ArrayList<RecentlyWord> recently_word_list = new ArrayList<>();

    TextView tv_recently;
    EditText edit_search;
    // Spinner spinner;
    ImageButton search_button;
    ToggleButton btn_record;
    RecyclerView recyclerView;
    Toolbar toolbar;
    LinearLayout searchPrevLayout, searchResultLayout;


    ComSearchActivity activity;

    //
    Today_Date today_date = new Today_Date();
    TimeValue timeValue;

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_search);

        data = new BoardModel();

        edit_search = findViewById(R.id.et_Search);
        // spinner = findViewById(R.id.spinner);
        search_button = findViewById(R.id.search_button);
        recyclerView = findViewById(R.id.list_searchRecord);
        toolbar = findViewById(R.id.search_board_toolbar);
        searchPrevLayout = findViewById(R.id.searchPrevLayout);
        searchResultLayout = findViewById(R.id.searchResultLayout);
        tv_recently = findViewById(R.id.tv_recently);

        //
        getBoardList();

        //
        recently_list_show();

        // toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("게시물 검색");


        activity = this;

        comSearchAdapter = new ComSearchAdapter(this,dataArrayList, "");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(comSearchAdapter);

        // spinner
        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this.getBaseContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        // spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pick_item = items[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pick_item = items[0];
            }
        });



        data.setAbleRecord(true);

        btn_record.setOnClickListener(v -> {
            boolean on = ((ToggleButton) v).isChecked();
            if (on)
                data.setAbleRecord(true); // 리사이클러뷰 레코드 ON
            else
                data.setAbleRecord(false); // 리사이클러뷰 레코드 OFF
        });



        btn_search.setOnClickListener(v -> {


        });

         */

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("onTextChanged", edit_search.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(edit_search.getText().length() <= 1){

                    recently_list_show();
                }
                else {
                    // 최근 검색어
                    searchPrevLayout.setVisibility(View.INVISIBLE);
                    searchResultLayout.setVisibility(View.VISIBLE);
                    tv_recently.setVisibility(View.INVISIBLE);
                    String text = edit_search.getText().toString();
                    Log.i("afterTextChanged", "check!!" + "  " + text);
                    Search(text);

                    recently_list_check = false;
                }
            }
        });

        search_button.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "두글자 이상 입력해주세요!!", Toast.LENGTH_SHORT).show();

        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.remove_recently_data:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("확인");
                builder.setMessage(" 정말로 최근 검색기록을 삭제하시겠습니까?? ");
                builder.setNegativeButton("예", (dialog, which) -> {
                    DocumentReference dr_recentlyData =
                            db.collection("Users").document(user.getUid())
                                    .collection("Community").document("RecentlySearched");
                    CollectionReference cr_recentlyData = dr_recentlyData.collection("item");

                    cr_recentlyData.get().addOnCompleteListener(task -> {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            cr_recentlyData.document(document.getId()).delete();
                        }
                        Map<String, Object> map = new HashMap<>();;
                        map.put("final_index", 0);
                        dr_recentlyData.update(map);

                        searchPrevLayout.setVisibility(View.VISIBLE);
                        searchResultLayout.setVisibility(View.INVISIBLE);

                        recentSearchList();
                        recently_list_check = true;
                    });

                })
                        .setPositiveButton("아니오", (dialog, which) -> {
                    dialog.dismiss();
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getBoardList(){
        db.collection("Community").document("board")
                .collection("item")
                .get()
                .addOnCompleteListener(task -> {

                    today_date.setNow();

                    for(QueryDocumentSnapshot document : task.getResult()) {
                        BoardModel boardModel = new BoardModel();

                        timeValue = new TimeValue(document.get("date").toString(), (document.get("time").toString()));
                        String time_value = timeValue.getTimeValue();

                        // TimeValue
                        boardModel.setTimeValue(time_value);
                        // 사진
                        boardModel.setImg(document.get("img").toString());
                        // 제목
                        boardModel.setTitle(document.get("title").toString());
                        // Index
                        boardModel.setBoard_index(document.get("index").toString());
                        // UID
                        boardModel.setUid(document.get("uid").toString());
                        // 조회수
                        boardModel.setViews(document.get("views").toString());
                        // 작성자의 index
                        boardModel.setWriter_index(document.get("writer_index").toString());
                        // 댓글 수
                        boardModel.setComment_count(document.get("comment_count").toString());
                        //
                        boardModel.setUp_count(document.get("up_count").toString());

                        tmpArrayList.add(boardModel);
                    }
                });
    }

    public void Search(String text){

        dataArrayList.clear();

        Log.i("dataArrayList.addAll", edit_search.getText().toString());

        dataArrayList.addAll(getContainBoard(text));

        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        comSearchAdapter = new ComSearchAdapter(this,dataArrayList, text);
        recyclerView.setAdapter(comSearchAdapter);
        comSearchAdapter.notifyDataSetChanged();
    }

    public ArrayList<BoardModel> getContainBoard(String text){
        containArrayList.clear();

        int count=0;

        for(BoardModel board : tmpArrayList){
            if((board.getTitle().contains(text)) && count < 10){
                containArrayList.add(board);
                count++;
            }
        }
        return containArrayList;
    }

    public void recentSearchList(){
        dataArrayList.clear();
        comSearchAdapter = new ComSearchAdapter(this,dataArrayList, "");
        recyclerView.setAdapter(comSearchAdapter);
        comSearchAdapter.notifyDataSetChanged();
    }

    public void recently_list_show(){

        if(!recently_list_check) {
            DocumentReference dr_recently_searched =
                    db.collection("Users").document(user.getUid())
                    .collection("Community").document("RecentlySearched");

            dr_recently_searched.get().addOnCompleteListener(task -> {
                Log.i("recently_searched.get()", "check!!");

                DocumentSnapshot document = task.getResult();

                if(document.get("final_index") != null) {
                    String final_index = document.get("final_index").toString();

                    Log.i("final_index", final_index);
                    if (final_index.equals("0")) {
                        // 최근 검색어
                        searchPrevLayout.setVisibility(View.VISIBLE);
                        searchResultLayout.setVisibility(View.INVISIBLE);
                        tv_recently.setVisibility(View.INVISIBLE);

                        recentSearchList();
                        recently_list_check = true;
                    } else {

                        searchPrevLayout.setVisibility(View.INVISIBLE);
                        searchResultLayout.setVisibility(View.VISIBLE);
                        tv_recently.setVisibility(View.VISIBLE);

                        dr_recently_searched.collection("item")
                                .whereLessThanOrEqualTo("index", Integer.valueOf(final_index))
                                .orderBy("index", Query.Direction.DESCENDING)
                                .limit(8)
                                .get()
                                .addOnCompleteListener(task1 -> {

                                    ArrayList<Map> arrayList = new ArrayList<>();

                                    Log.i("task1", String.valueOf(task1.getResult().size()));

                                    for (QueryDocumentSnapshot document2 : task1.getResult()) {
                                        arrayList.add(document2.getData());
                                    }

                                    recently_word_list.clear();

                                    for (Map<String, Object> item : arrayList) {
                                        recentlyWord = new RecentlyWord();
                                        recentlyWord.setWord(item.get("word").toString());

                                        recently_word_list.add(recentlyWord);

                                        Log.i("item.get(\"word\")", item.get("word").toString());
                                    }
                                    // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.

                                    recentlyWordAdapter = new RecentlyWordAdapter(recently_word_list);

                                    Log.i("Count", String.valueOf(recentlyWordAdapter.getItemCount()));
                                    recyclerView.setAdapter(recentlyWordAdapter);
                                    recentlyWordAdapter.notifyDataSetChanged();

                                    recently_list_check = true;
                                });
                    }
                }
                else{
                    // 최근 검색어
                    searchPrevLayout.setVisibility(View.VISIBLE);
                    searchResultLayout.setVisibility(View.INVISIBLE);
                    tv_recently.setVisibility(View.INVISIBLE);

                    recentSearchList();
                    recently_list_check = true;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.com_search_menu, menu);
        return true;
    }
}
