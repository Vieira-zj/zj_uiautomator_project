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
 * Implement callback onActivityFinished(), and call instrumented activity.
 * <p>
 * Run command:
 * $ adb shell am instrument com.example.zhengjin.funsettingsuitest/.test.JacocoInstrumentation
 */
public class JacocoInstrumentation extends Instrumentation implements FinishListener {

    private static final String TAG = "JacocoInstrumentation:";

    private Intent mIntent;
    private String mCoverageFilePath;

    public JacocoInstrumentation() {
    }

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
        try {
            this.createCoverageFile();
        } catch (Exception e) {
            e.printStackTrace();
            this.finish(-1, null);
        }

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
        this.finish(Activity.RESULT_OK, new Bundle());
    }

    private void createCoverageFile() throws Exception {
        createCoverageFile("", "");
    }

    public void createCoverageFile(String fileName) throws Exception {
        createCoverageFile("", fileName);
    }

    private void createCoverageFile(String fileDirAbsPath, String fileName) throws Exception {
        final String defaultDirPath =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/CoverageTest/";
        final String defaultFileName = "coverage.ec";

        final String fDir = StringUtils.isEmpty(fileDirAbsPath) ? defaultDirPath : fileDirAbsPath;
        final String fName = StringUtils.isEmpty(fileName) ? defaultFileName : fileName;
        mCoverageFilePath = fDir + fName;
        Log.d(TAG, "createCoverageFile(): " + mCoverageFilePath);

        File dir = new File(fDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new Exception("file.mkdirs() return false!");
            }
        }

        File file = new File(mCoverageFilePath);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new Exception("file.createNewFile() return false!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateCoverageReport() {
        Log.d(TAG, "generateCoverageReport(): " + mCoverageFilePath);

        OutputStream out = null;
        try {
            out = new FileOutputStream(mCoverageFilePath, false);
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
