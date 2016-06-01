package com.example.zhengjin.funsettingsuitest.testcasedemos;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcategory.DemoTests;
import com.example.zhengjin.funsettingsuitest.testtasks.TaskLauncher;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * Created by zhengjin on 2016/6/1.
 *
 * Test cases for TaskLauncher.java
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestTaskLauncher {

    private final String TAG = this.getClass().getSimpleName();
    private UiDevice mDevice;


    @Before
    public void setUp() {
        Log.d(TAG, String.format("***** Test %s start.", TAG));
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

//    @Test
//    @Category(DemoTests.class)
//    public void test1GetLauncherPackageName() {
//        final String results = "com.bestv.ott";
//        String pkg = TaskLauncher.getLauncherPackageName();
//
//        Assert.assertNotNull(pkg);
//        Assert.assertEquals(results, pkg);
//    }

    @Ignore
    @Category(DemoTests.class)
    public void test2BackToLauncher() {
        boolean results = TaskLauncher.backToLauncher(mDevice);
        Assert.assertTrue(results);
    }

    @Ignore
    @Category(DemoTests.class)
    public void test3NavigateToVideoTab() {
        boolean results = TaskLauncher.navigateToVideoTab(mDevice);
        Assert.assertTrue(results);
    }

    @Ignore
    @Category(DemoTests.class)
    public void test4NavigateToAppTab() {
        boolean results = TaskLauncher.navigateToAppTab(mDevice);
        Assert.assertTrue(results);
    }

    @Test
    @Category(DemoTests.class)
    public void test5OpenSpecifiedApp() {
        String appName = "沙发管家";
        boolean results = TaskLauncher.openSpecifiedApp(mDevice, appName);
        Assert.assertTrue(results);
    }

    @After
    public void clearUp() {
        Log.d(TAG, String.format("***** Test %s finished.", TAG));
    }

}
