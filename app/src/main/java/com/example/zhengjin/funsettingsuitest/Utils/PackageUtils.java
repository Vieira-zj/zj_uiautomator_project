package com.example.zhengjin.funsettingsuitest.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Debug;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.activity.TestApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjin on 2016/8/24.
 *
 * Include utils for app packages.
 */
public final class PackageUtils {

    private static final String TAG = PackageUtils.class.getSimpleName();
    private static final TestApplication TEST_APP;
    private static final Context CONTEXT;

    static {
        TEST_APP = TestApplication.getInstance();
        CONTEXT = TEST_APP.getApplicationContext();
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
        ActivityManager am = (ActivityManager) CONTEXT.getSystemService(Context.ACTIVITY_SERVICE);
        return am.getRunningAppProcesses();
    }

    public static int getProcessMemPss(int pid) {
        ActivityManager am = (ActivityManager) CONTEXT.getSystemService(Context.ACTIVITY_SERVICE);
        Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(new int[] {pid});
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
                Log.w(TAG, String.format(TEST_APP.mLocale, "The are (%d) packages are found.", size));
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

}
