package com.example.zhengjin.funsettingsuitest.testutils;

import android.os.Environment;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include the testing constants in common use.
 */
public final class TestConstants {

    public final static long SHORT_WAIT = 1000;
    public final static long WAIT = 3000;
    public final static long LONG_WAIT = 5000;
    public final static long TIME_OUT = 8000;

    public final static String FILE_MANAGER_PKG_NAME = "tv.fun.filemanager";
    public final static String SETTINGS_PKG_NAME = "tv.fun.settings";

    // cards text on launcher home left area
    public final static String FILM_CARD_TEXT = "电影";
    public final static String TV_SERIAL_CARD_TEXT = "电视剧";
    public final static String CHILDREN_CARD_TEXT = "少儿";
    public final static String VARIETY_CARD_TEXT = "综艺";
    public final static String FOLLOWING_TV_SERIAL_TEXT = "跟播";
    public final static String NEWLY_ADD_IN_7_DAYS_TEXT = "7日新增";

    public final static String SDCARD_PATH;
    public final static String TEST_ROOT_PATH;
    public final static String CAPTURES_PATH;

    static {
        SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();
        TEST_ROOT_PATH = String.format("%s/testlogs/", SDCARD_PATH);
        CAPTURES_PATH = TEST_ROOT_PATH + "uiautomator_captures";
    }

}
