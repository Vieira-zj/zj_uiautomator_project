package com.example.zhengjin.funsettingsuitest.testsuites;

import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhengjin on 2016/9/29.
 * <p>
 * Include the properties when running the test cases.
 */
public final class RunnerProfile {

    private static final String TAG = RunnerProfile.class.getSimpleName();

    // NOTE: set below properties manual before run test cases
    public static boolean isTakeScreenshot = false;
    public static boolean isAccountVipFree = false;

    // Below properties set auto
    public static boolean isPlatform938 = false;
    public static boolean isVersion30 = false;

    // Global properties
    public static String deviceName = "风行电视";

    static {
        setPlatformChipType();
        setSystemVersion();
    }

    public static int countAndPrintTestCasesForClass(Class<?> cls) {
        int count = 0;
        String clsName = cls.getSimpleName();
        Method[] methods = cls.getMethods();

        for (Method m : methods) {
            Annotation annotation = m.getAnnotation(org.junit.Test.class);
            if (annotation != null) {
                Log.d(TAG, String.format("Class %s, test case -> %s", clsName, m.getName()));
                ++count;
            }
        }
        Log.d(TAG, String.format("Class %s, total number of test cases -> %d", clsName, count));
        return count;
    }

    private static void setPlatformChipType() {
        String results = runShellCommand("getprop | grep chiptype");
        if (StringUtils.isEmpty(results)) {
            return;
        }
        if (results.contains("938")) {
            isPlatform938 = true;
        }
    }

    private static void setSystemVersion() {
        String results = runShellCommand("getprop | grep version.incremental");
        if (StringUtils.isEmpty(results)) {
            return;
        }

        Pattern pattern = Pattern.compile("([0-9]{1,2}\\.){3}[0-9]{1,2}");
        Matcher matcher = pattern.matcher(results);
        if (matcher.find()) {
            if (isSystemVersion30(matcher.group())) {
                isVersion30 = true;
            }
        }
    }

    private static String runShellCommand(String cmd) {
        ShellUtils.CommandResult cr = ShellUtils.execCommand(cmd, false, true);
        if (cr.mResult != 0 || StringUtils.isEmpty(cr.mSuccessMsg)) {
            return "";
        }

        return cr.mSuccessMsg;
    }

    private static boolean isSystemVersion30(String version) {
        String[] nums = version.split("\\.");
        return Integer.parseInt(nums[0]) >= 3;
    }

}
