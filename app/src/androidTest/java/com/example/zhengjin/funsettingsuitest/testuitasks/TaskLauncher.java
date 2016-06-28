package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionHome;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/6/1.
 *
 * Include the UI tasks on Launcher.
 */
public final class TaskLauncher {

//    private final static String TAG = TaskLauncher.class.getSimpleName();
    private static UiActionsManager ACTION = UiActionsManager.getInstance();

    public static BySelector getAllLauncherTabsSelector() {
        return By.res("com.bestv.ott:id/tab_title");
    }

    public static BySelector getLauncherTopBarSelector() {
        return By.res("com.bestv.ott:id/container");
    }

    public static BySelector getQuickAccessBtnSettingsSelector() {
        return By.res("com.bestv.ott:id/setting");
    }

    public static BySelector getQuickAccessBtnNetworkSelector() {
        return By.res("com.bestv.ott:id/network");
    }

    public static void backToLauncher(UiDevice device) {

//        String pkgName = getLauncherPackageName();
        final String launcherPackageName = device.getLauncherPackageName();  //"com.bestv.ott"
        final String message = "Error, fail to back to the launcher.";

        // method 1
//        ACTION.doDeviceActionAndWait(new DeviceActionHome(), WAIT);
//        String pkgName = device.getCurrentPackageName();
//        Assert.assertTrue(message, launcherPackageName.equals(pkgName));

        // method 2
        ACTION.doDeviceActionAndWait(new DeviceActionHome());
        boolean ret = TestHelper.waitForAppOpened(device, launcherPackageName);
        Assert.assertTrue(message, ret);
    }

    public static String getLauncherPackageName() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        String pkgName = resolveInfo.activityInfo.packageName;
        if ((pkgName == null) || ("".equals(pkgName))) {
            String message =
                    "Error in getLauncherPackageName(), the launcher package name is empty or null.";
            Assert.assertTrue(message, false);
        }

        return pkgName;
    }

    public static void navigateToVideoTab(UiDevice device) {

        backToLauncher(device);
        ACTION.doDeviceActionAndWait(new DeviceActionMoveUp());

        final String textVideoTab = ("视频");
        UiObject2 tabVideo = getSpecifiedTab(device, textVideoTab);

        String message = "Error in navigateToVideoTab(), the UI object textVideoTab is NOT found.";
        Assert.assertNotNull(message, tabVideo);

        message = "Error in navigateToVideoTab(), the UI object textVideoTab is NOT focused.";
        Assert.assertTrue(message, tabVideo.isFocused());
    }

    public static void navigateToAppTab(UiDevice device) {

        navigateToVideoTab(device);
        int repeatTwoTimes = 2;
        ACTION.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), repeatTwoTimes);

        String message = "Error in navigateToAppTab(), the UI object textAppTab is NOT found.";
        UiObject2 tabApp = getSpecifiedTab(device, "应用");
        Assert.assertNotNull(message, tabApp);

        message = "Error in navigateToAppTab(), the UI object textAppTab is NOT focused.";
        Assert.assertTrue(message, tabApp.isFocused());
    }

    private static UiObject2 getSpecifiedTab(UiDevice device, String tabName) {

        List<UiObject2> tabs = device.findObjects(getAllLauncherTabsSelector());
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

    public static void openSpecifiedApp(UiDevice device, String appName, String pkgName) {

        focusOnSpecifiedApp(device, appName);
        ACTION.doDeviceActionAndWait(new DeviceActionEnter());
        Assert.assertTrue(TestHelper.waitForAppOpened(device, pkgName));
    }

    public static void openSpecifiedApp(UiDevice device, String appName) {

        focusOnSpecifiedApp(device, appName);
        Assert.assertTrue(ACTION.doDeviceActionAndWait(new DeviceActionEnter(), WAIT));
    }

    private static void focusOnSpecifiedApp(UiDevice device, String appName) {

        navigateToAppTab(device);

        String message = String.format(
                "Error in openSpecifiedApp(), the app %s is NOT found.", appName);
        UiObject2 appTest = device.findObject(By.text(appName));
        Assert.assertNotNull(message, appTest);

        message = String.format(
                "Error in openSpecifiedApp(), the app container %s is NOT found.", appName);
        UiObject2 appContainer = appTest.getParent();
        Assert.assertNotNull(message, appContainer);

        ACTION.doClickActionAndWait(appContainer);
    }

    public static void clickOnQuickAccessButtonFromTopBar(
            UiDevice device, BySelector selector, String pkgName) {

        String message =
                "Error in clickOnQuickAccessButtonFromTopBar(), the settings button from top bar is NOT found.";
        UiObject2 quickAccessBtn = getQuickAccessButtonFromTopBar(device, selector);
        Assert.assertNotNull(message, quickAccessBtn);

        ACTION.doClickActionAndWait(quickAccessBtn);
        ACTION.doDeviceActionAndWait(new DeviceActionEnter());
        Assert.assertTrue(TestHelper.waitForAppOpened(device, pkgName));
    }

    private static void showLauncherTopBar(UiDevice device) {

        backToLauncher(device);
        int repeatTimes = 2;
        ACTION.doRepeatDeviceActionAndWait(new DeviceActionMoveUp(), repeatTimes);

        String message = "Error in showLauncherTopBar(), the top bar on launcher is NOT found.";
        UiObject2 bar = device.findObject(getLauncherTopBarSelector());
        Assert.assertNotNull(message, bar);

        message = "Error in showLauncherTopBar(), the top bar is NOT enabled.";
        Assert.assertTrue(message, bar.isEnabled());
    }

    private static UiObject2 getQuickAccessButtonFromTopBar(UiDevice device, BySelector selector) {
        showLauncherTopBar(device);
        return device.findObject(selector);
    }
}
