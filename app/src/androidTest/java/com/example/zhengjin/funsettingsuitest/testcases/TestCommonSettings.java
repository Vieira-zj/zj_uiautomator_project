package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import com.example.zhengjin.funsettingsuitest.testcategory.CategorySettingsTests;
import com.example.zhengjin.funsettingsuitest.testsuites.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionBack;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveLeft;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskSettings;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskWeather;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.TIME_OUT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WEATHER_PKG_NAME;

/**
 * Created by zhengjin on 2016/6/2.
 * <p>
 * Include test cases for common settings module.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestCommonSettings {

    private UiDevice mDevice;
    private UiActionsManager mAction;
    private TaskSettings mTask;
    private TaskWeather mWeatherTask;

    @BeforeClass
    public static void classSetUp() {
        ShellUtils.stopAndClearPackage(SETTINGS_PKG_NAME);
    }

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mTask = TaskSettings.getInstance();

        TaskLauncher.backToLauncher();
        TaskLauncher.clickOnButtonFromTopQuickAccessBar(
                TaskLauncher.getQuickAccessBtnSettingsSelector());
        Assert.assertTrue("Open Settings app.",
                TestHelper.waitForAppOpenedByUntil(SETTINGS_PKG_NAME));

        mAction.doDeviceActionAndWait(new DeviceActionMoveUp());  // request focus
    }

    @After
    public void clearUp() {
        if (RunnerProfile.isTakeScreenshot) {
            ShellUtils.takeScreenCapture(mDevice);
            ShellUtils.systemWaitByMillis(SHORT_WAIT);
        }

//        mAction.doRepeatDeviceActionAndWait(new DeviceActionBack(), 2);
        ShellUtils.stopProcess(SETTINGS_PKG_NAME);
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test11TitleNameOfSettingsPage() {
        UiObject2 settingsTitle =
                mDevice.findObject(mTask.getTitleOfSettingsPageSelector());
        Assert.assertNotNull(settingsTitle);
        String message = "Verify the title name of common settings page.";
        Assert.assertEquals(message, "通用设置", settingsTitle.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test12DeviceNameDefaultValue() {
        String message;

        UiObject2 deviceNameContainer =
                mDevice.findObject(mTask.getDeviceNameSettingItemContainerSelector());
        message = "Verify the device name item is NOT null.";
        Assert.assertNotNull(message, deviceNameContainer);
        message = "Verify the device name item is focused as default.";
        Assert.assertTrue(message, deviceNameContainer.isFocused());

        UiObject2 deviceNameKey = deviceNameContainer.findObject(By.text("设备名称"));
        message = "Verify the device name key text.";
        Assert.assertNotNull(message, deviceNameKey);

        UiObject2 deviceNameValue = deviceNameContainer.findObject(By.text("风行电视"));
        message = "Verify the device name value text.";
        Assert.assertNotNull(message, deviceNameValue);
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test13DeviceNameSubValues() {
        mAction.doDeviceActionAndWait(new DeviceActionEnter());
        mDevice.wait(Until.hasObject(mTask.getDialogDeviceNameListSelector()), WAIT);

        String message = "Verify the item %s in device name menu.";
        String[] subDeviceNames = {"风行电视", "客厅的电视", "卧室的电视", "书房的电视", "自定义"};
        for (String deviceName : subDeviceNames) {
            UiObject2 subDeviceName = mDevice.findObject(By.text(deviceName));
            Assert.assertNotNull(String.format(message, deviceName), subDeviceName);
        }
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test14SelectDeviceName() {
        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // open device name menu
        // select a sub device name and back
        String subDeviceName = "书房的电视";
        UiObject2 deviceName = mDevice.findObject(By.text(subDeviceName));
        TestHelper.waitForUiObjectClickable(deviceName);
        mAction.doClickActionAndWait(deviceName);

        String message = "Verify select a pre-defined device name.";
        UiObject2 deviceNameContainer =
                mDevice.findObject(mTask.getDeviceNameSettingItemContainerSelector());
        TestHelper.waitForUiObjectEnabled(deviceNameContainer);
        UiObject2 deviceNameValue = deviceNameContainer.findObject(By.text(subDeviceName));
        Assert.assertNotNull(message, deviceNameValue);
    }


    @Test
    @Category(CategorySettingsTests.class)
    public void test15SleepTimeDefaultValue() {
        String message;

        UiObject2 sleepSettingContainer =
                mDevice.findObject(mTask.getSleepTimeSettingItemContainerSelector());
        UiObject2 itemKey =
                sleepSettingContainer.findObject(mTask.getSettingItemKeySelector());
        message = "Verify the key text of sleep setting.";
        Assert.assertEquals(message, "休眠设置", itemKey.getText());

        UiObject2 itemValueContainer =
                sleepSettingContainer.findObject(mTask.getSettingSwitcherItemValueSelector());
        UiObject2 itemValue = itemValueContainer.findObject(By.clazz("android.widget.TextView"));
        message = "Verify the default value of sleep setting.";
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(itemValue));
        Assert.assertEquals(message, "永不休眠", itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test16SelectSleepTime() {
        String message;

        mTask.moveToSpecifiedSettingsItem(mTask.getSleepTimeSettingItemContainerSelector());
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 2);

        message = "Verify select the sleep time.";
        UiObject2 sleepSettingContainer =
                mDevice.findObject(mTask.getSleepTimeSettingItemContainerSelector());
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(sleepSettingContainer);

        Assert.assertNotNull(itemValue);
        Assert.assertEquals(message, "30分钟", itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test17ScreenSaverDefaultValue() {
        String message;

        mTask.scrollMoveToSpecificSettingsItem("屏保");
        UiObject2 screenSaverContainer =
                mDevice.findObject(mTask.getScreenSaverSettingItemContainerSelector());
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(screenSaverContainer);

        message = "Verify the default value of screen saver.";
        Assert.assertNotNull(itemValue);
        Assert.assertEquals(message, "5分钟（默认）", itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test18SelectScreenSaver() {
        String message;

        mTask.moveToSpecifiedSettingsItem(mTask.getScreenSaverSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionMoveLeft());

        UiObject2 screenSaverContainer =
                mDevice.findObject(mTask.getScreenSaverSettingItemContainerSelector());
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(screenSaverContainer);

        message = "Verify the selected value of screen saver.";
        Assert.assertNotNull(itemValue);
        Assert.assertEquals(message, "关闭", itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test21InstallUnknownAppDefaultValue() {
        String message;

        mTask.scrollMoveToSpecificSettingsItem("安装未知来源应用");
        UiObject2 installUnknownAppItemContainer =
                mDevice.findObject(mTask.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);

        message = "Verify the default value text of install unknown app settings item.";
        Assert.assertNotNull(valueText);
        Assert.assertEquals(message, "禁止", valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test22CancelAllowedInstallUnknownApp() {
        String message;

        mTask.moveToSpecifiedSettingsItem(mTask.getInstallUnknownAppSettingItemContainerSelector());

        // verify title
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        UiObject2 dialogTitle = mDevice.findObject(mTask.getTitleOfCommonDialogSelector());
        message = "Verify the common dialog title is enabled.";
        Assert.assertTrue(message, TestHelper.waitForUiObjectEnabled(dialogTitle));
        message = "Verify the text of common dialog title.";
        Assert.assertEquals(message, "安装未知应用", dialogTitle.getText());

        // verify click cancel button
        UiObject2 cancelBtn = mDevice.findObject(mTask.getCancelBtnOfCommonDialogSelector());
        message = "Verify the cancel button on common dialog is enabled.";
        Assert.assertTrue(message, TestHelper.waitForUiObjectClickable(cancelBtn));
        mAction.doClickActionAndWait(cancelBtn);

        UiObject2 installUnknownAppItemContainer =
                mDevice.findObject(mTask.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);
        message = "Verify the value text of install unknown app settings item.";
        Assert.assertEquals(message, "禁止", valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test23SelectAllowedInstallUnknownApp() {
        String message;

        mTask.moveToSpecifiedSettingsItem(mTask.getInstallUnknownAppSettingItemContainerSelector());

        // verify click confirm button
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        UiObject2 confirmBtn = mDevice.findObject(mTask.getConfirmBtnOfCommonDialogSelector());
        message = "Verify the confirm button on common dialog is enabled.";
        Assert.assertTrue(message, TestHelper.waitForUiObjectClickable(confirmBtn));
        mAction.doClickActionAndWait(confirmBtn);

        UiObject2 installUnknownAppItemContainer =
                mDevice.findObject(mTask.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);
        message = "Verify the value text of install unknown app settings item.";
        Assert.assertEquals(message, "允许", valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test25SystemRecoverDialogAndClickCancel() {
        String message;

        mTask.scrollMoveToSpecificSettingsItem("恢复出厂设置");
        UiObject2 recoverItem = mDevice.findObject(mTask.getSystemRecoverSettingItemSelector());
        mAction.doClickActionAndWait(recoverItem);  // open dialog

        // verification 1
        UiObject2 title = mDevice.findObject(mTask.getSystemRecoverDialogTitleSelector());
        Assert.assertNotNull(title);
        message = "Verify the content of recover dialog.";
        Assert.assertEquals(message, "您的设备将恢复出厂设置", title.getText());

        // verification 2
        UiObject2 cancelBtn =
                mDevice.findObject(mTask.getCancelBtnOfSystemRecoverDialogSelector());
        message = "Verify the cancel button in recover dialog.";
        Assert.assertTrue(message, TestHelper.waitForUiObjectClickable(cancelBtn));

        mAction.doClickActionAndWait(cancelBtn);
        message = "Verify back to common settings page after click the cancel button";
        Assert.assertTrue(message, TestHelper.waitForUiObjectEnabled(recoverItem));
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test26SaveInfoOnSystemRecoverDialog() {
        String message;

        mTask.scrollMoveToSpecificSettingsItem("恢复出厂设置");
        UiObject2 recoverItem = mDevice.findObject(mTask.getSystemRecoverSettingItemSelector());
        mAction.doClickActionAndWait(recoverItem);  // open dialog

        // verification 1
        UiObject2 confirmBtn =
                mDevice.findObject(mTask.getConfirmBtnOfSystemRecoverDialogSelector());
        Assert.assertNotNull(confirmBtn);
        message = "Verify the confirm button is default focused.";
        Assert.assertTrue(message, confirmBtn.isFocused());

        // verification 2
        UiObject2 saveInfoCheckbox = mDevice.findObject(mTask.getSaveInfoOfRecoverDialogSelector());
        Assert.assertNotNull(saveInfoCheckbox);

        mAction.doDeviceActionAndWait(new DeviceActionMoveUp());
        message = "Verify the saver information checkbox is focused.";
        Assert.assertTrue(message, saveInfoCheckbox.isFocused());

        mAction.doDeviceActionAndWait(new DeviceActionEnter());
        message = "Verify the saver information checkbox is checked.";
        Assert.assertTrue(message, saveInfoCheckbox.isChecked());
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    public void test31DefaultWallpaper() {
        // TODO: 2016/6/7
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    public void test32SubWallpapers() {
        // TODO: 2016/6/7
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    public void test33SelectWallpaper() {
        // TODO: 2016/6/7
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test41DefaultLocationOnSettings() {
        String message;

        UiObject2 locationItemContainer =
                mDevice.findObject(mTask.getLocationSettingItemContainerSelector());

        UiObject2 locationItemKey =
                locationItemContainer.findObject(mTask.getSettingItemKeySelector());
        message = "Verify the location item key text.";
        Assert.assertEquals(message, "天气位置", locationItemKey.getText());

        UiObject2 locationItemValue =
                locationItemContainer.findObject(mTask.getSettingItemValueSelector());
        message = "Verify the location item default value text on Common Settings.";
        Assert.assertEquals(message, "湖北 武汉", locationItemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test42OpenWeatherAppAndBackToSettings() {
        String message;

        mTask.moveToSpecifiedSettingsItem(mTask.getLocationSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter());
        message = "Verify open Weather home.";
        Assert.assertTrue(message,
                TestHelper.waitForAppOpenedByCheckCurPackage(WEATHER_PKG_NAME, TIME_OUT));

        mAction.doDeviceActionAndWait(new DeviceActionBack());
        message = "Verify back to Settings home.";
        Assert.assertTrue(message, TestHelper.waitForAppOpenedByCheckCurPackage(SETTINGS_PKG_NAME));
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    public void test43DefaultLocationOnSubPage() {
        // weather activities is not available for automation
        String message;

        mTask.moveToSpecifiedSettingsItem(mTask.getLocationSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter());
        Assert.assertTrue(TestHelper.waitForAppOpenedByCheckCurPackage(WEATHER_PKG_NAME, TIME_OUT));

        mWeatherTask.openBottomMenu();
        mWeatherTask.focusOnSpecifiedMenuButtonAndEnter(
                mWeatherTask.WEATHER_MENU_BUTTON_TEXT_ADD_CITY);

        message = "Verify the default province text on city manager.";
        Assert.assertEquals(message, "湖北", mWeatherTask.getSelectedLocationProvince());
        message = "Verify the default city text on city manager.";
        Assert.assertEquals(message, "武汉", mWeatherTask.getSelectedLocationCity());
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    public void test44SelectLocationOnSubPage() {
        // weather activities is not available for automation
        String message;
        String province = "江西";
        String city = "九江";

        mTask.moveToSpecifiedSettingsItem(mTask.getLocationSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter());

        mWeatherTask.openBottomMenu();
        mWeatherTask.focusOnSpecifiedMenuButtonAndEnter(
                mWeatherTask.WEATHER_MENU_BUTTON_TEXT_ADD_CITY);

        // select a location from sub page
        mWeatherTask.selectSpecifiedLocationProvince(province, false);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        mWeatherTask.selectSpecifiedLocationCity(city, false);
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mAction.doDeviceActionAndWait(new DeviceActionBack(), WAIT);
        message = "Verify the selected location item value text on Common Settings.";
        String expectedItemValueText = String.format("%s %s", province, city);
        UiObject2 locationItemContainer =
                mDevice.findObject(mTask.getLocationSettingItemContainerSelector());
        UiObject2 locationItemValue =
                locationItemContainer.findObject(mTask.getSettingItemValueSelector());
        Assert.assertEquals(message, expectedItemValueText, locationItemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test99ClearUpAfterAllTestCasesDone() {
        mTask.destroyInstance();
        mWeatherTask.destroyInstance();
    }

}
