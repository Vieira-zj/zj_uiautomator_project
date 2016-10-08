package com.example.zhengjin.funsettingsuitest.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhengjin on 2016/9/2.
 *
 * Include the helper utils.
 */
public final class HelperUtils {

    public static String getCurrentTime() {
        SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss-SSS", Locale.getDefault());
        Date curTime = new Date(System.currentTimeMillis());
        return formatter.format(curTime);
    }

}
