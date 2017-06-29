package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testrunner.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuiobjects.UiObjectsImageAndSound;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CLASS_TEXT_SWITCHER;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CLASS_TEXT_VIEW;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_IMAGE_AND_SOUND_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/12/14.
 * <p>
 * Include the UI selectors and tasks for image and sound settings.
 */

public final class TaskImageAndSound {

    private static TaskImageAndSound instance;

    private UiDevice device;
    private UiActionsManager action;
    private UiObjectsImageAndSound funUiObjects;

    public final String IMAGE_AND_SOUND_TEXT = "图像与声音";

    private TaskImageAndSound() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        action = UiActionsManager.getInstance();
        funUiObjects = UiObjectsImageAndSound.getInstance();
    }

    public static TaskImageAndSound getInstance() {
        if (instance == null) {
            synchronized (TaskImageAndSound.class) {
                if (instance == null) {
                    instance = new TaskImageAndSound();
                }
            }
        }
        return instance;
    }

    public static synchronized void destroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public UiObject2 getSwitcherValueOfColorTmpSetting(UiObject2 container) {
        UiObject2 switcher = container.findObject(By.clazz(CLASS_TEXT_SWITCHER));
        return switcher.findObject(By.clazz(CLASS_TEXT_VIEW));
    }

    public void openImageAndSoundSettingsPage() {
        if (RunnerProfile.isPlatform938) {
            this.openImageAndSoundSettingsPageByShellCmd();
        } else {  // platform 638
            TaskLauncher.openSpecifiedCardFromSettingsTab(IMAGE_AND_SOUND_TEXT);
        }

        Assert.assertTrue("openImageAndSoundSettingsPage, open failed!",
                TestHelper.waitForActivityOpenedByShellCmd(
                        SETTINGS_PKG_NAME, SETTINGS_IMAGE_AND_SOUND_ACT));
    }

    private void openImageAndSoundSettingsPageByShellCmd() {
        ShellUtils.startSpecifiedActivity(SETTINGS_PKG_NAME, SETTINGS_IMAGE_AND_SOUND_ACT);
        ShellUtils.systemWaitByMillis(WAIT);
    }

    public void focusOnSpecifiedImageAndSoundSettingsItem(String itemText) {
        for (int i = 0, maxMoves = 5; i < maxMoves; i++) {
            UiObject2 item = device.findObject(By.text(itemText));
            if (item.getParent().isFocused()) {
                return;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveDown());
        }

        Assert.assertTrue("focusOnSpecifiedImageAndSoundSettingsItem, error open setting item "
                + itemText, false);
    }

    public void focusOnSpecifiedImageParamsSettingsItem(String itemText) {
        UiObject2 item = device.findObject(By.text(itemText));
        for (int i = 0, maxMoves = 5; i < maxMoves; i++) {
            UiObject2 seekBar = item.getParent().findObject(
                    funUiObjects.getValueSeekBarOfSettingItemOnImageParamsSelector());
            if (seekBar != null && seekBar.isEnabled()) {
                return;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveDown());
        }

        Assert.assertTrue("focusOnSpecifiedImageParamsSettingsItem, error open setting item "
                + itemText, false);
    }

    public void focusOnResetToDefaultImageParamsSettingsItem() {
        action.doRepeatDeviceActionAndWait(new DeviceActionMoveDown(), 6);
        UiObject2 resetItem = device.findObject(
                funUiObjects.getResetToDefaultSettingItemOfImageParamsSelector());
        Assert.assertTrue("focusOnResetToDefaultImageParamsSettingsItem, failed to focus!",
                resetItem.isFocused());
    }

}
