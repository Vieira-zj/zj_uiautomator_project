package com.example.zhengjin.funsettingsuitest.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.example.zhengjin.funsettingsuitest.TestApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by zhengjin on 2016/8/23.
 *
 * Include the utils for android device.
 */
public final class DeviceUtils {

    private static final String TAG = DeviceUtils.class.getSimpleName();
    private static final TestApplication CONTEXT = TestApplication.getInstance();

    private static String sDeviceId = "";
    private static String sCpuModel = "";

    private static int sDisplayWidth = 0;
    private static int sDisplayHeight = 0;
    private static int sNumberOfCpuCores = 0;
    private static int sCpuFrequency = 0;
    private static long sFreeMemory = 0L;
    private static long sTotalMemory = 0L;

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getDeviceId() {
        if (StringUtils.isEmpty(sDeviceId)) {
            TelephonyManager tm =
                    (TelephonyManager) CONTEXT.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                sDeviceId = tm.getDeviceId();
            }
        }
        return sDeviceId;
    }

    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static int getDisplayHeight() {
        if (sDisplayHeight <= 0) {
            getDisplayMetrics(CONTEXT);
        }
        return sDisplayHeight;
    }

    public static int getDisplayWidth() {
        if (sDisplayWidth <= 0) {
            getDisplayMetrics(CONTEXT);
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
                        sCpuModel = items[1].trim();
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

    public static long getFreeMemory() {
        if (sFreeMemory == 0) {
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            ActivityManager am = (ActivityManager) CONTEXT.getSystemService(Context.ACTIVITY_SERVICE);
            am.getMemoryInfo(memoryInfo);
            sFreeMemory = memoryInfo.availMem;
        }
        return sFreeMemory;  // Byte
    }

    public static long getTotalMemory() {
        if (sTotalMemory == 0) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(new File("/proc/meminfo")));
                String tempStr;
                while ((tempStr = br.readLine()) != null) {
                    if (tempStr.contains("MemTotal")) {
                        String memory = tempStr.split(":")[1];
                        memory = memory.trim().split(" ")[0];
                        if (StringUtils.isNumeric(memory)) {
                            sTotalMemory = Long.parseLong(memory);
                        }
                    }
                }
            } catch (IOException e) {
                CONTEXT.logException(TAG, e);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        CONTEXT.logException(TAG, e);
                    }
                }
            }
        }

        return sTotalMemory;  // KB
    }
}
