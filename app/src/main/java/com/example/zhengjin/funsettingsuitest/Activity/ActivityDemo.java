package com.example.zhengjin.funsettingsuitest.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.zhengjin.funsettingsuitest.R;
import com.example.zhengjin.funsettingsuitest.utils.HttpUtils;
import com.squareup.okhttp.Request;

import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("deprecation")
public final class ActivityDemo extends AppCompatActivity {

    private EditText mEditorUserName = null;
    private Button mBtnOk = null;
    private Button mBtnHttpTest = null;
    private TextView mTextHelloMsg = null;
    private TextView mTextHttpTest = null;

    private List<BasicNameValuePair> URL_PARAMS = new ArrayList<>(5);
    private List<BasicNameValuePair> HEADER_PARAMS = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        this.initViews();

        if (mBtnOk != null) {
            mBtnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mEditorUserName != null && mTextHelloMsg != null) {
                        String input = mEditorUserName.getText().toString();
                        if ("".equals(input)) {
                            Toast.makeText(ActivityDemo.this,
                                    "Please input your name!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        mTextHelloMsg.setText(
                                String.format(Locale.getDefault(), "Hello, %s", input));
                    }
                }
            });
        }

        if (mBtnHttpTest != null) {
            mBtnHttpTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mTextHttpTest != null) {
                        new Thread(new HttpTestRunnable()).start();
                    }
                }
            });
        }
    }

    private void initViews() {
        mEditorUserName = (EditText) findViewById(R.id.editor_user_name);
        mBtnOk = (Button) findViewById(R.id.button_ok);
        mBtnHttpTest = (Button) findViewById(R.id.button_http_test);
        mTextHelloMsg = (TextView) findViewById(R.id.text_hello_msg);
        mTextHttpTest = (TextView) findViewById(R.id.text_http_test_msg);
    }

    private void initRequestParams() {
        URL_PARAMS.add(new BasicNameValuePair("cityid", "101200101"));
        HEADER_PARAMS.add(new BasicNameValuePair("apikey", "11c756e31e9bed863a743ccff784ddeb"));
    }

    private Request buildRequest() {
        this.initRequestParams();
        String url = HttpUtils.attachHttpGetParams(
                "http://apis.baidu.com/apistore/weatherservice/recentweathers", URL_PARAMS);
        return HttpUtils.buildRequestWithHeader(url, HEADER_PARAMS);
    }

    private JSONObject getJsonObject(String responseJson) {
        return JSON.parseObject(responseJson);
    }

    private JSONObject getRetDataOfWeatherData(JSONObject jsonData) {
        return jsonData.getJSONObject("retData");
    }

    private String getRetCodeOfWeatherData(JSONObject jsonData) {
        return String.valueOf(jsonData.getIntValue("errNum"));
    }

    private String getRetMessageOfWeatherData(JSONObject jsonData) {
        return jsonData.getString("errMsg");
    }

    private String getForecastWeatherData(JSONObject jsonData) {
        StringBuilder sb = new StringBuilder(10);

        JSONArray forecast = jsonData.getJSONArray("forecast");
        if (forecast.size() == 0) {
            return "null";
        }
        for (int i = 0, size = forecast.size(); i < size; i++) {
            JSONObject item = forecast.getJSONObject(i);
            sb.append(String.format("date: %s, week: %s, wind: %s/%s, temp: %s/%s, type: %s\n",
                    item.getString("date"), item.getString("week"),
                    item.getString("fengxiang"), item.getString("fengli"),
                    item.getString("hightemp"), item.getString("lowtemp"),
                    item.getString("type")));
        }

        return sb.toString();
    }

    private static final int HTTP_TEST = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String text = (String) msg.obj;
            if (msg.what == HTTP_TEST) {
                JSONObject resJsonObject = ActivityDemo.this.getJsonObject(text);
                mTextHttpTest.setText(String.format(
                        "Return code: %s\nReturn message: %s\nForecast:\n%s",
                        ActivityDemo.this.getRetCodeOfWeatherData(resJsonObject),
                        ActivityDemo.this.getRetMessageOfWeatherData(resJsonObject),
                        getForecastWeatherData(getRetDataOfWeatherData(resJsonObject))));
            }
        }
    };

    private class HttpTestRunnable implements Runnable {
        @Override
        public void run() {
            String response;
            try {
                response = HttpUtils.getStringFromServer(buildRequest());
            } catch (IOException e) {
                response = e.getMessage();
                e.printStackTrace();
            }

            Message msg = Message.obtain();
            msg.what = HTTP_TEST;
            msg.obj = response;
            handler.sendMessage(msg);
        }
    }

}
