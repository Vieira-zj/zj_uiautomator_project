package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.FileManagerTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionBack;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskFileManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;

/**
 * Created by zhengjin on 2016/6/7.
 *
 * Include the test cases for file manager APP.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestFileManager {

    private static UiActionsManager ACTION = UiActionsManager.getInstance();
    private UiDevice mDevice;

    @Before
    public void setUp() {

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        String appName = "文件管理";
        TaskLauncher.openSpecifiedApp(mDevice, appName);
        ShellUtils.systemWait(LONG_WAIT);
    }

    @After
    public void clearUp() {
        int repeatTimes = 3;
        ACTION.doRepeatUiActionAndWait(mDevice, new UiActionBack(), repeatTimes);
    }

    // Error: ddmlib.SyncException: Remote object doesn't exist!
    @Ignore
    @Category(FileManagerTests.class)
    public void test1SdcardTabNameAndFocused() {

        String tabContainerId = "tv.fun.filemanager:id/activity_fun_fm_tab";
        UiObject2 tabContainer = mDevice.findObject(By.res(tabContainerId));
        Assert.assertNotNull(tabContainer);

        String message = "Verify the sdcard tab name and is focused.";
        Assert.assertTrue(message, tabContainer.isFocused());
    }

    // Error: ddmlib.SyncException: Remote object doesn't exist!
    @Ignore
    @Category(FileManagerTests.class)
    public void test2VideoCardNameAndItemCount() {

        String videoCardId = "category_video";
        UiObject2 videoCard = mDevice.findObject(By.res(videoCardId));
        Assert.assertNotNull(videoCard);

        String message = "Verify the video card name.";
        String textId = "entity_name";
        String expectedText = "视频";
        UiObject2 videoCardText = videoCard.findObject(By.res(textId));
        Assert.assertNotNull(videoCardText);
        Assert.assertEquals(message, expectedText, videoCardText.getText());

        message = "Verify the video card count.";
        String countId = "entity_count";
        expectedText = "(0项)";
        UiObject2 videoCardCount = videoCard.findObject(By.res(countId));
        Assert.assertNotNull(videoCardCount);
        Assert.assertEquals(message, expectedText, videoCardCount.getText());
    }

    @Test
    @Category(FileManagerTests.class)
    public void test3OpenSdcardAllFilesCard() {

        TaskFileManager.openSdcardLocalFilesCard(mDevice);

        // verification 1
        UiObject2 mainTitle = mDevice.findObject(By.res(TaskFileManager.getMainTitleId()));
        String expectedText = "本地存储";
        String message = "Verify the text of main title from sdcard local files.";
        Assert.assertEquals(message, expectedText, mainTitle.getText());

        // verification 2
        UiObject2 subTitle = mDevice.findObject(By.res(TaskFileManager.getSubTitleId()));
        expectedText = "全部文件";
        message = "Verify the text of sub title from sdcard local files.";
        Assert.assertEquals(message, expectedText, subTitle.getText());
    }

    @Test
    @Category(FileManagerTests.class)
    public void test4NavigateToSpecifiedPath() {

        TaskFileManager.openSdcardLocalFilesCard(mDevice);

        String path = "/testfiles/testpics";
        TaskFileManager.navigateAndOpenSpecifiedFile(mDevice, path);

        UiObject2 subTitle = mDevice.findObject(By.res(TaskFileManager.getSubTitleId()));
        String expectedText = "testpics";
        String message = "Verify navigate to the specified path.";
        Assert.assertEquals(message, expectedText, subTitle.getText());
    }

    @Test
    @Category(FileManagerTests.class)
    public void test5OpenSpecifiedPicture() {

        TaskFileManager.openSdcardLocalFilesCard(mDevice);

        String filePath = "/testfiles/testpics/";
        String fileName = "4800x3600_5.jpg";
        TaskFileManager.navigateAndOpenSpecifiedFile(mDevice, filePath + fileName);

        String titleId = "tv.fun.filemanager:id/image_name_display";
        UiObject2 fileTitle = mDevice.findObject(By.res(titleId));

        String message = "Verify the specified picture is opened.";
        Assert.assertEquals(message, fileName, fileTitle.getText());
    }
}
