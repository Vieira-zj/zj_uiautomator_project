package com.example.zhengjin.funsettingsuitest.testcases;

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiWatcher;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcategory.Category24x7LauncherTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryHomeVideoTabTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveLeft;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskPlayingVideos;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskVideoHomeTab;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.Locale;

/**
 * Created by zhengjin on 2016/7/28.
 * <p>
 * Test playing videos, include test cases to run more than 8 hours.
 */

@RunWith(AndroidJUnit4.class)
public final class TestPlayingVideos {

    private static final String TAG = TestPlayingVideos.class.getSimpleName();

    private UiDevice mDevice;
    private UiActionsManager mAction;
    private TaskPlayingVideos mTask;
    private TaskVideoHomeTab mTaskVideoHomeTab;

    private static final String TEXT_CARD_FILM = "电影";
    private static final String TEXT_CARD_TV = "电视剧";

    private final int RANDOM_SELECT_NUM = 5;
    private final int PLAY_TIME_BY_SEC = 30;
    private final int RUN_TIMES = 3;

    @Before
    public void setUp() {
        mAction = UiActionsManager.getInstance();
        mTask = TaskPlayingVideos.getInstance();
        mTaskVideoHomeTab = TaskVideoHomeTab.getInstance();

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.registerWatcher("BufferRefreshFailedWatcher", new BufferRefreshFailedWatcher());

        ShellUtils.clearLogcatLog();
        TaskLauncher.backToLauncherByShell();
    }

    @After
    public void clearUp() {
        TaskLauncher.backToLauncherByShell();
    }

    @Test
    @Category(Category24x7LauncherTests.class)
    public void test11PlayerUIWhenPauseFilm() {
        mTaskVideoHomeTab.openSubPageFromLauncherHomeByText(TEXT_CARD_FILM);
        mTaskVideoHomeTab.navigateToVideoInAllTabOnVideoSubPage();
        String filmTitle = mTaskVideoHomeTab.randomSelectVideoAndOpenDetails(RANDOM_SELECT_NUM);

        this.logTestStart(filmTitle);
        TaskPlayingVideos.videoInfo info = mTask.getFilmInfoByName(filmTitle);
        mTaskVideoHomeTab.enterOnPlayButtonOnVideoDetailsPage(info);
        mTask.waitForVideoPlayerOpenedAndOnTop();

        try {
            for (int i = 0; i < RUN_TIMES; i++) {
                this.verifyPlayerUIWhenPause(filmTitle, PLAY_TIME_BY_SEC);
            }
        } finally {
            this.logTestEnd(filmTitle);
        }

        if (info != null && info.isVip()) {
            mTask.resetVideoProcessToBeginning();
            this.isPlayerProcessReset();
        }
    }

    @Test
    @Category(Category24x7LauncherTests.class)
    public void test12PlayingAndPauseFilm() {
        mTaskVideoHomeTab.openSubPageFromLauncherHomeByText(TEXT_CARD_FILM);
        mTaskVideoHomeTab.navigateToVideoInAllTabOnVideoSubPage();
        String filmTitle = mTaskVideoHomeTab.randomSelectVideoAndOpenDetails(RANDOM_SELECT_NUM);

        this.logTestStart(filmTitle);
        TaskPlayingVideos.videoInfo info = mTask.getFilmInfoByName(filmTitle);
        mTaskVideoHomeTab.enterOnPlayButtonOnVideoDetailsPage(info);
        mTask.waitForVideoPlayerOpenedAndOnTop();

        try {
            for (int i = 0; i < RUN_TIMES; i++) {
                this.verifyPlayingFilm(PLAY_TIME_BY_SEC);
            }
        } finally {
            this.logTestEnd(filmTitle);
        }

        if (info != null && info.isVip()) {
            mTask.resetVideoProcessToBeginning();
            this.isPlayerProcessReset();
        }
    }

    @Test
    @Category({Category24x7LauncherTests.class, CategoryDemoTests.class})
    public void test21PlayingPartTvSeriesInSeqOnDetails() {
        mTaskVideoHomeTab.openSubPageFromLauncherHomeByText(TEXT_CARD_TV);
        mTaskVideoHomeTab.navigateToVideoInAllTabOnVideoSubPage();
        String tvTitle = mTaskVideoHomeTab.randomSelectVideoAndOpenDetails(RANDOM_SELECT_NUM);

        this.logTestStart(tvTitle);
        TaskPlayingVideos.videoInfo info = mTask.getTvInfoByName(tvTitle);
        int latestNum = this.getTotalNumberOfTvSeriesToPlay(tvTitle, info);
        int totalPlayNum = latestNum <= RUN_TIMES ? latestNum : RUN_TIMES;
        Log.d(TAG, String.format("ZJ, the total times %d", totalPlayNum));
        try {
            for (int idx = 1; idx <= totalPlayNum; idx++) {
                this.selectAndPlaySpecifiedNumberOfTvSeries(idx, info);
                this.verifyPlayTvSeriesInSeqOnDetails(tvTitle, idx, PLAY_TIME_BY_SEC);
            }
        } finally {
            this.logTestEnd(tvTitle);
        }
    }

