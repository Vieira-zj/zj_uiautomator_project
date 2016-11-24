package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryWeatherTests;
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
public final class TestPlayingTvSeries {

    private final static String TAG = TestPlayingTvSeries.class.getSimpleName();

    private TaskPlayingVideos mTask;

    @Before
    public void setUp() {
        mTask = TaskPlayingVideos.getInstance();
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void testGetTvInfo() {
        final String tvName = "国家底线";

        TaskPlayingVideos.TvInfo tvInfo = mTask.getTvInfoByName(tvName);
        if (tvInfo != null) {
            Log.d(TAG, String.format(
                    "TvSeriesTest, tv info for %s, mediaId: %d, totalNum: %d, isEnd: %s, isVip: %s",
                    tvInfo.getTvName(), tvInfo.getMediaId(), tvInfo.getTotalNum(),
                    String.valueOf(tvInfo.getIsEnd()), tvInfo.getIsVip()));
        } else {
            Log.d(TAG, String.format("Failed to get the TvInfo for %s", tvName));
        }

        int num = mTask.getLatestTvTotalNumByName(tvName);
        if (num != -1) {
            Log.d(TAG, String.format("TvSeriesTest, the current total num for tv %s: %d",
                    tvName, num));
        } else {
            Log.d(TAG, String.format("Failed to get the current total num for %s", tvName));
        }

        Assert.assertTrue(true);
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test99ClearUpAfterAllTestCasesDone() {
        mTask.destroyInstance();
    }

}
