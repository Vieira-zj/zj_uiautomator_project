package com.example.zhengjin.funsettingsuitest.testtasks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import java.util.List;

/**
 * Created by zhengjin on 2016/6/1.
 *
 * Includes the UI tasks on Launcher.
 */
public final class TaskLauncher {

    final static long WAIT = 1000;
    final static String TAG = TaskLauncher.class.getSimpleName();

    public static boolean backToLauncher(UiDevice device) {

        final String results = "com.bestv.ott";

        device.pressHome();
        ShellUtils.systemWait(WAIT);

        String pkgName = getLauncherPackageName();
        if ((pkgName == null) || ("".equals(pkgName))) {
            return false;
        }
        return results.equals(pkgName);
    }

    private static String getLauncherPackageName() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return resolveInfo.activityInfo.packageName;
    }

    public static boolean navigateToVideoTab(UiDevice device) {

        final String textVideoTab = ("视频");

        if (!backToLauncher(device)) {
            return false;
        }
        device.pressDPadUp();
        ShellUtils.systemWait(WAIT);

        UiObject2 tabVideo = getSpecifiedTab(device, textVideoTab);
        if (tabVideo == null) {
            Log.e(TAG, "The UI object textVideoTab is NOT found.");
            return false;
        }

        return tabVideo.isFocused();
    }

    public static boolean navigateToAppTab(UiDevice device) {

        final String textAppTab = ("应用");

        if (!navigateToVideoTab(device)) {
            return false;
        }
        device.pressDPadRight();
        ShellUtils.systemWait(WAIT);
        device.pressDPadRight();
        ShellUtils.systemWait(WAIT);

        UiObject2 tabApp = getSpecifiedTab(device, textAppTab);
        if (tabApp == null) {
            Log.e(TAG, "The UI object textAppTab is NOT found.");
            return false;
        }

        return tabApp.isFocused();
    }

    private static UiObject2 getSpecifiedTab(UiDevice device, String tabName) {

        final String tabId = "com.bestv.ott:id/tab_title";

        List<UiObject2> tabs = device.findObjects(By.res(tabId));
        if (tabs.size() == 0) {
            return null;
        }

        for (UiObject2 tab : tabs) {
            if (tabName.equals(tab.getText())) {
                return tab;
            }
        }

        return null;
    }

    public static boolean openSpecifiedApp(UiDevice device, String appName) {

        navigateToAppTab(device);

        UiObject2 appTest = device.findObject(By.text(appName));
        if (appTest == null) {
            Log.e(TAG, String.format("The app %s is NOT found.", appName));
            return false;
        }

        UiObject2 appContainer = appTest.getParent();
        if (appContainer == null) {
            Log.e(TAG, String.format("The app container %s is NOT found.", appName));
            return false;
        }

        appContainer.click();
        ShellUtils.systemWait(WAIT);

        return device.pressEnter();
    }


}
