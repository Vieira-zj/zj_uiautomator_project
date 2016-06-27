package com.example.zhengjin.funsettingsuitest.testutils;

import android.os.SystemClock;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include the shared functions for test cases.
 */
public final class TestHelper {

    // TODO: 2016/6/24, use the wait(until) APIs replace current functions.

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

    public static boolean waitForAppOpenedV1(UiDevice device, String pkgName) {
        final long timeOut = LONG_WAIT;
        return waitForAppOpenedV1(device, pkgName, timeOut);
    }

    public static boolean waitForAppOpened(UiDevice device, String pkgName) {
        return device.wait(Until.hasObject(By.res(pkgName).depth(0)), LONG_WAIT);
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

}