    @Test
    @Category({Category24x7LauncherTests.class, CategoryDemoTests.class})
    public void test22PlayingPartTvSeriesInSeqBySwipe() {
//        mTaskVideoHomeTab.openSubPageFromLauncherHomeByText(TEXT_CARD_TV);
        mTaskVideoHomeTab.openVideoSubPageFromCateDetailsByText(TEXT_CARD_TV);
        mTaskVideoHomeTab.navigateToVideoInAllTabOnVideoSubPage();
        String tvTitle = mTaskVideoHomeTab.randomSelectVideoAndOpenDetails(RANDOM_SELECT_NUM);
        this.selectAndPlaySpecifiedNumberOfTvSeries(1);

        this.logTestStart(tvTitle);
        TaskPlayingVideos.videoInfo info = mTask.getTvInfoByName(tvTitle);
        int latestNum = this.getTotalNumberOfTvSeriesToPlay(tvTitle, info);
        int totalPlayNum = latestNum <= RUN_TIMES ? latestNum : RUN_TIMES;
        try {
            for (int idx = 1; idx <= totalPlayNum; idx++) {
                this.verifyPlayTvSeriesInSeqBySwipe(tvTitle, idx, PLAY_TIME_BY_SEC);
            }
        } finally {
            this.logTestEnd(tvTitle);
        }
    }

