package com.example.zhengjin.funsettingsuitest.testutils;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.utils.StringUtils;

import junit.framework.Assert;

import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CLASS_TEXT_VIEW;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.TIME_OUT;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include the shared UI functions for test cases.
 */
public final class TestHelper {

    private static final String TAG = TestHelper.class.getSimpleName();
    private static final UiDevice device =
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    public static boolean waitForAppOpenedByCheckCurPackage(String pkgName) {
        return waitForAppOpenedByCheckCurPackage(pkgName, TIME_OUT);
    }

    public static boolean waitForAppOpenedByCheckCurPackage(String pkgName, long timeOut) {
        return waitForAppOpenedByCheckCurPackage(pkgName, timeOut, SHORT_WAIT);
    }

    public static boolean waitForAppOpenedByCheckCurPackage(
            String pkgName, long timeOut, long interval) {
        device.waitForIdle();
        long start = SystemClock.uptimeMillis();
        while ((SystemClock.uptimeMillis() - start) < timeOut) {
            if (pkgName.equalsIgnoreCase(device.getCurrentPackageName())) {
                return true;
            }
            ShellUtils.systemWaitByMillis(interval);
        }

        return false;
    }

    public static boolean waitForAppOpenedByUntil(String pkgName) {
        return waitForAppOpenedByUntil(pkgName, TIME_OUT);
    }

    public static boolean waitForAppOpenedByUntil(String pkgName, long wait) {
        device.waitForIdle();
        return device.wait(Until.hasObject(By.pkg(pkgName).depth(0)), wait);
    }

    public static boolean waitForAppOpenedByShellCmd(String pkgName) {
        return waitForAppOpenedByShellCmd(pkgName, (int)TIME_OUT / 1000);
    }

    public static boolean waitForAppOpenedByShellCmd(String topActivity, int waitBySeconds) {
        String cmd = "dumpsys activity | grep mFocusedActivity";
        ShellUtils.CommandResult cr;

        for (int i = 0; i < waitBySeconds; i++) {
            cr = ShellUtils.execCommand(cmd, false, true);
            if (cr.mResult == 0) {
                if (!StringUtils.isEmpty(cr.mSuccessMsg) && cr.mSuccessMsg.contains(topActivity)) {
                    Log.d(TAG, "The top activity -> " + cr.mSuccessMsg);
                    return true;
                }
            }
            ShellUtils.systemWaitByMillis(SHORT_WAIT);
        }

        return false;
    }

    public static boolean waitForUiObjectEnabledByProperty(UiObject2 uiObj) {
        return waitForUiObjectEnabledByProperty(uiObj, TIME_OUT);
    }

    private static boolean waitForUiObjectEnabledByProperty(UiObject2 uiObj, long timeOut) {
        return waitForUiObjectEnabledByProperty(uiObj, timeOut, SHORT_WAIT);
    }

    private static boolean waitForUiObjectEnabledByProperty(
            UiObject2 uiObj, long timeOut, long interval) {
        device.waitForIdle();
        long start = SystemClock.uptimeMillis();
        while ((SystemClock.uptimeMillis() - start) < timeOut) {
            if (uiObj.isEnabled()) {
                return true;
            }
            ShellUtils.systemWaitByMillis(interval);
        }

        return false;
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

    public static boolean waitForLoadingComplete() {
        // if loading is showing, wait for loading disappear
        UiObject2 loading = device.findObject(By.res("com.bestv.ott:id/progressBar"));
        if (loading == null) {
            return false;
        }

        for (int i = 0, waitTimes = 10; i < waitTimes; i++) {
            SystemClock.sleep(SHORT_WAIT);
            loading = device.findObject(By.res("com.bestv.ott:id/progressBar"));
            if (loading == null) {
                return true;
            }
        }
        return false;
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
