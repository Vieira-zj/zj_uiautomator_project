package com.example.zhengjin.funsettingsuitest.activity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhengjin.funsettingsuitest.R;
import com.example.zhengjin.funsettingsuitest.service.ServiceMonitor;
import com.example.zhengjin.funsettingsuitest.utils.DeviceUtils;
import com.example.zhengjin.funsettingsuitest.utils.PackageUtils;

import java.util.Locale;

public final class ActivityUtilsTest2 extends AppCompatActivity {

    //    private static final String TAG = ActivityUtilsTest2.class.getSimpleName();

    private final Locale mLocale = Locale.getDefault();
    private final String SERVICE_TEXT_STATE_KEY = "SERVICE_TEXT_STATE_KEY";

    private Button mBtnProcessUtilsTest = null;
    private TextView mTextProcessUtilsTest = null;
    private Button mBtnDialogTest = null;
    private TextView mTextDialogTest = null;
    private Button mBtnFpsTest = null;
    private TextView mTextFpsTest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utils_test2);
        this.initViews();

        final Dialog dialog = new Dialog(this);
        this.initDialog(dialog);

        if (mBtnProcessUtilsTest != null) {
            mBtnProcessUtilsTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pkgName = "com.bestv.ott";
                    StringBuilder sb = new StringBuilder(5);
                    sb.append(getTopActivityName());
                    sb.append(isAppRunning(pkgName));
                    sb.append(getPackageMemorySize(pkgName));
                    sb.append(getTrafficDataByPackageName(pkgName));

                    mTextProcessUtilsTest.setText(sb.toString());
                }
            });
        }

        if (mBtnDialogTest != null) {
            mBtnDialogTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String text = "Dialog is showing";
                    mTextDialogTest.setText(text);
                    dialog.show();
                }
            });
        }

        if (mBtnFpsTest != null) {
            mBtnFpsTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ComponentName service =
                            new ComponentName(ActivityUtilsTest2.this, ServiceMonitor.class);
                    Intent intent = new Intent(ActivityUtilsTest2.this, ServiceMonitor.class);

                    if (PackageUtils.isServiceRunning(service)) {
                        stopService(intent);
                        final String text = "monitor service stop";
                        mTextFpsTest.setText(text);
                    } else {
                        final String text = "monitor service started";
                        startService(intent);
                        mTextFpsTest.setText(text);
                    }
                }
            });
        }

        this.restoreSavedInstanceState(savedInstanceState);
    }

    private void initViews() {
        mBtnProcessUtilsTest = (Button) this.findViewById(R.id.btn_process_utils_test);
        mBtnDialogTest = (Button) this.findViewById(R.id.btn_dialog_test);
        mBtnFpsTest = (Button) this.findViewById(R.id.btn_fps_test);
        mTextProcessUtilsTest = (TextView) this.findViewById(R.id.text_process_utils_test);
        mTextDialogTest = (TextView) this.findViewById(R.id.text_dialog_test);
        mTextFpsTest = (TextView) this.findViewById(R.id.text_fps_test);
    }

    private void initDialog(Dialog dialog) {
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setTitle("fps");

        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.START | Gravity.TOP);

            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.x = 50;
            lp.y = 50;
            lp.width = 300;
            lp.height = 400;
            lp.alpha = 0.7f;
            dialogWindow.setAttributes(lp);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SERVICE_TEXT_STATE_KEY, mTextFpsTest.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void restoreSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(SERVICE_TEXT_STATE_KEY)) {
            mTextFpsTest.setText(savedInstanceState.getString(SERVICE_TEXT_STATE_KEY));
        }
    }

    private String isAppRunning(String pkgName) {
        int pid = PackageUtils.getPidByPackageName(pkgName);
        if (pid != -1) {
            return String.format(mLocale, "Package %s is running, and pid is %d\n", pkgName, pid);
        }
        return String.format("Package %s is not running.\n", pkgName);
    }

    private String getTopActivityName() {
        String topActivity = PackageUtils.getTopActivity();
        return String.format("The top activity: %s\n", (topActivity == null ? "null" : topActivity));
    }

    private String getPackageMemorySize(String pkgName) {
        int pid = PackageUtils.getPidByPackageName(pkgName);
        if (pid == -1) {
            return String.format("Package %s is not running.\n", pkgName);
        }

        int memSize = DeviceUtils.getMemorySizeByPid(pid);
        return String.format(mLocale,
                "The memory size for package [%s] is: %d KB\n", pkgName, memSize);
    }

    private String getTrafficDataByPackageName(String pkgName) {
        int uid = PackageUtils.getUidByPackageName(pkgName);
        long pkgTraffic = DeviceUtils.getTrafficInfoByUid(uid);
        pkgTraffic = pkgTraffic / 1024L;  // Byte to KB
        if (pkgTraffic <= 0L) {
            return String.format("There is no traffic data for package [%s]!\n", pkgName);
        }

        return String.format(mLocale,
                "The total traffic data for package [%s] is: %d KB\n", pkgName, pkgTraffic);
    }

}
