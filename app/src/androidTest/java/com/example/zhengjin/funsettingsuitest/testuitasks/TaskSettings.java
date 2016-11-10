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
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;

import junit.framework.Assert;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CLASS_SCROLL_VIEW;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CLASS_TEXT_VIEW;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

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

    public BySelector getAdvancedItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_advanced");
    }

    public BySelector getSleepTimeSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_sleep");
    }

    public BySelector getScreenSaverSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_screen_saver");
    }

    public BySelector getWallpaperSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_wallpaper");
    }

    public BySelector getInputMethodSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_inputmethod");
    }

    public BySelector getLocationSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_locate");
    }

    public BySelector getInstallUnknownAppSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_unkown_source");
    }

    public BySelector getSystemRecoverSettingItemSelector() {
        return By.res("tv.fun.settings:id/setting_item_recovery");
    }

    public BySelector getSystemRecoverDialogTitleSelector() {
        return By.res("tv.fun.settings:id/recovery_title");
    }

    public BySelector getSaveInfoOfRecoverDialogSelector() {
        return By.res("tv.fun.settings:id/recovery_cbx_save_network_info");
    }

    public BySelector getConfirmBtnOfSystemRecoverDialogSelector() {
        return By.res("tv.fun.settings:id/recovery_btn_confirm");
    }

    public BySelector getCancelBtnOfSystemRecoverDialogSelector() {
        return By.res("tv.fun.settings:id/recovery_btn_cancel");
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

    public BySelector getSettingItemKeySelector() {
        return By.res("tv.fun.settings:id/item_title");
    }

    public BySelector getSettingItemValueSelector() {
        return By.res("tv.fun.settings:id/item_value");
    }

    public BySelector getSettingSwitcherItemValueSelector() {
        return By.res("tv.fun.settings:id/setting_item_value");
    }

    public UiObject2 getTextViewOfSwitcher(UiObject2 container) {
        UiObject2 switcher =
                container.findObject(this.getSettingSwitcherItemValueSelector());
        UiObject2 text = switcher.findObject(By.clazz(CLASS_TEXT_VIEW));
        return text;
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

    private void moveDownUntilSettingsItemFocused(UiObject2 item) {
        this.moveUntilSettingsItemFocused(item, true);
    }

    private void moveUpUntilSettingsItemFocused(UiObject2 item) {
        this.moveUntilSettingsItemFocused(item, false);
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

        Assert.assertTrue(
                "Error in moveUntilSettingsItemFocused(), the settings item is NOT focused.",
                false);
    }

    public void scrollMoveToSpecificSettingsItem(String itemText) {
        UiScrollable scroll =
                new UiScrollable(new UiSelector().className(CLASS_SCROLL_VIEW));
        scroll.setAsVerticalList();
        try {
            scroll.scrollTextIntoView(itemText);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            Assert.assertTrue(String.format("The ui object %s is NOT found.", itemText), false);
        }
    }

    public void selectSpecifiedSubWallpaper(String title) {
        UiObject2 wallpaper = device.findObject(By.text(title)).getParent();
        for (int i = 0, wallpaperSize = 4; i < wallpaperSize; i++) {
            if (wallpaper.isSelected()) {
                action.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
                return;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveRight());
        }

        Assert.assertTrue("Failed to select the specified wallpaper.", false);
    }

}
