package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject2;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryNetworkConfigsTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionBack;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionCenter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
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
import org.junit.Ignore;
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
public final class TestNetworkConfigs extends TestCaseBase {

    private final UiObjectsNetworkConfigs mFunUiObjects = UiObjectsNetworkConfigs.getInstance();
    private final TaskNetworkConfigs mTask = TaskNetworkConfigs.getInstance();

    private final String[] NETWORK_CONFIGS_ITEMS = {"有线网络", "移动热点", "无线网络"};
    private final String[] WIRED_NETWORK_ITEMS = {"IP地址", "子网掩码", "默认网关", "DNS服务器"};
    private final String TEXT_ON = "已开启";
    private final String TEXT_OFF = "已关闭";

    private String mMessage;

    @BeforeClass
    public static void classSetUp() {
        ShellUtils.stopAndClearPackage(SETTINGS_PKG_NAME);
    }

    @Before
    public void setUp() {
        TaskLauncher.backToLauncher();
        mTask.openNetworkConfigPage();
    }

    @After
    public void clearUp() {
        ShellUtils.stopProcessByPackageName(SETTINGS_PKG_NAME);
    }

    @Test
    @Category({CategoryNetworkConfigsTests.class})
    public void test01TitleTextOfNetworkConfigsPage() {
        final String NETWORK_CONFIGS_PAGE_TITLE = "网络设置";

        mMessage = "Verify title text of network configs page.";
        UiObject2 title = mDevice.findObject(mFunUiObjects.getTitleOfNetworkConfigsSelector());
        Assert.assertNotNull(title);
        Assert.assertEquals(mMessage, NETWORK_CONFIGS_PAGE_TITLE, title.getText());
    }

    @Test
    @Category({CategoryNetworkConfigsTests.class})
    public void test02WiredNetworkItemTitleAndStatusConnected() {
        final String WIRED_NETWORK_ITEM_TIPS = "网络连接成功";

        mMessage = "Verify wired network item is default focused.";
        UiObject2 container = mDevice.findObject(
                mFunUiObjects.getWiredNetworkItemContainerSelector());
        Assert.assertTrue(mMessage, (container.isFocused() || container.isSelected()));

        mMessage = "Verify title text of wired network item on network configs page.";
        UiObject2 title = container.findObject(mFunUiObjects.getWiredNetworkItemTitleSelector());
        Assert.assertEquals(mMessage, NETWORK_CONFIGS_ITEMS[0], title.getText());

        mMessage = "Verify tips text of wired network item when connected.";
        UiObject2 tips = container.findObject(mFunUiObjects.getWiredNetworkItemTipsSelector());
        Assert.assertEquals(mMessage, WIRED_NETWORK_ITEM_TIPS, tips.getText());
    }

    @Test
    @Category({CategoryNetworkConfigsTests.class})
    public void test03ManualSettingOffOnWiredNetworkConfigsPage() {
        final String WIRED_NETWORK_CONFIGS_PAGE_TITLE = "有线设置";
        final String MANUAL_SETTING_ITEM_TITLE = "手动设置";

        mAction.doDeviceActionAndWait(new DeviceActionCenter());

        mMessage = "Verify title text of wired network configs page.";
        UiObject2 title = mDevice.findObject(mFunUiObjects.getTitleOfNetworkConfigsSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(title));
        Assert.assertEquals(mMessage, WIRED_NETWORK_CONFIGS_PAGE_TITLE, title.getText());

        mMessage = "Verify title text of manual setting item.";
        UiObject2 manualItem = mDevice.findObject(
                mFunUiObjects.getManualSettingContainerOnWiredNetworkConfigsSelector());
        UiObject2 itemTitle = manualItem.findObject(By.text(MANUAL_SETTING_ITEM_TITLE));
        Assert.assertNotNull(mMessage, itemTitle);

        mMessage = "Verify manual setting item is default off.";
        UiObject2 itemValue = manualItem.findObject(
                mFunUiObjects.getManualSettingSwitchOnWiredNetworkConfigsSelector());
        Assert.assertEquals(mMessage, TEXT_OFF, itemValue.getText());

        mMessage = "Verify save button is enabled.";
        UiObject2 saveBtn = mDevice.findObject(
                mFunUiObjects.getSaveButtonOnWiredNetworkConfigsSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(saveBtn));

        mAction.doDeviceActionAndWait(new DeviceActionBack());
    }

