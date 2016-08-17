package com.example.zhengjin.funsettingsuitest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.zhengjin.funsettingsuitest.R;

public final class ActivityMain extends AppCompatActivity {

    static final int SUCCESS_EXIT = 0;

    private Button mBtnRunDemo;
    private Button mBtnRunTest;
    private Button mBtnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initViews();

        if (mBtnRunDemo != null) {
            mBtnRunDemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ActivityMain.this, ActivityDemo.class);
                    ActivityMain.this.startActivity(intent);
                }
            });
        }

        if (mBtnExit != null) {
            mBtnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    System.exit(SUCCESS_EXIT);
                }
            });
        }

    }

    private void initViews() {

        mBtnRunDemo = (Button) this.findViewById(R.id.btnRunDemo);
        mBtnRunTest = (Button) this.findViewById(R.id.btnRunTest);
        mBtnExit = (Button) this.findViewById(R.id.btnExit);
    }

}
