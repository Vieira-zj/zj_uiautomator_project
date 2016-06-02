package com.example.zhengjin.funsettingsuitest.testuiactions;

import android.support.test.uiautomator.UiDevice;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Wrapped UI action for up key.
 */
public final class UiActionMoveUp implements UiAction {

    @Override
    public boolean doUiAction(UiDevice device) {
        return device.pressDPadUp();
    }
}
