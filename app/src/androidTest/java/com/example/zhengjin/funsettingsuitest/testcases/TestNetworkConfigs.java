package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryNetworkConfigsTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionCenter;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuiobjects.UiObjectsNetworkConfigs;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskNetworkConfigs;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

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
        final String NETWORK_CONFIGS_PAGE_TITLE = "网络设置";

        mMessage = "Verify the title text of network configs page.";
        UiObject2 title = mDevice.findObject(mFunUiObjects.getTitleOfNetworkConfigsSelector());
        Assert.assertNotNull(title);
        Assert.assertEquals(mMessage, NETWORK_CONFIGS_PAGE_TITLE, title.getText());
    }

    @Test
    @Category({CategoryNetworkConfigsTests.class})
    public void test02WiredNetworkItemTitleAndStatusConnected() {
        final String WIRED_NETWORK_ITEM_TITLE = "有线网络";
        final String WIRED_NETWORK_ITEM_TIPS = "网络连接成功";

        mMessage = "Verify wired network item is default focused.";
        UiObject2 container = mDevice.findObject(
                mFunUiObjects.getWiredNetworkItemContainerSelector());
        Assert.assertTrue(mMessage, (container.isFocused() || container.isSelected()));

        mMessage = "Verify the title text of wired network item.";
        UiObject2 title = container.findObject(mFunUiObjects.getWiredNetworkItemTitleSelector());
        Assert.assertEquals(mMessage, WIRED_NETWORK_ITEM_TITLE, title.getText());

        mMessage = "Verify the tips text of wired network item when connected.";
        UiObject2 tips = container.findObject(mFunUiObjects.getWiredNetworkItemTipsSelector());
        Assert.assertEquals(mMessage, WIRED_NETWORK_ITEM_TIPS, tips.getText());
    }

    @Test
    @Category({CategoryNetworkConfigsTests.class})
    public void test03ManualSettingOffOnWiredNetworkConfigsPage() {
        final String WIRED_NETWORK_CONFIGS_PAGE_TITLE = "有线设置";
        final String MANUAL_SETTING_ITEM_TITLE = "手动设置";
        final String TEXT_OFF = "已关闭";

        mAction.doDeviceActionAndWait(new DeviceActionCenter());

        mMessage = "Verify the title text of wired network configs page.";
        UiObject2 title = mDevice.findObject(mFunUiObjects.getTitleOfNetworkConfigsSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(title));
        Assert.assertEquals(mMessage, WIRED_NETWORK_CONFIGS_PAGE_TITLE, title.getText());

        mMessage = "Verify the title text of manual setting item.";
        UiObject2 manualItem = mDevice.findObject(
                mFunUiObjects.getManualSettingContainerOnWireNetworkConfigsSelector());
        UiObject2 itemTitle = manualItem.findObject(By.text(MANUAL_SETTING_ITEM_TITLE));
        Assert.assertNotNull(mMessage, itemTitle);

        mMessage = "Verify manual setting item is default off.";
        UiObject2 itemValue = manualItem.findObject(
                mFunUiObjects.getManualSettingSwitchOnWireNetworkConfigsSelector());
        Assert.assertEquals(mMessage, TEXT_OFF, itemValue.getText());
    }

    @Test
    @Category({CategoryNetworkConfigsTests.class})
    public void test04InfoOnWiredNetworkConfigsPage() {
        String[] WIRED_NETWORK_ITEMS = {"IP地址", "子网掩码", "默认网关", "DNS服务器"};

        mAction.doDeviceActionAndWait(new DeviceActionCenter(), TestConstants.WAIT);

        mMessage = "Verify title text of ip item on wired network configs page.";
        UiObject2 ipContainer = mDevice.findObject(
                mFunUiObjects.getIpAddressContainerOnWireNetworkConfigsSelector());
        UiObject2 itemTitle = ipContainer.findObject(
                mFunUiObjects.getItemTitleOnWireNetworkConfigsSelector());
        Assert.assertEquals(mMessage, WIRED_NETWORK_ITEMS[0], itemTitle.getText());

        mMessage = "Verify edit text of ip item on wired network configs page.";
        UiObject2 itemValue =
                ipContainer.findObject(mFunUiObjects.getItemEditorOnWireNetworkConfigsSelector());
        Assert.assertEquals(
                mMessage, mTask.getIpAddressFromSystemProperties(), itemValue.getText());

        // TODO: 2017/6/29  

        Log.d(TAG, TestConstants.LOG_KEYWORD +
                "machine ip: " + mTask.getIpAddressFromSystemProperties());
        Log.d(TAG, TestConstants.LOG_KEYWORD +
                "gateway ip: " + mTask.getGatewayIpFromSystemProperties());
        Log.d(TAG, TestConstants.LOG_KEYWORD +
                "DNS ip: " + mTask.getDnsIpAddressFromSystemConfigs());
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
