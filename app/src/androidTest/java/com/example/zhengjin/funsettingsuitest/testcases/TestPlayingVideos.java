package com.example.zhengjin.funsettingsuitest.testcases;

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
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveLeft;
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

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;

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
//        TaskLauncher.backToLauncher();
    }

    @Test
    @Category(Category24x7LauncherTests.class)
    public void test11PlayerUIWhenPauseFilm() {
        mTaskVideoHomeTab.openSubPageFromLauncherHomeByText("电影");
        this.navigateToVideoInAllTabOnVideoSubPage();
        String filmTitle = mTaskVideoHomeTab.randomSelectVideoAndOpenDetails(5);
        mTaskVideoHomeTab.enterOnPlayButtonOnVideoDetailsPage();
        mTask.waitForVideoPlayerOpenedAndOnTop();

        for (int i = 0, retryTime = 3; i > retryTime; i++) {
            ShellUtils.systemWaitByMillis(3 * 60 * 1000L);  // set play time, 3 minutes
            this.isVideoPlayerOnTop();
            mAction.doDeviceActionAndWait(new DeviceActionEnter());  // pause player
            this.verifyPlayerUIWhenPause(filmTitle);
        }

        TaskPlayingVideos.videoInfo info = mTask.getFilmInfoByName(filmTitle);
        if (info != null && info.isVip()) {
            mTask.resetVideoProcessToStart(0.3f);
            this.isPlayerProcessReset(mDevice);
        }
    }

    @Test
    @Category(Category24x7LauncherTests.class)
    public void test12PlayingAndPauseFilm() {
        mTaskVideoHomeTab.openSubPageFromLauncherHomeByText("电影");
        this.navigateToVideoInAllTabOnVideoSubPage();
        String filmTitle = mTaskVideoHomeTab.randomSelectVideoAndOpenDetails(5);
        mTaskVideoHomeTab.enterOnPlayButtonOnVideoDetailsPage();
        mTask.waitForVideoPlayerOpenedAndOnTop();

        for (int i = 0, replayTimes = 3; i < replayTimes; i++) {
            this.verifyPlayingFilm(60);  // set play time, 1 minutes
        }

        TaskPlayingVideos.videoInfo info = mTask.getFilmInfoByName(filmTitle);
        if (info !=null && info.isVip()) {
            mTask.resetVideoProcessToStart(0.3f);
            this.isPlayerProcessReset(mDevice);
        }
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class})
    public void test99ClearUpAfterAllTestCasesDone() {
        mTask.destroyInstance();
        mTaskVideoHomeTab.destroyInstance();
    }

    private void navigateToVideoInAllTabOnVideoSubPage() {
        mAction.doDeviceActionAndWait(new DeviceActionMoveLeft(), LONG_WAIT);
        mAction.doDeviceActionAndWait(new DeviceActionMoveDown(), TestConstants.WAIT);
    }

    private void isVideoPlayerOnTop() {
        UiObject2 player = mDevice.findObject(mTask.getVideoPlayerByClassSelector());
        boolean ret = (player != null) && player.isEnabled();
        TestHelper.assertTrueAndCaptureIfFailed("Verify player is playing and on the top.", ret);
    }

    private void verifyPlayerUIWhenPause(String filmTitleText) {
        UiObject2 title = mDevice.findObject(mTask.getVideoNameInVideoPlayerSelector());
        Assert.assertNotNull(title);
        Assert.assertEquals("Verify film title when pause player.",
                filmTitleText.trim(), title.getText().trim());

        UiObject2 pauseBtn = mDevice.findObject(mTask.getPauseButtonInVideoPlayerSelector());
        Assert.assertNotNull("Verify pause icon when pause player.", pauseBtn);

        UiObject2 seekBar = mDevice.findObject(mTask.getSeekBarInVideoPlayerSelector());
        Assert.assertNotNull("Verify seek bar when pause player.", seekBar);

        UiObject2 totalTime =
                mDevice.findObject(mTask.getTotalTimeInSeekBarOfVideoPlayerSelector());
        Assert.assertNotNull(totalTime);
        Assert.assertTrue("Verify film total time when pause player.",
                totalTime.getText().contains(":"));
    }

    private void verifyPlayingFilm(int playTime) {
        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // pause player
        UiObject2 curTime =
                mDevice.findObject(mTask.getCurrentTimeInSeekBarOfVideoPlayerSelector());
        int startTime = formatFilmPlayTime(curTime.getText());

        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // play
        ShellUtils.systemWaitByMillis(playTime * 1000L);

        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // pause player
        curTime = mDevice.findObject(mTask.getCurrentTimeInSeekBarOfVideoPlayerSelector());
        int endTime = formatFilmPlayTime(curTime.getText());

        final int testBuffer = 5;
        Assert.assertTrue("Verify play time when playing film."
                , (endTime - startTime) >= (playTime - testBuffer));

        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // play
        this.isVideoPlayerOnTop();
    }

    private void isPlayerProcessReset(UiDevice device) {
        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // pause player

        UiObject2 curTimeObj =
                device.findObject(mTask.getCurrentTimeInSeekBarOfVideoPlayerSelector());
        Log.d(TAG, String.format("Video process is reset to %s", curTimeObj.getText()));

        int curPlayTime = this.formatFilmPlayTime(curTimeObj.getText());
        if (curPlayTime > 30) {
            Log.w(TAG, "isPlayerProcessReset, failed to reset the video to start.");
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
            Log.d(TAG, "Invoke BufferRefreshFailedWatcher.checkForCondition().");

            UiObject2 errorText = mDevice.findObject(By.textContains("缓冲失败"));
            if (errorText != null) {
                // if buffer refresh error occur, stop testing process
                Log.d(TAG, "Found error(Buffer Refresh Failed), force exit testing process.");
                int existCode = 0;
                System.exit(existCode);
                return true;
            }

            return false;
        }
    }

}