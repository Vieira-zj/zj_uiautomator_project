package com.example.zhengjin.funsettingsuitest.testutils;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import com.example.zhengjin.funsettingsuitest.utils.StringUtils;

import junit.framework.Assert;

import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CLASS_TEXT_VIEW;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.TIME_OUT;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include the shared UI functions for test cases.
 */
public final class TestHelper {

    private static UiDevice device =
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    public static boolean waitForAppOpenedByCheckCurPackage(
            String pkgName, long timeOut, long interval) {
        boolean flag_app_opened = false;

        device.waitForIdle();
        long start = SystemClock.uptimeMillis();
        while (!flag_app_opened && ((SystemClock.uptimeMillis() - start) < timeOut)) {
            flag_app_opened = pkgName.equals(device.getCurrentPackageName());
            ShellUtils.systemWaitByMillis(interval);
        }

        return flag_app_opened;
    }

    public static boolean waitForAppOpenedByCheckCurPackage(String pkgName, long timeOut) {
        final long interval = SHORT_WAIT;
        return waitForAppOpenedByCheckCurPackage(pkgName, timeOut, interval);
    }

    // Prefer to use waitForAppOpenedByUntil()
    public static boolean waitForAppOpenedByCheckCurPackage(String pkgName) {
        final long timeOut = LONG_WAIT;
        return waitForAppOpenedByCheckCurPackage(pkgName, timeOut);
    }

    public static boolean waitForAppOpenedByUntil(String pkgName) {
        device.waitForIdle();
        return device.wait(Until.hasObject(By.pkg(pkgName).depth(0)), TIME_OUT);
    }

    private static boolean waitForUiObjectEnabledByCheckProperty(
            UiObject2 uiObj, long timeOut, long interval) {
        boolean flag_UiObj_enabled = false;
        long start = SystemClock.uptimeMillis();

        while (!uiObj.isEnabled() && ((SystemClock.uptimeMillis() - start) < timeOut)) {
            flag_UiObj_enabled = uiObj.isEnabled();
            ShellUtils.systemWaitByMillis(interval);
        }

        return flag_UiObj_enabled;
    }

    private static boolean waitForUiObjectEnabledByCheckProperty(UiObject2 uiObj, long timeOut) {
        final long interval = SHORT_WAIT;
        return waitForUiObjectEnabledByCheckProperty(uiObj, timeOut, interval);
    }

    // Prefer to use waitForUiObjectEnabled()
    private static boolean waitForUiObjectEnabledByCheckProperty(UiObject2 uiObj) {
        final long timeOut = LONG_WAIT;
        return waitForUiObjectEnabledByCheckProperty(uiObj, timeOut);
    }

    public static boolean waitForUiObjectEnabled(UiObject2 uiObj) {
        return uiObj.wait(Until.enabled(true), TIME_OUT);
    }

    public static boolean waitForUiObjectClickable(UiObject2 uiObj) {
        return uiObj.wait(Until.clickable(true), TIME_OUT);
    }

    public static boolean waitForUiObjectExist(BySelector selector) {
        device.waitForIdle();
        return device.wait(Until.hasObject(selector), TIME_OUT);
    }

    public static UiObject2 waitForUiObjectExistAndReturn(BySelector selector) {
        device.waitForIdle();
        return device.wait(Until.findObject(selector), TIME_OUT);
    }

    public static List<UiObject2> waitForUiObjectsExistAndReturn(BySelector selector) {
        device.waitForIdle();
        return device.wait(Until.findObjects(selector), TIME_OUT);
    }

    public static void verifyEachTextViewHasTextInUiContainer(UiObject2 container) {
        List<UiObject2> listTexts = container.findObjects(By.clazz(CLASS_TEXT_VIEW));
        verifyEachTextViewHasTextInUiCollection(listTexts);
    }

    public static void verifyEachTextViewHasTextInUiCollection(List<UiObject2> list) {
        for (UiObject2 uiObj : list) {
            Assert.assertFalse("Verify the text in text view is not empty.",
                    StringUtils.isEmpty(uiObj.getText()));
        }
    }

}
