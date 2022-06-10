package com.example.Runner8.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.MainActivity;
import com.example.Runner8.R;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.example.Runner8.TRASH.MapAlarmService;
import com.example.Runner8.TestActivity;
import com.example.Runner8.ui.Graph.Today_Date;
import com.example.Runner8.ui.F_H.health.Adapter.Model.HealthData;
import com.example.Runner8.ui.F_H.health.SingleTon.HealthSingleTon;
import com.example.Runner8.ui.map.Adapter.MapAdapter;
import com.example.Runner8.ui.map.Adapter.Model.MapData;
import com.example.Runner8.ui.map.Calcurate.Calculator;
import com.example.Runner8.ui.map.Guide.GuideSoundDialogue;
import com.example.Runner8.ui.map.Kalman.KalmanFilter;
import com.example.Runner8.ui.map.Service.BackgroundLocationUpdateService;
import com.example.Runner8.ui.map.SingleTon.MapSingleTon;
import com.example.Runner8.ui.map.UFSRecord.MapUFSRecordActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.jvm.internal.Intrinsics;
import ted.gun0912.clustering.clustering.TedClusterItem;
import ted.gun0912.clustering.naver.TedNaverClustering;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.POWER_SERVICE;
import static android.content.Context.WIFI_SERVICE;
import static android.speech.tts.TextToSpeech.ERROR;

