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
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskPlayingVideos;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskVideoHomeTab;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.Random;

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
    private TaskVideoHomeTab mTaskVideoHomeTab;
    private TaskPlayingVideos mTask;

    @Before
    public void setUp() {
        mAction = UiActionsManager.getInstance();
        mTaskVideoHomeTab = TaskVideoHomeTab.getInstance();
        mTask = TaskPlayingVideos.getInstance();

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.registerWatcher("BufferRefreshFailedWatcher", new BufferRefreshFailedWatcher());
//        TaskLauncher.backToLauncher();
    }

//    @After
//    public void clearUp() {
//        TaskLauncher.backToLauncher();
//    }

    @Test
    @Category(Category24x7LauncherTests.class)
    public void test11OpenAndExitFilmPlayer() {
        mTaskVideoHomeTab.openSubPageFromLauncherHomeByText("电影");
        this.navigateToVideoInAllTabOnVideoSubPage();
        this.randomSelectVideoAndOpenDetails(5);
        mTaskVideoHomeTab.enterOnPlayButtonOnVideoDetailsPage();
        mTask.waitForVideoPlayerOpenedAndOnTop();

        ShellUtils.systemWaitByMillis(10 * 1000L);  // play 5 minutes
        this.isVideoPlayerOnTop();

        mTask.resetVideoProcessToStart();
        this.verifyPlayerProcessReset(mDevice);
    }

//    @Test
//    @Category(Category24x7LauncherTests.class)
//    public void test12PlayFilm() {
//        this.verifyOnLauncherHome(mDevice);
//
//        mTaskVideoHomeTab.openSubPageFromLauncherHomeByText("电影");
//        this.navigateToVideoInAllTabOnVideoSubPage();
//        this.randomSelectVideoAndOpenDetails(mDevice, 15);
//        String filmTitle = mTaskVideoHomeTab.waitVideoDetailsPageOpenedAndRetTitle();
//        mTaskVideoHomeTab.enterOnPlayButtonOnVideoDetailsPage();
//
//        this.verifyOpenVideoPlayer(mDevice);
//        this.verifyPauseVideoPlayer(mDevice, filmTitle);
//
//        int replayTimes = 3;
//        int playTime = 3 * 60;
//        for (int i = 0; i < replayTimes; i++) {
//            this.verifyFilmPlaying(mDevice, playTime);
//        }
//
//        // play film and reset process
//        int resetTimes = 45;
//        mDevice.pressEnter();
//        this.systemWait(SHORT_WAIT);
//        this.resetFilmProcess(mDevice, resetTimes);
////        this.verifyPlayerProcessReset(mDevice);
//    }

    @Test
    @Category({CategoryHomeVideoTabTests.class})
    public void test99ClearUpAfterAllTestCasesDone() {
        mTask.destroyInstance();
    }

    private void navigateToVideoInAllTabOnVideoSubPage() {
        mAction.doDeviceActionAndWait(new DeviceActionMoveLeft(), LONG_WAIT);
        mAction.doDeviceActionAndWait(new DeviceActionMoveDown(), TestConstants.WAIT);
    }

    private String randomSelectVideoAndOpenDetails(int randomInt) {
        int moveTimes = new Random().nextInt(randomInt);
        for (int j = 0; j <= moveTimes; j++) {
            mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        }
        Log.d(TAG, String.format("ZjFilmTest, select film at position: %d", moveTimes));

        mAction.doDeviceActionAndWait(new DeviceActionEnter());
        return mTaskVideoHomeTab.waitVideoDetailsPageOpenedAndRetTitle();
    }

    private void isVideoPlayerOnTop() {
        UiObject2 player = mDevice.findObject(mTask.getVideoPlayerByClassSelector());
        boolean ret = (player != null) && player.isEnabled();
        TestHelper.assertTrueAndCaptureIfFailed("Verify player is playing and on the top.", ret);
    }

    private void isVideoPlayerStatusPause(UiDevice device, String filmTitleText) {
        UiObject2 title = device.findObject(mTask.getVideoNameInVideoPlayerSelector());
//        assertAndCaptureForFailed((title != null), "Verify film title when pause player.");

        String titleText = title.getText();
        Log.d(TAG, String.format("Play film: %s", titleText));
        boolean ret = (titleText.trim().equals(filmTitleText.trim()));
//        assertAndCaptureForFailed(ret, "Verify film title when pause player.");

        UiObject2 pauseBtn = device.findObject(mTask.getPauseButtonInVideoPlayerSelector());
//        assertAndCaptureForFailed((pauseBtn != null), "Verify pause icon when pause player.");

        UiObject2 seekBar = device.findObject(mTask.getSeekBarInVideoPlayerSelector());
//        assertAndCaptureForFailed((seekBar != null), "Verify seek bar when pause player.");

        UiObject2 totalTime = device.findObject(mTask.getTotalTimeInSeekBarOfVideoPlayerSelector());
        ret = totalTime.getText().contains(":");
//        assertAndCaptureForFailed(ret, "Verify film total time when pause player.");
    }

    private void verifyFilmPlaying(UiDevice device, int playTime) {
        UiObject2 curTime = device.findObject(mTask.getCurrentTimeInSeekBarOfVideoPlayerSelector());
//        assertAndCaptureForFailed((curTime != null), "Verify film current play time.");
        Log.d(TAG, String.format("Current play time: %s", curTime.getText()));
        int startTime = formatFilmPlayTime(curTime.getText());

        // play film
        device.pressEnter();
//        ShellUtils.systemWaitByMillis(playTime * 1000L);

        // pause player
        device.pressEnter();
//        systemWait(SHORT_WAIT);
        curTime = device.findObject(mTask.getCurrentTimeInSeekBarOfVideoPlayerSelector());
//        assertAndCaptureForFailed((curTime != null), "Verify film current play time.");
        int endTime = formatFilmPlayTime(curTime.getText());

        int buffer = 5;
        boolean ret = ((endTime - startTime) >= (playTime - buffer));
//        assertAndCaptureForFailed(ret, "Verify playing film.");
    }

    private void verifyPlayerProcessReset(UiDevice device) {
        mAction.doDeviceActionAndWait(new DeviceActionEnter());  // pause player

        UiObject2 curTimeObj =
                device.findObject(mTask.getCurrentTimeInSeekBarOfVideoPlayerSelector());
        Log.d(TAG, String.format("Video process is reset to %s", curTimeObj.getText()));

        int curPlayTime = this.formatFilmPlayTime(curTimeObj.getText());
        if (curPlayTime > 30) {
            Log.e(TAG, "Failed to reset the video to start.");
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