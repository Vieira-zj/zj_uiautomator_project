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

    public BySelector getWifiHotSpotItemContainerSelector() {
        return By.res("tv.fun.settings:id/wifi_ap_layer");
    }

    public BySelector getWifiHotSpotItemTitleSelector() {
        return By.res("tv.fun.settings:id/wifi_ap_state_title");
    }

    public BySelector getWifiHotSpotItemTipsSelector() {
        return By.res("tv.fun.settings:id/wifi_ap_state_tips");
    }

    public BySelector getManualSettingContainerOnWiredNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/activity_network_type");
    }

    public BySelector getManualSettingSwitchOnWiredNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/activity_network_switch_text");
    }

    public BySelector getIpAddressContainerOnWiredNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/activity_ethernet_ip");
    }

    public BySelector getSubMaskContainerOnWiredNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/activity_ethernet_mask");
    }

    public BySelector getGatewayContainerOnWiredNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/activity_ethernet_gate");
    }

    public BySelector getDnsContainerOnWiredNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/activity_ethernet_dns");
    }

    public BySelector getSaveButtonOnWiredNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/activity_ethernet_save");
    }

    public BySelector getItemTitleOnWiredNetworkConfigsSelector() {
        return By.res("tv.fun.settings:id/ethernet_item_title");
    }

    public BySelector getItemEditorOnWiredNetworkConfigsSelector() {
        return By.clazz(TestConstants.CLASS_EDIT_VIEW);
    }

    public BySelector getContainerOfWifiHotSpotsListSelector() {
        return By.res("tv.fun.settings:id/wifi_list");
    }

}
