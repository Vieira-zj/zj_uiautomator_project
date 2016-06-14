package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/6/14.
 *
 * Include the tasks for file manager apk.
 */
public final class TaskFileManager {

    public static String getMainTitleId() {
        return "tv.fun.filemanager:id/activity_sub_title_main";
    }

    public static String getSubTitleId() {
        return "tv.fun.filemanager:id/activity_sub_title_sub";
    }

    public static void openSdcardLocalFilesCard(UiDevice device) {

        final int positionX = 1348;
        final int positionY = 408;
        String message = "Error in openSdcardLocalFilesCard(), click on AllFiles card.";
        Assert.assertTrue(message, device.click(positionX, positionY));
        ShellUtils.systemWait(LONG_WAIT);
    }

    public static void navigateToSpecifiedPath(UiDevice device, String path) {
        List<String> dirs = parsePath(path);

        for (String dir : dirs) {
            clickOnSpecifiedDirFromCurrentDir(device, dir);
        }
    }

    public static void navigateAndOpenSpecifiedFile(UiDevice device, String fileAbsPath) {
        navigateToSpecifiedPath(device, fileAbsPath);
    }

    public static void clickOnSpecifiedDirFromCurrentDir(UiDevice device, String dirName) {

        String scrollViewId = "tv.fun.filemanager:id/activity_sub_grid";
        UiScrollable fileList = new UiScrollable(new UiSelector().resourceId(scrollViewId));
        fileList.setAsVerticalList();

        String message;
        try {
            fileList.scrollTextIntoView(dirName);
            ShellUtils.systemWait(WAIT);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            message = String.format("Error in clickOnSpecifiedDirFromCurrentDir(), scroll to directory %s.", dirName);
            Assert.assertTrue(message, false);
        }

        UiObject2 dir = device.findObject(By.text(dirName));
        dir.click();
        ShellUtils.systemWait(WAIT);
    }

    public static void openSpecifiedFileFromCurrentDir(UiDevice device, String fileName) {
        clickOnSpecifiedDirFromCurrentDir(device, fileName);
    }

    // path like: android/data/tv.fun.filemanager
    // or: /android/data/tv.fun.filemanager/
    private static List<String> parsePath(String path) {

        String[] tempDirs = path.split("/");

        int levels = 20;
        List<String> dirs = new ArrayList<>(levels);
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


}
