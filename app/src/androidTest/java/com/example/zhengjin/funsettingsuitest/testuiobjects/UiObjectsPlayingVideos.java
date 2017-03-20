package com.example.zhengjin.funsettingsuitest.testuiobjects;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;

/**
 * Created by zhengjin on 2017/3/20.
 * <p>
 * Include the UI objects for playing video.
 */

public final class UiObjectsPlayingVideos {

    private static UiObjectsPlayingVideos ourInstance = new UiObjectsPlayingVideos();

    public static UiObjectsPlayingVideos getInstance() {
        return ourInstance;
    }

    public void destroyInstance() {
        if (ourInstance != null) {
            ourInstance = null;
        }
    }

    private UiObjectsPlayingVideos() {
    }

    public BySelector getVideoPlayerByClassSelector() {
        return By.clazz("com.funshion.player.play.funshionplayer.VideoViewPlayer");
    }

    public BySelector getVideoTitleOfVideoPlayerSelector() {
        return By.res("com.bestv.ott:id/video_player_title");
    }

    public BySelector getPauseButtonOfVideoPlayerSelector() {
        return By.res("com.bestv.ott:id/control_panel_pause_layout_btn");
    }

    public BySelector getSeekBarOfVideoPlayerSelector() {
        return By.res("com.bestv.ott:id/media_progress");
    }

    public BySelector getCurrentTimeInSeekBarOfVideoPlayerSelector() {
        return By.res("com.bestv.ott:id/time_current");
    }

    public BySelector getTotalTimeInSeekBarOfVideoPlayerSelector() {
        return By.res("com.bestv.ott:id/time_total");
    }

    public BySelector getSignalSourceCardOnHomeTvTabSelector() {
        return By.res("com.bestv.ott:id/home_tv");
    }

    public BySelector getHdmi1ItemFromSignalSourceDialog() {
        return By.res("tv.fun.settings:id/source_item_hdmi1");
    }

}
