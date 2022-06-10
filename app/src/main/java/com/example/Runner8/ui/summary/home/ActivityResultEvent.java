package com.example.Runner8.ui.summary.home;

import android.content.Intent;

public class ActivityResultEvent {

    private int requestCode;
    private int resultCode;
    private Intent data;

    public static ActivityResultEvent create(int requestCode, int resultCode, Intent intent){
        return new ActivityResultEvent(requestCode,resultCode,intent);
    }

    public ActivityResultEvent(int requestCode, int resultCode, Intent intent) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = intent;
    }

    public int getRequestCode(){
        return requestCode;
    }
    public int getResultCode(){
        return resultCode;
    }

    public Intent getData(){
        return data;
    }

}
