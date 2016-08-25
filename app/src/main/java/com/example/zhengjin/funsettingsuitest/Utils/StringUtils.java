package com.example.zhengjin.funsettingsuitest.utils;

/**
 * Created by zhengjin on 2016/8/23.
 *
 * Include the utils for String handle.
 */
public final class StringUtils {

    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    private static String objectToString(Object str) {
        if (str == null) {
            return "";
        }
        if (str instanceof String) {
            return (String) str;
        } else {
            return str.toString();
        }
    }

}
