package com.example.zhengjin.funsettingsuitest.testuiactions;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include the UI actions invoked by test tasks and test cases.
 */
public final class UiActionsManager {

    private static UiActionsManager instance = null;
    private static UiDevice mDevice = null;

    private UiActionsManager() {}

    public static synchronized UiActionsManager getInstance() {

        if (instance == null) {
            instance = new UiActionsManager();
        }
        if (mDevice == null) {
            mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        }

        return instance;
    }

    public boolean doUiActionAndWait(DeviceAction action, long wait) {

        boolean ret = action.doDeviceAction(mDevice);
        ShellUtils.systemWait(wait);
        return ret;
    }

    // default wait time is 1 second
    public boolean doUiActionAndWait(DeviceAction action) {
        return doUiActionAndWait(action, SHORT_WAIT);
    }

    public void doRepeatUiActionAndWait(DeviceAction action, int repeatTimes, long wait) {

        for (int i = 0; i < repeatTimes; ++i) {
            action.doDeviceAction(mDevice);
            ShellUtils.systemWait(wait);
        }
    }

    public void doRepeatUiActionAndWait(DeviceAction action, int repeatTimes) {
        doRepeatUiActionAndWait(action, repeatTimes, SHORT_WAIT);
    }

    public UiActionsManager doMultipleUiActionAndWait(DeviceAction action, long wait) {

        action.doDeviceAction(mDevice);
        ShellUtils.systemWait(wait);
        return this;
    }

    public UiActionsManager doMultipleUiActionAndWait(DeviceAction action) {
        return doMultipleUiActionAndWait(action, SHORT_WAIT);
    }

}
