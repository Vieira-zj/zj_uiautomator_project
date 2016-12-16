package com.example.zhengjin.funsettingsuitest.testutils;

import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.utils.StringUtils;

import junit.framework.Assert;

import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CLASS_TEXT_VIEW;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.TIME_OUT;

/**
 * Created by zhengjin on 2016/6/2.
 * <p>
 * Include the shared UI functions for test cases.
 */
public final class TestHelper {

    private static final String TAG = TestHelper.class.getSimpleName();
    private static final UiDevice device =
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    public enum SaveEnvType {
        CAPTURE, DUMP_LOG, CAP_AND_DUMP
    }

    public static void assertTrueAndSaveEnvIfFailed(
            String message, boolean result, SaveEnvType type) {
        if (!result) {
            takeCaptureAndDumpLogcat(type);
        }
        Assert.assertTrue(message, result);
    }

    private static void takeCaptureAndDumpLogcat(SaveEnvType type) {
        switch (type) {
            case CAPTURE:
                ShellUtils.takeScreenCapture(device);
                break;
            case DUMP_LOG:
                ShellUtils.dumpLogcatLog();
                break;
            case CAP_AND_DUMP:
                ShellUtils.dumpLogcatLog();
                ShellUtils.takeScreenCapture(device);
                break;
            default:
                Log.w(TAG, "takeCaptureAndDumpLogcat, invalid SaveEnvType!");
        }
    }

    public static boolean waitForAppOpenedByCheckCurPackage(String pkgName) {
        return waitForAppOpenedByCheckCurPackage(pkgName, TIME_OUT);
    }

    public static boolean waitForAppOpenedByCheckCurPackage(String pkgName, long timeOut) {
        device.waitForIdle();
        long start = SystemClock.uptimeMillis();
        while ((SystemClock.uptimeMillis() - start) < timeOut) {
            if (pkgName.equalsIgnoreCase(device.getCurrentPackageName())) {
                return true;
            }
            ShellUtils.systemWaitByMillis(SHORT_WAIT);
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

    public static boolean waitForActivityOpenedByShellCmd(String pkgName, String activityName) {
        return waitForActivityOpenedByShellCmd(pkgName, activityName, TIME_OUT);
    }

    public static boolean waitForActivityOpenedByShellCmd(
            String pkgName, String activityName, long waitByMillis) {
        return waitForViewOnTopByShellCmd(
                String.format("%s/%s", pkgName, activityName), waitByMillis);
    }

    public static boolean waitForAppOpenedByShellCmd(String pkgName) {
        return waitForViewOnTopByShellCmd(pkgName, TIME_OUT);
    }

    private static boolean waitForViewOnTopByShellCmd(String viewName, long waitByMillis) {
        ShellUtils.CommandResult cr;

        for (int i = 0; i < (int) waitByMillis / 1000L; i++) {
            cr = ShellUtils.getTopFocusedActivity();
            if (cr.mResult == 0) {
                if (!StringUtils.isEmpty(cr.mSuccessMsg) && cr.mSuccessMsg.contains(viewName)) {
                    Log.d(TAG, "GetViewOnTop, top activity: " + cr.mSuccessMsg);
                    return true;
                }
            }
            ShellUtils.systemWaitByMillis(SHORT_WAIT);
        }

        return false;
    }

    public static boolean waitForUiObjectEnabledByCheckIsEnabled(BySelector selector) {
        return waitForUiObjectEnabledByCheckIsEnabled(selector, TIME_OUT);
    }

    public static boolean waitForUiObjectEnabledByCheckIsEnabled(
            BySelector selector, long timeOut) {
        device.waitForIdle();
        long start = SystemClock.uptimeMillis();

        while ((SystemClock.uptimeMillis() - start) < timeOut) {
            UiObject2 uiObject = findUiObjectIgnoreRootNullException(selector);
            if (uiObject != null && uiObject.isEnabled()) {
                return true;
            }

            ShellUtils.systemWaitByMillis(SHORT_WAIT);
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
        UiObject2 loading =
                findUiObjectIgnoreRootNullException(TaskLauncher.getLoadingCircleSelector());
        if (loading == null) {
            return false;
        }

        for (int i = 0, waitTimes = 15; i < waitTimes; i++) {
            SystemClock.sleep(SHORT_WAIT);
            loading = findUiObjectIgnoreRootNullException(TaskLauncher.getLoadingCircleSelector());
            if (loading == null) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private static UiObject2 findUiObjectIgnoreRootNullException(BySelector selector) {
        UiObject2 uiObject = null;
        try {
            uiObject = device.findObject(selector);
        } catch (NullPointerException e) {
            // NullPointerException from root element, no handler
        }

        return uiObject;
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
