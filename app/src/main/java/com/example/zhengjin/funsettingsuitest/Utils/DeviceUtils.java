package com.example.zhengjin.funsettingsuitest.Utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by zhengjin on 2016/8/23.
 *
 * Include the utils for android device.
 */
public final class DeviceUtils {

    private static String mDeviceModel = null;

    private static int mDisplayWidth = 0;
    private static int mDisplayHeight = 0;

    public static String getDeviceModel() {
        if (StringUtils.isEmpty(mDeviceModel)) {
            mDeviceModel = Build.MODEL;
        }
        return mDeviceModel;
    }

    public static int getDisplayHeight(Context context) {
        if (mDisplayHeight <= 0) {
            getDisplayMetrics(context);
        }
        return mDisplayHeight;
    }

    public static int getDisplayWidth(Context context) {
        if (mDisplayWidth <= 0) {
            getDisplayMetrics(context);
        }
        return mDisplayWidth;
    }

    private static void getDisplayMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        if (wm != null) {
            Display display = wm.getDefaultDisplay();
            if (display != null) {
                DisplayMetrics dm = new DisplayMetrics();
                display.getMetrics(dm);
                mDisplayHeight = dm.heightPixels;
                mDisplayWidth = dm.widthPixels;
            }
        }

    }

}
