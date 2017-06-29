package com.example.zhengjin.funsettingsuitest.testuiobjects;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;

/**
 * Created by zhengjin on 2017/2/28.
 * <p>
 * Include the UI objects for settings apk.
 */
public final class UiObjectsSettings {

    private static UiObjectsSettings ourInstance;

    private UiObjectsSettings() {
    }

    public static UiObjectsSettings getInstance() {
        if (ourInstance == null) {
            synchronized (UiObjectsSettings.class) {
                if (ourInstance == null) {
                    ourInstance = new UiObjectsSettings();
                }
            }
        }
        return ourInstance;
    }

    public static synchronized void destroyInstance() {
        if (ourInstance != null) {
            ourInstance = null;
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

    public BySelector getDeviceNameEditorSelector() {
        return By.res("tv.fun.settings:id/device_edit");
    }

    public BySelector getDeviceNameConfirmButtonSelector() {
        return By.res("tv.fun.settings:id/device_name_btn_confirm");
    }

    public BySelector getAdvancedItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_advanced");
    }

    public BySelector getSettingItemsContainerSelector() {
        return By.res("tv.fun.settings:id/settings_container");
    }

    public BySelector getSleepTimeSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_sleep");
    }

    public BySelector getShutDownTimeSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_screen_shutdown_time");
    }

    public BySelector getFullScreenSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_input_source");
    }

    public BySelector getScreenProjectionSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_wifi_display");
    }

    public BySelector getSystemLanguageSettingItemContainerSelector() {
        return By.res("tv.fun.settings:id/setting_item_language");
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

    @SuppressWarnings("unused")
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

    public BySelector getSettingItemTipsSelector() {
        return By.res("tv.fun.settings:id/item_title_tips");
    }

    public BySelector getSettingItemValueSelector() {
        return By.res("tv.fun.settings:id/item_value");
    }

    public BySelector getSettingSwitcherItemValueSelector() {
        return By.res("tv.fun.settings:id/setting_item_value");
    }

    public BySelector getTitleOfSetShutDownTimeDialogSelector() {
        return By.res("tv.fun.settings:id/tp_title");
    }

    public BySelector getCheckboxOnShutDownTimeDialogSelector() {
        return By.res("tv.fun.settings:id/tp_checkbox");
    }

    public BySelector getHoursControlOnShutDownTimeDialogSelector() {
        return By.res("tv.fun.settings:id/tp_hour");
    }

    public BySelector getMinutesControlOnShutDownTimeDialogSelector() {
        return By.res("tv.fun.settings:id/tp_minute");
    }

}
