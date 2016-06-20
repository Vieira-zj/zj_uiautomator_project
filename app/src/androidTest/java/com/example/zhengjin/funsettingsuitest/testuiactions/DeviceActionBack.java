package com.example.zhengjin.funsettingsuitest.testuiactions;

import android.support.test.uiautomator.UiDevice;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Wrapped UI action for back key.
 */
public final class DeviceActionBack implements DeviceAction {

    @Override
    public boolean doDeviceAction(UiDevice device) {
        return device.pressBack();
    }

}
