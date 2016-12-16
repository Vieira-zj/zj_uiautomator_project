package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryImageAndSoundSettingsTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskImageAndSound;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
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

    private final String TEXT_IMAGE_AND_SOUND = "图像与声音";

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mTask = TaskImageAndSound.getInstance();

        TaskLauncher.backToLauncher();
        TaskLauncher.openSpecifiedCardFromSettingsTab(TEXT_IMAGE_AND_SOUND);
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
        mMessage = "Verify the text of Image and Sound settings page title.";
        UiObject2 title = mDevice.findObject(mTask.getTitleOfImageAndSoundSettingsSelector());
        Assert.assertEquals(TEXT_IMAGE_AND_SOUND, title.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test02TitleAndDefaultValueOfItemsOnImageAndSound() {
        mMessage = "Verify the setting items on Image and Sound settings page.";
        // TODO: 2016/12/16  
    }

}