    @Test
    @Category({CategoryNetworkConfigsTests.class})
    public void test04IpAndMaskOnWiredNetworkConfigsPage() {
        mAction.doDeviceActionAndWait(new DeviceActionCenter(), TestConstants.WAIT);

        // ip
        mMessage = "Verify title text of ip item on wired network configs page.";
        UiObject2 ipContainer = mDevice.findObject(
                mFunUiObjects.getIpAddressContainerOnWiredNetworkConfigsSelector());
        UiObject2 uiItemTitle = ipContainer.findObject(
                mFunUiObjects.getItemTitleOnWiredNetworkConfigsSelector());
        Assert.assertEquals(mMessage, WIRED_NETWORK_ITEMS[0], uiItemTitle.getText());

        mMessage = "Verify edit text of ip item on wired network configs page.";
        UiObject2 uiItemValue = ipContainer.findObject(
                mFunUiObjects.getItemEditorOnWiredNetworkConfigsSelector());
        Assert.assertEquals(mMessage, mTask.getIpAddressFromSystemProperties(),
                uiItemValue.getText());

        // mask
        mMessage = "Verify title text of mask item on wired network configs page.";
        UiObject2 maskContainer = mDevice.findObject(
                mFunUiObjects.getSubMaskContainerOnWiredNetworkConfigsSelector());
        uiItemTitle = maskContainer.findObject(
                mFunUiObjects.getItemTitleOnWiredNetworkConfigsSelector());
        Assert.assertEquals(mMessage, WIRED_NETWORK_ITEMS[1], uiItemTitle.getText());

        mMessage = "Verify edit text of mask item on wired network configs page.";
        final String DEFAULT_SUB_MASK = "255.255.255.0";
        uiItemValue = maskContainer.findObject(
                mFunUiObjects.getItemEditorOnWiredNetworkConfigsSelector());
        Assert.assertEquals(mMessage, DEFAULT_SUB_MASK, uiItemValue.getText());

        mAction.doDeviceActionAndWait(new DeviceActionBack());
        Log.d(TAG, TestConstants.LOG_KEYWORD +
                "machine ip: " + mTask.getIpAddressFromSystemProperties());
    }

    @Test
    @Category({CategoryNetworkConfigsTests.class})
    public void test05GatewayAndDnsOnWiredNetworkConfigsPage() {
        mAction.doDeviceActionAndWait(new DeviceActionCenter(), TestConstants.WAIT);

        // gateway
        mMessage = "Verify title text of gateway item on wired network configs page.";
        UiObject2 gatewayContainer = mDevice.findObject(
                mFunUiObjects.getGatewayContainerOnWiredNetworkConfigsSelector());
        UiObject2 uiItemTitle = gatewayContainer.findObject(
                mFunUiObjects.getItemTitleOnWiredNetworkConfigsSelector());
        Assert.assertEquals(mMessage, WIRED_NETWORK_ITEMS[2], uiItemTitle.getText());

        mMessage = "Verify edit text of gateway item on wired network configs page.";
        UiObject2 uiItemValue = gatewayContainer.findObject(
                mFunUiObjects.getItemEditorOnWiredNetworkConfigsSelector());
        Assert.assertEquals(mMessage, mTask.getGatewayIpFromSystemProperties(),
                uiItemValue.getText());

        // dns
        mMessage = "Verify title text of dns item on wired network configs page.";
        UiObject2 dnsContainer = mDevice.findObject(
                mFunUiObjects.getDnsContainerOnWiredNetworkConfigsSelector());
        uiItemTitle = dnsContainer.findObject(
                mFunUiObjects.getItemTitleOnWiredNetworkConfigsSelector());
        Assert.assertEquals(mMessage, WIRED_NETWORK_ITEMS[3], uiItemTitle.getText());

        mMessage = "Verify edit text of gateway item on wired network configs page.";
        uiItemValue = dnsContainer.findObject(
                mFunUiObjects.getItemEditorOnWiredNetworkConfigsSelector());
        Assert.assertEquals(mMessage, mTask.getDnsIpAddressFromSystemConfigs(),
                uiItemValue.getText());

        mAction.doDeviceActionAndWait(new DeviceActionBack());
        Log.d(TAG, TestConstants.LOG_KEYWORD +
                "gateway ip: " + mTask.getGatewayIpFromSystemProperties());
        Log.d(TAG, TestConstants.LOG_KEYWORD +
                "DNS ip: " + mTask.getDnsIpAddressFromSystemConfigs());
    }

