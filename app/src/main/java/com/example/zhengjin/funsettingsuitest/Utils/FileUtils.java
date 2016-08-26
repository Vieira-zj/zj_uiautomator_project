package com.example.zhengjin.funsettingsuitest.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by zhengjin on 2016/8/26.
 *
 * Include utils for file handler.
 */
public final class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static File getExternalStorageDir() {
        return isExternalStorageAvailable() ? Environment.getExternalStorageDirectory() : null;
    }



}
