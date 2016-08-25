package com.example.zhengjin.funsettingsuitest.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjin on 2016/8/24.
 *
 * Include utils for app packages.
 */
public final class PackageUtils {

    private static final String TAG = PackageUtils.class.getSimpleName();

    public static PackageInfo getAppPackageInfo(Context context, String pkgName) {
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        return pkgInfo;
    }

    public static List<String> getInstalledApps(Context context, boolean flagIncludeSystemApp) {
        List<String> installedAppsName = new ArrayList<>(50);
        List<ApplicationInfo> installedApps =
                context.getPackageManager().getInstalledApplications(0);

        if (flagIncludeSystemApp) {
            for (ApplicationInfo app : installedApps) {
                installedAppsName.add(app.packageName);
            }
        } else {
            for (ApplicationInfo app : installedApps) {
                if (!isSystemApp(app)) {
                    installedAppsName.add(app.packageName);
                }
            }
        }

        return installedAppsName;
    }

    private static boolean isSystemApp(ApplicationInfo info) {
        return (info != null) && ((info.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
    }

    public static List<ActivityManager.RunningAppProcessInfo> getRunningAppsProcessInfo(
            Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return am.getRunningAppProcesses();
    }

    public static int getProcessMemPss(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(new int[] {pid});
        return memoryInfo[0].getTotalPss();
    }

    public static void getPackageCacheSize() {
        // TODO: 2016/8/24
    }

}
