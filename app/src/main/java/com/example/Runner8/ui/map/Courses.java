package com.example.Runner8.ui.map;

import android.app.Activity;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.Runner8.R;
import com.example.Runner8.ui.map.Calcurate.Calculator;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class Courses implements Serializable {

    private ArrayList<Location> locations = new ArrayList<>();
    private ArrayList<Long> times = new ArrayList<>();
    private ArrayList<Boolean> stay_checks = new ArrayList<>();
    private ArrayList<com.naver.maps.geometry.LatLng> latLngs = new ArrayList<>();
    private ArrayList<LatLng> kalman_latLngs = new ArrayList<>();

    private NaverMap naverMap;

    private LatLng first_loc;
    private LatLng prev_loc;
    private LatLng cur_loc;
    private LatLng app_final_loc;

    private long start_time = 0;
    private long prev_time = 0;
    private long cur_time = 0;
    private long speed = 0;
    private long prev_main_time = 0;
    private long prev_background_time = 0;

    private double total_distance = 0;
    private double total_kcal = 0;
    private int total_second = 0;
    private String pick_course_index;

    private double course_distance = 0;

    // follow variable
    private boolean item_pick_check = false;

    // quarter variable
    private int user_qua_index = 0;
    private int solo_qua_index = 0;
    private int many_qua_index = 0;
    private int me_qua_index = 0;
    private double quarterly_distance = 0;

    // guide
    TextToSpeech textToSpeech;
    int finish_speech_count = 0;
    int interval_speech_count = 1;

    ///
    Calculator calculator = new Calculator();

    //
    MediaPlayer mediaPlayer;

    //
    Activity activity;


    private ArrayList<Location> register_quarter_locations = new ArrayList<>();
    private ArrayList<LatLng> record_quarter_locations = new ArrayList<>();
    private ArrayList<Integer> quarter_index = new ArrayList<>();

    private boolean stopService = true;

    private static final Courses COURSES_INSTANCE = new Courses();

    public double getCourse_distance() {
        return course_distance;
    }

    public void setCourse_distance(double course_distance) {
        this.course_distance = course_distance;
    }

    public static synchronized Courses getInstance(){
        return COURSES_INSTANCE;
    }

    public void clearTotal_second(){
        this.total_second = 0;
    }
    public int getTotal_second() {
        return total_second;
    }

    public void addTotal_second(){
        this.total_second += 1;
    }
    public void setTotal_kcal(double total_kcal) {
        this.total_kcal = total_kcal;
    }

    public double getTotal_kcal() {
        return total_kcal;
    }
    public void addTotal_kcal(double total_kcal){
        this.total_kcal += total_kcal;
    }
    public void clearTotal_kcal(){
        this.total_kcal = 0;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public int getSolo_qua_index() {
        return solo_qua_index;
    }

    public int getMany_qua_index() {
        return many_qua_index;
    }
    public void PlusSolo_qua_index(){
        this.solo_qua_index++;
    }
    public void PlusMany_qua_index(){
        this.many_qua_index++;
    }
    public void ClearSolo_qua_index(){
        this.solo_qua_index = 0;
    }
    public void ClearMany_qua_index(){
        this.many_qua_index = 0;
    }
    public int getMe_qua_index() {
        return me_qua_index;
    }
    public void PlusMe_qua_index(){
        this.me_qua_index++;
    }
    public void ClearMe_qua_index(){
        this.me_qua_index = 0;
    }

    public void clearKalman_latLngs(){
        this.kalman_latLngs.clear();
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setNaverMap(NaverMap naverMap) {
        this.naverMap = naverMap;
    }

    public NaverMap getNaverMap() {
        return naverMap;
    }

    public void addKalman_latLngs(LatLng kalman_latLngs) {
        this.kalman_latLngs.add(kalman_latLngs);
    }

    public void setPrev_main_time(long prev_main_time) {
        this.prev_main_time = prev_main_time;
    }

    public void setPrev_background_time(long prev_background_time) {
        this.prev_background_time = prev_background_time;
    }

    public long getPrev_background_time() {
        return prev_background_time;
    }

    public long getPrev_main_time() {
        return prev_main_time;
    }

    public ArrayList<LatLng> getKalman_latLngs() {
        return kalman_latLngs;
    }

    public int getKalman_latLngsSize(){
        return this.kalman_latLngs.size();
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLatLngs(ArrayList<LatLng> latLngs) {
        this.latLngs = latLngs;
    }

    public ArrayList<LatLng> getLatLngs() {
        return latLngs;
    }

    public void addLatLngs(com.naver.maps.geometry.LatLng latLng){
        this.latLngs.add(latLng);
    }

    public void removeLatLng(int position){
        this.latLngs.remove(position);
    }

    public void clearLatLng(){
        this.latLngs.clear();
    }

    public void setCur_loc(LatLng cur_loc) {
        this.cur_loc = cur_loc;
    }

    public void setPrev_loc(LatLng prev_loc) {
        this.prev_loc = prev_loc;
    }

    public static Courses getCoursesInstance() {
        return COURSES_INSTANCE;
    }

    public LatLng getCur_loc() {
        return cur_loc;
    }

    public LatLng getPrev_loc() {
        return prev_loc;
    }

    public void setStopService(boolean stopService) {
        this.stopService = stopService;
    }

    public boolean isStopService() {
        return stopService;
    }

    public void setFirst_loc(LatLng first_loc) {
        this.first_loc = first_loc;
    }

    public LatLng getFirst_loc() {
        return first_loc;
    }

    public void setApp_final_loc(LatLng app_final_loc) {
        this.app_final_loc = app_final_loc;
    }

    public LatLng getApp_final_loc() {
        return app_final_loc;
    }

    public void setCur_time(long cur_time) {
        this.cur_time = cur_time;
    }

    public void setPrev_time(long prev_time) {
        this.prev_time = prev_time;
    }

    public long getCur_time() {
        return cur_time;
    }

    public long getPrev_time() {
        return prev_time;
    }

    public void addTime(long time){
        this.times.add(time);
    }
    public void addStay_check(boolean flag){
        this.stay_checks.add(flag);
    }

    public ArrayList<Boolean> getStay_checks() {
        return stay_checks;
    }

    public ArrayList<Long> getTimes() {
        return times;
    }
    public void addLocations(Location location){
        this.locations.add(location);
    }

    public int getLocationsSize(){
        return this.locations.size();
    }

    public void clearLocations(){
        this.locations.clear();
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public double getTotal_distance() {
        return total_distance;
    }

    public void addTotal_distance(double distance){
        this.total_distance += distance;
    }

    public void initialTotal_distance(){
        this.total_distance = 0.0;
    }

    public void setPick_course_index(String pick_course_index) {
        this.pick_course_index = pick_course_index;
    }

    public String getPick_course_index() {
        return pick_course_index;
    }

    public void addQuarterly_distance(double distance){
        this.quarterly_distance += distance;
    }
    public void clearQuarterly_distance(){
        this.quarterly_distance = 0;
    }

    public ArrayList<Location> getRegister_quarter_locations() {
        return register_quarter_locations;
    }

    public void addQuarter_location(Location location){
        this.register_quarter_locations.add(location);
    }

    public double getQuarterly_distance() {
        return quarterly_distance;
    }

    public void addQuarter_index(int index){
        this.quarter_index.add(index);
    }

    public ArrayList<Integer> getQuarter_index() {
        return quarter_index;
    }

    public void clearQuarter_locations(){
        this.register_quarter_locations.clear();
    }
    public void clearQuarter_indexes(){
        this.quarter_index.clear();
    }
    public void PlusQuarter_index(){
        this.user_qua_index += 1;
    }
    public void clearQuarter_index(){
        this.user_qua_index = 0;
    }

    public void setItem_pick_check(boolean item_pick_check) {
        this.item_pick_check = item_pick_check;
    }

    public boolean isItem_pick_check() {
        return item_pick_check;
    }
    public void addRecord_quarter_locations(LatLng latLng){
        this.record_quarter_locations.add(latLng);
    }

    public ArrayList<LatLng> getRecord_quarter_locations() {
        return record_quarter_locations;
    }
    public void clearRecord_quarter_locations(){
        this.record_quarter_locations.clear();
    }

    public int getUser_qua_index() {
        return user_qua_index;
    }

    public void setTextToSpeech(TextToSpeech textToSpeech) {
        this.textToSpeech = textToSpeech;
    }

    public void setLanguageSpeech(Locale locale){
        this.textToSpeech.setLanguage(locale);
    }


    public void guideChecking(LatLng cur_loc, LatLng finish_loc){
        setLanguageSpeech(Locale.KOREA);
        // 도착지점으로부터 얼만큼 남았는지 확인

        double course_distance = getCourse_distance();
        int marginOfError = (int) (Math.round(course_distance / 1000.0) * 25);
        Log.i("marginOfError", marginOfError + "");

        if(total_distance > 200) {
            if (total_distance > course_distance - marginOfError - 200) {

                if (cur_loc.distanceTo(finish_loc) <= 100) {
                    String guide_text = "";
                    if (cur_loc.distanceTo(finish_loc) <= 10 && finish_speech_count == 3) {
                        // 빵빠래
                        mediaPlayer = MediaPlayer.create(activity, R.raw.finish);
                        mediaPlayer.start();

                        guide_text = "목적지에 도착했습니다.";
                        finish_speech_count++;
                    } else if (cur_loc.distanceTo(finish_loc) <= 20 && finish_speech_count == 2) {
                        guide_text = "20미터";
                        finish_speech_count++;
                    } else if (cur_loc.distanceTo(finish_loc) <= 50 && finish_speech_count == 1) {
                        guide_text = "50미터";
                        finish_speech_count++;
                    } else if (finish_speech_count == 0) {
                        guide_text = "도착지점으로부터 약 100m 남았습니다 . 휴대폰을 키고 finish 버튼 누를 준비를 해주세요";
                        finish_speech_count++;
                    }
                    speeching(guide_text);
                }
            }
        }
    }
    public void intervalSpeechChecking(double distance, int time){

        if(distance > 1000 * interval_speech_count){

            // 시간 포맷 필요
            String regular_time = calculator.time_result(distance);
            String current_time = calculator.format_time(time);

            // 초 단위로 다시 가져옴
            int secondOfTime = Integer.valueOf(regular_time.split("[ ]")[0]) * 60;

            // x시간 x분 x초로 포맷 해옴
            String kcal_time = calculator.format_time(secondOfTime);

            String kcal = String.valueOf(
                    calculator.kcal_result(Integer.valueOf(regular_time.split("[ ]")[0])));

            speeching("현재까지 " + interval_speech_count + "킬로미터");
            speechingTerm(1000);
            speeching(current_time);
            speechingTerm(1000);
            speeching(kcal + " 칼로리");

            interval_speech_count++;
        }
    }
    public void speechingTerm(long time){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.playSilentUtterance(time, textToSpeech.QUEUE_ADD, null);
        }
    }

    public void speeching(String msg){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(msg,
                    TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    public void settingSpeech(float pitch, float speechRate){
        textToSpeech.setPitch(pitch);
        textToSpeech.setSpeechRate(speechRate);
    }

    public void quarter_guide(String msg, LatLng cur_loc){
        if(Courses.getInstance().getRecord_quarter_locations().size() !=
                Courses.getInstance().getUser_qua_index()) {

            if (cur_loc.distanceTo(
                    Courses.getInstance().getRecord_quarter_locations().get(
                            Courses.getCoursesInstance().getUser_qua_index())) < 250) {

                Courses.getInstance().speeching(msg);
                // check
                Courses.getInstance().PlusQuarter_index();
                // 나중에 코스를 등록할 시 check_count 랑 분기점들 갯수를 비교하여 확인할 것.
            }
        }
    }
    public void clearSpeech_count(){
        this.finish_speech_count = 0;
    }
    public void clearIntervalSpeech_count(){
        this.interval_speech_count = 1;
    }
}
