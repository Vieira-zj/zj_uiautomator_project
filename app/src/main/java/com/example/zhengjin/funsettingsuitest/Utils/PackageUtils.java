package com.example.zhengjin.funsettingsuitest.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Debug;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.TestApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengjin on 2016/8/24.
 *
 * Include utils for app package.
 */
public final class PackageUtils {

    private static final String TAG = PackageUtils.class.getSimpleName();
    private static final TestApplication CONTEXT;
    private static final ActivityManager AM;
    private static final PackageManager PM;

    private static PackageSizeInfo sPackageSizeInfo;

    static {
        CONTEXT = TestApplication.getInstance();
        AM = (ActivityManager) CONTEXT.getSystemService(Context.ACTIVITY_SERVICE);
        PM = CONTEXT.getPackageManager();
    }

    public static PackageInfo getAppPackageInfo(String pkgName) {
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = PM.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "NameNotFoundException from getAppPackageInfo()!");
            e.printStackTrace();
        }

        return pkgInfo;
    }

    public static List<String> getInstalledAppsName(boolean flagIncludeSystemApp) {
        List<ApplicationInfo> installedApps = PM.getInstalledApplications(0);
        Collections.sort(installedApps, new ApplicationInfo.DisplayNameComparator(PM));

        List<String> installedAppsName = new ArrayList<>(50);
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

    public static Map<String, String> queryLaunchedApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos =
                PM.queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(PM));

        Map<String, String> launchedApps = new HashMap<>(50);
        for (ResolveInfo info : resolveInfos) {
            launchedApps.put(info.activityInfo.packageName, info.activityInfo.name);
        }

        return launchedApps;
    }

    public static List<InstrumentationInfo> queryInstrumentTests() {
        List<InstrumentationInfo> listInstInfo =
                PM.queryInstrumentation(null, PackageManager.GET_META_DATA);

        if ((listInstInfo != null) && (listInstInfo.size() > 0)) {
            for (InstrumentationInfo inst : listInstInfo) {
                Log.d(TAG, "Instrumentation package name: " + inst.packageName);
                Log.d(TAG, "Instrumentation target package: " + inst.targetPackage);
                Log.d(TAG, "Instrumentation runner: " + inst.name);
            }
            return listInstInfo;
        } else {
            Log.w(TAG, "No instrument tests found on target device!");
            return new ArrayList<>();
        }
    }

    public static List<ActivityManager.RunningAppProcessInfo> getRunningAppsProcessInfo() {
        return AM.getRunningAppProcesses();
    }

    private static ActivityManager.RunningAppProcessInfo getProcessInfoByPackageName(String pkgName) {
        List<ActivityManager.RunningAppProcessInfo> apps = getRunningAppsProcessInfo();
        for (ActivityManager.RunningAppProcessInfo app : apps) {
            if (app.processName != null && app.processName.equals(pkgName)) {
                return app;
            }
        }

        return null;
    }

    public static int getPidByPackageName(String pkgName) {
        ActivityManager.RunningAppProcessInfo appProcessInfo = getProcessInfoByPackageName(pkgName);
        if (appProcessInfo == null) {
            return -1;
        }
        return appProcessInfo.pid;
    }

    public static int getUidByPackageName(String pkgName) {
        ActivityManager.RunningAppProcessInfo appProcessInfo = getProcessInfoByPackageName(pkgName);
        if (appProcessInfo == null) {
            return -1;
        }
        return appProcessInfo.uid;
    }

    public static int getProcessMemPss(int pid) {
        Debug.MemoryInfo[] memoryInfo = AM.getProcessMemoryInfo(new int[] {pid});
        return memoryInfo[0].getTotalPss();
    }

    public static boolean startApp(String pkgName) {
        try {
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(pkgName);

            List<ResolveInfo> apps = PM.queryIntentActivities(resolveIntent, 0);
            int size = apps.size();
            if (size != 1) {
                Log.w(TAG, String.format("The are (%d) packages found.", size));
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
            Log.e(TAG, "Exception from startApp()!");
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    public static String getTopActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningTaskInfo> taskInfo = AM.getRunningTasks(1);
            if (taskInfo != null) {
//                return taskInfo.get(0).topActivity.getPackageName();
                return taskInfo.get(0).topActivity.toString();  // ComponentInfo {Package / Activity}
            }
        }

        return null;
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

    private static void getPackageSizeInfo(String pkgName) {
        if (sPackageSizeInfo != null) {
            return;
        }

        try {
            Method getPackageSizeInfo = PM.getClass().getMethod(
                    "getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            getPackageSizeInfo.invoke(PM, pkgName, new IPackageStatsObserver.Stub() {
                @Override
                public void onGetStatsCompleted(final PackageStats pStats, boolean succeeded)
                        throws RemoteException {
                    if (succeeded && pStats != null) {
                        String codeSize = Formatter.formatFileSize(CONTEXT, pStats.codeSize);
                        String dataSize = Formatter.formatFileSize(CONTEXT, pStats.dataSize);
                        String cacheSize = Formatter.formatFileSize(CONTEXT, pStats.cacheSize);
                        sPackageSizeInfo = new PackageSizeInfo(codeSize, dataSize, cacheSize);
                    }
                }
            });
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Log.e(TAG, "Exception from getPackageSizeInfo()!");
            e.printStackTrace();
        }
    }

    public static PackageSizeInfo getPackageUsedSize(String pkgName) {
        getPackageSizeInfo(pkgName);
        for (int i = 0; i <= 5; i++) {
            // wait for onGetStatsCompleted()
            SystemClock.sleep(1000);
            if (sPackageSizeInfo != null) {
                break;
            }
        }

        return sPackageSizeInfo;
    }

    public static class PackageSizeInfo {

        private String codeSize;
        private String dataSize;
        private String cacheSize;

        public PackageSizeInfo(String codeSize, String dataSize, String cacheSize) {
            this.codeSize = codeSize;
            this.dataSize = dataSize;
            this.cacheSize = cacheSize;
        }

        public String getCodeSize() {
            return codeSize;
        }

        public String getDataSize() {
            return dataSize;
        }

        public String getCacheSize() {
            return cacheSize;
        }
    }

}
