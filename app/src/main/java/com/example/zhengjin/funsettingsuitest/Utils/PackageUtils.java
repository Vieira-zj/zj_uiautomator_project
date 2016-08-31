package com.example.zhengjin.funsettingsuitest.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Debug;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.TestApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjin on 2016/8/24.
 *
 * Include utils for app package.
 */
public final class PackageUtils {

    private static final String TAG = PackageUtils.class.getSimpleName();
    private static final TestApplication CONTEXT;
    private static final ActivityManager AM;

    static {
        CONTEXT = TestApplication.getInstance();
        AM = (ActivityManager) CONTEXT.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public static PackageInfo getAppPackageInfo(String pkgName) {
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = CONTEXT.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        return pkgInfo;
    }

    public static List<String> getInstalledApps(boolean flagIncludeSystemApp) {
        List<String> installedAppsName = new ArrayList<>(50);
        List<ApplicationInfo> installedApps =
                CONTEXT.getPackageManager().getInstalledApplications(0);

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

    public static List<ActivityManager.RunningAppProcessInfo> getRunningAppsProcessInfo() {
        return AM.getRunningAppProcesses();
    }

    public static int getProcessMemPss(int pid) {
        Debug.MemoryInfo[] memoryInfo = AM.getProcessMemoryInfo(new int[] {pid});
        return memoryInfo[0].getTotalPss();
    }

    // TODO: 2016/8/26 Get cache size using reflection
//    public static void getPackageCacheSize() {
//    }

    public static boolean startApp(String pkgName) {
        try {
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(pkgName);

            List<ResolveInfo> apps =
                    CONTEXT.getPackageManager().queryIntentActivities(resolveIntent, 0);
            int size = apps.size();
            if (size != 1) {
                Log.w(TAG, String.format(CONTEXT.mLocale, "The are (%d) packages are found.", size));
                return false;
            }

            ResolveInfo ri = apps.iterator().next();
            String className = ri.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(new ComponentName(pkgName, className));
            CONTEXT.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean isAppOnTop(String pkgName) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningTaskInfo> taskInfo = AM.getRunningTasks(5);
            String top = taskInfo.get(0).topActivity.getPackageName();
            if (pkgName.equals(top)) {
                return true;
            }
        }

        return false;
    }

    public static void killBgProcess(String pkgName) {
        AM.killBackgroundProcesses(pkgName);
    }

    public static boolean isServiceRunning(ComponentName service) {
        List<ActivityManager.RunningServiceInfo> runningServices = AM.getRunningServices(256);
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServices) {
            if (serviceInfo.service.equals(service)) {
                return true;
            }
        }

        return false;
    }

}
