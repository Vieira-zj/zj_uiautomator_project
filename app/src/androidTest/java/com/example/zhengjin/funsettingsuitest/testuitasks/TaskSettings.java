package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;

import junit.framework.Assert;

/**
 * Created by zhengjin on 2016/6/6.
 *
 * Include the tasks for settings apk.
 */
public final class TaskSettings {

//    private final static String TAG = TaskSettings.class.getSimpleName();
    private static UiActionsManager ACTION = UiActionsManager.getInstance();

    public static BySelector getTitleOfSettingsPageSelector() {
        return By.res("tv.fun.settings:id/setting_title");
    }

    public static BySelector getDeviceNameSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_name");
    }

    public static BySelector getSleepTimeSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_sleep");
    }

    public static BySelector getSystemRecoverSettingItemKeySelector() {
        return By.res("tv.fun.settings:id/recovery_title");
    }

    public static BySelector getCancelBtnOfSystemRecoverDialogSelector() {
        return By.res("tv.fun.settings:id/recovery_btn_cancel");
    }

    public static void moveToSpecifiedSettingsItem(UiObject2 item) {

        final int maxMoveTimes = 15;
        int i = 0;
        while (!item.isFocused() && ((i++) < maxMoveTimes)) {
            ACTION.doDeviceActionAndWait(new DeviceActionMoveDown());
        }

        String message =
                "Error in moveToSpecifiedSettingsItem(), the settings item is NOT focused.";
        Assert.assertTrue(message, item.isFocused());
    }

    public static void scrollMoveToAndClickSettingsItem(UiDevice device, String itemText) {

        String message;
        final String scrollClass = "android.widget.ScrollView";
        UiScrollable scroll = new UiScrollable(new UiSelector().className(scrollClass));
        scroll.setAsVerticalList();

        try {
            scroll.scrollTextIntoView(itemText);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            message = String.format(
                    "Error in scrollMoveToAndClickSettingsItem(), the scrollable ui object %s is NOT found.", itemText);
            Assert.assertTrue(message, false);
        }

        UiObject2 settingsItem = device.findObject(By.text(itemText)).getParent();
        message = "Error in scrollMoveToAndClickSettingsItem(), the settings item is NOT found.";
        Assert.assertNotNull(message, settingsItem);

        ACTION.doClickActionAndWait(settingsItem);
    }

}
