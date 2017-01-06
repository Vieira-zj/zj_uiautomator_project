package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;

import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;

/**
 * Created by zhengjin on 2017/1/5.
 * <p>
 * Include the UI selectors and tasks for about info page test cases.
 */
public final class TaskAboutInfo {

    private static TaskAboutInfo instance;
    private UiDevice device;
    private UiActionsManager action;

    private TaskAboutInfo() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        action = UiActionsManager.getInstance();
    }

    public static synchronized TaskAboutInfo getInstance() {
        if (instance == null) {
            instance = new TaskAboutInfo();
        }

        return instance;
    }

    public void destroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public BySelector getSettingsAboutInfoPageTitleSelector() {
        return By.res("tv.fun.settings:id/setting_title");
    }

}
