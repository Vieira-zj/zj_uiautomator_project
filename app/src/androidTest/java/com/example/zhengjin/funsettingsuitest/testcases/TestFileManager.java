package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryFileManagerTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveLeft;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskFileManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;
import com.example.zhengjin.funsettingsuitest.utils.FileUtils;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.FILE_MANAGER_PKG_NAME;

/**
 * Created by zhengjin on 2016/6/7.
 * <p>
 * Include the test cases for file manager APP.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestFileManager {

    private UiDevice mDevice;
    private UiActionsManager mAction;
    private TaskFileManager mTask;

    private static final String TEXT_REMOVE_BUTTON = "删除";
    private static final String TEXT_HIDDEN_BUTTON = "隐藏";
    private static final String TEXT_SHOWALL_BUTTON = "显示全部";

    private static final String TEST_DIR_NAME = "testfile";
    private static final String TEST_DIR_PATH;
    private static final String TEST_HIDDEN_DIR_NAME = "hidden";
    private static final String TEST_HIDDEN_DIR_PATH;
    private static final String TEST_TXT_FILE_NAME = "test.txt";
    private static final String TEST_TXT_FILE_PATH;

    static {
        TEST_DIR_PATH = String.format("%s/%s/", FileUtils.getExternalStoragePath(), "testfile");
        TEST_HIDDEN_DIR_PATH = String.format("%s/%s", TEST_DIR_PATH, TEST_HIDDEN_DIR_NAME);
        TEST_TXT_FILE_PATH = String.format("%s/%s", TEST_DIR_PATH, TEST_TXT_FILE_NAME);
    }

    @BeforeClass
    public static void setUpClass() {
        prepareData();
    }

    @AfterClass
    public static void clearUpClass() {
        removeData();
    }

    private static void prepareData() {
        String message = "Prepare files for file manager test.";
        String cmdCreateDir = String.format("mkdir %s", TEST_DIR_PATH);
        String cmdCreateHiddenDir = String.format("mkdir %s", TEST_HIDDEN_DIR_PATH);
        String cmdCreateTxtFile = String.format("touch %s", TEST_TXT_FILE_PATH);

        ShellUtils.CommandResult result =
                ShellUtils.execCommand(
                        new String[]{cmdCreateDir, cmdCreateHiddenDir, cmdCreateTxtFile},
                        false, false);
        Assert.assertTrue(message, (result.mResult == 0));
    }

    private static void removeData() {
        String message = "Clear files for file manager test.";
        String removeAllFiles = String.format("rm -rf %s", TEST_DIR_PATH);

        ShellUtils.CommandResult result = ShellUtils.execCommand(removeAllFiles, false, false);
        Assert.assertTrue(message, (result.mResult == 0));
    }

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mTask = TaskFileManager.getInstance();

        TaskLauncher.openSpecifiedAppFromAppTab(mDevice, "文件管理");
    }

    @After
    public void clearUp() {
//        mAction.doRepeatDeviceActionAndWait(new DeviceActionBack(), 2);
        ShellUtils.stopAndClearPackage(FILE_MANAGER_PKG_NAME);
    }

    // Error: ddmlib.SyncException: Remote object doesn't exist!
    @Ignore
    @Category(CategoryFileManagerTests.class)
    public void test11SdcardTabNameAndFocused() {
        String message;

        message = "Verify the sdcard tab name is enabled.";
        UiObject2 tabContainer =
                mDevice.findObject(By.res("tv.fun.filemanager:id/activity_fun_fm_tab"));
        Assert.assertTrue(message, TestHelper.waitForUiObjectEnabled(tabContainer));

        message = "Verify the sdcard tab name is focused.";
        Assert.assertTrue(message, tabContainer.isFocused());
    }

    // Error: ddmlib.SyncException: Remote object doesn't exist!
    @Ignore
    @Category(CategoryFileManagerTests.class)
    public void test12VideoCardNameAndItemCount() {
        String message;

        UiObject2 videoCard = mDevice.findObject(By.res("tv.fun.filemanager:id/category_video"));
        message = "Verify the video card is enabled.";
        Assert.assertTrue(message, TestHelper.waitForUiObjectEnabled(videoCard));

        UiObject2 videoCardText = videoCard.findObject(By.res("tv.fun.filemanager:id/entity_name"));
        message = "Verify the text of video card.";
        Assert.assertNotNull(videoCardText);
        Assert.assertEquals(message, "视频", videoCardText.getText());

        UiObject2 videoCardCount = videoCard.findObject(By.res("tv.fun.filemanager:id/entity_count"));
        message = "Verify the files count of video card.";
        Assert.assertNotNull(videoCardCount);
        Assert.assertEquals(message, "(0项)", videoCardCount.getText());
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test13OpenAllFilesCardFromSdcardTab() {
        String message;

        mTask.openLocalFilesCard(mDevice);

        UiObject2 mainTitle = mDevice.findObject(mTask.getMainTitleSelector());
        message = "Verify the text of main title from sdcard local files.";
        Assert.assertNotNull(mainTitle);
        Assert.assertEquals(message, "本地存储", mainTitle.getText());

        UiObject2 subTitle = mDevice.findObject(mTask.getSubTitleSelector());
        message = "Verify the text of sub title from sdcard local files.";
        Assert.assertEquals(message, "全部文件", subTitle.getText());
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test14NavigateToSpecifiedPath() {
        mTask.openLocalFilesCard(mDevice);
        mTask.navigateToSpecifiedPath(mDevice, TEST_DIR_PATH);

        UiObject2 subTitle = mDevice.findObject(mTask.getSubTitleSelector());
        String message = "Verify navigate to the specified path.";
        Assert.assertEquals(message, TEST_DIR_NAME, subTitle.getText());
    }

    @Ignore
    @Category(CategoryFileManagerTests.class)
    public void test15OpenSpecifiedPicture() {
        // need push pic file to the device
        mTask.openLocalFilesCard(mDevice);

        String fileName = "4800x3600_5.jpg";
        mTask.navigateAndOpenSpecifiedFile(mDevice, (TEST_DIR_PATH + fileName));

        String titleId = "tv.fun.filemanager:id/image_name_display";
        UiObject2 fileTitle = mDevice.findObject(By.res(titleId));

        String message = "Verify the specified picture is opened.";
        Assert.assertEquals(message, fileName, fileTitle.getText());
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test16OpenUnknownTypeFile() {
        mTask.openLocalFilesCard(mDevice);

        String message = "Verify open unknown type file.";
        mTask.navigateAndOpenSpecifiedFile(mDevice, TEST_TXT_FILE_PATH);
        Assert.assertTrue(message,
                TestHelper.waitForUiObjectExist(mDevice, By.text(TEST_TXT_FILE_NAME)));
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test21MenuHideBtnExistForDir() {
        String message;

        // verification 1
        mTask.openLocalFilesCard(mDevice);
        UiObject2 menuTips = mDevice.findObject(mTask.getMenuTipsSelector());
        message = "Verify the menu tips is displayed.";
        Assert.assertTrue(message, menuTips.getText().contains("查看更多操作"));

        // verification 2
        mTask.navigateToSpecifiedPath(mDevice, TEST_DIR_PATH);
        mAction.doMultipleDeviceActionAndWait(new DeviceActionMoveRight())
                .doDeviceActionAndWait(new DeviceActionMoveLeft());  // request focus
        mTask.showMenuAndRequestFocus();

        UiObject2 menuHideBtnContainer =
                mDevice.findObject(mTask.getMenuHideBtnContainerSelector());
        message = "Verify the hide button is focused in the bottom menu.";
        Assert.assertNotNull(menuHideBtnContainer);
        Assert.assertTrue(message, menuHideBtnContainer.isFocused());

        UiObject2 menuHideBtn =
                menuHideBtnContainer.findObject(mTask.getMenuBtnTextSelector());
        message = "Verify the text of hide button in the bottom menu.";
        Assert.assertNotNull(menuHideBtn);
        Assert.assertEquals(message, TEXT_HIDDEN_BUTTON, menuHideBtn.getText());
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test22MenuRemoveAndHideBtnExistForFile() {
        String message;

        mTask.openLocalFilesCard(mDevice);
        mTask.navigateToSpecifiedPath(mDevice, TEST_DIR_PATH);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());  // request focus
        mTask.showMenuAndRequestFocus();

        // verification 1
        UiObject2 menuRemoveBtnContainer =
                mDevice.findObject(mTask.getMenuRemoveBtnContainerSelector());
        message = "Verify the remove button is focused in the bottom menu.";
        Assert.assertNotNull(menuRemoveBtnContainer);
        Assert.assertTrue(message, menuRemoveBtnContainer.isFocused());

        UiObject2 menuRemoveBtn =
                menuRemoveBtnContainer.findObject(mTask.getMenuBtnTextSelector());
        message = "Verify the text of remove button in the bottom menu.";
        Assert.assertEquals(message, TEXT_REMOVE_BUTTON, menuRemoveBtn.getText());

        // verification 2
        UiObject2 menuHideBtnContainer =
                mDevice.findObject(mTask.getMenuHideBtnContainerSelector());
        UiObject2 menuHideBtn =
                menuHideBtnContainer.findObject(mTask.getMenuBtnTextSelector());
        message = "Verify the text of hide button in the bottom menu.";
        Assert.assertEquals(message, TEXT_HIDDEN_BUTTON, menuHideBtn.getText());
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test23RemoveFileAndCancel() {
        String message;

        mTask.openLocalFilesCard(mDevice);
        mTask.navigateToSpecifiedPath(mDevice, TEST_DIR_PATH);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());  // request focus
        mTask.showMenuAndClickBtn(mDevice, TEXT_REMOVE_BUTTON);

        // verification 1
        message = "Verify the Cancel button of confirm dialog.";
        UiObject2 cancelBtn = mDevice.findObject(mTask.getCancelBtnOfConfirmDialog());
        Assert.assertNotNull(message, cancelBtn);

        // verification 2
        message = "Verify click cancel and do not remove a file.";
        mAction.doClickActionAndWait(cancelBtn);
        UiObject2 fileDeleted = mDevice.findObject(By.text(TEST_TXT_FILE_NAME));
        Assert.assertNotNull(message, fileDeleted);
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test24RemoveFile() {
        String message;

        mTask.openLocalFilesCard(mDevice);
        mTask.navigateToSpecifiedPath(mDevice, TEST_DIR_PATH);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());  // request focus
        mTask.showMenuAndClickBtn(mDevice, TEXT_REMOVE_BUTTON);

        // verification 1
        message = "Verify the Yes button of confirm dialog.";
        UiObject2 yesBtn = mDevice.findObject(mTask.getYesBtnOfConfirmDialog());
        Assert.assertNotNull(message, yesBtn);

        // verification 2
        message = "Verify click yes and remove a file.";
        mAction.doClickActionAndWait(yesBtn);
        UiObject2 fileDeleted = mDevice.findObject(By.text(TEST_TXT_FILE_NAME));
        Assert.assertNull(message, fileDeleted);
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test25HideAndShowDirectory() {
        String message;

        mTask.openLocalFilesCard(mDevice);
        mTask.navigateToSpecifiedPath(mDevice, TEST_DIR_PATH);
        mAction.doMultipleDeviceActionAndWait(new DeviceActionMoveRight())
                .doDeviceActionAndWait(new DeviceActionMoveLeft());  // request focus

        // verification 1
        mTask.showMenuAndClickBtn(mDevice, TEXT_HIDDEN_BUTTON);
        message = "Verify the directory is hidden.";
        UiObject2 fileHidden = mDevice.findObject(By.text(TEST_HIDDEN_DIR_NAME));
        Assert.assertNull(message, fileHidden);

        // verification 2
        mTask.showMenuAndClickBtn(mDevice, TEXT_SHOWALL_BUTTON);
        message = "Verify the directory is show.";
        UiObject2 fileShow = mDevice.findObject(By.text(TEST_HIDDEN_DIR_NAME));
        Assert.assertNotNull(message, fileShow);
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test26HideAndShowFile() {
        String message;

        mTask.openLocalFilesCard(mDevice);
        mTask.navigateToSpecifiedPath(mDevice, TEST_DIR_PATH);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());  // request focus

        // verification 1
        mTask.showMenuAndClickBtn(mDevice, TEXT_HIDDEN_BUTTON);
        message = "Verify the file is hidden.";
        UiObject2 fileHidden = mDevice.findObject(By.text(TEST_TXT_FILE_NAME));
        Assert.assertNull(message, fileHidden);

        // verification 2
        mTask.showMenuAndClickBtn(mDevice, TEXT_SHOWALL_BUTTON);
        message = "Verify the file is show.";
        UiObject2 fileShow = mDevice.findObject(By.text(TEST_TXT_FILE_NAME));
        Assert.assertNotNull(message, fileShow);
    }

}
