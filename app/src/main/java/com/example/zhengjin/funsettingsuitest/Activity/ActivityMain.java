package com.example.zhengjin.funsettingsuitest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.zhengjin.funsettingsuitest.R;
import com.example.zhengjin.funsettingsuitest.TestApplication;
import com.example.zhengjin.funsettingsuitest.test.JacocoHelper;
import com.example.zhengjin.funsettingsuitest.utils.HelperUtils;

public class ActivityMain extends AppCompatActivity {

    private Button mBtnStartDemo = null;
    private Button mBtnStartInstTest = null;
    private Button mBtnStartUtilsTest = null;
    private Button mBtnStartUtilsTest2 = null;
    private Button mBtnExit = null;

    private boolean mIsGenerateCoverageFile = TestApplication.IS_COVERAGE_TEST_ENABLE;
    private JacocoHelper jacocoHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // use instrumented activity instead of directly inject code in main activity
        if (mIsGenerateCoverageFile) {
            String tmpFileName = String.format("coverage_%s.ec", HelperUtils.getCurrentTime());
            jacocoHelper = new JacocoHelper();
            try {
                jacocoHelper.createCoverageFile(tmpFileName);
            } catch (Exception e) {
                e.printStackTrace();
                this.finish();
            }
        }

        setContentView(R.layout.activity_main);
        this.initViews();

        if (mBtnStartDemo != null) {
            mBtnStartDemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ActivityMain.this, ActivityDemo.class);
                    startActivity(intent);
                }
            });
        }

        if (mBtnStartInstTest != null) {
            mBtnStartInstTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ActivityMain.this, ActivityRunUiTest.class);
                    startActivity(intent);
                }
            });
        }

        if (mBtnStartUtilsTest != null) {
            mBtnStartUtilsTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ActivityMain.this, ActivityUtilsTest.class);
                    startActivity(intent);
                }
            });
        }

        if (mBtnStartUtilsTest2 != null) {
            mBtnStartUtilsTest2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ActivityMain.this, ActivityUtilsTest2.class);
                    startActivity(intent);
                }
            });
        }

        if (mBtnExit != null) {
            mBtnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    private void initViews() {
        mBtnStartDemo = (Button) findViewById(R.id.btn_start_demo);
        mBtnStartInstTest = (Button) findViewById(R.id.btn_start_inst_test);
        mBtnStartUtilsTest = (Button) findViewById(R.id.btn_start_utils_test);
        mBtnStartUtilsTest2 = (Button) findViewById(R.id.btn_start_utils_test2);
        mBtnExit = (Button) findViewById(R.id.btn_exit_app);
    }

    @Override
    public void onDestroy() {
        if (mIsGenerateCoverageFile && jacocoHelper != null) {
            jacocoHelper.generateCoverageReport();
        }

        super.onDestroy();
    }

}
