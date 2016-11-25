package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LAUNCHER_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.VIDEO_SUB_PAGE_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by Vieira on 2016/7/4.
 * <p>
 * Include UI selectors and tasks on video tab of home page.
 */
public final class TaskVideoHomeTab {

    private static TaskVideoHomeTab instance = null;

    private UiDevice device;
    private UiActionsManager action;

    private TaskVideoHomeTab() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        action = UiActionsManager.getInstance();
    }

    public static synchronized TaskVideoHomeTab getInstance() {
        if (instance == null) {
            instance = new TaskVideoHomeTab();
        }
        return instance;
    }

    public void destroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public BySelector getCardTitleOfLauncherHomeLeftAreaSelector() {
        return By.res("com.bestv.ott:id/title");
    }

    public BySelector getCardMainTitleOfLauncherHomeRightAreaSelector() {
        return By.res("com.bestv.ott:id/maintitle");
    }

    public BySelector getCardContainerOfVideoRecommendPageSelector() {
        return By.res("com.bestv.ott:id/grid");
    }

    public BySelector getTopTabTextOfVideoSubPageSelector() {
        return By.res("com.bestv.ott:id/tab_title");
    }

    public BySelector getCardMainTitleOfVideoSubPageSelector() {
        return By.res("com.bestv.ott:id/maintitle");
    }

    public BySelector getCardSubTitleOfVideoSubPageSelector() {
        return By.res("com.bestv.ott:id/subtitle");
    }

    public BySelector getVideoTitleOfVideoSubPageSelector() {
        return By.res("com.bestv.ott:id/title");
    }

    public BySelector getTitleTextOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/detail_title");
    }

    public BySelector getPlayButtonOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/detail_play_button");
    }

    public BySelector getRelatedVideoListOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/relate_list_view");
    }

    public UiObject2 getSpecifiedCardFromHomeLeftAreaByText(String search) {
        List<UiObject2> textList =
                device.findObjects(this.getCardTitleOfLauncherHomeLeftAreaSelector());
        return this.getSpecifiedTextViewFromUiCollection(textList, search);
    }

    public UiObject2 getSpecifiedCardFromHomeRightAreaByText(String search) {
        List<UiObject2> textList =
                device.findObjects(this.getCardMainTitleOfLauncherHomeRightAreaSelector());
        return this.getSpecifiedTextViewFromUiCollection(textList, search);
    }

    public void openSubPageFromLauncherHomeByText(String cardText) {
        UiObject2 card = this.getSpecifiedCardFromHomeLeftAreaByText(cardText);
        action.doClickActionAndWait(card);  // request focus

        action.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        Assert.assertTrue(String.format("openSubPageFromLauncherHomeByText, " +
                        "failed to open sub page %s", cardText),
                TestHelper.waitForActivityOpenedByShellCmd(LAUNCHER_PKG_NAME, VIDEO_SUB_PAGE_ACT));
        ShellUtils.systemWaitByMillis(LONG_WAIT);
    }

    public String waitVideoDetailsPageOpenedAndRetTitle() {
        Assert.assertTrue("waitVideoDetailsPageOpenedAndRetTitle, " +
                        "failed to open the video details page."
                , TestHelper.waitForUiObjectEnabledByProperty(
                        this.getTitleTextOfVideoDetailsPageSelector()));

        return device.findObject(this.getTitleTextOfVideoDetailsPageSelector()).getText();
    }

    public void enterOnPlayButtonOnVideoDetailsPage() {
        UiObject2 playBtn = device.findObject(this.getPlayButtonOfVideoDetailsPageSelector());
        Assert.assertTrue("enterOnPlayButtonOnVideoDetailsPage, the play button is NOT focused."
                , playBtn.isFocusable());
        action.doDeviceActionAndWait(new DeviceActionEnter());
    }

    private UiObject2 getSpecifiedTextViewFromUiCollection(
            List<UiObject2> list, String search) {
        Assert.assertFalse("getSpecifiedTextViewFromUiCollection, " +
                "error: the UI collection size is zero.", list.size() == 0);
        UiObject2 retUiObject = null;

        for (UiObject2 uiText : list) {
            if (search.equals(uiText.getText())) {
                retUiObject = uiText.getParent();
            }
        }
        Assert.assertNotNull(String.format("getSpecifiedTextViewFromUiCollection, " +
                "text(%s) is NOT found on launcher home page.", search), retUiObject);

        return retUiObject;
    }

}
