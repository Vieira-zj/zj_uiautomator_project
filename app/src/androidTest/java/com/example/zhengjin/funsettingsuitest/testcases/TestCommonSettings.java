package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.SettingsTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionBack;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskSettings;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include test cases for common settings module.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestCommonSettings {

//    private static final String PKG_UNDER_TEST = "tv.fun.settings";
//    private static final String TAG;
    private static UiActionsManager ACTION;

    private UiDevice mDevice;

    static {
//        TAG = TaskLauncher.class.getSimpleName();
        ACTION = UiActionsManager.getInstance();
    }

    @Before
    public void setUp() {

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        TaskLauncher.backToLauncher(mDevice);
        String settingsQuickAccessBtnId = "com.bestv.ott:id/setting";
        TaskLauncher.openQuickAccessButtonFromTopBar(mDevice, settingsQuickAccessBtnId);
        ACTION.doUiActionAndWait(mDevice, new UiActionMoveUp());  // request focus
    }

    @After
    public void clearUp() {
        int repeatTimes = 2;
        ACTION.doRepeatUiActionAndWait(mDevice, new UiActionBack(), repeatTimes);
    }

    @Test
    @Category(SettingsTests.class)
    public void test1SettingsTitleName() {

        final String settingsTitleId = "tv.fun.settings:id/setting_title";
        UiObject2 settingsTitle = mDevice.findObject(By.res(settingsTitleId));
        Assert.assertNotNull(settingsTitle);

        String message = "Verify the settings page title name.";
        final String expectedResult = "通用设置";
        Assert.assertEquals(message, expectedResult, settingsTitle.getText());
    }

    @Test
    @Category(SettingsTests.class)
    public void test2SettingsDeviceName() {

        final String containerId = "tv.fun.settings:id/setting_item_name";
        UiObject2 deviceNameContainer = mDevice.findObject(By.res(containerId));

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
    @Category(SettingsTests.class)
    public void test3SettingsSubDeviceNames() {

        String[] subDeviceNames = {"风行电视", "客厅的电视", "卧室的电视", "书房的电视", "自定义"};
        final String message = "Verify the item %s in device name menu.";

        ACTION.doUiActionAndWait(mDevice, new UiActionEnter());
        for (String deviceName : subDeviceNames) {
            UiObject2 subDeviceName = mDevice.findObject(By.text(deviceName));
            Assert.assertNotNull(String.format(message, deviceName), subDeviceName);
        }
    }

    @Test
    @Category(SettingsTests.class)
    public void test4SelectDeviceName() {

        ACTION.doUiActionAndWait(mDevice, new UiActionEnter());  // open device name menu
        // select a sub device name and back
        final String subDeviceName = "书房的电视";
        UiObject2 deviceName = mDevice.findObject(By.text(subDeviceName));
        deviceName.getParent().click();
        ShellUtils.systemWait(WAIT);

        final String containerId = "tv.fun.settings:id/setting_item_name";
        String message = "Verify select a pre-defined device name.";
        UiObject2 deviceNameContainer = mDevice.findObject(By.res(containerId));
        UiObject2 deviceNameValue = deviceNameContainer.findObject(By.text(subDeviceName));
        Assert.assertNotNull(message, deviceNameValue);
    }

    @Test
    @Category(SettingsTests.class)
    public void test5SleepSettingSubValues() {

        final String containerId = "tv.fun.settings:id/setting_item_sleep";
        final String DefaultSleepValue = "永不休眠";
        String message = "Verify the default value of sleep setting.";
        UiObject2 sleepContainer = mDevice.findObject(By.res(containerId));
        UiObject2 defaultText = sleepContainer.findObject(By.text(DefaultSleepValue));
        Assert.assertNotNull(message, defaultText);

        TaskSettings.moveToSpecifiedSettingsItem(mDevice, sleepContainer);

        message = "Verify the sub values %s of sleep setting.";
        String[] subSleepValues = {"15分钟","30分钟","60分钟","90分钟","120分钟","永不休眠"};
        for (String value : subSleepValues) {
            ACTION.doUiActionAndWait(mDevice, new UiActionMoveRight());
            UiObject2 container = mDevice.findObject(By.res(containerId));
            UiObject2 subSleepText = container.findObject(By.text(value));
            Assert.assertNotNull(String.format(message, value), subSleepText);
        }
    }

    @Test
    @Category(SettingsTests.class)
    public void test6SystemRecoverDialog() {

        final String recoverText = "恢复出厂设置";
        TaskSettings.scrollMoveToAndClickSettingsItem(mDevice, recoverText);  // open dialog

        String titleId = "tv.fun.settings:id/recovery_title";
        String message = "Verify the recover dialog title.";
        UiObject2 title = mDevice.findObject(By.res(titleId));
        Assert.assertNotNull(message, title);

        String expectText = "您的设备将恢复出厂设置";
        message = "Verify the text of recover dialog title.";
        Assert.assertEquals(message, expectText, title.getText());

        String cancelBtnId = "tv.fun.settings:id/recovery_btn_cancel";
        message = "Verify the cancel button in recover dialog.";
        UiObject2 cancelBtn = mDevice.findObject(By.res(cancelBtnId));
        Assert.assertNotNull(message, cancelBtn);

        cancelBtn.click();
        ShellUtils.systemWait(WAIT);
    }

    @Test
    @Category(SettingsTests.class)
    public void test7DefaultWallpaper() {
        // TODO: 2016/6/7
    }
}

