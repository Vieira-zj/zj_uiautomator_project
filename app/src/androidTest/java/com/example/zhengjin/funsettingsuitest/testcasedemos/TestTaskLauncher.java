package com.example.zhengjin.funsettingsuitest.testcasedemos;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcategory.DemoTests;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;

/**
 * Created by zhengjin on 2016/6/1.
 *
 * Include test cases for TaskLauncher.java
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestTaskLauncher {

    private final static String TAG = TestTaskLauncher.class.getSimpleName();
    private UiDevice mDevice;

    @Before
    public void setUp() {
        Log.d(TAG, String.format("***** Test %s start.", TAG));
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @After
    public void clearUp() {
        ShellUtils.systemWait(LONG_WAIT);
        Log.d(TAG, String.format("***** Test %s finished.", TAG));
    }

    @Test
    @Category(DemoTests.class)
    public void test1GetLauncherPackageName() {
        final String results = "com.bestv.ott";
        String pkg = TaskLauncher.getLauncherPackageName();

        Assert.assertNotNull(pkg);
        Assert.assertEquals(results, pkg);
    }

    @Test
    @Category(DemoTests.class)
    public void test2BackToLauncher() {
        TaskLauncher.backToLauncher(mDevice);
    }

    @Test
    @Category(DemoTests.class)
    public void test3NavigateToVideoTab() {
        TaskLauncher.navigateToVideoTab(mDevice);
    }

    @Test
    @Category(DemoTests.class)
    public void test4NavigateToAppTab() {
        TaskLauncher.navigateToAppTab(mDevice);
    }

    @Test
    @Category(DemoTests.class)
    public void test5OpenSpecifiedApp() {
        String appName = "沙发管家";
        TaskLauncher.openSpecifiedApp(mDevice, appName);
        ShellUtils.systemWait(LONG_WAIT);
    }

    @Test
    @Category(DemoTests.class)
    public void test6OpenSettingsFromTopBar() {
        String settingsId = "com.bestv.ott:id/setting";
//        String networkSettingId = "com.bestv.ott:id/network";
        TaskLauncher.openQuickAccessButtonFromTopBar(mDevice, settingsId);
    }

}
