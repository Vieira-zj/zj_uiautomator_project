package com.example.zhengjin.funsettingsuitest.utils;

/**
 * Created by zhengjin on 2016/8/23.
 *
 * Include the utils for String handle.
 */
public final class StringUtils {

    @SuppressWarnings("unused")
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    @SuppressWarnings("unused")
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

    public static boolean isNumeric(String str) {
        for (int i = 0, length = str.length(); i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
