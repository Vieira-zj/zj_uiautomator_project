package com.example.zhengjin.funsettingsuitest.Activity;

import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhengjin.funsettingsuitest.R;
import com.example.zhengjin.funsettingsuitest.Service.ServiceUiTestRunner;

import java.util.ArrayList;
import java.util.List;

public class ActivityRunUiTest extends AppCompatActivity {

    private static final String TAG = ActivityRunUiTest.class.getSimpleName();
    private static final int INIT_LIST_SIZE = 15;
    private List<InstrumentationInfo> mListInstrumentTests = new ArrayList<>(INIT_LIST_SIZE);

    private InstrumentationInfo mSelectInst;

    private ListView mListViewInstruments;
    private TextView mTextViewFullTestName;
    private Button mBtnRunTest;
    private EditText mEditTestMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_ui_test);
        this.initViews();

        mListViewInstruments.setAdapter(new ListAdapter());

        if (mBtnRunTest != null) {
            mBtnRunTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mEditTestMethod == null) {
                        return;
                    }
                    String testMethod = mEditTestMethod.getText().toString();
                    if ((testMethod == null) || ("".equals(testMethod))) {
                        Toast.makeText(ActivityRunUiTest.this,
                                "Please set the testing class and method", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (mSelectInst == null) {
                        Toast.makeText(ActivityRunUiTest.this, "Please select a instrumentation!",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent(ActivityRunUiTest.this, ServiceUiTestRunner.class);
//                    Intent intent = new Intent("com.zhengjin.test.intentservice");
                    intent.putExtra("pkgName", mSelectInst.packageName);
                    intent.putExtra("testRunner", mSelectInst.name);
                    ActivityRunUiTest.this.startService(intent);
                }
            });
        }
    }

    private void initViews() {

        mListViewInstruments = (ListView) this.findViewById(R.id.listInstrumentTests);
        mTextViewFullTestName = (TextView) this.findViewById(R.id.testFullNameToRun);
        mEditTestMethod = (EditText) this.findViewById(R.id.setRunTestMethod);
        mBtnRunTest = (Button) this.findViewById(R.id.btnRunInstrumentTest);
    }

    private void initInstrumentTests() {

        PackageManager pm = this.getPackageManager();
        List<InstrumentationInfo> listInsts =
                pm.queryInstrumentation(null, PackageManager.GET_META_DATA);
        if (listInsts != null) {
            for (InstrumentationInfo inst : listInsts) {
                Log.d(TAG, "Instrumentation package name: " + inst.packageName);
                Log.d(TAG, "Instrumentation target package: " + inst.targetPackage);
                Log.d(TAG, "Instrumentation runner: " + inst.name);

//                if (inst.targetPackage.toLowerCase().contains("android")) {
//                    continue;
//                }
                mListInstrumentTests.add(inst);
            }
        }
    }

    private class ListAdapter extends BaseAdapter {

        int mTempPosition = -1;

        public ListAdapter() {
            ActivityRunUiTest.this.initInstrumentTests();
        }

        @Override
        public int getCount() {
            return mListInstrumentTests.size();
        }

        @Override
        public Object getItem(int position) {
            return mListInstrumentTests.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = ActivityRunUiTest.this.getLayoutInflater().inflate(R.layout.list_item, null);

                holder = new ViewHolder();
                holder.selectRadioBtn = (RadioButton) convertView.findViewById(R.id.selectRadioBtn);
                holder.instrumentTestName = (TextView) convertView.findViewById(R.id.instrumentTestName);
                holder.selectRadioBtn.setId(position);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.selectRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (mTempPosition != -1) {
                            RadioButton tempBtn = (RadioButton) findViewById(mTempPosition);
                            if ((tempBtn != null) && (mTempPosition != -1)) {
                                tempBtn.setChecked(false);
                            }
                        }
                    }
                    mTempPosition = buttonView.getId();
                    mSelectInst = mListInstrumentTests.get(position);
                    mTextViewFullTestName.setText(mSelectInst.packageName);
                }
            });
            if (mTempPosition == position) {
                if (!holder.selectRadioBtn.isChecked()) {
                    holder.selectRadioBtn.setChecked(true);
                }
            }

            holder.instrumentTestName.setText(mListInstrumentTests.get(position).packageName);
            return convertView;
        }

        class ViewHolder {
            RadioButton selectRadioBtn;
            TextView instrumentTestName;
        }
    }

}
