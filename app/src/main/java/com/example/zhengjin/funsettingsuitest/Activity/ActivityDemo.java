package com.example.zhengjin.funsettingsuitest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhengjin.funsettingsuitest.R;

import java.util.Locale;

public final class ActivityDemo extends AppCompatActivity {

    private EditText mEditorUserName = null;
    private TextView mTextHelloMsg = null;
    private Button mBtnOk = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        this.initViews();

        if (mBtnOk != null) {
            mBtnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mEditorUserName != null && mTextHelloMsg != null) {
                        String input = mEditorUserName.getText().toString();
                        if ("".equals(input)) {
                            Toast.makeText(ActivityDemo.this,
                                    "Please input your name!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        mTextHelloMsg.setText(
                                String.format(Locale.getDefault(), "Hello, %s", input));
                    }
                }
            });
        }
    }

    private void initViews() {

        mEditorUserName = (EditText) findViewById(R.id.editor_user_name);
        mBtnOk = (Button) findViewById(R.id.button_ok);
        mTextHelloMsg = (TextView) findViewById(R.id.text_hello_msg);
    }

}
