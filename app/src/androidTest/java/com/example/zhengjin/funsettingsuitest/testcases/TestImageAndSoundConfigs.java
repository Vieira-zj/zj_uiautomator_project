package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryImageSoundConfigsTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryVersion30;
import com.example.zhengjin.funsettingsuitest.testrunner.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceAction;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionCenter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveLeft;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiobjects.UiObjectsImageAndSound;
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
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/12/14.
 * <p>
 * Include test cases for image and sound settings.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestImageAndSoundConfigs extends TestCaseBase {

    private final UiObjectsImageAndSound mFunUiObjects = UiObjectsImageAndSound.getInstance();
    private final TaskImageAndSound mTask = TaskImageAndSound.getInstance();

    private final String TURN_ON_TEXT = "已开启";
    private final String TURN_OFF_TEXT = "已关闭";

    private final String[] IMAGE_AND_SOUND_SETTINGS_TITLE_ARR =
            {"默认播放清晰度", "图像参数", "按键音", "环绕立体声道", "节能模式", "CEC遥控"};
    private final String[] IMAGE_PARAMS_SETTINGS_TITLE_ARR =
            {"色温", "背光", "亮度", "对比度", "饱和度", "恢复默认选项"};
    private final String[] IMAGE_PARAMS_SETTINGS_VALUE_ARR =
            {"标准", "10", "50", "50", "50"};
    private final String[] IMAGE_COLOR_TMP_VALUES_ARR = {"暖", "冷", "标准"};

    private String mMessage;

    @BeforeClass
    public static void classSetUp() {
        ShellUtils.stopProcessByPackageName(SETTINGS_PKG_NAME);
    }

    @Before
    public void setUp() {
        TaskLauncher.backToLauncher();
        mTask.openImageAndSoundSettingsPage();
    }

    @After
    public void clearUp() {
        ShellUtils.stopProcessByPackageName(SETTINGS_PKG_NAME);
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test01TitleOfImageAndSoundSettingsPage() {
        mMessage = "Verify the text of title on Image and Sound settings page.";
        UiObject2 title =
                mDevice.findObject(mFunUiObjects.getTitleOfImageAndSoundSettingsSelector());
        Assert.assertEquals(mMessage, mTask.IMAGE_AND_SOUND_TEXT, title.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test02TitleOfImageParamsSettingItem() {
        mMessage = "Verify the image params setting item is enabled.";
        UiObject2 imageParamsItem =
                mDevice.findObject(mFunUiObjects.getImageParamsSettingItemSelector());
        Assert.assertTrue(mMessage, (imageParamsItem != null && imageParamsItem.isEnabled()));

        mMessage = "Verify the title of image params setting item.";
        UiObject2 imageParamsTitle = imageParamsItem.findObject(
                mFunUiObjects.getImageAndSoundSettingItemTitleSelector());
        Assert.assertEquals(mMessage, IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[1],
                imageParamsTitle.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test03TitleAndDefaultValueOfPressKeySoundSettingItem() {
        mMessage = "Verify the press key sound setting item is enabled.";
        UiObject2 pressKeySoundItem =
                mDevice.findObject(mFunUiObjects.getPressKeySoundSettingItemSelector());
        Assert.assertTrue(mMessage, (pressKeySoundItem != null && pressKeySoundItem.isEnabled()));

        mMessage = "Verify the title of image params setting item.";
        UiObject2 pressKeySoundTitle = pressKeySoundItem.findObject(
                mFunUiObjects.getImageAndSoundSettingItemTitleSelector());
        Assert.assertEquals(mMessage, IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[2],
                pressKeySoundTitle.getText());

        mMessage = "Verify the default value of image params setting item.";
        UiObject2 pressKeySoundValue = pressKeySoundItem.findObject(
                mFunUiObjects.getImageAndSoundSettingItemValueSelector());
        String expected = RunnerProfile.isVersion30 ? TURN_ON_TEXT : TURN_OFF_TEXT;
        Assert.assertEquals(mMessage, expected, pressKeySoundValue.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test04TurnOffPressKeySoundSettingItem() {
        mTask.focusOnSpecifiedImageAndSoundSettingsItem(IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[2]);

        mMessage = "Verify the text after turn off the press key sound setting item.";
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        UiObject2 pressKeySoundItem =
                mDevice.findObject(mFunUiObjects.getPressKeySoundSettingItemSelector());
        UiObject2 pressKeySoundValue = pressKeySoundItem.findObject(
                mFunUiObjects.getImageAndSoundSettingItemValueSelector());
        String expected = RunnerProfile.isVersion30 ? TURN_OFF_TEXT : TURN_ON_TEXT;
        Assert.assertEquals(mMessage, expected, pressKeySoundValue.getText());

        mMessage = "Verify the text after turn on the press key sound setting item.";
        mAction.doDeviceActionAndWait(new DeviceActionCenter(), WAIT);
        pressKeySoundValue = pressKeySoundItem.findObject(
                mFunUiObjects.getImageAndSoundSettingItemValueSelector());
        expected = RunnerProfile.isVersion30 ? TURN_ON_TEXT : TURN_OFF_TEXT;
        Assert.assertEquals(mMessage, expected, pressKeySoundValue.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test05TitleAndDefaultValueOfAudioAroundSettingItem() {
        mMessage = "Verify the audio around setting item is enabled.";
        UiObject2 audioAroundItem =
                mDevice.findObject(mFunUiObjects.getAudioAroundSettingItemSelector());
        Assert.assertTrue(mMessage, (audioAroundItem != null && audioAroundItem.isEnabled()));

        mMessage = "Verify the title of audio around setting item.";
        UiObject2 audioAroundTitle = audioAroundItem.findObject(
                mFunUiObjects.getImageAndSoundSettingItemTitleSelector());
        Assert.assertEquals(mMessage, IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[3],
                audioAroundTitle.getText());

        mMessage = "Verify the default value of audio around setting item.";
        UiObject2 audioAroundValue = audioAroundItem.findObject(
                mFunUiObjects.getImageAndSoundSettingItemValueSelector());
        Assert.assertEquals(mMessage, TURN_OFF_TEXT, audioAroundValue.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test06TurnOnAudioAroundSettingItem() {
        mTask.focusOnSpecifiedImageAndSoundSettingsItem(IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[3]);

        mMessage = "Verify the text after turn on the audio around setting item.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        UiObject2 audioAroundItem =
                mDevice.findObject(mFunUiObjects.getAudioAroundSettingItemSelector());
        UiObject2 audioAroundValue = audioAroundItem.findObject(
                mFunUiObjects.getImageAndSoundSettingItemValueSelector());
        Assert.assertEquals(mMessage, TURN_ON_TEXT, audioAroundValue.getText());

        mMessage = "Verify the text after turn off the audio around setting item.";
        mAction.doDeviceActionAndWait(new DeviceActionCenter(), WAIT);
        audioAroundValue = audioAroundItem.findObject(
                mFunUiObjects.getImageAndSoundSettingItemValueSelector());
        Assert.assertEquals(mMessage, TURN_OFF_TEXT, audioAroundValue.getText());
    }

    @Test
    @Category({CategoryImageSoundConfigsTests.class, CategoryVersion30.class})
    public void test07ShowSavePowerModeHiddenItem() {
        mMessage = "Verify the image params setting item is default focused.";
        UiObject2 imageParamsItem =
                mDevice.findObject(mFunUiObjects.getImageParamsSettingItemSelector());
        Assert.assertTrue(mMessage, imageParamsItem.isFocused());

        mMessage = "Verify save power mode item is default hidden.";
        UiObject2 savePowerItem = mDevice.findObject(
                mFunUiObjects.getSavePowerModeSettingItemSelector());
        Assert.assertEquals(mMessage, null, savePowerItem);

        mAction.doMultipleDeviceActionsAndWait(new DeviceAction[]{
                new DeviceActionMoveLeft(), new DeviceActionMoveLeft(),
                new DeviceActionMoveUp(), new DeviceActionMoveLeft()}, 500L);

        mMessage = "Verify save power mode hidden item is shown after shorten keys.";
        savePowerItem = mDevice.findObject(
                mFunUiObjects.getSavePowerModeSettingItemSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(savePowerItem));

        mMessage = "Verify save power mode settings item title.";
        UiObject2 itemTitle = savePowerItem.findObject(
                mFunUiObjects.getImageAndSoundSettingItemTitleSelector());
        Assert.assertEquals(mMessage, IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[4], itemTitle.getText());

        mMessage = "Verify save power mode settings item value.";
        UiObject2 itemValue = savePowerItem.findObject(
                mFunUiObjects.getImageAndSoundSettingItemValueSelector());
        Assert.assertEquals(mMessage, TURN_ON_TEXT, itemValue.getText());
    }

    @Test
    @Category({CategoryImageSoundConfigsTests.class, CategoryVersion30.class})
    public void test08TitleAndValueOfCECRemoteControl() {
        mMessage = "Verify CEC remote control setting item is enabled.";
        UiObject2 cecControl = mDevice.findObject(
                mFunUiObjects.getCECRemoteControlSettingItemSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(cecControl));

        mMessage = "Verify CEC remote control setting item title.";
        UiObject2 itemTitle = cecControl.findObject(
                mFunUiObjects.getImageAndSoundSettingItemTitleSelector());
        Assert.assertEquals(mMessage, IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[5], itemTitle.getText());

        mMessage = "Verify CEC remote control setting item value.";
        UiObject2 itemValue = cecControl.findObject(
                mFunUiObjects.getImageAndSoundSettingItemValueSelector());
        Assert.assertEquals(mMessage, TURN_OFF_TEXT, itemValue.getText());
    }

    @Test
    @Category({CategoryImageSoundConfigsTests.class, CategoryVersion30.class})
    public void test09TitleAndValueOfDefaultPlayClarity() {
        mMessage = "Verify the default play clarity setting item is enabled.";
        UiObject2 playClarityItem =
                mDevice.findObject(mFunUiObjects.getDefaultPlayClaritySettingItemSelector());
        Assert.assertTrue(mMessage, (playClarityItem != null && playClarityItem.isEnabled()));

        mMessage = "Verify the title of default play clarity setting item.";
        UiObject2 playClarityTitle = playClarityItem.findObject(
                mFunUiObjects.getImageAndSoundSettingItemTitleSelector());
        Assert.assertEquals(mMessage, IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[0],
                playClarityTitle.getText());

        mMessage = "Verify the value of default play clarity setting item.";
        UiObject2 playClarityValue = mTask.getSwitcherValueOfColorTmpSetting(playClarityItem);
        Assert.assertEquals(mMessage, "超清", playClarityValue.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test11TitleAndDefaultValueOfColorTmpOnImageParams() {
        this.openImageParamsPageFromImageAndSound();

        mMessage = "Verify the color tmp setting item is enabled on image params page.";
        UiObject2 colorTmpItem =
                mDevice.findObject(mFunUiObjects.getColorTmpSettingItemOfImageParamsSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(colorTmpItem));

        mMessage = "Verify the color tmp setting item is default focused.";
        Assert.assertTrue(mMessage, colorTmpItem.isFocused());

        mMessage = "Verify the title of color tmp setting item.";
        UiObject2 itemTitle =
                colorTmpItem.findObject(mFunUiObjects.getTitleOfColorTmpSettingSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_TITLE_ARR[0], itemTitle.getText());

        mMessage = "Verify the default value of color tmp setting item.";
        UiObject2 itemValue = mTask.getSwitcherValueOfColorTmpSetting(colorTmpItem);
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_VALUE_ARR[0], itemValue.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test12SubValuesOfColorTmpOnImageParams() {
        this.openImageParamsPageFromImageAndSound();

        UiObject2 colorTmpItem =
                mDevice.findObject(mFunUiObjects.getColorTmpSettingItemOfImageParamsSelector());
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
    @Category(CategoryImageSoundConfigsTests.class)
    public void test13TitleAndDefaultValueOfBackLightOnImageParams() {
        this.openImageParamsPageFromImageAndSound();

        mMessage = "Verify the background light setting item is enabled on image params page.";
        UiObject2 backLightItem =
                mDevice.findObject(mFunUiObjects.getBackLightSettingItemOfImageParamsSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(backLightItem));

        mMessage = "Verify the title of background light setting item.";
        UiObject2 itemTitle = backLightItem.findObject(
                mFunUiObjects.getTitleOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_TITLE_ARR[1], itemTitle.getText());

        mMessage = "Verify the default value of background light setting item.";
        UiObject2 itemValue = backLightItem.findObject(
                mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_VALUE_ARR[1], itemValue.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test14SetValueOfBackLightOnImageParams() {
        this.openImageParamsPageFromImageAndSound();
        UiObject2 backLightItem =
                mDevice.findObject(mFunUiObjects.getBackLightSettingItemOfImageParamsSelector());
        TestHelper.waitForUiObjectEnabled(backLightItem);

        mMessage = "Verify text after minus background light value.";
        mTask.focusOnSpecifiedImageParamsSettingsItem(IMAGE_PARAMS_SETTINGS_TITLE_ARR[1]);
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 2);
        UiObject2 itemValue = backLightItem.findObject(
                mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("8", itemValue.getText());

        mMessage = "Verify text after plus background light value.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        itemValue = backLightItem.findObject(
                mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("9", itemValue.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test21TitleAndDefaultValueOfBrightnessOnImageParams() {
        this.openImageParamsPageFromImageAndSound();

        mMessage = "Verify the brightness setting item is enabled on image params page.";
        UiObject2 brightnessItem =
                mDevice.findObject(mFunUiObjects.getBrightnessSettingItemOfImageParamsSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(brightnessItem));

        mMessage = "Verify the title of brightness setting item.";
        UiObject2 itemTitle = brightnessItem.findObject(
                mFunUiObjects.getTitleOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_TITLE_ARR[2], itemTitle.getText());

        mMessage = "Verify the default value of brightness setting item.";
        UiObject2 itemValue = brightnessItem.findObject(
                mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_VALUE_ARR[2], itemValue.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test22SetValueOfBrightnessOnImageParams() {
        this.openImageParamsPageFromImageAndSound();
        UiObject2 brightnessItem =
                mDevice.findObject(mFunUiObjects.getBrightnessSettingItemOfImageParamsSelector());
        TestHelper.waitForUiObjectEnabled(brightnessItem);

        mMessage = "Verify text after minus brightness value.";
        mTask.focusOnSpecifiedImageParamsSettingsItem(IMAGE_PARAMS_SETTINGS_TITLE_ARR[2]);
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 3);
        UiObject2 itemValue = brightnessItem.findObject(
                mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("47", itemValue.getText());

        mMessage = "Verify text after plus brightness value.";
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 5);
        itemValue = brightnessItem.findObject(
                mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("52", itemValue.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test23TitleAndDefaultValueOfContrastOnImageParams() {
        this.openImageParamsPageFromImageAndSound();

        mMessage = "Verify the contrast setting item is enabled on image params page.";
        UiObject2 contrastItem =
                mDevice.findObject(mFunUiObjects.getContrastSettingItemOfImageParamsSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(contrastItem));

        mMessage = "Verify the title of contrast setting item.";
        UiObject2 itemTitle = contrastItem.findObject(
                mFunUiObjects.getTitleOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_TITLE_ARR[3], itemTitle.getText());

        mMessage = "Verify the default value of contrast setting item.";
        UiObject2 itemValue = contrastItem.findObject(
                mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_VALUE_ARR[3], itemValue.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test24SetValueOfContrastOnImageParams() {
        final long waitTime = 500L;

        this.openImageParamsPageFromImageAndSound();
        UiObject2 contrastItem =
                mDevice.findObject(mFunUiObjects.getContrastSettingItemOfImageParamsSelector());
        TestHelper.waitForUiObjectEnabled(contrastItem);

        mMessage = "Verify text after minus contrast value.";
        mTask.focusOnSpecifiedImageParamsSettingsItem(IMAGE_PARAMS_SETTINGS_TITLE_ARR[3]);
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 11, waitTime);
        UiObject2 itemValue = contrastItem.findObject(
                mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("39", itemValue.getText());

        mMessage = "Verify text after plus contrast value.";
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 2, waitTime);
        itemValue = contrastItem.findObject(
                mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("41", itemValue.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test25TitleAndDefaultValueOfSaturationOnImageParams() {
        this.openImageParamsPageFromImageAndSound();

        mMessage = "Verify the saturation setting item is enabled on image params page.";
        UiObject2 saturationItem =
                mDevice.findObject(mFunUiObjects.getSaturationSettingItemOfImageParamsSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(saturationItem));

        mMessage = "Verify the title of saturation setting item.";
        UiObject2 itemTitle = saturationItem.findObject(
                mFunUiObjects.getTitleOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_TITLE_ARR[4], itemTitle.getText());

        mMessage = "Verify the default value of saturation setting item.";
        UiObject2 itemValue = saturationItem.findObject(
                mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_VALUE_ARR[4], itemValue.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test26SetValueOfSaturationOnImageParams() {
        final long waitTime = 250L;

        this.openImageParamsPageFromImageAndSound();
        UiObject2 saturationItem =
                mDevice.findObject(mFunUiObjects.getSaturationSettingItemOfImageParamsSelector());
        TestHelper.waitForUiObjectEnabled(saturationItem);

        mMessage = "Verify text after minus saturationItem value.";
        mTask.focusOnSpecifiedImageParamsSettingsItem(IMAGE_PARAMS_SETTINGS_TITLE_ARR[4]);
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 4, waitTime);
        UiObject2 itemValue = saturationItem.findObject(
                mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("46", itemValue.getText());

        mMessage = "Verify text after plus saturationItem value.";
        mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 8, waitTime);
        itemValue = saturationItem.findObject(
                mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals("54", itemValue.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test27TitleOfResetAllToDefaultOnImageParams() {
        this.openImageParamsPageFromImageAndSound();

        mMessage = "Verify the reset all to default setting item is enabled on image params.";
        UiObject2 resetItem = mDevice.findObject(
                mFunUiObjects.getResetToDefaultSettingItemOfImageParamsSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(resetItem));

        mMessage = "Verify the title of reset all to default setting item.";
        UiObject2 title = resetItem.findObject(
                mFunUiObjects.getTitleOfResetToDefaultSettingItemSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_TITLE_ARR[5], title.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test28ValuesSetToDefaultAfterResetOnImageParams() {
        this.openImageParamsPageFromImageAndSound();
        UiObject2 resetItem = mDevice.findObject(
                mFunUiObjects.getResetToDefaultSettingItemOfImageParamsSelector());
        TestHelper.waitForUiObjectEnabled(resetItem);

        mTask.focusOnResetToDefaultImageParamsSettingsItem();
        mAction.doDeviceActionAndWait(new DeviceActionCenter(), LONG_WAIT);

        mMessage = "Verify the color tmp value is set to default after reset.";
        UiObject2 colorTmpItem =
                mDevice.findObject(mFunUiObjects.getColorTmpSettingItemOfImageParamsSelector());
        UiObject2 colorTmpValue = mTask.getSwitcherValueOfColorTmpSetting(colorTmpItem);
        Assert.assertEquals(mMessage, IMAGE_PARAMS_SETTINGS_VALUE_ARR[0], colorTmpValue.getText());

        mMessage = "Verify the background light value is set to default after reset.";
        UiObject2 backLightValue =
                mDevice.findObject(mFunUiObjects.getBackLightSettingItemOfImageParamsSelector())
                        .findObject(mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(mMessage, IMAGE_PARAMS_SETTINGS_VALUE_ARR[1], backLightValue.getText());

        mMessage = "Verify the brightness value is set to default after reset.";
        UiObject2 brightnessValue =
                mDevice.findObject(mFunUiObjects.getBrightnessSettingItemOfImageParamsSelector())
                        .findObject(mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(mMessage,
                IMAGE_PARAMS_SETTINGS_VALUE_ARR[2], brightnessValue.getText());

        mMessage = "Verify the contrast value is set to default after reset.";
        UiObject2 contrastValue =
                mDevice.findObject(mFunUiObjects.getContrastSettingItemOfImageParamsSelector())
                        .findObject(mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(mMessage, IMAGE_PARAMS_SETTINGS_VALUE_ARR[3], contrastValue.getText());

        mMessage = "Verify the saturation value is set to default after reset.";
        UiObject2 saturationValue =
                mDevice.findObject(mFunUiObjects.getSaturationSettingItemOfImageParamsSelector())
                        .findObject(mFunUiObjects.getValueOfImageSettingsOnImageParamsSelector());
        Assert.assertEquals(mMessage,
                IMAGE_PARAMS_SETTINGS_VALUE_ARR[4], saturationValue.getText());
    }

    @Test
    @Category(CategoryImageSoundConfigsTests.class)
    public void test99ClearUpAfterAllTestCasesDone() {
        TaskImageAndSound.destroyInstance();
        UiObjectsImageAndSound.destroyInstance();
    }

    private void openImageParamsPageFromImageAndSound() {
        mTask.focusOnSpecifiedImageAndSoundSettingsItem(IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[1]);
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), TestConstants.LONG_WAIT);
    }

}
