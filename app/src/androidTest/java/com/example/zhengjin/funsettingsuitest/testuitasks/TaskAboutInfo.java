package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testsuites.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionCenter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMenu;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_ABOUT_INFO_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2017/1/5.
 * <p>
 * Include the UI selectors and tasks for about info page test cases.
 */
public final class TaskAboutInfo {

    private static TaskAboutInfo instance;
    private UiDevice device;
    private UiActionsManager action;

    public final String ABOUT_INFO_PAGE_TEXT = "关于";

    private TaskAboutInfo() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        action = UiActionsManager.getInstance();
    }

    public static synchronized TaskAboutInfo getInstance() {
        if (instance == null) {
            instance = new TaskAboutInfo();
        }

        return instance;
    }

    public void destroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public BySelector getSettingsAboutInfoPageTitleSelector() {
        return By.res("tv.fun.settings:id/setting_title");
    }

    public BySelector getSettingsAboutInfoSubPageTitleSelector() {
        return By.res("tv.fun.settings:id/setting_title");
    }

    public BySelector getQuestionFeedbackItemSelector() {
        return By.res("tv.fun.settings:id/about_item_feedback");
    }

    public BySelector getQuestionFeedbackSubPageContentSelector() {
        return By.res("tv.fun.settings:id/feedback_tips_5");
    }

    public BySelector getBottomFeedbackMenuSelector() {
        return By.res("android:id/tv_fun_menu");
    }

    public BySelector getCatchLogButtonInFeedbackMenuSelector() {
        return By.res("tv.fun.settings:id/about_item_feedback_menu_tcpdump");
    }

    public BySelector getDumpLogToDiskButtonInFeedbackMenuSelector() {
        return By.res("tv.fun.settings:id/about_item_feedback_menu_dump_log");
    }

    public BySelector getFeedbackMenuButtonTextSelector() {
        return By.res("android:id/tv_fun_menu_text");
    }

    public UiObject2 getTextViewInAboutInfoItem(UiObject2 parent) {
        return parent.findObject(By.clazz(TestConstants.CLASS_TEXT_VIEW));
    }

    public void openAboutInfoHomePage() {
        if (RunnerProfile.isPlatform938) {
            ShellUtils.startSpecifiedActivity(SETTINGS_PKG_NAME, SETTINGS_ABOUT_INFO_ACT);
            ShellUtils.systemWaitByMillis(WAIT);
        } else {
            TaskLauncher.openSpecifiedCardFromSettingsTab(ABOUT_INFO_PAGE_TEXT);
        }

        Assert.assertTrue("openAboutInfoHomePage, open failed!",
                TestHelper.waitForActivityOpenedByShellCmd(
                        SETTINGS_PKG_NAME, SETTINGS_ABOUT_INFO_ACT));
        action.doDeviceActionAndWait(new DeviceActionMoveUp());  // request focus
    }

    public void openSpecifiedAboutInfoItemSubPage(String itemTitle) {
        focusOnSpecifiedAboutInfoItem(itemTitle);
        action.doDeviceActionAndWait(new DeviceActionCenter(), WAIT);
    }

    private void focusOnSpecifiedAboutInfoItem(String itemTitle) {
        UiObject2 title = device.findObject(By.text(itemTitle));
        for (int i = 0, moves = 7; i < moves; i++) {
            if (title.isSelected()) {
                return;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveDown());
        }

        Assert.assertTrue("focusOnSpecifiedAboutInfoItem, failed to focus on item " + itemTitle
                , false);
    }

    public void openBottomFeedBackMenu() {
        action.doDeviceActionAndWait(new DeviceActionMenu());
        Assert.assertTrue("openBottomFeedBackMenu, open bottom menu failed!",
                TestHelper.waitForUiObjectExist(this.getBottomFeedbackMenuSelector()));
    }

    private void focusOnSpecifiedButtonInFeedbackMenu(String btnText) {
        UiObject2 menuBtn = device.findObject(By.text(btnText)).getParent();
        for (int i = 0, moves = 3; i < moves; i++) {
            if (menuBtn.isFocused()) {
                return;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveRight());
        }

        Assert.assertTrue("focusOnSpecifiedButtonInFeedbackMenu, failed to focus on menu button"
                + btnText, false);
    }

    public void enterOnSpecifiedButtonInFeedbackMenu(String btnText) {
        this.focusOnSpecifiedButtonInFeedbackMenu(btnText);
        action.doDeviceActionAndWait(new DeviceActionCenter());
    }

}