    @Test
    @Category({Category24x7LauncherTests.class})
    public void test23PlayingAllTvSeriesInSeqOnDetails() {
        mTaskVideoHomeTab.openSubPageFromLauncherHomeByText(TEXT_CARD_TV);
        mTaskVideoHomeTab.navigateToVideoInAllTabOnVideoSubPage();
        String tvTitle = mTaskVideoHomeTab.selectVideoAtPositionAndOpenDetails(3);

        this.logTestStart(tvTitle);
        TaskPlayingVideos.videoInfo info = mTask.getTvInfoByName(tvTitle);
        int totalPlayNum = mTask.getLatestTvTotalNumByName(info);
        try {
            for (int idx = 1; idx <= totalPlayNum; idx++) {
                this.selectAndPlaySpecifiedNumberOfTvSeries(idx, info);
                this.verifyPlayTvSeriesInSeqOnDetails(tvTitle, idx, PLAY_TIME_BY_SEC);
            }
        } finally {
            this.logTestEnd(tvTitle);
        }
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class})
    public void test99ClearUpAfterAllTestCasesDone() {
        mTask.destroyInstance();
        mTaskVideoHomeTab.destroyInstance();
    }

    private void logTestStart(String videoTitle) {
        Log.d(TAG, String.format("%s START: %s", ShellUtils.getRunningMethodName(), videoTitle));
    }

    private void logTestEnd(String videoTitle) {
        Log.d(TAG, String.format("%s END: %s", ShellUtils.getRunningMethodName(), videoTitle));
    }

    private void verifyVideoPlayerOnTop() {
        UiObject2 player = mDevice.findObject(mTask.getVideoPlayerByClassSelector());
        TestHelper.assertTrueAndSaveEnvIfFailed("Verify player is playing and on the top."
                , (player != null && player.isEnabled()), TestHelper.SaveEnvType.CAP_AND_DUMP);
        ShellUtils.systemWaitByMillis(TestConstants.SHORT_WAIT);
    }

    private void verifyPlayerUIWhenPause(String filmTitleText, int playTimeBySec) {
        ShellUtils.systemWaitByMillis(playTimeBySec * 1000L);
        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // pause player

        UiObject2 title = mDevice.findObject(mTask.getVideoTitleOfVideoPlayerSelector());
        Assert.assertNotNull(title);
        Assert.assertEquals("Verify film title when pause player.",
                filmTitleText.trim(), title.getText().trim());

        UiObject2 pauseBtn = mDevice.findObject(mTask.getPauseButtonOfVideoPlayerSelector());
        Assert.assertNotNull("Verify pause icon when pause player.", pauseBtn);

        UiObject2 seekBar = mDevice.findObject(mTask.getSeekBarOfVideoPlayerSelector());
        Assert.assertNotNull("Verify seek bar when pause player.", seekBar);

        UiObject2 totalTime =
                mDevice.findObject(mTask.getTotalTimeInSeekBarOfVideoPlayerSelector());
        Assert.assertNotNull(totalTime);
        Assert.assertTrue("Verify film total time when pause player."
                , totalTime.getText().contains(":"));

        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // play
        this.verifyVideoPlayerOnTop();
    }

    private void verifyPlayingFilm(int playTimeBySec) {
        int startTime = mTask.getCurrentPlayTimeInVideoPlayer();
        ShellUtils.systemWaitByMillis(playTimeBySec * 1000L);
        int endTime = mTask.getCurrentPlayTimeInVideoPlayer();

        final int buffer = 10;
        int during = endTime - startTime;
        Assert.assertTrue("Verify the play time when playing film."
                , during >= (playTimeBySec - buffer) && during <= (playTimeBySec + buffer));

        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // play
        this.verifyVideoPlayerOnTop();
    }

    private void selectAndPlaySpecifiedNumberOfTvSeries(int number) {
        mTaskVideoHomeTab.focusOnTvSelectButtonOnVideoDetailsPage();
        UiObject2 cell = mTaskVideoHomeTab.getSpecifiedTvCellByIndex(String.valueOf(number));
        mAction.doClickActionAndWait(cell);
    }

    private void selectAndPlaySpecifiedNumberOfTvSeries(
            int number, TaskPlayingVideos.videoInfo info) {
        mTaskVideoHomeTab.focusOnTvSelectButtonOnVideoDetailsPage();

        UiObject2 cell = mTaskVideoHomeTab.getSpecifiedTvCellByIndex(String.valueOf(number));
        UiObject2 bottomTabContainer = mDevice.findObject(
                mTaskVideoHomeTab.getBottomTabContainerOfVideoDetailsPageSelector());
        if (cell == null && bottomTabContainer != null) {
            mAction.doRepeatDeviceActionAndWait(new DeviceActionMoveDown(), 4);  // move at bottom
            if (info.getIsEnd()) {
                mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
            } else {
                mAction.doDeviceActionAndWait(new DeviceActionMoveLeft());
            }
            cell = mTaskVideoHomeTab.getSpecifiedTvCellByIndex(String.valueOf(number));

        }
        mAction.doClickActionAndWait(cell);
    }

    private int getTotalNumberOfTvSeriesToPlay(String tvTitle, TaskPlayingVideos.videoInfo info) {
        if (info == null) {
            Log.e(TAG, String.format("getTotalNumberOfTvSeriesToPlay, error request info for tv %s"
                    , tvTitle));
            return 1;
        }
        if (info.isVip()) {
            return 1;
        }

        return mTask.getLatestTvTotalNumByName(info);
    }

    private void verifyTvNameAndNumberInVideoPlayer(String tvName, int TvNumber) {
        String titleOnPlayer = String.format(Locale.getDefault(), "%s 第%d集", tvName, TvNumber);
        UiObject2 titleUiObject = mDevice.findObject(mTask.getVideoTitleOfVideoPlayerSelector());
        Assert.assertEquals("Verify the tv series name and number."
                , titleOnPlayer, titleUiObject.getText());
    }

    private void verifyPlayTvSeriesInSeqOnDetails(String tvTitle, int idx, int playTimeBySec) {
        mTask.waitForVideoPlayerOpenedAndOnTop();
        SystemClock.sleep(playTimeBySec * 1000L);

        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // pause
        this.verifyTvNameAndNumberInVideoPlayer(tvTitle, idx);
        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // play
        this.verifyVideoPlayerOnTop();

        mTask.exitVideoPlayerByBack();
        this.verifyTvTitleAndNumberOnDetailsAfterExitPlayer(tvTitle, idx);
    }

    private void verifyTvTitleAndNumberOnDetailsAfterExitPlayer(String tvTitle, int idx) {
        Assert.assertEquals("Verify exit from video player"
                , tvTitle, mTaskVideoHomeTab.waitVideoDetailsPageOpenedAndRetTitle());

        String tvNumber = String.format(Locale.getDefault(), "第%d集", idx);
        UiObject2 numObject =
                mDevice.findObject(mTaskVideoHomeTab.getTvNumberTipsOfVideoDetailsPageSelector());
        Assert.assertEquals("Verify the TV series number in tips.", tvNumber, numObject.getText());
    }

    private void verifyPlayTvSeriesInSeqBySwipe(String tvTitle, int idx, int playTimeBySec) {
        mTask.waitForVideoPlayerOpenedAndOnTop();
        SystemClock.sleep(playTimeBySec * 1000L);

        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // pause
        this.verifyTvNameAndNumberInVideoPlayer(tvTitle, idx);
        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // play
        this.verifyVideoPlayerOnTop();

        mTask.resetVideoProcessToEnd();
    }

    private void isPlayerProcessReset() {
        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // pause player
        UiObject2 curTimeObj =
                mDevice.findObject(mTask.getCurrentTimeInSeekBarOfVideoPlayerSelector());

        final int timeAfterReset = 300;  // 5 minutes
        if (mTask.formatPlayTimeInVideoPlayer(curTimeObj.getText()) > timeAfterReset) {
            Log.w(TAG, "isPlayerProcessReset, failed to reset the video to beginning.");
        }
    }

    private class BufferRefreshFailedWatcher implements UiWatcher {
        @Override
        public boolean checkForCondition() {
            UiObject2 errorText = mDevice.findObject(By.textContains("缓冲失败"));
            if (errorText != null) {
                // if buffer refresh error occur, stop testing process
                Log.e(TAG, "BufferRefreshFailedWatcher, " +
                        "Buffer Refresh Failed, force exit testing process.");
                System.exit(-1);
                return true;
            }

            return false;
        }
    }

}