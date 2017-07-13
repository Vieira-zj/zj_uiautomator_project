package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import com.example.zhengjin.funsettingsuitest.testrunner.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMenu;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveLeft;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuiobjects.UiObjectsFileManager;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;
import com.example.zhengjin.funsettingsuitest.utils.StringUtils;

import junit.framework.Assert;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.FILE_MANAGER_HOME_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.FILE_MANAGER_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/6/14.
 * <p>
 * Include the UI selectors and tasks for file manager apk.
 */
public final class TaskFileManager {

    private static TaskFileManager instance;

    private final UiDevice device;
    private final UiActionsManager action;
    private final UiObjectsFileManager funUiObjects;

    private TaskFileManager() {
        device = TestConstants.GetUiDeviceInstance();
        action = UiActionsManager.getInstance();
        funUiObjects = UiObjectsFileManager.getInstance();
    }

    public static TaskFileManager getInstance() {
        if (instance == null) {
            synchronized (TaskFileManager.class) {
                if (instance == null) {
                    instance = new TaskFileManager();
                }
            }
        }
        return instance;
    }

    public static synchronized void destroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public void openFileManagerHomePage() {
        this.openFileManagerHomePage(false);
    }

    public void openFileManagerHomePage(boolean isByShell) {
        if (isByShell || RunnerProfile.isPlatform938) {
            ShellUtils.startSpecifiedActivity(FILE_MANAGER_PKG_NAME, FILE_MANAGER_HOME_ACT);
            ShellUtils.systemWaitByMillis(WAIT);
        } else {
            final String TEXT_FILE_MANAGER = "文件管理";
            TaskLauncher.openSpecifiedAppFromAppTab(TEXT_FILE_MANAGER);
        }

        Assert.assertTrue("openFileManagerHomePage, failed!",
                TestHelper.waitForActivityOpenedByShellCmd(
                        FILE_MANAGER_PKG_NAME, FILE_MANAGER_HOME_ACT));
    }

    public void openLocalFilesCard() {
        final int positionX = 1350;
        final int positionY = 450;
        this.clickOnSpecifiedCardOfFileManagerHome(positionX, positionY);
    }

    public void openCategoryVideoCard() {
        final int positionX = 450;
        final int positionY = 450;
        this.clickOnSpecifiedCardOfFileManagerHome(positionX, positionY);
        this.enterAndWaitForSpecifiedCardOpened();
    }

    public void openCategoryAppCard() {
        final int positionX = 650;
        final int positionY = 450;
        this.clickOnSpecifiedCardOfFileManagerHome(positionX, positionY);
        this.enterAndWaitForSpecifiedCardOpened();
    }

    public void openCategoryMusicCard() {
        final int positionX = 450;
        final int positionY = 750;
        this.clickOnSpecifiedCardOfFileManagerHome(positionX, positionY);
        this.enterAndWaitForSpecifiedCardOpened();
    }

    public void openCategoryPictureCard() {
        final int positionX = 750;
        final int positionY = 750;
        this.clickOnSpecifiedCardOfFileManagerHome(positionX, positionY);
        this.enterAndWaitForSpecifiedCardOpened();
    }

    private void clickOnSpecifiedCardOfFileManagerHome(int positionX, int positionY) {
        Assert.assertTrue("clickOnSpecifiedCardOfFileManagerHome, error when click at position.",
                device.click(positionX, positionY));
        ShellUtils.systemWaitByMillis(WAIT);
    }

    private void enterAndWaitForSpecifiedCardOpened() {
        action.doDeviceActionAndWait(new DeviceActionEnter());
        TestHelper.waitForUiObjectEnabled(device.findObject(funUiObjects.getSubTitleSelector()));
    }

    public void navigateAndOpenSpecifiedFile(String fileAbsPath) {
        this.navigateToSpecifiedPath(fileAbsPath);
    }

    public void navigateToSpecifiedPath(String path) {
        this.navigateToSpecifiedPath(path, RunnerProfile.isPlatform938);
    }

    private void navigateToSpecifiedPath(String path, boolean isByMove) {
        String rex = "mnt|sdcard|storage|emulated|[0-9]";
        Pattern pattern = Pattern.compile(rex);

        List<String> dirs = parsePath(path);
        for (String dir : dirs) {
            if (pattern.matcher(dir.toLowerCase()).find()) {
                continue;
            }

            if (isByMove) {
                this.moveAndEnterOnSpecifiedItemFromCurrentDir(dir);
            } else {
                this.clickOnSpecifiedItemFromCurrentDir(dir);
            }
        }
    }

    private void clickOnSpecifiedItemFromCurrentDir(String dirName) {
        this.clickOnSpecifiedItemFromCurrentDir(dirName, false);
    }

