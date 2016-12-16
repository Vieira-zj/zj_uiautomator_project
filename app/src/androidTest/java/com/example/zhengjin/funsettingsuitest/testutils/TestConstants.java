package com.example.zhengjin.funsettingsuitest.testutils;

import android.os.Environment;

/**
 * Created by zhengjin on 2016/6/2.
 * <p>
 * Include the testing constants in common use.
 */
public final class TestConstants {

    public final static long SHORT_WAIT = 1000L;
    public final static long WAIT = 3000L;
    public final static long LONG_WAIT = 5000L;
    public final static long TIME_OUT = 8000L;
    public final static long LONG_TIME_OUT = 15 * 1000L;

    public final static String LAUNCHER_PKG_NAME = "com.bestv.ott";
    public final static String SETTINGS_PKG_NAME = "tv.fun.settings";
    public final static String FILE_MANAGER_PKG_NAME = "tv.fun.filemanager";
    public final static String WEATHER_PKG_NAME = "tv.fun.weather";

    public final static String LAUNCHER_HOME_ACT = ".home.HomeActivity";
    public final static String SETTINGS_HOME_ACT = ".general.GeneralSettingsActivity";
    public final static String SETTINGS_IMAGE_AND_SOUND_ACT = ".picaudio.PicAudioSettingsActivity";
    public final static String FILE_MANAGER_HOME_ACT = ".FunFileManagerActivity";
    public final static String VIDEO_SUB_PAGE_ACT = ".retrieve.RetrieveActivity";
    public final static String VIDEO_PLAYER_ACT = ".player.FunVideoPlayerActivity";

    public final static String CLASS_TEXT_VIEW = "android.widget.TextView";
    public final static String CLASS_SCROLL_VIEW = "android.widget.ScrollView";

    private final static String TEST_ROOT_DIR_NAME = "test_logs";
    private final static String TEST_SNAPSHOT_DIR_NAME = "uiautomator_snapshots";
    private final static String TEST_LOGCAT_DIR_NAME = "logcat_log";
    private final static String SDCARD_PATH;
    final static String SNAPSHOT_PATH;
    final static String LOGCAT_PATH;

    private final static String SDCARD_STATUS_UN_AVAILABLE = "sdcard_unavailable";

    static {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();
        } else {
            SDCARD_PATH = SDCARD_STATUS_UN_AVAILABLE;
        }
        SNAPSHOT_PATH =
                String.format("%s/%s/%s", SDCARD_PATH, TEST_ROOT_DIR_NAME, TEST_SNAPSHOT_DIR_NAME);
        LOGCAT_PATH =
                String.format("%s/%s/%s", SDCARD_PATH, TEST_ROOT_DIR_NAME, TEST_LOGCAT_DIR_NAME);
    }

    static boolean isSdcardAvailable() {
        return SDCARD_STATUS_UN_AVAILABLE.equals(SDCARD_PATH);
    }

    public enum VideoType {
        FILM, TV, VARIETY_SHOW
    }

}
