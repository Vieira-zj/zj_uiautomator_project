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
