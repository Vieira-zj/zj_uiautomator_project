package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryImageAndSoundSettingsTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionCenter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveLeft;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskImageAndSound;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_IMAGE_AND_SOUND_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/12/14.
 * <p>
 * Include test cases for image and sound settings.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestImageAndSoundSettings {

    private UiDevice mDevice;
    private UiActionsManager mAction;
    private TaskImageAndSound mTask;
    private String mMessage;

    private final String IMAGE_AND_SOUND_TEXT = "图像与声音";
    private final String TURN_ON_TEXT = "已开启";
    private final String TURN_OFF_TEXT = "已关闭";
    private final String ENERGY_SAVER_TITLE_TEXT = "节能模式";

    private final String[] IMAGE_AND_SOUND_SETTINGS_TITLE_ARR =
            {"图像参数", "按键音", "开启环绕立体声道"};
    private final String[] IMAGE_PARAMS_SETTINGS_TITLE_ARR =
            {"色温", "背光", "亮度", "对比度", "饱和度", "恢复默认选项"};
    private final String[] IMAGE_PARAMS_SETTINGS_VALUE_ARR =
            {"标准", "10", "50", "50", "50"};

    private final String[] IMAGE_COLOR_TMP_VALUES_ARR = {"暖", "冷", "标准"};

    @BeforeClass
    public static void classSetUp() {
        ShellUtils.stopProcess(SETTINGS_PKG_NAME);
    }

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mTask = TaskImageAndSound.getInstance();

        TaskLauncher.backToLauncher();
        TaskLauncher.openSpecifiedCardFromSettingsTab(IMAGE_AND_SOUND_TEXT);
        Assert.assertTrue(TestHelper.waitForActivityOpenedByShellCmd(
                SETTINGS_PKG_NAME, SETTINGS_IMAGE_AND_SOUND_ACT));
    }

    @After
    public void clearUp() {
        ShellUtils.stopProcess(SETTINGS_PKG_NAME);
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test01TitleOfImageAndSoundSettingsPage() {
        mMessage = "Verify the text of title on Image and Sound settings page.";
        UiObject2 title = mDevice.findObject(mTask.getTitleOfImageAndSoundSettingsSelector());
        Assert.assertEquals(mMessage, IMAGE_AND_SOUND_TEXT, title.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test02TitleAndFocusOfImageParamsSettingItem() {
        mMessage = "Verify the image params setting item is enabled.";
        UiObject2 imageParamsItem = mDevice.findObject(mTask.getImageParamsSettingItemSelector());
        Assert.assertTrue(mMessage, (imageParamsItem != null && imageParamsItem.isEnabled()));

        mMessage = "Verify the image params setting item is default focused.";
        Assert.assertTrue(mMessage, imageParamsItem.isFocused());

        mMessage = "Verify the title of image params setting item.";
        UiObject2 imageParamsTitle =
                imageParamsItem.findObject(mTask.getImageAndSoundSettingItemTitleSelector());
        Assert.assertEquals(mMessage, IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[0]
                , imageParamsTitle.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test03TitleAndDefaultValueOfPressKeySoundSettingItem() {
        mMessage = "Verify the press key sound setting item is enabled.";
        UiObject2 pressKeySoundItem =
                mDevice.findObject(mTask.getPressKeySoundSettingItemSelector());
        Assert.assertTrue(mMessage, (pressKeySoundItem != null && pressKeySoundItem.isEnabled()));

        mMessage = "Verify the title of image params setting item.";
        UiObject2 pressKeySoundTitle =
                pressKeySoundItem.findObject(mTask.getImageAndSoundSettingItemTitleSelector());
        Assert.assertEquals(mMessage, IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[1]
                , pressKeySoundTitle.getText());

        mMessage = "Verify the default value of image params setting item.";
        UiObject2 pressKeySoundValue =
                pressKeySoundItem.findObject(mTask.getImageAndSoundSettingItemValueSelector());
        Assert.assertEquals(mMessage, TURN_OFF_TEXT, pressKeySoundValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test04TurnOnPressKeySoundSettingItem() {
        mTask.focusOnSpecifiedImageAndSoundSettingsItem(IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[1]);

        mMessage = "Verify the text after turn on the press key sound setting item.";
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        UiObject2 pressKeySoundItem =
                mDevice.findObject(mTask.getPressKeySoundSettingItemSelector());
        UiObject2 pressKeySoundValue =
                pressKeySoundItem.findObject(mTask.getImageAndSoundSettingItemValueSelector());
        Assert.assertEquals(mMessage, TURN_ON_TEXT, pressKeySoundValue.getText());

        mMessage = "Verify the text after turn off the press key sound setting item.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveLeft(), WAIT);
        pressKeySoundValue = pressKeySoundItem.findObject(mTask.getImageAndSoundSettingItemValueSelector());
        Assert.assertEquals(mMessage, TURN_OFF_TEXT, pressKeySoundValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test05TitleAndDefaultValueOfAudioAroundSettingItem() {
        mMessage = "Verify the audio around setting item is enabled.";
        UiObject2 audioAroundItem =
                mDevice.findObject(mTask.getAudioAroundSettingItemSelector());
        Assert.assertTrue(mMessage, (audioAroundItem != null && audioAroundItem.isEnabled()));

        mMessage = "Verify the title of audio around setting item.";
        UiObject2 audioAroundTitle =
                audioAroundItem.findObject(mTask.getImageAndSoundSettingItemTitleSelector());
        Assert.assertEquals(mMessage, IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[2]
                , audioAroundTitle.getText());

        mMessage = "Verify the default value of audio around setting item.";
        UiObject2 audioAroundValue =
                audioAroundItem.findObject(mTask.getImageAndSoundSettingItemValueSelector());
        Assert.assertEquals(mMessage, TURN_OFF_TEXT, audioAroundValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test06TurnOnAudioAroundSettingItem() {
        mTask.focusOnSpecifiedImageAndSoundSettingsItem(IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[1]);

        mMessage = "Verify the text after turn on the audio around setting item.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        UiObject2 audioAroundItem =
                mDevice.findObject(mTask.getAudioAroundSettingItemSelector());
        UiObject2 audioAroundValue =
                audioAroundItem.findObject(mTask.getImageAndSoundSettingItemValueSelector());
        Assert.assertEquals(mMessage, TURN_ON_TEXT, audioAroundValue.getText());

        mMessage = "Verify the text after turn off the audio around setting item.";
        mAction.doDeviceActionAndWait(new DeviceActionCenter(), WAIT);
        audioAroundValue = audioAroundItem.findObject(mTask.getImageAndSoundSettingItemValueSelector());
        Assert.assertEquals(mMessage, TURN_OFF_TEXT, audioAroundValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test07EnergySaverSettingItemIsHidden() {
        mMessage = "Verify the energy saver setting item is default hidden on Image and Sound.";
        UiObject2 saverItem = mDevice.findObject(By.text(ENERGY_SAVER_TITLE_TEXT));
        Assert.assertNull(mMessage, saverItem);
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test11TitleAndDefaultValueOfColorTmpOnImageParams() {
        this.openImageParamsPageFromImageAndSound();

        mMessage = "Verify the color tmp setting item is enabled on image params page.";
        UiObject2 colorTmpItem =
                mDevice.findObject(mTask.getColorTmpSettingItemOfImageParamsSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(colorTmpItem));

        mMessage = "Verify the color tmp setting item is default focused.";
        Assert.assertTrue(mMessage, colorTmpItem.isFocused());

        mMessage = "Verify the title of color tmp setting item.";
        UiObject2 itemTitle = colorTmpItem.findObject(mTask.getTitleOfColorTmpSettingSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_TITLE_ARR[0], itemTitle.getText());

        mMessage = "Verify the default value of color tmp setting item.";
        UiObject2 itemValue = mTask.getSwitcherValueOfColorTmpSetting(colorTmpItem);
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_VALUE_ARR[0], itemValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test12SubValuesOfColorTmpOnImageParams() {
        this.openImageParamsPageFromImageAndSound();

        UiObject2 colorTmpItem =
                mDevice.findObject(mTask.getColorTmpSettingItemOfImageParamsSelector());
        TestHelper.waitForUiObjectEnabled(colorTmpItem);

        mMessage = "Verify sub values of color tmp setting item at position %s";
        for (int i = 0, count = 3; i < count; i++) {
            mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 2, WAIT);
            UiObject2 itemValue = mTask.getSwitcherValueOfColorTmpSetting(colorTmpItem);
            Assert.assertEquals(String.format(mMessage, i)
                    , IMAGE_COLOR_TMP_VALUES_ARR[i], itemValue.getText());
        }

        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 2, WAIT);  // do update
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test13TitleAndDefaultValueOfBackLightOnImageParams() {
        this.openImageParamsPageFromImageAndSound();

        mMessage = "Verify the background light setting item is enabled on image params page.";
        UiObject2 backLightItem =
                mDevice.findObject(mTask.getBackLightSettingItemOfImageParamsSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(backLightItem));

        mMessage = "Verify the title of background light setting item.";
        UiObject2 itemTitle =
                backLightItem.findObject(mTask.getTitleOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_TITLE_ARR[1], itemTitle.getText());

        mMessage = "Verify the default value of background light setting item.";
        UiObject2 itemValue =
                backLightItem.findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_VALUE_ARR[1], itemValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test14SetValueOfBackLightOnImageParams() {
        this.openImageParamsPageFromImageAndSound();
        UiObject2 backLightItem =
                mDevice.findObject(mTask.getBackLightSettingItemOfImageParamsSelector());
        TestHelper.waitForUiObjectEnabled(backLightItem);

        mMessage = "Verify text after minus background light value.";
        mTask.focusOnSpecifiedImageParamsSettingsItem(IMAGE_PARAMS_SETTINGS_TITLE_ARR[1]);
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 2);
        UiObject2 itemValue =
                backLightItem.findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("8", itemValue.getText());

        mMessage = "Verify text after plus background light value.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        itemValue = backLightItem.findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("9", itemValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test21TitleAndDefaultValueOfBrightnessOnImageParams() {
        this.openImageParamsPageFromImageAndSound();

        mMessage = "Verify the brightness setting item is enabled on image params page.";
        UiObject2 brightnessItem =
                mDevice.findObject(mTask.getBrightnessSettingItemOfImageParamsSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(brightnessItem));

        mMessage = "Verify the title of brightness setting item.";
        UiObject2 itemTitle =
                brightnessItem.findObject(mTask.getTitleOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_TITLE_ARR[2], itemTitle.getText());

        mMessage = "Verify the default value of brightness setting item.";
        UiObject2 itemValue =
                brightnessItem.findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_VALUE_ARR[2], itemValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test22SetValueOfBrightnessOnImageParams() {
        this.openImageParamsPageFromImageAndSound();
        UiObject2 brightnessItem =
                mDevice.findObject(mTask.getBrightnessSettingItemOfImageParamsSelector());
        TestHelper.waitForUiObjectEnabled(brightnessItem);

        mMessage = "Verify text after minus brightness value.";
        mTask.focusOnSpecifiedImageParamsSettingsItem(IMAGE_PARAMS_SETTINGS_TITLE_ARR[2]);
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 3);
        UiObject2 itemValue =
                brightnessItem.findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("47", itemValue.getText());

        mMessage = "Verify text after plus brightness value.";
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 5);
        itemValue = brightnessItem.findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("52", itemValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test23TitleAndDefaultValueOfContrastOnImageParams() {
        this.openImageParamsPageFromImageAndSound();

        mMessage = "Verify the contrast setting item is enabled on image params page.";
        UiObject2 contrastItem =
                mDevice.findObject(mTask.getContrastSettingItemOfImageParamsSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(contrastItem));

        mMessage = "Verify the title of contrast setting item.";
        UiObject2 itemTitle =
                contrastItem.findObject(mTask.getTitleOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_TITLE_ARR[3], itemTitle.getText());

        mMessage = "Verify the default value of contrast setting item.";
        UiObject2 itemValue =
                contrastItem.findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_VALUE_ARR[3], itemValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test24SetValueOfContrastOnImageParams() {
        final long waitTime = 500L;

        this.openImageParamsPageFromImageAndSound();
        UiObject2 contrastItem =
                mDevice.findObject(mTask.getContrastSettingItemOfImageParamsSelector());
        TestHelper.waitForUiObjectEnabled(contrastItem);

        mMessage = "Verify text after minus contrast value.";
        mTask.focusOnSpecifiedImageParamsSettingsItem(IMAGE_PARAMS_SETTINGS_TITLE_ARR[3]);
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 11, waitTime);
        UiObject2 itemValue =
                contrastItem.findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("39", itemValue.getText());

        mMessage = "Verify text after plus contrast value.";
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 2, waitTime);
        itemValue = contrastItem.findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("41", itemValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test25TitleAndDefaultValueOfSaturationOnImageParams() {
        this.openImageParamsPageFromImageAndSound();

        mMessage = "Verify the saturation setting item is enabled on image params page.";
        UiObject2 saturationItem =
                mDevice.findObject(mTask.getSaturationSettingItemOfImageParamsSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(saturationItem));

        mMessage = "Verify the title of saturation setting item.";
        UiObject2 itemTitle =
                saturationItem.findObject(mTask.getTitleOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_TITLE_ARR[4], itemTitle.getText());

        mMessage = "Verify the default value of saturation setting item.";
        UiObject2 itemValue =
                saturationItem.findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_VALUE_ARR[4], itemValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test26SetValueOfSaturationOnImageParams() {
        final long waitTime = 250L;

        this.openImageParamsPageFromImageAndSound();
        UiObject2 saturationItem =
                mDevice.findObject(mTask.getSaturationSettingItemOfImageParamsSelector());
        TestHelper.waitForUiObjectEnabled(saturationItem);

        mMessage = "Verify text after minus saturationItem value.";
        mTask.focusOnSpecifiedImageParamsSettingsItem(IMAGE_PARAMS_SETTINGS_TITLE_ARR[4]);
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 4, waitTime);
        UiObject2 itemValue =
                saturationItem.findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("46", itemValue.getText());

        mMessage = "Verify text after plus saturationItem value.";
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 8, waitTime);
        itemValue = saturationItem.findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("54", itemValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test27TitleOfResetAllToDefaultOnImageParams() {
        this.openImageParamsPageFromImageAndSound();

        mMessage = "Verify the reset all to default setting item is enabled on image params.";
        UiObject2 resetItem =
                mDevice.findObject(mTask.getResetToDefaultSettingItemOfImageParamsSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(resetItem));

        mMessage = "Verify the title of reset all to default setting item.";
        UiObject2 title = resetItem.findObject(mTask.getTitleOfResetToDefaultSettingItemSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_TITLE_ARR[5], title.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test28ValuesSetToDefaultAfterResetOnImageParams() {
        this.openImageParamsPageFromImageAndSound();
        UiObject2 resetItem =
                mDevice.findObject(mTask.getResetToDefaultSettingItemOfImageParamsSelector());
        TestHelper.waitForUiObjectEnabled(resetItem);

        mTask.focusOnResetToDefaultImageParamsSettingsItem();
        mAction.doDeviceActionAndWait(new DeviceActionCenter(), LONG_WAIT);

        mMessage = "Verify the color tmp value is set to default after reset.";
        UiObject2 colorTmpItem =
                mDevice.findObject(mTask.getColorTmpSettingItemOfImageParamsSelector());
        UiObject2 colorTmpValue = mTask.getSwitcherValueOfColorTmpSetting(colorTmpItem);
        Assert.assertEquals(mMessage, IMAGE_PARAMS_SETTINGS_VALUE_ARR[0], colorTmpValue.getText());

        mMessage = "Verify the background light value is set to default after reset.";
        UiObject2 backLightValue =
                mDevice.findObject(mTask.getBackLightSettingItemOfImageParamsSelector())
                        .findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(mMessage, IMAGE_PARAMS_SETTINGS_VALUE_ARR[1], backLightValue.getText());

        mMessage = "Verify the brightness value is set to default after reset.";
        UiObject2 brightnessValue =
                mDevice.findObject(mTask.getBrightnessSettingItemOfImageParamsSelector())
                        .findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(mMessage,
                IMAGE_PARAMS_SETTINGS_VALUE_ARR[2], brightnessValue.getText());

        mMessage = "Verify the contrast value is set to default after reset.";
        UiObject2 contrastValue =
                mDevice.findObject(mTask.getContrastSettingItemOfImageParamsSelector())
                        .findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(mMessage, IMAGE_PARAMS_SETTINGS_VALUE_ARR[3], contrastValue.getText());

        mMessage = "Verify the saturation value is set to default after reset.";
        UiObject2 saturationValue =
                mDevice.findObject(mTask.getSaturationSettingItemOfImageParamsSelector())
                        .findObject(mTask.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(mMessage,
                IMAGE_PARAMS_SETTINGS_VALUE_ARR[4], saturationValue.getText());
    }

    private void openImageParamsPageFromImageAndSound() {
        mTask.focusOnSpecifiedImageAndSoundSettingsItem(IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[0]);
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), TestConstants.LONG_WAIT);
    }

}
