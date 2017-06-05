package com.example.zhengjin.funsettingsuitest.test;

import com.example.zhengjin.funsettingsuitest.activity.ActivityMain;

/**
 * Created by zhengjin on 2017/6/2.
 * <p>
 * For Jacoco.
 * Instrumented Activity extends from main activity, and invoked in Jacoco instrumentation.
 */

public class InstrumentedActivity extends ActivityMain {

    private FinishListener mListener;

    public void setFinishListener(FinishListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onDestroy() {
        super.finish();
        if (this.mListener != null) {
            mListener.onActivityFinished();
        }
        super.onDestroy();
    }

}
