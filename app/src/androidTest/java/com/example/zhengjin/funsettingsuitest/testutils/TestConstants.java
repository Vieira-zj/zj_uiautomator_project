package com.example.zhengjin.funsettingsuitest.testutils;

import android.util.Log;

import com.example.zhengjin.funsettingsuitest.utils.FileUtils;

import java.io.IOException;

/**
 * Created by zhengjin on 2016/6/2.
 * <p>
 * Include the testing constants in common use.
 */
public final class TestConstants {

    private final static String TAG = TestConstants.class.getSimpleName();
    public final static String LOG_KEYWORD = "ZJTest => ";

    public final static long SHORT_WAIT = 1000L;
    public final static long WAIT = 3000L;
    public final static long LONG_WAIT = 5000L;
    public final static long TIME_OUT = 8000L;
    public final static long LONG_TIME_OUT = 15 * 1000L;

    final static String LOG_LEVEL_DEBUG = "D";
    public final static String LOG_LEVEL_INFO = "I";
    public final static String LOG_LEVEL_WARN = "W";
//    public final static String LOG_LEVEL_ERROR = "E";
//    public final static String LOG_LEVEL_ASSERT = "A";

    public final static String TEXT_COMMON_SETTINGS = "通用设置";

    public final static String LAUNCHER_PKG_NAME = "com.bestv.ott";
    public final static String SETTINGS_PKG_NAME = "tv.fun.settings";
    public final static String FILE_MANAGER_PKG_NAME = "tv.fun.filemanager";
    public final static String WEATHER_PKG_NAME = "tv.fun.weather";

    public final static String LAUNCHER_HOME_ACT = ".home.HomeActivity";
    public final static String SETTINGS_HOME_ACT = ".general.GeneralSettingsActivity";
    public final static String ADVANCE_SETTINGS_HOME_ACT = ".general.AdvancedSettingsActivity";
    public final static String SETTINGS_IMAGE_AND_SOUND_ACT = ".picaudio.PicAudioSettingsActivity";
    public final static String SETTINGS_ABOUT_INFO_ACT = ".about.AboutActivity";
    public final static String FILE_MANAGER_HOME_ACT = ".FunFileManagerActivity";
    public final static String VIDEO_SUB_PAGE_ACT = ".retrieve.RetrieveActivity";
    public final static String VIDEO_PLAYER_ACT = ".player.FunVideoPlayerActivity";

    public final static String CLASS_TEXT_VIEW = "android.widget.TextView";
    public final static String CLASS_SCROLL_VIEW = "android.widget.ScrollView";
    public final static String CLASS_TEXT_SWITCHER = "android.widget.TextSwitcher";
    public final static String CLASS_WEB_VIEW = "android.webkit.WebView";
    public final static String CLASS_RELATIVE_LAYOUT = "android.widget.RelativeLayout";

    public final static String TEST_ROOT_DIR_PATH = getTestingRootPath();
    final static String SNAPSHOT_PATH = getSnapshotDirPath();
    final static String LOGCAT_LOG_PATH = getLogcatLogDirPath();

    private TestConstants() {
    }

    private static String getExternalStoragePath() {
        try {
            return FileUtils.getExternalStoragePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String defaultPath = "/data/local/tmp";
        Log.w(TAG, "Sdcard is not available, and use default path: " + defaultPath);
        return defaultPath;
    }

    private static String getTestingRootPath() {
        final String TEST_ROOT_DIR_NAME = "auto_test_logs";
        return String.format("/%s/%s", getExternalStoragePath(), TEST_ROOT_DIR_NAME);
    }

    private static String getSnapshotDirPath() {
        final String TEST_SNAPSHOT_DIR_NAME = "snapshots";
        return String.format("%s/%s", getTestingRootPath(), TEST_SNAPSHOT_DIR_NAME);
    }

    private static String getLogcatLogDirPath() {
        final String TEST_LOGCAT_DIR_NAME = "logcat_log";
        return String.format("%s/%s", getTestingRootPath(), TEST_LOGCAT_DIR_NAME);
    }

    public enum FunSystemVersion {

        V2("FunUI_v2.0"),
        V3("FunUI_v3.0");

        private final String name;

        FunSystemVersion(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum PlatformChipType {

        MSTAR638("Mstar_638"),
        MSTAR938("Mstar_938");

        private final String name;

        PlatformChipType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    @SuppressWarnings("unused")
    public enum VideoType {
        FILM, TV, VARIETY_SHOW
    }

}
