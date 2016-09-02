package com.example.zhengjin.funsettingsuitest;

import android.app.Application;
import android.util.Log;

import java.util.Locale;

/**
 * Created by zhengjin on 2016/8/26.
 * <p/>
 * Application for global environment.
 */
public final class TestApplication extends Application {

    private static TestApplication sInstance;

    public static final String EXTRA_KEY_EXEC_TIME = "ExecTime";
    public static final String EXTRA_KEY_INST_METHOD = "TestInstMethod";
    public static final String EXTRA_KEY_TEST_PACKAGE = "TestPkgName";
    public static final String EXTRA_KEY_TEST_RUNNER = "TestRunner";

    public static final String INST_LOG_FILE_NAME = "inst_test_log_%s.log";

    public final Locale mLocale = Locale.getDefault();

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static TestApplication getInstance() {
        return sInstance;
    }

    public void logException(String tag, Exception e) {
        Log.e(tag, String.format(mLocale, "EXCEPTION: %s", e.getMessage()));
        e.printStackTrace();
    }
}
