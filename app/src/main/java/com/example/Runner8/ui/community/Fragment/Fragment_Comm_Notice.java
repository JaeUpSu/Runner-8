package com.example.Runner8.ui.community.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.Runner8.R;
import com.example.Runner8.SingleTon.ScreenChangeDetect;
import com.example.Runner8.ui.community.Adapter.Notice.NoticeAdapter;
import com.example.Runner8.ui.community.Adapter.Notice.model.NoticeModel;
import com.example.Runner8.ui.community.TimeValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class Fragment_Comm_Notice extends Fragment {

    //
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    //
    ArrayList<NoticeModel> noticeModels = new ArrayList<>();

    // Class
    NoticeModel noticeModel;
    NoticeAdapter noticeAdapter;
    TimeValue timeValue;

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public static Fragment_Comm_Notice newInstance(int num){
        Fragment_Comm_Notice fragment = new Fragment_Comm_Notice();
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
        return inflater.inflate(R.layout.fragment_comm_notice,container,false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //
        recyclerView = view.findViewById(R.id.notice_recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.notice_swipe_refresh);


        //
        GetNoticeBoard();

        //
        swipeRefreshLayout.setOnRefreshListener(() -> {
            GetNoticeBoard();
            swipeRefreshLayout.setRefreshing(false);
        });

    }
    public void GetNoticeBoard(){

        noticeAdapter = new NoticeAdapter(getActivity(), noticeModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(noticeAdapter);

        DocumentReference dr_notice = db.collection("Community").document("noticeBoard");

        dr_notice.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();

            String final_index = document.get("final_index").toString();

            dr_notice.collection("item")
                    .whereLessThanOrEqualTo("index", Integer.valueOf(final_index))
                    .orderBy("index", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task1 -> {

                        ArrayList<Map> boardList = new ArrayList<>();

                        for(QueryDocumentSnapshot data : task1.getResult()){
                            boardList.add(data.getData());
                        }

                        for(Map<String, Object> map : boardList){
                            noticeModel = new NoticeModel();

                            timeValue = new TimeValue(map.get("date").toString(), (map.get("time").toString()));
                            String time_value = timeValue.getTimeValue();

                            noticeModel.setIndex(map.get("index").toString());
                            noticeModel.setTitle(map.get("title").toString());
                            noticeModel.setContent(map.get("content").toString());
                            noticeModel.setViews(map.get("views").toString());
                            noticeModel.setTimeValue(time_value);
                            noticeModels.add(noticeModel);
                            noticeAdapter.notifyItemInserted(0);
                        }
                    });
        });
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i("FragmentLifeCycle", "HomeFragment onActivityCreated!!");
    }

    @Override
    public void onResume() {
        super.onResume();

        if(ScreenChangeDetect.getInstance().isNoticePickBoardToNotice()){
            noticeModel = ScreenChangeDetect.getInstance().getNoticeModel();

            Log.i("isRemoveBoard", "false!!");
            noticeModels.set(Integer.valueOf(noticeModel.getArray_position()),
                    noticeModel);
            noticeAdapter.notifyItemChanged(Integer.valueOf(noticeModel.getArray_position()));
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
