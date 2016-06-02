package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionHome;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import java.util.List;

/**
 * Created by zhengjin on 2016/6/1.
 *
 * Includes the UI tasks on Launcher.
 */
public final class TaskLauncher {

    private final static int WAIT = 1000;
    private final static String TAG;
    private static UiActionsManager ACTION;

    static {
        TAG = TaskLauncher.class.getSimpleName();
        ACTION = UiActionsManager.getInstance();
    }

    public static boolean backToLauncher(UiDevice device) {

        final String results = "com.bestv.ott";

        ACTION.doUiActionAndWait(device, new UiActionHome());
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
        ACTION.doUiActionAndWait(device, new UiActionMoveUp());

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
        int repeatTwoTimes = 2;
        ACTION.doRepeatUiActionAndWait(device, new UiActionMoveRight(), repeatTwoTimes);

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
        if (tabs.size() == 1) {
            return tabs.get(0);
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

    public static boolean openQuickAccessButtonFromTopBar(UiDevice device, String id) {

        UiObject2 quickAccessBtn = getQuickAccessButtonFromTopBar(device, id);
        if (quickAccessBtn == null) {
            Log.e(TAG, "The settings button from top bar is NOT found.");
            return false;
        }
        quickAccessBtn.click();
        ShellUtils.systemWait(WAIT);

        return ACTION.doUiActionAndWait(device, new UiActionEnter());
    }

    private static boolean showLauncherTopBar(UiDevice device) {

        backToLauncher(device);
        int repeatTimes = 2;
        ACTION.doRepeatUiActionAndWait(device, new UiActionMoveUp(), repeatTimes);

        UiObject2 bar = device.findObject(By.res("com.bestv.ott:id/container"));
        if (bar == null) {
            Log.e(TAG, "The top bar on launcher is NOT found.");
            return false;
        }

        return bar.isEnabled();
    }

    private static UiObject2 getQuickAccessButtonFromTopBar(UiDevice device, String id) {

        showLauncherTopBar(device);
        return device.findObject(By.res(id));
    }
}
