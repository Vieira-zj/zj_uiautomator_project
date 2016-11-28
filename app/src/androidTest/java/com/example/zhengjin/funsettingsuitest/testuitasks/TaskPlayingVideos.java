package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;
import com.example.zhengjin.funsettingsuitest.utils.HttpUtils;
import com.squareup.okhttp.Request;

import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LAUNCHER_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.VIDEO_PLAYER_ACT;

/**
 * Created by zhengjin on 2016/11/24.
 * <p>
 * Contains the UI selectors and tasks for playing film and TV.
 */

@SuppressWarnings("deprecation")
public final class TaskPlayingVideos {

    private final static String TAG = TaskPlayingVideos.class.getSimpleName();

    private static TaskPlayingVideos instance = null;

    private UiDevice device;
    private UiActionsManager action;

    private TaskPlayingVideos() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        action = UiActionsManager.getInstance();
    }

    public static synchronized TaskPlayingVideos getInstance() {
        if (instance == null) {
            instance = new TaskPlayingVideos();
        }
        return instance;
    }

    public void destroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public BySelector getVideoPlayerByClassSelector() {
        return By.clazz("com.funshion.player.play.funshionplayer.VideoViewPlayer");
    }

    public BySelector getVideoNameInVideoPlayerSelector() {
        return By.res("com.bestv.ott:id/video_player_title");
    }

    public BySelector getPauseButtonInVideoPlayerSelector() {
        return By.res("com.bestv.ott:id/control_panel_pause_layout_btn");
    }

    public BySelector getSeekBarInVideoPlayerSelector() {
        return By.res("com.bestv.ott:id/media_progress");
    }

    public BySelector getCurrentTimeInSeekBarOfVideoPlayerSelector() {
        return By.res("com.bestv.ott:id/time_current");
    }

    public BySelector getTotalTimeInSeekBarOfVideoPlayerSelector() {
        return By.res("com.bestv.ott:id/time_total");
    }

    public void waitForVideoPlayerOpenedAndOnTop() {
        TestHelper.waitForActivityOpenedByShellCmd(LAUNCHER_PKG_NAME, VIDEO_PLAYER_ACT, 15);
        TestHelper.waitForUiObjectEnabledByCheckIsEnabled(
                this.getVideoPlayerByClassSelector(), 15 * 1000);
    }

    public void resetVideoProcessToStart(float swipePercent) {
        action.doDeviceActionAndWait(new DeviceActionMoveRight(), 250L);  // show seek bar
        UiObject2 seekBar = TestHelper.waitForUiObjectExistAndReturn(
                this.getSeekBarInVideoPlayerSelector());
        action.doDeviceActionAndWait(new DeviceActionMoveRight(), 200L);  // show seek bar
        seekBar.swipe(Direction.LEFT, swipePercent);
    }

    @Nullable
    public videoInfo getTvInfoByName(String tvName) {
        return getVideoInfoByName(tvName, TestConstants.VideoType.TV);
    }

    @Nullable
    public videoInfo getFilmInfoByName(String tvName) {
        return getVideoInfoByName(tvName, TestConstants.VideoType.FILM);
    }

    @Nullable
    private videoInfo getVideoInfoByName(String tvName, TestConstants.VideoType type) {
        final int limit = 50;
        return getVideoInfoByName(tvName, type, limit);
    }

    @Nullable
    private videoInfo getVideoInfoByName(
            String videoName, TestConstants.VideoType type, int limit) {
        JSONObject respObject = JSON.parseObject(this.doSendRequestAndRetResponse(
                this.buildVideoSearchGetRequest(limit, type)));
        if (!this.isResponseOk(respObject)) {
            return null;
        }

        return this.getVideoInfoByName(videoName, respObject);
    }

    private videoInfo getVideoInfoByName(String tvName, JSONObject jsonObject) {
        JSONArray dataTvs = jsonObject.getJSONArray("data");
        for (int idx = 0, size = dataTvs.size(); idx < size; idx++) {
            JSONObject tv = dataTvs.getJSONObject(idx);
            if (tvName.equals(tv.getString("name"))) {
                return new videoInfo(
                        tv.getIntValue("media_id"), tv.getString("name"),
                        tv.getIntValue("total_num"), tv.getBooleanValue("is_end"),
                        tv.getString("vip_type"));
            }
        }

        return null;
    }

    public int getLatestTvTotalNumByName(String tvName) {
        videoInfo videoInfo = this.getTvInfoByName(tvName);
        if (videoInfo == null) {
            return -1;
        }

        JSONObject respObj = JSON.parseObject(this.doSendRequestAndRetResponse(
                this.buildVideoDetailsGetRequest(videoInfo.getMediaId())));
        if (!this.isResponseOk(respObj)) {
            return -1;
        }

        return respObj.getJSONObject("data").getJSONArray("episodes").size();
    }

    private boolean isResponseOk(JSONObject response) {
        String retCode = response.getString("retCode");
        String retMsg = response.getString("retMsg");
        if ("200".equals(retCode) && "ok".equals(retMsg)) {
            return true;
        } else {
            Log.e(TAG, String.format(
                    "isResponseOk, error: response ret code: %s, ret message: %s"
                    , retCode, retMsg));
            return false;
        }
    }

    private List<BasicNameValuePair> buildVideoSearchGetRequestParams(
            int limit, TestConstants.VideoType type) {
        List<BasicNameValuePair> URL_PARAMS = new ArrayList<>(20);
        URL_PARAMS.add(new BasicNameValuePair("area", "0"));
        URL_PARAMS.add(new BasicNameValuePair("cate", "0"));
        URL_PARAMS.add(new BasicNameValuePair("year", "1900_2100"));
        URL_PARAMS.add(new BasicNameValuePair("pz", String.valueOf(limit)));
        URL_PARAMS.add(new BasicNameValuePair("pv", "0"));
        URL_PARAMS.add(new BasicNameValuePair("version", "2.10.0.7_s"));
        URL_PARAMS.add(new BasicNameValuePair("sid", "FD5551A-SU"));
        URL_PARAMS.add(new BasicNameValuePair("mac", "28:76:CD:01:96:F6"));
        URL_PARAMS.add(new BasicNameValuePair("chiptype", "638"));

        if (type == TestConstants.VideoType.FILM) {
            URL_PARAMS.add(new BasicNameValuePair("mtype", "1"));
            URL_PARAMS.add(new BasicNameValuePair("order", "1"));
        } else if (type == TestConstants.VideoType.TV) {
            URL_PARAMS.add(new BasicNameValuePair("mtype", "2"));
            URL_PARAMS.add(new BasicNameValuePair("order", "2"));
            URL_PARAMS.add(new BasicNameValuePair("pg", "1"));
        }

        return URL_PARAMS;
    }

    private List<BasicNameValuePair> buildVideoDetailsGetRequestParams(int tvId) {
        List<BasicNameValuePair> URL_PARAMS = new ArrayList<>(20);
        URL_PARAMS.add(new BasicNameValuePair("id", String.valueOf(tvId)));
        URL_PARAMS.add(new BasicNameValuePair("account_id", "203186836"));
        URL_PARAMS.add(new BasicNameValuePair("token", "u9YuGT9-L5BCLqIPdaRQlV_Qop5FGdiS1ei" +
                "P7vnT0ijo43tKEEZ6CvI8SxiSTMnms4x45Wx4jhpYDJKBvgUGLAMrqPNHMwPfYEBgfj3kbiY"));
        URL_PARAMS.add(new BasicNameValuePair("version", "2.10.0.7_s"));
        URL_PARAMS.add(new BasicNameValuePair("sid", "FD5551A-SU"));
        URL_PARAMS.add(new BasicNameValuePair("mac", "28:76:CD:01:96:F6"));
        URL_PARAMS.add(new BasicNameValuePair("chiptype", "638"));

        return URL_PARAMS;
    }

    private Request buildVideoSearchGetRequest(int limit, TestConstants.VideoType type) {
        final String tvAllUrl = "http://js.funtv.bestv.com.cn/search/mretrieve/v2";
        String url = HttpUtils.attachHttpGetParams(
                tvAllUrl, this.buildVideoSearchGetRequestParams(limit, type));
        return HttpUtils.buildRequest(url);
    }

    private Request buildVideoDetailsGetRequest(int tvId) {
        final String tvDetailsUrl = "http://jm.funtv.bestv.com.cn/media/episode/v2";
        String url = HttpUtils.attachHttpGetParams(
                tvDetailsUrl, this.buildVideoDetailsGetRequestParams(tvId));
        return HttpUtils.buildRequest(url);
    }

    private String doSendRequestAndRetResponse(Request request) {
        String response;
        try {
            response = HttpUtils.getStringFromServer(request);
        } catch (IOException e) {
            response = String.format("{\"retCode\": \"-1\", \"retMsg\": \"%s\"}", e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    public static class videoInfo {
        private int mediaId;
        private String tvName;
        private int totalNum;
        private boolean isEnd;
        private String vipType;

        videoInfo(int mediaId, String tvName, int totalNum, boolean isEnd, String vipType) {
            this.mediaId = mediaId;
            this.tvName = tvName;
            this.totalNum = totalNum;
            this.isEnd = isEnd;
            this.vipType = vipType;
        }

        public int getMediaId() {
            return this.mediaId;
        }

        public String getTvName() {
            return this.tvName;
        }

        public int getTotalNum() {
            return this.totalNum;
        }

        public boolean getIsEnd() {
            return this.isEnd;
        }

        public String getVipType() {
            return this.vipType;
        }

        public boolean isVip() {
            final String free = "free";
            final String vipFree = "vipfree";

            if (free.equals(this.vipType)) {
                return false;
            } else if (vipFree.equals(this.vipType)) {
                return true;
            } else {
                Log.e(TAG, String.format("isVip, invalid vip type %s", this.vipType));
                return false;
            }
        }
    }

}
