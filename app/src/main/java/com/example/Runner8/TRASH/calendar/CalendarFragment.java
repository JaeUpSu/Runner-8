package com.example.Runner8.TRASH.calendar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.example.Runner8.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CalendarFragment extends Fragment {

    private CalendarViewModel calendarViewModel;
    private CalendarView calendarView;
    private FloatingActionButton addButton;

    public selectDate Date;

    MemoAdapter memoAdapter = new MemoAdapter();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Memo> memoArrayList = new ArrayList<>();

    public CalendarFragment(){}

    // LayoutInflater : 레이아웃 XML 파일을 해당 View 객체로 인스턴스화 합니다.
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);

        // inflate : xml 에 씌여져 있는 view 의 정의를 실제 view 객체로 만드는 역할.
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        // 객체 설정
        calendarView = root.findViewById(R.id.calendarView);
        addButton = root.findViewById(R.id.add_memo);

        // Hide Button
        addButton.setEnabled(false);
        addButton.setVisibility(View.INVISIBLE);

        // (Firebase) 사용자 정보 얻기 위한 객체
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //
        //
        Drawable drawable = getResources().getDrawable(R.drawable.arrow_left);
        Drawable drawable1 = getResources().getDrawable(R.drawable.arrow_right);
        calendarView.setForwardButtonImage(drawable1);
        calendarView.setPreviousButtonImage(drawable);

        // listVew
        ListView listview = root.findViewById(R.id.listview);
        List<String> listMemo = new ArrayList<>();

        // 스티커
        List<EventDay> events = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_MONTH, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        events.add(new EventDay(calendar, R.drawable.traffic_light3, Color.parseColor("#228B22")));

        calendarView.setEvents(events);

        // 날짜 클릭 시 이벤트
        calendarView.setOnDayClickListener(eventDay -> {

            Calendar calendar1 = eventDay.getCalendar();
            String date = calendar1.getTime().toString();
            Log.i("Date",date.toString());
            Date = new selectDate(date.split("\\s"));

            Log.i("Date",Date.getDate());

            // words[0] : 요일
            // words[1] : month
            // words[2] : day
            // words[3] : time
            // words[4] : standard korea clock
            // words[5] : year

            db.collection("Users").document(user.getUid())
                    .collection("Calendar").document(Date.getDate())
                    .get()
                    .addOnCompleteListener(task -> {
                        // Document 정보 가져오기
                        DocumentSnapshot document = task.getResult();

                        listMemo.clear();
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext()
                                ,android.R.layout.simple_list_item_1, listMemo);

                        // document 정보가 존재하면 아래 실행
                        if(document.exists()){
                            List list = (List) document.getData().get("list");

                            if(list.size() != 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    HashMap map = (HashMap) list.get(i);
                                    listMemo.add(map.get("title").toString());
                                }
                            }
                        }
                        listview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    });

            // Show Button
            addButton.setEnabled(true);
            addButton.setVisibility(View.VISIBLE);
        });

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MemoActivity.class);
            intent.putExtra("ClickDate", Date);
            startActivity(intent);
        });
        return root;
    }

}

