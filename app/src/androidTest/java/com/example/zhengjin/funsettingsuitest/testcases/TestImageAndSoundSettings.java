package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryImageAndSoundSettingsTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
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

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_IMAGE_AND_SOUND_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;

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

    private final String[] IMAGE_AND_SOUND_SETTINGS_TITLE_ARR =
            {"图像参数", "按键音", "开启环绕立体声道"};
    private final String[] IMAGE_PARAMS_SETTINGS_TITLE_ARR =
            {"色温", "背光", "亮度", "对比度", "饱和度", "恢复默认选项"};
    private final String[] IMAGE_COLOR_TMP_VALUES_ARR = {"标准", "暖", "冷"};

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
//        ShellUtils.stopProcess(SETTINGS_PKG_NAME);
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
    public void test02TitleAndDefaultValueOfImageParamsSettingItem() {
        mMessage = "Verify the image params setting item is enabled.";
        UiObject2 imageParamsItem = mDevice.findObject(mTask.getImageParamsSettingItemSelector());
        Assert.assertTrue(mMessage, (imageParamsItem != null && imageParamsItem.isEnabled()));

        mMessage = "Verify the image params setting item is default focused.";
        Assert.assertTrue(mMessage, imageParamsItem.isFocused());

        mMessage = "Verify the title of image params setting item.";
        UiObject2 imageParamsTitle =
                imageParamsItem.findObject(mTask.getSettingItemTitleSelector());
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
                pressKeySoundItem.findObject(mTask.getSettingItemTitleSelector());
        Assert.assertEquals(mMessage, IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[1]
                , pressKeySoundTitle.getText());

        mMessage = "Verify the default value of image params setting item.";
        UiObject2 pressKeySoundValue =
                pressKeySoundItem.findObject(mTask.getSettingItemValueSelector());
        Assert.assertEquals(mMessage, TURN_OFF_TEXT, pressKeySoundValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test04TurnOnPressKeySoundSettingItem() {
        // TODO: 2016/12/16  
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
                audioAroundItem.findObject(mTask.getSettingItemTitleSelector());
        Assert.assertEquals(mMessage, IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[2]
                , audioAroundTitle.getText());

        mMessage = "Verify the default value of audio around setting item.";
        UiObject2 audioAroundValue =
                audioAroundItem.findObject(mTask.getSettingItemValueSelector());
        Assert.assertEquals(mMessage, TURN_OFF_TEXT, audioAroundValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test06TurnOnAudioAroundSettingItem() {
        // TODO: 2016/12/16  
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test11TitleAndDefaultValueOfColorTmpOnImageParams() {
        // TODO: 2016/12/16  
        mTask.focusOnSpecifiedImageAndSoundSettingsItem(IMAGE_AND_SOUND_SETTINGS_TITLE_ARR[0]);
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), TestConstants.LONG_WAIT);

        UiObject2 colorTmpItem =
                mDevice.findObject(mTask.getColorTmpSettingItemOfImageParamsSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(colorTmpItem));

        UiObject2 itemTitle = colorTmpItem.findObject(mTask.getTitleOfColorTmpSettingSelector());
        Assert.assertEquals(IMAGE_PARAMS_SETTINGS_TITLE_ARR[0], itemTitle.getText());

        UiObject2 itemValue = mTask.getSwitcherValueOfColorTmpSetting(colorTmpItem);
        Assert.assertEquals(IMAGE_COLOR_TMP_VALUES_ARR[0], itemValue.getText());
    }

}
