package com.example.zhengjin.funsettingsuitest.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhengjin.funsettingsuitest.R;
import com.example.zhengjin.funsettingsuitest.service.ServiceUiTestRunner;
import com.example.zhengjin.funsettingsuitest.utils.PackageUtils;
import com.example.zhengjin.funsettingsuitest.utils.StringUtils;

import java.util.List;

import static com.example.zhengjin.funsettingsuitest.TestApplication.EXTRA_KEY_EXEC_TIME;
import static com.example.zhengjin.funsettingsuitest.TestApplication.EXTRA_KEY_INST_METHOD;
import static com.example.zhengjin.funsettingsuitest.TestApplication.EXTRA_KEY_TEST_PACKAGE;
import static com.example.zhengjin.funsettingsuitest.TestApplication.EXTRA_KEY_TEST_RUNNER;

public final class ActivityRunUiTest extends AppCompatActivity {

//    private static final String TAG = ActivityRunUiTest.class.getSimpleName();

    private EditText mEditorInstTestMethod = null;
    private EditText mEditorExecTime = null;
    private ListView mListViewInstTests = null;
    private Button mBtnRunInstTest = null;
    private TextView mTextInstRunStatus = null;

    private List<InstrumentationInfo> mListInstInfo = null;
    private InstrumentationInfo mSelectInst = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_ui_test);
        this.initViews();
        this.initData();
        mListViewInstTests.setAdapter(new ListAdapter(this));

        if (mBtnRunInstTest != null) {
            mBtnRunInstTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!verifyTestMethodEditor() || !verifyExecTimeEditor() ||
                            !verifySelectedInstrument()) {
                        return;
                    }

                    Intent intent = new Intent(ActivityRunUiTest.this, ServiceUiTestRunner.class);
//                    Intent intent = new Intent("com.zhengjin.test.intentservice");
                    intent.putExtra(EXTRA_KEY_EXEC_TIME, mEditorExecTime.getText().toString());
                    intent.putExtra(EXTRA_KEY_INST_METHOD, mEditorInstTestMethod.getText().toString());
                    intent.putExtra(EXTRA_KEY_TEST_PACKAGE, mSelectInst.packageName);
                    intent.putExtra(EXTRA_KEY_TEST_RUNNER, mSelectInst.name);
                    ActivityRunUiTest.this.startService(intent);

                    ActivityRunUiTest.this.setRunningStatus();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ComponentName service = new ComponentName(this, ServiceUiTestRunner.class);
        if (PackageUtils.isServiceRunning(service)) {
            this.setRunningStatus();
        } else {
            if (!mBtnRunInstTest.isEnabled()) {
                this.setStopStatus();
            }
        }
    }

    private void setRunningStatus() {
        String tmpStr = "Instrument test is running ...";
        mBtnRunInstTest.setEnabled(false);
        mTextInstRunStatus.setText(tmpStr);
    }

    private void setStopStatus() {
        mBtnRunInstTest.setEnabled(true);
        mTextInstRunStatus.setText("");
    }

    private boolean verifySelectedInstrument() {
        if (mSelectInst == null) {
            String msg = "Please select a instrument package!";
            Toast.makeText(ActivityRunUiTest.this, msg, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean verifyExecTimeEditor() {
        if (mEditorExecTime == null) {
            return false;
        }

        String execTime = mEditorExecTime.getText().toString();
        if (StringUtils.isEmpty(execTime)) {
            String msg = "Please input the execution time!";
            Toast.makeText(ActivityRunUiTest.this, msg, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!StringUtils.isNumeric(execTime)) {
            String msg = "Please input the integer in execution time!";
            Toast.makeText(ActivityRunUiTest.this, msg, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean verifyTestMethodEditor() {
        if (mEditorInstTestMethod == null) {
            return false;
        }

        if (StringUtils.isEmpty(mEditorInstTestMethod.getText().toString())) {
            String msg = "Please input the instrument test method!";
            Toast.makeText(ActivityRunUiTest.this, msg, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void initViews() {
        mEditorExecTime = (EditText) findViewById(R.id.editor_exec_time);
        mEditorInstTestMethod = (EditText) findViewById(R.id.editor_inst_test_method);
        mListViewInstTests = (ListView) findViewById(R.id.list_inst_tests);
        mBtnRunInstTest = (Button) findViewById(R.id.btn_run_inst_test);
        mTextInstRunStatus = (TextView) findViewById(R.id.text_inst_run_status);
    }

    private void initData() {
        mListInstInfo = PackageUtils.queryInstrumentTests();

        // for default
        String defaultTestMethod = "testCases.TestFunTvFilm#testDemo";
        mEditorInstTestMethod.setText(defaultTestMethod);
    }

    private class ListAdapter extends BaseAdapter {
        private LayoutInflater inflater = null;
        private int mPrePosition = -1;

        ListAdapter(Context context) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mListInstInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return mListInstInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if ((convertView == null) || (convertView.getTag() == null)) {
                convertView = inflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();
                holder.radioBtn = (RadioButton) convertView.findViewById(R.id.instTestRadioBtn);
                holder.instTestName = (TextView) convertView.findViewById(R.id.instTestName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.radioBtn.setId(position);
            holder.radioBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RadioButton buttonView = (RadioButton) view;
                    int curPosition = buttonView.getId();
                    if (curPosition == mPrePosition) {
                        return;
                    }

                    buttonView.setChecked(true);
                    if (mPrePosition != -1) {
                        RadioButton tmpBtn = (RadioButton) findViewById(mPrePosition);
                        tmpBtn.setChecked(false);
                    }
                    mSelectInst = mListInstInfo.get(curPosition);
                    mPrePosition = curPosition;
                }
            });
            holder.instTestName.setText(mListInstInfo.get(position).packageName);

            return convertView;
        }

        private class ViewHolder {
            RadioButton radioBtn;
            TextView instTestName;
        }
    }

}
