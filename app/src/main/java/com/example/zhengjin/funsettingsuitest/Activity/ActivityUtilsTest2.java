package com.example.zhengjin.funsettingsuitest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhengjin.funsettingsuitest.R;
import com.example.zhengjin.funsettingsuitest.utils.DeviceUtils;
import com.example.zhengjin.funsettingsuitest.utils.PackageUtils;

import java.util.Locale;

public final class ActivityUtilsTest2 extends AppCompatActivity {

//    private static final String TAG = ActivityUtilsTest2.class.getSimpleName();

    private final Locale mLocale = Locale.getDefault();

    private Button mBtnProcessUtilsTest = null;
    private TextView mTextProcessUtilsTest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utils_test2);
        this.initViews();

        if (mBtnProcessUtilsTest != null) {
            mBtnProcessUtilsTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pkgName = "com.bestv.ott";
                    StringBuilder sb = new StringBuilder(5);
                    sb.append(getTopActivityName());
                    sb.append(isAppRunning(pkgName));
                    sb.append(getPackageMemorySize(pkgName));
                    sb.append(getTrafficDataByPackageName(pkgName));

                    mTextProcessUtilsTest.setText(sb.toString());
                }
            });
        }
    }

    private void initViews() {
        mBtnProcessUtilsTest = (Button) this.findViewById(R.id.btn_process_utils_test);
        mTextProcessUtilsTest = (TextView) this.findViewById(R.id.text_process_utils_test);
    }

    private String isAppRunning(String pkgName) {
        int pid = PackageUtils.getPidByPackageName(pkgName);
        if (pid != -1) {
            return String.format(mLocale, "Package %s is running, and pid is %d\n", pkgName, pid);
        }
        return String.format("Package %s is not running.\n", pkgName);
    }

    private String getTopActivityName() {
        String topActivity = PackageUtils.getTopActivity();
        return String.format("The top activity: %s\n", (topActivity  == null ? "null" : topActivity));
    }

    private String getPackageMemorySize(String pkgName) {
        int pid = PackageUtils.getPidByPackageName(pkgName);
        if (pid == -1) {
            return String.format("Package %s is not running.\n", pkgName);
        }

        int memSize = DeviceUtils.getMemorySizeByPid(pid);
        return String.format(mLocale,
                "The memory size for package [%s] is: %d KB\n", pkgName, memSize);
    }

    private String getTrafficDataByPackageName(String pkgName) {
        int uid = PackageUtils.getUidByPackageName(pkgName);
        long pkgTraffic = DeviceUtils.getTrafficInfoByUid(uid);
        pkgTraffic = pkgTraffic / 1024L;  // Byte to KB
        if (pkgTraffic <= 0L) {
            return String.format("There is no traffic data for package [%s]!\n", pkgName);
        }

        return String.format(mLocale,
                "The total traffic data for package [%s] is: %d KB\n", pkgName, pkgTraffic);
    }

}
