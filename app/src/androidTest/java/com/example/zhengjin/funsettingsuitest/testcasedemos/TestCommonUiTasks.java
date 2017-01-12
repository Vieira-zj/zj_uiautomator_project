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
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskAboutInfo;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskFileManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.FILE_MANAGER_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.TIME_OUT;

/**
 * Created by zhengjin on 2016/6/24.
 * <p>
 * Test the functions in test cases task.
 */

@RunWith(AndroidJUnit4.class)
public final class TestCommonUiTasks {

    private UiDevice mDevice;
    private TaskFileManager mFileManagerTask;
    private TaskAboutInfo mAboutInfoTask;

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mFileManagerTask = TaskFileManager.getInstance();
        mAboutInfoTask = TaskAboutInfo.getInstance();
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test01OpenAllFilesCard() {
        this.startFmMainActivityFromHomeScreen();
        mFileManagerTask.openLocalFilesCard();
        UiObject2 allFilesTitle = mDevice.findObject(By.text("全部文件"));
        Assert.assertNotNull(allFilesTitle);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test02GetNetworkInfoFor638() {
        TaskAboutInfo.NetworkInfo infoWired =
                mAboutInfoTask.getSysNetworkInfo(TaskAboutInfo.NetworkType.Wired);
        Assert.assertNotNull(infoWired);
        Assert.assertEquals("172.17.5.106", infoWired.getIpAddr());
        Assert.assertEquals("28:76:cd:01:96:f6", infoWired.getMacId());

        TaskAboutInfo.NetworkInfo info =
                mAboutInfoTask.getSysNetworkInfo(TaskAboutInfo.NetworkType.Wireless);
        Assert.assertNotNull(info);
        Assert.assertEquals("0.0.0.0", info.getIpAddr());
        Assert.assertEquals("34:c3:d2:0f:1d:03", info.getMacId());
    }

    @Ignore
    @Category(CategoryDemoTests.class)
    public void test11LongPressKey() {
        // TODO: 2016/8/10 long press action
    }

    private void startFmMainActivityFromHomeScreen() {
        /** It's a demo 1) use the wait(until) APIs for test case stability,
         * and 2) use the "context" to start the specified activity.
         */
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

}
