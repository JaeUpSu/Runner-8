package com.example.Runner8.ui.setting;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.Runner8.MainActivity;
import com.example.Runner8.R;
import com.example.Runner8.SingleTon.Sub_bundle;

public class AlarmService extends Service {

    private String NOTIFICATION_DOWNLOAD_ID = "1";
    private String NOTIFICATION_COMPLETE_ID = "2";
    private String CHANNEL_ID = "AlarmTest";
    private String CHANNEL_NAME = "default";
    private String CHANNEL_DESCRIPTION = "This is default notification channel";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotification();
        Log.d("AlarmService", "Alarm");

        return START_NOT_STICKY;
    }

    private void createNotification(){

        int max_progress = (int) Math.round(Double.valueOf(Sub_bundle.getInstance().getApp_amount()));
        int progress = (int) Math.round(Double.valueOf(Sub_bundle.getInstance().getTotal_food_kcal()));

        PendingIntent mPendingIntent = PendingIntent.getActivity(
                getApplication(),
                0,
                new Intent(getApplicationContext(), MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager)
                getApplication().getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
        }

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(getApplication(),CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("KCAL : ")
                .setContentText(Sub_bundle.getInstance().getTotal_food_kcal())
                .setContentIntent(mPendingIntent)       // 앱 켜짐
                .setProgress(max_progress, progress, false)
                .setColor(Color.RED)
                .setTimeoutAfter(20*1000)
                // 사용자가 탭을 클릭하면 자동 제거
                .setAutoCancel(true)
                // 잠금화면 공개상태 설정 (전체 표시)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        Notification notificationAlarm = builder.build();

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        startForeground(1, notificationAlarm);

    }

}