package com.example.zhengjin.funsettingsuitest.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.utils.FileUtils;
import com.example.zhengjin.funsettingsuitest.utils.HelperUtils;
import com.example.zhengjin.funsettingsuitest.utils.ShellCmdUtils;
import com.example.zhengjin.funsettingsuitest.utils.StringUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Locale;

import static com.example.zhengjin.funsettingsuitest.TestApplication.EXTRA_KEY_EXEC_TIME;
import static com.example.zhengjin.funsettingsuitest.TestApplication.EXTRA_KEY_INST_METHOD;
import static com.example.zhengjin.funsettingsuitest.TestApplication.EXTRA_KEY_TEST_PACKAGE;
import static com.example.zhengjin.funsettingsuitest.TestApplication.EXTRA_KEY_TEST_RUNNER;

public class ServiceUiTestRunner extends IntentService {

    private static final String TAG = ServiceUiTestRunner.class.getSimpleName();

    private final Locale mLocale = Locale.getDefault();
    private final String mInstTestLogPath = this.buildAbsFilePath("inst_test_log_%s.log");
    private final String mLogcatLogPath = this.buildAbsFilePath("inst_logcat_log_%s.log");

    private Process mLogcatProcess = null;
    private DataOutputStream mOutputStream = null;

    private int mTotalRunTimes = 0;
    private int mTotalFailed = 0;

    public ServiceUiTestRunner() {
        super(TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ret = super.onStartCommand(intent, flags, startId);
        this.logInstTestHeader();
        return ret;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int execTime = Integer.parseInt(intent.getExtras().getString(EXTRA_KEY_EXEC_TIME));  // minutes
        long execTimeLong = execTime * 60L * 1000L;
        String command = buildInstCommand(intent);

        long startTime = SystemClock.uptimeMillis();
        long during;
        this.runLogcatProcess();
        do {
            runInstCommandByExecTime(command);
            SystemClock.sleep(3000);
            during = SystemClock.uptimeMillis() - startTime;
            Log.d(TAG, String.format("Run instrument test %d times.", ++mTotalRunTimes));
            Log.d(TAG, String.format(mLocale, "Run process percent: %.2f",
                    getRunPercent(during, execTimeLong)) + "%");
        }
        while (during <= execTimeLong);
    }

    @Override
    public void onLowMemory() {
        this.killLogcatProcess();
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        this.logInstTestSummary();
        this.killLogcatProcess();
        super.onDestroy();
    }

    private String buildAbsFilePath(String fileName) {
        String externalStoragePath;
        try {
            externalStoragePath = FileUtils.getExternalStoragePath();
        } catch (IOException e) {
            e.printStackTrace();
            externalStoragePath = "/data/local/tmp/";
        }

        String tmpFileName = String.format(fileName, HelperUtils.getCurrentTime());
        return String.format("%s/%s", externalStoragePath, tmpFileName);
    }

    private String buildInstCommand(Intent intent) {
        String testMethod = intent.getStringExtra(EXTRA_KEY_INST_METHOD);
        String testPkgName = intent.getStringExtra(EXTRA_KEY_TEST_PACKAGE);
        String testRunner = intent.getStringExtra(EXTRA_KEY_TEST_RUNNER);
        String testClass = null;
        if (testPkgName != null) {
            testClass = testPkgName.substring(0, testPkgName.lastIndexOf("."));
        } else {
            Log.e(TAG, "var testPkgName is null.");
            this.stopSelf();
        }

        String commandInst = "am instrument -w -r";
        String commandExtraDebug = "-e debug false";
        String commandExtraClass = String.format("-e class %s.%s", testClass, testMethod);
        String commandRunner = String.format("%s/%s", testPkgName, testRunner);
        String command = String.format("%s %s %s %s",
                commandInst, commandExtraDebug, commandExtraClass, commandRunner);

        Log.d(TAG, String.format("The instrument command: %s", command));
        return command;
    }

    private void runInstCommandByExecTime(String command) {
        // need root auth to run instrument command
        // error: current process is killed after start instrument test, LOG:
        // I/ActivityManager(1651): Force stopping com.example.zhengjin.funsettingsuitest appid=1000 user=0: start instr
        // I/ActivityManager(1651): Killing 10979:com.example.zhengjin.funsettingsuitest/1000 (adj 0): stop com.example.zhengjin.funsettingsuitest
        // reason: test app and app under test are from the same AS project
        ShellCmdUtils.CommandResult cr = ShellCmdUtils.execCommand(command, true);
        if (cr.getReturnCode() != 0) {
            ++mTotalFailed;
        }
        logInstTestCmdResults(cr);

        // error: NullPointerException
//        Bundle arguments = new Bundle();
//        arguments.putString("debug", "false");
//        arguments.putString("class",
//                "com.example.zhengjin.funsettingsuitest.testcases.TestPlayingFilm#testDemo");
//        this.startInstrumentation(new ComponentName(testPkgName, testRunner), null, arguments);
    }

    private float getRunPercent(long during, long execTime) {
        float runPercent = (float) during / (float) execTime * 100f;
        if (runPercent > 100f) {
            runPercent = 100f;
        }
        return runPercent;
    }

    private void logInstTestHeader() {
        this.printAndWriteLog("START -----> the instrument test start!\n");
    }

    private void logInstTestSummary() {
        StringBuilder sb = new StringBuilder(5);
        sb.append("END -----> the instrument test finished!\n");
        sb.append(String.format(mLocale, "Total run times: %d\n", mTotalRunTimes));
        sb.append(String.format(mLocale, "Total passed: %d\n", (mTotalRunTimes - mTotalFailed)));
        sb.append(String.format(mLocale, "Total failed: %d\n", mTotalFailed));

        this.printAndWriteLog(sb.toString());
    }

    private void logInstTestCmdResults(ShellCmdUtils.CommandResult cr) {
        StringBuilder sb = new StringBuilder(5);
        sb.append(String.format(mLocale, "The instrument test result code: %d\n", cr.getReturnCode()));
        String tmpMsg = cr.getReturnSuccessMsg();
        if (!StringUtils.isEmpty(tmpMsg)) {
            sb.append(String.format(mLocale, "The instrument test success message: %s\n", tmpMsg));
        }
        tmpMsg = cr.getReturnErrorMsg();
        if (!StringUtils.isEmpty(tmpMsg)) {
            sb.append(String.format(mLocale, "The instrument test error message: %s\n\n", tmpMsg));
        }

        printAndWriteLog(sb.toString());
    }

    private void printAndWriteLog(String content) {
        Log.i(TAG, content);
        FileUtils.writeFileSdcard(mInstTestLogPath, content, true);
    }

    private void runLogcatProcess() {
        String command = String.format("logcat -c && logcat *:E -v time > %s", mLogcatLogPath);
        try {
            mLogcatProcess = Runtime.getRuntime().exec("sh");
            mOutputStream = new DataOutputStream(mLogcatProcess.getOutputStream());
            mOutputStream.write(command.getBytes());
            mOutputStream.writeBytes("\n");
            mOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void killLogcatProcess() {
        if (mLogcatProcess != null) {
            mLogcatProcess.destroy();
        }

        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
