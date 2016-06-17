package com.example.zhengjin.funsettingsuitest.testutils;

import android.os.SystemClock;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include the shared functions for test cases.
 */
public final class TestHelper {

    public static boolean waitForAppOpened(
            UiDevice device, String pkgName, long timeOut, long interval) {

        boolean flag_app_opened = false;
        long start = SystemClock.uptimeMillis();

        while (!flag_app_opened && ((SystemClock.uptimeMillis() - start) < timeOut)) {
            flag_app_opened = device.getCurrentPackageName().equals(pkgName);
            ShellUtils.systemWait(interval);
        }

        return flag_app_opened;
    }

    public static boolean waitForAppOpened(UiDevice device, String pkgName, long timeOut) {
        final long interval = SHORT_WAIT;
        return waitForAppOpened(device, pkgName, timeOut, interval);
    }

    public static boolean waitForAppOpened(UiDevice device, String pkgName) {
        final long timeOut = LONG_WAIT;
        return waitForAppOpened(device, pkgName, timeOut);
    }

    public static boolean waitForUiObjectEnabled(UiObject2 UiObj, long timeOut, long interval) {

        boolean flag_UiObj_enabled = false;
        long start = SystemClock.uptimeMillis();

        while (!UiObj.isEnabled() && ((SystemClock.uptimeMillis() - start) < timeOut)) {
            flag_UiObj_enabled = UiObj.isEnabled();
            ShellUtils.systemWait(interval);
        }

        return flag_UiObj_enabled;
    }

    public static boolean waitForUiObjectEnabled(UiObject2 UiObj, long timeOut) {
        final long interval = SHORT_WAIT;
        return waitForUiObjectEnabled(UiObj, timeOut, interval);
    }

    public static boolean waitForUiObjectEnabled(UiObject2 UiObj) {
        final long timeOut = LONG_WAIT;
        return waitForUiObjectEnabled(UiObj, timeOut);
    }

}

