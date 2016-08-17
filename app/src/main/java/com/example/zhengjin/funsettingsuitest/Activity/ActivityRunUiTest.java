package com.example.zhengjin.funsettingsuitest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.zhengjin.funsettingsuitest.R;
import com.example.zhengjin.funsettingsuitest.Service.ServiceUiTestRunner;

import java.util.ArrayList;
import java.util.List;

public class ActivityRunUiTest extends AppCompatActivity {

    private final static int INIT_LIST_SIZE = 15;
    private List<String> mListInstrumentTests = new ArrayList<>(INIT_LIST_SIZE);

    private String mInstrumentCommand;

    private ListView mListViewInstruments;
    private TextView mTextViewFullTestName;
    private Button mBtnRunTest;

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
                    Intent intent = new Intent(ActivityRunUiTest.this, ServiceUiTestRunner.class);
                    intent.putExtra("InstCommand", mInstrumentCommand);
                    ActivityRunUiTest.this.startService(intent);
                }
            });
        }
    }

    private void initViews() {

        mListViewInstruments = (ListView) this.findViewById(R.id.listInstrumentTests);
        mTextViewFullTestName = (TextView) this.findViewById(R.id.testFullNameToRun);
        mBtnRunTest = (Button) this.findViewById(R.id.btnRunTest);
    }

    private void initInstrumentTests() {

        mListInstrumentTests.add("com.example.zhengjin.funsettingsuitest.test");
        mListInstrumentTests.add("com.example.android.apis");
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
                    mTextViewFullTestName.setText(mListInstrumentTests.get(position));
                }
            });
            if (mTempPosition == position) {
                if (!holder.selectRadioBtn.isChecked()) {
                    holder.selectRadioBtn.setChecked(true);
                }
            }

            mInstrumentCommand = mListInstrumentTests.get(position);
            holder.instrumentTestName.setText(mListInstrumentTests.get(position));
            return convertView;
        }

        class ViewHolder {
            RadioButton selectRadioBtn;
            TextView instrumentTestName;
        }
    }

}
