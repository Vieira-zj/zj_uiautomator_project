package com.example.zhengjin.funsettingsuitest.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhengjin.funsettingsuitest.R;
import com.example.zhengjin.funsettingsuitest.Utils.DeviceUtils;
import com.example.zhengjin.funsettingsuitest.Utils.PackageUtils;
import com.example.zhengjin.funsettingsuitest.Utils.ShellUtils;

import java.util.Locale;

public final class ActivityUtilsTest extends AppCompatActivity {

    Button mBtnDeviceUtilsTest = null;
    TextView mTextDeviceUtilsTest = null;
    Button mBtnShellUtilsTest = null;
    TextView mTextShellUtilsTest = null;
    Button mBtnPkgUtilsTest = null;
    TextView mTextPkgUtilsTest = null;

    private static int DEVICE_UTILS = 1;

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
                        String text = String.format(Locale.getDefault(),
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
                        String tmpStr = PackageUtils.getAppPackageName(ActivityUtilsTest.this);
                        mTextPkgUtilsTest.setText(tmpStr);
                    }
                }
            });
        }
    }

    private void initViews() {
        mBtnDeviceUtilsTest = (Button) findViewById(R.id.btn_device_utils_test);
        mBtnShellUtilsTest = (Button) findViewById(R.id.btn_shell_utils_test);
        mBtnPkgUtilsTest = (Button) findViewById(R.id.btn_pkg_utils_test);
        mTextDeviceUtilsTest = (TextView) findViewById(R.id.text_device_utils_test);
        mTextShellUtilsTest = (TextView) findViewById(R.id.text_shell_utils_test);
        mTextPkgUtilsTest = (TextView) findViewById(R.id.text_pkg_utils_test);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DEVICE_UTILS) {
                String text = (String) msg.obj;
                mTextDeviceUtilsTest.setText(text);
            }
        }
    };

    private class DeviceUtilsRunnable implements Runnable {
        @Override
        public void run() {
            final int size = 15;
            StringBuilder sb = new StringBuilder(size);
            sb.append(taskGetDeviceModel());
            sb.append(taskGetDeviceId());
            sb.append(taskGetOsVersion());
            sb.append(taskGetDisplayMatrix());
            sb.append(taskGetCpuModel());
            sb.append(taskGetCpuCores());
            sb.append(taskGetCpuFreq());

            Message msg = Message.obtain();
            msg.obj = sb.toString();
            msg.what = DEVICE_UTILS;
            handler.sendMessage(msg);
        }
    }

    private String taskGetDeviceModel() {
        return String.format(
                Locale.getDefault(), "Device Model: %s\n", DeviceUtils.getDeviceModel());
    }

    private String taskGetDeviceId() {
        return String.format(Locale.getDefault(),
                "Device id: %s\n", DeviceUtils.getDeviceId(this));
    }

    private String taskGetOsVersion() {
        return String.format(Locale.getDefault(),
                "Android OS version: %s\n", DeviceUtils.getOsVersion());
    }

    private String taskGetDisplayMatrix() {
        int displayHeight = DeviceUtils.getDisplayHeight(this);
        int displayWidth = DeviceUtils.getDisplayWidth(this);
        return String.format(Locale.getDefault(),
                "Display: %d * %d\n", displayWidth, displayHeight);
    }

    private String taskGetCpuModel() {
        return String.format(Locale.getDefault(), "CPU Model: %s\n", DeviceUtils.getCpuModel());
    }

    private String taskGetCpuCores() {
        return String.format(Locale.getDefault(),
                "CPU Cores: %d\n", DeviceUtils.getNumberOfCpuCores());
    }

    private String taskGetCpuFreq() {
        float freq = (DeviceUtils.getCpuFrequency() / 1000f / 1000f);
        return String.format(Locale.getDefault(), "CPU Frequency: %.3f GHz\n", freq);
    }

}
