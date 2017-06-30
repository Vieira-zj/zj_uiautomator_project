package com.example.zhengjin.funsettingsuitest.testcasedemos;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Configurator;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.text.TextUtils;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskAboutInfo;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskFileManager;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.List;

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
@SdkSuppress(minSdkVersion = 18)
public final class TestCommonUiTasks {

    private static final String TAG = TestCommonUiTasks.class.getSimpleName();

    private UiDevice mDevice;
    private TaskFileManager mFileManagerTask;
    private TaskAboutInfo mAboutInfoTask;

    @Before
    public void setUp() {
        mDevice = TestConstants.GetUiDeviceInstance();
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

    @Test
    @Category(CategoryDemoTests.class)
    public void test03SetConfigurator() {
        Configurator configurator = Configurator.getInstance();

        long defaultKeyInjectDelay = configurator.getKeyInjectionDelay();  // 0
        Log.d(TAG, TestConstants.LOG_KEYWORD +
                "default key inject delay: " + defaultKeyInjectDelay);

        UiObject2 editor = mDevice.findObject(By.res("tv.fun.settings:id/device_edit"));
        Log.d(TAG, TestConstants.LOG_KEYWORD + "editor class: " + editor.getClassName());
        if (!editor.isFocused()) {
            Assert.fail("Editor is not focused!");
        }
        if (TextUtils.isEmpty(editor.getText())) {
            Assert.fail("Text in editor is empty!");
        }

        // setText: 1. text in editor cannot be empty; 2. clear and set text
        editor.setText("before config");
        configurator.setKeyInjectionDelay(500L);
        editor.setText("after config");

        Assert.assertTrue(true);
    }

    @Test
    @Category(CategoryDemoTests.class)
    @SuppressWarnings("deprecation")
    public void test04ClickAndWaitForNewWindow() throws UiObjectNotFoundException {
        mDevice.pressMenu();
        ShellUtils.systemWaitByMillis(TestConstants.SHORT_WAIT);
        UiObject settingBtn = new UiObject(new UiSelector().text("设置"));
        settingBtn.clickAndWaitForNewWindow(TestConstants.WAIT);

        Assert.assertTrue(TestHelper.waitForActivityOpenedByShellCmd(
                TestConstants.LAUNCHER_PKG_NAME, ".home.SettingActivity"));
    }

    @Test
    @Category(CategoryDemoTests.class)
    @SuppressWarnings("deprecation")
    public void test05ClickAndWaitForNewWindow() throws UiObjectNotFoundException {
        UiObject tab = mDevice.findObject(new UiSelector().text("支付代扣"));
        tab.click();  // get focus
        ShellUtils.systemWaitByMillis(TestConstants.SHORT_WAIT);
        tab.clickAndWaitForNewWindow(TestConstants.WAIT);

        Assert.assertTrue(TestHelper.waitForActivityOpenedByShellCmd(
                TestConstants.LAUNCHER_PKG_NAME, ".account.unBindWithHoldActivity"));
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test06InitUiObjectsBefore() {
        ShellUtils.startSpecifiedActivity("tv.fun.instructions", ".CoverActivity");
        ShellUtils.systemWaitByMillis(TestConstants.WAIT);

        // ui objects must be init after page is shown
        UiObject2 container = mDevice.findObject(
                By.res("tv.fun.instructions:id/indicator")
                        .clazz("android.widget.HorizontalScrollView"));
        List<UiObject2> tabs =
                container.findObjects(By.depth(2).clazz("android.widget.LinearLayout"));

        Assert.assertEquals("Verify count of tabs is 8.", 8, tabs.size());

        for (UiObject2 tab : tabs) {
            Log.d(TAG, TestConstants.LOG_KEYWORD + "tab name: " +
                    tab.findObject(By.clazz("android.widget.TextView")).getText());
        }
    }

    @Test
    @Ignore
    @Category(CategoryDemoTests.class)
    public void test11LongPressKey() {
        // TODO: 2016/8/10 long press action
    }

    /**
     * It's a demo 1) use the wait(until) APIs for test case stability,
     * and 2) use the "context" to start the specified APP.
     */
    private void startFmMainActivityFromHomeScreen() {
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
