package com.example.Runner8.ui.setting;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.Runner8.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SettingPreferenceFragment extends PreferenceFragmentCompat {

    private Calendar calendar;
    private SharedPreferences prefs;

    private String CHANNEL_ID = "AlarmTest";

    private  PendingIntent pendingIntent;
    private  AlarmManager alarmManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.root_preferences);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(prefListener);

    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            if(key.equals("signature")){
                Toast.makeText(getActivity(), "signature", Toast.LENGTH_SHORT).show();
            }

            if(key.equals("walked")){

                if(prefs.getBoolean("walked", false)){
                    // 알림 설정
                    setAlarm();
                    Toast.makeText(getActivity(), "On", Toast.LENGTH_SHORT).show();
                }
                else{
                    // 알림 끄기
                    CancelAlarm();
                    Toast.makeText(getActivity(), "Off", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    /* 알람 등록 */
    private void setAlarm() {

        // 알람 시간 설정
        this.calendar = Calendar.getInstance();
        this.calendar.setTimeInMillis(System.currentTimeMillis());

        /*  시간 직접 입력
        this.calendar.setTimeInMillis(System.currentTimeMillis());
        this.calendar.set(Calendar.HOUR_OF_DAY, 22);
        this.calendar.set(Calendar.MINUTE, 53);
        this.calendar.set(Calendar.SECOND, 0);
         */

        // Receiver 설정
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra("INPUT TEXT", this.calendar.getTimeInMillis());
        pendingIntent = PendingIntent.getBroadcast(getActivity(),
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.i("timePicker","Minute : " +  this.calendar.getTimeInMillis());
        intent.putExtra("state", "alarm on");

        // 알람 설정
        alarmManager = (AlarmManager)
                getActivity().getSystemService(Context.ALARM_SERVICE);

        // 정해진 시간에 알림 설정
        // alarmManager.set(AlarmManager.RTC_WAKEUP, this.calendar.getTimeInMillis(), pendingIntent);

        // 현재 시간
        long firstMills = System.currentTimeMillis();

        // 현재 시간으로부터 같은 시간 간격으로 알림 설정
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 10*1000,10*1000 ,pendingIntent);

        // Toast 보여주기 (알람 시간 표시)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Toast.makeText(getActivity(), "Alarm : " + SystemClock.elapsedRealtime(), Toast.LENGTH_LONG).show();
        Log.i("Alarm","Alarm" + format.format(calendar.getTime()));

    }
    private void CancelAlarm(){
        if (pendingIntent != null && alarmManager != null) {
            Log.i("AlarmService", "Alarm Off");
            alarmManager.cancel(pendingIntent);
        }
    }

}
