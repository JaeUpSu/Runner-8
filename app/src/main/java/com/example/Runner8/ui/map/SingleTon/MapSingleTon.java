package com.example.Runner8.ui.map.SingleTon;

import com.example.Runner8.ui.map.Kalman.KalmanFilter;
import com.example.Runner8.ui.map.QuarterOfCourses;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;

public class MapSingleTon {

    LatLng current_loc;
    ArrayList<LatLng> coords = new ArrayList<>();
    ArrayList<LatLng> kalman_coords = new ArrayList<>();

    ArrayList<String> sample_altitude = new ArrayList<>();
    ArrayList<String> sample_kalman_altitude = new ArrayList<>();

    int totally_final_index;
    int totally_total_count;
    int my_final_index;
    int my_total_count;
    int course_total_count;

    int TOTAL_COUNT;
    double TOTAL_DISTANCE;
    double TOTAL_KCAL;
    double TOTAL_AVG_SPEED;
    int TOTAL_SEC;

    boolean start_check;
    boolean quarter_check;
    boolean distance_check;
    boolean arrive_check;
    boolean all_check;

    boolean running_check = false;
    boolean foreground_check = false;


    double finish_lat;
    double finish_long;

    private FusedLocationProviderClient fusedLocationClient;

    KalmanFilter kalmanFilter = new KalmanFilter();

    ArrayList<String> documentId = new ArrayList<>();
    ArrayList<QuarterOfCourses> quarterOfCourses = new ArrayList<>();


    private static final MapSingleTon TOTAL_MAP_INSTANCE = new MapSingleTon();

    public static synchronized MapSingleTon getInstance() {return TOTAL_MAP_INSTANCE;}

    public void clearCoords(){
        this.coords.clear();
    }
    public void clearKalman_coords(){
        this.kalman_coords.clear();
    }
    public void addCoords(LatLng latLng){
        this.coords.add(latLng);
    }
    public void addKalman_coords(LatLng latLng){
        this.kalman_coords.add(latLng);
    }

    public ArrayList<LatLng> getCoords() {
        return coords;
    }

    public ArrayList<LatLng> getKalman_coords() {
        return kalman_coords;
    }

    public void setFusedLocationClient(FusedLocationProviderClient fusedLocationClient) {
        this.fusedLocationClient = fusedLocationClient;
    }

    public FusedLocationProviderClient getFusedLocationClient() {
        return fusedLocationClient;
    }

    public int getCourse_total_count() {
        return course_total_count;
    }

    public void setCourse_total_count(int course_total_count) {
        this.course_total_count = course_total_count;
    }

    public void setMy_final_index(int my_final_index) {
        this.my_final_index = my_final_index;
    }

    public void setTotally_final_index(int totally_final_index) {
        this.totally_final_index = totally_final_index;
    }

    public void setMy_total_count(int my_total_count) {
        this.my_total_count = my_total_count;
    }

    public void setTotally_total_count(int totally_total_count) {
        this.totally_total_count = totally_total_count;
    }

    public int getMy_final_index() {
        return my_final_index;
    }

    public int getMy_total_count() {
        return my_total_count;
    }

    public int getTotally_final_index() {
        return totally_final_index;
    }

    public int getTotally_total_count() {
        return totally_total_count;
    }

    public static MapSingleTon getTotalMapInstance() {
        return TOTAL_MAP_INSTANCE;
    }

    public void MinusMy_total_count(){
        this.my_total_count -= 1;
    }
    public void MinusTotally_total_count(){
        this.totally_total_count -= 1;
    }
    public void PlusMy_final_index(){
        this.my_final_index += 1;
    }
    public void PlusMy_total_count(){
        this.my_total_count += 1;
    }
    public void PlusTotally_final_index(){
        this.totally_final_index += 1;
    }
    public void PlusTotally_total_count(){
        this.totally_total_count += 1;
    }

    public void setArrive_check(boolean arrive_check) {
        this.arrive_check = arrive_check;
    }

    public void setDistance_check(boolean distance_check) {
        this.distance_check = distance_check;
    }

    public void setQuarter_check(boolean quarter_check) {
        this.quarter_check = quarter_check;
    }

    public void setStart_check(boolean start_check) {
        this.start_check = start_check;
    }

    public boolean isArrive_check() {
        return arrive_check;
    }

    public boolean isDistance_check() {
        return distance_check;
    }

    public boolean isQuarter_check() {
        return quarter_check;
    }

    public boolean isStart_check() {
        return start_check;
    }

