package com.example.zhengjin.funsettingsuitest.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhengjin.funsettingsuitest.R;
import com.example.zhengjin.funsettingsuitest.Utils.DeviceUtils;

public final class ActivityUtilsTest extends AppCompatActivity {

    Button mBtnUtilsTest = null;
    TextView mTextUtilsTest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utils_test);
        this.initViews();

        if (mBtnUtilsTest != null) {
            mBtnUtilsTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mTextUtilsTest != null) {
                        String line1 =
                                String.format("Device model: %s\n", DeviceUtils.getDeviceModel());

                        int displayHeight = DeviceUtils.getDisplayHeight(ActivityUtilsTest.this);
                        int displayWidth = DeviceUtils.getDisplayWidth(ActivityUtilsTest.this);
                        String line2 =
                                String.format("Display: %d * %d\n", displayWidth, displayHeight);

                        final int size = 15;
                        StringBuilder sb = new StringBuilder(size);
                        sb.append(line1);
                        sb.append(line2);
                        mTextUtilsTest.setText(sb.toString());
                    }
                }
            });
        }
    }

    private void initViews() {

        mBtnUtilsTest = (Button) findViewById(R.id.btnUtilsTest);
        mTextUtilsTest = (TextView) findViewById(R.id.textUtilsTest);
    }
}
