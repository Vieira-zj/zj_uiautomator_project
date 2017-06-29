package com.example.zhengjin.funsettingsuitest.testuiobjects;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;

/**
 * Created by zhengjin on 2017/2/28.
 * <p>
 * Include UI objects for image and sound settings.
 */
public final class UiObjectsImageAndSound {

    private static UiObjectsImageAndSound ourInstance;

    private UiObjectsImageAndSound() {
    }

    public static UiObjectsImageAndSound getInstance() {
        if (ourInstance == null) {
            synchronized (UiObjectsImageAndSound.class) {
                if (ourInstance == null) {
                    ourInstance = new UiObjectsImageAndSound();
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

    public BySelector getTitleOfImageAndSoundSettingsSelector() {
        return By.res("tv.fun.settings:id/setting_title");
    }

    public BySelector getDefaultPlayClaritySettingItemSelector() {
        return By.res("tv.fun.settings:id/setting_item_play_clarity");
    }

    public BySelector getImageParamsSettingItemSelector() {
        return By.res("tv.fun.settings:id/setting_item_pic_params");
    }

    public BySelector getPressKeySoundSettingItemSelector() {
        return By.res("tv.fun.settings:id/setting_item_sound_effect_enable");
    }

    public BySelector getAudioAroundSettingItemSelector() {
        return By.res("tv.fun.settings:id/setting_item_around_audio");
    }

    public BySelector getSavePowerModeSettingItemSelector() {
        return By.res("tv.fun.settings:id/setting_item_dynamic_backlight");
    }

    public BySelector getCECRemoteControlSettingItemSelector() {
        return By.res("tv.fun.settings:id/setting_item_cec_control");
    }

    public BySelector getImageAndSoundSettingItemTitleSelector() {
        return By.res("tv.fun.settings:id/item_title");
    }

    public BySelector getImageAndSoundSettingItemValueSelector() {
        return By.res("tv.fun.settings:id/item_value");
    }

    public BySelector getColorTmpSettingItemOfImageParamsSelector() {
        return By.res("tv.fun.settings:id/custom_scene_temp");
    }

    public BySelector getTitleOfColorTmpSettingSelector() {
        return By.res("tv.fun.settings:id/custom_scene_temp_title");
    }

    public BySelector getBackLightSettingItemOfImageParamsSelector() {
        return By.res("tv.fun.settings:id/custom_scene_backlight");
    }

    public BySelector getBrightnessSettingItemOfImageParamsSelector() {
        return By.res("tv.fun.settings:id/custom_scene_brightness");
    }

    public BySelector getContrastSettingItemOfImageParamsSelector() {
        return By.res("tv.fun.settings:id/custom_scene_contrast");
    }

    public BySelector getSaturationSettingItemOfImageParamsSelector() {
        return By.res("tv.fun.settings:id/custom_scene_saturation");
    }

    public BySelector getTitleOfImageSettingsOnImageParamsSelector() {
        return By.res("tv.fun.settings:id/scene_title");
    }

    public BySelector getValueOfImageSettingsOnImageParamsSelector() {
        return By.res("tv.fun.settings:id/scene_value");
    }

    public BySelector getValueSeekBarOfSettingItemOnImageParamsSelector() {
        return By.res("tv.fun.settings:id/scene_seekbar");
    }

    public BySelector getResetToDefaultSettingItemOfImageParamsSelector() {
        return By.res("tv.fun.settings:id/set_to_default");
    }

    public BySelector getTitleOfResetToDefaultSettingItemSelector() {
        return By.res("tv.fun.settings:id/set_to_default_title");
    }

}
