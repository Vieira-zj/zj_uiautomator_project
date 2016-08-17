package com.example.zhengjin.funsettingsuitest.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhengjin.funsettingsuitest.R;

public class ActivityDemo extends AppCompatActivity {

    private EditText mEdit;
    private TextView mText;
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
                    if (mEdit != null && mText != null) {
                        String tmp = mEdit.getText().toString();
                        mText.setText(BuildHelloMessage(tmp));
                    }
                }
            });
        }
    }

    private void initViews() {

        mEdit = (EditText) this.findViewById(R.id.edit1);
        mText = (TextView) this.findViewById(R.id.text1);
        mBtnOk = (Button) this.findViewById(R.id.buttonOK);
    }

    private String BuildHelloMessage (String msg) {

        String helloMsg = "Please enter your name.";
        if ("".equals(msg) || msg == null) {
            return helloMsg;
        }

        return String.format("Hello, %s", msg);
    }

}
