package com.example.zhengjin.funsettingsuitest.testcases;

/**
 * Created by zhengjin on 2016/8/10.
 *
 * Include 24 x 7 test cases for playing video film.
 */

import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiWatcher;
import android.support.test.uiautomator.Until;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcategory.Category24x7LauncherTests;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by zhengjin on 2016/7/28.
 *
 * Test playing film video.
 * A standalone test case to run more than 8 hours,
 * all methods are self-contained private.
 */
@RunWith(AndroidJUnit4.class)
public final class TestPlayingFilm {

    private static final String TAG = TestPlayingFilm.class.getSimpleName();
    private static final String CAPTURES_PATH = "/data/local/tmp/captures";

    private static final int SHORT_WAIT = 1;
    private static final int WAIT = 5;
    private static final int LONG_WAIT = 8;

    private UiDevice mDevice;

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.registerWatcher("BufferRefreshFailedWatcher", new BufferRefreshFailedWatcher());
        backToLauncherHome(mDevice);
    }

    @After
    public void clearUp() {
        backToLauncherHome(mDevice);
    }

    @Test
    public void testDemo() {
        Log.d(TAG, "This is a test demo.");
//        this.takeScreenCapture();
//        this.verifyVideoPlayerOnTop(mDevice);
    }

    @Test
    @Category(Category24x7LauncherTests.class)
    public void test11OpenAndExitFilmPlayer() {
        this.verifyOnLauncherHome(mDevice);

        UiObject2 tabText = this.getTabFromLauncherHomeByText(mDevice, "电影");
        this.openTabFromLauncherHomeByText(mDevice, tabText);

        int randomInt = 30;
        this.randomSelectFilmAndOpenDetails(mDevice, randomInt);

        int playVideoTime = 5 * 60;  // play 5 minutes
        this.verifyOpenVideoPlayer(mDevice);
        this.systemWait(playVideoTime);
        this.verifyVideoPlayerOnTop(mDevice);

        int resetTimes = 45;
        this.resetFilmProcess(mDevice, resetTimes);
//        this.verifyPlayerProcessReset(mDevice);
    }

    @Test
    @Category(Category24x7LauncherTests.class)
    public void test12PlayFilm() {
        this.verifyOnLauncherHome(mDevice);

        UiObject2 tabText = this.getTabFromLauncherHomeByText(mDevice, "电影");
        this.openTabFromLauncherHomeByText(mDevice, tabText);

        int randomInt = 15;
        String filmTitle = this.randomSelectFilmAndOpenDetails(mDevice, randomInt);
        this.verifyOpenVideoPlayer(mDevice);
        this.verifyPauseVideoPlayer(mDevice, filmTitle);

        int replayTimes = 3;
        int playTime = 3 * 60;
        for (int i = 0; i < replayTimes; i++) {
            this.verifyFilmPlaying(mDevice, playTime);
        }

        // play film and reset process
        int resetTimes = 45;
        mDevice.pressEnter();
        this.systemWait(SHORT_WAIT);
        this.resetFilmProcess(mDevice, resetTimes);
//        this.verifyPlayerProcessReset(mDevice);
    }

    private UiObject2 getTabFromLauncherHomeByText(UiDevice device, String tabText) {
        List<UiObject2> tabTitles = device.findObjects(By.res("com.bestv.ott:id/title"));
        Assert.assertTrue("Verify tabs on launcher home.", tabTitles.size() > 0);

        UiObject2 retTitle = null;
        for (UiObject2 title : tabTitles) {
            if (tabText.equals(title.getText())) {
                retTitle = title;
            }
        }
        Assert.assertNotNull(retTitle);

        return retTitle;
    }

    private void openTabFromLauncherHomeByText(UiDevice device, UiObject2 tabText) {
        tabText.getParent().click();
        systemWait(SHORT_WAIT);

        device.pressEnter();
        device.waitForIdle();
        systemWait(LONG_WAIT);
    }

    private String randomSelectFilmAndOpenDetails(UiDevice device, int randomInt) {
        // random select film
        device.pressDPadLeft();
        device.waitForIdle();
        systemWait(LONG_WAIT);

        device.pressDPadDown();
        systemWait(WAIT);

        int moveTimes = new Random().nextInt(randomInt);
        Log.d(TAG, String.format("Select film at position: %d", moveTimes));
        for (int j = 0; j <= moveTimes; j++) {
            device.pressDPadRight();
            systemWait(SHORT_WAIT);
        }

        // open film details
        device.pressEnter();
        device.waitForIdle();
        systemWait(LONG_WAIT);

        UiObject2 filmTitle = device.findObject(By.res("com.bestv.ott:id/detail_title"));
        assertAndCaptureForFailed((filmTitle != null), "Verify film title of details page.");

        String filmTitleText = filmTitle.getText();
        boolean ret = ((filmTitleText != null) && (!"".equals(filmTitleText)));
        assertAndCaptureForFailed(ret, "Verify film title of details page.");

        Log.d(TAG, String.format("Film title: %s", filmTitleText));
        return filmTitleText;
    }

    private void verifyOnLauncherHome(UiDevice device) {
        boolean ret = device.wait(Until.hasObject(By.pkg("com.bestv.ott").depth(0)), WAIT);
        Assert.assertTrue("Verify back to launcher home.", ret);
    }

    private void verifyOpenVideoPlayer(UiDevice device) {
        // open player and wait
        int waitPlayerOnTop = 15;
        device.pressEnter();
        this.systemWait(waitPlayerOnTop);

        boolean ret = device.wait(Until.hasObject(By.clazz(
                "com.funshion.player.play.funshionplayer.VideoViewPlayer")), waitPlayerOnTop);
        assertAndCaptureForFailed(ret, "Verify player is open and on the top.");
    }

    private void verifyVideoPlayerOnTop(UiDevice device) {
        UiObject2 player = device.findObject(By.clazz(
                "com.funshion.player.play.funshionplayer.VideoViewPlayer"));
        boolean ret = (player != null) && player.isEnabled();
        assertAndCaptureForFailed(ret, "Verify player is playing and on the top.");
    }

    private void verifyPauseVideoPlayer(UiDevice device, String filmTitleText) {
        // pause player
        device.pressEnter();
        systemWait(SHORT_WAIT);

        UiObject2 title = device.findObject(By.res("com.bestv.ott:id/video_player_title"));
        assertAndCaptureForFailed((title != null), "Verify film title when pause player.");

        String titleText = title.getText();
        Log.d(TAG, String.format("Play film: %s", titleText));
        boolean ret = (titleText.trim().equals(filmTitleText.trim()));
        assertAndCaptureForFailed(ret, "Verify film title when pause player.");

        UiObject2 pauseBtn = device.findObject(
                By.res("com.bestv.ott:id/control_panel_pause_layout_btn"));
        assertAndCaptureForFailed((pauseBtn != null), "Verify pause icon when pause player.");

        UiObject2 seekBar = device.findObject(By.res("com.bestv.ott:id/media_progress"));
        assertAndCaptureForFailed((seekBar != null), "Verify seek bar when pause player.");

        UiObject2 totalTime = device.findObject(By.res("com.bestv.ott:id/time_total"));
        ret = totalTime.getText().contains(":");
        assertAndCaptureForFailed(ret, "Verify film total time when pause player.");
    }

    private void verifyFilmPlaying(UiDevice device, int playTime) {
        UiObject2 curTime = device.findObject(By.res("com.bestv.ott:id/time_current"));
        assertAndCaptureForFailed((curTime != null), "Verify film current play time.");
        Log.d(TAG, String.format("Current play time: %s", curTime.getText()));
        int startTime = formatFilmPlayTime(curTime.getText());

        // play film
        device.pressEnter();
        systemWait(playTime);

        // pause player
        device.pressEnter();
        systemWait(SHORT_WAIT);
        curTime = device.findObject(By.res("com.bestv.ott:id/time_current"));
        assertAndCaptureForFailed((curTime != null), "Verify film current play time.");
        int endTime = formatFilmPlayTime(curTime.getText());

        int buffer = 5;
        boolean ret = ((endTime - startTime) >= (playTime - buffer));
        assertAndCaptureForFailed(ret, "Verify playing film.");
    }

    private void verifyPlayerProcessReset(UiDevice device) {
        // pause player
        device.pressEnter();
        systemWait(SHORT_WAIT);

        UiObject2 curTime = device.findObject(By.res("com.bestv.ott:id/time_current"));
        assertAndCaptureForFailed((curTime != null), "Verify film current play time.");
        Log.d(TAG, String.format("The player process is reset to %s", curTime.getText()));

        int timeAfterReset = 30;
        int curPlayTime = this.formatFilmPlayTime(curTime.getText());
        this.assertAndCaptureForFailed(
                (curPlayTime <= timeAfterReset), "Verify player process is reset.");
    }

    private void backToLauncherHome(UiDevice device) {
        device.pressHome();
        device.waitForIdle();
        systemWait(WAIT);
    }

    private void resetFilmProcess(UiDevice device, int resetTimes) {
        for (int i = 0; i <= resetTimes; i++) {
            device.pressDPadLeft();
            SystemClock.sleep(500);
        }
    }

    private void assertAndCaptureForFailed(boolean ret, String message) {
        if (!ret) {
            this.takeScreenCapture();
        }
        Assert.assertTrue(message, ret);
    }

    private int formatFilmPlayTime(String playTime) {
        String[] items = playTime.split(":");

        if (items.length == 2) {
            return 60 * convertPlayTimeToInt(items[0]) +
                    Integer.parseInt(items[1]);
        }
        if (items.length == 3) {
            return 60 * (60 * convertPlayTimeToInt(items[0])) +
                    60 * convertPlayTimeToInt(items[1]) +
                    Integer.parseInt(items[2]);
        }
        return 0;
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

    private void systemWait(int seconds) {
        SystemClock.sleep(seconds * 1000);
    }

    private void takeScreenCapture() {
        File localFile = new File("/data/local/tmp/captures");
        if (!localFile.exists()) {
            Assert.assertTrue(localFile.mkdirs());
        }

        String path = String.format("%s/capture_%s.png", CAPTURES_PATH, this.getCurrentTime());

        if (mDevice.takeScreenshot(new File(path))) {
            Log.d(TAG, String.format("Take capture and save at %s", path));
        } else {
            Log.e(TAG, String.format("Failed, take capture and save at %s", path));
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd_hhmmss_SSS", Locale.getDefault());
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
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