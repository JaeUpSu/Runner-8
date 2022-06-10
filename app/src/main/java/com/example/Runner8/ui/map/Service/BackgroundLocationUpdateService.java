package com.example.Runner8.ui.map.Service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.Runner8.MainActivity;
import com.example.Runner8.R;
import com.example.Runner8.ui.map.Calcurate.Calculator;
import com.example.Runner8.ui.map.Courses;
import com.example.Runner8.ui.map.SingleTon.MapSingleTon;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.naver.maps.geometry.LatLng;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BackgroundLocationUpdateService extends Service
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /* Declare in manifest
    <service android:name=".BackgroundLocationUpdateService"/>
    */

    private LocationManager locationManager;

    private final String TAG = "BackgroundLocationUpdateService";
    private final String TAG_LOCATION = "TAG_LOCATION";
    String CHANNEL_ID = "channel_location";
    String CHANNEL_NAME = "channel_location";
    private Context context;
    private boolean stopService = false;

    Notification notification;

    NotificationChannel channel;
    NotificationCompat.Builder builder = null;

    String speed = "";
    double TimeStamp = 0;
    double current_distance = 0;
    String currentSec = "";
    double current_kcal = 0;

    PendingIntent pendingIntent;

    Calculator calculator = new Calculator();

    /* For Google Fused API */
    protected GoogleApiClient mGoogleApiClient;
    protected LocationSettingsRequest mLocationSettingsRequest;
    private String latitude = "0.0", longitude = "0.0";
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    /* For Google Fused API */

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StartForeground();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    if (!stopService) {
                        //Perform your task here

                        // 알림 창이 하나만 뜨게 하는 것 찾아보기
                        notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setContentText("거리 : " + current_distance + "m \n"
                                        + "시간 : " + currentSec + "\n"
                                        + "칼로리 : " + current_kcal + "Kcal" + "TimeStamp : " + TimeStamp)
                                .setContentTitle("Runner 8")
                                .setChannelId(CHANNEL_ID)
                                .setSmallIcon(R.drawable.run1)
                                .setOngoing(true)
                                .setContentIntent(pendingIntent)
                                .build();


                        notification.flags = Notification.FLAG_NO_CLEAR;

                        startForeground(101, notification);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (!stopService) {
                        handler.postDelayed(this, TimeUnit.SECONDS.toMillis(1));
                    }
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        buildGoogleApiClient();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("onDestroy", "Service Stopped");
        stopService = true;
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Log.e(TAG_LOCATION, "Location Update Callback Removed");
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void StartForeground() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String CHANNEL_ID = "channel_location";
        String CHANNEL_NAME = "channel_location";

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
            builder.setChannelId(CHANNEL_ID);
            builder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        }

        builder.setContentTitle("Runner 8");
        builder.setSmallIcon(R.drawable.run1);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        notification = builder.build();
        startForeground(101, notification);
    }

    @Override
    public void onLocationChanged(Location location) {

        locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        Log.e(TAG_LOCATION, "" +
                "Location Changed Latitude : " + location.getLatitude() + "\tLongitude : " + location.getLongitude());

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                .PERMISSION_GRANTED) {
            return;
        }
        if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null){
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null){
                Log.i("GPS_PROVIDER", "GPS NETWORK");
            }
            else Log.i("GPS_PROVIDER", "GPS");
            Log.i("GPS_PROVIDER", loc.getLatitude() + "   " + loc.getLongitude());
        }
        else{
            Log.i("GPS_PROVIDER", "null");
            if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null){
                Log.i("GPS_PROVIDER", "NETWORK");

            }
            else Log.i("GPS_PROVIDER", "null");
        }

            if (location != null) {

                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());

                LatLng cur_latlng = new LatLng(location.getLatitude(), location.getLongitude());
                MapSingleTon.getInstance().setCurrent_loc(cur_latlng);

                if (Courses.getInstance().getLocations().size() == 0) {

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
                    Log.i("location", location.getLatitude() + "  " + location.getLongitude());

                    speed = String.valueOf(location.getSpeed());

                    // 방금 얻은 좌표 가져옴
                    LatLng c_l = new LatLng(location);
                    // 이전 좌표를 가져옴
                    LatLng p_l = Courses.getInstance().getPrev_loc();
                    // 현재 얻은 좌표를 넣어줌
                    Courses.getInstance().setCur_loc(c_l);

                    if (location.getAccuracy() < 40) {

                        if (p_l != null) {
                            // 거리 차이가 있어야 좌표를 얻음
                            if (c_l.distanceTo(p_l) > 0.0001) {

                                Courses.getInstance().setCur_time(System.currentTimeMillis());
                                Date date = new Date();

                                // LOG
                                //////////////////////////////////////////////////////////////////////////////////////////////////

                                //////////////////////////////////////////////////////////////////////////////////////////////////

                                // 실제 시간 계산용
                                Courses.getInstance().addTime(System.currentTimeMillis());

                                // Courses Locations
                                Courses.getInstance().addLocations(location);


                                // KalmanFilter
                                // TimeStamp, Speed, lat, long, accuracy


                                double cur_time = System.currentTimeMillis();
                                double prev_time = Courses.getInstance().getPrev_time();

                                // TimeStamp
                                TimeStamp = (cur_time - prev_time) / 1000;

                                Log.i("TimeStamp", TimeStamp + " ");

                                // Distance

                                double distance = Courses.getInstance().getPrev_loc().distanceTo(cur_latlng);

                                // SPEED
                                double speed = distance / TimeStamp;

                                Log.i("distance", distance + " ");
                                Log.i("getAccuracy", location.getAccuracy() + "");

                                // kalman
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    MapSingleTon.getInstance().processKalmanFilter(speed, location.getLatitude(),
                                            location.getLongitude(), System.currentTimeMillis(),
                                            location.getAccuracy(), location.getAltitude(), location.getVerticalAccuracyMeters());
                                }
                                LatLng kalman_latlng = new LatLng(MapSingleTon.getInstance().getKalmanFilter().getLatitude(),
                                        MapSingleTon.getInstance().getKalmanFilter().getLongitude());
                                Location kalman_location = new Location("kalman");
                                kalman_location.setLatitude(kalman_latlng.latitude);
                                kalman_location.setLongitude(kalman_latlng.longitude);

                                // Courses kalman
                                Courses.getInstance().addKalman_latLngs(kalman_latlng);

                                // 이전 좌표를 현재 좌표로
                                Courses.getInstance().setPrev_loc(c_l);

                                // 임시 시간 계산용
                                Courses.getInstance().setPrev_time(Courses.getInstance().getCur_time());

                                //
                                Courses.getInstance().addTotal_distance(distance);

                                //////////////////////////////////////////////////////////////////////////////////////////////////

                                // 초기값 설정

                                // 일정 간격 시 안내 (1km 당)

                                // 일정 간격 시 안내 (1km 당)
                                if (MapSingleTon.getInstance().isDistance_check()) {
                                    Courses.getInstance().setLanguageSpeech(Locale.KOREA);
                                    Courses.getInstance().intervalSpeechChecking(Courses.getInstance().getTotal_distance(),
                                            MapSingleTon.getInstance().getTOTAL_SEC());
                                }

                                // Quarter
                                // Courses.getInstance().intervalSpeechChecking(Courses.getInstance().getTotal_distance());

                                // QUARTER LOCATION
                                ///////////////////////////////////////////////////////////////////

                                // 지금까지 측정한 거리가 500m가 넘을 시 quarter point 를 저장 함.
                                if (Courses.getInstance().getQuarterly_distance() + distance > 500.0) {

                                    Courses.getInstance().addQuarter_location(location);
                                    Courses.getInstance().addQuarter_index(
                                            Courses.getInstance().getLocationsSize());

                                    // point 를 저장 했다면 quarterly_distance 를 다시 계산 함.
                                    Courses.getInstance().clearQuarterly_distance();
                                }
                                // 500m 안넘으면 그냥 계속 더함
                                else
                                    Courses.getInstance().addQuarterly_distance(distance);


                                // follow 일때 만 확인
                                if (Courses.getInstance().isItem_pick_check()) {

                                    if (MapSingleTon.getInstance().isQuarter_check()) {

                                        if (Courses.getInstance().getRecord_quarter_locations().size() !=
                                                Courses.getInstance().getUser_qua_index()) {

                                            if (cur_latlng.distanceTo(
                                                    Courses.getInstance().getRecord_quarter_locations().get(
                                                            Courses.getCoursesInstance().getUser_qua_index())) < 30) {

                                                Courses.getInstance().speeching(
                                                        Courses.getInstance().getUser_qua_index() + 1 + "구간을 지나고 있습니다.");
                                                // check
                                                Courses.getInstance().PlusQuarter_index();
                                                // 나중에 코스를 등록할 시 check_count 랑 분기점들 갯수를 비교하여 확인할 것.
                                            }
                                        }
                                    }

                                    if (MapSingleTon.getInstance().isArrive_check()) {
                                        LatLng finish_lng = new LatLng(Double.valueOf(MapSingleTon.getInstance().getFinish_lat()),
                                                Double.valueOf(MapSingleTon.getInstance().getFinish_long()));

                                        Courses.getInstance().guideChecking(cur_latlng, finish_lng);
                                    }
                                }

                                ///////////////////////////////////////////////////////////////////

                                // 거리 차이가 있다면 좌표 리스트에 넣어줌
                                Courses.getInstance().addLatLngs(c_l);

                                MapSingleTon.getInstance().addCoords(c_l);
                                MapSingleTon.getInstance().addKalman_coords(kalman_latlng);
                            }
                        }
                    }

                    current_distance = Math.round(Courses.getInstance().getTotal_distance() * 100) / 100;
                    int current_second = Courses.getInstance().getTotal_second();
                    String regular_time = calculator.time_result(current_distance);
                    current_kcal = Math.round(calculator.kcal_result(
                            Integer.valueOf(regular_time.split("[ ]")[0])) * 100) / 100;
                    currentSec = calculator.format_time_num(current_second);
                }
            }
            else {

                Log.i("SERVICE NULL", "check!!");
            }

        if (latitude.equalsIgnoreCase("0.0") && longitude.equalsIgnoreCase("0.0")) {
            requestLocationUpdate();
        } else {
            Log.e(TAG_LOCATION, "Latitude : " + location.getLatitude() + "\tLongitude : " + location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2 * 1000);
        mLocationRequest.setFastestInterval(1 * 1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(locationSettingsResponse -> {
                    Log.e(TAG_LOCATION, "GPS Success");
                    requestLocationUpdate();
                })
                .addOnFailureListener(e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                int REQUEST_CHECK_SETTINGS = 214;
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult((AppCompatActivity) context, REQUEST_CHECK_SETTINGS);

                            } catch (IntentSender.SendIntentException sie) {
                                Log.e(TAG_LOCATION, "Unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.e(TAG_LOCATION, "Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                    }
                })
                .addOnCanceledListener(() -> Log.e(TAG_LOCATION, "checkLocationSettings -> onCanceled"));
    }

    @Override
    public void onConnectionSuspended(int i) {
        connectGoogleClient();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mSettingsClient = LocationServices.getSettingsClient(context);

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        connectGoogleClient();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.e(TAG_LOCATION, "Location Received");
                mCurrentLocation = locationResult.getLastLocation();
                onLocationChanged(mCurrentLocation);
            }
        };
    }

    private void connectGoogleClient() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(context);
        if (resultCode == ConnectionResult.SUCCESS) {
            mGoogleApiClient.connect();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdate() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }
}