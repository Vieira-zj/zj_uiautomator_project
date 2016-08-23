package com.example.zhengjin.funsettingsuitest.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by zhengjin on 2016/8/23.
 *
 * Includes utils for android shell ENV.
 */
public final class ShellUtils {

    public static final String TAG = ShellUtils.class.getSimpleName();

    public static final String COMMAND_SU = "su";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    public static CommandResult execCommand(
            String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand(new String[] {command}, isRoot, isNeedResultMsg);
    }

    public static CommandResult execCommand(
            List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
        return execCommand((commands == null ? null : commands.toArray(new String[] {})),
                isRoot, isNeedResultMsg);
    }

    public static CommandResult execCommand(
            String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0 ) {
            return new CommandResult(result, null, null);
        }

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        DataOutputStream os = null;

        try {
            process = Runtime.getRuntime().exec(isRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (StringUtils.isEmpty(command)) {
                    continue;
                }
                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String tmpStr;
                while ((tmpStr = successResult.readLine()) != null) {
                    successMsg.append(tmpStr);
                }
                while ((tmpStr = errorResult.readLine()) != null) {
                    errorMsg.append(tmpStr);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }

            if (process != null) {
                process.destroy();
            }
        }

        return new CommandResult(result,
                (successMsg != null ? (StringUtils.isEmpty(successMsg.toString()) ? "null" : successMsg.toString()) : null),
                (errorMsg != null ? (StringUtils.isEmpty(errorMsg.toString()) ? "null" : errorMsg.toString()) : null));
    }

    public static class CommandResult {
        public int mResult;
        public String mSuccessMsg;
        public String mErrorMsg;

        public CommandResult(int results, String successMsg, String errorMsg) {
            mResult = results;
            mSuccessMsg = successMsg;
            mErrorMsg = errorMsg;
        }
    }

}
