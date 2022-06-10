package com.example.Runner8.TRASH;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.Runner8.MainActivity;
import com.example.Runner8.R;
import com.example.Runner8.ui.map.Courses;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;
import java.util.Calendar;

public class MapAlarmService extends Service {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSSS";

    private Thread mainThread;
    public static Intent serviceIntent = null;
    private boolean service_finish;
    private ArrayList<Marker> markers;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        serviceIntent = intent;
        Log.i("MapAlarmService", "Start Service");

        stopService(serviceIntent);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("MapAlarm onDestroy", "check!!");

        if(Courses.getInstance().isStopService()) {
            Log.i("SERVICE FINISH", "check!!");
            serviceIntent = null;
            return;
        }

        setAlarmTimer();
        serviceIntent = null;

        Thread.currentThread().interrupt();
        if(mainThread != null){
            mainThread.interrupt();
            mainThread = null;
        }
    }

    protected void setAlarmTimer(){
        Log.i("setAlarmTimer", "setAlarmTimer");

        final Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, 1);
        Intent intent = new Intent(this, MapAlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }

    public void createNotificationChannel(){

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "test")
                        .setSmallIcon(R.mipmap.ic_launcher)//drawable.splash)
                        .setContentTitle("Service test")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("test","Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 , notificationBuilder.build());
    }
}
