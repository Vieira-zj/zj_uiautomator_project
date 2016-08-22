package com.example.zhengjin.funsettingsuitest.Service;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ServiceUiTestRunner extends IntentService {

    private static final String TAG = ServiceUiTestRunner.class.getSimpleName();

    public ServiceUiTestRunner() {
        super(TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String testPackage = intent.getExtras().getString("pkgName");
        String testRunner = intent.getExtras().getString("testRunner");
        Log.d(TAG, String.format("The intent arguments: %s/%s", testPackage, testRunner));

        // TODO: 2016/8/18 run instrument test
        Bundle arguments = new Bundle();
        arguments.putString("debug", "false");
        arguments.putString("class",
                "com.example.zhengjin.funsettingsuitest.testcases.TestPlayingFilm#testDemo");
        this.startInstrumentation(new ComponentName(testPackage, testRunner), null, arguments);
    }
}
