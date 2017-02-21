package com.example.zhengjin.funsettingsuitest.testrunner;

import android.os.SystemClock;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.utils.StringUtils;

import junit.framework.Assert;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

/**
 * Created by zhengjin on 2017/2/20.
 * <p>
 * Customized runner listener to generate testing report.
 * Usage: -e listener com.example.zhengjin.funsettingsuitest.testrunner.RunnerListenerFunSettings
 */
@SuppressWarnings("unused")
public final class RunnerListenerFunSettings extends RunListener {

    private static final String TAG = RunnerListenerFunSettings.class.getSimpleName();
    private static final String ZJ_KEYWORD = "ZJTest => ";

    private final String XML_TAG_TEST_SUITES = "testsuites";
    private final String XML_TAG_TEST_SUITE = "testsuite";
    private final String XML_TAG_TOTAL_TIME = "runtime";
    private final String XML_TAG_TEST_CASE = "testcase";
    private final String XML_TAG_TEST_CASE_RESULTS = "testresult";

    private Writer mWriter;
    private XmlSerializer mTestSuiteSerializer;

    private long mTestStarted;
    private long mSuiteStarted;

    private int mTestCount;  // include ignore test cases
    private int mSuiteCount = 0;
    private String mCurrentTestClassName;
    private String mLastTestClassName;

    private boolean mIsCurrentCaseFailed = false;

    @Override
    public void testRunStarted(Description description) {
        this.initFileWriter();
        if (mTestSuiteSerializer != null) {
            mTestCount = description.testCount();
            this.buildTestReportHeader();
        }
    }

