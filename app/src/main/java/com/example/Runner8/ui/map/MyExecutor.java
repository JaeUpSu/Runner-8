package com.example.Runner8.ui.map;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyExecutor implements Executor {

    private  Executor executor;

    public MyExecutor(){
        Executors.newSingleThreadExecutor();
    }
    public MyExecutor(Executor executor){
        this.executor = executor;
    }
    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }

    private class MainThread implements Executor {

        private android.os.Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}