    @Test
    @Category({CategoryNetworkConfigsTests.class})
    public void test06WifiHotSpotItemTitleAndStatusOff() {
        mMessage = "Verify title text of wifi hot spot item on network configs page.";
        UiObject2 wifiApContainer =
                mDevice.findObject(mFunUiObjects.getWifiHotSpotItemContainerSelector());
        UiObject2 uiTitle = wifiApContainer.findObject(
                mFunUiObjects.getWifiHotSpotItemTitleSelector());
        Assert.assertEquals(mMessage, NETWORK_CONFIGS_ITEMS[1], uiTitle.getText());

        mMessage = "Verify wifi hot spot item is default off.";
        UiObject2 uiTips = wifiApContainer.findObject(
                mFunUiObjects.getWifiHotSpotItemTipsSelector());
        Assert.assertEquals(mMessage, TEXT_OFF, uiTips.getText());
    }

    @Test
    @Ignore
    @Category({CategoryNetworkConfigsTests.class})
    public void test07TitleAndItemOnWifiHotSpotPageWhenOff() {
        // pre-condition: wifi must be off
        mTask.moveToSpecifiedConfigsItemBySelector(
                mFunUiObjects.getWifiHotSpotItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionCenter());

        mMessage = "Verify the Wifi Hotspot config page is opened.";
        UiObject2 title = mDevice.findObject(mFunUiObjects.getTitleOfNetworkConfigsSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(title));

        mMessage = "Verify the title on Wifi Hotspot config page.";
        final String wifiHotspotConfigTitle = "移动热点设置";
        Assert.assertEquals(mMessage, wifiHotspotConfigTitle, title.getText());

        mMessage = "Verify the title of switch item on Wifi Hotspot config page.";
        final String switchItemTitle = "移动热点";
        UiObject2 switchItemContainer = mDevice.findObject(
                mFunUiObjects.getSwitchContainerOnWifiHotspotConfigPageSelector());
        UiObject2 itemTitle = switchItemContainer.findObject(
                mFunUiObjects.getItemTitleOnGeneralSettingsPageSelector());
        Assert.assertEquals(mMessage, switchItemTitle, itemTitle.getText());

        mMessage = "Verify the text of switch item on Wifi Hotspot config page.";
        UiObject2 itemText = switchItemContainer.findObject(
                mFunUiObjects.getItemTextOnGeneralSettingsPageSelector());
        Assert.assertEquals(mMessage, TEXT_OFF, itemText.getText());
    }

    @Test
    @Category({CategoryNetworkConfigsTests.class})
    public void test11WifiHotSpotsListWhenWifiDefaultOpened() {
        mMessage = "Verify title text of wifi network switch item on network configs page.";
        UiObject2 wifiContainer =
                mDevice.findObject(mFunUiObjects.getWifiNetworkSwitchItemContainerSelector());
        UiObject2 uiTitle =
                wifiContainer.findObject(mFunUiObjects.getWifiNetworkSwitchItemTitleSelector());
        Assert.assertEquals(mMessage, NETWORK_CONFIGS_ITEMS[2], uiTitle.getText());

        mMessage = "Verify wifi network switch is default on.";
        UiObject2 uiText = wifiContainer.findObject(
                mFunUiObjects.getWifiNetworkSwitchItemTextSelector());
        Assert.assertEquals(mMessage, TEXT_ON, uiText.getText());

        mMessage = "Verify the wifi list (wifi network is opened) on network configs page.";
        final int min_wifi_aps_size = 2;
        List<UiObject2> wifiList = mTask.getWifiHotSpotsList();
        Assert.assertTrue(mMessage, wifiList.size() > min_wifi_aps_size);

        Log.d(TAG, TestConstants.LOG_KEYWORD + "wifi list size: " + wifiList.size());
        for (UiObject2 wifi : wifiList) {
            UiObject2 wifiName = wifi.findObject(By.clazz(TestConstants.CLASS_TEXT_VIEW));
            Log.d(TAG, TestConstants.LOG_KEYWORD + "wifi ap name: " + wifiName.getText());
        }
    }

