package com.example.zhengjin.funsettingsuitest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public final class MainActivity extends AppCompatActivity {

    static final int SUCCESS_EXIT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText et = (EditText) this.findViewById(R.id.edit1);
        final Button btOk = (Button) this.findViewById(R.id.buttonOK);
        final Button btBack = (Button) this.findViewById(R.id.buttonBack);
        final TextView tv = (TextView) this.findViewById(R.id.text1);

        if (btOk != null) {
            btOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (et != null && tv != null) {
                        String tmp = et.getText().toString();
                        tv.setText(BuildHelloMessage(tmp));
                    }
                }
            });
        }

        if (btBack != null) {
            btBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    System.exit(SUCCESS_EXIT);
                }
            });
        }

    }

    public String BuildHelloMessage (String msg) {
        final String helloMsg = "Pls enter your name in edit text.";

        if ("".equals(msg) || msg == null) {
            return helloMsg;
        }

        return String.format("Hello, %s", msg);
    }
}