    public void setAll_check(boolean all_check) {
        this.all_check = all_check;
    }

    public boolean isAll_check() {
        return all_check;
    }

    public void setFinish_long(double finish_long) {
        this.finish_long = finish_long;
    }

    public void setFinish_lat(double finish_lat) {
        this.finish_lat = finish_lat;
    }

    public double getFinish_lat() {
        return finish_lat;
    }

    public double getFinish_long() {
        return finish_long;
    }

    public void setKalmanFilter(double Lat, double Long, long time,
                                double accuracy, double altitude, double verticalAccuracy){

        kalmanFilter.setState(Lat, Long, time, accuracy, altitude, verticalAccuracy);

    }
    public void processKalmanFilter(double speed, double Lat, double Long, long time,
                                    double accuracy, double altitude, double verticalAccuracy){
        kalmanFilter.process(speed, Lat, Long, time, accuracy, altitude, verticalAccuracy);
    }

    public KalmanFilter getKalmanFilter() {
        return kalmanFilter;
    }

    public ArrayList<QuarterOfCourses> getQuarterOfCourses() {
        return quarterOfCourses;
    }
    public int getQuarterOfCourseSize(){
        return this.quarterOfCourses.size();
    }

    public void getQuarterOfCourses_index(String id){

    }

    public void setQuarterOfCourses(ArrayList<QuarterOfCourses> quarterOfCourses) {
        this.quarterOfCourses = quarterOfCourses;
    }

    public void addQuarterOfCourses(QuarterOfCourses quarterOfCourses){

        this.quarterOfCourses.add(quarterOfCourses);
        this.documentId.add(quarterOfCourses.getId());

    }
    public void deleteQuarterOfCourses(String id){
        if(documentId.size() != 0) {
            if(documentId.indexOf(id) != -1) {

                int index = documentId.indexOf(id);
                this.documentId.remove(index);
                this.quarterOfCourses.remove(index);
            }
        }
    }

    public QuarterOfCourses getQuarterOfCourse(String id){

        return this.quarterOfCourses.get(documentId.indexOf(id));
    }


    public ArrayList<String> getDocumentId() {
        return documentId;
    }

    public void addGetDocumentId(String id){
        this.documentId.add(id);
    }

    public double getTOTAL_DISTANCE() {
        return TOTAL_DISTANCE;
    }

    public double getTOTAL_KCAL() {
        return TOTAL_KCAL;
    }

    public int getTOTAL_COUNT() {
        return TOTAL_COUNT;
    }

    public void setTOTAL_COUNT(int TOTAL_COUNT) {
        this.TOTAL_COUNT = TOTAL_COUNT;
    }

    public void setTOTAL_DISTANCE(double TOTAL_DISTANCE) {
        this.TOTAL_DISTANCE = TOTAL_DISTANCE;
    }

    public void setTOTAL_KCAL(double TOTAL_KCAL) {
        this.TOTAL_KCAL = TOTAL_KCAL;
    }

    public double getTOTAL_AVG_SPEED() {
        return TOTAL_AVG_SPEED;
    }

    public void setTOTAL_AVG_SPEED(double TOTAL_AVG_SPEED) {
        this.TOTAL_AVG_SPEED = TOTAL_AVG_SPEED;
    }

    public boolean isRunning_check() {
        return running_check;
    }

    public void setRunning_check(boolean running_check) {
        this.running_check = running_check;
    }

    public boolean isForeground_check() {
        return foreground_check;
    }

    public void setForeground_check(boolean foreground_check) {
        this.foreground_check = foreground_check;
    }

    public void setCurrent_loc(LatLng current_loc) {
        this.current_loc = current_loc;
    }

    public LatLng getCurrent_loc() {
        return current_loc;
    }

    public ArrayList<String> getSample_altitude() {
        return sample_altitude;
    }

    public ArrayList<String> getSample_kalman_altitude() {
        return sample_kalman_altitude;
    }
    public void addSample_altitude(String altitude){
        this.sample_altitude.add(altitude);
    }
    public void addSample_kalman_altitude(String altitude){
        this.sample_kalman_altitude.add(altitude);
    }

    public void setTOTAL_SEC(int TOTAL_SEC) {
        this.TOTAL_SEC = TOTAL_SEC;
    }

    public int getTOTAL_SEC() {
        return TOTAL_SEC;
    }
    public void clearTOTAL_SEC(){
        this.TOTAL_SEC = 0;
    }
    public void addTOTAL_SEC(){
        this.TOTAL_SEC += 1;
    }
}
