package com.example.zhengjin.funsettingsuitest.testcasedemos;

import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.util.List;
import java.util.Locale;

import static com.example.zhengjin.funsettingsuitest.testutils.ShellUtils.execCommand;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.FILE_MANAGER_HOME_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.FILE_MANAGER_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_HOME_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;
import static java.lang.String.format;


/**
 * Created by zhengjin on 2016/6/1.
 * <p>
 * Include test cases for ShellCmdUtils.java
 */
@RunWith(AndroidJUnit4.class)
public final class TestShellUtils {

    private final static String TAG = TestShellUtils.class.getSimpleName();

    private Context mContext;
    private UiDevice mDevice;

    @Before
    public void setUp() {
        Log.d(TAG, format("***** Test %s start.", TAG));
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        mDevice = UiDevice.getInstance(instrumentation);
        mContext = InstrumentationRegistry.getContext();
    }

    @After
    public void clearUp() {
        Log.d(TAG, format("***** Test %s finished.", TAG));
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test01PrintTestCasesName() {
        int count = 0;
        Class<?>[] classes = {TestCommonSettings.class, TestFileManager.class, TestWeather.class};
        for (Class<?> cls : classes) {
            count += RunnerProfile.countAndPrintTestCasesForClass(cls);
        }
        Log.d(TAG, format("Total number of test cases -> %d", count));
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test02ExecShellShCommand() {
        String command = "cat /system/build.prop | grep ro.product.model";
        ShellUtils.CommandResult cr = execCommand(command, false, true);

        String output = format(Locale.getDefault(),
                "Result code: %d\n Success message: %s\n Error message: %s",
                cr.mReturnCode,
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

        ShellUtils.CommandResult cr = execCommand(command, false, true);
        Log.d(TAG, TestConstants.LOG_KEYWORD + "result code: " + cr.mReturnCode);
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

        String cmd = format("am force-stop %s", FILE_MANAGER_PKG_NAME);
        ShellUtils.CommandResult cr = execCommand(cmd, false, true);
        String output = format(Locale.getDefault(),
                "Stop file manager\nResult code: %d\nSuccess message: %s\nError message: %s",
                cr.mReturnCode,
                (StringUtils.isEmpty(cr.mSuccessMsg) ? "null" : cr.mSuccessMsg),
                (StringUtils.isEmpty(cr.mErrorMsg) ? "null" : cr.mErrorMsg));
        Log.d(TAG, output);
        ShellUtils.systemWaitByMillis(TestConstants.WAIT);

        // add extra option "--user 0"
        cmd = format("am start --user 0 %s/%s",
                FILE_MANAGER_PKG_NAME, FILE_MANAGER_HOME_ACT);
        cr = execCommand(cmd, false, true);
        output = format(Locale.getDefault(),
                "Start file manager\nResult code: %d\nSuccess message: %s\nError message: %s",
                cr.mReturnCode,
                (StringUtils.isEmpty(cr.mSuccessMsg) ? "null" : cr.mSuccessMsg),
                (StringUtils.isEmpty(cr.mErrorMsg) ? "null" : cr.mErrorMsg));
        Log.d(TAG, output);

        ShellUtils.systemWaitByMillis(TestConstants.WAIT);
        Log.d(TAG, format("Top package is %s", mDevice.getCurrentPackageName()));
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test05StopAndStartCommonSettings() {
        String cmdStop = format("am force-stop %s", SETTINGS_PKG_NAME);
        String cmdStart = format("am start %s/%s", SETTINGS_PKG_NAME, SETTINGS_HOME_ACT);

        ShellUtils.CommandResult cr = execCommand(
                new String[]{cmdStop, cmdStart}, false, true);
        String output = format(Locale.getDefault(),
                "Result code: %d\nSuccess message: %s\nError message: %s", cr.mReturnCode,
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
        Log.d(TAG, format("Current running test: %s", ShellUtils.getRunningMethodName()));
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
    public void test13RemoveFile() {
        final String path = Environment.getExternalStorageDirectory() + File.separator +
                "auto_test_logs/snapshots/snapshot_2017-06-20_11-15-21-393.png";

        File testFile = new File(path);
        Log.d(TAG, TestConstants.LOG_KEYWORD + "remove file: " +
                testFile.getParentFile().getName() + File.separator + testFile.getName());

//        testFile.deleteOnExit();
        if (testFile.exists() && testFile.isFile()) {
            Assert.assertTrue(testFile.delete());
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
    public void test22StartActivityByActionFromCommand() {
        final String cmd = "am start -a android.settings.WIFI_SETTINGS";
        ShellUtils.CommandResult cr = ShellUtils.execCommand(cmd, false, false);
        Assert.assertEquals("Demo, start activity by action from command line.",
                0, cr.mReturnCode);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test23StartActivityByComponentFromContext() {
        Intent intent = new Intent();
        intent.setClassName("com.bestv.ott", "com.bestv.ott.home.HomeActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

        Assert.assertTrue("Demo, start activity by component from testing context.", true);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test24StartActivityByComponentFromCommand() {
        final String cmd = "am start -n \"com.bestv.ott/com.bestv.ott.home.HomeActivity\"";
        ShellUtils.CommandResult cr = ShellUtils.execCommand(cmd, false, false);
        Assert.assertEquals("Demo, start activity by component from command line.",
                0, cr.mReturnCode);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test25StartServiceByActionFromCommand() {
        final String cmd = "am startservice -a tv.fun.settings.action.inputsource";
        ShellUtils.CommandResult cr = ShellUtils.execCommand(cmd, false, false);
        Assert.assertEquals("Demo, start service by action from command line.",
                0, cr.mReturnCode);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test25StartServiceByComponentFromCommand() {
        final String cmd = "am startservice -n tv.fun.tvupgrade/.UpgradeService " +
                "--es service_start_flag boot";
        ShellUtils.CommandResult cr = ShellUtils.execCommand(cmd, false, false);
        Assert.assertEquals("Demo, start service by component from command line.",
                0, cr.mReturnCode);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test26GetCityWeatherFromContentProvider() {
        final String COL_AREA_NAME = "areaName";
        final String COL_TEMP_CUR = "curTemp";

        // WeatherContentProvider.query => content://tv.fun.weather.provider/weather/101200101
        final String WEATHER_URI = "content://tv.fun.weather.provider/weather";
        final long CITY_ID = 101200101;

        Uri contentUri = ContentUris.withAppendedId(Uri.parse(WEATHER_URI), CITY_ID);
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = resolver.query(contentUri, null, null, null, null);
            if (cursor != null) {
                String results = "";
                int areaNameIndex = cursor.getColumnIndex(COL_AREA_NAME);
                int curTempIndex = cursor.getColumnIndex(COL_TEMP_CUR);
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    results += format("City name: %s, and current temp: %s\n",
                            cursor.getString(areaNameIndex), cursor.getString(curTempIndex));
                }
                Log.d(TAG, TestConstants.LOG_KEYWORD + results);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Assert.assertNotNull("Demo, get city weather data from content provider.", cursor);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test27GetAppListFromContentProvider() {
        final String APP_LIST_URI = "content://tv.fun.tvupgrade.upgradeprovider";
        final String QUERY_ARG = "queryAppList";
        final String GET_KEY = "applist";

        Uri contentUri = Uri.parse(APP_LIST_URI);
        ContentResolver resolver = mContext.getContentResolver();
        Bundle bundle = resolver.call(contentUri, QUERY_ARG, null, null);

        if (bundle == null) {
            Assert.fail("Content is null from uri: " + APP_LIST_URI);
        }

        List<String> appList = bundle.getStringArrayList(GET_KEY);
        if (appList == null) {
            Assert.fail("Null for get array list from bundle by key: " + GET_KEY);
        }
        if (appList.size() == 0) {
            Assert.fail("Apps count is zero!");
        }

        Log.d(TAG, TestConstants.LOG_KEYWORD + "app size: " + appList.size());
        for (String app : appList) {
            Log.d(TAG, TestConstants.LOG_KEYWORD + "app: " + app);
        }
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test31QueryDataFromDatabase() {
        final String sqlQuery = "'select Volume from tbl_SoundSetting;'";
        final String cmd = "sqlite3 /tvdatabase/Database/user_setting.db " + sqlQuery;
        ShellUtils.CommandResult cr = ShellUtils.execCommand(cmd, false, true);

        String msg;
        if (cr.mReturnCode == 0) {
            msg = "Success and return content: " + cr.mSuccessMsg;
        } else {
            msg = String.format(Locale.getDefault(),
                    "return code %d, and error message %s", cr.mReturnCode, cr.mErrorMsg);
        }
        Log.d(TAG, TestConstants.LOG_KEYWORD + msg);
    }

    private void wait10Seconds() {
        ShellUtils.systemWaitByMillis(10 * SHORT_WAIT);
    }

}
