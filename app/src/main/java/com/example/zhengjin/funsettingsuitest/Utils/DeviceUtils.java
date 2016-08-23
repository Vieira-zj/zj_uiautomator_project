package com.example.zhengjin.funsettingsuitest.Utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by zhengjin on 2016/8/23.
 *
 * Include the utils for android device.
 */
public final class DeviceUtils {

    private static String sDeviceModel = "";
    private static String sDeviceId = "";
    private static String sOsVersion = "";
    private static String sCpuModel = "";

    private static int sDisplayWidth = 0;
    private static int sDisplayHeight = 0;
    private static int sNumberOfCpuCores = 0;
    private static int sCpuFrequency = 0;

    private static final String TAG = DeviceUtils.class.getSimpleName();

    public static String getDeviceModel() {
        if (StringUtils.isEmpty(sDeviceModel)) {
            sDeviceModel = Build.MODEL;
        }
        return sDeviceModel;
    }

    public static String getDeviceId(Context context) {
        if (StringUtils.isEmpty(sDeviceId)) {
            TelephonyManager tm =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                sDeviceId = tm.getDeviceId();
            }
        }
        return sDeviceId;
    }

    public static String getOsVersion() {
        if (StringUtils.isEmpty(sOsVersion)) {
            sOsVersion = Build.VERSION.RELEASE;
        }
        return sOsVersion;
    }

    public static int getDisplayHeight(Context context) {
        if (sDisplayHeight <= 0) {
            getDisplayMetrics(context);
        }
        return sDisplayHeight;
    }

    public static int getDisplayWidth(Context context) {
        if (sDisplayWidth <= 0) {
            getDisplayMetrics(context);
        }
        return sDisplayWidth;
    }

    private static void getDisplayMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            Display display = wm.getDefaultDisplay();
            if (display != null) {
                DisplayMetrics dm = new DisplayMetrics();
                display.getMetrics(dm);
                sDisplayHeight = dm.heightPixels;
                sDisplayWidth = dm.widthPixels;
            }
        }
    }

    public static int getNumberOfCpuCores() {
        if (sNumberOfCpuCores <= 0) {
            sNumberOfCpuCores = Runtime.getRuntime().availableProcessors();
        }
        return sNumberOfCpuCores;
    }

    public static int getCpuFrequency() {
        if (sCpuFrequency <= 0) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(
                        new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"));
                String cpuFreq = reader.readLine();
                if (!StringUtils.isEmpty(cpuFreq)) {
                    sCpuFrequency = Integer.parseInt(cpuFreq.trim());
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        }
        return sCpuFrequency;
    }

    public static String getCpuModel() {
        if (StringUtils.isEmpty(sCpuModel)) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader("/proc/cpuinfo"));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("model")) {
                        String[] items = line.split(":");
                        return items[1].trim();
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        }

        return sCpuModel;
    }

}
