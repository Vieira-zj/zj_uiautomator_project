package com.example.zhengjin.funsettingsuitest.Utils;

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
        PackageInfo pkgInfo;
        try {
            pkgInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
            return pkgInfo;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        return null;
    }

    public static List<String> getInstalledApps(Context context, boolean flagIncludeSystemApp) {
        List<String> installedAppsName = new ArrayList<>(80);
        List<ApplicationInfo> installedApps =
                context.getPackageManager().getInstalledApplications(0);

        for (ApplicationInfo app : installedApps) {
            if (flagIncludeSystemApp) {
                installedAppsName.add(app.packageName);
            } else {
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

    public List<ActivityManager.RunningAppProcessInfo> getRunningAppsInfo(Context context) {
        List<ActivityManager.RunningAppProcessInfo> runApps;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        runApps = am.getRunningAppProcesses();
        return runApps;
    }

    public int getProcessPid(ActivityManager.RunningAppProcessInfo info) {
        return info.pid;
    }

    public String getProcessName(Context context, ActivityManager.RunningAppProcessInfo info) {
        List<ApplicationInfo> installedApps =
                context.getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo app : installedApps) {
            if (app.processName.equals(info.processName)) {
                return app.packageName;
            }
        }

        return info.processName;
    }

    public int getProcessMemPss(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Debug.MemoryInfo[] infos = am.getProcessMemoryInfo(new int[] {pid});
        return infos[0].getTotalPss();
    }

    public static void getPackageCacheSize() {
        // TODO: 2016/8/24
    }

}
