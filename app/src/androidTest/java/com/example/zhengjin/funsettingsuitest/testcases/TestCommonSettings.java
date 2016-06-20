package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategorySettingsTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionBack;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskSettings;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.settingsPkg;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include test cases for common settings module.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestCommonSettings {

    private static UiActionsManager ACTION = UiActionsManager.getInstance();
    private UiDevice mDevice;

    @Before
    public void setUp() {

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        TaskLauncher.backToLauncher(mDevice);
        TaskLauncher.clickOnQuickAccessButtonFromTopBar(
                mDevice, TaskLauncher.getQuickAccessBtnSettingsSelector(), settingsPkg);
        ACTION.doDeviceActionAndWait(new DeviceActionMoveUp());  // request focus
    }

    @After
    public void clearUp() {
        int repeatTimes = 2;
        ACTION.doRepeatDeviceActionAndWait(new DeviceActionBack(), repeatTimes);
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test11TitleNameOfSettingsPage() {

        UiObject2 settingsTitle =
                mDevice.findObject(TaskSettings.getTitleOfSettingsPageSelector());
        Assert.assertNotNull(settingsTitle);

        String message = "Verify the title name of settings page.";
        final String expectedResult = "通用设置";
        Assert.assertEquals(message, expectedResult, settingsTitle.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test12SettingsDeviceName() {

        UiObject2 deviceNameContainer =
                mDevice.findObject(TaskSettings.getDeviceNameSettingItemContainerSelector());

        String message = "Verify the device name item and focused.";
        Assert.assertNotNull(message, deviceNameContainer);
        Assert.assertTrue(deviceNameContainer.isFocused());

        final String deviceNameKeyText = "设备名称";
        message = "Verify the device name key text.";
        UiObject2 deviceNameKey = deviceNameContainer.findObject(By.text(deviceNameKeyText));
        Assert.assertNotNull(message, deviceNameKey);

        final String deviceNameValueText = "风行电视";
        message = "Verify the device name value text.";
        UiObject2 deviceNameValue = deviceNameContainer.findObject(By.text(deviceNameValueText));
        Assert.assertNotNull(message, deviceNameValue);
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test13SettingsSubDeviceNames() {

        String[] subDeviceNames = {"风行电视", "客厅的电视", "卧室的电视", "书房的电视", "自定义"};
        final String message = "Verify the item %s in device name menu.";

        ACTION.doDeviceActionAndWait(new DeviceActionEnter());
        for (String deviceName : subDeviceNames) {
            UiObject2 subDeviceName = mDevice.findObject(By.text(deviceName));
            Assert.assertNotNull(String.format(message, deviceName), subDeviceName);
        }
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test14SelectDeviceName() {

        ACTION.doDeviceActionAndWait(new DeviceActionEnter());  // open device name menu
        // select a sub device name and back
        final String subDeviceName = "书房的电视";
        UiObject2 deviceName = mDevice.findObject(By.text(subDeviceName));
        ACTION.doClickActionAndWait(deviceName);

        final String containerId = "tv.fun.settings:id/setting_item_name";
        String message = "Verify select a pre-defined device name.";
        UiObject2 deviceNameContainer = mDevice.findObject(By.res(containerId));
        UiObject2 deviceNameValue = deviceNameContainer.findObject(By.text(subDeviceName));
        Assert.assertNotNull(message, deviceNameValue);
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test15SleepSettingSubValues() {

        final String DefaultSleepValue = "永不休眠";
        String message = "Verify the default value of sleep setting.";
        UiObject2 sleepContainer =
                mDevice.findObject(TaskSettings.getSleepTimeSettingItemContainerSelector());
        UiObject2 defaultText = sleepContainer.findObject(By.text(DefaultSleepValue));
        Assert.assertNotNull(message, defaultText);

        TaskSettings.moveToSpecifiedSettingsItem(sleepContainer);

        message = "Verify the sub values %s of sleep setting.";
        String[] subSleepValues = {"15分钟","30分钟","60分钟","90分钟","120分钟","永不休眠"};
        for (String value : subSleepValues) {
            ACTION.doDeviceActionAndWait(new DeviceActionMoveRight());
            UiObject2 container =
                    mDevice.findObject(TaskSettings.getSleepTimeSettingItemContainerSelector());
            UiObject2 subSleepText = container.findObject(By.text(value));
            Assert.assertNotNull(String.format(message, value), subSleepText);
        }
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test16SystemRecoverDialog() {

        final String recoverText = "恢复出厂设置";
        TaskSettings.scrollMoveToAndClickSettingsItem(mDevice, recoverText);  // open dialog

        String message = "Verify the recover dialog title.";
        UiObject2 title =
                mDevice.findObject(TaskSettings.getSystemRecoverSettingItemKeySelector());
        Assert.assertNotNull(message, title);

        String expectText = "您的设备将恢复出厂设置";
        message = "Verify the text of recover dialog title.";
        Assert.assertEquals(message, expectText, title.getText());

        message = "Verify the cancel button in recover dialog.";
        UiObject2 cancelBtn =
                mDevice.findObject(TaskSettings.getCancelBtnOfSystemRecoverDialogSelector());
        Assert.assertNotNull(message, cancelBtn);

        ACTION.doClickActionAndWait(cancelBtn);
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    public void test17DefaultWallpaper() {
        // TODO: 2016/6/7
    }
}

