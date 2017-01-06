package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

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

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_ABOUT_INFO_ACT;
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

    private final String ABOUT_INFO_PAGE_TEXT = "关于";

    @BeforeClass
    public static void classSetUp() {
        ShellUtils.stopProcess(SETTINGS_PKG_NAME);
    }

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mTask = TaskAboutInfo.getInstance();

        TaskLauncher.backToLauncher();
        TaskLauncher.openSpecifiedCardFromSettingsTab(ABOUT_INFO_PAGE_TEXT);
        Assert.assertTrue(TestHelper.waitForActivityOpenedByShellCmd(
                SETTINGS_PKG_NAME, SETTINGS_ABOUT_INFO_ACT));
    }

    @After
    public void clearUp() {
        ShellUtils.stopProcess(SETTINGS_PKG_NAME);
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test01AboutInfoPageTitle() {
        UiObject2 pageTitle = mDevice.findObject(mTask.getSettingsAboutInfoPageTitleSelector());

        mMessage = "Verify the About Info page title is enabled.";
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(pageTitle));
        mMessage = "Verify the title text on About Info page.";
        Assert.assertEquals(mMessage, ABOUT_INFO_PAGE_TEXT, pageTitle.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test02ProductInfoItem() {
        // TODO: 2017/1/6  
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test03ProductInfoSubPage() {
        // TODO: 2017/1/6
    }


    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test99ClearUpAfterAllTestCasesDone() {
        mTask.destroyInstance();
    }

}
