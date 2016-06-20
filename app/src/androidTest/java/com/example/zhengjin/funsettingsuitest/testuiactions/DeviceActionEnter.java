package com.example.zhengjin.funsettingsuitest.testuiactions;

import android.support.test.uiautomator.UiDevice;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Wrapped UI action for enter key.
 */
public final class DeviceActionEnter implements DeviceAction {

    @Override
    public boolean doDeviceAction(UiDevice device) {
        return device.pressEnter();
    }

}
