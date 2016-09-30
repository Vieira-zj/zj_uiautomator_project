package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.InstrumentationRegistry;
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

    private static TaskSettings instance = null;
    private UiDevice device;
    private UiActionsManager action;

    private TaskSettings() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        action = UiActionsManager.getInstance();
    }

    public static synchronized TaskSettings getInstance() {
        if (instance == null) {
            instance = new TaskSettings();
        }
        return instance;
    }

    public void destroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public BySelector getTitleOfSettingsPageSelector() {
        return By.res("tv.fun.settings:id/setting_title");
    }

    public BySelector getDeviceNameSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_name");
    }

    public BySelector getDialogDeviceNameListSelector() {
        return By.res("tv.fun.settings:id/dialog_list_list");
    }

    public BySelector getSleepTimeSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_sleep");
    }

    public BySelector getLocationSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_locate");
    }

    public BySelector getInstallUnknownAppSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_unkown_source");
    }

    public BySelector getInstallUnknownAppSettingItemValueSelector() {
        return By.res("tv.fun.settings:id/setting_item_value");
    }

    public BySelector getSystemRecoverSettingItemKeySelector() {
        return By.res("tv.fun.settings:id/recovery_title");
    }

    public BySelector getTitleOfCommonDialogSelector() {
        return By.res("tv.fun.settings:id/dialog_title");
    }

    public BySelector getConfirmBtnOfCommonDialogSelector() {
        return By.res("tv.fun.settings:id/dialog_btn_confirm");
    }

    public BySelector getCancelBtnOfCommonDialogSelector() {
        return By.res("tv.fun.settings:id/dialog_btn_cancel");
    }

    public BySelector getCancelBtnOfSystemRecoverDialogSelector() {
        return By.res("tv.fun.settings:id/recovery_btn_cancel");
    }

    public BySelector getSettingItemKeySelector() {
        return By.res("tv.fun.settings:id/item_title");
    }

    public BySelector getSettingItemValueSelector() {
        return By.res("tv.fun.settings:id/item_value");
    }

    public BySelector getSettingSwitcherItemValueSelector() {
        return By.res("tv.fun.settings:id/setting_item_value");
    }

    public BySelector getProvinceListSelector() {
        return By.res("tv.fun.settings:id/ws_province");
    }

    public BySelector getCityListSelector() {
        return By.res("tv.fun.settings:id/ws_city");
    }

    public BySelector getMiddleItemInProvinceCityListSelector() {
        return By.res("tv.fun.settings:id/wheel_view_tx3");
    }

    public void moveToSpecifiedSettingsItem(BySelector selector) {
        UiObject2 item = device.findObject(selector);  // find the item from top
        if (item != null) {
            this.moveDownUntilSettingsItemFocused(item);
        } else {  // find the item from bottom
            this.moveToBottomOnCommonSettingsPage();
            item = device.findObject(selector);
            Assert.assertNotNull("The settings item is NOT found on common settings page.", item);
            this.moveUpUntilSettingsItemFocused(item);
        }
    }

    private void moveToBottomOnCommonSettingsPage() {
        action.doRepeatDeviceActionAndWait(new DeviceActionMoveDown(), 10);
    }

    private void moveUntilSettingsItemFocused(UiObject2 item, boolean flagDirectionDown) {
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
            action.doDeviceActionAndWait(moveAction);
        }

        String message =
                "Error in moveUntilSettingsItemFocused(), the settings item is NOT focused.";
        Assert.assertTrue(message, false);
    }

    private void moveDownUntilSettingsItemFocused(UiObject2 item) {
        this.moveUntilSettingsItemFocused(item, true);
    }

    private void moveUpUntilSettingsItemFocused(UiObject2 item) {
        this.moveUntilSettingsItemFocused(item, false);
    }

    public void scrollMoveToAndClickSettingsItem(String itemText) {
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
        action.doClickActionAndWait(settingsItem);
    }

    public void selectSpecifiedLocationProvince(String provinceText, boolean directionUp) {
        DeviceAction moveAction;
        if (directionUp) {
            moveAction = new DeviceActionMoveUp();
        } else {
            moveAction = new DeviceActionMoveDown();
        }

        UiObject2 provinceList = device.findObject(this.getProvinceListSelector());
        UiObject2 middleProvince =
                provinceList.findObject(this.getMiddleItemInProvinceCityListSelector());
        for (int i = 0, maxMoveTimes = 30; i < maxMoveTimes; i++) {
            if (provinceText.equals(middleProvince.getText())) {
                return;
            }
            action.doDeviceActionAndWait(moveAction);
            middleProvince = provinceList.findObject(this.getMiddleItemInProvinceCityListSelector());
        }

        Assert.assertTrue("The specified province is NOT found!", false);
    }

    public void selectSpecifiedLocationCity(String cityText, boolean directionUp) {
        DeviceAction moveAction;
        if (directionUp) {
            moveAction = new DeviceActionMoveUp();
        } else {
            moveAction = new DeviceActionMoveDown();
        }

        UiObject2 cityList = device.findObject(this.getCityListSelector());
        UiObject2 middleCity =
                cityList.findObject(this.getMiddleItemInProvinceCityListSelector());
        for (int i = 0, maxMoveTimes = 30; i < maxMoveTimes; i++) {
            if (cityText.equals(middleCity.getText())) {
                return;
            }
            action.doDeviceActionAndWait(moveAction);
            middleCity = cityList.findObject(this.getMiddleItemInProvinceCityListSelector());
        }

        Assert.assertTrue("The specified city is NOT found!", false);
    }

}
