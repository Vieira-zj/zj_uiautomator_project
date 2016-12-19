package com.example.zhengjin.funsettingsuitest.testuiactions;

import android.support.test.uiautomator.UiDevice;

/**
 * Created by zhengjin on 2016/12/19.
 *
 * Wrapped UI action for center key.
 */

public final class DeviceActionCenter implements DeviceAction {
    @Override
    public boolean doDeviceAction(UiDevice device) {
        return device.pressDPadCenter();
    }
}
