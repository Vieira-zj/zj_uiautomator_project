package com.example.zhengjin.funsettingsuitest.testcasedemos;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcases.TestCommonSettings;
import com.example.zhengjin.funsettingsuitest.testcases.TestFileManager;
import com.example.zhengjin.funsettingsuitest.testcases.TestWeather;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;
import com.example.zhengjin.funsettingsuitest.testrunner.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskSettings;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;
import com.example.zhengjin.funsettingsuitest.utils.StringUtils;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.Locale;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.FILE_MANAGER_HOME_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.FILE_MANAGER_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_HOME_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;


/**
 * Created by zhengjin on 2016/6/1.
 * <p>
 * Include test cases for ShellCmdUtils.java
 */
@RunWith(AndroidJUnit4.class)
public final class TestShellUtils {

    private final static String TAG = TestShellUtils.class.getSimpleName();

    private UiDevice mDevice;
    private Context mContext;

    @Before
    public void setUp() {
        Log.d(TAG, String.format("***** Test %s start.", TAG));
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mContext = InstrumentationRegistry.getContext();
    }

    @After
    public void clearUp() {
        Log.d(TAG, String.format("***** Test %s finished.", TAG));
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test01PrintTestCasesName() {
        int count = 0;
        Class<?>[] classes = {TestCommonSettings.class, TestFileManager.class, TestWeather.class};
        for (Class<?> cls : classes) {
            count += RunnerProfile.countAndPrintTestCasesForClass(cls);
        }
        Log.d(TAG, String.format("Total number of test cases -> %d", count));
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test02ExecShellShCommand() {
        String command = "cat /system/build.prop | grep ro.product.model";
        ShellUtils.CommandResult cr = ShellUtils.execCommand(command, false, true);

        String output = String.format(Locale.getDefault(),
                "Result code: %d\n Success message: %s\n Error message: %s",
                cr.mResult,
                (StringUtils.isEmpty(cr.mSuccessMsg) ? "null" : cr.mSuccessMsg),
                (StringUtils.isEmpty(cr.mErrorMsg) ? "null" : cr.mErrorMsg));
        Log.d(TAG, TestConstants.LOG_KEYWORD + output);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test03ExecShellShCommand() {
        // uid 1000 not allowed to su
//        final String command = "chmod 666 /dev/input/event3";
        final String command = "ls /data/data/tv.fun.ottsecurity/databases";

        ShellUtils.CommandResult cr = ShellUtils.execCommand(command, false, true);
        Log.d(TAG, TestConstants.LOG_KEYWORD + "result code: " + cr.mResult);
        if (cr.mSuccessMsg.length() > 0) {
            Log.d(TAG, TestConstants.LOG_KEYWORD + "success message: " + cr.mErrorMsg);
        }
        if (cr.mErrorMsg.length() > 0) {
            Log.d(TAG, TestConstants.LOG_KEYWORD + "error message: " + cr.mErrorMsg);
        }
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test04StopAndStartFileManager() {
        // Note: need system authorized to execute 'start' and 'stop' shell command

        String cmd = String.format("am force-stop %s", FILE_MANAGER_PKG_NAME);
        ShellUtils.CommandResult cr = ShellUtils.execCommand(cmd, false, true);
        String output = String.format(Locale.getDefault(),
                "Stop file manager\nResult code: %d\nSuccess message: %s\nError message: %s",
                cr.mResult,
                (StringUtils.isEmpty(cr.mSuccessMsg) ? "null" : cr.mSuccessMsg),
                (StringUtils.isEmpty(cr.mErrorMsg) ? "null" : cr.mErrorMsg));
        Log.d(TAG, output);
        ShellUtils.systemWaitByMillis(TestConstants.WAIT);

        // add extra option "--user 0"
        cmd = String.format("am start --user 0 %s/%s",
                FILE_MANAGER_PKG_NAME, FILE_MANAGER_HOME_ACT);
        cr = ShellUtils.execCommand(cmd, false, true);
        output = String.format(Locale.getDefault(),
                "Start file manager\nResult code: %d\nSuccess message: %s\nError message: %s",
                cr.mResult,
                (StringUtils.isEmpty(cr.mSuccessMsg) ? "null" : cr.mSuccessMsg),
                (StringUtils.isEmpty(cr.mErrorMsg) ? "null" : cr.mErrorMsg));
        Log.d(TAG, output);

        ShellUtils.systemWaitByMillis(TestConstants.WAIT);
        Log.d(TAG, String.format("Top package is %s", mDevice.getCurrentPackageName()));
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test05StopAndStartCommonSettings() {
        String cmdStop = String.format("am force-stop %s", SETTINGS_PKG_NAME);
        String cmdStart = String.format("am start %s/%s", SETTINGS_PKG_NAME, SETTINGS_HOME_ACT);

        ShellUtils.CommandResult cr = ShellUtils.execCommand(
                new String[]{cmdStop, cmdStart}, false, true);
        String output = String.format(Locale.getDefault(),
                "Result code: %d\nSuccess message: %s\nError message: %s", cr.mResult,
                (StringUtils.isEmpty(cr.mSuccessMsg) ? "null" : cr.mSuccessMsg),
                (StringUtils.isEmpty(cr.mErrorMsg) ? "null" : cr.mErrorMsg));
        Log.d(TAG, output);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test06TakeScreenCapture() {
        TaskLauncher.backToLauncher();
        TestHelper.assertTrueAndTakeCaptureIfFailed("testTakeScreenCapture", false);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test07ClearAndDumpLogcatLog() {
        ShellUtils.clearLogcatLog();
        this.wait10Seconds();
        ShellUtils.dumpLogcatLog();

        ShellUtils.systemWaitByMillis(SHORT_WAIT);
        ShellUtils.clearLogcatLog();
        this.wait10Seconds();
        ShellUtils.dumpLogcatLog(TestConstants.LOG_LEVEL_INFO);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test08StartAndStopLogcatLog() {
        Thread t = ShellUtils.startLogcatLog(TestConstants.LOG_LEVEL_WARN);
        this.wait10Seconds();
        ShellUtils.stopLogcatLog(t);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test09PrintEnabledImeList() {
        TaskSettings task = TaskSettings.getInstance();
        task.isInputMethodEnabled();
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test10GetCurRunningMethodName() {
        Log.d(TAG, String.format("Current running test: %s", ShellUtils.getRunningMethodName()));
        Assert.assertTrue("Verify get current running test name.", true);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test11GetPlatformChipType() {
        if (RunnerProfile.isPlatform938) {
            Log.d(TAG, TestConstants.LOG_KEYWORD + "Platform chiptype is 938");
        } else {
            Log.d(TAG, TestConstants.LOG_KEYWORD + "Platform chiptype is 638");
        }
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test12GetSystemVersion30() {
        if (RunnerProfile.isVersion30) {
            Log.d(TAG, TestConstants.LOG_KEYWORD + "Version number is 3.0");
        } else {
            Log.d(TAG, TestConstants.LOG_KEYWORD + "Version number is 2.0");
        }
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test21StartActivityByActionFromContext() {
        Intent intent = new Intent();
        intent.setAction("android.settings.WIFI_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

        Assert.assertTrue("Demo, start activity by action from testing context.", true);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test22StartActivityByClassNameFromContext() {
        Intent intent = new Intent();
        intent.setClassName("com.bestv.ott", "com.bestv.ott.screen.vip");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

        Assert.assertTrue("Demo, start activity by class name from testing context.", true);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test23ContentProviderFromContext() {
        // TODO: 2017/5/4  
    }

    private void wait10Seconds() {
        ShellUtils.systemWaitByMillis(10 * SHORT_WAIT);
    }

}
