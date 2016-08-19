package com.example.zhengjin.funsettingsuitest.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhengjin.funsettingsuitest.R;

public class ActivityDemo extends AppCompatActivity {

    private EditText mEditUserName;
    private TextView mTextHelloMsg;
    private Button mBtnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        this.initViews();

        if (mBtnOk != null) {
            mBtnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mEditUserName != null && mTextHelloMsg != null) {
                        String input = mEditUserName.getText().toString();
                        if ("".equals(input)) {
                            Toast.makeText(ActivityDemo.this,
                                    "Please input your name first!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        mTextHelloMsg.setText(String.format("Hello, %s", input));
                    }
                }
            });
        }
    }

    private void initViews() {

        mEditUserName = (EditText) findViewById(R.id.editorUserName);
        mBtnOk = (Button) findViewById(R.id.buttonOK);
        mTextHelloMsg = (TextView) findViewById(R.id.textHelloMsg);
    }

}
