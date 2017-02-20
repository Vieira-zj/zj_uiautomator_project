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

    private static final String XML_TAG_TEST_SUITES = "test_suites";
    private static final String XML_TAG_TEST_SUITE = "test_suite";
    private static final String XML_TAG_RUN_TIME = "run_time";
    private static final String XML_TAG_TEST_CASE = "test_case";
    private static final String XML_TAG_TEST_CASE_RESULTS = "test_result";

    private Writer mWriter;
    private XmlSerializer mTestSuiteSerializer;

    private long mTestStarted;
    private long mSuiteStarted;

    private boolean mIsCurrentTestCaseFailed = false;
    private int mCurrentCount = 0;
    private int mSuiteCount = 0;
    private String mCurrentTestClassName;
    private String mLastTestClassName;

    @Override
    public void testRunStarted(Description description) {
        this.initFileWriter();

        if (mTestSuiteSerializer != null) {
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
            mTestSuiteSerializer.startTag(null, "system_info");
            mTestSuiteSerializer.attribute(null, "sdk_version", android.os.Build.VERSION.RELEASE);
            mTestSuiteSerializer.attribute(null, "device_type", android.os.Build.MODEL);
            mTestSuiteSerializer.endTag(null, "system_info");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testStarted(Description description) {
        if (mTestSuiteSerializer == null) {
            return;
        }

        mCurrentCount += 1;
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
            mTestSuiteSerializer.startTag(null, "suite_info");
            mTestSuiteSerializer.attribute(null, "count", String.valueOf(suiteCount));
            mTestSuiteSerializer.attribute(null, XML_TAG_RUN_TIME, this.formatRunTime(runTime));
            mTestSuiteSerializer.endTag(null, "suite_info");
            mTestSuiteSerializer.endTag(null, XML_TAG_TEST_SUITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildTestCaseHeader(Description description) {
        mTestStarted = SystemClock.uptimeMillis();
        try {
            mTestSuiteSerializer.startTag(null, XML_TAG_TEST_CASE);
            mTestSuiteSerializer.attribute(
                    null, "class_name", this.getSimpleTestClassName(mCurrentTestClassName));
            mTestSuiteSerializer.attribute(null, "method_name", description.getMethodName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testFailure(Failure failure) {
        if (mTestSuiteSerializer == null) {
            return;
        }
        mIsCurrentTestCaseFailed = true;
        this.buildFailedTestCaseInfo(failure);
    }

    private void buildFailedTestCaseInfo(Failure failure) {
        final String TEST_FAIL = "failed";

        final long runTime = SystemClock.uptimeMillis() - mTestStarted;
        try {
            mTestSuiteSerializer.attribute(null,
                    XML_TAG_RUN_TIME, this.formatRunTime(runTime));
            mTestSuiteSerializer.attribute(null, XML_TAG_TEST_CASE_RESULTS, TEST_FAIL);

            mTestSuiteSerializer.startTag(null, "failure");
            mTestSuiteSerializer.attribute(null, "message", failure.getMessage());
            mTestSuiteSerializer.startTag(null, "trace_info");
            mTestSuiteSerializer.text(failure.getTrace());
            mTestSuiteSerializer.endTag(null, "trace_info");
            mTestSuiteSerializer.endTag(null, "failure");
            mTestSuiteSerializer.endTag(null, XML_TAG_TEST_CASE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testFinished(Description description) {
        if (mTestSuiteSerializer == null) {
            return;
        }

        if (mIsCurrentTestCaseFailed) {
            mIsCurrentTestCaseFailed = false;
            return;
        }

        this.buildFinishedTestCaseInfo();
        this.doSerializerFlush();
    }

    private void buildFinishedTestCaseInfo() {
        final String TEST_PASS = "pass";

        final long runTime = SystemClock.uptimeMillis() - mTestStarted;
        try {
            mTestSuiteSerializer.attribute(
                    null, XML_TAG_RUN_TIME, this.formatRunTime(runTime));
            mTestSuiteSerializer.attribute(null, XML_TAG_TEST_CASE_RESULTS, TEST_PASS);
            mTestSuiteSerializer.endTag(null, XML_TAG_TEST_CASE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doSerializerFlush() {
        final int COUNT_OF_FLUSH = 10;

        if (mCurrentCount >= COUNT_OF_FLUSH) {
            try {
                mTestSuiteSerializer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void testRunFinished(Result result) {
        if (mTestSuiteSerializer != null) {
            this.buildTestSuiteInfo(mSuiteCount);
            this.buildXmlReportSummary(result);
        }
    }

    private void buildXmlReportSummary(Result result) {
        try {
            mTestSuiteSerializer.startTag(null, "summary");
            mTestSuiteSerializer.attribute(null, "total_time",
                    this.formatRunTime(result.getRunTime()));
            mTestSuiteSerializer.attribute(null, "total", String.valueOf(result.getRunCount()));
            mTestSuiteSerializer.attribute(null, "total_failed",
                    String.valueOf(result.getFailureCount()));
            mTestSuiteSerializer.attribute(null, "total_pass",
                    String.valueOf(result.getRunCount() - result.getFailureCount()));
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
        return String.format(Locale.getDefault(), "%dms", runTime);
    }

    private String getSimpleTestClassName(String fullClassName) {
        return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }

}
