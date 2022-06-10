package com.example.Runner8.TRASH;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.Runner8.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.util.FusedLocationSource;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MapWorker extends Worker implements LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSSS";

    private Location current_loc;
    private Location prev_loc;
    private LocationManager locationManager;
    private NotificationManager notificationManager;
    private LocationRequest locationRequest;
    private LocationListener locationListener;

    private FusedLocationSource locationSource;
    private FusedLocationProviderClient fusedLocationClient;
    private FusedLocationProviderClient fusedLocationClient1;


    private NaverMap naverMap;

    private double[] Lats = new double[100];
    private double[] Longs = new double[100];
    private int final_point = 0;
    int i = 0;

    Data data;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    CollectionReference cr_foreground = db.collection("Users").document(user.getUid())
            .collection("Map").document("Foreground")
            .collection("Point");

    public MapWorker(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {

        Log.i("doWork", "ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");

        notificationManager = (NotificationManager) getApplicationContext().getSystemService((Context.NOTIFICATION_SERVICE));
        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "position")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("RUN")
                .build();

        notificationManager.notify(10, notification);

        ForegroundInfo info = new ForegroundInfo(10, notification);
        setForegroundAsync(info);

        Data outputData = null;

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        // Location
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

/*
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 3, 0, this);


 */
        try {

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.i("PERMISSION", "check!!");
            }

            // 가장 최근 위치 가져오기
            if (locationManager != null) {
                Log.i("locationManager != null", "check!!");
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.i("GPS_PROVIDER", "check!!");
                    current_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Log.i("NETWORK_PROVIDER", "check!!");
                    current_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }

            // 얼마동안 실행시킬지 조건을 달아야 함 무한루프는 안됨


            if (current_loc.getLatitude() > 0) {
                // 가장 최근 위치 가져오기

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    current_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    current_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                locationRequest = new LocationRequest();

                i = 0;
                Timer timer = new Timer();

                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                 ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                Log.i("NOTPERMISSION", "check!!");
                                return;
                            }

                            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

                            locationManager.getCurrentLocation(LocationManager.NETWORK_PROVIDER, null, getApplicationContext().getMainExecutor(),
                                    new Consumer<Location>() {
                                        @Override
                                        public void accept(Location location) {


                                            if(location != null) {
                                                if (i == 20) timer.cancel();

                                                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                                                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));


                                                Log.i("Worker LastLocation", sdf.format(new Date(location.getTime())));
                                                Log.i("Worker getCurrentLastLocation", location.getLatitude() + " " + location.getLongitude());

                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                    Log.i("Worker 몇 초 전 LastLocation", String.valueOf(TimeUnit.NANOSECONDS.toMillis(SystemClock.elapsedRealtimeNanos()
                                                            - location.getElapsedRealtimeNanos())));
                                                }
                                                Log.i("WorkerLastLocation", "ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
                                                i++;
                                            }
                                            else{
                                                Log.i("Worker locationNULL", "check!!");
                                            }
                                        }
                                    });
                        }
                    }
                };
                timer.schedule(tt, 0, 5000);
            }


        }catch (Exception e){
            Log.i("Error", e.toString());
            return Result.failure();
        }

        return Result.success();
    }


    private void createNotificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "position";
            NotificationChannel channel = new NotificationChannel("position", name,
                    NotificationManager.IMPORTANCE_MIN);
            channel.setDescription("current position");

            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(){
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "position")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("RUN")
                .build();

        notificationManager.notify(10, notification);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.i("location", "check!!");
    }
}
