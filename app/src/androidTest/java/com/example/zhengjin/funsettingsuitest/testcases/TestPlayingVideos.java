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
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskPlayingVideos;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskVideoHomeTab;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import org.junit.Assert;
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
        TaskLauncher.backToLauncher();
    }

//    @After
//    public void clearUp() {
//        TaskLauncher.backToLauncher();
//    }

    @Test
    @Category(Category24x7LauncherTests.class)
    public void test11PlayingFilm() {
        mTaskVideoHomeTab.openSubPageFromLauncherHomeByText("电影");
        this.navigateToVideoInAllTabOnVideoSubPage();
        this.randomSelectVideoAndOpenDetails(5);
        mTaskVideoHomeTab.enterOnPlayButtonOnVideoDetailsPage();
        mTask.waitForVideoPlayerOpenedAndOnTop();

        ShellUtils.systemWaitByMillis(10 * 1000L);  // play 5 minutes
        this.isVideoPlayerOnTop();

        mTask.resetVideoProcessToStart();
        this.isPlayerProcessReset(mDevice);
    }

    @Test
    @Category(Category24x7LauncherTests.class)
    public void test12ActionsWhenPlayingFilm() {
        mTaskVideoHomeTab.openSubPageFromLauncherHomeByText("电影");
        this.navigateToVideoInAllTabOnVideoSubPage();
        String filmTitle = this.randomSelectVideoAndOpenDetails(5);
        mTaskVideoHomeTab.enterOnPlayButtonOnVideoDetailsPage();
        mTask.waitForVideoPlayerOpenedAndOnTop();

        mAction.doDeviceActionAndWait(new DeviceActionEnter());
        this.verifyPlayerWhenPause(filmTitle);

        int replayTimes = 3;
        int playTime = 5;
        for (int i = 0; i < replayTimes; i++) {
            this.verifyFilmPlaying(playTime);
        }

        // play film and reset process
        mTask.resetVideoProcessToStart();
        this.isPlayerProcessReset(mDevice);
    }

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

    private void verifyPlayerWhenPause(String filmTitleText) {
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

    private void verifyFilmPlaying(int playTime) {
        UiObject2 curTime =
                mDevice.findObject(mTask.getCurrentTimeInSeekBarOfVideoPlayerSelector());
        Assert.assertNotNull("Verify film current play time.", curTime);
        int startTime = formatFilmPlayTime(curTime.getText());

        // play film
        mAction.doDeviceActionAndWait(new DeviceActionEnter());
        ShellUtils.systemWaitByMillis(playTime * 1000L);

        // pause player
        mAction.doDeviceActionAndWait(new DeviceActionEnter());
        curTime = mDevice.findObject(mTask.getCurrentTimeInSeekBarOfVideoPlayerSelector());
        Assert.assertNotNull("Verify film current play time.", curTime);
        int endTime = formatFilmPlayTime(curTime.getText());

        final int buffer = 5;
        Assert.assertTrue("Verify playing film.", (endTime - startTime) >= (playTime - buffer));
    }

    private void isPlayerProcessReset(UiDevice device) {
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