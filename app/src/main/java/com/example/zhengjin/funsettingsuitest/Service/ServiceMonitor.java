package com.example.zhengjin.funsettingsuitest.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.utils.DeviceUtils;

import java.util.Locale;

public class ServiceMonitor extends Service {

    private final static String TAG = ServiceMonitor.class.getSimpleName();

    private final int wait = 1000;
    private Handler handler = new Handler();

    @Override
    public void onCreate() {
        Log.d(TAG, "monitor service onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "monitor service onStart");
        handler.postDelayed(monitorTask, this.wait);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "monitor service onDestroy");
        handler.removeCallbacks(monitorTask);
        super.onDestroy();
        this.stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable monitorTask = new Runnable() {
        @Override
        public void run() {
            // TODO: 2016/10/12 post data on UI instead of log.
            Log.d(TAG, String.format(Locale.getDefault(),
                    "fps: %.1f", DeviceUtils.getFramesPerSeconds()));
            handler.postDelayed(this, ServiceMonitor.this.wait);
        }
    };
}
