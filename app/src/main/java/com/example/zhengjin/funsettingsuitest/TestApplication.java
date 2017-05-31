package com.example.zhengjin.funsettingsuitest;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

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

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        sInstance = this;
    }

    public static TestApplication getInstance() {
        return sInstance;
    }

}
