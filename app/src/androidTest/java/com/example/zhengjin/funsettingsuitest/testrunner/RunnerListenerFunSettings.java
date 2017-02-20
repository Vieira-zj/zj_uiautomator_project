package com.example.zhengjin.funsettingsuitest.testrunner;

import android.os.SystemClock;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

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

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.TEST_ROOT_DIR_PATH;

/**
 * Created by zhengjin on 2017/2/20.
 * <p>
 * Customized runner listener to generate testing report.
 */

@SuppressWarnings("unused")
public final class RunnerListenerFunSettings extends RunListener {

    private static final String TAG = RunnerListenerFunSettings.class.getSimpleName();
    private static final String ZJ_KEYWORD = "ZJTest => ";

    private static final String XML_TAG_TEST_SUITES = "test_suites";
    private static final String XML_TAG_TEST_CASE = "test_case";
    private static final String XML_TAG_TEST_CASE_RUN_TIME = "run_time";
    private static final String XML_TAG_TEST_CASE_RESULTS = "test_result";

    private Writer mWriter;
    private XmlSerializer mTestSuiteSerializer;
    private long mTestStarted;
    private int mCurrentCount = 0;

    @Override
    public void testRunStarted(Description description) {
        this.initFileWriter();
        if (mTestSuiteSerializer != null) {
            this.buildXmlReportHeader();
        }
    }

    private void initFileWriter() {
        final String TEST_RESULTS_DIR = TEST_ROOT_DIR_PATH;
        final String JUNIT_XML_FILE =
                String.format("fun_settings_TEST-all_%s.xml", ShellUtils.getCurrentDateTime());

        File resultDir = new File(TEST_RESULTS_DIR);
        if (!resultDir.exists()) {
            Assert.assertTrue(String.format("mkdir failed: %s", resultDir.getAbsolutePath()),
                    resultDir.mkdir());
        }

        File resultFile = new File(TEST_RESULTS_DIR, JUNIT_XML_FILE);
        Log.d(TAG, String.format("Saving xml report: %s", resultFile.getAbsolutePath()));
        try {
            mWriter = new FileWriter(resultFile);
            mTestSuiteSerializer = newSerializer(mWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildXmlReportHeader() {
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

    private XmlSerializer newSerializer(Writer writer) {
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

    @Override
    public void testStarted(Description description) {
        if (mTestSuiteSerializer != null) {
            this.buildTestCaseHeader(description);
        }
    }

    private void buildTestCaseHeader(Description description) {
        mTestStarted = SystemClock.uptimeMillis();

        try {
            mTestSuiteSerializer.startTag(null, XML_TAG_TEST_CASE);
            mTestSuiteSerializer.attribute(null, "class_name", description.getClassName());
            mTestSuiteSerializer.attribute(null, "method_name", description.getMethodName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testFinished(Description description) {
        mCurrentCount += 1;
        if (mTestSuiteSerializer != null) {
            this.buildTestCaseInfoIfFinished(description);
        }
        this.doSerializerFlush();
    }

    private void buildTestCaseInfoIfFinished(Description description) {
        final String TEST_PASS = "pass";
        final long runTime = SystemClock.uptimeMillis() - mTestStarted;

        try {
            mTestSuiteSerializer.attribute(
                    null, XML_TAG_TEST_CASE_RUN_TIME, this.formatRunTime(runTime));
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
    public void testFailure(Failure failure) {
        mCurrentCount += 1;
        if (mTestSuiteSerializer != null) {
            this.buildTestCaseInfoIfFailure(failure);
        }
    }

    private void buildTestCaseInfoIfFailure(Failure failure) {
        final String TEST_FAIL = "failed";
        final long runTime = SystemClock.uptimeMillis() - mTestStarted;

        try {
            mTestSuiteSerializer.startTag(null, "failure");
            mTestSuiteSerializer.attribute(null, "message", failure.getMessage());
            mTestSuiteSerializer.attribute(null, "trace", failure.getTrace());
            mTestSuiteSerializer.endTag(null, "failure");

            mTestSuiteSerializer.attribute(null,
                    XML_TAG_TEST_CASE_RUN_TIME, this.formatRunTime(runTime));
            mTestSuiteSerializer.attribute(null, XML_TAG_TEST_CASE_RESULTS, TEST_FAIL);
            mTestSuiteSerializer.endTag(null, XML_TAG_TEST_CASE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testRunFinished(Result result) {
        if (mTestSuiteSerializer != null) {
            this.buildXmlReportSummary(result);
        }
    }

    private void buildXmlReportSummary(Result result) {
        try {
            mTestSuiteSerializer.startTag(null, "summary");
            mTestSuiteSerializer.attribute(null, "total_time",
                    this.formatRunTime(result.getRunTime()));
            mTestSuiteSerializer.attribute(null, "total", String.valueOf(result.getRunCount()));
            mTestSuiteSerializer.attribute(null, "total_pass",
                    String.valueOf(result.getRunCount() - result.getFailureCount()));
            mTestSuiteSerializer.attribute(null, "total_failed",
                    String.valueOf(result.getFailureCount()));
            mTestSuiteSerializer.endTag(null, "summary");

            mTestSuiteSerializer.endTag(null, XML_TAG_TEST_SUITES);
            mTestSuiteSerializer.endDocument();
            mTestSuiteSerializer.flush();
            mWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (mWriter != null) {
                try {
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

//    private String getTestSuiteName(String fullClassName) {
//        return fullClassName.substring(0, fullClassName.lastIndexOf("."));
//    }

}
