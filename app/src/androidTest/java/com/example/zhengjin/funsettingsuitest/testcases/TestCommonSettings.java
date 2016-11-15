package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategorySettingsTests;
import com.example.zhengjin.funsettingsuitest.testsuites.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceAction;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionBack;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
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

import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CLASS_TEXT_VIEW;
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
    private String mMessage;

    private final String SELECT_DEVICE_NAME = "书房的电视";
    private final String SELF_DEFINE_DEVICE_NAME = "funshionTV-test";
    private final String TEXT_INPUT_METHOD = "百度输入法TV版";
    private final String TEXT_FORBIDDEN = "禁止";
    private final String TEXT_SYSTEM_RECOVERY = "恢复出厂设置";
    private String[] TEXT_WALLPAPERS = {"神秘紫光", "霞光黄昏", "静谧月夜", "朦胧山色"};

    @BeforeClass
    public static void classSetUp() {
        ShellUtils.stopAndClearPackage(SETTINGS_PKG_NAME);
    }

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mTask = TaskSettings.getInstance();
        mWeatherTask = TaskWeather.getInstance();

        TaskLauncher.backToLauncher();
        TaskLauncher.clickOnButtonFromTopQuickAccessBar(
                TaskLauncher.getQuickAccessBtnSettingsSelector());
        Assert.assertTrue("Open Settings app.",
                TestHelper.waitForAppOpenedByUntil(SETTINGS_PKG_NAME, WAIT));

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
        mMessage = "Verify the title name of common settings page.";
        Assert.assertEquals(mMessage, "通用设置", settingsTitle.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test12DeviceNameDefaultValue() {
        UiObject2 deviceNameContainer =
                mDevice.findObject(mTask.getDeviceNameSettingItemContainerSelector());
        mMessage = "Verify the device name item is NOT null.";
        Assert.assertNotNull(mMessage, deviceNameContainer);
        mMessage = "Verify the device name item is focused as default.";
        Assert.assertTrue(mMessage, deviceNameContainer.isFocused());

        UiObject2 deviceNameKey = deviceNameContainer.findObject(By.text("设备名称"));
        mMessage = "Verify the device name key text.";
        Assert.assertNotNull(mMessage, deviceNameKey);

        UiObject2 deviceNameValue = deviceNameContainer.findObject(By.text("风行电视"));
        mMessage = "Verify the device name value text.";
        Assert.assertNotNull(mMessage, deviceNameValue);
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test13DeviceNameSubValues() {
        mAction.doDeviceActionAndWait(new DeviceActionEnter());
        mDevice.wait(Until.hasObject(mTask.getDialogDeviceNameListSelector()), WAIT);

        mMessage = "Verify the item %s in device name menu.";
        String[] subDeviceNames = {"风行电视", "客厅的电视", "卧室的电视", "书房的电视", "自定义"};
        for (String deviceName : subDeviceNames) {
            UiObject2 subDeviceName = mDevice.findObject(By.text(deviceName));
            Assert.assertNotNull(String.format(mMessage, deviceName), subDeviceName);
        }
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test14_01SelectDeviceName() {
        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // open device name menu
        // select a sub device name and back
        UiObject2 deviceName = mDevice.findObject(By.text(SELECT_DEVICE_NAME));
        TestHelper.waitForUiObjectClickable(deviceName);
        mAction.doClickActionAndWait(deviceName, WAIT);

        mMessage = "Verify select a pre-defined device name.";
        UiObject2 deviceNameContainer =
                mDevice.findObject(mTask.getDeviceNameSettingItemContainerSelector());
        UiObject2 deviceNameValue = deviceNameContainer.findObject(By.text(SELECT_DEVICE_NAME));
        Assert.assertNotNull(deviceNameValue);
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(deviceNameValue));
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test14_02SelfDefineDeviceNameAndCancel() {
        try {
            mTask.openSelfDefineDeviceNamePage();

            // verification 1
            mMessage = "Verify the title of self-define device name activity.";
            UiObject2 title = mDevice.findObject(mTask.getTitleOfSettingsPageSelector());
            Assert.assertEquals("自定义设备名称", title.getText());

            UiObject2 editor = mDevice.findObject(mTask.getDeviceNameEditorSelector());
            mMessage = "Verify the device name editor is default focused.";
            Assert.assertTrue(mMessage, editor.isFocused());
            mMessage = "Verify the text in the device name editor.";
            Assert.assertEquals(mMessage, SELECT_DEVICE_NAME, editor.getText());

            // verification 2
            mMessage = "Verify cancel and back from the self-define device name activity.";
            mTask.inputEnTextInDeviceName("test");
            mAction.doDeviceActionAndWait(new DeviceActionBack(), WAIT);
            Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(
                    mTask.getDeviceNameValueByText(SELECT_DEVICE_NAME)));
        } finally {
            mTask.enableInputMethod();
        }
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test14_03SelfDefineDeviceNameAndConfirm() {
        try {
            mTask.openSelfDefineDeviceNamePage();

            // checkpoint 1
            mMessage = "Verify define a customized device name.";
            mTask.clearTextOfEditorView(SELECT_DEVICE_NAME.length());
            mTask.inputEnTextInDeviceName(SELF_DEFINE_DEVICE_NAME);
            UiObject2 editor = mDevice.findObject(mTask.getDeviceNameEditorSelector());
            Assert.assertEquals(mMessage, SELF_DEFINE_DEVICE_NAME, editor.getText());

            mMessage = "Verify the confirm button is focused.";
            mAction.doDeviceActionAndWait(new DeviceActionMoveDown());
            UiObject2 btnConfirm = mDevice.findObject(mTask.getDeviceNameConfirmButtonSelector());
            Assert.assertTrue(mMessage, btnConfirm.isFocused());

            // checkpoint 2
            mMessage = "Self-defined device name is updated success.";
            mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
            Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(
                    mTask.getDeviceNameValueByText(SELF_DEFINE_DEVICE_NAME)));
        } finally {
            mTask.enableInputMethod();
        }
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test14_04SelfDefineEmptyNameAndConfirm() {
        mTask.openSelfDefineDeviceNamePage();

        mMessage = "Verify define empty device name and submit.";
        mTask.clearTextOfEditorView(SELF_DEFINE_DEVICE_NAME.length());
        mAction.doMultipleDeviceActionsAndWait(new DeviceAction[]
                {new DeviceActionBack(), new DeviceActionMoveDown(), new DeviceActionEnter()});
        UiObject2 editor = mDevice.findObject(mTask.getDeviceNameEditorSelector());
        Assert.assertNull(mMessage, editor.getText());

        mMessage = "Verify the pre-defined device name is unchanged.";
        mAction.doDeviceActionAndWait(new DeviceActionBack(), WAIT);
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(
                mTask.getDeviceNameValueByText(SELF_DEFINE_DEVICE_NAME)));
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test15SleepTimeDefaultValue() {
        mTask.moveToSpecifiedSettingsItem(mTask.getAdvancedItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        UiObject2 sleepSettingContainer =
                mDevice.findObject(mTask.getSleepTimeSettingItemContainerSelector());
        TestHelper.waitForUiObjectEnabled(sleepSettingContainer);
        UiObject2 itemKey =
                sleepSettingContainer.findObject(mTask.getSettingItemKeySelector());
        mMessage = "Verify the key text of sleep setting.";
        Assert.assertEquals(mMessage, "休眠设置", itemKey.getText());

        UiObject2 itemValueContainer =
                sleepSettingContainer.findObject(mTask.getSettingSwitcherItemValueSelector());
        UiObject2 itemValue = itemValueContainer.findObject(By.clazz("android.widget.TextView"));
        mMessage = "Verify the default value of sleep setting.";
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(itemValue));
        Assert.assertEquals(mMessage, "永不休眠", itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test16SelectSleepTime() {
        mTask.moveToSpecifiedSettingsItem(mTask.getAdvancedItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        UiObject2 sleepSettingContainer =
                mDevice.findObject(mTask.getSleepTimeSettingItemContainerSelector());
        TestHelper.waitForUiObjectEnabled(sleepSettingContainer);
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 2);

        mMessage = "Verify the selected the sleep time.";
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(sleepSettingContainer);
        Assert.assertNotNull(itemValue);
        Assert.assertEquals(mMessage, "30分钟", itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test17ScreenSaverDefaultValue() {
        mTask.scrollMoveToSpecificSettingsItem("屏保");

        mMessage = "Verify the default value of screen saver.";
        UiObject2 screenSaverContainer =
                mDevice.findObject(mTask.getScreenSaverSettingItemContainerSelector());
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(screenSaverContainer);
        Assert.assertNotNull(itemValue);
        Assert.assertEquals(mMessage, "5分钟（默认）", itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test18SelectScreenSaver() {
        mTask.moveToSpecifiedSettingsItem(mTask.getScreenSaverSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionMoveLeft());

        UiObject2 screenSaverContainer =
                mDevice.findObject(mTask.getScreenSaverSettingItemContainerSelector());
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(screenSaverContainer);

        mMessage = "Verify the selected value of screen saver.";
        Assert.assertNotNull(itemValue);
        Assert.assertEquals(mMessage, "关闭", itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test21InstallUnknownAppDefaultValue() {
        mTask.scrollMoveToSpecificSettingsItem("安装未知来源应用");
        UiObject2 installUnknownAppItemContainer =
                mDevice.findObject(mTask.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);

        mMessage = "Verify the default value text of install unknown app settings item.";
        Assert.assertNotNull(valueText);
        Assert.assertEquals(mMessage, TEXT_FORBIDDEN, valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test22SelectAllowedInstallUnknownAppAndCancel() {
        mTask.moveToSpecifiedSettingsItem(mTask.getInstallUnknownAppSettingItemContainerSelector());

        // verify title
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        UiObject2 dialogTitle = mDevice.findObject(mTask.getTitleOfCommonDialogSelector());
        mMessage = "Verify the common dialog title is enabled.";
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(dialogTitle));
        mMessage = "Verify the text of common dialog title.";
        Assert.assertEquals(mMessage, "安装未知应用", dialogTitle.getText());

        // verify click cancel button
        UiObject2 cancelBtn = mDevice.findObject(mTask.getCancelBtnOfCommonDialogSelector());
        mMessage = "Verify the cancel button on common dialog is enabled.";
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(cancelBtn));
        mAction.doClickActionAndWait(cancelBtn);

        UiObject2 installUnknownAppItemContainer =
                mDevice.findObject(mTask.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);
        mMessage = "Verify the value text of install unknown app settings item.";
        Assert.assertEquals(mMessage, TEXT_FORBIDDEN, valueText.getText());
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryDemoTests.class})
    public void test23SelectAllowedInstallUnknownAppAndConfirm() {
        mTask.moveToSpecifiedSettingsItem(mTask.getInstallUnknownAppSettingItemContainerSelector());

        // verify click confirm button
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        UiObject2 confirmBtn = mDevice.findObject(mTask.getConfirmBtnOfCommonDialogSelector());
        mMessage = "Verify the confirm button on common dialog is enabled.";
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(confirmBtn));
        mAction.doClickActionAndWait(confirmBtn);

        UiObject2 installUnknownAppItemContainer =
                mDevice.findObject(mTask.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);
        mMessage = "Verify the value text of install unknown app settings item after allowed.";
        Assert.assertEquals(mMessage, "允许", valueText.getText());
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryDemoTests.class})
    public void test24SelectForbiddenInstallUnknownApp() {
        mTask.moveToSpecifiedSettingsItem(mTask.getInstallUnknownAppSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionMoveLeft(), WAIT);

        UiObject2 installUnknownAppItemContainer =
                mDevice.findObject(mTask.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);
        mMessage = "Verify the value text of install unknown app settings item after forbidden.";
        Assert.assertEquals(mMessage, TEXT_FORBIDDEN, valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test25SystemRecoverDialogAndClickCancel() {
        mTask.scrollMoveToSpecificSettingsItem(TEXT_SYSTEM_RECOVERY);
        UiObject2 recoverItem = mDevice.findObject(mTask.getSystemRecoverSettingItemSelector());
        mAction.doClickActionAndWait(recoverItem);  // open dialog

        // verification 1
        UiObject2 title = mDevice.findObject(mTask.getSystemRecoverDialogTitleSelector());
        Assert.assertNotNull(title);
        mMessage = "Verify the content of recover dialog.";
        Assert.assertEquals(mMessage, "您的设备将恢复出厂设置", title.getText());

        // verification 2
        UiObject2 cancelBtn =
                mDevice.findObject(mTask.getCancelBtnOfSystemRecoverDialogSelector());
        mMessage = "Verify the cancel button in recover dialog.";
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(cancelBtn));

        mAction.doClickActionAndWait(cancelBtn);
        mMessage = "Verify back to common settings page after click the cancel button";
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(recoverItem));
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test26SaveInfoOnSystemRecoverDialog() {
        mTask.scrollMoveToSpecificSettingsItem(TEXT_SYSTEM_RECOVERY);
        UiObject2 recoverItem = mDevice.findObject(mTask.getSystemRecoverSettingItemSelector());
        mAction.doClickActionAndWait(recoverItem);  // open dialog

        // verification 1
        UiObject2 confirmBtn =
                mDevice.findObject(mTask.getConfirmBtnOfSystemRecoverDialogSelector());
        Assert.assertNotNull(confirmBtn);
        mMessage = "Verify the confirm button is default focused.";
        Assert.assertTrue(mMessage, confirmBtn.isFocused());

        // verification 2
        UiObject2 saveInfoCheckbox = mDevice.findObject(mTask.getSaveInfoOfRecoverDialogSelector());
        Assert.assertNotNull(saveInfoCheckbox);

        mAction.doDeviceActionAndWait(new DeviceActionMoveUp());
        mMessage = "Verify the saver information checkbox is focused.";
        Assert.assertTrue(mMessage, saveInfoCheckbox.isFocused());

        mAction.doDeviceActionAndWait(new DeviceActionEnter());
        mMessage = "Verify the saver information checkbox is checked.";
        Assert.assertTrue(mMessage, saveInfoCheckbox.isChecked());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test31WallpaperDefaultValue() {
        UiObject2 itemWallpaper =
                mDevice.findObject(mTask.getWallpaperSettingItemContainerSelector());

        mMessage = "Verify the item key for wallpaper setting item.";
        UiObject2 itemKey = itemWallpaper.findObject(mTask.getSettingItemKeySelector());
        Assert.assertEquals(mMessage, "壁纸", itemKey.getText());
        mMessage = "Verify the default wallpaper.";
        UiObject2 itemValue = itemWallpaper.findObject(mTask.getSettingItemValueSelector());
        Assert.assertEquals(mMessage, TEXT_WALLPAPERS[0], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test32SubWallpapersOnSelectPage() {
        mTask.moveToSpecifiedSettingsItem(mTask.getWallpaperSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mMessage = "Verify there are 4 sub wallpapers on wallpaper select page.";
        List<UiObject2> wallpapers = mDevice.findObjects(By.clazz(CLASS_TEXT_VIEW));
        Assert.assertEquals(mMessage, TEXT_WALLPAPERS.length, wallpapers.size());

        mMessage = "Verify the 1st sub wallpaper is default selected.";
        UiObject2 defaultWallpaper = mDevice.findObject(By.text(TEXT_WALLPAPERS[0])).getParent();
        Assert.assertTrue(mMessage, defaultWallpaper.isSelected());

        for (UiObject2 wallpaper : wallpapers) {
            String title = wallpaper.getText();
            mMessage = String.format("Verify the sub wallpaper %s is shown.", title);
            Assert.assertTrue(mMessage, this.IsSubWallpaperIncluded(title));
        }
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test33SelectWallpaper() {
        final String selectWallpaper = TEXT_WALLPAPERS[2];

        mTask.moveToSpecifiedSettingsItem(mTask.getWallpaperSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        mTask.selectSpecifiedSubWallpaper(selectWallpaper);

        mMessage = "Verify setting item value is changed to the selected wallpaper.";
        mAction.doDeviceActionAndWait(new DeviceActionBack());
        UiObject2 itemWallpaper =
                mDevice.findObject(mTask.getWallpaperSettingItemContainerSelector());
        UiObject2 itemValue = itemWallpaper.findObject(mTask.getSettingItemValueSelector());
        Assert.assertEquals(mMessage, selectWallpaper, itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test34InputMethodDefaultValue() {
        UiObject2 itemInput =
                mDevice.findObject(mTask.getInputMethodSettingItemContainerSelector());

        mMessage = "Verify the item key for input method setting item.";
        UiObject2 itemKey = itemInput.findObject(mTask.getSettingItemKeySelector());
        Assert.assertEquals(mMessage, "输入法", itemKey.getText());

        mMessage = "Verify the default input method.";
        UiObject2 inputMethod = mTask.getTextViewOfSwitcher(itemInput);
        Assert.assertEquals(mMessage, TEXT_INPUT_METHOD, inputMethod.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test35SelectInputMethod() {
        mTask.moveToSpecifiedSettingsItem(mTask.getInputMethodSettingItemContainerSelector());

        // verification 1
        mMessage = "Verify select input method by left key event.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveLeft(), WAIT);
        UiObject2 itemInput =
                mDevice.findObject(mTask.getInputMethodSettingItemContainerSelector());
        UiObject2 inputMethod = mTask.getTextViewOfSwitcher(itemInput);
        Assert.assertEquals(mMessage, TEXT_INPUT_METHOD, inputMethod.getText());

        // verification 2
        mMessage = "Verify select input method by right key event.";
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 2);
        ShellUtils.systemWaitByMillis(WAIT);
        inputMethod = mTask.getTextViewOfSwitcher(itemInput);
        Assert.assertEquals(mMessage, TEXT_INPUT_METHOD, inputMethod.getText());
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    public void test41DefaultLocationOnSettings() {
        // weather location settings item is remove from settings home page
        UiObject2 locationItemContainer =
                mDevice.findObject(mTask.getLocationSettingItemContainerSelector());

        UiObject2 locationItemKey =
                locationItemContainer.findObject(mTask.getSettingItemKeySelector());
        mMessage = "Verify the location item key text.";
        Assert.assertEquals(mMessage, "天气位置", locationItemKey.getText());

        UiObject2 locationItemValue =
                locationItemContainer.findObject(mTask.getSettingItemValueSelector());
        mMessage = "Verify the location item default value text on Common Settings.";
        Assert.assertEquals(mMessage, "湖北 武汉", locationItemValue.getText());
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    public void test42OpenWeatherAppAndBackToSettings() {
        // weather location settings item is remove from settings home page
        mTask.moveToSpecifiedSettingsItem(mTask.getLocationSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter());
        mMessage = "Verify open Weather app home.";
        Assert.assertTrue(mMessage,
                TestHelper.waitForAppOpenedByCheckCurPackage(WEATHER_PKG_NAME, TIME_OUT));

        mAction.doDeviceActionAndWait(new DeviceActionBack());
        mMessage = "Verify back to Settings app home.";
        Assert.assertTrue(mMessage,
                TestHelper.waitForAppOpenedByCheckCurPackage(SETTINGS_PKG_NAME));
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    public void test43DefaultLocationOnSubPage() {
        // weather activities is not available for automation
        mTask.moveToSpecifiedSettingsItem(mTask.getLocationSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter());
        Assert.assertTrue(TestHelper.waitForAppOpenedByCheckCurPackage(WEATHER_PKG_NAME, TIME_OUT));

        mWeatherTask.openBottomMenu();
        mWeatherTask.ClickOnSpecifiedMenuButtonByText(
                mWeatherTask.WEATHER_MENU_BUTTON_TEXT_ADD_CITY);

        mMessage = "Verify the default province text on city manager.";
        Assert.assertEquals(mMessage, "湖北", mWeatherTask.getSelectedLocationProvince());
        mMessage = "Verify the default city text on city manager.";
        Assert.assertEquals(mMessage, "武汉", mWeatherTask.getSelectedLocationCity());
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    public void test44SelectLocationOnSubPage() {
        // Error obtaining UI hierarchy on weather home activity
        String province = "江西";
        String city = "九江";

        mTask.moveToSpecifiedSettingsItem(mTask.getLocationSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter());

        mWeatherTask.openBottomMenu();
        mWeatherTask.ClickOnSpecifiedMenuButtonByText(
                mWeatherTask.WEATHER_MENU_BUTTON_TEXT_ADD_CITY);

        // select a location from sub page
        mWeatherTask.selectSpecifiedLocation(
                new String[]{province, city}, new boolean[]{false, false});
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mAction.doDeviceActionAndWait(new DeviceActionBack(), WAIT);
        mMessage = "Verify the selected location item value text on Common Settings.";
        String expectedItemValueText = String.format("%s %s", province, city);
        UiObject2 locationItemContainer =
                mDevice.findObject(mTask.getLocationSettingItemContainerSelector());
        UiObject2 locationItemValue =
                locationItemContainer.findObject(mTask.getSettingItemValueSelector());
        Assert.assertEquals(mMessage, expectedItemValueText, locationItemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test99ClearUpAfterAllTestCasesDone() {
        mTask.destroyInstance();
        mWeatherTask.destroyInstance();
    }

    private boolean IsSubWallpaperIncluded(String wallpaper) {
        for (String text : TEXT_WALLPAPERS) {
            if (text.equals(wallpaper)) {
                return true;
            }
        }
        return false;
    }

}
