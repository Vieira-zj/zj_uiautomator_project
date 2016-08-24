package com.example.zhengjin.funsettingsuitest.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by zhengjin on 2016/8/24.
 *
 * Include utils for app packages.
 */
public final class PackageUtils {

    private static final String TAG = PackageUtils.class.getSimpleName();

    public static String getAppPackageName(Context context) {
        PackageInfo pkgInfo;
        try {
            pkgInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pkgInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        return "";
    }


}
