package com.example.Runner8.ui.map.Object;

import android.location.Location;

public class Points {
    long time;
    Location location;

    public Location getLocation() {
        return location;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
