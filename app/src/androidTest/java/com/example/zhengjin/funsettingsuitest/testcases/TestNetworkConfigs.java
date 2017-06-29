package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryNetworkConfigsTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuiobjects.UiObjectsNetworkConfigs;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskNetworkConfigs;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;

/**
 * Created by zhengjin on 2017/6/29.
 * <p>
 * Include test cases for network settings.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestNetworkConfigs {

    private final static String TAG = TestNetworkConfigs.class.getSimpleName();

    private UiDevice mDevice;
    private UiActionsManager mAction;
    private UiObjectsNetworkConfigs mFunUiObjects;
    private TaskNetworkConfigs mTask;
    private String mMessage;

    @BeforeClass
    public static void classSetUp() {
        ShellUtils.stopAndClearPackage(SETTINGS_PKG_NAME);
    }

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mFunUiObjects = UiObjectsNetworkConfigs.getInstance();
        mTask = TaskNetworkConfigs.getInstance();

        TaskLauncher.backToLauncher();
        TaskLauncher.openNetworkConfigFromLauncherQuickAccessBar();
    }

    @After
    public void clearUp() {
        ShellUtils.stopProcessByPackageName(SETTINGS_PKG_NAME);
    }

    @Test
    @Category({CategoryNetworkConfigsTests.class})
    public void test01TitleTextOfNetworkConfigsPage() {
        mMessage = "Verify the title text of network configs page.";
        final String NETWORK_TITLE = "网络设置";
        UiObject2 title = mDevice.findObject(mFunUiObjects.getTitleOfNetworkConfigsSelector());
        Assert.assertNotNull(title);
        Assert.assertEquals(mMessage, NETWORK_TITLE, title.getText());
    }

    @Test
    @Category({CategoryNetworkConfigsTests.class})
    public void test11WifiHotSpotsListWhenWifiDefaultOpened() {
        mMessage = "Verify the wifi list when open network configs and wifi is default opened.";
        List<UiObject2> wifiList = mTask.getWifiHotSpotsList();
        Assert.assertTrue(mMessage, wifiList.size() > 0);

        Log.d(TAG, TestConstants.LOG_KEYWORD + "wifi list size: " + wifiList.size());
        for (UiObject2 wifi : wifiList) {
            UiObject2 wifiName = wifi.findObject(By.clazz(TestConstants.CLASS_TEXT_VIEW));
            Log.d(TAG, TestConstants.LOG_KEYWORD + "wifi name: " + wifiName.getText());
        }
    }

    @Test
    @Category(CategoryNetworkConfigsTests.class)
    public void test99ClearUpAfterAllTestCasesDone() {
        TaskNetworkConfigs.destroyInstance();
        UiObjectsNetworkConfigs.destroyInstance();
    }

}
