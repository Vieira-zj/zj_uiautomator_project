package com.example.zhengjin.funsettingsuitest.testrunner;

import android.os.SystemClock;
import android.util.Log;

import org.junit.experimental.categories.Category;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhengjin on 2017/2/20.
 * <p>
 * A run listener demo extends from RunListener.
 * Usage: -e listener com.example.zhengjin.funsettingsuitest.testrunner.RunnerListenerDemo
 * <p>
 * Refer to http://stackoverflow.com/questions/35230276/specifying-custom-runlistener-in-androidmanifest-metadata-not-working
 * <p>
 * Run sequence:
 * testRunStarted() -> testStarted() -> testFailure() -> testFinished() -> testRunFinished()
 */
@SuppressWarnings("unused")
public final class RunnerListenerDemo extends RunListener {

    private static final String TAG = RunnerListenerDemo.class.getSimpleName();
    private static final String ZJ_KEYWORD = "ZJTest => ";

    private long mTestStarted;

    /**
     * Called before any tests have been run.
     */
    @Override
    public void testRunStarted(Description description) {
        if (description.isSuite()) {
            Log.d(TAG, ZJ_KEYWORD + "Starting, number of test cases to execute: " +
                    description.testCount());

            List<Description> descriptions = description.getChildren();
            Log.d(TAG, ZJ_KEYWORD +
                    String.format("Test case list(count %d): ", descriptions.size()));
            for (Description desc : descriptions) {
                if (desc != null) {
                    Log.d(TAG, ZJ_KEYWORD + description.toString());
                }
            }
        }

        if (description.isTest()) {
            Log.d(TAG, ZJ_KEYWORD + "Test class name: " + description.getClassName());
            Log.d(TAG, ZJ_KEYWORD + "Test method name: " + description.getMethodName());
        }
    }

    /**
     * Called when all tests have finished.
     */
    @Override
    public void testRunFinished(Result result) {
        Log.d(TAG, ZJ_KEYWORD + "Number of test cases to execute: " + result.getRunCount());
        Log.d(TAG, ZJ_KEYWORD + "Number of test cases to failure: " + result.getFailureCount());
        Log.d(TAG, ZJ_KEYWORD +
                String.format("Total test execution time: %d ms", result.getRunTime()));
    }

    /**
     * Called when an atomic test is about to be started.
     */
    @Override
    public void testStarted(Description description) {
        if (description.isTest()) {
            Log.d(TAG, ZJ_KEYWORD + "Starting execution of test case: " +
                    description.getDisplayName());

            Collection<Annotation> annotations = description.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Category.class)) {
                    Log.d(TAG, ZJ_KEYWORD + "Test case category: " + annotation.toString());
                }
            }
        }

        mTestStarted = SystemClock.uptimeMillis();
    }

    /**
     * Called when an atomic test has finished, whether the test succeeds or fails.
     */
    @Override
    public void testFinished(Description description) {
        Log.d(TAG, ZJ_KEYWORD + String.format("Execution time: %d ms",
                (SystemClock.uptimeMillis() - mTestStarted)));
        Log.d(TAG, ZJ_KEYWORD + "Finished execution of test case: " + description.getMethodName());
    }

    /**
     * Called when an atomic test fails.
     */
    @Override
    public void testFailure(Failure failure) {
        Log.d(TAG, ZJ_KEYWORD + "Execution of test case failed: " + failure.getMessage());
        Log.d(TAG, ZJ_KEYWORD + "Exception: " + failure.getTrace());
    }

    /**
     * Called when a test will not be run, generally because a test method is annotated with Ignore.
     */
    @Override
    public void testIgnored(Description description) {
        Log.d(TAG, ZJ_KEYWORD + "Execution of test case ignored: " + description.getMethodName());
    }

}
