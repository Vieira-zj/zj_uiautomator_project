package com.example.zhengjin.funsettingsuitest.testsuites;

import android.util.Log;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;

/**
 * Created by zhengjin on 2016/9/29.
 *
 * Include the properties when running the test cases.
 */

public final class RunnerProfile {

    private static final String TAG = RunnerProfile.class.getSimpleName();

    public static boolean isTakeScreenshot = false;

    public static int countAndPrintTestCasesForClass(Class<?> cls) {
        int count = 0;
        Method[] methods = cls.getMethods();
        for (Method m : methods) {
            Annotation annotation = m.getAnnotation(org.junit.Test.class);
            if (annotation != null) {
                Log.d(TAG, String.format(
                        "Class %s, test case -> %s", cls.getSimpleName(), m.getName()));
                ++count;
            }
        }

        Log.d(TAG, String.format("Class %s, total number of test cases -> %d",
                cls.getSimpleName(), count));
        return count;
    }

}
