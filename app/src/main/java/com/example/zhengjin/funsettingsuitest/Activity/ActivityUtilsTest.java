package com.example.zhengjin.funsettingsuitest.activity;

import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhengjin.funsettingsuitest.R;
import com.example.zhengjin.funsettingsuitest.utils.DeviceUtils;
import com.example.zhengjin.funsettingsuitest.utils.FileUtils;
import com.example.zhengjin.funsettingsuitest.utils.PackageUtils;
import com.example.zhengjin.funsettingsuitest.utils.ShellUtils;

import java.io.File;
import java.util.List;
import java.util.Locale;

public final class ActivityUtilsTest extends AppCompatActivity {

    private static final  String TAG = ActivityUtilsTest.class.getSimpleName();

    private static int DEVICE_UTILS = 1;
    private static int PACKAGE_UTILS = 2;
    private static int FILE_UTILS = 3;

    private Button mBtnDeviceUtilsTest = null;
    private TextView mTextDeviceUtilsTest = null;
    private Button mBtnShellUtilsTest = null;
    private TextView mTextShellUtilsTest = null;
    private Button mBtnPkgUtilsTest = null;
    private TextView mTextPkgUtilsTest = null;
    private Button mBtnFileUtilsTest = null;
    private TextView mTextFileUtilsTest = null;
    private Button mBtnStartActivityTest = null;
    private TextView mTextStartActivityTest = null;

    private boolean mFlagStartActivity = true;

