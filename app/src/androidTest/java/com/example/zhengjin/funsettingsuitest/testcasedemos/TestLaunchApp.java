package com.example.zhengjin.funsettingsuitest.testcasedemos;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskFileManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

/**
 * Created by zhengjin on 2016/6/24.
 *
 * It's a demo 1) use the wait(until) APIs for test case stability,
 * and 2) use the "context" to start the specified activity.
 */
@RunWith(AndroidJUnit4.class)
public final class TestLaunchApp {

    private static final String PACKAGE_NAME = "tv.fun.filemanager";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() {

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // go to home
        mDevice.pressHome();
        String launcherPkg = mDevice.getLauncherPackageName();
        Assert.assertNotNull(launcherPkg);
        boolean ret = mDevice.wait(Until.hasObject(By.pkg(launcherPkg).depth(0)), LAUNCH_TIMEOUT);
        if (!ret) {
            throw new RuntimeException("Error, back to home failed.");
        }

        // start app
        Context context = InstrumentationRegistry.getContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(PACKAGE_NAME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME)), LAUNCH_TIMEOUT);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test1OpenAllFilesCard() {

        TaskFileManager.openSdcardLocalFilesCard(mDevice);
        UiObject2 allFilesTitle = mDevice.findObject(By.text("全部文件"));
        Assert.assertNotNull(allFilesTitle);
    }


}
