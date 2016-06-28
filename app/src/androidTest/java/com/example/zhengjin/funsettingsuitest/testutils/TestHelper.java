package com.example.zhengjin.funsettingsuitest.testutils;

import android.os.Environment;
import android.os.SystemClock;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import junit.framework.Assert;

import java.io.File;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CAPTURES_PATH;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include the shared functions for test cases.
 */
public final class TestHelper {

    public static boolean waitForAppOpenedV1(
            UiDevice device, String pkgName, long timeOut, long interval) {

        boolean flag_app_opened = false;
        long start = SystemClock.uptimeMillis();

        while (!flag_app_opened && ((SystemClock.uptimeMillis() - start) < timeOut)) {
            flag_app_opened = device.getCurrentPackageName().equals(pkgName);
            ShellUtils.systemWait(interval);
        }

        return flag_app_opened;
    }

    public static boolean waitForAppOpenedV1(UiDevice device, String pkgName, long timeOut) {
        final long interval = SHORT_WAIT;
        return waitForAppOpenedV1(device, pkgName, timeOut, interval);
    }

    @Deprecated
    public static boolean waitForAppOpenedV1(UiDevice device, String pkgName) {
        final long timeOut = LONG_WAIT;
        return waitForAppOpenedV1(device, pkgName, timeOut);
    }

    public static boolean waitForAppOpened(UiDevice device, String pkgName) {

        device.waitForIdle();
//        device.hasObject(By.pkg(pkgName).depth(0));
        return device.wait(Until.hasObject(By.pkg(pkgName).depth(0)), LONG_WAIT);
    }

    public static boolean waitForUiObjectEnabledV1(UiObject2 uiObj, long timeOut, long interval) {

        boolean flag_UiObj_enabled = false;
        long start = SystemClock.uptimeMillis();

        while (!uiObj.isEnabled() && ((SystemClock.uptimeMillis() - start) < timeOut)) {
            flag_UiObj_enabled = uiObj.isEnabled();
            ShellUtils.systemWait(interval);
        }

        return flag_UiObj_enabled;
    }

    public static boolean waitForUiObjectEnabledV1(UiObject2 uiObj, long timeOut) {
        final long interval = SHORT_WAIT;
        return waitForUiObjectEnabledV1(uiObj, timeOut, interval);
    }

    @Deprecated
    public static boolean waitForUiObjectEnabledV1(UiObject2 uiObj) {
        final long timeOut = LONG_WAIT;
        return waitForUiObjectEnabledV1(uiObj, timeOut);
    }

    public static boolean waitForUiObjectEnabled(UiObject2 uiObj) {
        return uiObj.wait(Until.enabled(true), LONG_WAIT);
    }

    public static boolean waitForUiObjectClickable(UiObject2 uiObj) {
        return uiObj.wait(Until.clickable(true), LONG_WAIT);
    }

    public static void doScreenCapture(UiDevice device) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File testDirPath = new File(CAPTURES_PATH);
            if (!testDirPath.exists()) {
                testDirPath.mkdirs();
            }
        } else {
            Assert.assertTrue("Error, the sdcard is NOT mount.", false);
        }

        final String suffix = ".png";
        String filePath = String.format(
                "%s/capture_%s%s", CAPTURES_PATH, ShellUtils.getCurrentTime(), suffix);
        Assert.assertTrue("Take screenshot.", device.takeScreenshot(new File(filePath)));
    }

}
