package com.example.zhengjin.funsettingsuitest.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

/**
 * Created by zhengjin on 2017/5/31.
 * <p>
 * Test demo, create leaks handle by LeakCanary.
 */

public class LeakTest {

    private Context mCtx;
    private TextView mTextView;

    @SuppressLint("StaticFieldLeak")
    private static LeakTest instance = null;

    private LeakTest(Context context) {
        this.mCtx = context;
    }

    public static LeakTest getInstance(Context context) {
        // pass Application Context instead of Activity Context
        if (instance == null) {
            instance = new LeakTest(context);
        }
        return instance;
    }

    public void setRetainedTextView(TextView tv) {
        // TextView tv holds activity context
        this.mTextView = tv;
        mTextView.setText(mCtx.getString(android.R.string.ok));
    }

    public void removeTextView() {
        if (mTextView != null) {
            mTextView = null;
        }
    }

}
