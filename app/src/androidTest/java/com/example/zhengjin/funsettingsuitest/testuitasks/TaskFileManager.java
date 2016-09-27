package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMenu;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/6/14.
 *
 * Include the UI selectors and tasks for file manager apk.
 */
public final class TaskFileManager {

    private static UiActionsManager ACTION = UiActionsManager.getInstance();

    public static BySelector getMainTitleSelector() {
        return By.res("tv.fun.filemanager:id/activity_sub_title_main");
    }

    public static BySelector getSubTitleSelector() {
        return By.res("tv.fun.filemanager:id/activity_sub_title_sub");
    }

    public static BySelector getMenuTipsSelector() {
        return By.res("tv.fun.filemanager:id/activity_sub_menu_tips");
    }

    public static BySelector getMenuRemoveBtnContainerSelector() {
        return By.res("tv.fun.filemanager:id/menu_item_del_id");
    }

    public static BySelector getMenuHideBtnContainerSelector() {
        return By.res("tv.fun.filemanager:id/menu_item_hide_id");
    }

    public static BySelector getMenuBtnTextSelector() {
        return By.res("android:id/tv_fun_menu_text");
    }

    public static BySelector getYesBtnOfConfirmDialog() {
        return By.res("tv.fun.filemanager:id/confirm_dialog_btn_confirm");
    }

    public static BySelector getCancelBtnOfConfirmDialog() {
        return By.res("tv.fun.filemanager:id/confirm_dialog_btn_cancel");
    }

    public static void openLocalFilesCard(UiDevice device) {
        final int positionX = 1348;
        final int positionY = 408;
        String message = "Error in openLocalFilesCard(), click on AllFiles card.";
        Assert.assertTrue(message, device.click(positionX, positionY));
        ShellUtils.systemWait(WAIT);
    }

    public static void navigateToSpecifiedPath(UiDevice device, String path) {
        List<String> dirs = parsePath(path);
        for (String dir : dirs) {
            clickOnSpecifiedItemFromCurrentDir(device, dir);
        }
    }

    public static void navigateAndOpenSpecifiedFile(UiDevice device, String fileAbsPath) {
        navigateToSpecifiedPath(device, fileAbsPath);
    }

    // Item for directory and file
    public static void clickOnSpecifiedItemFromCurrentDir(
            UiDevice device, String dirName, boolean flag_bottom) {
        final int ScrollSteps = 5;
        String scrollViewId = "tv.fun.filemanager:id/activity_sub_grid";
        UiScrollable fileList = new UiScrollable(new UiSelector().resourceId(scrollViewId));
        fileList.setAsVerticalList();

        try {
            fileList.scrollTextIntoView(dirName);
            ShellUtils.systemWait(SHORT_WAIT);
            if (flag_bottom) {
                fileList.scrollForward(ScrollSteps);
                ShellUtils.systemWait(SHORT_WAIT);
            }
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            String message = String.format(
                    "Error in clickOnSpecifiedItemFromCurrentDir(), scroll to UI object %s.", dirName);
            Assert.assertTrue(message, false);
        }

        UiObject2 dir = device.findObject(By.text(dirName));
        ACTION.doClickActionAndWait(dir);
    }

    public static void clickOnSpecifiedItemFromCurrentDir(UiDevice device, String dirName) {
        clickOnSpecifiedItemFromCurrentDir(device, dirName, false);
    }

    // path like: android/data/tv.fun.filemanager
    // or: /android/data/tv.fun.filemanager/
    private static List<String> parsePath(String path) {
        int levels = 20;
        List<String> dirs = new ArrayList<>(levels);

        String[] tempDirs = path.split("/");
        for (String dir : tempDirs) {
            if (!"".equals(dir)) {
                dirs.add(dir);
            }
        }
        if (dirs.size() == 0) {
            String message = "Error in parsePath(), the dirs size is 0.";
            Assert.assertTrue(message, false);
        }

        return dirs;
    }

    public static void showMenuAndRequestFocus() {
        ACTION.doDeviceActionAndWait(new DeviceActionMenu());
        ACTION.doDeviceActionAndWait(new DeviceActionMoveDown());  // request focus
    }

    public static void showMenuAndClickRemoveBtn() {
        showMenuAndRequestFocus();
        ACTION.doDeviceActionAndWait(new DeviceActionEnter());
    }

    public static void showMenuAndClickHideBtn() {
        showMenuAndRequestFocus();
        ACTION.doDeviceActionAndWait(new DeviceActionMoveRight());
        ACTION.doDeviceActionAndWait(new DeviceActionEnter());
    }

}
