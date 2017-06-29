package com.example.zhengjin.funsettingsuitest.testuiobjects;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;

/**
 * Created by zhengjin on 2017/4/21.
 * <p>
 * Include UI objects for launcher home page.
 */

public class UiObjectsLauncher {

    private static UiObjectsLauncher ourInstance;

    private UiObjectsLauncher() {
    }

    public static UiObjectsLauncher getInstance() {
        if (ourInstance == null) {
            synchronized (UiObjectsLauncher.class) {
                if (ourInstance == null) {
                    ourInstance = new UiObjectsLauncher();
                }
            }
        }
        return ourInstance;
    }

    public static synchronized void destroyInstance() {
        if (ourInstance != null) {
            ourInstance = null;
        }
    }

    public BySelector getAllLauncherTabsSelector() {
        return By.res("com.bestv.ott:id/tab_title");
    }

    public BySelector getLauncherTopBarSelector() {
        return By.res("com.bestv.ott:id/container");
    }

    public BySelector getQuickAccessTabSettingsSelector() {
        return By.res("com.bestv.ott:id/setting");
    }

    public BySelector getQuickAccessTabWeatherSelector() {
        return By.res("com.bestv.ott:id/weather");
    }

    public BySelector getQuickAccessTabNetworkSelector() {
        return By.res("com.bestv.ott:id/network");
    }

    public BySelector getSettingsEntrySelector() {
        return By.res("com.bestv.ott:id/setting_entry");
    }

    public BySelector getLoadingCircleSelector() {
        return By.res("com.bestv.ott:id/progressBar");
    }

}
