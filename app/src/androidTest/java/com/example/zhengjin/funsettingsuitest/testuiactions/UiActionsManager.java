package com.example.zhengjin.funsettingsuitest.testuiactions;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include the UI actions invoked by test tasks and test cases.
 */
public final class UiActionsManager {

    private static UiActionsManager instance = null;
    private static UiDevice device = null;

    private UiActionsManager() {}

    public static synchronized UiActionsManager getInstance() {

        if (instance == null) {
            instance = new UiActionsManager();
        }
        if (device == null) {
            device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        }

        return instance;
    }

    public boolean doDeviceActionAndWait(DeviceAction action, long wait) {

        boolean ret = action.doDeviceAction(device);
        ShellUtils.systemWait(wait);
        return ret;
    }

    // default wait time is 1 second
    public boolean doDeviceActionAndWait(DeviceAction action) {
        return doDeviceActionAndWait(action, SHORT_WAIT);
    }

    public boolean doDeviceActionAndWaitForIdle(DeviceAction action, long wait) {

        boolean ret = action.doDeviceAction(device);

        if (wait > 0) {
            device.waitForIdle(wait);
        } else {
            device.waitForIdle();
        }
        return ret;
    }

    public boolean doDeviceActionAndWaitForIdle(DeviceAction action) {

        long defaultWait = 0;
        return doDeviceActionAndWaitForIdle(action, defaultWait);
    }

    public void doRepeatDeviceActionAndWait(DeviceAction action, int repeatTimes, long wait) {

        for (int i = 0; i < repeatTimes; ++i) {
            action.doDeviceAction(device);
            ShellUtils.systemWait(wait);
        }
    }

    public void doRepeatDeviceActionAndWait(DeviceAction action, int repeatTimes) {
        doRepeatDeviceActionAndWait(action, repeatTimes, SHORT_WAIT);
    }

    public UiActionsManager doMultipleDeviceActionAndWait(DeviceAction action, long wait) {

        action.doDeviceAction(device);
        ShellUtils.systemWait(wait);
        return this;
    }

    public UiActionsManager doMultipleDeviceActionAndWait(DeviceAction action) {
        return doMultipleDeviceActionAndWait(action, SHORT_WAIT);
    }

    public void doClickActionAndWait(UiObject2 uiObj) {

        uiObj.click();
        ShellUtils.systemWait(SHORT_WAIT);
    }

}
