package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionHome;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import junit.framework.Assert;

import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/6/1.
 *
 * Include the UI tasks on Launcher.
 */
public final class TaskLauncher {

//    private final static String TAG;
    private static UiActionsManager ACTION;

    static {
//        TAG = TaskLauncher.class.getSimpleName();
        ACTION = UiActionsManager.getInstance();
    }

    public static void backToLauncher(UiDevice device) {

        ACTION.doUiActionAndWait(device, new UiActionHome());
        String pkgName = getLauncherPackageName();

        final String name = "com.bestv.ott";
        String message = "Error in backToLauncher(), the launcher package name is incorrect.";
        Assert.assertTrue(message, name.equals(pkgName));
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
        ACTION.doUiActionAndWait(device, new UiActionMoveUp());

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
        ACTION.doRepeatUiActionAndWait(device, new UiActionMoveRight(), repeatTwoTimes);

        final String textAppTab = ("应用");
        UiObject2 tabApp = getSpecifiedTab(device, textAppTab);

        String message = "Error in navigateToAppTab(), the UI object textAppTab is NOT found.";
        Assert.assertNotNull(message, tabApp);

        message = "Error in navigateToAppTab(), the UI object textAppTab is NOT focused.";
        Assert.assertTrue(message, tabApp.isFocused());
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

    public static void openSpecifiedApp(UiDevice device, String appName) {

        navigateToAppTab(device);

        UiObject2 appTest = device.findObject(By.text(appName));
        String message = String.format(
                "Error in openSpecifiedApp(), the app %s is NOT found.", appName);
        Assert.assertNotNull(message, appTest);

        UiObject2 appContainer = appTest.getParent();
        message = String.format(
                "Error in openSpecifiedApp(), the app container %s is NOT found.", appName);
        Assert.assertNotNull(message, appContainer);

        appContainer.click();
        ShellUtils.systemWait(WAIT);
        Assert.assertTrue(device.pressEnter());
    }

    public static void openQuickAccessButtonFromTopBar(UiDevice device, String id) {

        UiObject2 quickAccessBtn = getQuickAccessButtonFromTopBar(device, id);
        String message =
                "Error in openQuickAccessButtonFromTopBar(), the settings button from top bar is NOT found.";
        Assert.assertNotNull(message, quickAccessBtn);

        quickAccessBtn.click();
        ShellUtils.systemWait(WAIT);
        Assert.assertTrue(ACTION.doUiActionAndWait(device, new UiActionEnter(), LONG_WAIT));
    }

    private static void showLauncherTopBar(UiDevice device) {

        backToLauncher(device);
        int repeatTimes = 2;
        ACTION.doRepeatUiActionAndWait(device, new UiActionMoveUp(), repeatTimes);

        String topBarId = "com.bestv.ott:id/container";
        UiObject2 bar = device.findObject(By.res(topBarId));

        String message = "Error in showLauncherTopBar(), the top bar on launcher is NOT found.";
        Assert.assertNotNull(message, bar);

        message = "Error in showLauncherTopBar(), the top bar is NOT enabled.";
        Assert.assertTrue(message, bar.isEnabled());
    }

    private static UiObject2 getQuickAccessButtonFromTopBar(UiDevice device, String id) {

        showLauncherTopBar(device);
        return device.findObject(By.res(id));
    }
}
