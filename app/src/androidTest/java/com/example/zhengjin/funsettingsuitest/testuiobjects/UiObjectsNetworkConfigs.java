package com.example.zhengjin.funsettingsuitest.testuiobjects;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;

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

    public BySelector getContainerOfWifiHotSpotsListSelector() {
        return By.res("tv.fun.settings:id/wifi_list");
    }

}
