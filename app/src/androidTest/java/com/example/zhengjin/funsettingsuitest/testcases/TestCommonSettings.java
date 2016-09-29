package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import com.example.zhengjin.funsettingsuitest.testcategory.CategorySettingsTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskSettings;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
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

/**
 * Created by zhengjin on 2016/6/2.
 *
 * Include test cases for common settings module.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestCommonSettings {

    private static UiActionsManager sAction = UiActionsManager.getInstance();
    private UiDevice mDevice;

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        TaskLauncher.backToLauncher(mDevice);
        TaskLauncher.clickOnButtonFromTopQuickAccessBar(
                mDevice, TaskLauncher.getQuickAccessBtnSettingsSelector());
        sAction.doDeviceActionAndWait(new DeviceActionMoveUp());  // request focus
    }

    @After
    public void clearUp() {
        ShellUtils.takeScreenCapture(mDevice);
        ShellUtils.systemWait(SHORT_WAIT);

//        sAction.doRepeatDeviceActionAndWait(new DeviceActionBack(), 2);
        ShellUtils.stopProcess(TestConstants.SETTINGS_PKG_NAME);
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
        String message;

        UiObject2 deviceNameContainer =
                mDevice.findObject(TaskSettings.getDeviceNameSettingItemContainerSelector());
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
    public void test13SettingsSubDeviceNames() {
        sAction.doDeviceActionAndWait(new DeviceActionEnter());
        mDevice.wait(Until.hasObject(TaskSettings.getDialogDeviceNameListSelector()), WAIT);

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
        sAction.doDeviceActionAndWait(new DeviceActionEnter());  // open device name menu
        // select a sub device name and back
        String subDeviceName = "书房的电视";
        UiObject2 deviceName = mDevice.findObject(By.text(subDeviceName));
        TestHelper.waitForUiObjectClickable(deviceName);
        sAction.doClickActionAndWait(deviceName);

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
        String message;

        TaskSettings.moveToSpecifiedSettingsItem(
                mDevice, TaskSettings.getSleepTimeSettingItemContainerSelector());

        UiObject2 sleepSettingContainer =
                mDevice.findObject(TaskSettings.getSleepTimeSettingItemContainerSelector());
        UiObject2 itemKey =
                sleepSettingContainer.findObject(TaskSettings.getSettingItemKeySelector());
        message = "Verify the key text of sleep setting.";
        Assert.assertEquals(message,  "休眠设置", itemKey.getText());

        message = "Verify the sub values %s of sleep setting.";
        String[] subSleepValues = {"永不休眠","15分钟","30分钟","60分钟","90分钟","120分钟"};
        for (String value : subSleepValues) {
            UiObject2 subSleepText = mDevice.findObject(By.text(value));
            Assert.assertNotNull(String.format(message, value), subSleepText);
            sAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        }
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test17DefaultLocationOnSettings() {
        String message;

        UiObject2 locationItemContainer =
                mDevice.findObject(TaskSettings.getLocationSettingItemContainerSelector());

        UiObject2 locationItemKey =
                locationItemContainer.findObject(TaskSettings.getSettingItemKeySelector());
        message = "Verify the location item key text.";
        Assert.assertEquals(message, "天气位置", locationItemKey.getText());

        UiObject2 locationItemValue =
                locationItemContainer.findObject(TaskSettings.getSettingItemValueSelector());
        message = "Verify the location item default value text on Common Settings.";
        Assert.assertEquals(message, "湖北 武汉", locationItemValue.getText());
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    // requirement is update
    public void test18DefaultLocationOnSubPage() {
        String message;

        TaskSettings.scrollMoveToAndClickSettingsItem(mDevice, "天气位置");

        UiObject2 provinceList = mDevice.findObject(TaskSettings.getProvinceListSelector());
        UiObject2 middleProvince =
                provinceList.findObject(TaskSettings.getMiddleItemFromProvinceCityList());
        message = "Verify the default province text on location page.";
        Assert.assertEquals(message, "湖北", middleProvince.getText());

        UiObject2 cityList = mDevice.findObject(TaskSettings.getCityListSelector());
        UiObject2 middleCity =
                cityList.findObject(TaskSettings.getMiddleItemFromProvinceCityList());
        message = "Verify the default city text on location page.";
        Assert.assertEquals(message, "武汉", middleCity.getText());
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    // requirement is update
    public void test19SelectLocationOnSubPage() {
        String message;
        String province = "江西";
        String city = "九江";

        TaskSettings.scrollMoveToAndClickSettingsItem(mDevice, "天气位置");
        // select a location from sub page
        TaskSettings.selectSpecifiedLocationProvince(mDevice, province, false);
        sAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        TaskSettings.selectSpecifiedLocationCity(mDevice, city, false);
        sAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        message = "Verify the selected location item value text on Common Settings.";
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
        String message;

        TaskSettings.scrollMoveToAndClickSettingsItem(mDevice, "安装未知来源应用");

        UiObject2 installUnknownAppItemContainer =
                mDevice.findObject(TaskSettings.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 itemKey =
                installUnknownAppItemContainer.findObject(TaskSettings.getSettingItemKeySelector());
        message = "Verify the key text of install unknown app settings item.";
        Assert.assertEquals(message, "安装未知来源应用", itemKey.getText());

        UiObject2 itemValue = installUnknownAppItemContainer.findObject(
                TaskSettings.getInstallUnknownAppSettingItemValueSelector());
        UiObject2 valueText = itemValue.findObject(By.clazz("android.widget.TextView"));
        message = "Verify the default value text of install unknown app settings item.";
        Assert.assertEquals(message, "禁止", valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test22SelectAllowedFromInstallUnknownAppItem() {
        String message;

        TaskSettings.moveToSpecifiedSettingsItem(
                mDevice, TaskSettings.getInstallUnknownAppSettingItemContainerSelector());

        // verify title
        sAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        UiObject2 dialogTitle = mDevice.findObject(TaskSettings.getTitleOfCommonDialogSelector());
        message = "Verify the common dialog title is enabled.";
        Assert.assertTrue(message, TestHelper.waitForUiObjectEnabled(dialogTitle));
        message = "Verify the text of common dialog title.";
        Assert.assertEquals(message, "安装未知应用", dialogTitle.getText());

        // verify click confirm button
        UiObject2 confirmBtn = mDevice.findObject(TaskSettings.getConfirmBtnOfCommonDialogSelector());
        message = "Verify the confirm button on common dialog is enabled.";
        Assert.assertTrue(message, TestHelper.waitForUiObjectClickable(confirmBtn));
        sAction.doClickActionAndWait(confirmBtn);

        UiObject2 installUnknownAppItemContainer =
                mDevice.findObject(TaskSettings.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 itemValue = installUnknownAppItemContainer.findObject(
                TaskSettings.getInstallUnknownAppSettingItemValueSelector());
        UiObject2 valueText = itemValue.findObject(By.clazz("android.widget.TextView"));
        message = "Verify the value text of install unknown app settings item.";
        Assert.assertEquals(message, "允许", valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test23SelectForbiddenFromInstallUnknownAppItem() {
        // TODO: 2016/9/28
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test24SystemRecoverDialog() {
        String message;
        String recoverText = "恢复出厂设置";

        TaskSettings.scrollMoveToAndClickSettingsItem(mDevice, recoverText);  // open dialog

        // verification 1
        UiObject2 title =
                mDevice.findObject(TaskSettings.getSystemRecoverSettingItemKeySelector());
        message = "Verify the text of recover dialog title.";
        Assert.assertNotNull(message, title);
        message = "Verify the content of recover dialog.";
        Assert.assertEquals(message, "您的设备将恢复出厂设置", title.getText());

        // verification 2
        UiObject2 cancelBtn =
                mDevice.findObject(TaskSettings.getCancelBtnOfSystemRecoverDialogSelector());
        message = "Verify the cancel button in recover dialog.";
        Assert.assertNotNull(message, cancelBtn);

        sAction.doClickActionAndWait(cancelBtn);
        UiObject2 recoverItem = mDevice.findObject(By.text(recoverText));
        message = "Verify back to common settings page after click the cancel button";
        Assert.assertNotNull(message, recoverItem);
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    public void test25SaveInfoOnSystemRecoverDialog() {
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

