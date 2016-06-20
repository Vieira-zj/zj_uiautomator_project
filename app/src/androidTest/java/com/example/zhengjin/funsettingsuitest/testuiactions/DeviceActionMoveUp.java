package com.example.zhengjin.funsettingsuitest.testuiactions;

import android.support.test.uiautomator.UiDevice;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Wrapped UI action for up key.
 */
public final class DeviceActionMoveUp implements DeviceAction {

    @Override
    public boolean doDeviceAction(UiDevice device) {
        return device.pressDPadUp();
    }
}
