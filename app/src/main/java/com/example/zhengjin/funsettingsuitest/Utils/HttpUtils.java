package com.example.zhengjin.funsettingsuitest.utils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhengjin on 2016/11/22.
 *
 * Http utils by using the OkHttp.
 */

@SuppressWarnings("deprecation")
public final class HttpUtils {

    private static final int TIME_OUT = 30;
    private static final String CHARSET_NAME = "UTF-8";

    private static final OkHttpClient mClient = new OkHttpClient();

    static {
        mClient.setConnectTimeout(TIME_OUT, TimeUnit.SECONDS);
    }

    public static Request buildRequest(String url) {
        return new Request.Builder().url(url).build();
    }

    public static Request buildRequestWithHeader(String url, List<BasicNameValuePair> params) {
        Request.Builder requestUrl = new Request.Builder().url(url);
        for (BasicNameValuePair p : params) {
            requestUrl.addHeader(p.getName(), p.getValue());
        }

        return requestUrl.build();
    }

    private static Response execute(Request request) throws IOException {
        return mClient.newCall(request).execute();
    }

    public static void execute(Request request, Callback responseCallback) {
        mClient.newCall(request).enqueue(responseCallback);
    }

    public static String getStringFromServer(Request request) throws IOException {
        Response response = execute(request);
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code: " + response);
        }
    }

    private static String formatParams(List<BasicNameValuePair> params) {
        return URLEncodedUtils.format(params, CHARSET_NAME);
    }

    public static String attachHttpGetParams(String url, List<BasicNameValuePair> params) {
        return String.format("%s?%s", url, formatParams(params));
    }

    public static String attachHttpGetParam(String url, String name, String value) {
        return String.format("%s?%s=%s", url, name, value);
    }

}
