package com.example.zhengjin.funsettingsuitest.testcasedemos;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskPlayingVideos;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

/**
 * Created by zhengjin on 2016/10/8.
 * <p>
 * Test playing tv series video.
 * Include test cases to run more than 8 hours.
 */

@RunWith(AndroidJUnit4.class)
public final class TestRemoteJsonAPIs {

    private final static String TAG = TestRemoteJsonAPIs.class.getSimpleName();

    private TaskPlayingVideos mTask;

    @Before
    public void setUp() {
        mTask = TaskPlayingVideos.getInstance();
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test11GetTvInfo() {
        final String tvName = "国家底线";

        TaskPlayingVideos.videoInfo tvInfo = mTask.getTvInfoByName(tvName);
        if (tvInfo != null) {
            Log.d(TAG, String.format(
                    "TvSeriesTest, tv info for %s, mediaId: %d, totalNum: %d, isEnd: %s, isVip: %s",
                    tvInfo.getTvName(), tvInfo.getMediaId(), tvInfo.getTotalNum(),
                    String.valueOf(tvInfo.getIsEnd()), tvInfo.getVipType()));
        } else {
            Log.d(TAG, String.format("Failed to get the tv info for %s", tvName));
        }

        Assert.assertTrue(true);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test12GetFilmInfo() {
        final String filmName = "那年夏天";

        TaskPlayingVideos.videoInfo filmInfo = mTask.getFilmInfoByName(filmName);
        if (filmInfo != null) {
            Log.d(TAG, String.format(
                    "FilmTest, film info for %s, mediaId: %d, totalNum: %d, isEnd: %s, isVip: %s",
                    filmInfo.getTvName(), filmInfo.getMediaId(), filmInfo.getTotalNum(),
                    String.valueOf(filmInfo.getIsEnd()), filmInfo.getVipType()));
        } else {
            Log.d(TAG, String.format("Failed to get the film info for %s", filmName));
        }

        Assert.assertTrue(true);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test13GetTvCurTotalNum() {
        final String tvName = "国家底线";

        TaskPlayingVideos.videoInfo tvInfo = mTask.getFilmInfoByName(tvName);
        int num = mTask.getLatestTvTotalNumByName(tvInfo);
        if (num != -1) {
            Log.d(TAG, String.format("TvSeriesTest, the current total num for tv %s: %d",
                    tvName, num));
        } else {
            Log.d(TAG, String.format("Failed to get the current total num for %s", tvName));
        }

        Assert.assertTrue(true);
    }

}
