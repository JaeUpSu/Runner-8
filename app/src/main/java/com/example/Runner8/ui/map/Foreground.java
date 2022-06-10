package com.example.Runner8.ui.map;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class Foreground implements Application.ActivityLifecycleCallbacks {

    private static Foreground instance;

    public static void init(Application app) {
        if (instance == null) {
            instance = new Foreground();
            app.registerActivityLifecycleCallbacks(instance);
        }
    }

    public static Foreground get() {
        return instance;
    }

    private Foreground() {
    }

    private AppStatus mAppStatus;

    public AppStatus getAppStatus() {
        return mAppStatus;
    }


    // check if app is return foreground
    public boolean isBackground() {
        return mAppStatus.ordinal() == AppStatus.BACKGROUND.ordinal();
    }


    public enum AppStatus {
        BACKGROUND, RETURNED_TO_FOREGROUND, FOREGROUND;
    }


    private int running = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (++running == 1) {
            mAppStatus = AppStatus.RETURNED_TO_FOREGROUND;
        } else if (running > 1) {
            mAppStatus = AppStatus.FOREGROUND;
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (--running == 0) {
            mAppStatus = AppStatus.BACKGROUND;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }


}
