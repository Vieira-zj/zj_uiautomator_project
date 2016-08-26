package com.example.zhengjin.funsettingsuitest.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.utils.ShellUtils;

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
        String testPkgName = intent.getExtras().getString("TestPkgName");
        String testRunner = intent.getExtras().getString("TestRunner");
        String testMethod = "testcasedemos.TestShellUtils#testExecShellShCommand";

        String commandInst = "am instrument -w -r";
        String commandExtraDebug = "-e debug false";
        String commandExtraClass = String.format(
                Locale.getDefault(), "-e class %s.%s", this.getPackageName(), testMethod);
        String commandRunner = String.format(
                Locale.getDefault(), "%s/%s", testPkgName, testRunner);
        String command = String.format(Locale.getDefault(), "%s %s %s %s",
                commandInst, commandExtraDebug, commandExtraClass, commandRunner);
        Log.d(TAG, String.format("The instrument command: %s", command));

        // need root to run instrument command
        // current process is killed after start instrument test, LOG:
        // I/ActivityManager(1651): Force stopping com.example.zhengjin.funsettingsuitest appid=1000 user=0: start instr
        // I/ActivityManager(1651): Killing 10979:com.example.zhengjin.funsettingsuitest/1000 (adj 0): stop com.example.zhengjin.funsettingsuitest
        ShellUtils.CommandResult cr = ShellUtils.execCommand(command, false, true);
        Log.d(TAG, String.format("The instrument test result code: %d", cr.mResult));
        Log.d(TAG, String.format("The instrument test success message: %s", cr.mSuccessMsg));
        Log.d(TAG, String.format("The instrument test error message: %s", cr.mErrorMsg));

        // error: NullPointerException
//        Bundle arguments = new Bundle();
//        arguments.putString("debug", "false");
//        arguments.putString("class",
//                "com.example.zhengjin.funsettingsuitest.testcases.TestPlayingFilm#testDemo");
//        this.startInstrumentation(new ComponentName(testPkgName, testRunner), null, arguments);
    }

}
