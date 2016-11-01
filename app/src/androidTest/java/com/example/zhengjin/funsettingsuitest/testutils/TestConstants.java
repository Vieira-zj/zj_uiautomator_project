package com.example.zhengjin.funsettingsuitest.testutils;

import android.os.Environment;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include the testing constants in common use.
 */
public final class TestConstants {

    public final static long SHORT_WAIT = 1000L;
    public final static long WAIT = 3000L;
    public final static long LONG_WAIT = 5000L;
    public final static long TIME_OUT = 8000L;

    public final static String SETTINGS_PKG_NAME = "tv.fun.settings";
    public final static String FILE_MANAGER_PKG_NAME = "tv.fun.filemanager";
    public final static String WEATHER_PKG_NAME = "tv.fun.weather";

    public final static String SETTINGS_HOME_ACT = ".general.GeneralSettingsActivity";
    public final static String FILE_MANAGER_HOME_ACT = ".FunFileManagerActivity";

    public final static String CLASS_TEXT_VIEW = "android.widget.TextView";
    public final static String CLASS_SCROLL_VIEW = "android.widget.ScrollView";

    public final static String SDCARD_PATH;
    public final static String TEST_ROOT_PATH;
    public final static String CAPTURES_PATH;

    static {
        SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();
        TEST_ROOT_PATH = String.format("%s/testlogs/", SDCARD_PATH);
        CAPTURES_PATH = TEST_ROOT_PATH + "uiautomator_captures";
    }

}