public class Fragment_Map extends Fragment
        implements View.OnClickListener, OnMapReadyCallback {

    // Naver Map

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int BACKGROUND_PERMISSION_REQUEST_CODE = 1002;
    private String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private String BACKGROUND_PERMISSIONS = Manifest.permission.ACCESS_BACKGROUND_LOCATION;

    LocationRequest locationRequest;

    //
    TedNaverClustering tedNaverClustering;
    TedClusterItem pick_tedClusterItem;
    ArrayList<JavaItem> javaItems = new ArrayList<>();

    // 사용자의 위치를 받아오기 위한 request_code
    private LocationManager locationManager;                    // activity 에서 직접 사용자의 위치를 수신하기 위한 객체
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private ArrayList<Marker> GpsMarkers = new ArrayList<>();
    private ArrayList<Marker> GpsMarkers2 = new ArrayList<>();
    private ArrayList<Marker> quarter_markers = new ArrayList<>();

    private Marker my_marker, ufs_marker;
    private Marker ufs_start_marker = new Marker();
    private Marker ufs_arrive_marker = new Marker();

    private UiSettings uiSettings;
    private PathOverlay path = new PathOverlay();
    private PathOverlay kalman_path = new PathOverlay();
    private PathOverlay ufs_path = new PathOverlay();
    private PathOverlay clickPath = new PathOverlay();
    private CameraPosition cameraPosition;
    private LocationTrackingMode locationTrackingMode;

    List<Marker> markers = new ArrayList<>();

    PathOverlay registeredPath = new PathOverlay();
    final Boolean[] myBool = {false};


    Intent serviceIntent;

    private ArrayList<Double> kalman_ufs_lat = new ArrayList<>();
    private ArrayList<Double> kalman_ufs_long = new ArrayList<>();

    private ArrayList<LatLng> ufs_latLngs;
    private ArrayList<LatLng> clickLists = new ArrayList<>();
    private ArrayList<Marker> markerCourseList = new ArrayList<>();

    // Media
    ////////////////////////////////////////////////////////////////////////////////////////
    private MediaPlayer mediaPlayer;

    // UFS
    ////////////////////////////////////////////////////////////////////////////////////////

    private ArrayList<Double> ufs_lat = new ArrayList<>();
    private ArrayList<Double> ufs_long = new ArrayList<>();
    private ArrayList<Integer> ufs_time = new ArrayList<>();

    ArrayList<UfsOther> ufsOthers = new ArrayList<>();
    ArrayList<OverlayImage> markerIcons = new ArrayList<>();

    UfsOther ufsOther = new UfsOther();

    ////////////////////////////////////////////////////////////////////////////////////////
    private TimerTask timer_UFSTask, timer_countDownTask, timer_courseTask;
    private Timer timer = new Timer();
    private LatLng current_loc, arrive_loc;

    private FusedLocationProviderClient fusedLocationProviderClient;

    //
    Location prev_location;

    //
    WifiManager wifiManager;

    ToggleButton filter_all, filter_distance, filter_kcal, btn_coursePickItem, filter_like;
    Button create_line, ufs, go;
    ToggleButton filter_ran, filter_merge, like_button;
    ImageButton  record_button;
    RecyclerView recyclerView;
    EditText et_search;
    MapAdapter adapter;
    ArrayList<MapData> dataArrayList = new ArrayList<>();
    TextView txtDistMap, txtTimeMap, txtKcalMap, txtPickCourse,
            txtPickKM, txtPickTIME, txtPickKCAL, tv_timer, altitude, kalman_altitude,
            count_down, tv_like_count, tv_solo, tv_many, tv_me, provider_check;
    LinearLayout layout_ItemPick, linearMap;
    FrameLayout recycler_flame, run_data_frame, running_timer, countDown_frame;
    ImageView draw_btn;

    int markerTag = 0, check_count = 0, index_count = 0;
    int startCourse_sec = 0, startCourse_minute = 0, startCourse_hour = 0;
    int my_courses_size = 0, like = 0, qua_index = 0, quarter_check_count = 0, ted_pick_point = 0, ran_like_count = 0;
    long timer_count = 0;
    double total_dist = 0;
    boolean ran_like_check = false;
    boolean run_timer_check = false;
    boolean drawFlag = false;
    boolean drawer_marker_flag = false;
    boolean clearFlag = false;
    boolean ufs_flag = false;
    boolean MY_checked = false;
    boolean solo_check = true;
    boolean many_check = false;
    boolean me_check = false;
    boolean register_course_check = true;
    boolean permission_check = false;
    boolean first_point_check = false;
    boolean start_count_flag = false;
    boolean first_check = true;
    String timeFormat = "";
    String pick_course_id;
    String totally_course_id;

    //
    ArrayList<String> sampling_altitude = new ArrayList<>();
    ArrayList<String> sampling_kalman_altitude = new ArrayList<>();
    Button test;

    // start X,Y + end X,Y   + startName   + endName

    String home;
    ArrayList<String> likeLists;

    Toolbar toolbar;
    ContextMenuDialogFragment contextMenuDialogFragment;

    // CLASS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    Today_Date today_date = new Today_Date();
    Filtering filtering = new Filtering();
    MapData pick_item_mapData;
    Calculator calculator = new Calculator();

    // GUIDE
    ////////////////////////////////////////////////////////////////////////////////////////////////


    TextToSpeech textToSpeech;
    boolean guide_check = true;


    ////////////////////////////////////////////////////////////////////////////////////////////////

    BottomNavigationView bottomNavigationView;

    //
    SharedPreferences sf;
    SharedPreferences.Editor editor;
    // FIREBASE
    ////////////////////////////////////////////////////////////////////////////////////////////////

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    CollectionReference cr_map = db.collection("Users").document(user.getUid())
            .collection("Map");

    DocumentReference dr_myCourses = db.collection("Users").document(user.getUid())
            .collection("Map").document("MyCourses");

    CollectionReference cr_totally_mapCourses = db.collection("Map").document("User_Courses")
            .collection("Courses");

    DocumentReference dr_totally_map = db.collection("Map").document("User_Courses");

    DocumentReference dr_myMap_like = db.collection("Users").document(user.getUid())
            .collection("Map").document("Like");

    DocumentReference dr_mapTOTAL = db.collection("Users").document(user.getUid())
            .collection("Map").document("TOTAL");

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("onCreateView", "onCreateView!!");
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.mapmain, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstancdState) {
        super.onViewCreated(view, savedInstancdState);
        Log.i("onViewCreated", "onViewCreated!!");

        toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolbar);

        initToolbar();
        initMenuFragment();

        home = "";
        likeLists = new ArrayList<>();

        et_search = view.findViewById(R.id.et_mapsearch);
        filter_all = view.findViewById(R.id.filter_all);
        filter_kcal = view.findViewById(R.id.filter_kcal);
        filter_distance = view.findViewById(R.id.filter_distance);
        recyclerView = view.findViewById(R.id.test_Course);
        create_line = view.findViewById(R.id.create_line);
        draw_btn = view.findViewById(R.id.draw_btn);
        recycler_flame = view.findViewById(R.id.recycler_flame);
        run_data_frame = view.findViewById(R.id.run_data_frame);
        tv_timer = view.findViewById(R.id.tv_timer);
        running_timer = view.findViewById(R.id.running_timer);
        countDown_frame = view.findViewById(R.id.countDown_frame);
        count_down = view.findViewById(R.id.count_down);
        go = view.findViewById(R.id.go);
        linearMap = view.findViewById(R.id.linearMap);
        record_button = view.findViewById(R.id.record_button);
        filter_ran = view.findViewById(R.id.filter_ran);
        filter_merge = view.findViewById(R.id.filter_merge);
        like_button = view.findViewById(R.id.like_button);
        tv_like_count = view.findViewById(R.id.tv_like_count);
        tv_solo = view.findViewById(R.id.tv_solo);
        tv_many = view.findViewById(R.id.tv_many);
        tv_me = view.findViewById(R.id.tv_me);
        filter_like = view.findViewById(R.id.filter_like);

        //
        provider_check = view.findViewById(R.id.provider_check);

        //
        altitude = view.findViewById(R.id.altitude);
        kalman_altitude = view.findViewById(R.id.kalman_altitude);

        //
        txtDistMap = view.findViewById(R.id.mapDistTxt);
        txtTimeMap = view.findViewById(R.id.mapTimeTxt);
        txtKcalMap = view.findViewById(R.id.mapKcalTxt);
        // btn_regist = view.findViewById(R.id.btn_regist);
        layout_ItemPick = view.findViewById(R.id.layout_ItemPick);
        btn_coursePickItem = view.findViewById(R.id.btn_pickcousre);
        txtPickCourse = view.findViewById(R.id.tv_pickcourse);
        txtPickKM = view.findViewById(R.id.tv_courseDist);
        txtPickTIME = view.findViewById(R.id.tv_courseTime);
        txtPickKCAL = view.findViewById(R.id.tv_courseKcal);

        //
        test = view.findViewById(R.id.test);

        //
        markerIcons.add(OverlayImage.fromResource(R.drawable.marker_user_red));
        markerIcons.add(OverlayImage.fromResource(R.drawable.marker_user_blue));
        markerIcons.add(OverlayImage.fromResource(R.drawable.marker_user_yellow));

        //
        locationRequest = LocationRequest.create();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(3000);
        locationRequest.setMaxWaitTime(6000);

        //
        //
        sf = getActivity().getSharedPreferences("isFirstPermissionCheck", MODE_PRIVATE);
        editor = sf.edit();
        editor.putBoolean("first_check", true);
        editor.commit();

        //
        Courses.getInstance().setActivity(getActivity());

        // set SingleTon
        setSingleTon();

        // Adapter
        adapter = new MapAdapter(dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        MapData myList = new MapData();
        myList.setCourse_name("RUN MY");
        dataArrayList.add(0, myList);
        adapter.notifyItemInserted(0);

        // Default 모두 보기
        getAllCourses();

        // guide
        textToSpeech = new TextToSpeech(getContext(), status -> {
            if (status != ERROR) {
                textToSpeech.setLanguage(Locale.KOREA);
            }
        });
        Courses.getInstance().setTextToSpeech(textToSpeech);

        adapter.setOnItemClickListener((holder, v, position) -> {

            Log.i("setOnItemClickListener", "setOnItemClickListener" + "");
            pick_item_mapData = adapter.getData(position);

            Log.i("position", position + "");
            // My Courses 인지 Filtering 인지 구분해야함.
            if (position > 0) {

                MapSingleTon.getInstance().setFinish_lat(Double.valueOf(pick_item_mapData.getFinish_lat()));
                MapSingleTon.getInstance().setFinish_long(Double.valueOf(pick_item_mapData.getFinish_long()));
                //
                pick_tedClusterItem = javaItems.get(position - 1);
                tedNaverClustering.removeItem(pick_tedClusterItem);

                //
                holder.getButton().setChecked(false);
                course_pick_event(position);
            } else {

                Log.i("setOnItemClickListener", MY_checked + "");
                myBool[0] = !myBool[0];
                // my courses 리스트
                // My
                if (myBool[0]) {

                    MY_checked = true;

                    pick_item_mapData.setCourse_name("RUN ALL");
                    holder.getTextView().setText("RUN ALL");
                    if (!(filtering.getFilter_name().equals("ran") || filtering.getFilter_name().equals("like"))) {

                        removeCourses(dataArrayList.size());

                        // ALL -> MY 선택된 필터링으로 가져옴.
                        getMyCoursesFilteringData(filtering.getFilter_name());
                    }
                }
                // All
                else {

                    //
                    MY_checked = false;

                    pick_item_mapData.setCourse_name("RUN MY");
                    holder.getTextView().setText("RUN MY");

                    if (!(filtering.getFilter_name().equals("ran") || filtering.getFilter_name().equals("like"))) {
                        removeCourses(dataArrayList.size());
                        // 필터링 해서 리스트에 추가됐던걸 기억해서 지운다음 추가해야됌

                        // 무슨 필터링을 했는지
                        String filter_name = filtering.getFilter_name();

                        // 해당 필터링 했을 때 리스트 추가
                        getFilteringData(filter_name);
                    }

                }
            }
        });

        // 삭제 기능
        adapter.setLongClick_listener((holder, v, position) -> {

            Log.i("position", position + " ");
            Log.i("setLongClick_listener", "setLongClick_listener");

            MapData LongClick_mapData = adapter.getData(position);
            // My Course 데이터이기 때문에 myCouorse index를 가져옴
            String course_index = LongClick_mapData.getIndex();

            Log.i("course_index", course_index);

            // My course 보는 상태에서
            if (myBool[0]) {
                // My course 데이터들만 Long 클릭 했을 때 가능해야 함
                if (position > 0 && position <= my_courses_size) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setTitle("확인")
                            .setMessage(" 코스를 삭제하시겠습니까? 해당 코스의 FOLLOW 데이터도 모두 삭제됩니다.")
                            .setNegativeButton("예", (dialog, which) -> {

                                MapSingleTon.getInstance().MinusMy_total_count();
                                MapSingleTon.getInstance().MinusTotally_total_count();

                                int my_total_count = MapSingleTon.getInstance().getMy_total_count();
                                int totally_total_count = MapSingleTon.getInstance().getTotally_total_count();

                                Map<String, Object> map_dr_myCourses = new HashMap<>();
                                map_dr_myCourses.put("total_count", my_total_count);

                                dr_myCourses.collection("courses")
                                        .whereEqualTo("index", Integer.valueOf(course_index))
                                        .get()
                                        .addOnCompleteListener(task -> {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                String totally_map_index = document.get("totally_index").toString();

                                                // 내 계정 데이터 삭제
                                                dr_myCourses.collection("courses").document(document.getId()).delete();
                                                // 전체에서 내 데이터 삭제
                                                dr_totally_map.collection("Courses").document(totally_map_index).delete();
                                            }
                                        });

                                Map<String, Object> map_dr_totallyCourses = new HashMap<>();
                                map_dr_totallyCourses.put("total_count", totally_total_count);

                                dr_totally_map.update(map_dr_totallyCourses);
                                dr_myCourses.update(map_dr_myCourses);

                                dataArrayList.remove(position);
                                adapter.notifyItemRemoved(position);

                                // singleTon
                                MapSingleTon.getInstance().deleteQuarterOfCourses(course_index);

                            })
                            .setPositiveButton("아니오", (dialog, which) -> {
                                dialog.dismiss();
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            if (!MapSingleTon.getInstance().isRunning_check()) {
                Sub_bundle.getInstance().PlusBackStackCount();

                Log.i("item.getItemId()", item.getItemId() + "\n");
                Log.i("item.getItemId()", Sub_bundle.getInstance().getBackStackCount() + "");
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        navController.navigate(R.id.nav_home);
                        break;
                    case R.id.nav_community:
                        navController.navigate(R.id.nav_community);
                        break;
                    case R.id.nav_map:
                        navController.navigate(R.id.nav_map);
                        break;
                    case R.id.nav_setting:
                        navController.navigate(R.id.nav_setting);
                        break;
                }
                return true;
            }
            Toast.makeText(getContext(), "달리기를 멈춰 주세요!!", Toast.LENGTH_SHORT).show();
            return false;
        });

        //
        setOnclickListeners();

        // MapFragment
        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // FusedLocationSource 는 런타임 권한 처리를 위해 액티비티 또는 프래그먼트를 필요로 합니다.

        // 생성자에 액티비티나 프래그먼트 객체를 전달하고 권한 요청 코드를 지정해야 합니다.
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        // 백그라운드 위치 권한 dialog..
        /*
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle("백그라운드 위치 권한을 위해 항상 허용으로 설정해주세요.")
                    .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermission();
                        }
                    });

            AlertDialog dialog = alertDialog.create();
            dialog.show();

             */

        create_line.setOnClickListener(v -> {

            // 저장할때
            if (MapSingleTon.getInstance().isRunning_check()) {

                // FOLLOW
                if (Courses.getInstance().isItem_pick_check()) {
                    // layout_ItemPick.setVisibility(View.VISIBLE);                // 선택했을떄 리스트

                    // 도착 장소에 있는지 확인
                    if (arrive_loc.distanceTo(current_loc) > 0) {

                        // FOLLOW 기준에 부합하는지 확인
                        // 확인작업
                        // 총 거리
                        double Total_Distance = Courses.getInstance().getTotal_distance();
                        double original_Distance = Double.valueOf(pick_item_mapData.getCourseDist());

                        // original_Distance 1km 마다 오차 10m 씩 가능
                        boolean error = distance_comparison(original_Distance, Total_Distance);

                        // 분기
                        if (error &&
                                Courses.getInstance().getQuarter_index().size()
                                        == Courses.getInstance().getUser_qua_index()) {

                            if (Courses.getInstance().getLocations().size() > 1) {

                                // Timer Out
                                if (get_follow_mode().equals("many")) stopOthersOfTimer();
                                else if (get_follow_mode().equals("me")) ufsOther.stopTimerTask();
                                else if (get_follow_mode().equals("solo")) stopUFSTimerTask();

                                // 기록 ( 시간, 거리, kcal)
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                        .setTitle("확인")
                                        .setMessage(" 목표 장소에 도착하셨습니다.!! ")
                                        .setNegativeButton("예", (dialog, which) -> {

                                            Log.i("pick_course_id", pick_course_id);
                                            Log.i("totally_course_id", totally_course_id);

                                            // health - run 연동
                                            // Today RUN 데이터에 추가
                                            TODAY_RUNNING_UPDATE();

                                            TOTAL_AVG_DATA_UPDATE();

                                            // TOTALMAP
                                            updateTOTALMAP(Courses.getInstance().getTotal_distance(),
                                                    calculator.kcal_result(
                                                            Integer.valueOf(calculator.time_result(
                                                                    Courses.getInstance().getTotal_distance()).split("[ ]")[0])));

                                            // FOLLOW COUNT UPDATE
                                            cr_totally_mapCourses.document(totally_course_id)
                                                    .get()
                                                    .addOnCompleteListener(task -> {
                                                        DocumentSnapshot document = task.getResult();
                                                        int follow_count = Integer.valueOf(document.get("follow_count").toString());
                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put("follow_count", follow_count + 1);
                                                        cr_totally_mapCourses.document(totally_course_id).update(map);
                                                    });

                                            CollectionReference cr_top20 =
                                                    cr_totally_mapCourses.document(totally_course_id).collection("TOP20");
                                            // 도착장소에서 제대로 눌렀을 때

                                            // Top 에 들었는지 안들었는지 확인
                                            // Top 데이터 시간 정렬 -> 가장 낮은 시간과 비교 ->
                                            // Top 에 들었다면 가장 낮은 기록 delete -> 데이터 저장
                                            cr_top20.orderBy("total_time")
                                                    .get()
                                                    .addOnCompleteListener(task -> {
                                                        // 데이터가 있을 떄
                                                        if (task.getResult().size() != 0) {
                                                            Log.i("task.isSuccessful()", "task.isSuccessful()");
                                                            if (task.getResult().size() < 20)
                                                                renewal_my_course_data(cr_top20);
                                                            else {
                                                                // Top 에 드는 지 확인
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    String total_time = document.get("total_time").toString();

                                                                    // Top 에 들면 원래 데이터 제거
                                                                    if (Integer.valueOf(total_time) > MapSingleTon.getInstance().getTOTAL_SEC()) {
                                                                        // 제거
                                                                        cr_top20.document(document.getId()).delete();

                                                                        // 추가
                                                                        renewal_my_course_data(cr_top20);
                                                                    }
                                                                    // Top 에 들지 못함
                                                                    else return;

                                                                    // 가장 낮은 시간만 확인하면 되므로 바로 return
                                                                    return;
                                                                }
                                                            }
                                                        }
                                                        // 데이터가 없을 때
                                                        else renewal_my_course_data(cr_top20);

                                                    });

                                            // 기본적으로 본인 기록에 남아야 함
                                            // 날짜, 시간
                                            DocumentReference dr_ran = cr_map.document("RAN");

                                            dr_ran.collection("Courses")
                                                    .whereEqualTo("index", pick_course_id)
                                                    .get()
                                                    .addOnCompleteListener(task -> {

                                                        // 기록이 원래 있다면
                                                        // 평균 시간, 기록 횟수, 기록 갱신 여부 확인 해야 함
                                                        if (task.getResult().size() != 0) {

                                                            Log.i("RECORD", "CHECK!!");

                                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                                int finish_count = Integer.valueOf(document.get("finish_count").toString());
                                                                int avg_time = Integer.valueOf(document.get("avg_time").toString());
                                                                int point_count = Integer.valueOf(document.get("point_count").toString());

                                                                avg_time = Math.round((MapSingleTon.getInstance().getTOTAL_SEC() +
                                                                        avg_time * finish_count) / (finish_count + 1));

                                                                Map<String, Object> map = new HashMap<>();
                                                                map.put("finish_count", finish_count + 1);
                                                                map.put("avg_time", avg_time);

                                                                // 기록 갱신 여부 확인
                                                                int prev_time = Integer.valueOf(document.get("time").toString());
                                                                // 갱신
                                                                if (MapSingleTon.getInstance().getTOTAL_SEC() < prev_time) {

                                                                    today_date.setNow();

                                                                    map.put("time", MapSingleTon.getInstance().getTOTAL_SEC());
                                                                    map.put("my_total_distance", Courses.getInstance().getTotal_distance());
                                                                    map.put("date", today_date.getFormat_date());
                                                                    map.put("point_count", Courses.getInstance().getLocations().size());

                                                                    CollectionReference cr_record = dr_ran.collection("Courses").document(pick_course_id)
                                                                            .collection("Record");

                                                                    // 삭제하고 할 필요 없음
                                                                    // 추가 할 데이터 set 해주고 남아있는 document 삭제 하면 됨

                                                                    // 원래 코스 라인 데이터 삭제할 필요 없이 point_count 로 해결 봄
                                                                    addMyFollowCourseData(dr_ran);
                                                                }

                                                                dr_ran.collection("Courses").document(pick_course_id).update(map);
                                                            }
                                                        }
                                                        //
                                                        else {
                                                            Log.i("NO RECORD", "CHECK!!");

                                                            Map<String, Object> map = new HashMap<>();

                                                            today_date.setNow();
                                                            // 날짜
                                                            map.put("date", today_date.getFormat_date());
                                                            // 시간 (기록)
                                                            map.put("time", MapSingleTon.getInstance().getTOTAL_SEC());
                                                            // 코스 이름
                                                            map.put("name", pick_item_mapData.getCourse_name());
                                                            // 코스 길이
                                                            map.put("main_total_distance", pick_item_mapData.getCourseDist());
                                                            // 코스 길이
                                                            map.put("my_total_distance", Courses.getInstance().getTotal_distance());
                                                            //
                                                            map.put("index", Integer.valueOf(pick_course_id));
                                                            //
                                                            map.put("avg_time", MapSingleTon.getInstance().getTOTAL_SEC());
                                                            //
                                                            map.put("finish_count", 1);
                                                            //
                                                            map.put("point_count", Courses.getInstance().getLocations().size());

                                                            dr_ran.collection("Courses").document(pick_course_id)
                                                                    .set(map);

                                                            // 코스 라인 데이터 추가
                                                            addMyFollowCourseData(dr_ran);
                                                        }
                                                    });

                                            create_line_record_cancel();
                                            create_line.setText("FOLLOW");
                                            create_line.setBackgroundResource(R.drawable.circle_board_follow);
                                            // user_course_remove();
                                            // 상대 마커
                                            if (ufs_marker != null) ufs_marker.setMap(null);

                                            running_timer.setVisibility(View.INVISIBLE);
                                            layout_ItemPick.setVisibility(View.VISIBLE);
                                            run_data_frame.setVisibility(View.INVISIBLE);
                                            recycler_flame.setVisibility(View.INVISIBLE);

                                            dialog.dismiss();
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else
                                Toast.makeText(getActivity(), "데이터가 부족합니다!!", Toast.LENGTH_SHORT).show();
                        } else {
                            // 기준에 부합하지 않았다는 안내를 해야 함
                            Toast.makeText(getActivity(), " 팔로우 기록 기준에 부합하지 않습니다. \n 기록 저장에 실패합니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        // 다른 장소에서 눌렀을 때
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                .setTitle("확인")
                                .setMessage(" 목표 장소에 도착하지 않았습니다. 중단하시겠습니까?")
                                .setNegativeButton("예", (dialog, which) -> {
                                    create_line_record_cancel();
                                    create_line.setText("FOLLOW");
                                    create_line.setBackgroundResource(R.drawable.circle_board_follow);

                                    if (get_follow_mode().equals("many")) stopOthersOfTimer();
                                    else if (get_follow_mode().equals("me"))
                                        ufsOther.stopTimerTask();
                                    else if (get_follow_mode().equals("solo")) stopUFSTimerTask();

                                    // user_course_remove();
                                    // 상대 마커
                                    if (ufs_marker != null) ufs_marker.setMap(null);

                                    running_timer.setVisibility(View.INVISIBLE);
                                    layout_ItemPick.setVisibility(View.VISIBLE);
                                    run_data_frame.setVisibility(View.INVISIBLE);
                                    recycler_flame.setVisibility(View.INVISIBLE);                 // 리사이클러뷰
                                })
                                .setPositiveButton("아니오", (dialog, which) -> {
                                    dialog.dismiss();
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
                else {
                    // 코스는 최소 1km 가 되어야 함.
                    double Total_Distance = Courses.getInstance().getTotal_distance();

                    // 총 거리는 1km 넘어야 함. (수정할 것임)
                    if (Total_Distance > 0) {

                        cr_totally_mapCourses.get().addOnCompleteListener(task -> {

                            Location start_location = Courses.getInstance().getLocations().get(0);
                            LatLng user_start_latLng = new LatLng(start_location.getLatitude(), start_location.getLongitude());

                            int size = Courses.getInstance().getLocations().size();
                            Location finish_location = Courses.getInstance().getLocations().get(size - 1);
                            LatLng user_finish_latLng = new LatLng(finish_location.getLatitude(), finish_location.getLongitude());

                            String msg = "동일한 코스가 존재합니다. 기존과 다른 코스를 만들어보세요!!";

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                double start_lat = Double.valueOf(document.get("start_point_lat").toString());
                                double start_long = Double.valueOf(document.get("start_point_long").toString());
                                double finish_lat = Double.valueOf(document.get("finish_point_lat").toString());
                                double finish_long = Double.valueOf(document.get("finish_point_long").toString());
                                double total_distance = Double.valueOf(document.get("total_distance").toString());
                                LatLng db_start_latLng = new LatLng(start_lat, start_long);
                                LatLng db_finish_latLng = new LatLng(finish_lat, finish_long);

                                if (user_start_latLng.distanceTo(db_start_latLng) < 10) {
                                    // 조건에 해당 되지 않음
                                    // 다른 장소에서 눌렀을 때
                                    Log.i("EQUAL COURSE start", "check!!");
                                } else {
                                    if (user_finish_latLng.distanceTo(db_finish_latLng) < 10) {
                                        // 조건에 해당 되지 않음
                                        Log.i("EQUAL COURSE finish", "check!!");
                                    } else {
                                        if (Math.abs(total_distance - total_dist) < 50) {

                                            int count = 0;

                                            QuarterOfCourses courses = MapSingleTon.getInstance().getQuarterOfCourse(document.getId());

                                            int quarter_size = courses.getQuarter_Lat().size();
                                            int user_quarter_size = Courses.getInstance().getRegister_quarter_locations().size();

                                            Log.i("documentID", document.getId());
                                            Log.i("quearter_size", quarter_size + "");

                                            if (quarter_size != 0 && quarter_size == user_quarter_size) {

                                                for (int i = 0; i < quarter_size; i++) {
                                                    double quarter_lat = Double.valueOf(courses.getQuarter_Lat().get(i).toString());
                                                    double quarter_long = Double.valueOf(courses.getQuarter_Long().get(i).toString());

                                                    double user_quarter_lat = Courses.getInstance().getRegister_quarter_locations().get(i).getLatitude();
                                                    double user_quarter_long = Courses.getInstance().getRegister_quarter_locations().get(i).getLongitude();

                                                    LatLng quarter_loc = new LatLng(quarter_lat, quarter_long);
                                                    LatLng user_quarter_loc = new LatLng(user_quarter_lat, user_quarter_long);

                                                    if (quarter_loc.distanceTo(user_quarter_loc) < 50)
                                                        count++;
                                                }
                                                if (quarter_size == 1) {
                                                    if (count != 1) {
                                                        Log.i("EQUAL COURSE quarter1", "check!!");
                                                        register_course_check = false;
                                                    }
                                                } else {
                                                    if (count < quarter_size / 2) {
                                                        Log.i("EQUAL COURSE quarters", "check!!");
                                                        register_course_check = false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (register_course_check) registerCourse();
                            else equalCourseGuide(msg);
                        });
                    } else {
                        String message = "코스의 거리는 최소 1km 이상이어야 합니다.";
                        equalCourseGuide(message);
                    }
                }
            }
            else {
                onCheckPermission();
                if (permission_check) {
                    //
                    Courses.getInstance().clearLocations();
                    Courses.getInstance().initialTotal_distance();

                    // Quarter clear
                    Courses.getInstance().clearQuarterly_distance();
                    Courses.getInstance().clearQuarter_locations();
                    Courses.getInstance().clearQuarter_indexes();
                    Courses.getInstance().clearQuarter_index();
                    Courses.getInstance().clearRecord_quarter_locations();
                    Courses.getInstance().clearIntervalSpeech_count();
                    quarter_check_count = 0;
                    qua_index = 0;

                    // GUIDE
                    Courses.getInstance().clearSpeech_count();
                    Courses.getInstance().setLanguageSpeech(Locale.KOREA);


                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Snackbar snackBar = Snackbar.make(getView(), "백그라운드에서 위치를 가져오기 위해  \n" +
                                        "항상 허용 권한이 필요합니다.",
                                Snackbar.LENGTH_INDEFINITE);
                        snackBar.setAction("확인", v1 -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        });
                        snackBar.show();
                    }

                    // 코스 아이템을 클릭 한 상태면 U F S
                    if (Courses.getInstance().isItem_pick_check()) {

                        // 현재 위치가 시작장소인지 확인
                        if (current_loc != null) {

                            LatLng latLng = new LatLng(ufs_lat.get(0), ufs_long.get(0));

                            // 3m 이내에 있을 시 가능
                            if (current_loc.distanceTo(latLng) > 0) {

                                // 타이머 가동
                                startCountDownTimer();

                                ufs_latLngs = new ArrayList<>();
                                ufs_latLngs.add(latLng);
                                ufs_path = new PathOverlay();

                                Log.i("First Point", latLng.latitude + "  " + latLng.longitude);

                                if (get_follow_mode().equals("many")) {
                                    Log.i("many", "manymanymanymany");

                                    // 카메라 bearing 설정


                                    // 시간에 맞춰 그리기 , UFS
                                    StartOthersOfTimerScheduler();

                                } else if (get_follow_mode().equals("me")) {
                                    ufsOther.startTimer(getActivity(), MarkerIcons.RED, timer, naverMap, textToSpeech);

                                } else if (get_follow_mode().equals("solo")) {
                                    // 코스 정보 부분 -> 타이머로 변경

                                    // ufsOther.startTimer(getActivity(), MarkerIcons.RED, timer, naverMap);
                                    // 시간에 맞춰 그리기 , UFS
                                    startUFSTimer();
                                }

                                // 코스 등록 시작
                                create_line_start();

                                // quarter get data
                                if (MY_checked) {
                                    dr_myCourses.collection("courses").document(pick_course_id)
                                            .collection("quarters")
                                            .orderBy("index")
                                            .get()
                                            .addOnCompleteListener(task -> {
                                                if (task.getResult().size() != 0)
                                                    getQuarter_locations(task);
                                            });
                                } else {
                                    cr_totally_mapCourses.document(totally_course_id)
                                            .collection("quarters")
                                            .orderBy("index")
                                            .get()
                                            .addOnCompleteListener(task -> {
                                                if (task.getResult().size() != 0)
                                                    getQuarter_locations(task);
                                            });
                                }
                            } else
                                Toast.makeText(getActivity(), "시작 위치로 가주세요!!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "위치 활성화가 안되어있습니다.!!", Toast.LENGTH_SHORT).show();
                            // Log.i("CURRENT_LOC", "위치 활성화 안되어있음");
                        }
                    }
                    // 코스 아이템 선택한게 아니면 그냥 코스 등록
                    else {
                        startCountDownTimer();
                        create_line_start();
                    }
                }
            }
        });

        test.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TestActivity.class);
            startActivity(intent);
        });
    }

    public void onCheckPermission() {

        boolean backgroundLocationGranted = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                != PackageManager.PERMISSION_GRANTED;

        boolean isFirstCheck = sf.getBoolean("first_check", true);

        Log.i("isFirstCheck", isFirstCheck + "");

        if (hasLocationPermissions()) {
            Log.i("onCheckPermission", "권한 설정 필요");
            /*
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(getContext());
            localBuilder.setTitle("권한 설정")
                    .setMessage("백그라운드 위치 권한을 위해 항상 허용으로 설정해주세요.")
                    .setPositiveButton("권한 설정하러 가기", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
                            try {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:" + getActivity().getPackageName()));
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                                Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                startActivity(intent);
                            }
                        }})
                    .setNegativeButton("취소하기", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                            Toast.makeText(getContext(),"RUN 기능이 제한됩니다.",Toast.LENGTH_SHORT).show();
                        }})
                    .create()
                    .show();

             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Log.i("onCheckPermission", "shouldShowRequestPermissionRationale");
                Snackbar snackBar = Snackbar.make(getView(), "위치를 가져오기 위해서 위치 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE);
                first_check = false;
                snackBar.setAction("권한 승인", v -> {
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
                });
                snackBar.show();
            } else {
                if (first_check) {
                    Log.i("isFirstCheck", "true");
                    first_check = false;
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    Snackbar snackBar = Snackbar.make(getView(), "위치를 가져오기 위해서 위치 권한이 필요합니다.",
                            Snackbar.LENGTH_INDEFINITE);
                    snackBar.setAction("권한 승인", v -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    });
                    snackBar.show();
                }
            }
        } else permission_check = true;

    }

    public void backgroundPermissionDialog() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(getContext());
        localBuilder.setTitle("권한 설정")
                .setMessage("백그라운드 위치 권한을 위해 항상 허용으로 설정해주세요.")
                .setPositiveButton("권한 설정하러 가기", (paramAnonymousDialogInterface, paramAnonymousInt) -> {
                    try {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{BACKGROUND_PERMISSIONS}, BACKGROUND_PERMISSION_REQUEST_CODE);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("취소하기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                        Toast.makeText(getContext(), "백그라운드 위치 업데이트가 제한됩니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .create()
                .show();

    }

    private void setClickableFiltering(boolean flag) {
        filter_merge.setClickable(flag);
        filter_all.setClickable(flag);
        filter_ran.setClickable(flag);
        filter_kcal.setClickable(flag);
        filter_distance.setClickable(flag);
    }

    public void setSingleTon() {

        Log.i("setSingleTon", "setSingleTon");

        dr_myCourses.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            int final_index = Integer.valueOf(document.get("final_index").toString());
            int total_count = Integer.valueOf(document.get("total_count").toString());
            MapSingleTon.getInstance().setMy_final_index(final_index);
            MapSingleTon.getInstance().setMy_total_count(total_count);
        });

        dr_totally_map.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            int final_index = Integer.valueOf(document.get("final_index").toString());
            int total_count = Integer.valueOf(document.get("total_count").toString());
            MapSingleTon.getInstance().setTotally_final_index(final_index);
            MapSingleTon.getInstance().setTotally_total_count(total_count);
        });

        Log.i("user.getUid()", user.getUid());

        // VOICE GUIDE
        db.collection("Users").document(user.getUid())
                .collection("Map").document("Sound")
                .get()
                .addOnCompleteListener(task -> {

                    Log.i("task.getResult", task.getResult().exists() + "");

                    DocumentSnapshot document = task.getResult();
                    boolean all_check = Boolean.valueOf(document.get("all").toString());
                    boolean start_check = Boolean.valueOf(document.get("start").toString());
                    boolean arrive_check = Boolean.valueOf(document.get("arrive").toString());
                    boolean quarter_check = Boolean.valueOf(document.get("quarter").toString());
                    boolean distance_check = Boolean.valueOf(document.get("distance").toString());

                    MapSingleTon.getInstance().setAll_check(all_check);
                    MapSingleTon.getInstance().setStart_check(start_check);
                    MapSingleTon.getInstance().setArrive_check(arrive_check);
                    MapSingleTon.getInstance().setQuarter_check(quarter_check);
                    MapSingleTon.getInstance().setDistance_check(distance_check);

                });

    }

    public void setOnclickListeners() {
        go.setOnClickListener(this);
        draw_btn.setOnClickListener(this);
        filter_distance.setOnClickListener(this);
        filter_all.setOnClickListener(this);
        filter_kcal.setOnClickListener(this);
        filter_ran.setOnClickListener(this);
        filter_merge.setOnClickListener(this);
        filter_like.setOnClickListener(this);
        tv_many.setOnClickListener(this);
        tv_solo.setOnClickListener(this);
        tv_me.setOnClickListener(this);
    }

    // onRequestPermissionResult()의 결과를 FusedLocationSource 의
    // onRequestPermissionsResult()에 전달해야 합니다.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        // grantResults = PREMISSION_GRANTED
        // permissions = ACCESS_FINE_LOCATION...
        // requestCode = PERMISSION_REQUEST_CODE

        // PERMISSION {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}

        // grantResults[0] = 앱 사용 중에만 허용
        // grantResults[1] = 이번만 허용
        // grantResults[2] = 거부

        Log.i("onRequestPermissions", "onRequestPermissionsResult");
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            Log.i("PERMISSION_REQUEST_CODE", "PERMISSION_REQUEST_CODE");
            if (locationManager != null) {
                Log.i("locationManager", "check!!");

                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.i("onRequest", "checkSelfPermission!!");
                    editor.putBoolean("first_check", false);

                    return;
                } else permission_check = true;

                uiSettings.setLocationButtonEnabled(true);
                Log.i("checkSelfPermission", "check!!");
            }
        }

        /*
        // 기본 설정
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }

         */

        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    public boolean hasLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        Log.i("onMapReady", "check!!");
        // 비동기로 Map 객체 가져오기
        this.naverMap = naverMap;
        Courses.getInstance().setNaverMap(naverMap);
        this.naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, true);
        this.naverMap.setLocationSource(locationSource);

        // UI Setting
        uiSettings = naverMap.getUiSettings();
        // NoFollow 가 Default 임
        uiSettings.setLocationButtonEnabled(true);

        // 처음 위치 Default
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location != null) {
                        Log.i("getCurrentLocation", "check!!");
                        cameraPosition = new CameraPosition(
                                new LatLng(location.getLatitude(), location.getLongitude())
                                , 14, // 줌 레벨
                                20, // 기울임 각도
                                180 // 베어링 각도
                        );
                        naverMap.setCameraPosition(cameraPosition);
                    } else {
                        // Toast.makeText(getActivity(), "실내에 있지마세요!!", Toast.LENGTH_SHORT).show();
                    }
                });


        MapSingleTon.getInstance().setFusedLocationClient(fusedLocationProviderClient);

        // 위치 추척 모드
        // None : 추적 x
        // NoFollow : 위치 추적 활성화, 지도는 움직이지 않음
        // Follow : 위치, 지도 움직임. API 나 제스처를 사용해 카메라를 움직일 경우 NoFollow 로 바뀜
        // Face : 오버레이, 카메라의 좌표, 베어링이 사용자의 위치 방향에 따라 움직임.

        // 위치 변경 이벤트
        // 위치 추적 모드가 활성화되고 사용자의 위치가 변경되면 onLocationChange() 콜백 메서드가 호출되며,
        // 파라미터로 사용자의 위치가 전달됩니다.

        this.naverMap.addOnLocationChangeListener(location -> {

            Log.i("naveMapLocation", String.valueOf(location.getLatitude()));
            Log.i("ACCURACY", String.valueOf(location.getAccuracy()));
            current_loc = new LatLng(location.getLatitude(), location.getLongitude());
            MapSingleTon.getInstance().setCurrent_loc(current_loc);

            wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
            List<ScanResult> scanList = wifiManager.getScanResults();
            int scanSize = scanList.size();
            int i=0;
            for(ScanResult data: scanList){
                Log.i("data" + " " + i++, data.level + "");
            }
            provider_check.setText(scanList.get(scanSize - 1).level + "");


            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                    .PERMISSION_GRANTED) {
                return;
            }
            if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null){
                if (MapSingleTon.getInstance().isRunning_check() && !start_count_flag) {
                    if (check_count == 0) {
                        Toast.makeText(Fragment_Map.this.getActivity(), "지금부터 경로를 그립니다.!!", Toast.LENGTH_SHORT).show();
                        create_line.setText("FINISH");
                        create_line.setBackgroundResource(R.drawable.circle_board_finish);
                        check_count++;

                    }

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    path.setColor(Color.GREEN);
                    path.setWidth(6);
                    kalman_path.setColor(Color.YELLOW);
                    kalman_path.setWidth(12);

                    //  Courses Setting
                    // Courses.getInstance().setPrev_loc(latLng);

                    // Log.i("Courses.getInstance()", Courses.getInstance().getLocations().size() + "");
                    if (Courses.getInstance().getLocations().size() == 0) {
                        first_point_check = true;

                        double bearing = 180;

                        if (Courses.getInstance().isItem_pick_check()) {
                            if (Courses.getInstance().getRecord_quarter_locations().size() != 0) {
                                double Lat = Courses.getInstance().getRecord_quarter_locations().get(0).latitude;
                                double Long = Courses.getInstance().getRecord_quarter_locations().get(0).longitude;

                                bearing = bearingP1toP2(latLng.latitude, latLng.longitude, Lat, Long);

                                Log.i("bearing Lat", Lat + "");
                                Log.i("bearing Long", Long + "");
                                Log.i("bearing", bearing + "");
                            }
                        }

                        cameraPosition = new CameraPosition(latLng, // 대상 지점
                                16, // 줌 레벨
                                60, // 기울임 각도
                                bearing // 베어링 각도
                        );
                        naverMap.setCameraPosition(cameraPosition);

                        // Log.i("GpsMarkers.size()", "0");

                        Fragment_Map.this.set_my_marker(latLng);
                        MapSingleTon.getInstance().addCoords(latLng);
                        MapSingleTon.getInstance().addKalman_coords(latLng);

                        // TIME
                        ///////////////////////////////////////////////////////////////////

                        // Courses Data (time)
                        Courses.getInstance().addTime(System.currentTimeMillis());

                        // Courses time (prev)
                        Courses.getInstance().setPrev_time(System.currentTimeMillis());

                        // Courses start time
                        Courses.getInstance().setStart_time(System.currentTimeMillis());

                        // LOCATION
                        ///////////////////////////////////////////////////////////////////

                        // Courses Locations
                        Courses.getInstance().addLocations(location);

                        // initial prev_location
                        LatLng prev_latlng = new LatLng(location.getLatitude(), location.getLongitude());
                        Courses.getInstance().setPrev_loc(prev_latlng);

                        // KALMAN
                        ///////////////////////////////////////////////////////////////////

                        // Kalman 초기값 설정
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            MapSingleTon.getInstance().setKalmanFilter(location.getLatitude(), location.getLongitude(),
                                    System.currentTimeMillis(), location.getAccuracy(), location.getAltitude(), location.getVerticalAccuracyMeters());
                        }

                        //
                        Courses.getInstance().addQuarterly_distance(0.0);
                    }
                    else if (Courses.getInstance().getLocations().size() != 0) {

                        if (Courses.getInstance().getPrev_loc().distanceTo(latLng) > 0.10) {

                            // my_marker remove
                            if (my_marker != null)
                                my_marker.setMap(null);
                            Fragment_Map.this.set_my_marker(latLng);

                            MapSingleTon.getInstance().addCoords(latLng);

                            path.setCoords(MapSingleTon.getInstance().getCoords());
                            // prev_point++;
                            // marker.setMap(naverMap);

                            // Courses Data (time, stay)
                            Courses.getInstance().addTime(System.currentTimeMillis());

                            // Courses Locations
                            Courses.getInstance().addLocations(location);

                            //
                            LatLng cur_latlng = new LatLng(location.getLatitude(), location.getLongitude());
                            double distance = Courses.getInstance().getPrev_loc().distanceTo(cur_latlng);
                            double cur_time = System.currentTimeMillis();
                            double prev_time = Courses.getInstance().getPrev_time();

                            double time = (cur_time - prev_time) / 1000;
                            double speed = distance / time;

                            // LOG
                            ///////////////////////////////////////////////////////////////////

                            // Log.i("distance", distance + "");
                            // Log.i("SPEED", speed + "");
                            // Log.i("ACCURACY", location.getAccuracy() + "");
                            // og.i("altitude", location.getAltitude() + "");

                            // Log.i("kalmanFilter", location.getLatitude() + "  " + location.getLongitude());

                            //
                            sampling_altitude.add(String.valueOf(location.getAltitude()));
                            MapSingleTon.getInstance().addSample_altitude(String.valueOf(location.getAltitude()));

                            Log.i("ACCURACY", location.getAccuracy() + "");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                Log.i("ALTITUDE ACCURACY", location.getVerticalAccuracyMeters() + "");
                            }

                            // KALMAN
                            ///////////////////////////////////////////////////////////////////
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                MapSingleTon.getInstance().processKalmanFilter(speed, location.getLatitude(),
                                        location.getLongitude(), System.currentTimeMillis(),
                                        location.getAccuracy(), location.getAltitude(), location.getVerticalAccuracyMeters());
                            }


                            LatLng kalman_latlng = new LatLng(MapSingleTon.getInstance().getKalmanFilter().getLatitude(),
                                    MapSingleTon.getInstance().getKalmanFilter().getLongitude());
                            MapSingleTon.getInstance().addKalman_coords(kalman_latlng);
                            kalman_path.setCoords(MapSingleTon.getInstance().getKalman_coords());
                            kalman_path.setMap(naverMap);
                            path.setMap(naverMap);

                            //
                            sampling_kalman_altitude.add(String.valueOf(MapSingleTon.getInstance().getKalmanFilter().getAltitude()));
                            MapSingleTon.getInstance().addSample_kalman_altitude(
                                    String.valueOf(MapSingleTon.getInstance().getKalmanFilter().getAltitude()));

                            Log.i("ACCURACY", MapSingleTon.getInstance().getKalmanFilter().getAccuracy() + "");

                            // Courses kalman
                            Courses.getInstance().addKalman_latLngs(kalman_latlng);

                            // SETTING
                            ///////////////////////////////////////////////////////////////////

                            Courses.getInstance().setPrev_time(System.currentTimeMillis());
                            Courses.getInstance().setPrev_loc(cur_latlng);
                            Courses.getInstance().addTotal_distance(distance);

                            // GUID
                            ///////////////////////////////////////////////////////////////////

                            // 일정 간격 시 안내 (1km 당)
                            if (MapSingleTon.getInstance().isDistance_check()) {
                                Courses.getInstance().setLanguageSpeech(Locale.KOREA);
                                Courses.getInstance().intervalSpeechChecking(Courses.getInstance().getTotal_distance(),
                                        MapSingleTon.getInstance().getTOTAL_SEC());
                            }

                            // QUARTER LOCATION
                            ///////////////////////////////////////////////////////////////////

                            // 거리로 측정 (약 500m)
                            // 지금까지 측정한 거리가 500m가 넘을 시 quarter point 를 저장 함.
                            if (Courses.getInstance().getQuarterly_distance() + distance > 500.0) {

                                Courses.getInstance().addQuarter_location(location);
                                Courses.getInstance().addQuarter_index(
                                        Courses.getInstance().getLocationsSize());

                                // point 를 저장 했다면 quarterly_distance 를 다시 계산 함.
                                Courses.getInstance().clearQuarterly_distance();
                            }

                            // 500m 안넘으면 그냥 계속 더함
                            else Courses.getInstance().addQuarterly_distance(distance);

                            // 코스 체크
                            ///////////////////////////////////////////////////////////////////

                            // follow 일때 만 확인

                            if (Courses.getInstance().isItem_pick_check()) {

                                if (MapSingleTon.getInstance().isQuarter_check()) {

                                    if (Courses.getInstance().getRecord_quarter_locations().size() !=
                                            Courses.getInstance().getUser_qua_index()) {

                                        LatLng quarter_loc = Courses.getInstance().getRecord_quarter_locations().get(
                                                Courses.getCoursesInstance().getUser_qua_index());

                                        if (current_loc.distanceTo(quarter_loc) < 30) {
                                            // bearing
                                            bearingP1toP2(cur_latlng.latitude, cur_latlng.longitude,
                                                    quarter_loc.latitude, quarter_loc.longitude);

                                            Courses.getInstance().setLanguageSpeech(Locale.KOREA);
                                            Courses.getInstance().speeching(
                                                    (Courses.getInstance().getUser_qua_index() + 1) + "구간을 지나고 있습니다.");
                                            // check
                                            Courses.getInstance().PlusQuarter_index();
                                            // 나중에 코스를 등록할 시 check_count 랑 분기점들 갯수를 비교하여 확인할 것.
                                        }
                                    }
                                }

                                if (MapSingleTon.getInstance().isArrive_check()) {
                                    LatLng finish_lng = new LatLng(Double.valueOf(pick_item_mapData.getFinish_lat()),
                                            Double.valueOf(pick_item_mapData.getFinish_long()));

                                    Courses.getInstance().guideChecking(current_loc, finish_lng);
                                }
                            }

                            // SET TEXT
                            ///////////////////////////////////////////////////////////////////

                            txtDistMap.setText(String.format("%.2f", Courses.getInstance().getTotal_distance()) + " m");
                            txtKcalMap.setText(String.format("%.2f", speed) + "m/s");
                            altitude.setText(String.format("%.5f", location.getAltitude()));
                            kalman_altitude.setText(String.format("%.5f", MapSingleTon.getInstance().getKalmanFilter().getAltitude()));
                        }
                    }
                }
                else {
                    if (check_count > 0) {
                        create_line.setText("R U N");
                        create_line.setBackgroundResource(R.drawable.circle_board_start);
                        check_count = 0;
                    }
                }
                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null){
                    // provider_check.setText("GPS NETWORK");
                }
                else // provider_check.setText("GPS");
                Log.i("GPS_PROVIDER", loc.getLatitude() + "   " + loc.getLongitude());
            }
            else{
                Log.i("GPS_PROVIDER", "null");
                if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null){
                    provider_check.setText("NETWORK");

                }
                else provider_check.setText("NULL");
            }
        });


        prev_location = new Location("PREV");

        naverMap.setOnMapClickListener((pointF, latLng) -> {

            Log.i("click", latLng.latitude + " " + latLng.longitude);
            // kalman altitude 구하는 코드

            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(latLng.latitude, latLng.longitude))
                    .animate(CameraAnimation.Easing);
            naverMap.moveCamera(cameraUpdate);

            if (drawFlag) {
                Marker markerDot = new Marker();
                InfoWindow infoWindow = new InfoWindow();

                clickLists.add(new LatLng(latLng.latitude, latLng.longitude));
                markerDot.setPosition(clickLists.get(clickLists.size() - 1));
                Log.i("markersize", markerCourseList.size() + "");

                markerCourseList.add(markerDot);
                if (clickLists.size() > 1) {
                    Double dist = calculator.dist_num(clickLists);
                    total_dist += dist;
                    txtDistMap.setText(calculator.dist_result(calculator.dist_point(clickLists)));
                    txtTimeMap.setText(calculator.time_result(calculator.dist_point(clickLists)));
                    txtKcalMap.setText(
                            calculator.kcal_result(Integer.valueOf(calculator.time_result(total_dist).split("[ ]")[0])) + " kcal");

                    clickPath.setCoords(clickLists);
                    clickPath.setColor(Color.GREEN);
                    clickPath.setWidth(30);
                    clickPath.setMap(naverMap);

                    infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getActivity()) {
                        @NonNull
                        @Override
                        public CharSequence getText(@NonNull InfoWindow infoWindow) {
                            String out = "";
                            out = "점 거리 : " + calculator.dist_result(calculator.dist_point(clickLists));
                            return out;
                        }
                    });

                    markerDot.setOnClickListener(overlay -> {
                        if (markerDot.getInfoWindow() == null) {
                            // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                            infoWindow.open(markerDot);
                        } else {
                            // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                            infoWindow.close();
                        }
                        return true;
                    });
                    markerCourseList.add(markerDot);
                }

                int i = 0;
                for (Marker marker : markerCourseList) {
                    if (i == 0) {
                        marker.setIcon(MarkerIcons.LIGHTBLUE);
                        marker.setCaptionText("시작장소");
                    } else if (i == markerCourseList.size() - 1) {
                        marker.setIcon(MarkerIcons.PINK);
                        infoWindow.open(marker);
                    } else marker.setIcon(OverlayImage.fromResource(R.drawable.middledot));

                    marker.setMap(naverMap);
                    i++;
                }
                drawer_marker_flag = true;
            }
        });

    }

    public static short bearingP1toP2(double latitude1, double longitude1, double latitude2, double longitude2) {
        // 현재 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
        double Cur_Lat_radian = latitude1 * (Math.PI / 180);
        double Cur_Lon_radian = longitude1 * (Math.PI / 180);


        // 목표 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
        double Dest_Lat_radian = latitude2 * (Math.PI / 180);
        double Dest_Lon_radian = longitude2 * (Math.PI / 180);

        // radian distance
        double radian_distance = 0;
        radian_distance = Math.acos(Math.sin(Cur_Lat_radian) * Math.sin(Dest_Lat_radian)
                + Math.cos(Cur_Lat_radian) * Math.cos(Dest_Lat_radian) * Math.cos(Cur_Lon_radian - Dest_Lon_radian));

        // 목적지 이동 방향을 구한다.(현재 좌표에서 다음 좌표로 이동하기 위해서는 방향을 설정해야 한다. 라디안값이다.
        double radian_bearing = Math.acos((Math.sin(Dest_Lat_radian) - Math.sin(Cur_Lat_radian)
                * Math.cos(radian_distance)) / (Math.cos(Cur_Lat_radian) * Math.sin(radian_distance)));// acos의 인수로 주어지는 x는 360분법의 각도가 아닌 radian(호도)값이다.

        double true_bearing = 0;
        if (Math.sin(Dest_Lon_radian - Cur_Lon_radian) < 0) {
            true_bearing = radian_bearing * (180 / Math.PI);
            true_bearing = 360 - true_bearing;
        } else {
            true_bearing = radian_bearing * (180 / Math.PI);
        }

        return (short) true_bearing;
    }

    public void registerCourse() {
        // 코스 데이터가 하나 밖에 없다면 저장 할 수 없음
        if (Courses.getInstance().getLocations().size() == 1) {
            Toast.makeText(getActivity(), "데이터가 부족합니다!!", Toast.LENGTH_SHORT).show();
            return;
        }ａｓｖａｓｖｓａｖ
        //
        CustomDialogue dialogue = new CustomDialogue(clickLists, "이준형");
        dialogue.show(getParentFragmentManager(), "Dialog");
        dialogue.setDialogListener(new CustomDialogue.CustomDialogueListener() {
            @Override
            public void onPositiveClicked(String registerName, ArrayList<LatLng> geoList) {

                TODAY_RUNNING_UPDATE();

                //
                run_data_frame.setVisibility(View.INVISIBLE);
                running_timer.setVisibility(View.INVISIBLE);
                recycler_flame.setVisibility(View.VISIBLE);                 // 리사이클러뷰

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) run_data_frame.getLayoutParams();
                params.width = 300; // 300
                params.height = 245; // 245
                run_data_frame.setLayoutParams(params);

                run_timer_check = false;


                naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);
                MapSingleTon.getInstance().setRunning_check(false);

                // singleTon

                MapSingleTon.getInstance().PlusMy_final_index();
                MapSingleTon.getInstance().PlusMy_total_count();

                MapSingleTon.getInstance().PlusTotally_final_index();
                MapSingleTon.getInstance().PlusTotally_total_count();

                int my_final_index = MapSingleTon.getInstance().getMy_final_index();
                int my_total_count = MapSingleTon.getInstance().getMy_total_count();

                int totally_final_index = MapSingleTon.getInstance().getTotally_final_index();
                int totally_total_count = MapSingleTon.getInstance().getTotally_total_count();

                Map<String, Object> fi2 = new HashMap<>();
                fi2.put("final_index", totally_final_index);
                fi2.put("total_count", totally_total_count);

                db.collection("Map").document("User_Courses").set(fi2);

                Map<String, Object> fi = new HashMap<>();
                fi.put("final_index", my_final_index);
                fi.put("total_count", my_total_count);

                //
                dr_myCourses.update(fi);

                QuarterOfCourses quarterOfCourses = new QuarterOfCourses();
                quarterOfCourses.setId(String.valueOf(totally_final_index));

                double total_distance = Courses.getInstance().getTotal_distance();
                int size = Courses.getInstance().getLocations().size();

                // courses location data
                for (int i = 0; i < size; i++) {

                    Map<String, Object> map = new HashMap<>();

                    Location location = Courses.getInstance().getLocations().get(i);

                    // 좌표
                    map.put("index", i + 1);
                    map.put("lat", location.getLatitude());
                    map.put("long", location.getLongitude());
                    // map.put("location", Courses.getInstance().getLocations().get(i));

                    Courses.getInstance().setPrev_time(Courses.getInstance().getTimes().get(i));
                    // 시간
                    map.put("time", Courses.getInstance().getTimes().get(i));

                    // SAVE
                    dr_myCourses.collection("courses")
                            .document(String.valueOf(my_final_index)).collection("points")
                            .document(String.valueOf(i + 1)).set(map);

                    // totally map SAVE
                    cr_totally_mapCourses.document(String.valueOf(totally_final_index))
                            .collection("points").document(String.valueOf(i + 1)).set(map);

                }

                // quarter points
                for (int i = 0; i < Courses.getInstance().getRegister_quarter_locations().size(); i++) {

                    Map<String, Object> map = new HashMap<>();

                    Location location = Courses.getInstance().getRegister_quarter_locations().get(i);
                    int quarter_index = Courses.getInstance().getQuarter_index().get(i);

                    map.put("index", quarter_index);
                    map.put("lat", location.getLatitude());
                    map.put("long", location.getLongitude());

                    //
                    quarterOfCourses.addQuarter_Lat(location.getLatitude());
                    quarterOfCourses.addQuarter_Long(location.getLongitude());

                    // SAVE
                    dr_myCourses.collection("courses")
                            .document(String.valueOf(my_final_index)).collection("quarters")
                            .document(String.valueOf(quarter_index)).set(map);

                    // totally map SAVE
                    cr_totally_mapCourses.document(String.valueOf(totally_final_index))
                            .collection("quarters")
                            .document(String.valueOf(quarter_index)).set(map);
                }

                Map<String, Object> my_map = new HashMap<>();
                Map<String, Object> totally_map = new HashMap<>();

                // kcal
                double kcal = calculator.kcal_result(
                        Integer.valueOf(calculator.time_result(total_distance).split("[ ]")[0]));

                // TOTALMAP
                ////////////////////////////////////////////////////////////////////////////
                updateTOTALMAP(total_distance, kcal);

                ////////////////////////////////////////////////////////////////////////////

                today_date.setNow();
                // 거리
                my_map.put("total_distance", total_distance);
                totally_map.put("total_distance", total_distance);
                // 칼로리
                my_map.put("kcal", kcal);
                totally_map.put("kcal", kcal);
                // 코스 이름
                my_map.put("name", registerName);
                totally_map.put("name", registerName);
                // 시간
                my_map.put("total_time", MapSingleTon.getInstance().getTOTAL_SEC());
                totally_map.put("total_time", MapSingleTon.getInstance().getTOTAL_SEC());
                // Primary key
                my_map.put("index", my_final_index);
                totally_map.put("index", totally_final_index);
                // 날짜
                my_map.put("date", today_date.getFormat_date());
                totally_map.put("date", today_date.getFormat_date());

                //
                totally_map.put("total_avg_time", MapSingleTon.getInstance().getTOTAL_SEC());
                totally_map.put("total_avg_distance", total_distance);

                //
                // 팔로우 개수
                my_map.put("follow_count", 0);
                totally_map.put("follow_count", 0);

                // 출발점
                my_map.put("start_point_lat",
                        Courses.getInstance().getLocations().get(0).getLatitude());
                my_map.put("start_point_long",
                        Courses.getInstance().getLocations().get(0).getLongitude());
                totally_map.put("start_point_lat",
                        Courses.getInstance().getLocations().get(0).getLatitude());
                totally_map.put("start_point_long",
                        Courses.getInstance().getLocations().get(0).getLongitude());

                // 도착점
                my_map.put("finish_point_lat",
                        Courses.getInstance().getLocations().get(size - 1).getLatitude());
                my_map.put("finish_point_long",
                        Courses.getInstance().getLocations().get(size - 1).getLongitude());
                totally_map.put("finish_point_lat",
                        Courses.getInstance().getLocations().get(size - 1).getLatitude());
                totally_map.put("finish_point_long",
                        Courses.getInstance().getLocations().get(size - 1).getLongitude());

                // 좋아요 초기화
                my_map.put("like", 0);
                totally_map.put("like", 0);

                // totally index
                my_map.put("totally_index", totally_final_index);
                // uid (Totally 의 경우에만 저장)
                totally_map.put("uid", user.getUid());

                // 등록자의 닉네임도 저장
                db.collection("Users").document(user.getUid())
                        .collection("Profile").document("diet_profile")
                        .get()
                        .addOnCompleteListener(task -> {
                            DocumentSnapshot document = task.getResult();
                            String nickName = document.get("nickName").toString();
                            my_map.put("nickName", nickName);
                            totally_map.put("nickName", nickName);

                            dr_myCourses.collection("courses").document(String.valueOf(my_final_index)).set(my_map);
                            // Course Data SAVE
                            cr_totally_mapCourses.document(String.valueOf(totally_final_index)).set(totally_map);
                        });

                // Log.i("total_distance", total_distance + "");
                // Log.i("kcal", kcal + "");
                // Log.i("uid", user.getUid() + "");

                // SingleTon
                MapSingleTon.getInstance().addQuarterOfCourses(quarterOfCourses);

                create_line_record_cancel();
                // Log.i("SAVE", "check!!");
            }

            @Override
            public void onNegativeClicked() {

                //
                run_data_frame.setVisibility(View.INVISIBLE);
                running_timer.setVisibility(View.INVISIBLE);
                recycler_flame.setVisibility(View.VISIBLE);                 // 리사이클러뷰

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) run_data_frame.getLayoutParams();
                params.width = 300; // 300
                params.height = 245; // 245
                run_data_frame.setLayoutParams(params);

                run_timer_check = false;

                create_line_record_cancel();
            }

            @Override
            public void onResumeClicked() {
            }
        });
    }

    // TOTALMAP
    public void updateTOTALMAP(double total_distance, double kcal) {
        Map<String, Object> mapTOTAL = new HashMap<>();
        double TOTAL_DISTANCE = MapSingleTon.getInstance().getTOTAL_DISTANCE();
        TOTAL_DISTANCE += total_distance;
        double TOTAL_KCAL = MapSingleTon.getInstance().getTOTAL_KCAL();
        TOTAL_KCAL += kcal;
        double TOTAL_SPEED = MapSingleTon.getInstance().getTOTAL_AVG_SPEED();
        TOTAL_SPEED += total_distance / MapSingleTon.getInstance().getTOTAL_SEC();
        mapTOTAL.put("total_count", MapSingleTon.getInstance().getTOTAL_COUNT() + 1);
        mapTOTAL.put("total_distance", TOTAL_DISTANCE);
        mapTOTAL.put("total_kcal", TOTAL_KCAL);
        mapTOTAL.put("total_avg_speed", TOTAL_SPEED);

        dr_mapTOTAL.set(mapTOTAL);
    }

    public void TOTAL_AVG_DATA_UPDATE() {

        cr_totally_mapCourses.document(totally_course_id)
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot document = task.getResult();

                    double total_avg_time = Double.valueOf(document.get("total_avg_time").toString());

                    total_avg_time = (total_avg_time + MapSingleTon.getInstance().getTOTAL_SEC()) / 2;

                    // 바로 저장
                    Map<String, Object> course_data = new HashMap<>();
                    course_data.put("total_avg_time", total_avg_time);

                    cr_totally_mapCourses.document(totally_course_id).update(course_data);
                });
    }

    public void TODAY_RUNNING_UPDATE() {

        CollectionReference cr_health_today = db.collection("Users").document(user.getUid())
                .collection("Health").document("today")
                .collection("list");

        boolean equal_check = false;


        double running_kcal = calculator.kcal_result(
                Integer.valueOf(calculator.time_result(
                        Courses.getInstance().getTotal_distance()).split("[ ]")[0]));

        double total_health_kcal = Double.valueOf(Sub_bundle.getInstance().getTotal_health_kcal());
        total_health_kcal += running_kcal;
        Sub_bundle.getInstance().setTotal_health_kcal(String.valueOf(total_health_kcal));

        HealthData healthData = new HealthData();
        healthData.setName("RUNNING");
        healthData.setKcal(running_kcal);
        healthData.setImg_src(R.drawable.run1);
        healthData.setTime((int) Math.round(MapSingleTon.getInstance().getTOTAL_SEC() / 60));

        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < HealthSingleTon.getInstance().getArray_healthData().size(); i++) {
            HealthData new_data = HealthSingleTon.getInstance().getArray_healthData().get(i);
            String health_name = new_data.getName();

            // 추가했다면 시간과 칼로리를 더해줌
            if (health_name.equals(healthData.getName())) {
                new_data.setTime(new_data.getTime() + healthData.getTime());
                map.put("time", new_data.getTime());
                new_data.setKcal(new_data.getKcal() + healthData.getKcal());
                HealthSingleTon.getInstance().getArray_healthData().set(i, new_data);
                map.put("kcal", new_data.getKcal());

                cr_health_today.document(healthData.getName()).update(map);
                equal_check = true;
                Log.i("equal_check", "true");
                break;
            }
        }
        // 추가 안했었다면 그냥 추가
        if (!equal_check) {
            HealthSingleTon.getInstance().getArray_healthData().add(healthData);

            map.put("name", healthData.getName());
            map.put("kcal", healthData.getKcal());
            map.put("time", healthData.getTime());
            map.put("imgSrc", healthData.getImg_src());

            cr_health_today.document(healthData.getName()).set(map);
        }

        // db
        Map<String, Object> total_map = new HashMap<>();
        total_map.put("TotalHealthKcalOfDay", total_health_kcal);
        db.collection("Users").document(user.getUid()).update(total_map);

    }

    // GUIDE
    ///////////////////////////////////////////////////////////////////////////////////////
    public void equalCourseGuide(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("확인")
                .setMessage(message)
                .setNegativeButton("예", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setPositiveButton("끝내기", (dialog, which) -> {
                    run_data_frame.setVisibility(View.INVISIBLE);
                    running_timer.setVisibility(View.INVISIBLE);
                    recycler_flame.setVisibility(View.VISIBLE);                 // 리사이클러뷰

                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) run_data_frame.getLayoutParams();
                    params.width = 300; // 300
                    params.height = 245; // 245
                    run_data_frame.setLayoutParams(params);

                    run_timer_check = false;

                    naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);
                    MapSingleTon.getInstance().setRunning_check(false);

                    create_line_record_cancel();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // FOLLOW FUNCTION
    ///////////////////////////////////////////////////////////////////////////////////////
    public void get_random_others_follow_data(Task<QuerySnapshot> task, UfsOther uo) {
        Log.i("others_follow_data", "get_random_others_follow_data");
        Log.i("task", task.getResult().size() + "");

        double prev_time = 0;
        double result_distance = 0;
        int result_time = 0;
        int count = 0;

        if (get_follow_mode().equals("me")) uo.setMe_mode_check(true);
        else if (get_follow_mode().equals("many")) uo.setMe_mode_check(false);

        LatLng prev_Lng = new LatLng(0, 0);

        for (QueryDocumentSnapshot document : task.getResult()) {

            String Lat = document.get("lat").toString();
            String Long = document.get("long").toString();
            double time = Double.valueOf(document.get("time").toString());

            //
            LatLng latLng = new LatLng(Double.valueOf(Lat), Double.valueOf(Long));

            if (document.getId().equals("1")) {
                prev_time = Double.valueOf(document.get("time").toString());
                uo.addUfs_lat(Double.valueOf(Lat));
                uo.addUfs_long(Double.valueOf(Long));

                prev_Lng = new LatLng(Double.valueOf(Lat), Double.valueOf(Long));
                // Log.i("getId().equals(\"1\")", "getId().equals(\"1\")");
            } else {
                double tem_result = Math.round((time - prev_time) / 1000);
                if (tem_result == 0)
                    result_time = 1;
                else result_time = (int) tem_result;

                uo.addUfs_time(result_time);
                // Log.i("prev_time", prev_time + "");
                prev_time = time;

                // Log.i("result_time", result_time + "");

                uo.addUfs_lat(Double.valueOf(Lat));
                uo.addUfs_long(Double.valueOf(Long));

                result_distance += prev_Lng.distanceTo(latLng);
                Log.i("result_distance", result_distance + "");
                uo.addUfs_distance(result_distance);

                prev_Lng = latLng;
                // Log.i("uoData", uo.getUfs_lat().get(count) + "");

            }
            Log.i("time", time + "");
            count++;
        }

        ufsOthers.add(uo);
    }

    // COMPARISON
    ///////////////////////////////////////////////////////////////////////////////////////

    public boolean distance_comparison(double original, double total) {

        // 1km 당 25m 의 오차범위를 허용해 줄 것임
        int marginOfError = (int) (Math.round(original / 1000.0) * 25);

        // 1km 이상부터 코스를 등록할 수 있음
        if (original + marginOfError > total || original - marginOfError < total) return true;
        else return false;
    }

    // FOLLOW FUNCTION
    ///////////////////////////////////////////////////////////////////////////////////////

    public void addMyFollowCourseData(DocumentReference dr_ran) {

        Log.i("size", Courses.getInstance().getLocations().size() + "");
        for (int i = 0; i < Courses.getInstance().getLocations().size(); i++) {

            Map<String, Object> map = new HashMap<>();

            Location location = Courses.getInstance().getLocations().get(i);

            // 좌표
            map.put("index", i + 1);
            map.put("lat", location.getLatitude());
            map.put("long", location.getLongitude());
            // map.put("location", Courses.getInstance().getLocations().get(i));

            Courses.getInstance().setPrev_time(Courses.getInstance().getTimes().get(i));
            // 시간
            map.put("time", Courses.getInstance().getTimes().get(i));

            Log.i("addMyFollowCourseData", i + "");

            // 코스 정보 등록
            dr_ran.collection("Courses").document(pick_course_id)
                    .collection("points").document(String.valueOf(i + 1))
                    .set(map);
        }
    }

    public void renewal_my_course_data(CollectionReference cr_top20) {

        // 본인 기록이 있는지 확인 해야 함
        cr_top20.whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(task12 -> {
                    // 본인 기록 존재
                    if (task12.getResult().size() != 0) {
                        // 본인 기록과 현재 기록 비교
                        for (QueryDocumentSnapshot document2 : task12.getResult()) {

                            String total_time2 = document2.get("total_time").toString();


                            // 현재 기록이 더 좋으면
                            if (Integer.valueOf(total_time2) > MapSingleTon.getInstance().getTOTAL_SEC()) {

                                // 기록 갱신
                                course_data_record();
                            }
                            // 아니면 return
                            else return;
                        }
                    }
                    // 본인 기록 없음
                    else course_data_record();
                });
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    // COURSE DATA
    ///////////////////////////////////////////////////////////////////////////////////////

    public void course_pick_event(int index) {
        //
        Courses.getInstance().setItem_pick_check(true);
        setClickableFiltering(false);

        Log.i("position", index + "");
/*
        Courses.getInstance().settingSpeech((float) 0.8, (float) 1.0);
        Courses.getInstance().speeching("현재 까지 1킬로미터");
        Courses.getInstance().speechingTerm(1000);
        Courses.getInstance().speeching("50분 20초");
        Courses.getInstance().speechingTerm(1000);
        Courses.getInstance().speeching("500칼로리");



 */

        // marker
        // CourseMarkers.get(index - 1).setHideCollidedMarkers(false);

        //
        pick_course_id = pick_item_mapData.getIndex();
        if(MY_checked){
            dr_myCourses.collection("courses").document(pick_course_id)
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        String totally_index = document.get("totally_index").toString();
                        totally_course_id = totally_index;
                    });
        }
        else totally_course_id = pick_course_id;

        Log.i("pick_course_id", pick_course_id);
        Log.i("data.getName()", pick_item_mapData.getCourse_name());

        btn_coursePickItem.setTextOn(adapter.getChoseUnit(index));
        btn_coursePickItem.setTextOff(adapter.getChoseUnit(index));
        btn_coursePickItem.setChecked(true);

        // setText
        txtPickCourse.setText(pick_item_mapData.getCourse_name());
        txtPickKM.setText((Math.round(
                Double.valueOf(pick_item_mapData.getCourseDist()) / 1000 * 100) / 100.0) + " km");
        txtPickTIME.setText(pick_item_mapData.getTime() + " 초");
        txtPickKCAL.setText(pick_item_mapData.getKcal() + " kcal");

        // Out new Frame
        btn_coursePickItem.setOnClickListener(v1 -> {

            user_course_remove();
            // holder.getButton().setChecked(false);
            Courses.getInstance().setItem_pick_check(false);
            // Filtering 클릭 되게 하기
            setClickableFiltering(true);

            create_line.setText("R U N");
            create_line.setBackgroundResource(R.drawable.circle_board_start);

            if (pick_tedClusterItem != null) tedNaverClustering.addItem(pick_tedClusterItem);

            // CourseMarkers.get(index - 1).setHideCollidedMarkers(true);
        });

        like = Integer.valueOf(pick_item_mapData.getLike());
        tv_like_count.setText("좋아요 " + like + "개");

        DocumentReference dr_pickCourse;
        CollectionReference cr_pickCourse;

        // MY
        if (MY_checked) {
            like_button.setVisibility(View.INVISIBLE);

            dr_pickCourse = dr_myCourses.collection("courses")
                    .document(pick_item_mapData.getIndex());
            //
            dr_pickCourse.get().addOnCompleteListener(task -> {
                DocumentSnapshot document = task.getResult();
                String totally_index = document.get("totally_index").toString();
                Courses.getInstance().setPick_course_index(totally_index);
            });

            getNickNameOfCourseRegister(user.getUid());
        }
        // ALL
        else {
            dr_pickCourse = cr_totally_mapCourses.document(pick_item_mapData.getIndex());

            //
            Courses.getInstance().setPick_course_index(pick_item_mapData.getIndex());

            getNickNameOfCourseRegister(pick_item_mapData.getUid());

            //
            if (pick_item_mapData.getUid().equals(user.getUid())) {
                like_button.setVisibility(View.INVISIBLE);
            }
            else {
                like_button.setVisibility(View.VISIBLE);

                // 본인이 클릭 했는지 확인
                dr_myMap_like.collection("courses")
                        .whereEqualTo("index", Integer.valueOf(pick_item_mapData.getIndex()))
                        .get()
                        .addOnCompleteListener(task -> {

                            // 존재
                            if (task.getResult().size() == 1)
                                like_button.setChecked(true);
                            else
                                like_button.setChecked(false);
                        });

                // 클릭 이벤트
                like_button.setOnClickListener(v12 -> {

                    // default check (false)
                    Map<String, Object> map = new HashMap<>();
                    Map<String, Object> my_like_course = new HashMap<>();
                    my_like_course.put("index", Integer.valueOf(pick_item_mapData.getIndex()));

                    // 눌렀는데 check true -> 원래 false 였다는 것 -> 채워져야 함
                    if (like_button.isChecked()) {
                        like += 1;
                        dr_myMap_like.collection("courses")
                                .document(pick_item_mapData.getIndex()).set(my_like_course);

                    } else {
                        like -= 1;
                        dr_myMap_like.collection("courses")
                                .document(pick_item_mapData.getIndex()).delete();
                    }

                    pick_item_mapData.setLike(String.valueOf(like));
                    map.put("like", like);

                    // setText
                    tv_like_count.setText("좋아요 " + String.valueOf(like) + "개");

                    cr_totally_mapCourses.document(pick_item_mapData.getIndex()).update(map);
                });
            }
        }

        record_button.setOnClickListener(v12 -> {
            Intent intent = new Intent(getContext(), MapUFSRecordActivity.class);
            startActivity(intent);
        });

        cr_pickCourse = dr_pickCourse.collection("points");
        //
        cr_pickCourse.orderBy("index").get().addOnCompleteListener(task -> getLineDataOfCourse(task));

        // 분기점 마크
        dr_pickCourse.collection("quarters")
                .orderBy("index")
                .get()
                .addOnCompleteListener(task -> {

                    if (task.getResult().size() != 0) {
                        int quarter = 1;
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            double point_lat = Double.valueOf(document.get("lat").toString());
                            double point_long = Double.valueOf(document.get("long").toString());

                            LatLng latLng = new LatLng(point_lat, point_long);

                            Marker marker = new Marker();
                            marker.setPosition(latLng);
                            marker.setCaptionText((quarter++) + "구간");
                            marker.setCaptionAligns(Align.Top);
                            marker.setIconTintColor(Color.rgb(112, 148, 254));
                            quarter_markers.add(marker);
                            marker.setMap(naverMap);
                        }
                    }
                });
    }

    public void getQuarter_locations(Task<QuerySnapshot> task) {
        for (QueryDocumentSnapshot document : task.getResult()) {

            LatLng latLng = new LatLng(Double.valueOf(document.get("lat").toString()),
                    Double.valueOf(document.get("long").toString()));

            Courses.getInstance().addRecord_quarter_locations(latLng);
        }
    }

    public void getLineDataOfCourse(Task<QuerySnapshot> task) {
        // for 문 이지만 코스는 하나

        // ufs 초기화
        ufs_lat.clear();
        ufs_long.clear();
        ufs_time.clear();

        // kalman_ufs 초기화
        kalman_ufs_lat.clear();
        kalman_ufs_long.clear();

        ArrayList<LatLng> kalman_db_LatLngs = new ArrayList<>();
        ArrayList<LatLng> db_LatLngs = new ArrayList<>();
        ufs_start_marker = new Marker();
        ufs_arrive_marker = new Marker();
        int size = task.getResult().size();

        long prev_time = 0;

        // new Frame
        layout_ItemPick.setVisibility(View.VISIBLE);
        recycler_flame.setVisibility(View.INVISIBLE);

        // 거리
        double total_time = 0;
        // kalman 위치 가져오기
        KalmanFilter kalmanFilter = new KalmanFilter();

        // ufsOther = new UfsOther();

        for (QueryDocumentSnapshot document1 : task.getResult()) {
            Log.i("document", document1.getId());

            // 위치 가져오기
            double Lat = Double.valueOf(document1.get("lat").toString());
            double Long = Double.valueOf(document1.get("long").toString());

            // 처음 마커
            if (document1.getId().equals("1")) {

                LatLng db_latLng = new LatLng(Lat, Long);
                kalman_db_LatLngs.add(db_latLng);
                db_LatLngs.add(db_latLng);
                Log.i("check", "check!!check!!check!!check!!check!!check!!check!!");

                ufs_start_marker.setPosition(db_latLng);
                ufs_start_marker.setIcon(OverlayImage.fromResource(R.drawable.start));
                ufs_start_marker.setCaptionText("시작장소");
                ufs_start_marker.setMap(naverMap);

                // ufs
                ufs_lat.add(Double.valueOf(document1.get("lat").toString()));
                ufs_long.add(Double.valueOf(document1.get("long").toString()));
                prev_time = java.lang.Long.valueOf(document1.get("time").toString());
                Log.i("prev_time", String.valueOf(prev_time));

                // kalman ufs
                kalman_ufs_lat.add(Double.valueOf(document1.get("lat").toString()));
                kalman_ufs_long.add(Double.valueOf(document1.get("long").toString()));
                Courses.getInstance().setPrev_loc(db_latLng);
                Courses.getInstance().setPrev_time(prev_time);

                // speed, lat, long, time, altitude, accuracy

                // kalmanFilter
                kalmanFilter.setState(Lat, Long, prev_time, 2, 0, 0);

                // Camera
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(db_latLng)
                        .animate(CameraAnimation.Easing);
                naverMap.moveCamera(cameraUpdate);
            } else {

                LatLng db_latLng = new LatLng(Lat, Long);
                db_LatLngs.add(db_latLng);
                LatLng kalman_db_latLng;

                if (document1.getId().equals(String.valueOf(size))) {

                    arrive_loc = db_latLng;
                    ufs_arrive_marker.setPosition(db_latLng);
                    ufs_arrive_marker.setIcon(OverlayImage.fromResource(R.drawable.end));
                    ufs_arrive_marker.setCaptionText("도착장소");
                    ufs_arrive_marker.setMap(naverMap);
                }

                double g_t = Double.valueOf(document1.get("time").toString());
                double p_t = prev_time;
                int result_time;
                if (Math.round((g_t - p_t) / 1000) == 0)
                    result_time = 1;
                else result_time = (int) Math.round((g_t - p_t) / 1000);

                ////////////////////////////////////////////////////////////////////////////////////////////////////
                // kalman ufs
                // speed
                // distance
                double distance = db_latLng.distanceTo(Courses.getInstance().getPrev_loc());
                double time = (g_t - Courses.getInstance().getPrev_time()) / 1000;
                double speed = distance / time;

                kalmanFilter.process(speed, Lat, Long, (long) g_t, 5, 0, 0);

                kalman_ufs_lat.add(kalmanFilter.getLatitude());
                kalman_ufs_long.add(kalmanFilter.getLongitude());
                kalman_db_latLng = new LatLng(kalmanFilter.getLatitude(), kalmanFilter.getLongitude());
                kalman_db_LatLngs.add(kalman_db_latLng);

                Courses.getInstance().setPrev_time((long) g_t);
                Courses.getInstance().setPrev_loc(db_latLng);

                /*
                Log.i("ufs_lat", document1.get("lat").toString());
                Log.i("ufs_long", document1.get("long").toString());
                Log.i("kalman_ufs_lat", String.valueOf(kalmanFilter.getLatitude()));
                Log.i("kalman_ufs_long", String.valueOf(kalmanFilter.getLongitude()));

                Log.i("distance", String.valueOf(distance));
                Log.i("time", String.valueOf(time));
                Log.i("speed", String.valueOf(speed));
                Log.i("LINE", "//////////////////////////////////////////////////////////////////////////");
                 */
                ////////////////////////////////////////////////////////////////////////////////////////////////////

                // ufs
                ufs_lat.add(Double.valueOf(document1.get("lat").toString()));
                ufs_long.add(Double.valueOf(document1.get("long").toString()));
                ufs_time.add(result_time);
                Log.i("prev_time", String.valueOf(java.lang.Long.valueOf(document1.get("time").toString()) - prev_time));
                prev_time = java.lang.Long.valueOf(document1.get("time").toString());

            }
        }

        if (size != 1) {
            // registeredPath.setCoords(db_LatLngs);
            registeredPath.setCoords(kalman_db_LatLngs);
            registeredPath.setWidth(40);
            registeredPath.setColor(Color.rgb(228, 255, 96));
            registeredPath.setMap(naverMap);
        }


        /////////////////////////////////////////////////////////////////////////////////
        // R U N Button -> U F S Button
        // setText
        create_line.setText("FOLLOW");
        create_line.setBackgroundResource(R.drawable.circle_board_follow);

    }

    public void removeCourses(int size) {

        while (size > 1) {
            dataArrayList.remove(1);
            adapter.notifyItemRemoved(1);
            size--;
        }
    }

    public void getAllCourses() {

        // 처음은 모두 보기
        filtering.setToggleButton(filter_all);
        filtering.setFilter_name("index");
        adapter.setCurrent_filter_name("index");

        Log.i("MY_checked", MY_checked + "");
        // 전체 코스 보여지는 상태
        if (!MY_checked) {
            Log.i("getAllCourses", "MY_checked");
            cr_totally_mapCourses
                    .orderBy("index", filtering.getDirection())
                    .get()
                    .addOnCompleteListener(task -> {
                        Log.i("cr_totally_mapCourses", "cr_totally_mapCourses");
                        getCourses(task);
                    });
        }
        // 마이코스 보여지는 상태
        // 마이코스 인 상태에서 ALL 필터링을 누를 시 필터링된 마이코스가 나와야 함
        else {
            Log.i("getAllCourses", "MY_NO_checked");
            getMyCoursesFilteringData(filtering.getFilter_name());
        }
    }

    public void getCourses(Task<QuerySnapshot> task) {

        MapData myData;
        int index = 1;

        // int marker_run_index = 0;

        // 아이템이 계속 추가 되는 것을 방지.
        if (tedNaverClustering != null) tedNaverClustering.clearItems();

        if (filtering.getFilter_name().equals("ran") || filtering.getFilter_name().equals("like")) {
            Log.i("ran_like_check", ran_like_check + "");
            if (ran_like_check) {
                if (ran_like_count == 2) {
                    // javaItems.clear();
                    Log.i("count", ran_like_count + "");
                    ran_like_check = false;
                } else ran_like_count -= 1;
            } else {
                javaItems.clear();
                ran_like_check = true;
            }
        }
        else javaItems.clear();

        for (QueryDocumentSnapshot document : task.getResult()) {

            Log.i("index", index + "  ");
            Log.i("dataArrayList", dataArrayList.get(0) + "  " + dataArrayList.size());

            myData = new MapData();

            String name = document.get("name").toString();
            double start_point_lat = Double.valueOf(document.get("start_point_lat").toString());
            double start_point_long = Double.valueOf(document.get("start_point_long").toString());
            double finish_point_lat = Double.valueOf(document.get("finish_point_lat").toString());
            double finish_point_long = Double.valueOf(document.get("finish_point_long").toString());
            String total_distance = document.get("total_distance").toString();

            //
            myData.setStart_lat(String.valueOf(start_point_lat));
            myData.setStart_long(String.valueOf(start_point_long));
            myData.setFinish_lat(String.valueOf(finish_point_lat));
            myData.setFinish_long(String.valueOf(finish_point_long));

            myData.setCourse_name(name);
            myData.setIndex(document.getId());
            myData.setLike(document.get("like").toString());
            if (document.get("uid") != null) myData.setUid(document.get("uid").toString());

            myData.setCourseDist(total_distance);
            Courses.getInstance().setCourse_distance(Double.valueOf(total_distance));

            myData.setKcal(document.get("kcal").toString());
            myData.setTime(document.get("total_time").toString());

            // Log.i("start_point_lat", start_point_lat + "");
            // Log.i("start_point_long", start_point_long + "");
            //
            LatLng latLng = new LatLng(start_point_lat, start_point_long);
            JavaItem javaItem = new JavaItem(latLng);
            javaItems.add(javaItem);

            /*
            // 마커 표시
            Marker marker = new Marker();
            marker.setPosition(new LatLng(start_point_lat, start_point_long));
            // marker.setTag(document.getId());
            marker_run_index = marker_run_index % 5;
            // marker.setIcon(OverlayImage.fromResource(marker_run.get(marker_run_index)));
            marker.setHideCollidedMarkers(true);
            marker.setCaptionText(String.valueOf(Math.round(
                    Double.valueOf(total_distance) / 1000 * 100) / 100.0));
            marker.setCaptionMinZoom(17);

            marker.setTag(index);
            marker.setOnClickListener(overlay -> {
                user_course_remove();
                int item_index = Integer.valueOf(overlay.getTag().toString());

                pick_item_mapData = dataArrayList.get(item_index);
                Log.i("item_index", item_index + "");
                course_pick_event(item_index);
                return false;
            });
            CourseMarkers.add(marker);
            // marker.setMap(naverMap);

             */

            // Log.i("dataArrayList", dataArrayList.get(0) + "  " + dataArrayList.size());
            dataArrayList.add(index, myData);
            adapter.notifyItemInserted(index);
            index++;
            // marker_run_index++;
        }

        tedNaverClustering =
                TedNaverClustering.Companion.with(getContext(), naverMap)
                        .customMarker(tedClusterItem -> {

                            Marker marker = new Marker();
                            LatLng latLng = new LatLng(tedClusterItem.getTedLatLng().getLatitude(),
                                    tedClusterItem.getTedLatLng().getLongitude());
                            marker.setPosition(latLng);
                            marker.setIcon(OverlayImage.fromResource(R.drawable.pngwing_run2));

                            return marker;
                        })
                        .markerClickListener(tedClusterItem -> {

                            //
                            user_course_remove();

                            String lat = String.valueOf(tedClusterItem.getTedLatLng().getLatitude());

                            for (int i = 1; i < dataArrayList.size(); i++) {
                                MapData mapData = dataArrayList.get(i);
                                if (mapData.getStart_lat().equals(lat)) {
                                    ted_pick_point = i;
                                    Log.i("point", ted_pick_point + "");
                                    break;
                                }
                            }

                            pick_tedClusterItem = tedClusterItem;
                            tedNaverClustering.removeItem(tedClusterItem);

                            pick_item_mapData = adapter.getData(ted_pick_point);
                            course_pick_event(ted_pick_point);

                            return null;
                        })
                        .clusterClickListener(tedClusterItemCluster -> {
                            double zommLevel = cameraPosition.zoom;
                            LatLng latLng = new LatLng(tedClusterItemCluster.getPosition().getLatitude(),
                                    tedClusterItemCluster.getPosition().getLongitude());

                            CameraPosition cameraPosition = new CameraPosition(latLng, ++zommLevel // 줌 레벨
                            );
                            naverMap.setCameraPosition(cameraPosition);
                            return null;
                        })
                        .customCluster(tedClusterItemCluster -> {

                            TextView textView = new TextView(getContext());
                            textView.setBackgroundResource(R.drawable.pngwing_with_run);
                            textView.setText(String.valueOf(tedClusterItemCluster.getSize()));
                            textView.setTextColor(Color.rgb(60, 110, 254));
                            textView.setTextSize(20);
                            textView.setTypeface(null, Typeface.BOLD);

                            return textView;
                        })
                        .make();
        tedNaverClustering.addItems(javaItems);

        Log.i("javaItems", javaItems.size() + "");
    }

    public void course_data_record() {
        double total_distance = Courses.getInstance().getTotal_distance();

        for (int i = 0; i < Courses.getInstance().getLocations().size(); i++) {

            Map<String, Object> map = new HashMap<>();

            Location location = Courses.getInstance().getLocations().get(i);

            // 좌표
            map.put("index", i + 1);
            map.put("lat", location.getLatitude());
            map.put("long", location.getLongitude());
            // map.put("location", Courses.getInstance().getLocations().get(i));

            Courses.getInstance().setPrev_time(Courses.getInstance().getTimes().get(i));
            // 시간
            map.put("time", Courses.getInstance().getTimes().get(i));

            // SAVE
            cr_totally_mapCourses.document(pick_course_id).collection("TOP20")
                    .document(user.getUid()).collection("points")
                    .document(String.valueOf(i + 1)).set(map);
        }

        // kcal
        double kcal = calculator.kcal_result(
                Integer.valueOf(calculator.time_result(total_distance).split("[ ]")[0]));

        today_date.setNow();

        // 바로 저장
        Map<String, Object> course_data = new HashMap<>();

        course_data.put("total_distance", total_distance);
        course_data.put("kcal", kcal);
        course_data.put("uid", user.getUid());
        course_data.put("nickName", Sub_bundle.getInstance().getNickName());
        course_data.put("total_time", MapSingleTon.getInstance().getTOTAL_SEC());
        course_data.put("date", today_date.getFormat_date());
        course_data.put("point_count", Courses.getInstance().getLocationsSize());

        cr_totally_mapCourses.document(pick_course_id).collection("TOP20")
                .document(user.getUid()).set(course_data);

    }

    public void set_my_marker(LatLng latLng) {
        my_marker = new Marker();
        my_marker.setPosition(latLng);
        my_marker.setIcon(OverlayImage.fromResource(R.drawable.user_marker_black));
        my_marker.setCaptionText("나");
        my_marker.setCaptionAligns(Align.Top);
        // GpsMarkers.add(my_marker);
        my_marker.setMap(naverMap);
    }

    public void user_course_remove() {

        layout_ItemPick.setVisibility(View.INVISIBLE);
        recycler_flame.setVisibility(View.VISIBLE);

        // holder.getButton().setChecked(false);

        registeredPath.setMap(null);

        ufs_start_marker.setMap(null);
        ufs_arrive_marker.setMap(null);
        // 상대 마커
        if (ufs_marker != null) ufs_marker.setMap(null);

        // update
        ///////////////////////
        for (Marker marker : quarter_markers) marker.setMap(null);
        quarter_markers.clear();
        ///////////////////////

        markers.clear();
    }

    public void getNickNameOfCourseRegister(String uid) {
        db.collection("Users").document(uid)
                .collection("Profile").document("diet_profile")
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot document = task.getResult();
                    String nickName = document.get("nickName").toString();
                    pick_item_mapData.setNickName(nickName);
                });

    }

    // 코스 등록을 취소 할 때 ( R U N , FOLLOW 취소 시)

    // MARKER
    /////////////////////////////////////////////////////////////////////////////////////////


    // INITIAL
    ///////////////////////////////////////////////////////////////////////////////////////

    public void create_line_record_cancel() {
        Log.i("onNegativeClicked", "check!!");

        MapSingleTon.getInstance().setRunning_check(false);
        create_line.setText("R U N");
        create_line.setBackgroundResource(R.drawable.circle_board_start);

        // 타이머 멈춤
        stopCourseTimer();

        MapSingleTon.getInstance().clearCoords();
        if(path != null)
            path.setMap(null);

        MapSingleTon.getInstance().clearKalman_coords();
        if(kalman_path != null)
            kalman_path.setMap(null);

        if(my_marker != null)
            my_marker.setMap(null);
    }

    public void create_line_start() {
        countDown_frame.setVisibility(View.VISIBLE);
        linearMap.setVisibility(View.INVISIBLE);

        //
        run_data_frame.setVisibility(View.VISIBLE);
        running_timer.setVisibility(View.VISIBLE);
        recycler_flame.setVisibility(View.INVISIBLE);
        layout_ItemPick.setVisibility(View.INVISIBLE);

        txtKcalMap.setText("속도");

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) run_data_frame.getLayoutParams();
        params.width = 300; // 300
        params.height = 160; // 245
        run_data_frame.setLayoutParams(params);

        run_timer_check = true;

        // 경로 검색 클릭 시 백그라운드 권한 허용창 뜨게 하기

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        }

        // GPS 권한
        if (!(PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PermissionChecker.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            Log.i("ACCESS_BACKGROUND", "check!!");
        }

        // 베터리 절전 모드 해제 권한
        PowerManager pm = (PowerManager) getActivity().getSystemService(POWER_SERVICE);
        boolean isWhiteListing = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(getActivity().getPackageName());
        }
        if (!isWhiteListing) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
            startActivity(intent);
        }

        // valuable initial
        check_count = 0;
        markerTag = 0;
        GpsMarkers.clear();
        GpsMarkers2.clear();
        MapSingleTon.getInstance().clearCoords();

        // 현재 위치와 지도가 같이 움직이도록 설정
        this.naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        MapSingleTon.getInstance().setRunning_check(true);
    }

    // TIMER
    ///////////////////////////////////////////////////////////////////////////////////////

    public void StartOthersOfTimerScheduler() {

        Log.i("TimerScheduler", "StartOthersOfTimerScheduler");
        for (int i = 0; i < ufsOthers.size(); i++) {
            ufsOthers.get(i).startTimer(getActivity(), markerIcons.get(i), timer, naverMap, textToSpeech);
        }
    }

    public void stopOthersOfTimer() {
        for (int i = 0; i < ufsOthers.size(); i++) {
            ufsOthers.get(i).stopTimerTask();
        }
    }

    public void startUFSTimer() {

        timer_count = 0;
        index_count = 0;
        ufs_marker = new Marker();
        ufs_marker.setIcon(OverlayImage.fromResource(R.drawable.marker_user_green));
        ufs_marker.setCaptionText("상대");
        ufs_marker.setCaptionAligns(Align.Top);

        //
        Courses.getInstance().ClearSolo_qua_index();

        timer_UFSTask = new TimerTask() {

            @Override
            public void run() {

                Log.i("ufs_time", String.valueOf(ufs_time.get(index_count)));

                    getActivity().runOnUiThread(() -> {

                        // 다음 좌표까지 걸린 시간 확인
                        if (++timer_count == ufs_time.get(index_count)) {

                            // kalman
                            LatLng kal_tem_latLng = new LatLng(kalman_ufs_lat.get(index_count + 1), kalman_ufs_long.get(index_count + 1));
                            Log.i("After Point", ufs_lat.get(index_count + 1) + "  " + ufs_long.get(index_count + 1));
                    /*
                    LatLng tem_latLng = new LatLng(ufs_lat.get(index_count + 1), ufs_long.get(index_count + 1));
                    Log.i("After Point", ufs_lat.get(index_count + 1) + "  " + ufs_long.get(index_count + 1));

                     */
                            // 첫번째 움직였을때
                            if (index_count == 0) {

                                ufs_marker.setPosition(kal_tem_latLng);
                                // ufs_marker.setPosition(tem_latLng);

                                // 첫번째 움직였을때 마커 그리기
                                ufs_marker.setMap(naverMap);
                                Log.i("index_count == 0", ufs_time.get(index_count) + " ");
                                Log.i("index_count == 0", ufs_marker.getPosition().latitude +
                                        " " + ufs_marker.getPosition().longitude);
                            }
                            else {

                                if (index_count == ufs_time.size() - 1) {
                                    Log.i("stopTimerTask", "STOP!!");

                                    // 마커 null
                                    // Draw
                                    ufs_marker.setMap(null);

                                    // 새로 마크 표시
                                    ufs_marker = new Marker();
                                    ufs_marker.setIcon(OverlayImage.fromResource(R.drawable.marker_user_green));
                                    ufs_marker.setPosition(kal_tem_latLng);
                                    // ufs_marker.setPosition(tem_latLng);
                                    ufs_marker.setCaptionText("상대");
                                    ufs_marker.setCaptionAligns(Align.Top);

                                    ufs_marker.setMap(naverMap);
                                    Log.i("index_count", ufs_marker.getPosition().latitude +
                                            " " + ufs_marker.getPosition().longitude);

                                    stopUFSTimerTask();
                                    return;
                                }

                                // 마크 지우기, 라인 그리기
                                ufs_marker.setMap(null);

                                Log.i("index_count ", ufs_marker.getPosition().latitude +
                                        " " + ufs_marker.getPosition().longitude);

                                // 새로 마크 표시
                                ufs_marker = new Marker();
                                ufs_marker.setIcon(OverlayImage.fromResource(R.drawable.marker_user_green));
                                ufs_marker.setPosition(kal_tem_latLng);
                                // ufs_marker.setPosition(tem_latLng);
                                ufs_marker.setCaptionText("상대");
                                ufs_marker.setCaptionAligns(Align.Top);

                                ufs_marker.setMap(naverMap);
                                Log.i("index_count", ufs_marker.getPosition().latitude +
                                        " " + ufs_marker.getPosition().longitude);

                                if (MapSingleTon.getInstance().isQuarter_check()) {

                                    if (Courses.getInstance().getRecord_quarter_locations().size() !=
                                            Courses.getInstance().getUser_qua_index()) {

                                        if (MapSingleTon.getInstance().getCurrent_loc().distanceTo(
                                                Courses.getInstance().getRecord_quarter_locations().get(
                                                        Courses.getCoursesInstance().getSolo_qua_index())) < 30) {

                                            Courses.getInstance().speeching(pick_item_mapData.getNickName() + "님이" +
                                                    Courses.getInstance().getSolo_qua_index() + 1 + "구간을 지나고 있습니다.");
                                            // check
                                            Courses.getInstance().PlusSolo_qua_index();
                                            // 나중에 코스를 등록할 시 check_count 랑 분기점들 갯수를 비교하여 확인할 것.
                                        }
                                    }
                                }
                            }
                            index_count++;
                            timer_count = 0;
                            Log.i("timer_count", timer_count + "");
                        }

                    });

            }
        };

        timer.schedule(timer_UFSTask, 0, 1000);

    }

    public void stopUFSTimerTask() {
        if (timer_UFSTask != null) {
            timer_UFSTask.cancel();
            timer_UFSTask = null;
        }
    }

    public static boolean isAppOnForeground(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        if(appProcessInfos == null){
            return false;
        }
        final String pakageName = context.getPackageName();
        for(ActivityManager.RunningAppProcessInfo appProcess : appProcessInfos){
            if(appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcess.processName.equals(pakageName)){
                return true;
            }
        }
        return false;
    }

    public void startCourseTimer() {


        MapSingleTon.getInstance().clearTOTAL_SEC();
        // total_sec = 0;
        Courses.getInstance().clearTotal_second();

        startCourse_sec = 0;
        timeFormat = (startCourse_sec) + "초";

        timer_courseTask = new TimerTask() {

            @Override
            public void run() {

                if (run_timer_check) {
                    if (startCourse_sec >= 60) {
                        startCourse_minute += 1;
                        startCourse_sec = 0;
                        timeFormat = String.format("%02d", startCourse_hour) + ":" +
                                String.format("%02d", startCourse_minute) + ":" + String.format("%02d", startCourse_sec);

                        if (startCourse_minute >= 60) {
                            startCourse_hour += 1;
                            startCourse_minute = 0;
                            timeFormat = String.format("%02d", startCourse_hour) + ":" +
                                    String.format("%02d", startCourse_minute) + ":" + String.format("%02d", startCourse_sec);
                        }
                    }
                    timeFormat = String.format("%02d", startCourse_hour) + ":" +
                            String.format("%02d", startCourse_minute) + ":" + String.format("%02d", startCourse_sec);

                    if(isAppOnForeground(getContext()))
                        getActivity().runOnUiThread(() -> tv_timer.setText(timeFormat));
                    startCourse_sec++;

                    MapSingleTon.getInstance().addTOTAL_SEC();
                    // total_sec++;
                    Courses.getInstance().addTotal_second();
                }
                else{
                    stopCourseTimer();
                    return;
                }

            }
        };
        timer.schedule(timer_courseTask, 0, 1000);
    }

    public void stopCourseTimer(){
        if(timer_courseTask != null){
            timeFormat = String.format("%02d", 0) + ":" +
                    String.format("%02d", 0) + ":" + String.format("%02d", 0);
            getActivity().runOnUiThread(() -> tv_timer.setText(timeFormat));
            timer_courseTask.cancel();
            timer_courseTask = null;
            run_timer_check = false;
            startCourse_sec = 0;
            startCourse_minute = 0;
            startCourse_hour = 0;
        }
    }

    public void startCountDownTimer(){

        startCourse_sec = 10;
        start_count_flag = true;

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer = null;
        }

        if(guide_check) Courses.getInstance().setLanguageSpeech(Locale.ENGLISH);

        timer_countDownTask = new TimerTask() {
            @Override
            public void run() {

                getActivity().runOnUiThread(() -> {
                    Log.i("startCourse_sec", startCourse_sec + "");
                    if(startCourse_sec == 0){
                        stopCountDownTimer();
                        return;
                    }
                    if(MapSingleTon.getInstance().isStart_check()) {
                        if (guide_check)
                            Courses.getInstance().speeching(String.valueOf(startCourse_sec));
                    }
                    count_down.setText(String.valueOf(startCourse_sec--));
                });
            }
        };
        timer.schedule(timer_countDownTask, 0, 1000);
    }

    public void stopCountDownTimer(){
        if (timer_countDownTask != null) {
            Log.i("stopCountDownTimer",  "check!!");
            start_count_flag = false;
            countDown_frame.setVisibility(View.INVISIBLE);
            linearMap.setVisibility(View.VISIBLE);
            timer_countDownTask.cancel();
            timer_countDownTask = null;

            if(mediaPlayer == null){
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.shot);
                mediaPlayer.start();
            }

            startCourseTimer();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    public void requestPermission() {
        ActivityCompat.requestPermissions(
                requireActivity(), PERMISSIONS,
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("onStart", "onStart!!");

        // 위치 서비스가 실행 되고 있었다면
        if(!Courses.getInstance().isStopService()) {


            Courses.getInstance().setStopService(true);
            getActivity().stopService(serviceIntent);
            serviceIntent = null;

            int size = Courses.getInstance().getLatLngs().size();
            int i=0;

            Log.i("BACKGROUND SIZE", size + "");

            if(size != 0) {
                if(!first_point_check){

                    set_my_marker(Courses.getInstance().getLatLngs().get(0));
                    MapSingleTon.getInstance().addCoords(Courses.getInstance().getLatLngs().get(0));
                    MapSingleTon.getInstance().addKalman_coords(Courses.getInstance().getLatLngs().get(0));
                    path.setCoords(MapSingleTon.getInstance().getCoords());
                    kalman_path.setCoords(MapSingleTon.getInstance().getKalman_coords());
                    i++;
                    first_point_check = true;
                }

                long result_time = System.currentTimeMillis() - Courses.getInstance().getPrev_main_time();

                my_marker.setMap(null);

                path.setCoords(MapSingleTon.getInstance().getCoords());
                path.setColor(Color.RED);
                path.setWidth(12);
                kalman_path.setCoords(MapSingleTon.getInstance().getKalman_coords());
                kalman_path.setColor(Color.BLUE);
                kalman_path.setWidth(12);

                path.setMap(naverMap);
                kalman_path.setMap(naverMap);

                // 코스 등록시 라인, 마커 그리기
                /*
                for(;i<size;i++){

                    LatLng latLng = Courses.getInstance().getLatLngs().get(i);
                    LatLng kalman_lat = Courses.getInstance().getKalman_latLngs().get(i);
                    Log.i("CHECK COURSES", latLng.latitude + "  " + latLng.longitude);

                    // my_marker remove
                    my_marker.setMap(null);

                    set_my_marker(latLng);

                    Log.i("GpsMarkers.size()", "0");
                    Marker background_marker = new Marker();
                    background_marker.setPosition(latLng);
                    background_marker.setCaptionText("b");
                    background_marker.setIcon(MarkerIcons.BLUE);
                    background_marker.setTag(markerTag++);
                    // GpsMarkers.add(background_marker);
                    coords.add(latLng);
                    path.setCoords(coords);
                    path.setColor(Color.RED);
                    path.setWidth(12);

                    // kalman
                    Marker kalman_marker = new Marker();
                    kalman_marker.setPosition(kalman_lat);
                    kalman_marker.setCaptionText("b_k");
                    kalman_marker.setIcon(MarkerIcons.YELLOW);
                    kalman_coords.add(kalman_lat);
                    kalman_path.setCoords(kalman_coords);
                    kalman_path.setColor(Color.BLUE);
                    kalman_path.setWidth(12);

                    Log.i("coords", String.valueOf(coords.size()));
                    // background_marker.setMap(naverMap);
                    // kalman_marker.setMap(naverMap);
                    path.setMap(naverMap);
                    kalman_path.setMap(naverMap);
                }

                 */

                Courses.getInstance().clearLatLng();
                Courses.getInstance().clearKalman_latLngs();
            }
            else{

                Log.i("Courses Data ", "NULL!!");
            }
        }
    }

    // 사용자가 프래그먼트를 떠나면 첫번 째로 이 메서드를 호출합니다.
    // 사용자가 돌아오지 않을 수도 있으므로 여기에 현재 사용자 세션을 넘어 지속되어야 하는 변경사항을 저장합니다

    @Override
    public void onPause() {
        super.onPause();
        Log.i("onPause", "onPause!!");

    }

    // onPause()대신 onStop()을 사용하면 사용자가 멀티 윈도우 모드에서 Activity 를 보고 있더라도 UI 관련 작업이 계속 진행 됩니다.

    @Override
    public void onResume() {

        super.onResume();
        Log.i("onResume", "onResume!!!");
    }

    @Override
    public void onStop() {
        super.onStop();

        locationTrackingMode = this.naverMap.getLocationTrackingMode();

        Log.i("onStop", "check!!");
        if (locationManager != null) {
            Log.i("locationManager != null", "check!!");
            // 위치 추적 모드 활성화 되어있을 시 서비스 실행
            // 지금은 임시로 반대로해놓음

            // 현재 트래킹 모드인지 확인인
            if(locationTrackingMode == LocationTrackingMode.NoFollow ||
                    locationTrackingMode == LocationTrackingMode.Follow ||
                    locationTrackingMode == LocationTrackingMode.Face){

                // 경로 만들기를 눌렀는지 확인 필요
                if(MapSingleTon.getInstance().isRunning_check()) {

                    if(MapAlarmService.serviceIntent == null){
                        Log.i("MapAlarmService", "START");


                        // 서비스를 종료할지 확인하는 FLAG
                        Courses.getInstance().setStopService(false);

                        serviceIntent = new Intent(getContext(), BackgroundLocationUpdateService.class);
                        getActivity().startService(serviceIntent);

/*
                        // 서비스를 종료할지 확인하는 FLAG
                        Courses.getInstance().setStopService(false);

                        serviceIntent = new Intent(getContext(), MapAlarmService.class);
                        getActivity().startService(serviceIntent);

                         */


                         /*

                        serviceIntent = new Intent(getContext(), MapService.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            getActivity().startForegroundService(serviceIntent);
                        }

                          */

                        Courses.getInstance().setPrev_main_time(System.currentTimeMillis());

                    }
                    else{
                        /*
                        serviceIntent = MapAlarmService.serviceIntent;
                        Log.i("MapAlarmService", "Already");

                         */
                    }
                    Log.i("startService", "check!!");
                }
            }
            else Log.i("removeUpdates", "check!!");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("onDestroyView", "onDestroyView!!");
    }

    private void parseResult3(ArrayList<MapData> item)
    {
        ArrayList<MapData> dataArrayList = new ArrayList<>();
        for (int i = 0; i < item.size(); i++){
            MapData data = new MapData();
            data.setCourse_name(item.get(i).getCourse_name());
            dataArrayList.add(data);
            adapter = new MapAdapter(dataArrayList);
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.go) stopCountDownTimer();

        if(!MapSingleTon.getInstance().isRunning_check()) {

            switch (v.getId()) {
                case R.id.filter_all:
                    getFilteringData("index");
                    break;
                case R.id.filter_distance:
                    getFilteringData("total_distance");
                    break;
                case R.id.filter_kcal:
                    getFilteringData("kcal");

                    // setText
                    break;
                case R.id.filter_ran:
                    getFilteringData("ran");
                    break;

                case R.id.filter_like:
                    getFilteringData("like");
                    break;

                case R.id.filter_merge:
                    // 높은 순
                    if (filter_merge.isChecked()) {
                        Log.i("filtering.setMerge", "high");
                        filtering.setMerge(true);
                    }
                    // 낮은 순
                    else {
                        Log.i("filtering.setMerge", "low");
                        filtering.setMerge(false);
                    }

                    String filter_name = filtering.getFilter_name();
                    // 삭제
                    removeCourses(dataArrayList.size());
                    // 추가
                    getFilteringData(filter_name);

                    break;
                case R.id.tv_solo:
                    set_follow_mode("solo");
                    break;

                case R.id.tv_many:

                    set_follow_mode("many");

                    // 다수의 데이터를 가져와야 함
                    cr_totally_mapCourses.document(pick_item_mapData.getIndex())
                            .collection("TOP20")
                            .get()
                            .addOnCompleteListener(task -> {

                                int size = task.getResult().size();
                                Log.i("size", size + "");

                                if (size == 0) {
                                    // dialog
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                            .setTitle("확인")
                                            .setMessage("기록된 데이터가 없습니다. 다른 모드로 진행해주세요.!!")
                                            .setNegativeButton("예", (dialog, which) -> {
                                                set_follow_mode("solo");
                                                dialog.dismiss();
                                            });

                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                    return;

                                } else {
                                    String[] seq = new String[size];
                                    int[] point_counts = new int[size];
                                    String[] nickName = new String[size];
                                    int count = 0;

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        seq[count] = document.getId();
                                        point_counts[count] = Integer.valueOf(document.get("point_count").toString());
                                        nickName[count] = document.get("nickName").toString();

                                        Log.i("nickName", document.get("nickName").toString());
                                        count++;
                                    }

                                    int point = 0;
                                    boolean eq_check = false;
                                    Random r = new Random();
                                    int[] randoms = new int[3];
                                    while (point < 3 && point < size) {
                                        if (randoms == null) {
                                            Log.i("randoms", "null");
                                            randoms[point] = r.nextInt(size);
                                            point++;
                                        } else {
                                            int ran = r.nextInt(size);
                                            for (int i = 0; i < point; i++) {
                                                if (randoms[i] == ran) {
                                                    eq_check = true;
                                                    break;
                                                } else eq_check = false;
                                            }
                                            if (!eq_check) {
                                                randoms[point] = ran;
                                                point++;
                                            }
                                        }
                                    }

                                    int k = 0;

                                    Log.i("randoms", randoms[0] + "");
                                    Log.i("count", count + "");
                                    // 최대 3개까지 가져오며 3개 보다 작을 경우 그 개수만큼 가져옴
                                    while (k < 3 && k < size) {
                                        UfsOther uo = new UfsOther();
                                        uo.setUid(seq[randoms[k]]);
                                        uo.setNickName(nickName[randoms[k]]);
                                        Log.i("k", k + "");

                                        uo.clearUfs_distance();

                                        ran_like_check = false;
                                        cr_totally_mapCourses.document(pick_item_mapData.getIndex())
                                                .collection("TOP20").document(seq[randoms[k]])
                                                .collection("points")
                                                .whereLessThanOrEqualTo("index", point_counts[randoms[k++]])
                                                .get()
                                                .addOnCompleteListener(task1 -> get_random_others_follow_data(task1, uo));
                                    }
                                }
                            });
                    break;
                case R.id.tv_me:

                    set_follow_mode("me");

                    //
                    CollectionReference my_ran = db.collection("Users").document(user.getUid())
                            .collection("Map").document("RAN")
                            .collection("Courses").document(pick_course_id)
                            .collection("points");

                    my_ran.get().addOnCompleteListener(task -> {

                        if (task.getResult().isEmpty()) {
                            Log.i("isEmpty", "check!!");
                            // dialog
                            // dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                    .setTitle("확인")
                                    .setMessage("기록된 데이터가 없습니다. 다른 모드로 진행해주세요.!!")
                                    .setNegativeButton("예", (dialog, which) -> {
                                        set_follow_mode("solo");
                                        dialog.dismiss();
                                    });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return;
                        } else {
                            Log.i("isEmpty", "non check!!");
                            ran_like_check = false;
                            get_random_others_follow_data(task, ufsOther);
                        }
                    });
                    break;
            }
        }
        return;
    }

    // Filtering Function
    ///////////////////////////////////////////////////////////////////////////////////////

    public void getMyCoursesFilteringData(String filter_name){


        // DB My Courses 이름 먼저 가져오기
        dr_myCourses.collection("courses")
                .orderBy(filter_name, filtering.getDirection())
                .get()
                .addOnCompleteListener(task -> {
                    my_courses_size = task.getResult().size();
                    getCourses(task);
                });
    }

    public void getFilteringData(String filter_name){
        switch(filter_name){
            case "index":
                if(filter_all.isChecked()){
                    //
                    filtering.setFilter_name("index");
                    //
                    adapter.setCurrent_filter_name("index");

                    // checking + image 변경
                    filtering.changedChecked(filter_all);

                    // 제거
                    removeCourses(dataArrayList.size());

                    //
                    getAllCourses();

                }
                break;
            case "total_distance":
                filtering_courses_setting(filter_distance, "total_distance");
                break;
            case "kcal":
                filtering_courses_setting(filter_kcal, "kcal");
                break;
            case "ran":
                if(filter_ran.isChecked()){

                    //
                    filtering.setFilter_name("ran");
                    adapter.setCurrent_filter_name("ran");
                    // checking + image 변경
                    filtering.changedChecked(filter_ran);

                    // 제거
                    removeCourses(dataArrayList.size());

                    db.collection("Users").document(user.getUid())
                            .collection("Map").document("RAN")
                            .collection("Courses")
                            .orderBy("index", filtering.getDirection())
                            .get()
                            .addOnCompleteListener(task -> {

                                ran_like_count = task.getResult().size();

                                for(QueryDocumentSnapshot document : task.getResult()){

                                    Log.i("document", document.getId());
                                    cr_totally_mapCourses
                                            .whereEqualTo("index", Integer.valueOf(document.getId()))
                                            .get()
                                            .addOnCompleteListener(task1 -> { getCourses(task1); });
                                }
                            });
                }
                break;
            case "like":
                if(filter_like.isChecked()){
                    //
                    filtering.setFilter_name("like");
                    adapter.setCurrent_filter_name("like");
                    // checking + image 변경
                    filtering.changedChecked(filter_like);

                    // 제거
                    removeCourses(dataArrayList.size());

                    dr_myMap_like.collection("courses")
                            .orderBy("index", filtering.getDirection())
                            .get()
                            .addOnCompleteListener(task -> {

                                ran_like_count = task.getResult().size();

                                for(QueryDocumentSnapshot document : task.getResult()){

                                    cr_totally_mapCourses
                                            .whereEqualTo("index", Integer.valueOf(document.getId()))
                                            .get()
                                            .addOnCompleteListener(task1 -> { getCourses(task1); });
                                }


                            });

                }
                break;
        }
    }

    public void filtering_courses_setting(ToggleButton toggleButton, String filter){

        // 안눌려져 있었음
        // 보여줌 + 원래 필터링 되어있던거 제거
        if(toggleButton.isChecked()){

            //
            filtering.setFilter_name(filter);
            adapter.setCurrent_filter_name(filter);

            // checking + image 변경
            filtering.changedChecked(toggleButton);

            // 제거
            removeCourses(dataArrayList.size());

            if(!MY_checked) {
                // Add
                cr_totally_mapCourses.orderBy(filter, filtering.getDirection())
                        .get()
                        .addOnCompleteListener(task -> getCourses(task));
            }
            else getMyCoursesFilteringData(filtering.getFilter_name());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    // Toolbar
    ///////////////////////////////////////////////////////////////////////////////////////

    private void initToolbar() {

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initMenuFragment() {

        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);        // permet de fermer le menu en cliquant sur une zone sans bouton
        contextMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);

        contextMenuDialogFragment.setMenuItemClickListener((view, integer) -> {

            switch (integer){
                case 1:
                    Log.i("eraser_fab", "check!!");

                    // U F S 중 일 때
                    if(ufs_marker != null){
                        if(ufs_marker.getPosition() != null) ufs_marker.setMap(null);
                    }

                    if(ufs_latLngs != null){
                        if(ufs_latLngs.size() != 0) ufs_latLngs.clear();
                    }

                    if(ufs_path != null){
                        if(ufs_path.getCoords().size() != 0) ufs_path.setMap(null);
                    }

                    if (timer_UFSTask != null) {
                        timer_UFSTask.cancel();
                        timer_UFSTask = null;
                    }

                    // 그림 그릴 때
                    Log.i("markerFlag", drawer_marker_flag + "");
                    Log.i("clearFlag", clearFlag + "");
                    Log.i("clickLists", clickLists.size() + "");
                    Log.i("markerCourseList", markerCourseList.size() + "");
                    if (drawer_marker_flag) {
                        if (clickLists.size() > 0) {
                            for (Marker marker : markerCourseList)
                                marker.setMap(null);
                            clickPath.setMap(null);
                            clickLists.clear();
                            markerCourseList.clear();
                            drawer_marker_flag = false;
                            clearFlag = false;
                            total_dist = 0;
                            txtDistMap.setText("");
                            txtTimeMap.setText("");
                            txtKcalMap.setText("");
                        }
                    }
                    break;
                // 안내
                case 2:
                    // new Activity
                    // Intent intent = new Intent(getContext(), GuideActivity.class);
                    // startActivity(intent);

                    GuideSoundDialogue dialogue = new GuideSoundDialogue();
                    dialogue.show(getParentFragmentManager(),"Dialog");
                    break;

                case 3:
                    drawFlag = !drawFlag;
                    if(drawFlag) {
                        run_data_frame.setVisibility(View.VISIBLE);
                        draw_btn.setBackgroundResource(R.drawable.circle_board2);
                    }
                    else {
                        run_data_frame.setVisibility(View.INVISIBLE);
                        draw_btn.setBackgroundResource(R.drawable.circle_board);
                    }


                    break;

            }

            return null;
        });
    }

    private List<MenuObject> getMenuObjects() {

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResourceValue(R.drawable.icn_close);

        MenuObject eraser = new MenuObject("지우개");
        eraser.setResourceValue(R.drawable.eraser_2);

        MenuObject guide = new MenuObject("음성 안내");
        guide.setResourceValue(R.drawable.guide_2);

        MenuObject draw_map = new MenuObject("그리기");
        draw_map.setResourceValue(R.drawable.draw_map);


        menuObjects.add(close);
        menuObjects.add(eraser);
        menuObjects.add(guide);
        menuObjects.add(draw_map);

        return menuObjects;
    }

    private final void showContextMenuDialogFragment() {
        if (this.getActivity().getSupportFragmentManager().findFragmentByTag("ContextMenuDialogFragment") == null) {
            ContextMenuDialogFragment var10000 = this.contextMenuDialogFragment;
            if (var10000 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("contextMenuDialogFragment");
            }

            var10000.show(this.getActivity().getSupportFragmentManager(), "ContextMenuDialogFragment");
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_main ,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.context_menu)
            showContextMenuDialogFragment();

        return super.onOptionsItemSelected(item);
    }
    public String get_follow_mode(){

        String mode = "";
        if(me_check) mode = "me";
        else if(many_check) mode = "many";
        else if(solo_check) mode = "solo";
        return mode;
    }
    public void set_follow_mode(String mode){

        if(mode.equals("me")){

            try {
                ufsOther.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            ufsOther = new UfsOther();

            tv_solo.setText("ㅁ Solo");
            tv_many.setText("ㅁ Many");
            tv_me.setText("☑ Me");
            solo_check = false;
            many_check = false;
            me_check = true;


        }
        else if(mode.equals("many")){

            ufsOthers.clear();

            tv_solo.setText("ㅁ Solo");
            tv_many.setText("☑ Many");
            tv_me.setText("ㅁ Me");
            solo_check = false;
            many_check = true;
            me_check = false;
        }
        else if(mode.equals("solo")){

            try {
                ufsOther.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            ufsOther = new UfsOther();

            tv_solo.setText("☑ Solo");
            tv_many.setText("ㅁ Many");
            tv_me.setText("ㅁ Me");
            solo_check = true;
            many_check = false;
            me_check = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy", "onDestroy");

        if(serviceIntent != null) {
            getActivity().stopService(serviceIntent);
            serviceIntent = null;
        }
    }
}
