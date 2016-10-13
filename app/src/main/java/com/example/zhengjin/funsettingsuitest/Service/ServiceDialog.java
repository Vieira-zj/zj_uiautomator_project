package com.example.zhengjin.funsettingsuitest.service;

import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.zhengjin.funsettingsuitest.R;

public final class ServiceDialog extends Service {

    private Dialog mDialog;
    private Handler mHandler;
    private Runnable mRunnable;

    private final long delay = 1000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.initDialog();
        this.initHandler();
        mHandler.post(mRunnable);
    }

    private void initDialog() {
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.dialog_layout);
        mDialog.setTitle("Dialog Test");

        Window dialogWindow = mDialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
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

    private void initHandler() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
                mHandler.postDelayed(this, delay);
            }
        };
    }

    @Override
    public void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        super.onDestroy();
    }
}
