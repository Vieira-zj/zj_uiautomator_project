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
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryHomeVideoTabTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
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
        TaskLauncher.backToLauncher();
    }

    @After
    public void clearUp() {
        TaskLauncher.backToLauncher();
    }

    @Test
    @Category(Category24x7LauncherTests.class)
    public void test11PlayerUIWhenPauseFilm() {
        mTaskVideoHomeTab.openSubPageFromLauncherHomeByText(TEXT_CARD_FILM);
        mTaskVideoHomeTab.navigateToVideoInAllTabOnVideoSubPage();
        String filmTitle = mTaskVideoHomeTab.randomSelectVideoAndOpenDetails(RANDOM_SELECT_NUM);

        TaskPlayingVideos.videoInfo info = mTask.getFilmInfoByName(filmTitle);
        mTaskVideoHomeTab.enterOnPlayButtonOnVideoDetailsPage(info);
        mTask.waitForVideoPlayerOpenedAndOnTop();

        for (int i = 0; i < RUN_TIMES; i++) {
            this.verifyPlayerUIWhenPause(filmTitle, PLAY_TIME_BY_SEC);
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

        TaskPlayingVideos.videoInfo info = mTask.getFilmInfoByName(filmTitle);
        mTaskVideoHomeTab.enterOnPlayButtonOnVideoDetailsPage(info);
        mTask.waitForVideoPlayerOpenedAndOnTop();

        for (int i = 0; i < RUN_TIMES; i++) {
            this.verifyPlayingFilm(PLAY_TIME_BY_SEC);
        }

        if (info != null && info.isVip()) {
            mTask.resetVideoProcessToBeginning();
            this.isPlayerProcessReset();
        }
    }

    @Test
    @Category(Category24x7LauncherTests.class)
    public void test21PlayingTvSeriesInSeqOnDetails() {
        mTaskVideoHomeTab.openSubPageFromLauncherHomeByText(TEXT_CARD_TV);
        mTaskVideoHomeTab.navigateToVideoInAllTabOnVideoSubPage();
        String tvTitle = mTaskVideoHomeTab.randomSelectVideoAndOpenDetails(RANDOM_SELECT_NUM);

        int latestNum = this.getTotalNumberOfTvSeriesToPlay(tvTitle);
        int totalPlayNum = latestNum <= RUN_TIMES ? latestNum : RUN_TIMES;
        for (int idx = 1; idx <= totalPlayNum; idx++) {
            this.verifyPlayTvSeriesInSeqOnDetails(tvTitle, idx, PLAY_TIME_BY_SEC);
        }
    }

    @Test
    @Category(Category24x7LauncherTests.class)
    public void test22PlayingTvSeriesInSeqBySwipe() {
        mTaskVideoHomeTab.openSubPageFromLauncherHomeByText(TEXT_CARD_TV);
        mTaskVideoHomeTab.navigateToVideoInAllTabOnVideoSubPage();
        String tvTitle = mTaskVideoHomeTab.randomSelectVideoAndOpenDetails(RANDOM_SELECT_NUM);
        this.selectAndPlaySpecifiedNumberOfTvSeries(1);

        int latestNum = this.getTotalNumberOfTvSeriesToPlay(tvTitle);
        int totalPlayNum = latestNum <= RUN_TIMES ? latestNum : RUN_TIMES;
        for (int idx = 1; idx <= totalPlayNum; idx++) {
            this.verifyPlayTvSeriesInSeqBySwipe(tvTitle, idx, PLAY_TIME_BY_SEC);
        }
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class})
    public void test99ClearUpAfterAllTestCasesDone() {
        mTask.destroyInstance();
        mTaskVideoHomeTab.destroyInstance();
    }

    private void verifyVideoPlayerOnTop() {
        UiObject2 player = mDevice.findObject(mTask.getVideoPlayerByClassSelector());
        TestHelper.assertTrueAndCaptureIfFailed("Verify player is playing and on the top."
                , (player != null && player.isEnabled()));
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
        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // pause player
        UiObject2 curTime =
                mDevice.findObject(mTask.getCurrentTimeInSeekBarOfVideoPlayerSelector());
        int startTime = formatFilmPlayTime(curTime.getText());

        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // play
        ShellUtils.systemWaitByMillis(playTimeBySec * 1000L);

        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // pause player
        curTime = mDevice.findObject(mTask.getCurrentTimeInSeekBarOfVideoPlayerSelector());
        int endTime = formatFilmPlayTime(curTime.getText());

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

    private int getTotalNumberOfTvSeriesToPlay(String tvTitle) {
        TaskPlayingVideos.videoInfo info = mTask.getTvInfoByName(tvTitle);
        if (info == null) {
            Log.e(TAG, String.format("getTotalNumberOfTvSeriesToPlay, error request info for tv %s"
                    , tvTitle));
            return 1;
        }
        if (info.isVip()) {
            return 1;
        }

        return mTask.getLatestTvTotalNumByName(tvTitle);
    }

    private void verifyTvNameAndNumberInVideoPlayer(String tvName, int TvNumber) {
        String titleOnPlayer = String.format(Locale.getDefault(), "%s 第%d集", tvName, TvNumber);
        UiObject2 titleUiObject = mDevice.findObject(mTask.getVideoTitleOfVideoPlayerSelector());
        Assert.assertEquals("Verify the tv series name and number."
                , titleOnPlayer, titleUiObject.getText());
    }

    private void verifyPlayTvSeriesInSeqOnDetails(String tvTitle, int idx, int playTimeBySec) {
        this.selectAndPlaySpecifiedNumberOfTvSeries(idx);
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
        if (this.formatFilmPlayTime(curTimeObj.getText()) > timeAfterReset) {
            Log.w(TAG, "isPlayerProcessReset, failed to reset the video to beginning.");
        }
    }

    private int formatFilmPlayTime(String playTime) {
        String[] items = playTime.split(":");

        if (items.length == 2) {
            return 60 * convertPlayTimeToInt(items[0]) + Integer.parseInt(items[1]);
        }
        if (items.length == 3) {
            return 60 * (60 * convertPlayTimeToInt(items[0])) +
                    60 * convertPlayTimeToInt(items[1]) + Integer.parseInt(items[2]);
        }
        return -1;
    }

    private int convertPlayTimeToInt(String playTime) {
        if ("00".equals(playTime)) {
            return 0;
        }
        if (playTime.startsWith("0")) {
            return Integer.parseInt(playTime.substring(1));
        }
        return Integer.parseInt(playTime);
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