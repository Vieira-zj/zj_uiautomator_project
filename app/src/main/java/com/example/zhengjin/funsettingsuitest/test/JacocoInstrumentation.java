package com.example.zhengjin.funsettingsuitest.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by zhengjin on 2017/6/2.
 * <p>
 * For Jacoco.
 * Implement callback onActivityFinished(), and call instrument activity.
 */

public class JacocoInstrumentation extends Instrumentation implements FinishListener {

    private static final String TAG = "JacocoInstrumentation:";
    private static String DEFAULT_COVERAGE_FILE_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/coverage.ec";

    private final Bundle mResults = new Bundle();
    private Intent mIntent;
    private String mCoverageFilePath;

    public JacocoInstrumentation() {
    }

    @Override
    public void onCreate(Bundle arguments) {
        Log.d(TAG, "onCreate(" + arguments + ")");
        super.onCreate(arguments);

        DEFAULT_COVERAGE_FILE_PATH = this.getContext().getFilesDir().getPath() + "/coverage.ec";
        if (arguments != null) {
            mCoverageFilePath = arguments.getString("coverageFile");
        }

        // init intent and start instrument
        mIntent = new Intent(this.getTargetContext(), InstrumentedActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.start();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();

        Looper.prepare();
        // start instrument activity and set callback
        InstrumentedActivity activity = (InstrumentedActivity) startActivitySync(mIntent);
        activity.setFinishListener(this);
    }

    @Override
    public void onActivityFinished() {
        Log.d(TAG, "onActivityFinished()");
        this.generateCoverageReport();
        this.finish(Activity.RESULT_OK, mResults);
    }

    private void generateCoverageReport() {
        final String savePath = this.getCoverageFilePath();
        Log.d(TAG, "generateCoverageReport(): " + savePath);

        File file = new File(savePath);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new IOException("Create new file failed!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        OutputStream out = null;
        try {
            out = new FileOutputStream(savePath, false);
            Object agent =
                    Class.forName("org.jacoco.agent.rt.RT").getMethod("getAgent").invoke(null);
            out.write((byte[]) agent.getClass()
                    .getMethod("getExecutionData", boolean.class).invoke(agent, false));
        } catch (Exception e) {
            Log.d(TAG, e.toString(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getCoverageFilePath() {
        return StringUtils.isEmpty(mCoverageFilePath) ?
                DEFAULT_COVERAGE_FILE_PATH : mCoverageFilePath;
    }

}
