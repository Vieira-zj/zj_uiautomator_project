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
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.FILE_MANAGER_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.TIME_OUT;

/**
 * Created by zhengjin on 2016/6/24.
 *
 * It's a demo 1) use the wait(until) APIs for test case stability,
 * and 2) use the "context" to start the specified activity.
 */
@RunWith(AndroidJUnit4.class)
public final class TestLauncherApp {

    private TaskFileManager mFileManagerTask;
    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mFileManagerTask = TaskFileManager.getInstance();

        // go to home
        mDevice.pressHome();
        String launcherPkg = mDevice.getLauncherPackageName();
        Assert.assertNotNull(launcherPkg);
        boolean ret = mDevice.wait(Until.hasObject(By.pkg(launcherPkg).depth(0)), LONG_WAIT);
        if (!ret) {
            throw new RuntimeException("Error, back to home failed.");
        }

        // start app
        Context context = InstrumentationRegistry.getContext();
        Intent intent =
                context.getPackageManager().getLaunchIntentForPackage(FILE_MANAGER_PKG_NAME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME)), TIME_OUT);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test1OpenAllFilesCard() {
        mFileManagerTask.openLocalFilesCard(mDevice);
        UiObject2 allFilesTitle = mDevice.findObject(By.text("全部文件"));
        Assert.assertNotNull(allFilesTitle);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test2TakeScreenCaptures() {
        TaskLauncher.backToLauncher(mDevice);
        ShellUtils.takeScreenCapture(mDevice);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test3LongPressKey() {
        // TODO: 2016/8/10 long press action
    }

}
