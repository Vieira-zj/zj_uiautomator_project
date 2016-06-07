package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import junit.framework.Assert;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/6/6.
 *
 * Include the tasks on settings page
 */
public final class TaskSettings {

//    private final static String TAG = TaskSettings.class.getSimpleName();
    private static UiActionsManager ACTION = UiActionsManager.getInstance();

    public static void moveToSpecifiedSettingsItem(UiDevice device, UiObject2 item) {

        final int maxMoveTimes = 15;
        int i = 0;
        while (!item.isFocused() && ((i++) < maxMoveTimes)) {
            ACTION.doUiActionAndWait(device, new UiActionMoveDown());
        }

        String message =
                "Error in moveToSpecifiedSettingsItem(), the settings item is NOT focused.";
        Assert.assertTrue(message, item.isFocused());
    }

    public static void scrollMoveToAndClickSettingsItem(UiDevice device, String itemText) {

        String message;
        final String scrollClass = "android.widget.ScrollView";
        UiScrollable scroll = new UiScrollable(new UiSelector().className(scrollClass));
        scroll.setAsVerticalList();

        try {
            scroll.scrollTextIntoView(itemText);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
            message = String.format(
                    "Error in scrollMoveToAndClickSettingsItem(), the scrollable ui object %s is NOT found.", itemText);
            Assert.assertTrue(message, false);
        }

        UiObject2 settingsItem = device.findObject(By.text(itemText)).getParent();
        message = "Error in scrollMoveToAndClickSettingsItem(), the settings item is NOT found.";
        Assert.assertNotNull(message, settingsItem);

        settingsItem.click();
        ShellUtils.systemWait(WAIT);
    }

}