    private final Locale mLocale = Locale.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utils_test);
        this.initViews();

        if (mBtnDeviceUtilsTest != null) {
            mBtnDeviceUtilsTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mTextDeviceUtilsTest != null) {
                        new Thread(new DeviceUtilsRunnable()).start();
                    }
                }
            });
        }

        if (mBtnShellUtilsTest != null) {
            mBtnShellUtilsTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mTextShellUtilsTest != null) {
                        String command = "cat /system/build.prop | grep ro.product.model";
                        ShellUtils.CommandResult cr = ShellUtils.execCommand(command, false, true);
                        String text = String.format(mLocale,
                                "Result code: %d\n Success message: %s\n Error message: %s",
                                cr.mResult, cr.mSuccessMsg, cr.mErrorMsg);
                        mTextShellUtilsTest.setText(text);
                    }
                }
            });
        }

        if (mBtnPkgUtilsTest != null) {
            mBtnPkgUtilsTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mTextPkgUtilsTest != null) {
                        new Thread(new PackageUtilsRunnable()).start();
                    }
                }
            });
        }

        if (mBtnFileUtilsTest != null) {
            mBtnFileUtilsTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mTextFileUtilsTest != null) {
                        new Thread(new FileUtilsRunnable()).start();
                    }
                }
            });
        }

        if (mBtnStartActivityTest != null) {
            mBtnStartActivityTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pkgName = "tv.fun.filemanager";
                    if (mFlagStartActivity) {
                        PackageUtils.startApp(pkgName);
                        if (PackageUtils.isAppOnTop(pkgName)) {
                            Log.d(TAG, String.format(mLocale, "The package (%s) is on top.", pkgName));
                        }

                        mFlagStartActivity = false;
                        String killMessage = "Kill Background Process Test";
                        mTextStartActivityTest.setText(killMessage);
                    } else {
                        PackageUtils.killBgProcess(pkgName);
                        mFlagStartActivity = true;
                        String startMessage = "Start Activity Test";
                        mTextStartActivityTest.setText(startMessage);
                    }
                }
            });
        }
    }

    private void initViews() {
        mBtnDeviceUtilsTest = (Button) findViewById(R.id.btn_device_utils_test);
        mBtnShellUtilsTest = (Button) findViewById(R.id.btn_shell_utils_test);
        mBtnPkgUtilsTest = (Button) findViewById(R.id.btn_pkg_utils_test);
        mBtnStartActivityTest = (Button) findViewById(R.id.btn_start_activity_test);
        mBtnFileUtilsTest = (Button) findViewById(R.id.btn_file_utils_test);
        mTextDeviceUtilsTest = (TextView) findViewById(R.id.text_device_utils_test);
        mTextShellUtilsTest = (TextView) findViewById(R.id.text_shell_utils_test);
        mTextPkgUtilsTest = (TextView) findViewById(R.id.text_pkg_utils_test);
        mTextFileUtilsTest = (TextView) findViewById(R.id.text_file_utils_test);
        mTextStartActivityTest = (TextView) findViewById(R.id.text_start_activity_test);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String text = (String) msg.obj;
            if (msg.what == DEVICE_UTILS) {
                mTextDeviceUtilsTest.setText(text);
            } else if (msg.what == PACKAGE_UTILS) {
                mTextPkgUtilsTest.setText(text);
            } else if (msg.what == FILE_UTILS) {
                mTextFileUtilsTest.setText(text);
            }
        }
    };

    private class DeviceUtilsRunnable implements Runnable {
        @Override
        public void run() {
            final int size = 15;
            StringBuilder sb = new StringBuilder(size);
            sb.append(getDeviceModel());
            sb.append(getDeviceId());
            sb.append(getOsVersion());
            sb.append(getDisplayMatrix());
            sb.append(getCpuModel());
            sb.append(getCpuCores());
            sb.append(getCpuFreq());
            sb.append(getTotalMemory());
            sb.append(getFreeMemory());

            Message msg = Message.obtain();
            msg.obj = sb.toString();
            msg.what = DEVICE_UTILS;
            handler.sendMessage(msg);
        }
    }

    private class PackageUtilsRunnable implements Runnable {
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder(10);
            sb.append(getCurPkgName());
            sb.append(getInstalledApps());
            sb.append(getRunningProcessName());

            Message msg = Message.obtain();
            msg.obj = sb.toString();
            msg.what = PACKAGE_UTILS;
            handler.sendMessage(msg);
        }
    }

    private class FileUtilsRunnable implements Runnable {
        @Override
        public void run() {
            String path = String.format(mLocale, "%s/%s", getSdcardPath(), "test.log");

            StringBuilder sb = new StringBuilder(10);
            sb.append("test\n");
            sb.append("test line one\n");
            sb.append("test line two\n");
            sb.append(String.format(mLocale,
                    "Sdcard total size: %d Mb\n", FileUtils.getDirTotalSize(getSdcardPath())));
            sb.append(String.format(mLocale,
                    "Sdcard free size: %d Mb\n", FileUtils.getDirFreeSize(getSdcardPath())));
            FileUtils.writeFileSdcard(path, sb.toString());

            String tmpStr = "test line three\n";
            FileUtils.writeFileSdcard(path, tmpStr, true);
            SystemClock.sleep(1000);

            Message msg = Message.obtain();
            msg.obj = FileUtils.readFileSdcard(path);
            msg.what = FILE_UTILS;
            handler.sendMessage(msg);

            // clear after file utils test
            FileUtils.deleteFile(new File(path));
        }
    }

    private String getDeviceModel() {
        return String.format(mLocale, "Device Model: %s\n", DeviceUtils.getDeviceModel());
    }

    private String getDeviceId() {
        return String.format(mLocale, "Device id: %s\n", DeviceUtils.getDeviceId());
    }

    private String getOsVersion() {
        return String.format(mLocale, "Android OS version: %s\n", DeviceUtils.getOsVersion());
    }

    private String getDisplayMatrix() {
        int displayHeight = DeviceUtils.getDisplayHeight();
        int displayWidth = DeviceUtils.getDisplayWidth();
        return String.format(mLocale, "Display: %d * %d\n", displayWidth, displayHeight);
    }

    private String getCpuModel() {
        return String.format(mLocale, "CPU Model: %s\n", DeviceUtils.getCpuModel());
    }

    private String getCpuCores() {
        return String.format(mLocale, "CPU Cores: %d\n", DeviceUtils.getNumberOfCpuCores());
    }

    private String getCpuFreq() {
        float freq = (DeviceUtils.getCpuFrequency() / 1000f / 1000f);
        return String.format(mLocale, "CPU Frequency: %.3f GHz\n", freq);
    }

    private String getTotalMemory() {
        long total = (DeviceUtils.getTotalMemory() / 1024L);
        return String.format(mLocale, "Total Memory: %d MB\n", total);
    }

    private String getFreeMemory() {
        long free = (DeviceUtils.getFreeMemory() / 1024L / 1024L);
        return String.format(mLocale, "Free Memory: %d MB\n", free);
    }

    private String getCurPkgName() {
        PackageInfo pkgInfo = PackageUtils.getAppPackageInfo(getPackageName());
        if (pkgInfo == null) {
            return "null";
        }
        return String.format(mLocale, "Current package name: %s\n", pkgInfo.packageName);
    }

    private String getInstalledApps() {
        List<String> installedApps = PackageUtils.getInstalledApps(false);
        StringBuilder sb = new StringBuilder(30);
        for (String app : installedApps) {
            sb.append(String.format(mLocale, "%s \n", app));
        }
        return String.format(mLocale, "Installed APPs: %s", sb.toString());
    }

    private String getRunningProcessName() {
        List<ActivityManager.RunningAppProcessInfo> runningApps =
                PackageUtils.getRunningAppsProcessInfo();
        StringBuilder sb = new StringBuilder(40);
        sb.append(String.format(mLocale, "Running process: %d\n", runningApps.size()));
        for (ActivityManager.RunningAppProcessInfo app : runningApps) {
            int memPss = PackageUtils.getProcessMemPss(app.pid);
            sb.append(String.format(mLocale, "%s : %s : %d \n", app.pid, app.processName, memPss));
        }

        return sb.toString();
    }

    private String getSdcardPath() {
        File file = FileUtils.getExternalStorageDir();
        if (file == null) {
            Log.e(TAG, "The external storage (sdcard) is not available!");
            return "";
        }
        return file.getAbsolutePath();
    }

}
