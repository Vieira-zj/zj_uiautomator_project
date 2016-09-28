package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceAction;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;

import junit.framework.Assert;

/**
 * Created by zhengjin on 2016/6/6.
 *
 * Include the UI selectors and tasks for settings apk.
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

    public static BySelector getTitleOfCommonDialogSelector() {
        return By.res("tv.fun.settings:id/dialog_title");
    }

    public static BySelector getConfirmBtnOfCommonDialogSelector() {
        return By.res("tv.fun.settings:id/dialog_btn_confirm");
    }

    public static BySelector getCancelBtnOfCommonDialogSelector() {
        return By.res("tv.fun.settings:id/dialog_btn_cancel");
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
        if (item != null) {
            moveDownUntilSettingsItemFocused(item);
        } else {  // find the item from bottom
            moveToBottomOnCommonSettingsPage();
            item = device.findObject(selector);
            Assert.assertNotNull("The settings item is NOT found on common settings page.", item);
            moveUpUntilSettingsItemFocused(item);
        }
    }

    private static void moveToBottomOnCommonSettingsPage() {
        ACTION.doRepeatDeviceActionAndWait(new DeviceActionMoveDown(), 10);
    }

    private static void moveUntilSettingsItemFocused(UiObject2 item, boolean flagDirectionDown) {
        DeviceAction moveAction;
        if (flagDirectionDown) {
            moveAction = new DeviceActionMoveDown();
        } else {
            moveAction = new DeviceActionMoveUp();
        }

        for (int i = 0, maxMoveTimes = 15; i < maxMoveTimes; i++) {
            if (item.isFocused()) {
                return;
            }
            ACTION.doDeviceActionAndWait(moveAction);
        }

        String message =
                "Error in moveUntilSettingsItemFocused(), the settings item is NOT focused.";
        Assert.assertTrue(message, false);
    }

    private static void moveDownUntilSettingsItemFocused(UiObject2 item) {
        moveUntilSettingsItemFocused(item, true);
    }

    private static void moveUpUntilSettingsItemFocused(UiObject2 item) {
        moveUntilSettingsItemFocused(item, false);
    }

    public static void scrollMoveToAndClickSettingsItem(UiDevice device, String itemText) {
        String message;

        UiScrollable scroll =
                new UiScrollable(new UiSelector().className("android.widget.ScrollView"));
        scroll.setAsVerticalList();
        try {
            scroll.scrollTextIntoView(itemText);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            message = String.format(
                    "Error in scrollMoveToAndClickSettingsItem(), the ui object %s is NOT found.",
                    itemText);
            Assert.assertTrue(message, false);
        }

        UiObject2 settingsItem = device.findObject(By.text(itemText)).getParent();
        message = "Error in scrollMoveToAndClickSettingsItem(), the settings item is NOT found.";
        Assert.assertNotNull(message, settingsItem);
        ACTION.doClickActionAndWait(settingsItem);
    }

    public static void selectSpecifiedLocationProvince(
            UiDevice device, String provinceText, boolean directionUp) {
        DeviceAction moveAction;
        if (directionUp) {
            moveAction = new DeviceActionMoveUp();
        } else {
            moveAction = new DeviceActionMoveDown();
        }

        UiObject2 provinceList = device.findObject(getProvinceListSelector());
        UiObject2 middleProvince =
                provinceList.findObject(getMiddleItemFromProvinceCityList());
        for (int i = 0, maxMoveTimes = 30; i < maxMoveTimes; i++) {
            if (provinceText.equals(middleProvince.getText())) {
                return;
            }
            ACTION.doDeviceActionAndWait(moveAction);
            middleProvince = provinceList.findObject(getMiddleItemFromProvinceCityList());
        }

        Assert.assertTrue("The specified province is NOT found!", false);
    }

    public static void selectSpecifiedLocationCity(
            UiDevice device, String cityText, boolean directionUp) {
        DeviceAction moveAction;
        if (directionUp) {
            moveAction = new DeviceActionMoveUp();
        } else {
            moveAction = new DeviceActionMoveDown();
        }

        UiObject2 cityList = device.findObject(getCityListSelector());
        UiObject2 middleCity =
                cityList.findObject(getMiddleItemFromProvinceCityList());
        for (int i = 0, maxMoveTimes = 30; i < maxMoveTimes; i++) {
            if (cityText.equals(middleCity.getText())) {
                return;
            }
            ACTION.doDeviceActionAndWait(moveAction);
            middleCity = cityList.findObject(getMiddleItemFromProvinceCityList());
        }

        Assert.assertTrue("The specified city is NOT found!", false);
    }

}
