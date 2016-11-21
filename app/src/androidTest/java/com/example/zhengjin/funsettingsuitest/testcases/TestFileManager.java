package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryFileManagerTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMenu;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveLeft;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
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

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.FILE_MANAGER_HOME_ACT;
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
    private String mMessage;

    private static final String TEST_ROOT_DIR_NAME = "TestFile";
    private static final String TEST_ROOT_DIR_PATH;
    private static final String TEST_DIR_NAME = "TestDirectory";
    private static final String TEST_DIR_PATH;
    private static final String TEST_MEDIA_DIR_NAME = "TestMediaDir";
    private static final String TEST_MEDIA_DIR_PATH;

    private static final String TEST1_FILE_NAME = "TestFile1.log";
    private static final String TEST1_FILE_PATH;
    private static final String TEST2_FILE_NAME = "TestFile2.log";
    private static final String TEST2_FILE_PATH;
    private static final String TEST1_VIDEO_FILE_NAME = "TestVideo1.mp4";
    private static final String TEST1_VIDEO_FILE_PATH;
    private static final String TEST2_VIDEO_FILE_NAME = "TestVideo2.mp4";
    private static final String TEST2_VIDEO_FILE_PATH;

    private final String TEXT_REMOVE_BUTTON = "删除";
    private final String TEXT_HIDDEN_BUTTON = "隐藏";
    private final String TEXT_SHOWALL_BUTTON = "显示全部";

    private final String TEXT_SDCARD = "sdcard";

    static {
        TEST_ROOT_DIR_PATH =
                String.format("%s/%s", FileUtils.getExternalStoragePath(), TEST_ROOT_DIR_NAME);

        TEST_DIR_PATH = String.format("%s/%s", TEST_ROOT_DIR_PATH, TEST_DIR_NAME);
        TEST1_FILE_PATH = String.format("%s/%s", TEST_ROOT_DIR_PATH, TEST1_FILE_NAME);
        TEST2_FILE_PATH = String.format("%s/%s", TEST_DIR_PATH, TEST2_FILE_NAME);

        TEST_MEDIA_DIR_PATH = String.format("%s/%s", TEST_ROOT_DIR_PATH, TEST_MEDIA_DIR_NAME);
        TEST1_VIDEO_FILE_PATH = String.format("%s/%s", TEST_MEDIA_DIR_PATH, TEST1_VIDEO_FILE_NAME);
        TEST2_VIDEO_FILE_PATH = String.format("%s/%s", TEST_MEDIA_DIR_PATH, TEST2_VIDEO_FILE_NAME);
    }

    @BeforeClass
    public static void classSetUp() {
        ShellUtils.stopAndClearPackage(FILE_MANAGER_PKG_NAME);
        prepareData();
    }

    @AfterClass
    public static void classClearUp() {
        removeData();
    }

    private static void prepareData() {
        String message = "Prepare files for file manager test.";
        String cmdCreateTestDir = String.format("mkdir -p %s", TEST_DIR_PATH);
        String cmdCreateTxtFile1 = String.format("touch %s", TEST1_FILE_PATH);
        String cmdCreateTxtFile2 = String.format("touch %s", TEST2_FILE_PATH);

        String cmdCreateMediaDir = String.format("mkdir -p %s", TEST_MEDIA_DIR_PATH);
        String cmdCreateVideoFile1 = String.format("touch %s", TEST1_VIDEO_FILE_PATH);
        String cmdCreateVideoFile2 = String.format("touch %s", TEST2_VIDEO_FILE_PATH);

        String commands[] = {cmdCreateTestDir, cmdCreateTxtFile1, cmdCreateTxtFile2,
                cmdCreateMediaDir, cmdCreateVideoFile1, cmdCreateVideoFile2};

        ShellUtils.CommandResult result = ShellUtils.execCommand(commands, false, false);
        Assert.assertTrue(message, (result.mResult == 0));
    }

    private static void removeData() {
        String message = "Clear files for file manager test.";
        String removeAllFiles = String.format("rm -rf %s", TEST_ROOT_DIR_PATH);

        ShellUtils.CommandResult result = ShellUtils.execCommand(removeAllFiles, false, false);
        Assert.assertTrue(message, (result.mResult == 0));
    }

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mTask = TaskFileManager.getInstance();

        TaskLauncher.openSpecifiedAppFromAppTab("文件管理");
        boolean isAppOpened = TestHelper.waitForAppOpenedByShellCmd(
                String.format("%s/%s", FILE_MANAGER_PKG_NAME, FILE_MANAGER_HOME_ACT));
        Assert.assertTrue("Open File Manager app.", isAppOpened);
    }

    @After
    public void clearUp() {
        ShellUtils.stopProcess(FILE_MANAGER_PKG_NAME);
    }

    @Ignore
    @Category(CategoryFileManagerTests.class)
    public void test11FileManagerHomeTabShow() {
        mMessage = "Verify the sdcard tab name is enabled.";
        UiObject2 tabContainer = mDevice.findObject(mTask.getFileManagerHomeTabSelector());
        Assert.assertNotNull(tabContainer);
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(tabContainer));
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test13OpenAllFilesCardFromSdcardTab() {
        mTask.openLocalFilesCard();

        mMessage = "Verify the text of main title from sdcard local files.";
        UiObject2 mainTitle = mDevice.findObject(mTask.getMainTitleSelector());
        Assert.assertNotNull(mainTitle);
        Assert.assertEquals(mMessage, "本地存储", mainTitle.getText());

        mMessage = "Verify the text of sub title from sdcard local files.";
        UiObject2 subTitle = mDevice.findObject(mTask.getSubTitleSelector());
        Assert.assertEquals(mMessage, "全部文件", subTitle.getText());
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test14NavigateToSpecifiedPath() {
        mTask.openLocalFilesCard();
        mTask.navigateToSpecifiedPath(TEST_ROOT_DIR_PATH);

        mMessage = "Verify navigate to the specified path.";
        UiObject2 subTitle = mDevice.findObject(mTask.getSubTitleSelector());
        Assert.assertEquals(mMessage, TEST_ROOT_DIR_NAME, subTitle.getText());
    }

    @Ignore
    @Category(CategoryFileManagerTests.class)
    public void test15OpenSpecifiedPicture() {
        // need push pic file to the device
        // TODO: 2016/11/21
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test16OpenUnknownTypeFile() {
        mTask.openLocalFilesCard();

        mMessage = "Verify open the unknown type file.";
        mTask.navigateAndOpenSpecifiedFile(TEST1_FILE_PATH);
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectExist(By.text(TEST1_FILE_NAME)));
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test21MenuHideBtnExistForDir() {
        mTask.openLocalFilesCard();

        // verification 1
        mMessage = "Verify the menu tips is displayed.";
        UiObject2 menuTips = mDevice.findObject(mTask.getMenuTipsSelector());
        Assert.assertTrue(mMessage, menuTips.getText().contains("查看更多操作"));

        // verification 2
        mTask.navigateToSpecifiedPath(TEST_ROOT_DIR_PATH);
        mAction.doChainedDeviceActionAndWait(new DeviceActionMoveRight())
                .doDeviceActionAndWait(new DeviceActionMoveLeft());  // request focus
        mTask.showMenuAndRequestFocus();

        mMessage = "Verify the hide button is focused in the bottom menu.";
        UiObject2 menuHideBtnContainer =
                mDevice.findObject(mTask.getMenuHideBtnContainerSelector());
        Assert.assertNotNull(menuHideBtnContainer);
        Assert.assertTrue(mMessage, menuHideBtnContainer.isFocused());

        mMessage = "Verify the text of hide button in the bottom menu.";
        UiObject2 menuHideBtn =
                menuHideBtnContainer.findObject(mTask.getMenuBtnTextSelector());
        Assert.assertNotNull(menuHideBtn);
        Assert.assertEquals(mMessage, TEXT_HIDDEN_BUTTON, menuHideBtn.getText());
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test22MenuRemoveAndHideBtnExistForFile() {
        mTask.openLocalFilesCard();
        mTask.navigateToSpecifiedPath(TEST_ROOT_DIR_PATH);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());  // request focus
        mTask.showMenuAndRequestFocus();

        // verification 1
        mMessage = "Verify the remove button is focused in the bottom menu.";
        UiObject2 menuRemoveBtnContainer =
                mDevice.findObject(mTask.getMenuRemoveBtnContainerSelector());
        Assert.assertNotNull(menuRemoveBtnContainer);
        Assert.assertTrue(mMessage, menuRemoveBtnContainer.isFocused());

        mMessage = "Verify the text of remove button in the bottom menu.";
        UiObject2 menuRemoveBtn =
                menuRemoveBtnContainer.findObject(mTask.getMenuBtnTextSelector());
        Assert.assertEquals(mMessage, TEXT_REMOVE_BUTTON, menuRemoveBtn.getText());

        // verification 2
        mMessage = "Verify the text of hide button in the bottom menu.";
        UiObject2 menuHideBtnContainer =
                mDevice.findObject(mTask.getMenuHideBtnContainerSelector());
        UiObject2 menuHideBtn =
                menuHideBtnContainer.findObject(mTask.getMenuBtnTextSelector());
        Assert.assertEquals(mMessage, TEXT_HIDDEN_BUTTON, menuHideBtn.getText());
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test23HideAndShowDirectory() {
        mTask.openLocalFilesCard();
        mTask.navigateToSpecifiedPath(TEST_ROOT_DIR_PATH);
        mAction.doChainedDeviceActionAndWait(new DeviceActionMoveRight())
                .doDeviceActionAndWait(new DeviceActionMoveLeft());  // request focus

        mMessage = "Verify the directory is hidden after click Hide button.";
        mTask.showMenuAndClickBtn(TEXT_HIDDEN_BUTTON);
        UiObject2 fileHidden = mDevice.findObject(By.text(TEST_DIR_NAME));
        Assert.assertNull(mMessage, fileHidden);

        mMessage = "Verify the Show All button is displayed in menu after hide file.";
        UiObject2 btnContainer = mDevice.findObject(mTask.getMenuShowAllBtnContainerSelector());
        UiObject2 btnShowAll = btnContainer.findObject(mTask.getMenuBtnTextSelector());
        Assert.assertEquals(mMessage, TEXT_SHOWALL_BUTTON, btnShowAll.getText());

        mMessage = "Verify the directory is show after click Show All button.";
        mTask.showMenuAndClickBtn(TEXT_SHOWALL_BUTTON);
        UiObject2 fileShow = mDevice.findObject(By.text(TEST_DIR_NAME));
        Assert.assertNotNull(mMessage, fileShow);
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test24HideAndShowFile() {
        mTask.openLocalFilesCard();
        mTask.navigateToSpecifiedPath(TEST_ROOT_DIR_PATH);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());  // request focus

        mMessage = "Verify the file is hidden after click Hide button.";
        mTask.showMenuAndClickBtn(TEXT_HIDDEN_BUTTON);
        UiObject2 fileHidden = mDevice.findObject(By.text(TEST1_FILE_NAME));
        Assert.assertNull(mMessage, fileHidden);

        mMessage = "Verify the file is show after click Show All button.";
        mTask.showMenuAndClickBtn(TEXT_SHOWALL_BUTTON);
        UiObject2 fileShow = mDevice.findObject(By.text(TEST1_FILE_NAME));
        Assert.assertNotNull(mMessage, fileShow);
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test25RemoveFileAndCancel() {
        mTask.openLocalFilesCard();
        mTask.navigateToSpecifiedPath(TEST_ROOT_DIR_PATH);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());  // request focus
        mTask.showMenuAndClickBtn(TEXT_REMOVE_BUTTON);

        mMessage = "Verify the Cancel button of confirm dialog.";
        UiObject2 cancelBtn = mDevice.findObject(mTask.getCancelBtnOfConfirmDialogSelector());
        Assert.assertNotNull(mMessage, cancelBtn);

        mMessage = "Verify click cancel and do not remove a file.";
        mAction.doClickActionAndWait(cancelBtn);
        UiObject2 fileDeleted = mDevice.findObject(By.text(TEST1_FILE_NAME));
        Assert.assertNotNull(mMessage, fileDeleted);
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test26RemoveFileAndConfirm() {
        mTask.openLocalFilesCard();
        mTask.navigateToSpecifiedPath(TEST_ROOT_DIR_PATH);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());  // request focus
        mTask.showMenuAndClickBtn(TEXT_REMOVE_BUTTON);

        mMessage = "Verify the Yes button of confirm dialog.";
        UiObject2 confirmBtn = mDevice.findObject(mTask.getYesBtnOfConfirmDialogSelector());
        Assert.assertNotNull(mMessage, confirmBtn);

        mMessage = "Verify click yes and remove a file.";
        mAction.doClickActionAndWait(confirmBtn);
        UiObject2 fileDeleted = mDevice.findObject(By.text(TEST1_FILE_NAME));
        Assert.assertNull(mMessage, fileDeleted);
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test27HideAndShowOnlyFileOfDirectory() {
        mTask.openLocalFilesCard();
        mTask.navigateToSpecifiedPath(TEST_DIR_PATH);
        mAction.doDeviceActionAndWait(new DeviceActionMoveUp());  // request focus

        mTask.showMenuAndClickBtn(TEXT_HIDDEN_BUTTON);
        mMessage = "Verify the tips of empty directory from all files card.";
        UiObject2 tips = mDevice.findObject(mTask.getTipsOfEmptyDirFromLocalFilesCardSelector());
        Assert.assertNotNull(tips);
        Assert.assertEquals(mMessage, "未发现可显示的文件", tips.getText());

        mMessage = "Verify the file is show after click Show All.";
        mTask.showMenuAndClickBtn(TEXT_SHOWALL_BUTTON);
        UiObject2 hiddenFile = mDevice.findObject(By.text(TEST2_FILE_NAME));
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(hiddenFile));
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test28RemoveOnlyFileOfDirectory() {
        mTask.openLocalFilesCard();
        mTask.navigateToSpecifiedPath(TEST_DIR_PATH);

        // remove file
        mAction.doDeviceActionAndWait(new DeviceActionMoveUp());  // request focus
        mTask.showMenuAndClickBtn(TEXT_REMOVE_BUTTON);
        UiObject2 confirmBtn = mDevice.findObject(mTask.getYesBtnOfConfirmDialogSelector());
        mAction.doClickActionAndWait(confirmBtn);

        mMessage = "Verify the tips of empty directory from all files card.";
        UiObject2 tips = mDevice.findObject(mTask.getTipsOfEmptyDirFromLocalFilesCardSelector());
        Assert.assertNotNull(tips);
        Assert.assertEquals(mMessage, "未发现可显示的文件", tips.getText());
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test31MessageWhenEmptyForVideoCard() {
        mTask.openCategoryVideoCard();

        mMessage = "Verify the tips when no files in video card.";
        UiObject2 tips = mDevice.findObject(mTask.getTipsOfEmptyDirFromLocalFilesCardSelector());
        Assert.assertNotNull(tips);
        Assert.assertEquals(mMessage, "未发现可播放的视频", tips.getText());

        mMessage = "Verify the menu is NOT shown when no files in video card.";
        mAction.doDeviceActionAndWait(new DeviceActionMenu());
        UiObject2 menu = mDevice.findObject(mTask.getMenuContainerSelector());
        Assert.assertNull(mMessage, menu);
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test32MessageWhenEmptyForAppCard() {
        mTask.openCategoryAppCard();

        mMessage = "Verify the tips when no files in APP card.";
        UiObject2 tips = mDevice.findObject(mTask.getTipsOfEmptyDirFromLocalFilesCardSelector());
        Assert.assertNotNull(tips);
        Assert.assertEquals(mMessage, "未发现可安装的应用", tips.getText());

        mMessage = "Verify the menu is NOT shown when no files in APP card.";
        mAction.doDeviceActionAndWait(new DeviceActionMenu());
        UiObject2 menu = mDevice.findObject(mTask.getMenuContainerSelector());
        Assert.assertNull(mMessage, menu);
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test33MessageWhenEmptyForMusicCard() {
        mTask.openCategoryMusicCard();

        mMessage = "Verify the tips when no files in music card.";
        UiObject2 tips = mDevice.findObject(mTask.getTipsOfEmptyDirFromLocalFilesCardSelector());
        Assert.assertNotNull(tips);
        Assert.assertEquals(mMessage, "未发现可播放的音乐", tips.getText());

        mMessage = "Verify the menu is NOT shown when no files in music card.";
        mAction.doDeviceActionAndWait(new DeviceActionMenu());
        UiObject2 menu = mDevice.findObject(mTask.getMenuContainerSelector());
        Assert.assertNull(mMessage, menu);
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test34MessageWhenEmptyForPictureCard() {
        mTask.openCategoryPictureCard();

        mMessage = "Verify the tips when no files in picture card.";
        UiObject2 tips = mDevice.findObject(mTask.getTipsOfEmptyDirFromLocalFilesCardSelector());
        Assert.assertNotNull(tips);
        Assert.assertEquals(mMessage, "未发现可显示的图片", tips.getText());

        mMessage = "Verify the menu is NOT shown when no files in picture card.";
        mAction.doDeviceActionAndWait(new DeviceActionMenu());
        UiObject2 menu = mDevice.findObject(mTask.getMenuContainerSelector());
        Assert.assertNull(mMessage, menu);
    }

    @Ignore
    @Category(CategoryFileManagerTests.class)
    public void test35VideoCardNameAndItemsCount() {
        // Error obtaining UI hierarchy
        mDevice.waitForIdle();

        mMessage = "Verify the video card is enabled.";
        UiObject2 videoCardContainer = mDevice.findObject(mTask.getVideoCategorySelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(videoCardContainer));

        mMessage = "Verify the text of video card.";
        UiObject2 videoCardText = videoCardContainer.findObject(mTask.getCategoryTitleSelector());
        Assert.assertNotNull(videoCardText);
        Assert.assertEquals(mMessage, "视频", videoCardText.getText());

        mMessage = "Verify the files count of video card.";
        UiObject2 videoCardCount =
                videoCardContainer.findObject(mTask.getCategoryEntiesCountSelector());
        Assert.assertNotNull(videoCardCount);
        Assert.assertEquals(mMessage, "(0项)", videoCardCount.getText());
    }

    @Test
    @Category({CategoryFileManagerTests.class})
    public void test41_01HideVideoFile() {
        mTask.openLocalFilesCard();
        mTask.navigateToSpecifiedPath(TEST_MEDIA_DIR_PATH);
        mTask.moveUntilSpecifiedItemSelected(TEST1_VIDEO_FILE_NAME);  // request focus

        mMessage = "Verify the video file is hidden after click Hide button.";
        mTask.showMenuAndClickBtn(TEXT_HIDDEN_BUTTON);
        UiObject2 fileHidden = mDevice.findObject(By.text(TEST1_VIDEO_FILE_NAME));
        Assert.assertNull(mMessage, fileHidden);

        mMessage = "Verify the video file is hidden from video category.";
        this.backToFileManagerHome();
        mTask.openCategoryVideoCard();

        fileHidden = mDevice.findObject(By.text(TEST1_VIDEO_FILE_NAME));
        Assert.assertNull(mMessage, fileHidden);
    }

    @Test
    @Category({CategoryFileManagerTests.class})
    public void test41_02ShowVideoFile() {
        mTask.openLocalFilesCard();
        mTask.navigateToSpecifiedPath(TEST_MEDIA_DIR_PATH);

        mMessage = "Verify the video file is shown after click Show All button.";
        mTask.showMenuAndClickBtn(TEXT_SHOWALL_BUTTON);
        UiObject2 fileShown = mDevice.findObject(By.text(TEST1_VIDEO_FILE_NAME));
        Assert.assertNotNull(fileShown);
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(fileShown));

        mMessage = "Verify the video file is shown from video category.";
        this.backToFileManagerHome();
        mTask.openCategoryVideoCard();

        fileShown = mDevice.findObject(By.text(TEST1_VIDEO_FILE_NAME));
        Assert.assertNotNull(fileShown);
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(fileShown));
    }

    @Test
    @Category({CategoryFileManagerTests.class})
    public void test41_03HideVideoDirectory() {
        // TODO: 2016/11/21 for dir
    }

    @Test
    @Category({CategoryFileManagerTests.class})
    public void test41_04ShowVideoDirectory() {
        // TODO: 2016/11/21 for dir 
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test41_05RemoveVideoFileFromAllFilesCategory() {
        mTask.openLocalFilesCard();
        mTask.navigateToSpecifiedPath(TEST_MEDIA_DIR_PATH);
        mTask.moveUntilSpecifiedItemSelected(TEST1_VIDEO_FILE_NAME);

        mMessage = "Verify the video file is removed after click Show All button.";
        this.removeFileAndConfirm();
        UiObject2 fileRemoved = mDevice.findObject(By.text(TEST1_VIDEO_FILE_NAME));
        Assert.assertNull(mMessage, fileRemoved);

        mMessage = "Verify the video file is removed from video category.";
        this.backToFileManagerHome();
        mTask.openCategoryVideoCard();
        fileRemoved = mDevice.findObject(By.text(TEST1_VIDEO_FILE_NAME));
        Assert.assertNull(mMessage, fileRemoved);
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test41_06RemoveVideoFileFromVideoCategory() {
        mTask.openCategoryVideoCard();
        mTask.moveUntilSpecifiedItemSelected(TEST2_VIDEO_FILE_NAME);

        mMessage = "Verify the video file is removed from video category.";
        this.removeFileAndConfirm();
        UiObject2 fileRemoved = mDevice.findObject(By.text(TEST2_VIDEO_FILE_NAME));
        Assert.assertNull(mMessage, fileRemoved);

        mMessage = "Verify the video file is removed from all files category.";
        this.backToFileManagerHome();
        mTask.openLocalFilesCard();
        mTask.navigateToSpecifiedPath(TEST_MEDIA_DIR_PATH);

        fileRemoved = mDevice.findObject(By.text(TEST2_VIDEO_FILE_NAME));
        Assert.assertNull(mMessage, fileRemoved);
    }

    @Test
    @Category({CategoryFileManagerTests.class})
    public void test42_01HidePictureFile() {
        // TODO: 2016/11/21 pic category
    }

    @Test
    @Category(CategoryFileManagerTests.class)
    public void test99ClearUpAfterAllTestCasesDone() {
        mTask.destroyInstance();
    }

    public void backToFileManagerHome() {
        ShellUtils.startSpecifiedActivity(FILE_MANAGER_PKG_NAME, FILE_MANAGER_HOME_ACT);
        TestHelper.waitForAppOpenedByShellCmd(
                String.format("%s/%s", FILE_MANAGER_PKG_NAME, FILE_MANAGER_HOME_ACT));
    }

    public void removeFileAndConfirm() {
        mTask.showMenuAndClickBtn(TEXT_REMOVE_BUTTON);
        UiObject2 btnConfirm = mDevice.findObject(mTask.getYesBtnOfConfirmDialogSelector());
        Assert.assertTrue("Wait for confirm button enabled.",
                TestHelper.waitForUiObjectEnabled(btnConfirm));
        mAction.doClickActionAndWait(btnConfirm);
    }

}
