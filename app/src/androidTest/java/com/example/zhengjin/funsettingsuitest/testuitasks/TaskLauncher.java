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

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/6/1.
 * <p>
 * Include the UI tasks on Launcher.
 * This task is used by each module, so keep static.
 */
public final class TaskLauncher {

    private static UiActionsManager ACTION;
    private static UiDevice DEVICE;

    static {
        ACTION = UiActionsManager.getInstance();
        DEVICE = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    private static BySelector getAllLauncherTabsSelector() {
        return By.res("com.bestv.ott:id/tab_title");
    }

    private static BySelector getLauncherTopBarSelector() {
        return By.res("com.bestv.ott:id/container");
    }

    public static BySelector getQuickAccessBtnSettingsSelector() {
        return By.res("com.bestv.ott:id/setting");
    }

    public static BySelector getQuickAccessBtnWeatherSelector() {
        return By.res("com.bestv.ott:id/weather");
    }

    public static BySelector getQuickAccessBtnNetworkSelector() {
        return By.res("com.bestv.ott:id/network");
    }

    public static void backToLauncher() {
        // method 1
//        ACTION.doDeviceActionAndWait(new DeviceActionHome(), WAIT);
//        Assert.assertTrue("Error in backToLauncher(), fail to back to the launcher home.",
//                getLauncherPackageName().equals(DEVICE.getCurrentPackageName()));

        // method 2
        ACTION.doDeviceActionAndWait(new DeviceActionHome(), WAIT);
        Assert.assertTrue("Error in backToLauncher(), fail to back to the launcher home.",
                TestHelper.waitForAppOpenedByUntil(DEVICE.getLauncherPackageName()));
    }

    public static String getLauncherPackageName() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return resolveInfo.activityInfo.packageName;
    }

    public static void navigateToVideoTab() {
        backToLauncher();
        ACTION.doDeviceActionAndWait(new DeviceActionMoveUp());

        UiObject2 tabVideo = getSpecifiedTab("视频");
        Assert.assertNotNull("Error in navigateToVideoTab(), textVideoTab is NOT found.",
                tabVideo);
        Assert.assertTrue("Error in navigateToVideoTab(), textVideoTab is NOT focused.",
                tabVideo.getParent().isFocused());
    }

    public static void navigateToAppTab() {
        navigateToVideoTab();
        for (int i = 0, moveTimes = 4; i < moveTimes; i++) {
            ACTION.doDeviceActionAndWait(new DeviceActionMoveRight());
            UiObject2 tabApp = getSpecifiedTab("应用");
            if (tabApp != null && tabApp.getParent().isFocused()) {
                return;
            }
        }

        Assert.assertTrue("Error in navigateToAppTab(), textAppTab is NOT focused.", false);
    }

    private static UiObject2 getSpecifiedTab(String tabName) {
        List<UiObject2> tabs = DEVICE.findObjects(getAllLauncherTabsSelector());
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

    public static void openSpecifiedAppFromAppTab(String appName) {
        focusOnSpecifiedAppFromAppTab(appName);
        Assert.assertTrue(ACTION.doDeviceActionAndWait(new DeviceActionEnter(), LONG_WAIT));
    }

    private static void focusOnSpecifiedAppFromAppTab(String appName) {
        navigateToAppTab();

        UiObject2 appTest = DEVICE.findObject(By.text(appName));
        Assert.assertNotNull(String.format(
                "Error in openSpecifiedAppFromAppTab(), app %s is NOT found.", appName),
                appTest);

        UiObject2 appContainer = appTest.getParent();
        Assert.assertNotNull(String.format(
                "Error in openSpecifiedAppFromAppTab(), app container %s is NOT found.", appName),
                appContainer);

        ACTION.doClickActionAndWait(appContainer);
    }

    public static void clickOnButtonFromTopQuickAccessBar(BySelector selector) {
        showLauncherTopBar();
        UiObject2 quickAccessBtn = DEVICE.findObject(selector);
        Assert.assertNotNull("Error in clickOnButtonFromTopQuickAccessBar(), " +
                "the settings button from top bar is NOT found.", quickAccessBtn);

        if (!quickAccessBtn.isFocused()) {
            ACTION.doClickActionAndWait(quickAccessBtn);
        }
        ACTION.doDeviceActionAndWait(new DeviceActionEnter(), LONG_WAIT);
    }

    private static void showLauncherTopBar() {
        backToLauncher();
        ACTION.doRepeatDeviceActionAndWait(new DeviceActionMoveUp(), 2);

        UiObject2 bar = DEVICE.findObject(getLauncherTopBarSelector());
        Assert.assertNotNull(bar);
        Assert.assertTrue("Error in showLauncherTopBar(), top bar is NOT enabled.",
                bar.isEnabled());
    }

}
