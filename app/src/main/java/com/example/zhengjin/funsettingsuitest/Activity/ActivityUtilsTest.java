package com.example.zhengjin.funsettingsuitest.activity;

import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhengjin.funsettingsuitest.R;
import com.example.zhengjin.funsettingsuitest.utils.DeviceUtils;
import com.example.zhengjin.funsettingsuitest.utils.PackageUtils;
import com.example.zhengjin.funsettingsuitest.utils.ShellUtils;

import java.util.List;
import java.util.Locale;

public final class ActivityUtilsTest extends AppCompatActivity {

    private static int DEVICE_UTILS = 1;
    private static int PACKAGE_UTILS = 2;

    private Button mBtnDeviceUtilsTest = null;
    private TextView mTextDeviceUtilsTest = null;
    private Button mBtnShellUtilsTest = null;
    private TextView mTextShellUtilsTest = null;
    private Button mBtnPkgUtilsTest = null;
    private TextView mTextPkgUtilsTest = null;
    private Button mBtnStartActivityTest = null;

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

        if (mBtnStartActivityTest != null) {
            mBtnStartActivityTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pkgName = "tv.fun.filemanager";
                    PackageUtils.startApp(pkgName);
                }
            });
        }
    }

    private void initViews() {
        mBtnDeviceUtilsTest = (Button) findViewById(R.id.btn_device_utils_test);
        mBtnShellUtilsTest = (Button) findViewById(R.id.btn_shell_utils_test);
        mBtnPkgUtilsTest = (Button) findViewById(R.id.btn_pkg_utils_test);
        mBtnStartActivityTest = (Button) findViewById(R.id.btn_start_activity_test);
        mTextDeviceUtilsTest = (TextView) findViewById(R.id.text_device_utils_test);
        mTextShellUtilsTest = (TextView) findViewById(R.id.text_shell_utils_test);
        mTextPkgUtilsTest = (TextView) findViewById(R.id.text_pkg_utils_test);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String text = (String) msg.obj;
            if (msg.what == DEVICE_UTILS) {
                mTextDeviceUtilsTest.setText(text);
            } else if (msg.what == PACKAGE_UTILS) {
                mTextPkgUtilsTest.setText(text);
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

    private String getDeviceModel() {
        return String.format(mLocale, "Device Model: %s\n", DeviceUtils.getDeviceModel());
    }

    private String getDeviceId() {
        return String.format(mLocale, "Device id: %s\n", DeviceUtils.getDeviceId(this));
    }

    private String getOsVersion() {
        return String.format(mLocale, "Android OS version: %s\n", DeviceUtils.getOsVersion());
    }

    private String getDisplayMatrix() {
        int displayHeight = DeviceUtils.getDisplayHeight(this);
        int displayWidth = DeviceUtils.getDisplayWidth(this);
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

}
