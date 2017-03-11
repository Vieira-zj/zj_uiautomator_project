package com.example.zhengjin.funsettingsuitest.testrunner;

import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
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
    // Flag take snapshot after each test case done
    public static boolean isTakeSnapshot = false;
    public static boolean isAccountVipFree = false;

    // Below properties set auto
    public static boolean isPlatform938 = isPlatformChipType938();
    public static boolean isVersion30 = isFunSystemVersion30();

    // Global properties set in test cases
    public static String deviceName = "风行电视";

    private RunnerProfile() {
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

    private static boolean isPlatformChipType938() {
        return getPlatformChipType938() == TestConstants.PlatformChipType.MSTAR938;
    }

    private static TestConstants.PlatformChipType getPlatformChipType938() {
        String results = runShellCommand("getprop | grep chiptype");
        if (!StringUtils.isEmpty(results) && results.contains("938")) {
            return TestConstants.PlatformChipType.MSTAR938;
        }

        return TestConstants.PlatformChipType.MSTAR638;
    }

    private static boolean isFunSystemVersion30() {
        return getSystemVersion() == TestConstants.FunSystemVersion.V3;
    }

    private static TestConstants.FunSystemVersion getSystemVersion() {
        String results = runShellCommand("getprop | grep version.incremental");
        if (StringUtils.isEmpty(results)) {
            return TestConstants.FunSystemVersion.V2;
        }

        Pattern pattern = Pattern.compile("([0-9]{1,2}\\.){3}[0-9]{1,2}");
        Matcher matcher = pattern.matcher(results);
        if (matcher.find()) {
            if (isVersionNumber30(matcher.group())) {
                return TestConstants.FunSystemVersion.V3;
            }
        }

        return TestConstants.FunSystemVersion.V2;
    }

    private static boolean isVersionNumber30(String version) {
        String[] nums = version.split("\\.");
        return Integer.parseInt(nums[0]) >= 3;
    }

    private static String runShellCommand(String cmd) {
        ShellUtils.CommandResult cr = ShellUtils.execCommand(cmd, false, true);
        if (cr.mResult != 0 || StringUtils.isEmpty(cr.mSuccessMsg)) {
            return "";
        }

        return cr.mSuccessMsg;
    }

}
