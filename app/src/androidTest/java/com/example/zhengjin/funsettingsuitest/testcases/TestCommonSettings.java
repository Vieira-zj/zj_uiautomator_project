package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

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

    private final String[] SUB_VALUES_SLEEP_TIME =
            {"永不休眠", "15分钟", "30分钟", "60分钟", "90分钟", "120分钟"};
    private final String[] SUB_VALUES_SCREEN_SAVER =
            {"5分钟（默认）", "10分钟", "15分钟", "20分钟", "关闭"};
    private final String[] SUB_VALUES_SHUTDOWN_TV_TIME =
            {"关闭", "30分钟", "60分钟", "90分钟", "120分钟"};
    private final String[] SUB_VALUES_WALLPAPER = {"神秘紫光", "霞光黄昏", "静谧月夜", "朦胧山色"};

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
        mTask.openCommonSettingsHomePage();
    }

    @After
    public void clearUp() {
        if (RunnerProfile.isTakeScreenshot) {
            ShellUtils.takeScreenCapture(mDevice);
            ShellUtils.systemWaitByMillis(SHORT_WAIT);
        }
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
    public void test15_01OpenAdvancedSettingItem() {
        mTask.moveToSpecifiedSettingsItem(mTask.getAdvancedItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mMessage = "Verify the text of title on advanced page.";
        UiObject2 advancedPageTitle =
                mDevice.findObject(mTask.getTitleOfSettingsPageSelector());
        TestHelper.waitForUiObjectEnabled(advancedPageTitle);
        Assert.assertEquals("高级设置", advancedPageTitle.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test15_02SleepTimeDefaultValue() {
        mTask.moveToSpecifiedSettingsItem(mTask.getAdvancedItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        TestHelper.waitForUiObjectEnabled(
                mDevice.findObject(mTask.getTitleOfSettingsPageSelector()));

        mMessage = "Verify the key text of sleep setting.";
        UiObject2 sleepSettingContainer =
                mDevice.findObject(mTask.getSleepTimeSettingItemContainerSelector());
        UiObject2 itemKey =
                sleepSettingContainer.findObject(mTask.getSettingItemKeySelector());
        Assert.assertEquals(mMessage, "休眠设置", itemKey.getText());

        mMessage = "Verify the default value of sleep setting.";
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(sleepSettingContainer);
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(itemValue));
        Assert.assertEquals(mMessage, SUB_VALUES_SLEEP_TIME[0], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test15_01SleepTimeSubValues() {
        mTask.moveToSpecifiedSettingsItem(mTask.getAdvancedItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        TestHelper.waitForUiObjectEnabled(
                mDevice.findObject(mTask.getTitleOfSettingsPageSelector()));

        mMessage = "Verify the sleep time sub value at position %d.";
        for (int i = 1; i < SUB_VALUES_SLEEP_TIME.length; i++) {
            mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
            UiObject2 itemValue = mTask.getTextViewOfSwitcher(
                    mDevice.findObject(mTask.getSleepTimeSettingItemContainerSelector()));
            Assert.assertEquals(String.format(mMessage, i),
                    SUB_VALUES_SLEEP_TIME[i], itemValue.getText());
        }

        mMessage = "Verify move from last to first sleep time item value.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(
                mDevice.findObject(mTask.getSleepTimeSettingItemContainerSelector()));
        Assert.assertEquals(mMessage, SUB_VALUES_SLEEP_TIME[0], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test16SelectSleepTime() {
        mTask.moveToSpecifiedSettingsItem(mTask.getAdvancedItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        TestHelper.waitForUiObjectEnabled(
                mDevice.findObject(mTask.getTitleOfSettingsPageSelector()));

        mMessage = "Verify select the sleep time.";
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 3);
        ShellUtils.systemWaitByMillis(SHORT_WAIT);

        UiObject2 sleepSettingContainer =
                mDevice.findObject(mTask.getSleepTimeSettingItemContainerSelector());
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(sleepSettingContainer);
        Assert.assertNotNull(itemValue);
        Assert.assertEquals(mMessage, SUB_VALUES_SLEEP_TIME[3], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test17_01SetShutDownTvTimeDefaultValue() {
        mTask.moveToSpecifiedSettingsItem(mTask.getAdvancedItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        TestHelper.waitForUiObjectEnabled(
                mDevice.findObject(mTask.getTitleOfSettingsPageSelector()));

        UiObject2 shutDownTvContainer =
                mDevice.findObject(mTask.getSetShutDownTvItemContainerSelector());

        mMessage = "Verify the key text of set shutdown tv time.";
        UiObject2 itemKey =
                shutDownTvContainer.findObject(mTask.getSettingItemKeySelector());
        Assert.assertEquals(mMessage, "定时关机", itemKey.getText());

        mMessage = "Verify the default value of set shutdown tv time.";
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(shutDownTvContainer);
        Assert.assertNotNull(itemValue);
        Assert.assertEquals(mMessage, SUB_VALUES_SHUTDOWN_TV_TIME[0], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test17_02SetShutDownTvTimeSubValues() {
        mTask.moveToSpecifiedSettingsItem(mTask.getAdvancedItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        TestHelper.waitForUiObjectEnabled(
                mDevice.findObject(mTask.getTitleOfSettingsPageSelector()));

        mMessage = "Verify the set shutdown tv time sub value at position %d.";
        mTask.moveToSpecifiedSettingsItem(mTask.getSetShutDownTvItemContainerSelector());
        for (int i = 1; i < SUB_VALUES_SHUTDOWN_TV_TIME.length; i++) {
            mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
            UiObject2 itemValue = mTask.getTextViewOfSwitcher(
                    mDevice.findObject(mTask.getSetShutDownTvItemContainerSelector()));
            Assert.assertEquals(String.format(mMessage, i),
                    SUB_VALUES_SHUTDOWN_TV_TIME[i], itemValue.getText());
        }

        mMessage = "Verify move from last to first value of set shutdown tv time.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(
                mDevice.findObject(mTask.getSetShutDownTvItemContainerSelector()));
        Assert.assertEquals(mMessage, SUB_VALUES_SHUTDOWN_TV_TIME[0], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test18ScreenSaverDefaultValue() {
        mTask.scrollMoveToSpecificSettingsItem("屏保");

        mMessage = "Verify the default value of screen saver.";
        UiObject2 screenSaverContainer =
                mDevice.findObject(mTask.getScreenSaverSettingItemContainerSelector());
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(screenSaverContainer);
        Assert.assertNotNull(itemValue);
        Assert.assertEquals(mMessage, SUB_VALUES_SCREEN_SAVER[0], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test18_01ScreenSaverSubValues() {
        mTask.moveToSpecifiedSettingsItem(mTask.getScreenSaverSettingItemContainerSelector());
        ShellUtils.systemWaitByMillis(SHORT_WAIT);

        mMessage = "Verify the sub values of screen saver at position %d.";
        for (int i = 1; i < SUB_VALUES_SCREEN_SAVER.length; i++) {
            mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
            UiObject2 itemValue = mTask.getTextViewOfSwitcher(
                    mDevice.findObject(mTask.getScreenSaverSettingItemContainerSelector()));
            Assert.assertEquals(String.format(mMessage, i),
                    SUB_VALUES_SCREEN_SAVER[i], itemValue.getText());
        }

        mMessage = "Verify move from last to first screen saver item value.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(
                mDevice.findObject(mTask.getScreenSaverSettingItemContainerSelector()));
        Assert.assertEquals(mMessage, SUB_VALUES_SCREEN_SAVER[0], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test19SelectScreenSaver() {
        mTask.moveToSpecifiedSettingsItem(mTask.getScreenSaverSettingItemContainerSelector());
        ShellUtils.systemWaitByMillis(SHORT_WAIT);
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 2);
        ShellUtils.systemWaitByMillis(WAIT);

        mMessage = "Verify select the screen saver value.";
        UiObject2 screenSaverContainer =
                mDevice.findObject(mTask.getScreenSaverSettingItemContainerSelector());
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(screenSaverContainer);
        Assert.assertNotNull(itemValue);
        Assert.assertEquals(mMessage, SUB_VALUES_SCREEN_SAVER[3], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test21InstallUnknownAppDefaultValue() {
        mTask.scrollMoveToSpecificSettingsItem("安装未知来源应用");

        mMessage = "Verify the default value text of install unknown app settings item.";
        UiObject2 installUnknownAppItemContainer =
                mDevice.findObject(mTask.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);
        Assert.assertNotNull(valueText);
        Assert.assertEquals(mMessage, TEXT_FORBIDDEN, valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test22AllowedInstallUnknownAppAndCancel() {
        mTask.moveToSpecifiedSettingsItem(mTask.getInstallUnknownAppSettingItemContainerSelector());

        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        UiObject2 dialogTitle = mDevice.findObject(mTask.getTitleOfCommonDialogSelector());
        mMessage = "Verify the common dialog title is enabled.";
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(dialogTitle));
        mMessage = "Verify the text of common dialog title.";
        Assert.assertEquals(mMessage, "安装未知应用", dialogTitle.getText());

        mMessage = "Verify the cancel button on common dialog is enabled.";
        UiObject2 cancelBtn = mDevice.findObject(mTask.getCancelBtnOfCommonDialogSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(cancelBtn));
        mAction.doClickActionAndWait(cancelBtn);

        mMessage = "Verify the value text of install unknown app settings item.";
        UiObject2 installUnknownAppItemContainer =
                mDevice.findObject(mTask.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);
        Assert.assertEquals(mMessage, TEXT_FORBIDDEN, valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test23AllowedInstallUnknownAppAndConfirm() {
        mTask.moveToSpecifiedSettingsItem(mTask.getInstallUnknownAppSettingItemContainerSelector());

        mMessage = "Verify the confirm button on common dialog is enabled.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        UiObject2 confirmBtn = mDevice.findObject(mTask.getConfirmBtnOfCommonDialogSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(confirmBtn));
        mAction.doClickActionAndWait(confirmBtn);

        mMessage = "Verify the value text of install unknown app settings item after allowed.";
        UiObject2 installUnknownAppItemContainer =
                mDevice.findObject(mTask.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);
        Assert.assertEquals(mMessage, "允许", valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test24ForbiddenInstallUnknownApp() {
        mTask.moveToSpecifiedSettingsItem(mTask.getInstallUnknownAppSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionMoveLeft(), WAIT);

        mMessage = "Verify the value text of install unknown app settings item after forbidden.";
        UiObject2 installUnknownAppItemContainer =
                mDevice.findObject(mTask.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);
        Assert.assertEquals(mMessage, TEXT_FORBIDDEN, valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test25SystemRecoverDialogAndClickCancel() {
        mTask.scrollMoveToSpecificSettingsItem(TEXT_SYSTEM_RECOVERY);
        UiObject2 recoverItem = mDevice.findObject(mTask.getSystemRecoverSettingItemSelector());
        mAction.doClickActionAndWait(recoverItem);  // open dialog

        // verification 1
        mMessage = "Verify the content of recover dialog.";
        UiObject2 title = mDevice.findObject(mTask.getSystemRecoverDialogTitleSelector());
        Assert.assertNotNull(title);
        Assert.assertEquals(mMessage, "您的设备将恢复出厂设置", title.getText());

        // verification 2
        mMessage = "Verify the cancel button in recover dialog.";
        UiObject2 cancelBtn =
                mDevice.findObject(mTask.getCancelBtnOfSystemRecoverDialogSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(cancelBtn));

        mMessage = "Verify back to common settings page after click the cancel button";
        mAction.doClickActionAndWait(cancelBtn);
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(recoverItem));
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test26SaveInfoOnSystemRecoverDialog() {
        mTask.scrollMoveToSpecificSettingsItem(TEXT_SYSTEM_RECOVERY);
        UiObject2 recoverItem = mDevice.findObject(mTask.getSystemRecoverSettingItemSelector());
        mAction.doClickActionAndWait(recoverItem);  // open dialog

        // verification 1
        mMessage = "Verify the confirm button is default focused.";
        UiObject2 confirmBtn =
                mDevice.findObject(mTask.getConfirmBtnOfSystemRecoverDialogSelector());
        Assert.assertNotNull(confirmBtn);
        Assert.assertTrue(mMessage, confirmBtn.isFocused());

        // verification 2
        UiObject2 saveInfoCheckbox = mDevice.findObject(mTask.getSaveInfoOfRecoverDialogSelector());
        Assert.assertNotNull(saveInfoCheckbox);

        mMessage = "Verify the saver information checkbox is focused.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveUp());
        Assert.assertTrue(mMessage, saveInfoCheckbox.isFocused());

        mMessage = "Verify the saver information checkbox is checked.";
        mAction.doDeviceActionAndWait(new DeviceActionEnter());
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
        Assert.assertEquals(mMessage, SUB_VALUES_WALLPAPER[0], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test32SubWallpapersOnSelectPage() {
        mTask.moveToSpecifiedSettingsItem(mTask.getWallpaperSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mMessage = "Verify there are 4 sub wallpapers on wallpaper select page.";
        List<UiObject2> wallpapers = mDevice.findObjects(By.clazz(CLASS_TEXT_VIEW));
        Assert.assertEquals(mMessage, SUB_VALUES_WALLPAPER.length, wallpapers.size());

        mMessage = "Verify the 1st sub wallpaper is default selected.";
        UiObject2 defaultWallpaper =
                mDevice.findObject(By.text(SUB_VALUES_WALLPAPER[0])).getParent();
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
        final String selectWallpaper = SUB_VALUES_WALLPAPER[2];

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

        mMessage = "Verify select input method by left key event.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveLeft(), WAIT);
        UiObject2 inputMethod = mTask.getTextViewOfSwitcher(
                mDevice.findObject(mTask.getInputMethodSettingItemContainerSelector()));
        Assert.assertEquals(mMessage, TEXT_INPUT_METHOD, inputMethod.getText());

        mMessage = "Verify select input method by right key event.";
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 2);
        ShellUtils.systemWaitByMillis(WAIT);
        inputMethod = mTask.getTextViewOfSwitcher(
                mDevice.findObject(mTask.getInputMethodSettingItemContainerSelector()));
        Assert.assertEquals(mMessage, TEXT_INPUT_METHOD, inputMethod.getText());
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    public void test41DefaultLocationOnSettings() {
        // weather location settings item is remove from settings home page
        UiObject2 locationItemContainer =
                mDevice.findObject(mTask.getLocationSettingItemContainerSelector());

        mMessage = "Verify the location item key text.";
        UiObject2 locationItemKey =
                locationItemContainer.findObject(mTask.getSettingItemKeySelector());
        Assert.assertEquals(mMessage, "天气位置", locationItemKey.getText());

        mMessage = "Verify the location item default value text on Common Settings.";
        UiObject2 locationItemValue =
                locationItemContainer.findObject(mTask.getSettingItemValueSelector());
        Assert.assertEquals(mMessage, "湖北 武汉", locationItemValue.getText());
    }

    @Ignore
    @Category(CategorySettingsTests.class)
    public void test42OpenWeatherAppAndBackToSettings() {
        // weather location settings item is remove from settings home page
        mMessage = "Verify open Weather app home.";
        mTask.moveToSpecifiedSettingsItem(mTask.getLocationSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter());
        Assert.assertTrue(mMessage,
                TestHelper.waitForAppOpenedByCheckCurPackage(WEATHER_PKG_NAME, TIME_OUT));

        mMessage = "Verify back to Settings app home.";
        mAction.doDeviceActionAndWait(new DeviceActionBack());
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

        mMessage = "Verify the selected location item value text on Common Settings.";
        mAction.doDeviceActionAndWait(new DeviceActionBack(), WAIT);
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
        for (String text : SUB_VALUES_WALLPAPER) {
            if (text.equals(wallpaper)) {
                return true;
            }
        }
        return false;
    }

}
