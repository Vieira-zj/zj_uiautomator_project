package com.example.zhengjin.funsettingsuitest.testcasedemos;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;
import com.example.zhengjin.funsettingsuitest.Utils.ShellUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Locale;

/**
 * Created by zhengjin on 2016/6/1.
 *
 * Include test cases for ShellUtils.java
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestShellUtils {

    private final static String TAG = TestShellUtils.class.getSimpleName();

    @Before
    public void setUp() {
        Log.d(TAG, String.format("***** Test %s start.", TAG));
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void testExecShellShCommand() {
        String command = "cat /system/build.prop | grep ro.product.model";
        ShellUtils.CommandResult cr = ShellUtils.execCommand(command, false, true);
        String output = String.format(Locale.getDefault(),
                "Result code: %d\n Success message: %s\n Error message: %s",
                cr.mResult, cr.mSuccessMsg, cr.mErrorMsg);
        Log.d(TAG, output);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void testExecShellAmStartCommand() {
        // add extra option "--user 0"
        String command = "am start --user 0 tv.fun.filemanager/.FunFileManagerActivity";
        ShellUtils.CommandResult cr = ShellUtils.execCommand(command, false, true);
        String output = String.format(Locale.getDefault(),
                "Result code: %d\n Success message: %s\n Error message: %s",
                cr.mResult, cr.mSuccessMsg, cr.mErrorMsg);
        Log.d(TAG, output);
    }

    @After
    public void clearUp() {
        Log.d(TAG, String.format("***** Test %s finished.", TAG));
    }

}