    private void initFileWriter() {
        final String TEST_RESULTS_DIR = TestConstants.TEST_ROOT_DIR_PATH;
        final String JUNIT_XML_FILE =
                String.format("fun_settings_TEST-all_%s.xml", ShellUtils.getCurrentDateTime());

        File resultDir = new File(TEST_RESULTS_DIR);
        if (!resultDir.exists()) {
            Assert.assertTrue(String.format("mkdir failed: %s", resultDir.getAbsolutePath()),
                    resultDir.mkdir());
        }

        File resultFile = new File(TEST_RESULTS_DIR, JUNIT_XML_FILE);
        Log.d(TAG, ZJ_KEYWORD + "Saving testing report: " + resultFile.getAbsolutePath());
        try {
            mWriter = new FileWriter(resultFile);
            mTestSuiteSerializer = createSerializer(mWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private XmlSerializer createSerializer(Writer writer) {
        try {
            XmlPullParserFactory pf = XmlPullParserFactory.newInstance();
            XmlSerializer serializer = pf.newSerializer();
            serializer.setOutput(writer);
            return serializer;
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void buildTestReportHeader() {
        try {
            mTestSuiteSerializer.startDocument(null, null);

            mTestSuiteSerializer.startTag(null, XML_TAG_TEST_SUITES);
            mTestSuiteSerializer.startTag(null, "systeminfo");
            mTestSuiteSerializer.attribute(null, "sdkversion", android.os.Build.VERSION.RELEASE);
            mTestSuiteSerializer.attribute(null, "devicetype", android.os.Build.MODEL);
            mTestSuiteSerializer.endTag(null, "systeminfo");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testStarted(Description description) {
        if (mTestSuiteSerializer == null) {
            return;
        }

        mSuiteCount += 1;
        mCurrentTestClassName = description.getClassName();

        // run 1st case
        if (StringUtils.isEmpty(mLastTestClassName)) {
            mLastTestClassName = mCurrentTestClassName;
            this.buildTestSuiteHeader(mCurrentTestClassName);
        }

        // run other cases
        if (!mCurrentTestClassName.equals(mLastTestClassName)) {
            mSuiteCount -= 1;  // exclude new suite class
            this.buildTestSuiteInfo(mSuiteCount);
            this.buildTestSuiteHeader(mCurrentTestClassName);
            mSuiteCount = 1;  // start from new suite class
            mLastTestClassName = mCurrentTestClassName;
        }
        this.buildTestCaseHeader(description);
    }

    private void buildTestSuiteHeader(String suiteName) {
        mSuiteStarted = SystemClock.uptimeMillis();
        try {
            mTestSuiteSerializer.startTag(null, XML_TAG_TEST_SUITE);
            mTestSuiteSerializer.attribute(null, "name", suiteName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildTestSuiteInfo(int suiteCount) {
        final long runTime = SystemClock.uptimeMillis() - mSuiteStarted;
        try {
            mTestSuiteSerializer.startTag(null, "suiteinfo");
            mTestSuiteSerializer.attribute(null, "count", String.valueOf(suiteCount));
            mTestSuiteSerializer.attribute(null, XML_TAG_TOTAL_TIME, this.formatRunTime(runTime));
            mTestSuiteSerializer.endTag(null, "suiteinfo");
            mTestSuiteSerializer.endTag(null, XML_TAG_TEST_SUITE);
            mTestSuiteSerializer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildTestCaseHeader(Description description) {
        mTestStarted = SystemClock.uptimeMillis();
        try {
            mTestSuiteSerializer.startTag(null, XML_TAG_TEST_CASE);
            mTestSuiteSerializer.attribute(
                    null, "classname", this.getSimpleTestClassName(mCurrentTestClassName));
            mTestSuiteSerializer.attribute(null, "methodname", description.getMethodName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testFailure(Failure failure) {
        if (mTestSuiteSerializer == null) {
            return;
        }
        mIsCurrentCaseFailed = true;
        this.buildTestCaseInfo(failure);
    }

    @Override
    public void testFinished(Description description) {
        if (mTestSuiteSerializer == null) {
            return;
        }
        if (mIsCurrentCaseFailed) {
            mIsCurrentCaseFailed = false;
            return;
        }
        this.buildTestCaseInfo(null);
    }

    private void buildTestCaseInfo(Failure failure) {
        try {
            mTestSuiteSerializer.attribute(null, "time",
                    this.formatRunTime(SystemClock.uptimeMillis() - mTestStarted));
            if (failure != null) {
                this.buildFailedTestCaseInfo(failure);
            } else {
                this.buildFinishedTestCaseInfo();
            }
            mTestSuiteSerializer.endTag(null, XML_TAG_TEST_CASE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildFailedTestCaseInfo(Failure failure) throws IOException {
        final String TEST_FAIL = "failed";
        mTestSuiteSerializer.attribute(null, XML_TAG_TEST_CASE_RESULTS, TEST_FAIL);

        mTestSuiteSerializer.startTag(null, "failure");
        String messageText = failure.getMessage();
        if (StringUtils.isEmpty(messageText)) {
            messageText = "Exception";
        }
        mTestSuiteSerializer.attribute(null, "message", messageText);

        String traceText = failure.getTrace();
        if (!StringUtils.isEmpty(traceText)) {
            mTestSuiteSerializer.startTag(null, "trace");
            mTestSuiteSerializer.text(traceText);
            mTestSuiteSerializer.endTag(null, "trace");
        }
        mTestSuiteSerializer.endTag(null, "failure");
    }

    private void buildFinishedTestCaseInfo() throws IOException {
        final String TEST_PASS = "pass";
        mTestSuiteSerializer.attribute(null, XML_TAG_TEST_CASE_RESULTS, TEST_PASS);
    }

    @Override
    public void testRunFinished(Result result) {
        if (mTestSuiteSerializer != null) {
            this.buildTestSuiteInfo(mSuiteCount);
            this.buildTestReportSummary(result);
        }
    }

    private void buildTestReportSummary(Result result) {
        try {
            mTestSuiteSerializer.startTag(null, "summary");
            mTestSuiteSerializer.attribute(null, XML_TAG_TOTAL_TIME,
                    this.formatRunTime(result.getRunTime()));
            mTestSuiteSerializer.attribute(null, "total", String.valueOf(mTestCount));
            mTestSuiteSerializer.attribute(null, "totalfailed",
                    String.valueOf(result.getFailureCount()));
            mTestSuiteSerializer.attribute(null, "totalpass",
                    String.valueOf(result.getRunCount() - result.getFailureCount()));
            mTestSuiteSerializer.attribute(null, "ignore", String.valueOf(result.getIgnoreCount()));
            mTestSuiteSerializer.endTag(null, "summary");

            mTestSuiteSerializer.endTag(null, XML_TAG_TEST_SUITES);
            mTestSuiteSerializer.endDocument();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (mTestSuiteSerializer != null) {
                try {
                    mTestSuiteSerializer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mWriter != null) {
                try {
                    mWriter.flush();
                    mWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String formatRunTime(long runTime) {
        return String.format(Locale.getDefault(), "%.1fs", (runTime / 1000.0f));
    }

    private String getSimpleTestClassName(String fullClassName) {
        return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }

}
