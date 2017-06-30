package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceAction;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionBack;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionCenter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveLeft;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuiobjects.UiObjectsSettings;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;
import com.example.zhengjin.funsettingsuitest.utils.StringUtils;

import junit.framework.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CLASS_SCROLL_VIEW;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CLASS_TEXT_VIEW;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/6/6.
 * <p>
 * Include general tasks for settings apk.
 */
public final class TaskSettings {

    private static TaskSettings instance;

    private final UiDevice device;
    private final UiActionsManager action;
    private final UiObjectsSettings funUiObjects;

    public final String TITLE_SET_SHUTDOWN_TIME_DIALOG = "设置定时关机";

    private TaskSettings() {
        device = TestConstants.GetUiDeviceInstance();
        action = UiActionsManager.getInstance();
        funUiObjects = UiObjectsSettings.getInstance();
    }

    public static synchronized TaskSettings getInstance() {
        if (instance == null) {
            synchronized (TaskSettings.class) {
                if (instance == null) {
                    instance = new TaskSettings();
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

    public UiObject2 getTextViewOfSwitcher(UiObject2 container) {
        TestHelper.waitForUiObjectEnabled(container);
        UiObject2 switcher =
                container.findObject(funUiObjects.getSettingSwitcherItemValueSelector());
        return switcher.findObject(By.clazz(CLASS_TEXT_VIEW));
    }

    public UiObject2 getDeviceNameValueByText(String nameText) {
        UiObject2 deviceNameContainer =
                device.findObject(funUiObjects.getDeviceNameSettingItemContainerSelector());
        return deviceNameContainer.findObject(By.text(nameText));
    }

    public void moveToSpecifiedSettingsItem(BySelector selector) {
        UiObject2 item = device.findObject(selector);  // find the item from top
        if (item != null) {
            this.moveDownUntilSettingsItemFocused(item);
        } else {
            // find the item from bottom
            this.moveToBottomOnCommonSettingsPage();
            item = device.findObject(selector);
            Assert.assertNotNull("moveToSpecifiedSettingsItem, " +
                    "settings item is NOT found on common settings.", item);
            this.moveUpUntilSettingsItemFocused(item);
        }
        ShellUtils.systemWaitByMillis(SHORT_WAIT);
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

        Assert.assertTrue("moveUntilSettingsItemFocused, settings item is NOT focused.", false);
    }

    public void scrollMoveToSpecificSettingsItem(String itemText) {
        UiScrollable scroll =
                new UiScrollable(new UiSelector().className(CLASS_SCROLL_VIEW));
        scroll.setAsVerticalList();
        try {
            scroll.scrollTextIntoView(itemText);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            Assert.assertTrue(String.format("scrollMoveToSpecificSettingsItem, " +
                    "setting item %s is NOT found.", itemText), false);
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

        Assert.assertTrue("selectSpecifiedSubWallpaper, failed select the wallpaper.", false);
    }

    public void openSelfDefineDeviceNamePage() {
        action.doDeviceActionAndWait(new DeviceActionEnter());
        UiObject2 itemSelfDefine = device.findObject(By.text("自定义"));
        action.doClickActionAndWait(itemSelfDefine, WAIT);
    }

    public boolean isInputMethodEnabled() {
        ShellUtils.CommandResult cr = ShellUtils.execCommand("ime list -s", false, true);
        return cr.mReturnCode == 0 && !StringUtils.isEmpty(cr.mSuccessMsg);
    }

    public void enableInputMethod() {
        if (!isInputMethodEnabled()) {
            ShellUtils.execCommand("ime enable com.baidu.input_baidutv/.ImeService", false, false);
        }
    }

    private void disableInputMethod() {
        if (isInputMethodEnabled()) {
            ShellUtils.execCommand(
                    "ime disable com.baidu.input_baidutv/.ImeService", false, false);
        }
    }

    public boolean inputEnTextInDeviceName(String text) {
        this.disableInputMethod();
        ShellUtils.systemWaitByMillis(TestConstants.SHORT_WAIT);
        ShellUtils.CommandResult cr =
                ShellUtils.execCommand(String.format("input text %s", text), false, false);
        return cr.mReturnCode == 0;
    }

    public void clearTextOfEditorView(int charCount) {
        for (int i = 0; i < charCount; i++) {
//            ShellUtils.execCommand("input keyevent KEYCODE_DEL", false, false);
            device.pressDelete();
            ShellUtils.systemWaitByMillis(200L);
        }
    }

    public void openAdvancedSettingsPage() {
        this.moveToSpecifiedSettingsItem(funUiObjects.getAdvancedItemContainerSelector());
        action.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        TestHelper.waitForUiObjectEnabled(
                device.findObject(funUiObjects.getTitleOfSettingsPageSelector()));
    }

    public void openSetShutDownTimeDialog() {
        this.moveToSpecifiedSettingsItem(
                funUiObjects.getShutDownTimeSettingItemContainerSelector());
        action.doDeviceActionAndWait(new DeviceActionCenter());
        TestHelper.waitForTextVisible(TITLE_SET_SHUTDOWN_TIME_DIALOG);
    }

    public void openSystemLanguageSelectionDialog() {
        this.moveToSpecifiedSettingsItem(
                funUiObjects.getSystemLanguageSettingItemContainerSelector());
        action.doDeviceActionAndWait(new DeviceActionCenter());
        TestHelper.waitForUiObjectExist(funUiObjects.getTitleOfCommonDialogSelector());
    }

    private void clickOnConfirmButtonOfCommonDialog() {
        this.focusOnButtonOfCommonDialog(CommonDialogButtons.CONFIRM_BUTTON);
        action.doDeviceActionAndWait(new DeviceActionEnter());
    }

    public void clickOnCancelButtonOfCommonDialog() {
        this.focusOnButtonOfCommonDialog(CommonDialogButtons.CANCEL_BUTTON);
        action.doDeviceActionAndWait(new DeviceActionEnter());
    }

    private void focusOnButtonOfCommonDialog(CommonDialogButtons dialogButton) {
        UiObject2 uiButton;
        DeviceAction uiAction;

        if (dialogButton == CommonDialogButtons.CONFIRM_BUTTON) {
            uiButton = device.findObject(funUiObjects.getConfirmBtnOfCommonDialogSelector());
            uiAction = new DeviceActionMoveUp();
        } else {
            uiButton = device.findObject(funUiObjects.getCancelBtnOfCommonDialogSelector());
            uiAction = new DeviceActionMoveDown();
        }

        if (uiButton.isFocused() || uiButton.isSelected()) {
            return;
        }

        for (int i = 0, max = 3; i < max; i++) {
            action.doDeviceActionAndWait(uiAction);
            if (uiButton.isFocused() || uiButton.isSelected()) {
                return;
            }
        }

        Assert.fail("focusOnConfirmButtonOfCommonDialog, fail!");
    }

    private enum CommonDialogButtons {
        CONFIRM_BUTTON, CANCEL_BUTTON
    }

    public void restoreSystemLanguageAsChineseByApp() {
        ShellUtils.startSpecifiedActivity(
                TestConstants.SETTINGS_PKG_NAME, ".general.AdvancedSettingsActivity");
        ShellUtils.systemWaitByMillis(TestConstants.WAIT);
        TestHelper.waitForUiObjectEnabled(
                device.findObject(funUiObjects.getTitleOfSettingsPageSelector()));

        this.openSystemLanguageSelectionDialog();
        this.clickOnConfirmButtonOfCommonDialog();
    }

    @SuppressWarnings("unused")
    public void restoreSystemLanguageAsChineseByFramework() {
        // cannot update UI from instrument process
        try {
            Class<?> cls = Class.forName("com.android.internal.app.LocalePicker");
            Method updateLocale = cls.getDeclaredMethod("updateLocale", Locale.class);
            updateLocale.invoke(cls.newInstance(), Locale.CHINESE);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            Assert.fail("setSystemLanguageAsEnglishByFramework, failed!");
        }
    }

    public String getValueOfTimeControlOnShutdownDialog(UiObject2 container) {
        List<UiObject2> timeItems = container.findObjects(By.clazz(TestConstants.CLASS_TEXT_VIEW));

        final int timeControlValuesCount = 3;
        Assert.assertEquals(
                "getValueOfTimeControlOnShutDownTimeDialog, verify 3 values show in time control.",
                timeControlValuesCount, timeItems.size());

        String[] arrTimeText = new String[timeControlValuesCount];
        for (int i = 0; i < timeControlValuesCount; i++) {
            arrTimeText[i] = timeItems.get(i).getText();
        }

        Arrays.sort(arrTimeText);
        return arrTimeText[1];
    }

    public void checkSetShutDownTimeCheckbox() {
        UiObject2 checkbox = device.findObject(
                funUiObjects.getCheckboxOnShutDownTimeDialogSelector());
        if (checkbox.isChecked()) {
            return;
        }

        if (!checkbox.isFocused()) {
            action.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 2);
        }
        action.doDeviceActionAndWait(new DeviceActionCenter());
        Assert.assertTrue("checkSetShutDownTimeCheckbox, failed to set check",
                checkbox.isChecked());
    }

    public void unCheckSetShutDownTimeCheckbox() {
        UiObject2 checkbox = device.findObject(
                funUiObjects.getCheckboxOnShutDownTimeDialogSelector());
        if (!checkbox.isChecked()) {
            return;
        }

        if (!checkbox.isFocused()) {
            action.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 2);
        }
        action.doDeviceActionAndWait(new DeviceActionCenter());
        Assert.assertFalse("unCheckSetShutDownTimeCheckbox, failed to set unchecked!",
                checkbox.isChecked());
    }

    public int getHoursOfCurrentTime() {
        // "hh" for 12, "HH" for 24
        SimpleDateFormat formatter = new SimpleDateFormat("HH", Locale.getDefault());
        Date curTime = new Date(System.currentTimeMillis());
        return Integer.parseInt(formatter.format(curTime));
    }

    public int getMinutesOfCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("mm", Locale.getDefault());
        Date curTime = new Date(System.currentTimeMillis());
        return Integer.parseInt(formatter.format(curTime));
    }

    public int getIntValueFromTimeControlText(String timeVal) {
        if (timeVal.startsWith("0")) {
            return Integer.parseInt(timeVal.substring(1));
        }
        return Integer.parseInt(timeVal);
    }

    public int addHoursValue(int hourVal, int addVal) {
        final int maxVal = 23;
        int tmpVal = hourVal + addVal;
        return tmpVal > maxVal ? maxVal : tmpVal;
    }

    public int subHoursValue(int hourVal, int subVal) {
        final int minVal = 0;
        int tmpVal = hourVal - subVal;
        return tmpVal < minVal ? minVal : tmpVal;
    }

    public int addMinutesValue(int minVal, int addVal) {
        final int maxVal = 59;
        int tmpVal = minVal + addVal;
        return tmpVal > maxVal ? maxVal : tmpVal;
    }

    public int subMinutesValue(int minutesVal, int subVal) {
        final int minVal = 0;
        int tmpVal = minutesVal - subVal;
        return tmpVal < minVal ? minVal : tmpVal;
    }

    public void focusOnHoursOfShutDownTimeControl() {
        this.checkSetShutDownTimeCheckbox();

        UiObject2 hoursContainer;
        for (int i = 0, max = 2; i < max; i++) {
            hoursContainer =
                    device.findObject(funUiObjects.getHoursControlOnShutDownTimeDialogSelector());
            if (hoursContainer.isSelected()) {
                return;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveRight());
        }
        Assert.assertTrue("focusOnHoursOfShutDownTimeControl, focus failed!", false);
    }

    public void focusOnMinutesOfShutDownTimeControl() {
        this.checkSetShutDownTimeCheckbox();

        UiObject2 minContainer;
        for (int i = 0, max = 3; i < max; i++) {
            minContainer = device.findObject(
                    funUiObjects.getMinutesControlOnShutDownTimeDialogSelector());
            if (minContainer.isSelected()) {
                return;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveRight());
        }
        Assert.assertTrue("focusOnMinutesOfShutDownTimeControl, focus failed!", false);
    }

    public String setAndGetHoursOfShutDownTime(DeviceAction deviceAction, int moveTimes) {
        UiObject2 hoursContainer =
                device.findObject(funUiObjects.getHoursControlOnShutDownTimeDialogSelector());
        Assert.assertTrue("setAndGetHoursOfShutDownTime, NOT focus on hours of time control!",
                hoursContainer.isSelected());

        action.doRepeatDeviceActionAndWait(deviceAction, moveTimes);
        return this.getValueOfTimeControlOnShutdownDialog(hoursContainer);
    }

    public String setAndGetMinutesOfShutDownTime(DeviceAction deviceAction, int moveTimes) {
        UiObject2 minContainer =
                device.findObject(funUiObjects.getMinutesControlOnShutDownTimeDialogSelector());
        Assert.assertTrue(
                "setAndGetMinutesOfShutDownTime, NOT focus on minutes of time control!",
                minContainer.isSelected());

        action.doRepeatDeviceActionAndWait(deviceAction, moveTimes);
        return this.getValueOfTimeControlOnShutdownDialog(minContainer);
    }

    public void unSetShutDownTvTime() {
        this.openSetShutDownTimeDialog();
        this.unCheckSetShutDownTimeCheckbox();
        action.doDeviceActionAndWait(new DeviceActionBack());
    }

}
