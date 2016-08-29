package com.example.zhengjin.funsettingsuitest.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.util.List;

public final class ActivityRunUiTest extends AppCompatActivity {

    private static final String TAG = ActivityRunUiTest.class.getSimpleName();

    private EditText mEditorInstTestMethod = null;
    private EditText mEditorExecTime = null;
    private ListView mListViewInstTests = null;
    private Button mBtnRunInstTest = null;
    private TextView mTextInstTestFullName = null;

    private List<InstrumentationInfo> mListInstInfo = null;
    private InstrumentationInfo mSelectInst = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_ui_test);
        this.initViews();

        this.queryInstrumentTests();
        mListViewInstTests.setAdapter(new ListAdapter(this));

        if (mBtnRunInstTest != null) {
            mBtnRunInstTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mEditorInstTestMethod == null) {
                        return;
                    }
//                    String instTestMethod = mEditorInstTestMethod.getText().toString();
//                    if ("".equals(instTestMethod)) {
//                        String msg = "Please input the instrument test method!";
//                        Toast.makeText(ActivityRunUiTest.this, msg, Toast.LENGTH_LONG).show();
//                        return;
//                    }

                    if (mEditorExecTime == null) {
                        return;
                    }
//                    String execTime = mEditorExecTime.getText().toString();
//                    if ("".equals(execTime)) {
//                        String msg = "Please input the execution time!";
//                        Toast.makeText(ActivityRunUiTest.this, msg, Toast.LENGTH_LONG).show();
//                    }

                    if (mSelectInst == null) {
                        String msg = "Please select a instrument package!";
                        Toast.makeText(ActivityRunUiTest.this, msg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent intent = new Intent(ActivityRunUiTest.this, ServiceUiTestRunner.class);
//                    Intent intent = new Intent("com.zhengjin.test.intentservice");
                    intent.putExtra("TestPkgName", mSelectInst.packageName);
                    intent.putExtra("TestRunner", mSelectInst.name);
                    ActivityRunUiTest.this.startService(intent);
                }
            });
        }
    }

    private void initViews() {
        mEditorInstTestMethod = (EditText) findViewById(R.id.editor_inst_test_method);
        mEditorExecTime = (EditText) findViewById(R.id.editor_exec_time);
        mListViewInstTests = (ListView) findViewById(R.id.list_inst_tests);
        mBtnRunInstTest = (Button) findViewById(R.id.btn_run_inst_test);
        mTextInstTestFullName = (TextView) findViewById(R.id.inst_test_full_name);
    }

    private void queryInstrumentTests() {
        PackageManager pm = this.getPackageManager();
        mListInstInfo = pm.queryInstrumentation(null, PackageManager.GET_META_DATA);

        if ((mListInstInfo != null) && (mListInstInfo.size() > 0)) {
            for (InstrumentationInfo inst : mListInstInfo) {
                Log.d(TAG, "Instrumentation package name: " + inst.packageName);
                Log.d(TAG, "Instrumentation target package: " + inst.targetPackage);
                Log.d(TAG, "Instrumentation runner: " + inst.name);
            }
        } else {
            Log.i(TAG, "No instrument test cases found on target device!");
        }
    }

    private class ListAdapter extends BaseAdapter {
        private LayoutInflater inflater = null;
        private int mPrePosition = -1;

        public ListAdapter(Context context) {
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
                    mTextInstTestFullName.setText(mSelectInst.packageName);  // TODO: 2016/8/22
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
