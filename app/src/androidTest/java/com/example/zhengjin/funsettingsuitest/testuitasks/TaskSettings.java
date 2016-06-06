package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;

/**
 * Created by zhengjin on 2016/6/6.
 *
 * Include the tasks on settings page
 */
public final class TaskSettings {

    private static UiActionsManager ACTION = UiActionsManager.getInstance();

    public static void moveToSpecifiedSettingsItem(UiDevice device, UiObject2 item) {

        int maxMoveTimes = 15;
        int i = 0;
        while (!item.isFocused() && i++ < maxMoveTimes) {
            ACTION.doUiActionAndWait(device, new UiActionMoveDown());
        }
    }

}
