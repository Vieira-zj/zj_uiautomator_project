package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import com.example.zhengjin.funsettingsuitest.testcategory.CategorySettingsTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionBack;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskSettings;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG;

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include test cases for common settings module.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestCommonSettings {

    private static UiActionsManager action = UiActionsManager.getInstance();
    private UiDevice mDevice;

    @Before
    public void setUp() {

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        TaskLauncher.backToLauncher(mDevice);
        TaskLauncher.clickOnQuickAccessButtonFromTopBar(
                mDevice, TaskLauncher.getQuickAccessBtnSettingsSelector(), SETTINGS_PKG);
        action.doDeviceActionAndWait(new DeviceActionMoveUp());  // request focus
    }

    @After
    public void clearUp() {

        TestHelper.doScreenCapture(mDevice);
        ShellUtils.systemWait(SHORT_WAIT);

        int repeatTimes = 2;
        action.doRepeatDeviceActionAndWait(new DeviceActionBack(), repeatTimes);
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test11TitleNameOfSettingsPage() {

        UiObject2 settingsTitle =
                mDevice.findObject(TaskSettings.getTitleOfSettingsPageSelector());
        Assert.assertNotNull(settingsTitle);

        String message = "Verify the title name of common settings page.";
        Assert.assertEquals(message, "通用设置", settingsTitle.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test12DefaultSettingsDeviceName() {

        UiObject2 deviceNameContainer =
                mDevice.findObject(TaskSettings.getDeviceNameSettingItemContainerSelector());

        String message = "Verify the device name item is focused as default.";
        Assert.assertNotNull(message, deviceNameContainer);
        Assert.assertTrue(deviceNameContainer.isFocused());

        message = "Verify the device name key text.";
        UiObject2 deviceNameKey = deviceNameContainer.findObject(By.text("设备名称"));
        Assert.assertNotNull(message, deviceNameKey);

        message = "Verify the device name value text.";
        UiObject2 deviceNameValue = deviceNameContainer.findObject(By.text("风行电视"));
        Assert.assertNotNull(message, deviceNameValue);
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test13SettingsSubDeviceNames() {

        action.doDeviceActionAndWait(new DeviceActionEnter());
        mDevice.wait(Until.findObject(TaskSettings.getDialogDeviceNameListSelector()), WAIT);

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

        action.doDeviceActionAndWait(new DeviceActionEnter());  // open device name menu
        // select a sub device name and back
        String subDeviceName = "书房的电视";
        UiObject2 deviceName = mDevice.findObject(By.text(subDeviceName));
        TestHelper.waitForUiObjectClickable(deviceName);
        action.doClickActionAndWait(deviceName);

        String message = "Verify select a pre-defined device name.";
        UiObject2 deviceNameContainer =
                mDevice.findObject(TaskSettings.getDeviceNameSettingItemContainerSelector());
        TestHelper.waitForUiObjectEnabled(deviceNameContainer);
        UiObject2 deviceNameValue = deviceNameContainer.findObject(By.text(subDeviceName));
        Assert.assertNotNull(message, deviceNameValue);
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test15SleepSettingSubValues() {

        TaskSettings.moveToSpecifiedSettingsItem(
                mDevice, TaskSettings.getSleepTimeSettingItemContainerSelector());

        String message = "Verify the key text of sleep setting.";
        String expectedKeyText = "休眠设置";
        UiObject2 sleepSettingContainer =
                mDevice.findObject(TaskSettings.getSleepTimeSettingItemContainerSelector());
        UiObject2 itemKey =
                sleepSettingContainer.findObject(TaskSettings.getSettingItemKeySelector());
        Assert.assertEquals(message, expectedKeyText, itemKey.getText());

        message = "Verify the sub values %s of sleep setting.";
        String[] subSleepValues = {"永不休眠","15分钟","30分钟","60分钟","90分钟","120分钟"};
        for (String value : subSleepValues) {
            UiObject2 subSleepText = mDevice.findObject(By.text(value));
            Assert.assertNotNull(String.format(message, value), subSleepText);
            action.doDeviceActionAndWait(new DeviceActionMoveRight());
        }
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test17DefaultLocationOnSettings() {

        UiObject2 locationItemContainer =
                mDevice.findObject(TaskSettings.getLocationSettingItemContainerSelector());

        String message = "Verify the location item key text.";
        String expectedItemKeyText = "天气位置";
        UiObject2 locationItemKey =
                locationItemContainer.findObject(TaskSettings.getSettingItemKeySelector());
        Assert.assertEquals(message, expectedItemKeyText, locationItemKey.getText());

        message = "Verify the location item default value text on Common Settings.";
        String expectedItemValueText= "湖北 武汉";
        UiObject2 locationItemValue =
                locationItemContainer.findObject(TaskSettings.getSettingItemValueSelector());
        Assert.assertEquals(message, expectedItemValueText, locationItemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test18DefaultLocationOnSubPage() {

        TaskSettings.scrollMoveToAndClickSettingsItem(mDevice, "天气位置");

        String message = "Verify the default province text on location page.";
        String expectedProvinceText = "湖北";
        UiObject2 provinceList = mDevice.findObject(TaskSettings.getProvinceListSelector());
        UiObject2 middleProvince =
                provinceList.findObject(TaskSettings.getMiddleItemFromProvinceCityList());
        Assert.assertEquals(message, expectedProvinceText, middleProvince.getText());

        message = "Verify the default city text on location page.";
        String expectedCityText = "武汉";
        UiObject2 cityList = mDevice.findObject(TaskSettings.getCityListSelector());
        UiObject2 middleCity =
                cityList.findObject(TaskSettings.getMiddleItemFromProvinceCityList());
        Assert.assertEquals(message, expectedCityText, middleCity.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test19SelectLocationOnSubPage() {

        TaskSettings.scrollMoveToAndClickSettingsItem(mDevice, "天气位置");

        // select a location from sub page
        String province = "江西";
        String city = "九江";
        TaskSettings.selectSpecifiedLocationProvince(mDevice, province, false);
        action.doDeviceActionAndWait(new DeviceActionMoveRight());
        TaskSettings.selectSpecifiedLocationCity(mDevice, city, false);
        action.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        String message = "Verify the selected location item value text on Common Settings.";
        String expectedItemValueText = String.format("%s %s", province, city);
        UiObject2 locationItemContainer =
                mDevice.findObject(TaskSettings.getLocationSettingItemContainerSelector());
        UiObject2 locationItemValue =
                locationItemContainer.findObject(TaskSettings.getSettingItemValueSelector());
        Assert.assertEquals(message, expectedItemValueText, locationItemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test21InstallUnknownAppDefaultValue() {

        TaskSettings.scrollMoveToAndClickSettingsItem(mDevice, "安装未知来源应用");

        String message = "Verify the key text of install unknown app settings item.";
        String expectedText = "安装未知来源应用";
        UiObject2 installUnknownAppItemContainer =
                mDevice.findObject(TaskSettings.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 itemKey =
                installUnknownAppItemContainer.findObject(TaskSettings.getSettingItemKeySelector());
        Assert.assertEquals(message, expectedText, itemKey.getText());

        message = "Verify the default value text of install unknown app settings item.";
        expectedText = "禁止";
        UiObject2 itemValue = installUnknownAppItemContainer.findObject(
                TaskSettings.getInstallUnknownAppSettingItemValueSelector());
        UiObject2 valueText = itemValue.findObject(By.clazz("android.widget.TextView"));
        Assert.assertEquals(message, expectedText, valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test22SelectPermitFromInstallUnknownAppItem() {

        TaskSettings.moveToSpecifiedSettingsItem(
                mDevice, TaskSettings.getInstallUnknownAppSettingItemContainerSelector());

        // TODO: 2016/6/23
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test24SystemRecoverDialog() {

        final String recoverText = "恢复出厂设置";
        TaskSettings.scrollMoveToAndClickSettingsItem(mDevice, recoverText);  // open dialog

        // verification 1
        String message = "Verify the text of recover dialog title.";
        UiObject2 title =
                mDevice.findObject(TaskSettings.getSystemRecoverSettingItemKeySelector());
        Assert.assertNotNull(message, title);

        String expectText = "您的设备将恢复出厂设置";
        message = "Verify the content of recover dialog.";
        Assert.assertEquals(message, expectText, title.getText());

        // verification 2
        message = "Verify the cancel button in recover dialog.";
        UiObject2 cancelBtn =
                mDevice.findObject(TaskSettings.getCancelBtnOfSystemRecoverDialogSelector());
        Assert.assertNotNull(message, cancelBtn);

        message = "Verify back to common settings page after click the cancel button";
        action.doClickActionAndWait(cancelBtn);
        UiObject2 recoverItem = mDevice.findObject(By.text(recoverText));
        Assert.assertNotNull(message, recoverItem);
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    public void test24SaveInfoOnSystemRecoverDialog() {
        // TODO: 2016/6/24
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
    public void test33SelectAWallpapers() {
        // TODO: 2016/6/7
    }
}

