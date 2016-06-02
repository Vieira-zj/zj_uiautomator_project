package com.example.zhengjin.funsettingsuitest.testcasedemos;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcategory.DemoTests;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * Created by zhengjin on 2016/6/1.
 *
 * Test cases for ShellUtils.java
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
    @Category(DemoTests.class)
    public void test1ExecShellCommandWithOutput() {

        String input = "ls";
        String actResults = ShellUtils.execShellCommandWithOutput(input);
        Log.d(TAG, String.format("***** Shell output: \n %s", actResults));
        Assert.assertNotNull(actResults);
    }

    @Test
    @Category(DemoTests.class)
    public void test2ExecShellCommand() {

        String input = "ls";
        boolean actResults = ShellUtils.execShellCommand(input);
        Assert.assertTrue(actResults);
    }



    @After
    public void clearUp() {
        Log.d(TAG, String.format("***** Test %s finished.", TAG));
    }

}
