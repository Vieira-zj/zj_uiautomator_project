package com.example.zhengjin.funsettingsuitest.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

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
    private static final String DEFAULT_COVERAGE_FILE_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/coverage.ec";

    private final Bundle mResults = new Bundle();
    private Intent mIntent;

    public JacocoInstrumentation() {
    }

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
        this.createCoverageFile(DEFAULT_COVERAGE_FILE_PATH);

        // init intent and start instrument
        mIntent = new Intent(this.getTargetContext(), InstrumentedActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        Looper.prepare();

        // start instrumented activity and set callback
        InstrumentedActivity activity = (InstrumentedActivity) this.startActivitySync(mIntent);
        activity.setFinishListener(this);
    }

    @Override
    public void onActivityFinished() {
        this.generateCoverageReport();
        this.finish(Activity.RESULT_OK, mResults);
    }

    public void createCoverageFile() {
        createCoverageFile(DEFAULT_COVERAGE_FILE_PATH);
    }

    private void createCoverageFile(String filePath) {
        Log.d(TAG, "createCoverageFile(): " + filePath);

        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new IOException("file.createNewFile() return false!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateCoverageReport() {
        Log.d(TAG, "generateCoverageReport(): " + DEFAULT_COVERAGE_FILE_PATH);

        OutputStream out = null;
        try {
            out = new FileOutputStream(DEFAULT_COVERAGE_FILE_PATH, false);
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

}
