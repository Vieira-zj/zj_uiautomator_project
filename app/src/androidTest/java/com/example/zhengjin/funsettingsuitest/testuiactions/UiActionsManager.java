package com.example.zhengjin.funsettingsuitest.testuiactions;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;

/**
 * Created by zhengjin on 2016/6/2.
 * <p>
 * Include the UI actions invoked by test tasks and test cases.
 */
public final class UiActionsManager {

    private static UiActionsManager instance = null;

    private UiDevice mDevice = null;

    private UiActionsManager() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    public static synchronized UiActionsManager getInstance() {
        if (instance == null) {
            instance = new UiActionsManager();
        }
        return instance;
    }

    // default wait time is 1 second
    public boolean doDeviceActionAndWait(DeviceAction action) {
        return doDeviceActionAndWait(action, SHORT_WAIT);
    }

    public boolean doDeviceActionAndWait(DeviceAction action, long wait) {
        boolean ret = action.doDeviceAction(mDevice);
        ShellUtils.systemWaitByMillis(wait);
        return ret;
    }

    public boolean doDeviceActionAndWaitForIdle(DeviceAction action) {
        long defaultWait = 0;
        return doDeviceActionAndWaitForIdle(action, defaultWait);
    }

    private boolean doDeviceActionAndWaitForIdle(DeviceAction action, long wait) {
        boolean ret = action.doDeviceAction(mDevice);

        if (wait > 0) {
            mDevice.waitForIdle(wait);
        } else {
            mDevice.waitForIdle();
        }
        return ret;
    }

    public void doRepeatDeviceActionAndWait(DeviceAction action, int repeatTimes) {
        doRepeatDeviceActionAndWait(action, repeatTimes, SHORT_WAIT);
    }

    public void doRepeatDeviceActionAndWait(DeviceAction action, int repeatTimes, long wait) {
        for (int i = 0; i < repeatTimes; ++i) {
            action.doDeviceAction(mDevice);
            ShellUtils.systemWaitByMillis(wait);
        }
    }

    public void doMultipleDeviceActionsAndWait(DeviceAction[] actions) {
        doMultipleDeviceActionsAndWait(actions, SHORT_WAIT);
    }

    public void doMultipleDeviceActionsAndWait(DeviceAction[] actions, long wait) {
        for (DeviceAction action : actions) {
            action.doDeviceAction(mDevice);
            ShellUtils.systemWaitByMillis(wait);
        }
    }

    public UiActionsManager doChainedDeviceActionAndWait(DeviceAction action) {
        return doChainedDeviceActionAndWait(action, SHORT_WAIT);
    }

    private UiActionsManager doChainedDeviceActionAndWait(DeviceAction action, long wait) {
        action.doDeviceAction(mDevice);
        ShellUtils.systemWaitByMillis(wait);
        return this;
    }

    public void doClickActionAndWait(UiObject2 uiObject) {
        doClickActionAndWait(uiObject, SHORT_WAIT);
    }

    public void doClickActionAndWait(UiObject2 uiObject, long wait) {
        uiObject.click();
        ShellUtils.systemWaitByMillis(wait);
    }

}
