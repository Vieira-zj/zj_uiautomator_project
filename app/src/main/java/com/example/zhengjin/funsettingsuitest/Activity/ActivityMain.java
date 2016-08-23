package com.example.zhengjin.funsettingsuitest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.zhengjin.funsettingsuitest.R;

public final class ActivityMain extends AppCompatActivity {

    private Button mBtnStartDemo = null;
    private Button mBtnStartInstTest = null;
    private Button mBtnStartUtilsTest = null;
    private Button mBtnExit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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

        mBtnStartDemo = (Button) findViewById(R.id.btnStartDemo);
        mBtnStartInstTest = (Button) findViewById(R.id.btnStartInstTest);
        mBtnStartUtilsTest = (Button) findViewById(R.id.btnStartUtilsTest);
        mBtnExit = (Button) findViewById(R.id.btnExitApp);
    }

}
