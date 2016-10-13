package com.example.zhengjin.funsettingsuitest.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.zhengjin.funsettingsuitest.R;
import com.example.zhengjin.funsettingsuitest.service.ServiceDialog;
import com.example.zhengjin.funsettingsuitest.service.ServiceMonitor;
import com.example.zhengjin.funsettingsuitest.utils.DeviceUtils;
import com.example.zhengjin.funsettingsuitest.utils.PackageUtils;

import java.util.Locale;

public final class ActivityUtilsTest2 extends AppCompatActivity {

    //    private static final String TAG = ActivityUtilsTest2.class.getSimpleName();

    private final Locale mLocale = Locale.getDefault();

    private final String SERVICE_TEXT_STATE_KEY = "SERVICE_TEXT_STATE_KEY";
    private final String POPUP_WINDOW_TEXT_STATE_KEY = "POPUP_WINDOW_TEXT_STATE_KEY";

    private Button mBtnProcessUtilsTest = null;
    private TextView mTextProcessUtilsTest = null;
    private Button mBtnDialogTest = null;
    private TextView mTextDialogTest = null;
    private Button mBtnGlobalDialogTest = null;
    private TextView mTextGlobalDialogTest = null;
    private Button mBtnPopupWindowTest = null;
    private TextView mTextPopupWindowTest = null;
    private Button mBtnFpsTest = null;
    private TextView mTextFpsTest = null;

    private Dialog mDialog;
    private PopupWindow mWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utils_test2);
        this.initViews();
        this.initDialog();
        this.initPopupWindow();

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
                    mDialog.show();
                }
            });
        }

        if (mBtnGlobalDialogTest != null) {
            mBtnGlobalDialogTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ComponentName service =
                            new ComponentName(ActivityUtilsTest2.this, ServiceDialog.class);
                    Intent intent = new Intent(ActivityUtilsTest2.this, ServiceDialog.class);
                    if (PackageUtils.isServiceRunning(service)) {
                        stopService(intent);
                        final String text = "Global dialog is dismiss";
                        mTextGlobalDialogTest.setText(text);
                    } else {
                        startService(intent);
                        final String text = "Global dialog is showing";
                        mTextGlobalDialogTest.setText(text);
                    }
                }
            });
        }

        if (mBtnPopupWindowTest != null) {
            mBtnPopupWindowTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mWindow.isShowing()) {
                        mWindow.dismiss();
                        final String text = "Popup window is dismiss";
                        mTextPopupWindowTest.setText(text);
                    } else {
                        showPopupWindow();
                        final String text = "Popup window is show";
                        mTextPopupWindowTest.setText(text);
                    }
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
        mBtnGlobalDialogTest = (Button) this.findViewById(R.id.btn_global_dialog_test);
        mBtnPopupWindowTest = (Button) this.findViewById(R.id.btn_popup_window_test);
        mBtnFpsTest = (Button) this.findViewById(R.id.btn_fps_test);

        mTextProcessUtilsTest = (TextView) this.findViewById(R.id.text_process_utils_test);
        mTextDialogTest = (TextView) this.findViewById(R.id.text_dialog_test);
        mTextGlobalDialogTest = (TextView) this.findViewById(R.id.text_global_dialog_test);
        mTextPopupWindowTest = (TextView) this.findViewById(R.id.text_popup_window_test);
        mTextFpsTest = (TextView) this.findViewById(R.id.text_fps_test);
    }

    private void initDialog() {
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_layout);
        mDialog.setTitle("Dialog Test");

        Window dialogWindow = mDialog.getWindow();
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

    private void initPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View layout = inflater.inflate(R.layout.popup_window_layout, null);
        layout.getBackground().setAlpha(100);  // 0~255

        mWindow = new PopupWindow(layout, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void showPopupWindow() {
        mWindow.showAtLocation(this.findViewById(R.id.layout_activity_utils_test2),
                Gravity.START | Gravity.TOP, 50, 50);
//        this.setBackgroundAlpha(0.3f);
    }

    private void setBackgroundAlpha(float bgAlpha) {
        // set Alpha for whole screen background
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha;  // 0.0~1.0
        this.getWindow().setAttributes(lp);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SERVICE_TEXT_STATE_KEY, mTextFpsTest.getText().toString());
        outState.putString(POPUP_WINDOW_TEXT_STATE_KEY, mTextPopupWindowTest.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void restoreSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SERVICE_TEXT_STATE_KEY)) {
                mTextFpsTest.setText(savedInstanceState.getString(SERVICE_TEXT_STATE_KEY));
            }
            if (savedInstanceState.containsKey(POPUP_WINDOW_TEXT_STATE_KEY)) {
                mTextPopupWindowTest.setText(savedInstanceState.getString(
                        POPUP_WINDOW_TEXT_STATE_KEY));
            }
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
