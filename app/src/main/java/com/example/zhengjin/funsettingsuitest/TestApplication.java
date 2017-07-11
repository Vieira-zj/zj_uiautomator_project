package com.example.zhengjin.funsettingsuitest;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by zhengjin on 2016/8/26.
 * <p/>
 * Application for global environment.
 */
public final class TestApplication extends Application {

    public static final String EXTRA_KEY_EXEC_TIME = "ExecTime";
    public static final String EXTRA_KEY_INST_METHOD = "TestInstMethod";
    public static final String EXTRA_KEY_TEST_PACKAGE = "TestPkgName";
    public static final String EXTRA_KEY_TEST_RUNNER = "TestRunner";

    // pre-condition: set "testCoverageEnabled = true" in build.gradle
    public static final boolean IS_COVERAGE_TEST_ENABLE = false;

    private static final boolean IS_LEAK_TEST = false;

    private static TestApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        if (IS_LEAK_TEST) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return;
            }
            LeakCanary.install(this);
        }

        sInstance = this;
    }

    public static TestApplication getInstance() {
        return sInstance;
    }

}
