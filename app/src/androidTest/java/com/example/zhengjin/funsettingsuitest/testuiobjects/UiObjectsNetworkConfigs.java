package com.example.zhengjin.funsettingsuitest.testuiobjects;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;

import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;

/**
 * Created by zhengjin on 2017/6/29.
 * <p>
 * Include UI objects for network configuration page.
 */
public final class UiObjectsNetworkConfigs {

    private static UiObjectsNetworkConfigs ourInstance;

    public static UiObjectsNetworkConfigs getInstance() {
        if (ourInstance == null) {
            synchronized (UiObjectsNetworkConfigs.class) {
                if (ourInstance == null) {
                    ourInstance = new UiObjectsNetworkConfigs();
                }
            }
        }
        return ourInstance;
    }

    private UiObjectsNetworkConfigs() {
    }

    public static synchronized void destroyInstance() {
        if (ourInstance != null) {
            ourInstance = null;
        }
    }

    public BySelector getTitleOfNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/setting_title");
    }

    public BySelector getWiredNetworkItemContainerSelector() {
        return By.res("tv.fun.settings:id/ethernet_state_layer");
    }

    public BySelector getWiredNetworkItemTitleSelector() {
        return By.res("tv.fun.settings:id/ethernet_state_title");
    }

    public BySelector getWiredNetworkItemTipsSelector() {
        return By.res("tv.fun.settings:id/ethernet_state_tips");
    }

    public BySelector getManualSettingContainerOnWireNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/activity_network_type");
    }

    public BySelector getManualSettingSwitchOnWireNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/activity_network_switch_text");
    }

    public BySelector getIpAddressContainerOnWireNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/activity_ethernet_ip");
    }

    public BySelector getSubMaskContainerOnWireNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/activity_ethernet_mask");
    }

    public BySelector getGatewayContainerOnWireNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/activity_ethernet_gate");
    }

    public BySelector getNdsContainerOnWireNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/activity_ethernet_dns");
    }

    public BySelector getItemTitleOnWireNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/ethernet_item_title");
    }

    public BySelector getItemEditorOnWireNetworkConfigsSelector() {
        return By.clazz(TestConstants.CLASS_EDIT_VIEW);
    }

    public BySelector getContainerOfWifiHotSpotsListSelector() {
        return By.res("tv.fun.settings:id/wifi_list");
    }

}
