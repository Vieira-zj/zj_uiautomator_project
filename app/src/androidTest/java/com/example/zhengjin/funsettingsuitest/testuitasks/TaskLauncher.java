package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testrunner.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionCenter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionHome;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LAUNCHER_HOME_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LAUNCHER_PKG_NAME;
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

    public static final String TAG = TaskLauncher.class.getSimpleName();

    public static final String[] LAUNCHER_HOME_TABS =
            {"电视", "视频", "体育", "少儿", "应用", "设置"};

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

//    public static BySelector getQuickAccessBtnNetworkSelector() {
//        return By.res("com.bestv.ott:id/network");
//    }

    private static BySelector getSettingsEntrySelector() {
        return By.res("com.bestv.ott:id/setting_entry");
    }

    public static BySelector getLoadingCircleSelector() {
        return By.res("com.bestv.ott:id/progressBar");
    }
    public static void backToLauncher() {
        if (RunnerProfile.isPlatform938) {
            backToLauncherByShell();
        } else {
            backToLauncherByDevice();
        }
    }

    private static void backToLauncherByDevice() {
        ACTION.doDeviceActionAndWait(new DeviceActionHome(), WAIT);
        Assert.assertTrue("backToLauncher, failed to back to the launcher home.",
                TestHelper.waitForAppOpenedByUntil(DEVICE.getLauncherPackageName()));
    }

    private static void backToLauncherByPm() {
        ACTION.doDeviceActionAndWait(new DeviceActionHome(), WAIT);
        Assert.assertTrue("backToLauncherByPm, failed to back to the launcher home.",
                getLauncherPackageName().equals(DEVICE.getCurrentPackageName()));
    }

    private static void backToLauncherByShell() {
        ACTION.doDeviceActionAndWait(new DeviceActionHome(), WAIT);
        Assert.assertTrue("backToLauncherByShell, failed to back to the launcher home.",
                TestHelper.waitForActivityOpenedByShellCmd(LAUNCHER_PKG_NAME, LAUNCHER_HOME_ACT));
    }

    public static String getLauncherPackageName() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return resolveInfo.activityInfo.packageName;
    }

    public static void navigateToSpecifiedTopTab(String tabText) {
        navigateToVideoTab();

        UiObject2 tabApp;
        if (RunnerProfile.isVersion30 && LAUNCHER_HOME_TABS[5].equals(tabText)) {
            tabApp = DEVICE.findObject(getSettingsEntrySelector());
        } else {
            tabApp = getSpecifiedTab(tabText);
        }
        for (int i = 0, moveTimes = 7; i < moveTimes; i++) {
            ACTION.doDeviceActionAndWait(new DeviceActionMoveRight());
            if (tabApp != null) {
                if (tabApp.isFocused() || tabApp.isSelected()) {
                    return;
                }
            }
        }

        Assert.assertTrue(String.format(
                "navigateToSpecifiedTopTab, tab %s is NOT focused.", tabText), false);
    }

    public static void navigateToVideoTab() {
        backToLauncherByPm();
        ACTION.doDeviceActionAndWait(new DeviceActionMoveUp());

        UiObject2 tabVideo = getSpecifiedTab(LAUNCHER_HOME_TABS[1]);
        Assert.assertNotNull("navigateToVideoTab, video tab is NOT found.", tabVideo);
        Assert.assertTrue("navigateToVideoTab, video is NOT focused.",
                (tabVideo.isFocused() || tabVideo.isSelected()));
    }

    @Nullable
    private static UiObject2 getSpecifiedTab(String tabName) {
        List<UiObject2> tabs = DEVICE.findObjects(getAllLauncherTabsSelector());
        if (tabs.size() == 0) {
            Log.e(TAG, "getSpecifiedTab, no tabs found on launcher!");
            return null;
        }

        for (UiObject2 tab : tabs) {
            if (tabName.equals(tab.getText())) {
                return tab.getParent();
            }
        }
        return null;
    }

    public static void openSpecifiedAppFromAppTab(String appName) {
        openSpecifiedCardFromTopTab(LAUNCHER_HOME_TABS[4], appName);
    }

    static void openSpecifiedCardFromSettingsTab(String cardText) {
        openSpecifiedCardFromTopTab(LAUNCHER_HOME_TABS[5], cardText);
    }

    private static void openSpecifiedCardFromTopTab(String tabText, String cardText) {
        navigateToSpecifiedTopTab(tabText);
        ACTION.doDeviceActionAndWait(new DeviceActionCenter(), WAIT);

        UiObject2 appCard = DEVICE.findObject(By.text(cardText));
        Assert.assertNotNull("OpenSpecifiedCardFromTopTab, tab NOT found: " + cardText, appCard);
        ACTION.doClickActionAndWait(appCard.getParent());  // set focus
        ACTION.doDeviceActionAndWait(new DeviceActionEnter(), LONG_WAIT);
    }

    public static void clickOnButtonFromTopQuickAccessBar(BySelector selector) {
        showLauncherTopBar();
        UiObject2 quickAccessBtn = DEVICE.findObject(selector);
        Assert.assertNotNull("clickOnButtonFromTopQuickAccessBar, " +
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
        Assert.assertTrue("showLauncherTopBar, top bar is NOT enabled.", bar.isEnabled());
    }

}
