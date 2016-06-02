package com.example.zhengjin.funsettingsuitest.testuiactions;

import android.support.test.uiautomator.UiDevice;

import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include the UI actions invoked by test tasks and test cases.
 */
public final class UiActionsManager {

    private static UiActionsManager instance = null;
    static final long WAIT = 1000;

    private UiActionsManager() {}

    public static synchronized UiActionsManager getInstance() {

        if (instance == null) {
            instance = new UiActionsManager();
        }

        return instance;
    }

    // default wait time is 1 second
    public boolean doUiActionAndWait(UiDevice device, UiAction action) {

        boolean ret = action.doUiAction(device);
        ShellUtils.systemWait(WAIT);
        return ret;
    }

    public boolean doUiActionAndWait(UiDevice device, UiAction action, long wait) {

        boolean ret = action.doUiAction(device);
        ShellUtils.systemWait(wait);
        return ret;
    }

    public boolean doRepeatUiActionAndWait(UiDevice device, UiAction action, int repeatTimes) {

        boolean ret = true;
        for (int i = 0; i < repeatTimes; ++i) {
            ret = (ret && action.doUiAction(device));
            ShellUtils.systemWait(WAIT);
        }
        return ret;
    }

    public boolean doRepeatUiActionAndWait(
            UiDevice device, UiAction action, int repeatTimes, long wait) {

        boolean ret = true;
        for (int i = 0; i < repeatTimes; ++i) {
            ret = (ret && action.doUiAction(device));
            ShellUtils.systemWait(wait);
        }
        return ret;
    }


}
