package com.example.zhengjin.funsettingsuitest.testuiactions;

import android.support.test.uiautomator.UiDevice;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Wrapped UI action for home key.
 */
public final class UiActionHome implements UiAction {

    @Override
    public boolean doUiAction(UiDevice device) {
        return device.pressHome();
    }

}
