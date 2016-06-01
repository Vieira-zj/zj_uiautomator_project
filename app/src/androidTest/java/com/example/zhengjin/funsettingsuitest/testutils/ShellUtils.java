package com.example.zhengjin.funsettingsuitest.testutils;

import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by zhengjin on 2016/5/31.
 *
 * Includes the utils for shell env.
 */
public final class ShellUtils {

    private final static String TAG = ShellUtils.class.getSimpleName();

    private static String getShellCommandOutput(Process process) throws IOException {

        InputStream inputstream = process.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

        String line = "";
        StringBuilder sb = new StringBuilder(line);
        while ((line = bufferedreader.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }

        return sb.toString();  // not return null
    }

    public static boolean execShellCommand(String cmd) {

        Runtime runtime = Runtime.getRuntime();
        Process process;

        try {
            process = runtime.exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try {
            if (process.waitFor() != 0) {
                Log.d(TAG, String.format("***** exit value = %d", process.exitValue()));
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static String execShellCommandWithOutput(String cmd) {

        Runtime runtime = Runtime.getRuntime();
        Process process;

        final String ERROR = "error";
        final String NULL = "null";
        String output;
        try {
            process = runtime.exec(cmd);

            String tmp = getShellCommandOutput(process);
            if ("".equals(tmp)) {
                output = NULL;
            } else {
                output = tmp;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;
        }

        try {
            if (process.waitFor() != 0) {
                Log.d(TAG, String.format("***** exit value = %d", process.exitValue()));
                output = ERROR;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ERROR;
        }

        return output;
    }

    @Deprecated
    public static void execShellRootCommand(String cmd) {

        Process process = null;

        try {
            process = Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        if (process == null) {
            Log.e(TAG, "The runtime process is null.");
            System.exit(1);
        }
        PrintWriter pw = new PrintWriter(process.getOutputStream());
        pw.println(cmd);
        pw.flush();

        try {
            if (process.waitFor() != 0) {
                Log.d(TAG, String.format("exit value = %d", process.exitValue()));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void systemWait(long ms) {
        SystemClock.sleep(ms);
    }


}