    @Test
    @Category({CategoryNetworkConfigsTests.class})
    public void test12TurnOffAndOnWifiNetwork() {
        mMessage = "Verify wifi list is shown when wifi network switch is on.";
        UiObject2 wifiList =
                mDevice.findObject(mFunUiObjects.getContainerOfWifiHotSpotsListSelector());
        Assert.assertNotNull(mMessage, wifiList);

        // on -> off
        mMessage = "Verify wifi network switch text when set on to off.";
        mTask.moveToSpecifiedConfigsItemBySelector(
                mFunUiObjects.getWifiNetworkSwitchItemContainerSelector());
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), TestConstants.WAIT);

        UiObject2 wifiContainer =
                mDevice.findObject(mFunUiObjects.getWifiNetworkSwitchItemContainerSelector());
        UiObject2 uiText = wifiContainer.findObject(By.text(TEXT_OFF));
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(uiText));

        mMessage = "Verify wifi list is hidden when set wifi network switch on to off.";
        wifiList = mDevice.findObject(mFunUiObjects.getContainerOfWifiHotSpotsListSelector());
        Assert.assertNull(mMessage, wifiList);

        // off -> on
        mMessage = "Verify wifi network switch text when set off to on.";
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), 30 * 1000L);
        uiText = wifiContainer.findObject(By.text(TEXT_ON));
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(uiText));

        mMessage = "Verify wifi list is shown when set wifi network switch off to on.";
        wifiList = mDevice.findObject(mFunUiObjects.getContainerOfWifiHotSpotsListSelector());
        Assert.assertNotNull(mMessage, wifiList);
    }

    @Test
    @Category({CategoryNetworkConfigsTests.class})
    public void test15OpenAndConfigItemsOfQuickConnectView() {
        final String TEXT_QUICK_CONNECT = "快速连接";

        mMessage = "Verify wifi network quick connect button is enabled.";
        UiObject2 quickConnectBtn =
                mDevice.findObject(mFunUiObjects.getQuickConnectButtonOnNetworkConfigsSelector());
        Assert.assertTrue(mMessage, quickConnectBtn.isClickable());
        Assert.assertEquals(mMessage, TEXT_QUICK_CONNECT, quickConnectBtn.getText());

        mMessage = "Verify page title text of quick connect view.";
        mTask.openQuickConnectViewFromNetworkConfigs();
        UiObject2 uiTitle = mDevice.findObject(mFunUiObjects.getTitleOfNetworkConfigsSelector());
        Assert.assertEquals(mMessage, TEXT_QUICK_CONNECT, uiTitle.getText());

        mMessage = "Verify config items (%s) are enabled on quick connect view.";
        UiObject2 uiItemTitle;
        final String[] QUICK_CONNECT_CONFIG_ITEMS = {"添加网络", "WPS连接", "手机扫码连接"};
        for (String itemText : QUICK_CONNECT_CONFIG_ITEMS) {
            uiItemTitle = mDevice.findObject(By.text(itemText));
            Assert.assertNotNull(String.format(mMessage, itemText), uiItemTitle);
        }
    }

    @Test
    @Category(CategoryNetworkConfigsTests.class)
    public void test99ClearUpAfterAllTestCasesDone() {
        TaskNetworkConfigs.destroyInstance();
        UiObjectsNetworkConfigs.destroyInstance();
    }

}
