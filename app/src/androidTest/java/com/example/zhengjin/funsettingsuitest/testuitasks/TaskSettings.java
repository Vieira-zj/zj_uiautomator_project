package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
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

    public static BySelector getDialogDeviceNameListSelector() {
        return By.res("tv.fun.settings:id/dialog_list_list");
    }

    public static BySelector getSleepTimeSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_sleep");
    }

    public static BySelector getLocationSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_locate");
    }

    public static BySelector getInstallUnknownAppSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_unkown_source");
    }

    public static BySelector getInstallUnknownAppSettingItemValueSelector() {
        return By.res("tv.fun.settings:id/setting_item_value");
    }

    public static BySelector getSystemRecoverSettingItemKeySelector() {
        return By.res("tv.fun.settings:id/recovery_title");
    }

    public static BySelector getCancelBtnOfSystemRecoverDialogSelector() {
        return By.res("tv.fun.settings:id/recovery_btn_cancel");
    }

    public static BySelector getSettingItemKeySelector() {
        return By.res("tv.fun.settings:id/item_title");
    }

    public static BySelector getSettingItemValueSelector() {
        return By.res("tv.fun.settings:id/item_value");
    }

    public static BySelector getProvinceListSelector() {
        return By.res("tv.fun.settings:id/ws_province");
    }

    public static BySelector getCityListSelector() {
        return By.res("tv.fun.settings:id/ws_city");
    }

    public static BySelector getMiddleItemFromProvinceCityList() {
        return By.res("tv.fun.settings:id/wheel_view_tx3");
    }

    public static void moveToSpecifiedSettingsItem(UiDevice device, BySelector selector) {

        UiObject2 item = device.findObject(selector);  // find the item from top
        if (item == null) {  // find the item from bottom
            moveToBottomOnCommonSettingsPage();
            item = device.findObject(selector);
            if (item == null) {
                Assert.assertTrue("The settings item is NOT found in common settings page.", false);
            }
            moveUpUntilSettingsItemFocused(item);
        } else {
            moveDownUntilSettingsItemFocused(item);
        }
    }

    private static void moveToBottomOnCommonSettingsPage() {
        ACTION.doRepeatDeviceActionAndWait(new DeviceActionMoveDown(), 10);
    }

    private static void moveUntilSettingsItemFocused(UiObject2 item, boolean flagDirectionDown) {

        final int maxMoveTimes = 15;
        int i = 0;
        while (!item.isFocused() && ((i++) < maxMoveTimes)) {
            if (flagDirectionDown) {
                ACTION.doDeviceActionAndWait(new DeviceActionMoveDown());
            } else {
                ACTION.doDeviceActionAndWait(new DeviceActionMoveUp());
            }
        }

        if (i == maxMoveTimes) {
            String message =
                    "Error in moveUntilSettingsItemFocused(), the settings item is NOT focused.";
            Assert.assertTrue(message, false);
        }
    }

    private static void moveDownUntilSettingsItemFocused(UiObject2 item) {
        moveUntilSettingsItemFocused(item, true);
    }

    private static void moveUpUntilSettingsItemFocused(UiObject2 item) {
        moveUntilSettingsItemFocused(item, false);
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

    public static void selectSpecifiedLocationProvince(
            UiDevice device, String provinceText, boolean directionUp) {

        UiObject2 provinceList = device.findObject(getProvinceListSelector());
        UiObject2 middleProvince =
                provinceList.findObject(getMiddleItemFromProvinceCityList());

        int maxMoveTimes = 30;
        int i = 0;
        while (!provinceText.equals(middleProvince.getText()) && ((i++) < maxMoveTimes)) {
            if (directionUp) {
                ACTION.doDeviceActionAndWait(new DeviceActionMoveUp());
            } else {
                ACTION.doDeviceActionAndWait(new DeviceActionMoveDown());
            }
            middleProvince = provinceList.findObject(getMiddleItemFromProvinceCityList());
        }

        if (i == maxMoveTimes) {
            Assert.assertTrue("The specified province is NOT found!", false);
        }
    }

    public static void selectSpecifiedLocationCity(
            UiDevice device, String cityText, boolean directionUp) {

        UiObject2 cityList = device.findObject(getCityListSelector());
        UiObject2 middleCity =
                cityList.findObject(getMiddleItemFromProvinceCityList());

        int maxMoveTimes = 30;
        int i = 0;
        while (!cityText.equals(middleCity.getText()) && ((i++) < maxMoveTimes)) {
            if (directionUp) {
                ACTION.doDeviceActionAndWait(new DeviceActionMoveUp());
            } else {
                ACTION.doDeviceActionAndWait(new DeviceActionMoveDown());
            }
            middleCity = cityList.findObject(getMiddleItemFromProvinceCityList());
        }

        if (i == maxMoveTimes) {
            Assert.assertTrue("The specified city is NOT found!", false);
        }
    }

}
