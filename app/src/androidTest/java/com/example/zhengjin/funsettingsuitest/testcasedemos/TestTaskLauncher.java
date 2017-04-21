package com.example.zhengjin.funsettingsuitest.testcasedemos;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuiobjects.UiObjectsLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/6/1.
 * <p>
 * Include test cases for TaskLauncher.java
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestTaskLauncher {

    private final static String TAG = TestTaskLauncher.class.getSimpleName();
    private UiDevice mDevice;
    private UiActionsManager mAction;
    private UiObjectsLauncher mFunUiObjects;

    public TestTaskLauncher() {
        Log.d(TAG, String.format("***** Test class (%s) init.", TAG));
    }

    @Before
    public void setUp() {
        Log.d(TAG, String.format("***** Test %s start.", TAG));
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mFunUiObjects = UiObjectsLauncher.getInstance();
    }

    @After
    public void clearUp() {
        ShellUtils.systemWaitByMillis(WAIT);
        Log.d(TAG, String.format("***** Test %s finished.", TAG));
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test01GetLauncherPackageName() {
        String pkg = TaskLauncher.getLauncherPackageName();
        Assert.assertNotNull(pkg);
        Assert.assertEquals(TestConstants.LAUNCHER_PKG_NAME, pkg);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test02BackToLauncher() {
        TaskLauncher.backToLauncher();
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test03NavigateToVideoTab() {
        TaskLauncher.navigateToVideoTab();
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test04NavigateToAppTab() {
        TaskLauncher.navigateToSpecifiedTopTab(TaskLauncher.LAUNCHER_HOME_TABS[4]);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test05OpenSpecifiedApp() {
        String appName = "唯品会";
        TaskLauncher.openSpecifiedAppFromAppTab(appName);
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test06OpenSettingsFromTopBar() {
        TaskLauncher.clickOnButtonFromTopQuickAccessBar(
                mFunUiObjects.getQuickAccessBtnSettingsSelector());
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test07SelectSpecifiedTabOnLauncher() {
        // test using multiple search conditions in findObject()
        TaskLauncher.backToLauncher();
        mAction.doDeviceActionAndWait(new DeviceActionMoveUp());

        UiObject2 scrollView = mDevice.findObject(By.clazz("android.widget.HorizontalScrollView")
                .res("com.bestv.ott:id/indicator"));
        UiObject2 selectedTab;
        UiObject2 tabText;
        for (int idx = 1, max = 10; idx < max; idx++) {
            selectedTab = scrollView.findObject(By.clazz(
                    "android.widget.RelativeLayout").selected(true));
            tabText = selectedTab.findObject(By.clazz("android.widget.TextView"));
            if (tabText.getText().equals("应用")) {
                break;
            }
            mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        }
    }

}
