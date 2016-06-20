package com.example.zhengjin.funsettingsuitest.testuiactions;

import android.support.test.uiautomator.UiDevice;

/**
 * Created by zhengjin on 2016/6/17.
 *
 * Wrapped UI action for menu key.
 */
public final class DeviceActionMenu implements DeviceAction {

    @Override
    public boolean doDeviceAction(UiDevice device) {
        return device.pressMenu();
    }

}
