package com.example.zhengjin.funsettingsuitest.testrunner;

import android.os.SystemClock;
import android.util.Log;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Created by zhengjin on 2017/2/20.
 * <p>
 * A run listener demo extends from RunListener.
 * Usage: -e listener com.example.zhengjin.funsettingsuitest.testrunner.RunnerListenerDemo
 *
 * Refer to http://stackoverflow.com/questions/35230276/specifying-custom-runlistener-in-androidmanifest-metadata-not-working
 */

@SuppressWarnings("unused")
public final class RunnerListenerDemo extends RunListener {

    private static final String TAG = RunnerListenerDemo.class.getSimpleName();
    private static final String ZJ_KEYWORD = "ZJTest => ";

    private long mTestStarted;

    @Override
    public void testRunStarted(Description description) {
        Log.d(TAG, ZJ_KEYWORD + "Starting, number of test cases to execute: " +
                description.testCount());
    }

    @Override
    public void testRunFinished(Result result) {
        Log.d(TAG, ZJ_KEYWORD + "Number of test cases to execute: " + result.getRunCount());
        Log.d(TAG, ZJ_KEYWORD + "Number of test cases to failure: " + result.getFailureCount());
        Log.d(TAG, ZJ_KEYWORD +
                String.format("Total test execution time: %d ms", result.getRunTime()));
    }

    @Override
    public void testStarted(Description description) {
        Log.d(TAG, ZJ_KEYWORD + "Starting execution of test case: " + description.getMethodName());
        mTestStarted = SystemClock.uptimeMillis();
    }

    @Override
    public void testFinished(Description description) {
        Log.d(TAG, ZJ_KEYWORD + String.format("Execution time: %d ms",
                (SystemClock.uptimeMillis() - mTestStarted)));
        Log.d(TAG, ZJ_KEYWORD + "Finished execution of test case: " + description.getMethodName());
    }

    @Override
    public void testFailure(Failure failure) {
        Log.d(TAG, ZJ_KEYWORD + "Execution of test case failed: " + failure.getMessage());
        Log.d(TAG, ZJ_KEYWORD + "Exception: " + failure.getTrace());
    }

    @Override
    public void testIgnored(Description description) {
        Log.d(TAG, ZJ_KEYWORD + "Execution of test case ignored: " + description.getMethodName());
    }

}
