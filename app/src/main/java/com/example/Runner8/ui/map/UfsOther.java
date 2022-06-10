package com.example.Runner8.ui.map;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.Runner8.ui.map.SingleTon.MapSingleTon;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class UfsOther {

    private ArrayList<Double> ufs_lat = new ArrayList<>();
    private ArrayList<Double> ufs_long  = new ArrayList<>();
    private ArrayList<Integer> ufs_time  = new ArrayList<>();
    private ArrayList<Double> ufs_distance = new ArrayList<>();

    private Marker ufs_marker = new Marker();
    private TimerTask ufs_task;

    private int timer_count = 0;
    private int index_count = 0;

    private String uid;
    private String nickName;

    private TextToSpeech textToSpeech;
    private int dis_count = 1;
    private int speech_check_count = 1;

    private boolean me_mode_check = false;

    public void addUfs_distance(double distance){
        this.ufs_distance.add(distance);
    }
    public void clearUfs_distance(){
        this.ufs_distance.clear();
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public ArrayList<Double> getUfs_lat() {
        return ufs_lat;
    }

    public ArrayList<Double> getUfs_long() {
        return ufs_long;
    }

    public ArrayList<Integer> getUfs_time() {
        return ufs_time;
    }

    public void addUfs_lat(Double lat){
        this.ufs_lat.add(lat);
    }
    public void addUfs_long(Double Long){
        this.ufs_long.add(Long);

    }public void addUfs_time(int time){
        this.ufs_time.add(time);
    }

    public Marker getUfs_marker() {
        return ufs_marker;
    }

    public void setUfs_marker(Marker ufs_marker) {
        this.ufs_marker = ufs_marker;
    }

    public TimerTask getUfs_task() {
        return ufs_task;
    }

    public void setUfs_task(TimerTask ufs_task) {
        this.ufs_task = ufs_task;
    }

    public void setIndex_count(int index_count) {
        this.index_count = index_count;
    }

    public int getIndex_count() {
        return index_count;
    }
    public void addIndex_count(){
        this.index_count += 1;
    }
    public void clearIndex_count(){
        this.index_count = 0;
    }

    public void setTimer_count(int timer_count) {
        this.timer_count = timer_count;
    }

    public int getTimer_count() {
        return timer_count;
    }

    public void addTimer_count(){
        this.timer_count += 1;
    }
    public void clearTimer_count(){
        this.timer_count = 0;
    }
    public void settingMarker(OverlayImage color){
        ufs_marker.setIcon(color);
        ufs_marker.setCaptionText(getNickName());
        ufs_marker.setCaptionAligns(Align.Top);
    }

    public void newUfs_marker(){
        this.ufs_marker = new Marker();
    }

    public void stopTimerTask(){
        if (this.ufs_task != null) {
            ufs_task.cancel();
            ufs_task = null;

            getUfs_marker().setMap(null);
        }
    }

    public void setMe_mode_check(boolean me_mode_check) {
        this.me_mode_check = me_mode_check;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void clearDis_count(){
        this.dis_count = 1;
    }

    public void startTimer(Activity activity, OverlayImage color, Timer timer, NaverMap naverMap, TextToSpeech textToSpeech){

        settingMarker(color);

        Log.i("uo", getUfs_lat().size() + "");
        clearTimer_count();
        clearIndex_count();
        clearDis_count();

        Courses.getInstance().setLanguageSpeech(Locale.KOREA);

        if(me_mode_check) Courses.getInstance().ClearMe_qua_index();

        this.ufs_task = new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(() -> {

                    Log.i("uo", "uid : " + getUid() + "count : " + getIndex_count() + "time : " + getUfs_time().get(getIndex_count()));

                    // 다음 좌표까지 걸린 시간 확인
                    addTimer_count();
                    if (getTimer_count() == getUfs_time().get(getIndex_count())) {

                        LatLng latLng = new LatLng(getUfs_lat().get(getIndex_count() + 1),
                                getUfs_long().get(getIndex_count() + 1));
                        Log.i("After Point", ufs_lat.get(getIndex_count() + 1) + "  " +
                                ufs_long.get(getIndex_count() + 1));

                        // 첫번째 움직였을때
                        if (getIndex_count() == 0) {

                            getUfs_marker().setPosition(latLng);
                            // ufs_marker.setPosition(tem_latLng);

                            // 첫번째 움직였을때 마커 그리기
                            getUfs_marker().setMap(naverMap);

                            // Log.i("uo", "uid : " + getUid() + "point(lat) : " + getUfs_marker().getPosition().latitude + "point(long) : " + getUfs_marker().getPosition().longitude);

                            // 첫번째 움직였는데 끝난 경우
                            if(getUfs_time().size() == 1){
                                stopTimerTask();
                                return;
                            }

                            addIndex_count();
                            clearTimer_count();
                        }
                        else {

                            if (getIndex_count() == getUfs_time().size() - 1) {
                                // 마커 null
                                // Draw
                                textToSpeech.speak(getNickName() + "님이 도착하셨습니다",
                                        TextToSpeech.QUEUE_ADD, null, null);

                                stopTimerTask();
                                return;
                            }

                            // 마크 지우기, 라인 그리기
                            getUfs_marker().setMap(null);

                            // Log.i("index_count ", getUfs_marker().getPosition().latitude + " " + getUfs_marker().getPosition().longitude);

                            // 새로 마크 표시
                            newUfs_marker();
                            settingMarker(color);
                            getUfs_marker().setPosition(latLng);
                            getUfs_marker().setMap(naverMap);

                            // Log.i("index_count", getUfs_marker().getPosition().latitude + " " + getUfs_marker().getPosition().longitude);

                            //
                            if(ufs_distance.get(getIndex_count()) > 500 * dis_count) {
                                Courses.getInstance().speeching(getNickName() + "님이" + 500 * dis_count + " 미터를 뛰었습니다!!");
                                dis_count++;
                            }

                            // 분기
                            if(MapSingleTon.getInstance().isQuarter_check()) {

                                if (me_mode_check) {
                                    if (Courses.getInstance().getRecord_quarter_locations().size() !=
                                            Courses.getInstance().getMe_qua_index()) {

                                        if (latLng.distanceTo(
                                                Courses.getInstance().getRecord_quarter_locations().get(
                                                        Courses.getCoursesInstance().getMe_qua_index())) < 30) {

                                            Courses.getInstance().speeching("상대가 " +
                                                    (Courses.getInstance().getMe_qua_index() + 1) + "구간을 지나고 있습니다.");
                                            // check
                                            Courses.getInstance().PlusMe_qua_index();
                                            // 나중에 코스를 등록할 시 check_count 랑 분기점들 갯수를 비교하여 확인할 것.
                                        }
                                    }
                                }
                            }

                            addIndex_count();
                            clearTimer_count();
                        }
                        // Log.i("uo", "uid : " + getUid() + "ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
                    }
                });
            }
        };

        timer.schedule(this.ufs_task, 0, 1000);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
