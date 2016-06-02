package com.example.zhengjin.funsettingsuitest.testuiactions;

import android.support.test.uiautomator.UiDevice;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Wrapped UI action for enter key.
 */
public final class UiActionEnter implements UiAction {

    @Override
    public boolean doUiAction(UiDevice device) {
        return device.pressEnter();
    }

}
