package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryAboutInfoTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryImageAndSoundSettingsTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskAboutInfo;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
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
 * Created by zhengjin on 2017/1/5.
 * <p>
 * Include test cases for system information page.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestAboutInfoPage {

    private UiDevice mDevice;
    private UiActionsManager mAction;
    private TaskAboutInfo mTask;
    private String mMessage;

    @BeforeClass
    public static void classSetUp() {
        ShellUtils.stopProcess("");
    }

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mTask = TaskAboutInfo.getInstance();

        TaskLauncher.backToLauncher();
        TaskLauncher.openSpecifiedCardFromSettingsTab("");
        Assert.assertTrue(TestHelper.waitForActivityOpenedByShellCmd(
                SETTINGS_PKG_NAME, SETTINGS_IMAGE_AND_SOUND_ACT));
    }

    @After
    public void clearUp() {
        ShellUtils.stopProcess("");
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test01AboutInfoPageTitle() {
        // TODO: 2017/1/5  
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test99ClearUpAfterAllTestCasesDone() {
        mTask.destroyInstance();
    }

}
