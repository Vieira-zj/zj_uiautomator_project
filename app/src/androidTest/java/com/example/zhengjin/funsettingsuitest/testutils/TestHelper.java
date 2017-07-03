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

import com.example.zhengjin.funsettingsuitest.testuiobjects.UiObjectsLauncher;
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
    private static final UiDevice DEVICE =
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    private static final UiObjectsLauncher FUN_OBJECTS = UiObjectsLauncher.getInstance();

    private TestHelper() {
    }

    public static void assertTrueAndTakeCaptureIfFailed(String message, boolean result) {
        if (!result) {
            ShellUtils.takeScreenCapture(DEVICE);
        }
        Assert.assertTrue(message, result);
    }

    @SuppressWarnings("unused")
    public static boolean waitForAppOpenedByCheckCurPackage(String pkgName) {
        return waitForAppOpenedByCheckCurPackage(pkgName, TIME_OUT);
    }

    private static boolean waitForAppOpenedByCheckCurPackage(String pkgName, long timeOut) {
        DEVICE.waitForIdle();
        long start = SystemClock.uptimeMillis();
        while ((SystemClock.uptimeMillis() - start) < timeOut) {
            if (pkgName.equalsIgnoreCase(DEVICE.getCurrentPackageName())) {
                return true;
            }
            ShellUtils.systemWaitByMillis(SHORT_WAIT);
        }

        return false;
    }

    public static boolean waitForAppOpenedByUntil(String pkgName) {
        return waitForAppOpenedByUntil(pkgName, TIME_OUT);
    }

    private static boolean waitForAppOpenedByUntil(String pkgName, long wait) {
        DEVICE.waitForIdle();
        return DEVICE.wait(Until.hasObject(By.pkg(pkgName).depth(0)), wait);
    }

    public static boolean waitForActivityOpenedByShellCmd(String pkgName, String activityName) {
        return waitForActivityOpenedByShellCmd(pkgName, activityName, TIME_OUT);
    }

    public static boolean waitForActivityOpenedByShellCmd(
            String pkgName, String activityName, long waitByMillis) {
        return waitForViewOnTopByShellCmd(
                String.format("%s/%s", pkgName, activityName), waitByMillis);
    }

    private static boolean waitForViewOnTopByShellCmd(String viewName, long waitByMillis) {
        ShellUtils.CommandResult cr;

        for (int i = 0; i < (int) waitByMillis / 1000L; i++) {
            cr = ShellUtils.getTopFocusedActivity();
            if (cr.mReturnCode == 0 && cr.mSuccessMsg.contains(viewName)) {
                Log.d(TAG, "GetViewOnTop, top activity: " + cr.mSuccessMsg);
                return true;
            }
            if (cr.mReturnCode == 1 && StringUtils.isEmpty(cr.mSuccessMsg)) {
                Log.d(TAG, "GetViewOnTop, top view name is empty, it's a bug for platform 938.");
                return true;
            }
            ShellUtils.systemWaitByMillis(SHORT_WAIT);
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static boolean waitForUiObjectEnabledByCheckIsEnabled(BySelector selector) {
        return waitForUiObjectEnabledByCheckIsEnabled(selector, TIME_OUT);
    }

    public static boolean waitForUiObjectEnabledByCheckIsEnabled(
            BySelector selector, long timeOut) {
        DEVICE.waitForIdle();
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
        DEVICE.waitForIdle();
        return DEVICE.wait(Until.hasObject(selector), TIME_OUT);
    }

    public static UiObject2 waitForUiObjectExistAndReturn(BySelector selector) {
        DEVICE.waitForIdle();
        return DEVICE.wait(Until.findObject(selector), TIME_OUT);
    }

    public static List<UiObject2> waitForUiObjectsExistAndReturn(BySelector selector) {
        DEVICE.waitForIdle();
        return DEVICE.wait(Until.findObjects(selector), TIME_OUT);
    }

    @SuppressWarnings("unused")
    public static boolean waitForTextGone(String uiText) {
        DEVICE.waitForIdle();
        return DEVICE.wait(Until.gone(By.text(uiText)), TIME_OUT);
    }

    public static boolean waitForTextGone(UiObject2 parent, String uiText) {
        DEVICE.waitForIdle();
        return parent.wait(Until.gone(By.text(uiText)), TIME_OUT);
    }

    @SuppressWarnings("unused")
    public static boolean waitForTextVisible(String uiText) {
        DEVICE.waitForIdle();
        return DEVICE.wait(Until.hasObject(By.text(uiText)), TIME_OUT);
    }

    public static boolean waitForTextVisible(UiObject2 parent, String uiText) {
        DEVICE.waitForIdle();
        return parent.wait(Until.hasObject(By.text(uiText)), TIME_OUT);
    }

    public static boolean verifyIsUiObjectFocused(UiObject2 uiObject) {
        return uiObject.isFocused() || uiObject.isSelected();
    }

    public static boolean waitForLoadingComplete() {
        // if loading is showing, wait for loading disappear
        UiObject2 loading =
                findUiObjectIgnoreRootNullException(FUN_OBJECTS.getLoadingCircleSelector());
        if (loading == null) {
            return false;
        }

        for (int i = 0, waitTimes = 15; i < waitTimes; i++) {
            SystemClock.sleep(SHORT_WAIT);
            loading = findUiObjectIgnoreRootNullException(FUN_OBJECTS.getLoadingCircleSelector());
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
            uiObject = DEVICE.findObject(selector);
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
