package com.example.zhengjin.funsettingsuitest.Service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.Utils.ShellUtils;

import java.util.Locale;

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

        String commandInst = "am instrument -w -r";
        String commandExtraDebug = "-e debug false";
        String commandExtraClass =
                "-e class com.example.zhengjin.funsettingsuitest.testcases.TestPlayingFilm#testDemo";
        String commandRunner = String.format(Locale.getDefault(), "%s/%s", testPackage, testRunner);
        String command = String.format(Locale.getDefault(), "%s %s %s %s",
                commandInst, commandExtraDebug, commandExtraClass, commandRunner);
        Log.d(TAG, String.format("The instrument command: %s", command));

        // TODO: 2016/8/23 need root to run instrument command
        ShellUtils.CommandResult cr = ShellUtils.execCommand(command, true, true);
        Log.d(TAG, String.format("The instrument test result code: %d", cr.mResult));
        Log.d(TAG, String.format("The instrument test success message: %s", cr.mSuccessMsg));
        Log.d(TAG, String.format("The instrument test error message: %s", cr.mErrorMsg));

//        Bundle arguments = new Bundle();
//        arguments.putString("debug", "false");
//        arguments.putString("class",
//                "com.example.zhengjin.funsettingsuitest.testcases.TestPlayingFilm#testDemo");
//        this.startInstrumentation(new ComponentName(testPackage, testRunner), null, arguments);
    }
}
