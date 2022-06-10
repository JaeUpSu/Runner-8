package com.example.Runner8.ui.community.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.Runner8.R;
import com.example.Runner8.SingleTon.ScreenChangeDetect;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.example.Runner8.ui.Graph.Today_Date;
import com.example.Runner8.ui.community.Adapter.Board.BoardAdapter;
import com.example.Runner8.ui.community.Adapter.Board.model.BoardModel;
import com.example.Runner8.ui.community.Adapter.HotBoard.HotBoardAdapter;
import com.example.Runner8.ui.community.Activity.ComPickBoardActivity;
import com.example.Runner8.ui.community.TimeValue;
import com.example.Runner8.ui.community.Activity.UploadBoardActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment_Comm_Home extends Fragment {

    NestedScrollView nestedScrollView;
    RecyclerView recyclerView, list_hot;
    ProgressBar progressBar;
    FloatingActionButton upload_board_button;
    SwipeRefreshLayout swipe_refresh_layout;

    //
    ArrayList<BoardModel> dataArrayList = new ArrayList<>();
    ArrayList<BoardModel> hotBoards = new ArrayList<>();
    ArrayList<BoardData> hot_board = new ArrayList<>();
    BoardAdapter adapter;
    HotBoardAdapter hotBoardAdapter;
    Hot3Adapter hot3Adapter;
    ViewPager2 hot3pager;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // Class
    Today_Date today_date = new Today_Date();
    BoardModel boardModel;
    TimeValue timeValue;
    BoardData boardData;

    CollectionReference cr_total_item = db.collection("Community").document("board")
            .collection("item");

    int page = 1, limit = 10, index, count = 0, errorCount = 0;

    public static Fragment_Comm_Home newInstance(int num){
        Fragment_Comm_Home fragment = new Fragment_Comm_Home();
        Bundle args = new Bundle();
        args.putInt("num",num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comm_home,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("onViewCreated", "onViewCreated");

        nestedScrollView = view.findViewById(R.id.home_scroll_view);
        recyclerView = view.findViewById(R.id.home_recycler_list);
        progressBar = view.findViewById(R.id.home_progress_bar);
        upload_board_button = view.findViewById(R.id.upload_board_button);
        swipe_refresh_layout =  view.findViewById(R.id.home_swipe_refresh);
        hot3pager = view.findViewById(R.id.hot3pager);
        // list_hot = view.findViewById(R.id.list_hot);

        swipe_refresh_layout.setOnRefreshListener(() -> {
            OnRefresh();
            swipe_refresh_layout.setRefreshing(false);
        });

        // 처음 리스트 출력
        InitialHotBoard();
        OnRefresh();

        nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (scrollY == nestedScrollView.getChildAt(0).
                    getMeasuredHeight() - v.getMeasuredHeight())
            {
                if(index != 0) {
                    count = 0;
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    db_getData();
                }
            }

        });

        upload_board_button.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), UploadBoardActivity.class);
            intent.putExtra("edit", false);
            startActivity(intent);
        });



    }
    public void InitialHotBoard(){

        Log.i("InitialHotBoard", "check!!");
        /*
        hotBoards.clear();
        hotBoardAdapter = new HotBoardAdapter(getActivity(), hotBoards);
        list_hot.setLayoutManager(new LinearLayoutManager(getContext()));
        list_hot.setAdapter(hotBoardAdapter);

         */
        hot_board.clear();
        hot3Adapter = new Hot3Adapter(hot_board, hotBoards);
        hot3pager.setAdapter(hot3Adapter);

        hot3Adapter.setOnItemClickListener((holder, v, position) -> {

            Log.i("hot3Adapter", "hot3Adapter");

            BoardModel boardModel = hotBoards.get(position);

            int views = Integer.valueOf(boardModel.getViews()) + 1;
            boardModel.setViews(String.valueOf(views));

            //
            boardModel.setArray_position(String.valueOf(position));

            Map<String, Object> map = new HashMap<>();
            map.put("views", views);

            // total_comm
            cr_total_item.document(boardModel.getBoard_index()).update(map);

            // user_comm
            db.collection("Users").document(boardModel.getUid())
                    .collection("Comm").document(boardModel.getWriter_index())
                    .update(map);

            Intent intent = new Intent(getContext(), ComPickBoardActivity.class);
            intent.putExtra("BoardModel", boardModel);
            startActivity(intent);
        });

        db.collection("Community").document("board")
                .collection("item")
                .orderBy("up_count", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnCompleteListener(task -> {

                    Log.i("taskSize", String.valueOf(task.getResult().size()));

                    int[] img_src = new int[3];
                    img_src[0] = R.drawable.first;
                    img_src[1] = R.drawable.second;
                    img_src[2] = R.drawable.third;
                    int count = 0;

                    ArrayList<Map> arrayList = new ArrayList<>();

                    if(errorCount == 3) return;

                    for(QueryDocumentSnapshot document : task.getResult()){
                        arrayList.add(document.getData());
                        errorCount++;
                    }

                    for(Map map : arrayList){

                        //
                        boardData = new BoardData();

                        // 제목, 댓글 수 , 추천 수
                        boardData.setImg_src(img_src[count]);
                        boardData.setTitle(map.get("title").toString());
                        boardData.setComment(Integer.valueOf(map.get("comment_count").toString()));
                        boardData.setUp(Integer.valueOf(map.get("up_count").toString()));
                        // boardData.setName(map.get("uid").toString());
                        hot_board.add(boardData);
                        count++;

                        boardModel = new BoardModel();

                        timeValue = new TimeValue(map.get("date").toString(), (map.get("time").toString()));
                        String time_value = timeValue.getTimeValue();

                        // TimeValue
                        boardModel.setTimeValue(time_value);
                        // 사진
                        boardModel.setImg(map.get("img").toString());
                        // 제목
                        boardModel.setTitle(map.get("title").toString());
                        // Index
                        boardModel.setBoard_index(map.get("index").toString());
                        // UID
                        boardModel.setUid(map.get("uid").toString());
                        // 조회수
                        boardModel.setViews(map.get("views").toString());
                        // 작성자의 index
                        boardModel.setWriter_index(map.get("writer_index").toString());
                        // 댓글 수
                        boardModel.setComment_count(map.get("comment_count").toString());
                        // 댓글 마지막 index
                        boardModel.setComment_final_index(map.get("comment_final_index").toString());
                        //
                        boardModel.setUp_count(map.get("up_count").toString());
                        hotBoards.add(boardModel);
                    }

                    hot3Adapter.notifyDataSetChanged();
                });
    }

    public void db_getData(){

        Log.i("Start db_getData!!", "ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");

        DocumentReference board = db.collection("Community")
                .document("board");

        board.collection("item")
                .whereLessThanOrEqualTo("index", index)
                .orderBy("index", Query.Direction.DESCENDING)
                .limit(limit+1)
                .get()
                .addOnCompleteListener(task1 -> {
                    today_date.setNow();
                    int task_size = task1.getResult().size();
                    boolean return_check = false;

                    if(task_size == limit+1) return_check = true;

                    Log.i("task_size", String.valueOf(task_size));

                    if(task_size == 0) {
                        Log.i("INVISIBLE", "INVISIBLE");
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }

                    ArrayList<Map> boardList = new ArrayList<>();

                    for (QueryDocumentSnapshot qds : task1.getResult()) {
                        boardList.add(qds.getData());
                    }

                    for(Map<String, Object> map : boardList){
                        // Log.i("map", map.get("index").toString());

                        boardModel = new BoardModel();

                        index = Integer.valueOf(map.get("index").toString());
                        // Log.i("index", String.valueOf(index));
                        // Log.i("count", String.valueOf(count));

                        // 더 게시물이 있으니 여기서 멈춤
                        if(return_check && count == limit){
                            adapter = new BoardAdapter(getActivity(), dataArrayList);
                            recyclerView.setAdapter(adapter);
                            return;
                        }

                        timeValue = new TimeValue(map.get("date").toString(), (map.get("time").toString()));
                        String time_value = timeValue.getTimeValue();

                        // TimeValue
                        boardModel.setTimeValue(time_value);
                        // 사진
                        boardModel.setImg(map.get("img").toString());
                        // 제목
                        boardModel.setTitle(map.get("title").toString());
                        // Index
                        boardModel.setBoard_index(map.get("index").toString());
                        // UID
                        boardModel.setUid(map.get("uid").toString());
                        // 조회수
                        boardModel.setViews(map.get("views").toString());
                        // 작성자의 index
                        boardModel.setWriter_index(map.get("writer_index").toString());
                        // 댓글 수
                        boardModel.setComment_count(map.get("comment_count").toString());
                        // 댓글 마지막 index
                        boardModel.setComment_final_index(map.get("comment_final_index").toString());
                        //
                        boardModel.setUp_count(map.get("up_count").toString());

                        dataArrayList.add(boardModel);
                        count++;

                        adapter.notifyItemInserted(dataArrayList.size());
                    }
                    // 끝맺음 , 더이상 게시물이 없음
                    index = 0;
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }

    // 스크롤시 업데이트 할때 사용
    public void OnRefresh(){

        index = Integer.valueOf(Sub_bundle.getInstance().getTotal_comm_index());
        count = 0;
        errorCount = 0;
        Log.i("index", String.valueOf(index));

        dataArrayList.clear();
        adapter = new BoardAdapter(getActivity(),dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        InitialHotBoard();
        db_getData();
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i("FragmentLifeCycle", "HomeFragment onActivityCreated!!");
    }

    @Override
    public void onResume() {
        super.onResume();

        // pickBoardActivity 에서 넘어왔는지 확인
        // pickBoard 에서 변경된 데이터를 업데이트 해줌.
        if(ScreenChangeDetect.getInstance().isPickBoardToComHome()){

            Log.i("ScreenChangeDetect", "check!!");

            boardModel = ScreenChangeDetect.getInstance().getBoardModel();
            Log.i("boardModel_position()", boardModel.getArray_position());

            if(boardModel.getArray_position() == null) {
                Log.i("String 초기화 x", "check!!");
                return;
            }

            if(ScreenChangeDetect.getInstance().isRemoveBoard()){
                Log.i("isRemoveBoard", "check!!");

                dataArrayList.remove(Integer.valueOf(boardModel.getArray_position()));
                ScreenChangeDetect.getInstance().setRemoveBoard(false);

                adapter.notifyItemRemoved(Integer.valueOf(boardModel.getArray_position()));
                adapter.notifyItemRangeChanged(Integer.valueOf(boardModel.getArray_position()), dataArrayList.size());

                ScreenChangeDetect.getInstance().setPickBoardToComHome(false);
                return;
            }
            else {
                Log.i("isRemoveBoard", "false!!");
                dataArrayList.set(Integer.valueOf(boardModel.getArray_position()),
                        boardModel);
            }

            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            ScreenChangeDetect.getInstance().setPickBoardToComHome(false);
        }
        // UploadBoard 에서 왔는지 체크
        else if(ScreenChangeDetect.getInstance().isUploadBoardToComHome()){

            boardModel = ScreenChangeDetect.getInstance().getBoardModel();
            Log.i("boardModel_title()", boardModel.getTitle());

            dataArrayList.add(0, boardModel);
            adapter.notifyItemInserted(0);

            ScreenChangeDetect.getInstance().setUploadBoardToComHome(false);
        }
        Log.i("FragmentLifeCycle", "HomeFragment onResume!!");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.i("FragmentLifeCycle", "HomeFragment onPause!!");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i("FragmentLifeCycle", "HomeFragment onStop!!");
    }

}
