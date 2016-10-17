package com.example.zhengjin.funsettingsuitest.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
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

    private final String FPS_SERVICE_TEXT_STATE_KEY = "FPS_SERVICE_TEXT_STATE_KEY";
    private final String FLOATING_WINDOW_TEXT_STATE_KEY = "FLOATING_WINDOW_TEXT_STATE_KEY";

    private Button mBtnProcessUtilsTest = null;
    private TextView mTextProcessUtilsTest = null;
    private Button mBtnDialogTest = null;
    private TextView mTextDialogTest = null;
    private Button mBtnGlobalDialogTest = null;
    private TextView mTextGlobalDialogTest = null;
    private Button mBtnPopupWindowTest = null;
    private TextView mTextPopupWindowTest = null;
    private Button mBtnFloatingWindowTest = null;
    private TextView mTextFloatingWindowTest = null;
    private Button mBtnFpsTest = null;
    private TextView mTextFpsTest = null;

    private PopupWindow mWindow = null;

    private WindowManager mWindowManager = null;
    private View mFloatingWindow;
    private boolean mIsFloating = false;

    private Intent mDialogServiceIntent = null;
    private ComponentName mDialogServiceComponent = null;
    private Intent mFpsServiceIntent = null;
    private ComponentName mFpsServiceComponent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utils_test2);
        this.initViews();

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
                    initDialog().show();
                    final String text = "Show dialog";
                    mTextDialogTest.setText(text);
                }
            });
        }

        if (mBtnGlobalDialogTest != null) {
            this.initGlobalDialogServiceInfo();
            mBtnGlobalDialogTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (PackageUtils.isServiceRunning(mDialogServiceComponent)) {
                        stopService(mDialogServiceIntent);
                        final String text = "Global dialog is dismiss";
                        mTextGlobalDialogTest.setText(text);
                    } else {
                        startService(mDialogServiceIntent);
                        final String text = "Global dialog is showing";
                        mTextGlobalDialogTest.setText(text);
                    }
                }
            });
            mBtnGlobalDialogTest.setVisibility(View.GONE);
        }
        if (mTextGlobalDialogTest != null) {
            mTextGlobalDialogTest.setVisibility(View.GONE);
        }

        if (mBtnPopupWindowTest != null) {
            this.initPopupWindow();
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

        if (mBtnFloatingWindowTest != null) {
            mBtnFloatingWindowTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mIsFloating) {
                        createFloatingWindow();
                        final String text = "Floating window is show";
                        mTextFloatingWindowTest.setText(text);
                    } else {
                        destroyFloatingWindow();
                        final String text = "Floating window is dismiss";
                        mTextFloatingWindowTest.setText(text);
                    }
                }
            });
        }

        if (mBtnFpsTest != null) {
            this.initFpsServiceInfo();
            mBtnFpsTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (PackageUtils.isServiceRunning(mFpsServiceComponent)) {
                        stopService(mFpsServiceIntent);
                        final String text = "monitor service stop";
                        mTextFpsTest.setText(text);
                    } else {
                        final String text = "monitor service started";
                        startService(mFpsServiceIntent);
                        mTextFpsTest.setText(text);
                    }
                }
            });
        }

        this.restoreSavedInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        if (PackageUtils.isServiceRunning(mDialogServiceComponent)) {
            this.stopService(mDialogServiceIntent);
        }
        if (PackageUtils.isServiceRunning(mFpsServiceComponent)) {
            this.stopService(mFpsServiceIntent);
        }

        if (mIsFloating) {
            this.destroyFloatingWindow();
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(FPS_SERVICE_TEXT_STATE_KEY, mTextFpsTest.getText().toString());
        outState.putString(FLOATING_WINDOW_TEXT_STATE_KEY,
                mTextFloatingWindowTest.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void restoreSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(FPS_SERVICE_TEXT_STATE_KEY)) {
                mTextFpsTest.setText(savedInstanceState.getString(FPS_SERVICE_TEXT_STATE_KEY));
            }
            if (savedInstanceState.containsKey(FLOATING_WINDOW_TEXT_STATE_KEY)) {
                mTextFloatingWindowTest.setText(savedInstanceState.getString(
                        FLOATING_WINDOW_TEXT_STATE_KEY));
            }
        }
    }

    private void initViews() {
        mBtnProcessUtilsTest = (Button) this.findViewById(R.id.btn_process_utils_test);
        mBtnDialogTest = (Button) this.findViewById(R.id.btn_dialog_test);
        mBtnGlobalDialogTest = (Button) this.findViewById(R.id.btn_global_dialog_test);
        mBtnPopupWindowTest = (Button) this.findViewById(R.id.btn_popup_window_test);
        mBtnFloatingWindowTest = (Button) this.findViewById(R.id.btn_floating_window_test);
        mBtnFpsTest = (Button) this.findViewById(R.id.btn_fps_test);

        mTextProcessUtilsTest = (TextView) this.findViewById(R.id.text_process_utils_test);
        mTextDialogTest = (TextView) this.findViewById(R.id.text_dialog_test);
        mTextGlobalDialogTest = (TextView) this.findViewById(R.id.text_global_dialog_test);
        mTextPopupWindowTest = (TextView) this.findViewById(R.id.text_popup_window_test);
        mTextFloatingWindowTest = (TextView) this.findViewById(R.id.text_floating_window_test);
        mTextFpsTest = (TextView) this.findViewById(R.id.text_fps_test);
    }

    private Dialog initDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setTitle("Dialog Test");

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

        return dialog;
    }

    @SuppressLint("InflateParams")
    private void initPopupWindow() {
        View windowLayout = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.popup_window_layout, null);
        windowLayout.getBackground().setAlpha(100);  // 0~255

        mWindow = new PopupWindow(windowLayout, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void showPopupWindow() {
        mWindow.showAtLocation(this.findViewById(R.id.layout_activity_utils_test2),
                Gravity.START | Gravity.TOP, 50, 50);
//        this.setBackgroundAlpha(0.1f);
    }

    private void setBackgroundAlpha(float bgAlpha) {
        // set Alpha for full screen background
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha;  // 0.0~1.0
        this.getWindow().setAttributes(lp);
    }

    private void initGlobalDialogServiceInfo() {
        mDialogServiceIntent =
                new Intent(ActivityUtilsTest2.this, ServiceDialog.class);
        mDialogServiceComponent =
                new ComponentName(ActivityUtilsTest2.this, ServiceDialog.class);
    }

    @SuppressLint("InflateParams")
    private void createFloatingWindow() {
        mWindowManager = (WindowManager)
                getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mFloatingWindow = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.floating_window_layout, null);
        mWindowManager.addView(mFloatingWindow, this.initFloatingWindowLayout());
        mIsFloating = true;
    }

    private WindowManager.LayoutParams initFloatingWindowLayout() {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        wmParams.gravity = Gravity.START | Gravity.TOP;
        wmParams.x = 100;
        wmParams.y = 100;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.alpha = 0.7f;

        return wmParams;
    }

    private void destroyFloatingWindow() {
        if (mWindowManager != null && mFloatingWindow != null) {
            mWindowManager.removeView(mFloatingWindow);
            mIsFloating = false;
            mFloatingWindow = null;
        }
    }

    private void initFpsServiceInfo() {
        mFpsServiceIntent = new Intent(ActivityUtilsTest2.this, ServiceMonitor.class);
        mFpsServiceComponent = new ComponentName(ActivityUtilsTest2.this, ServiceMonitor.class);
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
