package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcategory.CategorySettingsTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryVersion20;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryVersion30;
import com.example.zhengjin.funsettingsuitest.testrunner.RunnerProfile;
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
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskSettings;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
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

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/6/2.
 * <p>
 * Include test cases for common settings module.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestCommonSettings {

    private static final String TAG = TestCommonSettings.class.getSimpleName();

    private final String SELECT_DEVICE_NAME = "书房的电视";
    private final String SELF_DEFINE_DEVICE_NAME = "funshionTV-test";

    private final String TEXT_INPUT_METHOD = "百度输入法TV版";
    private final String TEXT_SYSTEM_RECOVERY = "恢复出厂设置";
    private final String TEXT_FORBIDDEN = "禁止";
    private final String TEXT_CLOSED = "关闭";
    private final String TEXT_SHUTDOWN_TV_TIME = "定时关机";

    private final String[] SUB_VALUES_SLEEP_TIME =
            {"永不休眠", "15分钟", "30分钟", "60分钟", "90分钟", "120分钟"};
    private final String[] SUB_VALUES_SCREEN_SAVER =
            {"5分钟（默认）", "10分钟", "15分钟", "20分钟", "关闭"};
    private final String[] SUB_VALUES_SHUTDOWN_TV_TIME =
            {"关闭", "30分钟", "60分钟", "90分钟", "120分钟"};
    private final String[] SUB_VALUES_WALLPAPER =
            {"星夜如梦", "神秘紫光", "霞光黄昏", "静谧月夜", "朦胧山色"};

    private UiDevice mDevice;
    private UiActionsManager mAction;
    private UiObjectsSettings mFunUiObjects;
    private TaskSettings mTask;

    private String mMessage;

    @BeforeClass
    public static void classSetUp() {
        ShellUtils.stopAndClearPackage(SETTINGS_PKG_NAME);
    }

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mFunUiObjects = UiObjectsSettings.getInstance();
        mTask = TaskSettings.getInstance();

        TaskLauncher.backToLauncher();
        mTask.openCommonSettingsHomePage();
    }

    @After
    public void clearUp() {
        if (RunnerProfile.isTakeSnapshot) {
            ShellUtils.takeScreenCapture(mDevice);
            ShellUtils.systemWaitByMillis(SHORT_WAIT);
        }
        ShellUtils.stopProcessByPackageName(SETTINGS_PKG_NAME);
    }

    @Test
    @Category({CategorySettingsTests.class})
    public void test11TitleNameOfSettingsPage() {
        UiObject2 settingsTitle =
                mDevice.findObject(mFunUiObjects.getTitleOfSettingsPageSelector());
        Assert.assertNotNull(settingsTitle);
        mMessage = "Verify the title name of common settings page.";
        Assert.assertEquals(mMessage, mTask.TEXT_COMMON_SETTINGS, settingsTitle.getText());
    }

    @Test
    @Category({CategorySettingsTests.class})
    public void test12DeviceNameDefaultValue() {
        UiObject2 deviceNameContainer =
                mDevice.findObject(mFunUiObjects.getDeviceNameSettingItemContainerSelector());
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
    @Category({CategorySettingsTests.class})
    public void test13DeviceNameSubValues() {
        mAction.doDeviceActionAndWait(new DeviceActionEnter());
        mDevice.wait(Until.hasObject(mFunUiObjects.getDialogDeviceNameListSelector()), WAIT);

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
                mDevice.findObject(mFunUiObjects.getDeviceNameSettingItemContainerSelector());
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
            UiObject2 title = mDevice.findObject(mFunUiObjects.getTitleOfSettingsPageSelector());
            Assert.assertEquals("自定义设备名称", title.getText());

            UiObject2 editor = mDevice.findObject(mFunUiObjects.getDeviceNameEditorSelector());
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
            UiObject2 editor = mDevice.findObject(mFunUiObjects.getDeviceNameEditorSelector());
            Assert.assertEquals(mMessage, SELF_DEFINE_DEVICE_NAME, editor.getText());

            mMessage = "Verify the confirm button is focused.";
            mAction.doDeviceActionAndWait(new DeviceActionMoveDown());
            UiObject2 btnConfirm =
                    mDevice.findObject(mFunUiObjects.getDeviceNameConfirmButtonSelector());
            Assert.assertTrue(mMessage, btnConfirm.isFocused());

            // checkpoint 2
            mMessage = "Self-defined device name is updated success.";
            mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
            Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(
                    mTask.getDeviceNameValueByText(SELF_DEFINE_DEVICE_NAME)));

            // if success, set global var
            RunnerProfile.deviceName = SELF_DEFINE_DEVICE_NAME;
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
        UiObject2 editor = mDevice.findObject(mFunUiObjects.getDeviceNameEditorSelector());
        Assert.assertNull(mMessage, editor.getText());

        mMessage = "Verify the pre-defined device name is unchanged.";
        mAction.doDeviceActionAndWait(new DeviceActionBack(), WAIT);
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(
                mTask.getDeviceNameValueByText(SELF_DEFINE_DEVICE_NAME)));
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test15_01OpenAdvancedSettingItem() {
        mTask.moveToSpecifiedSettingsItem(mFunUiObjects.getAdvancedItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mMessage = "Verify the text of title on advanced page.";
        UiObject2 advancedPageTitle =
                mDevice.findObject(mFunUiObjects.getTitleOfSettingsPageSelector());
        TestHelper.waitForUiObjectEnabled(advancedPageTitle);
        Assert.assertEquals("高级设置", advancedPageTitle.getText());
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryVersion30.class})
    public void test15_02FullScreenSettingItemOnAdvance() {
        mTask.openAdvancedSettingsPage();

        mMessage = "Verify the title of full screen setting item.";
        UiObject2 itemContainer = mDevice.findObject(
                mFunUiObjects.getFullScreenSettingItemContainerSelector());
        UiObject2 itemTitle = itemContainer.findObject(mFunUiObjects.getSettingItemKeySelector());
        Assert.assertEquals(mMessage, "信源自动全屏", itemTitle.getText());

        mMessage = "Verify the tips of full screen setting item.";
        UiObject2 itemTips = itemContainer.findObject(mFunUiObjects.getSettingItemTipsSelector());
        Assert.assertEquals(mMessage, "信源下重启，自动全屏播放", itemTips.getText());

        mMessage = "Verify the full screen setting item is default turn off.";
        UiObject2 itemValue = itemContainer.findObject(mFunUiObjects.getSettingItemValueSelector());
        Assert.assertEquals(mMessage, "已关闭", itemValue.getText());
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryVersion30.class})
    public void test15_03ScreenProjectionSettingItemOnAdvance() {
        mTask.openAdvancedSettingsPage();

        mMessage = "Verify the title of screen projection setting item.";
        UiObject2 itemContainer = mDevice.findObject(
                mFunUiObjects.getScreenProjectionSettingItemContainerSelector());
        UiObject2 itemTitle = itemContainer.findObject(mFunUiObjects.getSettingItemKeySelector());
        Assert.assertEquals(mMessage, "无线投屏", itemTitle.getText());

        mMessage = "Verify the value of screen projection setting item.";
        UiObject2 itemValue = itemContainer.findObject(mFunUiObjects.getSettingItemValueSelector());
        Assert.assertEquals(mMessage, "已开启", itemValue.getText());
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryVersion30.class})
    public void test15_04ExternalSettingItemsIsHidden() {
        mTask.openAdvancedSettingsPage();

        mMessage = "Verify the sleep time setting item is default focused.";
        UiObject2 sleepSettingContainer =
                mDevice.findObject(mFunUiObjects.getSleepTimeSettingItemContainerSelector());
        Assert.assertTrue(mMessage, sleepSettingContainer.isFocused());

        mMessage = "Verify hidden items is NOT shown after shorten keys, and items count is 4.";
        mAction.doRepeatDeviceActionAndWait(new DeviceActionCenter(), 5, 500L);
        ShellUtils.systemWaitByMillis(WAIT);

        UiObject2 settingItemsContainer =
                mDevice.findObject(mFunUiObjects.getSettingItemsContainerOnAdvancedSelector());
        List<UiObject2> settingItems = settingItemsContainer.findObjects(
                By.clazz(TestConstants.CLASS_RELATIVE_LAYOUT).maxDepth(1));
        Assert.assertEquals(mMessage, 4, settingItems.size());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test16_01SleepTimeDefaultValue() {
        mTask.openAdvancedSettingsPage();

        mMessage = "Verify the key text of sleep time setting.";
        UiObject2 sleepSettingContainer =
                mDevice.findObject(mFunUiObjects.getSleepTimeSettingItemContainerSelector());
        UiObject2 itemKey =
                sleepSettingContainer.findObject(mFunUiObjects.getSettingItemKeySelector());
        Assert.assertEquals(mMessage, "休眠设置", itemKey.getText());

        mMessage = "Verify the default value of sleep time setting.";
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(sleepSettingContainer);
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(itemValue));
        Assert.assertEquals(mMessage, SUB_VALUES_SLEEP_TIME[0], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test16_02SleepTimeSubValues() {
        mTask.openAdvancedSettingsPage();

        mMessage = "Verify the sleep time sub value at position %d.";
        for (int i = 1; i < SUB_VALUES_SLEEP_TIME.length; i++) {
            mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
            UiObject2 itemValue = mTask.getTextViewOfSwitcher(
                    mDevice.findObject(mFunUiObjects.getSleepTimeSettingItemContainerSelector()));
            Assert.assertEquals(String.format(mMessage, i),
                    SUB_VALUES_SLEEP_TIME[i], itemValue.getText());
        }

        mMessage = "Verify move from last to first sleep time item value.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(
                mDevice.findObject(mFunUiObjects.getSleepTimeSettingItemContainerSelector()));
        Assert.assertEquals(mMessage, SUB_VALUES_SLEEP_TIME[0], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test16_03SelectSleepTime() {
        mTask.openAdvancedSettingsPage();

        mMessage = "Verify select the sleep time.";
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 3);
        ShellUtils.systemWaitByMillis(SHORT_WAIT);

        UiObject2 sleepSettingContainer =
                mDevice.findObject(mFunUiObjects.getSleepTimeSettingItemContainerSelector());
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(sleepSettingContainer);
        Assert.assertNotNull(itemValue);
        Assert.assertEquals(mMessage, SUB_VALUES_SLEEP_TIME[3], itemValue.getText());
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryVersion30.class})
    public void test17_01TitleAndValueOfSetShutDownTime() {
        mTask.openAdvancedSettingsPage();

        mMessage = "Verify the key text of set shutdown tv time.";
        UiObject2 shutDownTimeItem =
                mDevice.findObject(mFunUiObjects.getShutDownTimeSettingItemContainerSelector());
        UiObject2 itemKey = shutDownTimeItem.findObject(mFunUiObjects.getSettingItemKeySelector());
        Assert.assertEquals(mMessage, TEXT_SHUTDOWN_TV_TIME, itemKey.getText());

        mMessage = "Verify the default value of set shutdown tv time.";
        UiObject2 itemValue =
                shutDownTimeItem.findObject(mFunUiObjects.getSettingItemValueSelector());
        Assert.assertEquals(mMessage, TEXT_CLOSED, itemValue.getText());
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryVersion30.class})
    public void test17_02TitleAndTipsOnSetShutDownTimeDialog() {
        mTask.openAdvancedSettingsPage();
        mTask.openSetShutDownTimeDialog();

        mMessage = "Verify the title of set shutdown time dialog.";
        UiObject2 title = mDevice.findObject(
                mFunUiObjects.getTitleOfSetShutDownTimeDialogSelector());
        Assert.assertEquals(mMessage, mTask.TITLE_SET_SHUTDOWN_TIME_DIALOG, title.getText());

        mMessage = "Verify the text of tips on set shutdown time dialog.";
        Assert.assertTrue(mMessage, TestHelper.waitForTextVisible("* 退出时自动保存"));
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryVersion30.class})
    public void test17_03ShutDownTimeCheckboxOnDialog() {
        mTask.openAdvancedSettingsPage();
        mTask.openSetShutDownTimeDialog();

        UiObject2 checkbox = mDevice.findObject(
                mFunUiObjects.getCheckboxOnShutDownTimeDialogSelector());
        mMessage = "Verify the checkbox is default focused.";
        Assert.assertTrue(checkbox.isFocused());
        mMessage = "Verify the checkbox is default un-check.";
        Assert.assertFalse(checkbox.isChecked());
        mMessage = "Verify the text of checkbox on set shutdown time dialog.";
        Assert.assertEquals(mMessage, "开启定时关机", checkbox.getText());

        mMessage = "Verify only the checkbox can be focused when un-checked.";
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 2);
        Assert.assertTrue(mMessage, checkbox.isFocused());
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryVersion30.class})
    public void test17_04TimesControlOnShutDownTimeDialog() {
        mTask.openAdvancedSettingsPage();
        mTask.openSetShutDownTimeDialog();

        int expectedHour = mTask.getHoursOfCurrentTime();
        int expectedMin = mTask.getMinutesOfCurrentTime();

        mMessage = "Verify the value of hours time control is default as current time.";
        UiObject2 hoursContainer =
                mDevice.findObject(mFunUiObjects.getHoursControlOnShutDownTimeDialogSelector());
        UiObject2 actualHour = mTask.getValueOfTimeControlOnShutDownTimeDialog(hoursContainer);
        Assert.assertNotNull(actualHour);
        Assert.assertEquals(mMessage, expectedHour,
                mTask.getIntValueFromTimeControlText(actualHour.getText()));

        mMessage = "Verify the value of minutes time control is default as current time.";
        UiObject2 minContainer =
                mDevice.findObject(mFunUiObjects.getMinutesControlOnShutDownTimeDialogSelector());
        UiObject2 actualMin =
                mTask.getValueOfTimeControlOnShutDownTimeDialog(minContainer);
        Assert.assertNotNull(actualMin);
        Assert.assertEquals(mMessage, expectedMin,
                mTask.getIntValueFromTimeControlText(actualMin.getText()));
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryVersion30.class})
    public void test17_05EditHoursControlOnShutDownTimeDialog() {
        try {
            mTask.openAdvancedSettingsPage();
            mTask.openSetShutDownTimeDialog();
            int expectedHour = mTask.getHoursOfCurrentTime();

            mMessage = "Verify the hours control can be focused after checkbox is checked.";
            mTask.checkSetShutDownTimeCheckbox();
            mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
            UiObject2 hoursContainer = mDevice.findObject(
                    mFunUiObjects.getHoursControlOnShutDownTimeDialogSelector());
            Assert.assertTrue(mMessage, hoursContainer.isSelected());

            mMessage = "Verify the hours is updated when move up on hours control.";
            int upMoveTimes = 1;
            mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveUp(), upMoveTimes);
            UiObject2 actualHour =
                    mTask.getValueOfTimeControlOnShutDownTimeDialog(hoursContainer);
            Assert.assertNotNull(actualHour);

            expectedHour = mTask.subHoursValue(expectedHour, upMoveTimes);
            Assert.assertEquals(mMessage, expectedHour,
                    mTask.getIntValueFromTimeControlText(actualHour.getText()));

            mMessage = "Verify the hours is updated when move down on hours control.";
            int downMoveTimes = 3;
            mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveDown(), downMoveTimes);
            actualHour = mTask.getValueOfTimeControlOnShutDownTimeDialog(hoursContainer);
            Assert.assertNotNull(actualHour);
            Assert.assertEquals(mMessage,
                    mTask.addHoursValue(expectedHour, downMoveTimes),
                    mTask.getIntValueFromTimeControlText(actualHour.getText()));
        } finally {
            // exit with un-save the updated value
            mTask.unCheckSetShutDownTimeCheckbox();
        }
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryVersion30.class})
    public void test17_06EditMinutesControlOnSetShutDownTimeDialog() {
        try {
            mTask.openAdvancedSettingsPage();
            mTask.openSetShutDownTimeDialog();
            int expectedMin = mTask.getMinutesOfCurrentTime();

            mMessage = "Verify the minutes control can be focused after checkbox is checked.";
            mTask.checkSetShutDownTimeCheckbox();
            mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 2);
            UiObject2 minContainer = mDevice.findObject(
                    mFunUiObjects.getMinutesControlOnShutDownTimeDialogSelector());
            Assert.assertTrue(mMessage, minContainer.isSelected());

            mMessage = "Verify the minutes is updated when move up on hours control.";
            int upMoveTimes = 2;
            mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveUp(), upMoveTimes);
            UiObject2 actualMin =
                    mTask.getValueOfTimeControlOnShutDownTimeDialog(minContainer);
            Assert.assertNotNull(actualMin);

            expectedMin = mTask.subMinutesValue(expectedMin, upMoveTimes);
            Assert.assertEquals(mMessage, expectedMin,
                    mTask.getIntValueFromTimeControlText(actualMin.getText()));

            mMessage = "Verify the minutes is updated when move down on hours control.";
            int downMoveTimes = 3;
            mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveDown(), downMoveTimes);
            actualMin = mTask.getValueOfTimeControlOnShutDownTimeDialog(minContainer);
            Assert.assertNotNull(actualMin);
            Assert.assertEquals(mMessage,
                    mTask.addMinutesValue(expectedMin, downMoveTimes),
                    mTask.getIntValueFromTimeControlText(actualMin.getText()));
        } finally {
            // exit with un-save the updated value
            mTask.unCheckSetShutDownTimeCheckbox();
        }
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryVersion30.class})
    public void test17_11SetShutDownTimeAndSave() {
        try {
            mTask.openAdvancedSettingsPage();
            mTask.openSetShutDownTimeDialog();

            mTask.focusOnHoursOfShutDownTimeControl();
            String expectedHour = mTask.setAndGetHoursOfShutDownTime(new DeviceActionMoveDown(), 2);
            mTask.focusOnMinutesOfShutDownTimeControl();
            String expectedMin = mTask.setAndGetMinutesOfShutDownTime(new DeviceActionMoveUp(), 2);
            mAction.doDeviceActionAndWait(new DeviceActionCenter());  // save

            mMessage = "Verify the shutdown time is updated on settings page after saved.";
            UiObject2 shutDownTimeContainer = TestHelper.waitForUiObjectExistAndReturn(
                    mFunUiObjects.getShutDownTimeSettingItemContainerSelector());
            UiObject2 itemValue =
                    shutDownTimeContainer.findObject(mFunUiObjects.getSettingItemValueSelector());
            Assert.assertEquals(mMessage,
                    String.format("%s:%s关机", expectedHour, expectedMin), itemValue.getText());
        } finally {
            mTask.unSetShutDownTvTime();
        }
    }

    @Test
    @Ignore
    @Category({CategorySettingsTests.class})
    public void test17_12SetShutDownTimeEqualCurTime() {
        // TODO: 2017/2/24
    }

    @Test
    @Ignore
    @Category({CategorySettingsTests.class})
    public void test17_13SetShutDownTimeLessThanCurTime() {
        // TODO: 2017/2/24
    }

    @Test
    @Category({CategorySettingsTests.class})
    public void test17_14UnsetShutDownTime() {
        mTask.openAdvancedSettingsPage();
        mTask.unSetShutDownTvTime();

        mMessage = "Verify the shutdown time item is closed on settings page after saved.";
        UiObject2 shutDownTimeItem = TestHelper.waitForUiObjectExistAndReturn(
                mFunUiObjects.getShutDownTimeSettingItemContainerSelector());
        UiObject2 itemValue =
                shutDownTimeItem.findObject(mFunUiObjects.getSettingItemValueSelector());
        Assert.assertEquals(mMessage, TEXT_CLOSED, itemValue.getText());
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryVersion30.class})
    public void test17_15SetShutDownTimeLessThanFiveMinutes() {
        mTask.openAdvancedSettingsPage();
        mTask.openSetShutDownTimeDialog();

        mTask.focusOnMinutesOfShutDownTimeControl();
        String expectedMin = mTask.setAndGetMinutesOfShutDownTime(new DeviceActionMoveDown(), 4);
        mAction.doDeviceActionAndWait(new DeviceActionCenter());
        Log.d(TAG, String.format("%s set and save shutdown time: %s minutes",
                TestConstants.LOG_KEYWORD, expectedMin));

        mMessage = "Verify the status of shutdown time settings is closed" +
                " when set time less than 5 minutes.";
        UiObject2 shutDownTimeItem = TestHelper.waitForUiObjectExistAndReturn(
                mFunUiObjects.getShutDownTimeSettingItemContainerSelector());
        UiObject2 itemValue =
                shutDownTimeItem.findObject(mFunUiObjects.getSettingItemValueSelector());
        Assert.assertEquals(mMessage, TEXT_CLOSED, itemValue.getText());
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryVersion20.class})
    public void test17_21SetShutDownTvTimeDefaultValue() {
        UiObject2 itemContainer =
                mDevice.findObject(mFunUiObjects.getShutDownTimeSettingItemContainerSelector());

        mMessage = "Verify the key text of set shutdown tv time.";
        UiObject2 itemKey = itemContainer.findObject(mFunUiObjects.getSettingItemKeySelector());
        Assert.assertEquals(mMessage, TEXT_SHUTDOWN_TV_TIME, itemKey.getText());

        mMessage = "Verify the default value of set shutdown tv time.";
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(itemContainer);
        Assert.assertNotNull(itemValue);
        Assert.assertEquals(mMessage, SUB_VALUES_SHUTDOWN_TV_TIME[0], itemValue.getText());
    }

    @Test
    @Category({CategorySettingsTests.class, CategoryVersion20.class})
    public void test17_22SetShutDownTvTimeSubValues() {
        mTask.openAdvancedSettingsPage();
        mTask.moveToSpecifiedSettingsItem(
                mFunUiObjects.getShutDownTimeSettingItemContainerSelector());
        ShellUtils.systemWaitByMillis(SHORT_WAIT);

        mMessage = "Verify the set shutdown tv time sub value at position %d.";
        UiObject2 itemContainer =
                mDevice.findObject(mFunUiObjects.getShutDownTimeSettingItemContainerSelector());
        UiObject2 itemValue;
        for (int i = 1; i < SUB_VALUES_SHUTDOWN_TV_TIME.length; i++) {
            mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
            itemValue = mTask.getTextViewOfSwitcher(itemContainer);
            Assert.assertEquals(String.format(mMessage, i + 1),
                    SUB_VALUES_SHUTDOWN_TV_TIME[i], itemValue.getText());
        }

        mMessage = "Verify move from last to first value of set shutdown tv time.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        itemValue = mTask.getTextViewOfSwitcher(itemContainer);
        Assert.assertEquals(mMessage, SUB_VALUES_SHUTDOWN_TV_TIME[0], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test18_01ScreenSaverDefaultValue() {
        mTask.scrollMoveToSpecificSettingsItem("屏保");

        mMessage = "Verify the default value of screen saver.";
        UiObject2 screenSaverContainer =
                mDevice.findObject(mFunUiObjects.getScreenSaverSettingItemContainerSelector());
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(screenSaverContainer);
        Assert.assertNotNull(itemValue);
        Assert.assertEquals(mMessage, SUB_VALUES_SCREEN_SAVER[0], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test18_02SelectScreenSaver() {
        mTask.moveToSpecifiedSettingsItem(
                mFunUiObjects.getScreenSaverSettingItemContainerSelector());
        mAction.doChainedDeviceActionAndWait(new DeviceActionMoveLeft())
                .doDeviceActionAndWait(new DeviceActionMoveLeft(), WAIT);

        mMessage = "Verify select the screen saver value.";
        UiObject2 screenSaverContainer =
                mDevice.findObject(mFunUiObjects.getScreenSaverSettingItemContainerSelector());
        UiObject2 itemValue = mTask.getTextViewOfSwitcher(screenSaverContainer);
        Assert.assertNotNull(itemValue);
        Assert.assertEquals(mMessage, SUB_VALUES_SCREEN_SAVER[3], itemValue.getText());

        // reset to default
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 2);
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test18_03ScreenSaverSubValues() {
        mTask.moveToSpecifiedSettingsItem(
                mFunUiObjects.getScreenSaverSettingItemContainerSelector());
        ShellUtils.systemWaitByMillis(SHORT_WAIT);

        mMessage = "Verify the sub values of screen saver at position %d.";
        UiObject2 itemValue;
        for (int i = 1; i < SUB_VALUES_SCREEN_SAVER.length; i++) {
            mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
            itemValue = mTask.getTextViewOfSwitcher(
                    mDevice.findObject(mFunUiObjects.getScreenSaverSettingItemContainerSelector()));
            Assert.assertEquals(String.format(mMessage, (i + 1)),
                    SUB_VALUES_SCREEN_SAVER[i], itemValue.getText());
        }

        mMessage = "Verify screen saver item value when move from last to first.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        UiObject2 itemValueContainer = mDevice.findObject(
                mFunUiObjects.getScreenSaverSettingItemContainerSelector());
        Assert.assertTrue(TestHelper.waitForTextGone(itemValueContainer,
                SUB_VALUES_SCREEN_SAVER[SUB_VALUES_SCREEN_SAVER.length - 1]));
        Assert.assertTrue(mMessage,
                TestHelper.waitForTextVisible(itemValueContainer, SUB_VALUES_SCREEN_SAVER[0]));
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test21InstallUnknownAppDefaultValue() {
        mTask.scrollMoveToSpecificSettingsItem("安装未知来源应用");

        mMessage = "Verify the default value text of install unknown app settings item.";
        UiObject2 installUnknownAppItemContainer = mDevice.findObject(
                mFunUiObjects.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);
        Assert.assertNotNull(valueText);
        Assert.assertEquals(mMessage, TEXT_FORBIDDEN, valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test22AllowedInstallUnknownAppAndCancel() {
        mTask.moveToSpecifiedSettingsItem(
                mFunUiObjects.getInstallUnknownAppSettingItemContainerSelector());

        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        UiObject2 dialogTitle = mDevice.findObject(mFunUiObjects.getTitleOfCommonDialogSelector());
        mMessage = "Verify the common dialog title is enabled.";
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(dialogTitle));
        mMessage = "Verify the text of common dialog title.";
        Assert.assertEquals(mMessage, "安装未知应用", dialogTitle.getText());

        mMessage = "Verify the cancel button on common dialog is enabled.";
        UiObject2 cancelBtn =
                mDevice.findObject(mFunUiObjects.getCancelBtnOfCommonDialogSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(cancelBtn));
        mAction.doClickActionAndWait(cancelBtn);

        mMessage = "Verify the value text of install unknown app settings item.";
        UiObject2 installUnknownAppItemContainer = mDevice.findObject(
                mFunUiObjects.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);
        Assert.assertEquals(mMessage, TEXT_FORBIDDEN, valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test23AllowedInstallUnknownAppAndConfirm() {
        mTask.moveToSpecifiedSettingsItem(
                mFunUiObjects.getInstallUnknownAppSettingItemContainerSelector());

        mMessage = "Verify the confirm button on common dialog is enabled.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        UiObject2 confirmBtn =
                mDevice.findObject(mFunUiObjects.getConfirmBtnOfCommonDialogSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(confirmBtn));
        mAction.doClickActionAndWait(confirmBtn);

        mMessage = "Verify the value text of install unknown app settings item after allowed.";
        UiObject2 installUnknownAppItemContainer = mDevice.findObject(
                mFunUiObjects.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);
        Assert.assertEquals(mMessage, "允许", valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test24ForbiddenInstallUnknownApp() {
        mTask.moveToSpecifiedSettingsItem(
                mFunUiObjects.getInstallUnknownAppSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionMoveLeft(), WAIT);

        mMessage = "Verify the value text of install unknown app settings item after forbidden.";
        UiObject2 installUnknownAppItemContainer = mDevice.findObject(
                mFunUiObjects.getInstallUnknownAppSettingItemContainerSelector());
        UiObject2 valueText = mTask.getTextViewOfSwitcher(installUnknownAppItemContainer);
        Assert.assertEquals(mMessage, TEXT_FORBIDDEN, valueText.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test25SystemRecoverDialogAndClickCancel() {
        mTask.scrollMoveToSpecificSettingsItem(TEXT_SYSTEM_RECOVERY);
        UiObject2 recoverItem =
                mDevice.findObject(mFunUiObjects.getSystemRecoverSettingItemSelector());
        mAction.doClickActionAndWait(recoverItem);  // open dialog

        // verification 1
        mMessage = "Verify the content of recover dialog.";
        UiObject2 title = mDevice.findObject(mFunUiObjects.getSystemRecoverDialogTitleSelector());
        Assert.assertNotNull(title);
        Assert.assertEquals(mMessage, "您的设备将恢复出厂设置", title.getText());

        // verification 2
        mMessage = "Verify the cancel button in recover dialog.";
        UiObject2 cancelBtn =
                mDevice.findObject(mFunUiObjects.getCancelBtnOfSystemRecoverDialogSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(cancelBtn));

        mMessage = "Verify back to common settings page after click the cancel button";
        mAction.doClickActionAndWait(cancelBtn);
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(recoverItem));
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test26SaveInfoOnSystemRecoverDialog() {
        mTask.scrollMoveToSpecificSettingsItem(TEXT_SYSTEM_RECOVERY);
        UiObject2 recoverItem =
                mDevice.findObject(mFunUiObjects.getSystemRecoverSettingItemSelector());
        mAction.doClickActionAndWait(recoverItem);  // open dialog

        // verification 1
        mMessage = "Verify the confirm button is default focused.";
        UiObject2 confirmBtn =
                mDevice.findObject(mFunUiObjects.getConfirmBtnOfSystemRecoverDialogSelector());
        Assert.assertNotNull(confirmBtn);
        Assert.assertTrue(mMessage, confirmBtn.isFocused());

        // verification 2
        UiObject2 saveInfoCheckbox =
                mDevice.findObject(mFunUiObjects.getSaveInfoOfRecoverDialogSelector());
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
                mDevice.findObject(mFunUiObjects.getWallpaperSettingItemContainerSelector());

        mMessage = "Verify the item key for wallpaper setting item.";
        UiObject2 itemKey = itemWallpaper.findObject(mFunUiObjects.getSettingItemKeySelector());
        Assert.assertEquals(mMessage, "壁纸", itemKey.getText());
        mMessage = "Verify the default wallpaper.";
        UiObject2 itemValue = itemWallpaper.findObject(mFunUiObjects.getSettingItemValueSelector());
        Assert.assertEquals(mMessage, SUB_VALUES_WALLPAPER[0], itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test32SubWallpapersOnSelectPage() {
        mTask.moveToSpecifiedSettingsItem(mFunUiObjects.getWallpaperSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        UiObject2 selectedWallpaper;
        for (String wallpaper : SUB_VALUES_WALLPAPER) {
            selectedWallpaper = mDevice.findObject(By.text(wallpaper));
            mMessage = String.format("Verify the sub wallpaper %s is enabled.", wallpaper);
            Assert.assertTrue(mMessage, selectedWallpaper.isEnabled());
            mMessage = String.format("Verify the sub wallpaper %s is selected.", wallpaper);
            Assert.assertTrue(mMessage, selectedWallpaper.getParent().isSelected());

            mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        }
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test33SelectWallpaper() {
        final String selectWallpaper = SUB_VALUES_WALLPAPER[2];

        mTask.moveToSpecifiedSettingsItem(mFunUiObjects.getWallpaperSettingItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        mTask.selectSpecifiedSubWallpaper(selectWallpaper);

        mMessage = "Verify setting item value is changed to the selected wallpaper.";
        mAction.doDeviceActionAndWait(new DeviceActionBack());
        UiObject2 itemWallpaper =
                mDevice.findObject(mFunUiObjects.getWallpaperSettingItemContainerSelector());
        UiObject2 itemValue = itemWallpaper.findObject(mFunUiObjects.getSettingItemValueSelector());
        Assert.assertEquals(mMessage, selectWallpaper, itemValue.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test34InputMethodDefaultValue() {
        UiObject2 itemInput =
                mDevice.findObject(mFunUiObjects.getInputMethodSettingItemContainerSelector());

        mMessage = "Verify the item key for input method setting item.";
        UiObject2 itemKey = itemInput.findObject(mFunUiObjects.getSettingItemKeySelector());
        Assert.assertEquals(mMessage, "输入法", itemKey.getText());

        mMessage = "Verify the default input method.";
        UiObject2 inputMethod = mTask.getTextViewOfSwitcher(itemInput);
        Assert.assertEquals(mMessage, TEXT_INPUT_METHOD, inputMethod.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test35SelectInputMethod() {
        mTask.moveToSpecifiedSettingsItem(
                mFunUiObjects.getInputMethodSettingItemContainerSelector());

        mMessage = "Verify select input method by left key event.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveLeft(), WAIT);
        UiObject2 inputMethod = mTask.getTextViewOfSwitcher(
                mDevice.findObject(mFunUiObjects.getInputMethodSettingItemContainerSelector()));
        Assert.assertEquals(mMessage, TEXT_INPUT_METHOD, inputMethod.getText());

        mMessage = "Verify select input method by right key event.";
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 2);
        ShellUtils.systemWaitByMillis(WAIT);
        inputMethod = mTask.getTextViewOfSwitcher(
                mDevice.findObject(mFunUiObjects.getInputMethodSettingItemContainerSelector()));
        Assert.assertEquals(mMessage, TEXT_INPUT_METHOD, inputMethod.getText());
    }

    @Test
    @Category(CategorySettingsTests.class)
    public void test99ClearUpAfterAllTestCasesDone() {
        mTask.destroyInstance();
        mFunUiObjects.destroyInstance();
    }

}
