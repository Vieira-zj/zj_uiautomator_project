package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testsuites.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveLeft;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import java.util.List;
import java.util.Random;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LAUNCHER_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_TIME_OUT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.VIDEO_SUB_PAGE_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;
import static com.example.zhengjin.funsettingsuitest.utils.ShellCmdUtils.TAG;

/**
 * Created by Vieira on 2016/7/4.
 * <p>
 * Include UI selectors and tasks on video tab of home page.
 */
public final class TaskVideoHomeTab {

    private static TaskVideoHomeTab instance = null;

    private UiDevice device;
    private UiActionsManager action;

    public static final String TEXT_CARD_FILM = "电影";
    public static final String TEXT_CARD_TV = "电视剧";

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

    private BySelector getCardTitleOfLauncherHomeLeftAreaSelector() {
        return By.res("com.bestv.ott:id/title");
    }

    private BySelector getCardMainTitleOfLauncherHomeRightAreaSelector() {
        return By.res("com.bestv.ott:id/maintitle");
    }

    private BySelector getAllImagesOfMainCateOnCateDetailsSelector() {
        return By.res("com.bestv.ott:id/poster");
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

    private BySelector getPlayButtonOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/detail_play_button");
    }

    private BySelector getSelectButtonOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/detail_select_button");
    }

    private BySelector getTryWatchButtonOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/detail_try_button");
    }

    public BySelector getTvNumberTipsOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/detail_tip_button");
    }

    public BySelector getRelatedVideoListOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/relate_list_view");
    }

    private BySelector getAllTvCellsOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/tv_cell");
    }

    public BySelector getBottomTabContainerOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/episode_view");
    }

    public void openFilmSubPageFromLauncherHome() {
        if (RunnerProfile.isPlatform938) {
            this.openFilmSubPageFromLauncherHomeByMove();
        } else {
            this.openSubPageFromLauncherHomeByText(TEXT_CARD_FILM);
        }
    }

    public void openTvSubPageFromLauncherHome() {
        if (RunnerProfile.isPlatform938) {
            this.openTvSubPageFromLauncherHomeByMove();
        } else {
            this.openSubPageFromLauncherHomeByText(TEXT_CARD_TV);
        }
    }

    private void openFilmSubPageFromLauncherHomeByMove() {
        action.doChainedDeviceActionAndWait(new DeviceActionMoveDown())
                .doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        this.waitForVideoSubPageOpened(TEXT_CARD_FILM);
    }

    private void openTvSubPageFromLauncherHomeByMove() {
        action.doChainedDeviceActionAndWait(new DeviceActionMoveDown())
                .doChainedDeviceActionAndWait(new DeviceActionMoveRight())
                .doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        this.waitForVideoSubPageOpened(TEXT_CARD_TV);
    }

    private void openSubPageFromLauncherHomeByText(String cardTitle) {
        UiObject2 card = this.getSpecifiedCardFromHomeLeftAreaByText(cardTitle);
        action.doClickActionAndWait(card);  // request focus
        action.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        this.waitForVideoSubPageOpened(cardTitle);
    }

    public void openVideoSubPageFromCateDetailsByText(String title) {
        this.openCateDetailsSubPageFromLauncherHomeByMove();
        UiObject2 card = this.getMainCateCardOnCateDetailsByText(title);
        action.doClickActionAndWait(card);
        action.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
        this.waitForVideoSubPageOpened(title);
    }

    private void openCateDetailsSubPageFromLauncherHomeByMove() {
        action.doChainedDeviceActionAndWait(new DeviceActionMoveRight())
                .doRepeatDeviceActionAndWait(new DeviceActionMoveDown(), 2);
        action.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);
    }

    @Nullable
    private UiObject2 getMainCateCardOnCateDetailsByText(String title) {
        List<UiObject2> images =
                device.findObjects(this.getAllImagesOfMainCateOnCateDetailsSelector());
        for (UiObject2 image : images) {
            UiObject2 container = image.getParent();
            if (container.findObject(By.text(title)) != null) {
                return container;
            }
        }

        return null;
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

    private void waitForVideoSubPageOpened(String title) {
        waitForVideoSubPageOpened(title, true);
    }

    public void waitForVideoSubPageOpened(String title, boolean isWaitLoading) {
        Assert.assertTrue(String.format("waitForVideoSubPageOpened, " +
                        "failed to open sub page %s", title),
                TestHelper.waitForActivityOpenedByShellCmd(LAUNCHER_PKG_NAME, VIDEO_SUB_PAGE_ACT));
        if (isWaitLoading) {
            TestHelper.waitForLoadingComplete();
        } else {
            ShellUtils.systemWaitByMillis(LONG_WAIT);
        }
    }

    public void navigateToAllTabAndSelectVideoOnVideoSubPage() {
        action.doDeviceActionAndWait(new DeviceActionMoveLeft(), LONG_WAIT);
        TestHelper.waitForLoadingComplete();
        action.doDeviceActionAndWait(new DeviceActionMoveDown(), WAIT);
    }

    public String waitVideoDetailsPageOpenedAndRetTitle() {
        TestHelper.waitForLoadingComplete();
        Assert.assertTrue("waitVideoDetailsPageOpenedAndRetTitle, failed open video details page."
                , TestHelper.waitForUiObjectEnabledByCheckIsEnabled(
                        this.getTitleTextOfVideoDetailsPageSelector(), LONG_TIME_OUT));

        return device.findObject(this.getTitleTextOfVideoDetailsPageSelector()).getText();
    }

    public String selectVideoAtPositionAndOpenDetails(int position) {
        if (position < 0) {
            action.doDeviceActionAndWait(new DeviceActionMoveLeft());
        }
        for (int i = 0; i < position; i++) {
            action.doDeviceActionAndWait(new DeviceActionMoveRight());
        }
        action.doDeviceActionAndWait(new DeviceActionEnter());

        return this.waitVideoDetailsPageOpenedAndRetTitle();
    }

    public String selectVideoInSeqFromAllListAndOpenDetails() {
        action.doChainedDeviceActionAndWait(new DeviceActionMoveRight())
                .doDeviceActionAndWait(new DeviceActionEnter());
        return waitVideoDetailsPageOpenedAndRetTitle();
    }

    public String randomSelectVideoAndOpenDetails(int randomInt) {
        int moveTimes = new Random().nextInt(randomInt);
        for (int j = 0; j <= moveTimes; j++) {
            action.doDeviceActionAndWait(new DeviceActionMoveRight());
        }
        Log.d(TAG, String.format("randomSelectVideoAndOpenDetails, select film at position: %d"
                , moveTimes));

        action.doDeviceActionAndWait(new DeviceActionEnter());
        return this.waitVideoDetailsPageOpenedAndRetTitle();
    }

    public void enterOnPlayButtonOnVideoDetailsPage(TaskPlayingVideos.videoInfo info) {
        UiObject2 btn;
        if (!RunnerProfile.isAccountVipFree && info != null && info.isVip()) {
            btn = device.findObject(this.getTryWatchButtonOfVideoDetailsPageSelector());
        } else {
            btn = device.findObject(this.getPlayButtonOfVideoDetailsPageSelector());
        }

        if (!btn.isFocused()) {
            action.doClickActionAndWait(btn);
        }
        action.doDeviceActionAndWait(new DeviceActionEnter());
    }

    public void focusOnTvSelectButtonOnVideoDetailsPage() {
        UiObject2 selectBtn = device.findObject(this.getSelectButtonOfVideoDetailsPageSelector());
        if (!selectBtn.isFocused()) {
            action.doClickActionAndWait(selectBtn, WAIT);
        }
    }

    @Nullable
    public UiObject2 getSpecifiedTvCellByIndex(String index) {
        List<UiObject2> tvCells =
                device.findObjects(this.getAllTvCellsOfVideoDetailsPageSelector());
        for (UiObject2 cell : tvCells) {
            if (index.equals(cell.getText())) {
                return cell;
            }
        }

        return null;
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
