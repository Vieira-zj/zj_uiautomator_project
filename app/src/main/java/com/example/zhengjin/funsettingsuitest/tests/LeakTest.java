package com.example.zhengjin.funsettingsuitest.tests;

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

    // use Application Context instead of Activity Context
    public static LeakTest getInstance(Context context) {
        if (instance == null) {
            instance = new LeakTest(context);
        }
        return instance;
    }

    // textview tv hold activity context
    public void setRetainedTextView(TextView tv) {
        this.mTextView = tv;
        mTextView.setText(mCtx.getString(android.R.string.ok));
    }

    public void removeTextView() {
        if (mTextView != null) {
            mTextView = null;
        }
    }

}
