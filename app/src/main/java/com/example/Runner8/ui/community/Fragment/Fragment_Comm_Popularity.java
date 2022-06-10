package com.example.Runner8.ui.community.Fragment;

import android.content.Context;
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

import com.example.Runner8.R;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.example.Runner8.ui.Graph.Today_Date;
import com.example.Runner8.ui.community.Adapter.Board.BoardAdapter;
import com.example.Runner8.ui.community.Adapter.Board.model.BoardModel;
import com.example.Runner8.ui.community.TimeValue;
import com.example.Runner8.ui.community.singleTon.TotalCounts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class Fragment_Comm_Popularity extends Fragment {

    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    SwipeRefreshLayout swipe_refresh_layout;

    //

    ArrayList<BoardModel> dataArrayList = new ArrayList<>();
    BoardAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // Class
    Today_Date today_date = new Today_Date();
    BoardModel boardModel;
    TimeValue timeValue;

    // Valuable
    ArrayList<Integer> array_current_views = new ArrayList<>();
    int page = 1, limit = 10, current_views,
            index, count = 0, board_total_count = 0, EqualViewsCounting = 0, EqualViewCount = 0;

    public static Fragment_Comm_Popularity newInstance(int num){
        Fragment_Comm_Popularity fragment = new Fragment_Comm_Popularity();
        Bundle args = new Bundle();
        args.putInt("num",num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comm_popularity,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("Frag_Com_Popularity", " onViewCreated !!");


        nestedScrollView = view.findViewById(R.id.popular_scroll_view);
        recyclerView = view.findViewById(R.id.popular_recycler_list);
        progressBar = view.findViewById(R.id.popular_progress_bar);
        swipe_refresh_layout =  view.findViewById(R.id.popular_swipe_refresh);


        swipe_refresh_layout.setOnRefreshListener(() -> {
            OnRefresh();
            swipe_refresh_layout.setRefreshing(false);
        });

        // 초기 출력
        OnRefresh();

        /*
        swipe_refresh_layout.setOnRefreshListener(() -> {
            OnRefresh();
            swipe_refresh_layout.setRefreshing(false);
        });

        nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (scrollY == nestedScrollView.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())
            {
                if(current_views != -1) {
                    count = 0;
                    page++;
                    progressBar.setVisibility(View.VISIBLE);
                    getData();
                }
            }
        });

         */
    }

    // 일정 좋아요 수가 넘는 게시물들을 가져오는 코드
    public void getData(){
        db.collection("Community").document("board")
                .collection("item")
                .whereGreaterThanOrEqualTo("up_count", 2)
                .get()
                .addOnCompleteListener(task -> {

                    today_date.setNow();

                    ArrayList<Map> boardList = new ArrayList<>();

                    for (QueryDocumentSnapshot qds : task.getResult()) {
                        boardList.add(qds.getData());
                    }

                    for(Map map : boardList){
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

                        dataArrayList.add(boardModel);
                    }
                    adapter = new BoardAdapter(getActivity(), dataArrayList);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.INVISIBLE);
                });
    }

    // 조회수 순서대로 정렬해서 limit 만큼 계속 가져오는 코드
    public void db_getData(){

        Query getBoard;
        DocumentReference board = db.collection("Community")
                .document("board");

        if(page == 1)
            getBoard = board.collection("item")
                    .orderBy("views", Query.Direction.DESCENDING)
                    .limit(limit+ EqualViewsCounting + 1);
        else
            getBoard = board.collection("item")
                    .whereLessThanOrEqualTo("views", array_current_views.get(array_current_views.size() - 1))
                    .orderBy("views", Query.Direction.DESCENDING)
                    .limit(limit+ EqualViewsCounting + 1);

        getBoard.get().addOnCompleteListener(task1 -> {
            today_date.setNow();
            int task_size = task1.getResult().size();
            int reduplicationCount = 0;
            boolean return_check = false;

            if (task_size == limit + EqualViewsCounting + 1) return_check = true;

            Log.i("task_size", (task_size) + " return check : " + (return_check));

            if (task_size == EqualViewsCounting) {
                Log.i("INVISIBLE", "INVISIBLE");
                progressBar.setVisibility(View.INVISIBLE);
                return;
            }

            ArrayList<Map> boardList = new ArrayList<>();

            for (QueryDocumentSnapshot qds : task1.getResult()) {
                boardList.add(qds.getData());
            }

            // 다시 카운팅 해야 됌
            EqualViewsCounting = 0;

            for (Map<String, Object> map : boardList) {
                // Log.i("map", map.get("index").toString());

                Log.i("EqualViewsCounting", String.valueOf(EqualViewsCounting));

                boardModel = new BoardModel();
                current_views = Integer.valueOf(map.get("views").toString());
                array_current_views.add(current_views);

                Log.i("array_current_views", "arraySize : " + (array_current_views.size()));
                // EqualViewsCounting
                if (array_current_views.size() != 1) {
                    Log.i("array_current_views",  " prev : " + array_current_views.get(array_current_views.size() - 2) +
                            "  " + "current : " + current_views);
                    if (array_current_views.get(array_current_views.size() - 2) == current_views)
                        EqualViewsCounting++;
                    else
                        EqualViewsCounting = 0;
                }

                // 중복된 게시물을 가져오기 때문에 바로 넘겨줘야 한다.
                if (EqualViewCount != 0) {
                    if (reduplicationCount < EqualViewCount) {
                        reduplicationCount++;
                        count++;
                        continue;
                    } else if (reduplicationCount == EqualViewCount)
                        EqualViewCount = 0;
                }

                index = Integer.valueOf(map.get("index").toString());
                // Log.i("index", String.valueOf(index));
                Log.i("count", String.valueOf(count));

                // 더 게시물이 있으니 여기서 멈춤
                if (return_check && count == limit) {
                    Log.i("return_check", "check!!!!!");
                    EqualViewCount = EqualViewsCounting;
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

                Log.i("qds.get(\"TimeValue\")", boardModel.getTimeValue());
                Log.i("qds.get(\"title\")", boardModel.getTitle());
                Log.i("data.get(\"views\")", boardModel.getViews());
                Log.i("comment_final_index", boardModel.getComment_final_index());

                dataArrayList.add(boardModel);
                Log.i("dataArrayList.add", "dataArrayList.add" + " " + (current_views));
                count++;
                board_total_count++;

                Log.i("COUNTING", "count :" + (count) + "board_total_count" + (board_total_count));

                Log.i("cutLine", "ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ\n");
                Log.i("cutLine", "ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
                // 끝맺음 , 더이상 게시물이 없음
                if (board_total_count ==
                        Integer.valueOf(TotalCounts.getInstance().getBoard_count())) {
                    Log.i("끝맺음", "check!!!");
                    current_views = -1;
                    page++;
                    adapter = new BoardAdapter(getActivity(), dataArrayList);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
            }
        });
    }

    // 스크롤시 업데이트 할때 사용
    public void OnRefresh(){

        board_total_count = 0;
        count = 0;
        page = 1;
        array_current_views.clear();

        index = Integer.valueOf(Sub_bundle.getInstance().getTotal_comm_index());

        dataArrayList.clear();

        adapter = new BoardAdapter(getActivity(),dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        getData();
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i("FragmentLifeCycle", "HomeFragment onActivityCreated!!");
    }

    @Override
    public void onResume() {
        super.onResume();

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
