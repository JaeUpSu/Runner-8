package com.example.Runner8.ui.map.ForegroundService;

import android.content.Context;
import android.location.Location;

import androidx.preference.PreferenceManager;

public class Common {


    private static final String KEY_REQUESTING_LOCATION_UPDATES = "LocationUpdateEnable";

    public static  String getLocationText(Location mLocation){
        return mLocation == null? "Unknown Location" : new StringBuilder()
                .append(mLocation.getLatitude())
                .append("/")
                .append(mLocation.getLongitude())
                .toString();
    }

    public static void setRequestingLocationUpdates(Context context, boolean value) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, value)
                .apply();
                
    }

    public static boolean setRequestingLocationUpdates(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }
}