    private void clickOnSpecifiedItemFromCurrentDir(String itemName, boolean flag_bottom) {
        // Item for both directory and file
        UiScrollable fileList = new UiScrollable(new UiSelector()
                .resourceId("tv.fun.filemanager:id/activity_sub_grid"));
        fileList.setAsVerticalList();
        try {
            fileList.scrollTextIntoView(itemName);
            ShellUtils.systemWaitByMillis(SHORT_WAIT);
            if (flag_bottom) {
                final int ScrollSteps = 5;
                fileList.scrollForward(ScrollSteps);
                ShellUtils.systemWaitByMillis(SHORT_WAIT);
            }
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            Assert.assertTrue("clickOnSpecifiedItemFromCurrentDir, error scroll to item "
                    + itemName, false);
        }

        UiObject2 item = device.findObject(By.text(itemName));
        action.doClickActionAndWait(item);
    }

    public void moveUntilSpecifiedItemSelected(String itemText) {
        moveAndEnterOnSpecifiedItemFromCurrentDir(itemText, false);
    }

    private void moveAndEnterOnSpecifiedItemFromCurrentDir(String itemName) {
        moveAndEnterOnSpecifiedItemFromCurrentDir(itemName, true);
    }

    private void moveAndEnterOnSpecifiedItemFromCurrentDir(String itemName, boolean isEnter) {
        action.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 3);  // focus on 1st item

        UiObject2 item = null;
        for (int i = 0, tryTimes = 10; i < tryTimes; i++) {
            item = device.findObject(By.text(itemName));
            if (item != null && item.isEnabled()) {
                break;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveDown());
        }
        Assert.assertNotNull("moveAndEnterOnSpecifiedItemFromCurrentDir, failed to find item "
                + itemName, item);

        for (int i = 0, tryTimes = 50; i < tryTimes; i++) {
            if (item.isSelected()) {
                if (isEnter) {
                    action.doDeviceActionAndWait(new DeviceActionEnter());
                }
                return;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveRight());
        }
        Assert.assertTrue("moveAndEnterOnSpecifiedItemFromCurrentDir, failed to select item "
                + itemName, false);
    }

    // path like: android/data/tv.fun.filemanager
    // or: /android/data/tv.fun.filemanager/
    private List<String> parsePath(String path) {
        Assert.assertFalse("parsePath, invalid path: " + path, StringUtils.isEmpty(path));

        String[] tempDirs = path.split("/");
        Assert.assertTrue("parsePath, exception: the dirs size is 0.", tempDirs.length > 0);

        if (tempDirs.length == 1) {
            return Collections.singletonList(path);
        }
        return Arrays.asList(tempDirs);
    }

    public void showMenuAndClickBtn(String btnText) {
        boolean isFocused = false;

        this.showMenuAndRequestFocus();
        UiObject2 btn = device.findObject(By.text(btnText));
        Assert.assertNotNull(String.format("showMenuAndClickBtn, Button %s is not found."
                , btnText), btn);
        UiObject2 btnContainer = btn.getParent();

        for (int i = 0, maxMove = 5; i < maxMove; i++) {
            if (btnContainer.isFocused()) {
                isFocused = true;
                break;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveRight());
        }
        Assert.assertTrue(String.format("showMenuAndClickBtn, Button %s is not focused."
                , btnText), isFocused);

        action.doDeviceActionAndWait(new DeviceActionEnter());
    }

    public void showMenuAndRequestFocus() {
        action.doDeviceActionAndWait(new DeviceActionMenu());
        action.doDeviceActionAndWait(new DeviceActionMoveDown());  // request focus
    }

    public File createPicTestFile(UiDevice device, String dirPath) {
        final String mMessage = "createPicTestFile, for testing setup.";
        String savedPath = ShellUtils.takeScreenCapture(device, dirPath);
        Assert.assertFalse(mMessage, StringUtils.isEmpty(savedPath));

        File testPicFile = new File(savedPath);
        Assert.assertTrue(mMessage, testPicFile.exists() && testPicFile.isFile());

        return testPicFile;
    }

    public void deleteTestFile(File testPicFile) {
        if (testPicFile.exists() && testPicFile.isFile()) {
            Assert.assertTrue("deleteTestFile, for testing clear up.", testPicFile.delete());
        }
    }

    public void restartFileManagerApp() {
        ShellUtils.stopProcessByPackageName(TestConstants.FILE_MANAGER_PKG_NAME);
        ShellUtils.systemWaitByMillis(TestConstants.SHORT_WAIT);
        ShellUtils.startSpecifiedActivity(
                TestConstants.FILE_MANAGER_PKG_NAME, TestConstants.FILE_MANAGER_HOME_ACT);
        ShellUtils.systemWaitByMillis(TestConstants.WAIT);
    }

}
