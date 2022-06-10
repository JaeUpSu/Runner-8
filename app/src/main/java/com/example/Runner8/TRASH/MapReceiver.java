package com.example.Runner8.TRASH;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class MapReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent sIntent = new Intent(context, MapService.class);                                       // check

        Log.i("AlarmService", "AlarmService");

        // Oreo(26) 버전 이후부터는 Background 에서 실행을 금지하기 때문에 Foreground 에서 실행해야 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {                                           // check
            context.startForegroundService(sIntent);
        } else {
            context.startService(sIntent);
        }
    }
}
