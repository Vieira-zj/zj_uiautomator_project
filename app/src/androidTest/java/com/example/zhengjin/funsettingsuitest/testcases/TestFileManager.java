package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryFileManagerTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionBack;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskFileManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.FILE_MANAGER_PKG;

/**
 * Created by zhengjin on 2016/6/7.
 *
 * Include the test cases for file manager APP.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestFileManager {

    private static UiActionsManager action = UiActionsManager.getInstance();
    private UiDevice mDevice;

    @Before
    public void setUp() {

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        String appName = "文件管理";
        TaskLauncher.openSpecifiedAppFromAppTab(mDevice, appName);
    }

    @After
    public void clearUp() {
        int repeatTimes = 3;
        action.doRepeatDeviceActionAndWait(new DeviceActionBack(), repeatTimes);
    }

    // Error: ddmlib.SyncException: Remote object doesn't exist!
    @Ignore
    @Category(CategoryFileManagerTests.class)
    public void test11SdcardTabNameAndFocused() {

        String tabContainerId = "tv.fun.filemanager:id/activity_fun_fm_tab";
        UiObject2 tabContainer = mDevice.findObject(By.res(tabContainerId));
        Assert.assertNotNull(tabContainer);

        String message = "Verify the sdcard tab name and is focused.";
        Assert.assertTrue(message, tabContainer.isFocused());
    }

    // Error: ddmlib.SyncException: Remote object doesn't exist!
    @Ignore
    @Category(CategoryFileManagerTests.class)
    public void test12VideoCardNameAndItemCount() {

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
    @Category(CategoryFileManagerTests.class)
    public void test13OpenSdcardAllFilesCard() {

        TaskFileManager.openLocalFilesCard(mDevice);

        // verification 1
        UiObject2 mainTitle = mDevice.findObject(TaskFileManager.getMainTitleSelector());
        String expectedText = "本地存储";
        String message = "Verify the text of main title from sdcard local files.";
        Assert.assertEquals(message, expectedText, mainTitle.getText());

        // verification 2
        UiObject2 subTitle = mDevice.findObject(TaskFileManager.getSubTitleSelector());
        expectedText = "全部文件";
        message = "Verify the text of sub title from sdcard local files.";
        Assert.assertEquals(message, expectedText, subTitle.getText());
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test14NavigateToSpecifiedPath() {

        TaskFileManager.openLocalFilesCard(mDevice);

        String path = "/testfiles/testpics";
        TaskFileManager.navigateAndOpenSpecifiedFile(mDevice, path);

        UiObject2 subTitle = mDevice.findObject(TaskFileManager.getSubTitleSelector());
        String expectedText = "testpics";
        String message = "Verify navigate to the specified path.";
        Assert.assertEquals(message, expectedText, subTitle.getText());
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test15OpenSpecifiedPicture() {

        TaskFileManager.openLocalFilesCard(mDevice);

        String filePath = "/testfiles/testpics/";
        String fileName = "4800x3600_5.jpg";
        TaskFileManager.navigateAndOpenSpecifiedFile(mDevice, filePath + fileName);

        String titleId = "tv.fun.filemanager:id/image_name_display";
        UiObject2 fileTitle = mDevice.findObject(By.res(titleId));

        String message = "Verify the specified picture is opened.";
        Assert.assertEquals(message, fileName, fileTitle.getText());
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test16OpenUnknownTypeFile() {

        TaskFileManager.openLocalFilesCard(mDevice);

        String message = "Verify open unknown type file.";
        TaskFileManager.clickOnSpecifiedItemFromCurrentDir(mDevice, "applog");
        Assert.assertEquals(message, FILE_MANAGER_PKG, mDevice.getCurrentPackageName());
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test20MenuHideBtnForDir() {

        TaskFileManager.openLocalFilesCard(mDevice);

        // verification 1
        UiObject2 menuTips = mDevice.findObject(TaskFileManager.getMenuTipsSelector());
        String expectedText = "查看更多操作";
        String message = "Verify the menu tips is displayed.";
        Assert.assertTrue(message, menuTips.getText().contains(expectedText));

        // verification 2
        TaskFileManager.clickOnSpecifiedItemFromCurrentDir(mDevice, "testfiles");
        TaskFileManager.showMenuAndRequestFocus();

        UiObject2 menuHideBtnContainer =
                mDevice.findObject(TaskFileManager.getMenuHideBtnContainerSelector());
        message = "Verify the hide button is focused in the bottom menu.";
        Assert.assertTrue(message, menuHideBtnContainer.isFocused());

        UiObject2 menuHideBtn =
                menuHideBtnContainer.findObject(TaskFileManager.getMenuBtnTextSelector());
        expectedText = "隐藏";
        message = "Verify the text of hide button in the bottom menu.";
        Assert.assertEquals(message, expectedText, menuHideBtn.getText());
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test21MenuRemoveAndHideBtnForFile() {

        TaskFileManager.openLocalFilesCard(mDevice);

        TaskFileManager.clickOnSpecifiedItemFromCurrentDir(mDevice, "applog");
        action.doDeviceActionAndWait(new DeviceActionEnter());  // request focus
        TaskFileManager.showMenuAndRequestFocus();

        // verification 1
        UiObject2 menuRemoveBtnContainer =
                mDevice.findObject(TaskFileManager.getMenuRemoveBtnContainerSelector());
        String message = "Verify the remove button is focused in the bottom menu.";
        Assert.assertTrue(message, menuRemoveBtnContainer.isFocused());

        UiObject2 menuRemoveBtn =
                menuRemoveBtnContainer.findObject(TaskFileManager.getMenuBtnTextSelector());
        message = "Verify the text of remove button in the bottom menu.";
        Assert.assertEquals(message, "删除", menuRemoveBtn.getText());

        // verification 2
        UiObject2 menuHideBtnContainer =
                mDevice.findObject(TaskFileManager.getMenuHideBtnContainerSelector());
        UiObject2 menuHideBtn =
                menuHideBtnContainer.findObject(TaskFileManager.getMenuBtnTextSelector());
        message = "Verify the text of hide button in the bottom menu.";
        Assert.assertEquals(message, "隐藏", menuHideBtn.getText());
    }

    @Ignore
    @Category(CategoryFileManagerTests.class)
    public void test22RemoveFileAndCancel() {
        // TODO: 2016/6/20
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test23RemoveFile() {
        TaskFileManager.openLocalFilesCard(mDevice);
        TaskFileManager.navigateToSpecifiedPath(mDevice, "/testfiles/testpics");

        String fileName = "990522-1548-32.jpg";
        TaskFileManager.clickOnSpecifiedItemFromCurrentDir(mDevice, fileName);

        action.doMultipleDeviceActionAndWait(new DeviceActionBack())  // disappear pic bar
                .doMultipleDeviceActionAndWait(new DeviceActionBack())  // exit pic browser
                .doMultipleDeviceActionAndWait(new DeviceActionEnter());  // request focus
        TaskFileManager.showMenuAndClickRemoveBtn();

        // verification 1
        String message = "Verify the Yes button of confirm dialog.";
        UiObject2 yesBtn = mDevice.findObject(TaskFileManager.getYesBtnOfConfirmDialog());
        Assert.assertNotNull(message, yesBtn);

        // verification 2
        message = "Verify remove a file.";
        action.doClickActionAndWait(yesBtn);
        UiObject2 fileRemoved = mDevice.findObject(By.text(fileName));
        Assert.assertNull(message, fileRemoved);
    }

    @Ignore
    @Category(CategoryFileManagerTests.class)
    public void test24HideFile() {
        // TODO: 2016/6/20
    }

    @Ignore
    @Category(CategoryFileManagerTests.class)
    public void test25ShowHiddenFiles() {
        // TODO: 2016/6/20
    }


}
