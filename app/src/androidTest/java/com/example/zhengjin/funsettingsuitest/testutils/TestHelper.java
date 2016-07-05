package com.example.zhengjin.funsettingsuitest.testutils;

import android.os.Environment;
import android.os.SystemClock;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import junit.framework.Assert;

import java.io.File;
import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CAPTURES_PATH;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.TIME_OUT;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include the shared functions for test cases.
 */
public final class TestHelper {

    @Deprecated
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

    @Deprecated
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
        return device.wait(Until.hasObject(By.pkg(pkgName).depth(0)), TIME_OUT);
    }

    @Deprecated
    public static boolean waitForUiObjectEnabledV1(UiObject2 uiObj, long timeOut, long interval) {

        boolean flag_UiObj_enabled = false;
        long start = SystemClock.uptimeMillis();

        while (!uiObj.isEnabled() && ((SystemClock.uptimeMillis() - start) < timeOut)) {
            flag_UiObj_enabled = uiObj.isEnabled();
            ShellUtils.systemWait(interval);
        }

        return flag_UiObj_enabled;
    }

    @Deprecated
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
        return uiObj.wait(Until.enabled(true), TIME_OUT);
    }

    public static boolean waitForUiObjectClickable(UiObject2 uiObj) {
        return uiObj.wait(Until.clickable(true), TIME_OUT);
    }

    public static boolean waitForUiObjectVisible(UiDevice device, BySelector selector) {

        device.waitForIdle();
        return device.wait(Until.hasObject(selector),TIME_OUT);
    }

    public static UiObject2 waitForUiObjectVisibleAndReturn(UiDevice device, BySelector selector) {

        device.waitForIdle();
        return device.wait(Until.findObject(selector),TIME_OUT);
    }

    public static List<UiObject2> waitForMultipleUiObjectsVisibleAndReturn(
            UiDevice device, BySelector selector) {

        device.waitForIdle();
        return device.wait(Until.findObjects(selector),TIME_OUT);
    }

    public static void verifyEachTextViewHasTextInUiContainer(UiObject2 container) {

        List<UiObject2> listObj = container.findObjects(By.clazz("android.widget.TextView"));
        for (UiObject2 uiObj : listObj) {
            Assert.assertFalse("Verify the text in text view is not empty.",
                    "".equals(uiObj.getText()));
        }
    }

    public static void verifyEachTextViewHasTextInUiCollection(List<UiObject2> list) {

        for (UiObject2 uiObj : list) {
            Assert.assertFalse("Verify the text in text view is not empty.",
                    "".equals(uiObj.getText()));
        }
    }

    public static void doScreenCapture(UiDevice device) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File testDirPath = new File(CAPTURES_PATH);
            if (!testDirPath.exists()) {
                 if (!testDirPath.mkdirs()) {
                     Assert.assertTrue(String.format(
                             "Error, make directory(%s) for captures failed.", CAPTURES_PATH), false);
                 }
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
