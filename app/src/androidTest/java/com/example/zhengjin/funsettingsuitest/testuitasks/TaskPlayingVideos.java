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
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionBack;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveLeft;
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
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_TIME_OUT;
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

    public void waitForVideoPlayerOpenedAndOnTop() {
        // wait for buffer loading activity disappear
        TestHelper.waitForActivityOpenedByShellCmd(
                LAUNCHER_PKG_NAME, VIDEO_PLAYER_ACT, LONG_TIME_OUT);
        // wait for adv, and loading in video player
        TestHelper.waitForUiObjectEnabledByCheckIsEnabled(
                this.getVideoPlayerByClassSelector(), LONG_TIME_OUT);
    }

    public void exitVideoPlayerByBack() {
        action.doRepeatDeviceActionAndWait(new DeviceActionBack(), 2, 500L);
    }

    public void resetVideoProcessToBeginning() {
        swipeOnVideoProcess(Direction.LEFT);
        action.doRepeatDeviceActionAndWait(new DeviceActionMoveLeft(), 6, 500L);
    }

    public void resetVideoProcessToEnd() {
        swipeOnVideoProcess(Direction.RIGHT);
        action.doRepeatDeviceActionAndWait(new DeviceActionMoveRight(), 6, 500L);
    }

    private void swipeOnVideoProcess(Direction direction) {
        final long wait = 200L;
        final float percent = 1.0f;
        final int step = 300;

        action.doDeviceActionAndWait(new DeviceActionMoveRight(), wait);  // show seek bar
        UiObject2 seekBar = TestHelper.waitForUiObjectExistAndReturn(
                this.getSeekBarOfVideoPlayerSelector());
        action.doDeviceActionAndWait(new DeviceActionMoveRight(), wait);  // show seek bar
        seekBar.swipe(direction, percent, step);
    }

    public int getCurrentPlayTimeInVideoPlayer() {
        action.doDeviceActionAndWait(new DeviceActionEnter());  // pause player
        UiObject2 curTime =
                device.findObject(this.getCurrentTimeInSeekBarOfVideoPlayerSelector());
        int playingTime = formatPlayTimeInVideoPlayer(curTime.getText());
        action.doDeviceActionAndWait(new DeviceActionEnter());  // play

        return playingTime;
    }

    public int formatPlayTimeInVideoPlayer(String playTime) {
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

    @Nullable
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

    public int getLatestTvTotalNumByName(videoInfo info) {
        if (info == null) {
            return -1;
        }

        if (info.isEnd) {
            int total = info.getTotalNum();
            if (total != 0) {
                return total;
            }
        }

        JSONObject respObj = JSON.parseObject(this.doSendRequestAndRetResponse(
                this.buildVideoDetailsGetRequest(info.getMediaId())));
        if (!this.isResponseOk(respObj)) {
            return -1;
        }
        int count = respObj.getJSONObject("data").getJSONArray("episodes").size();
        return count == 0 ? 1 : count;
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
