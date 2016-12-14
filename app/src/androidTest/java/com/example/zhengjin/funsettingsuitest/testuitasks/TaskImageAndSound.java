package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;

/**
 * Created by zhengjin on 2016/12/14.
 *
 * Include the UI selectors and tasks for settings module.
 */

public final class TaskImageAndSound {

    private static TaskImageAndSound instance = null;
    private UiDevice device;
    private UiActionsManager action;

    private TaskImageAndSound() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        action = UiActionsManager.getInstance();
    }

    public static synchronized TaskImageAndSound getInstance() {
        if (instance == null) {
            instance = new TaskImageAndSound();
        }
        return instance;
    }

    public void destroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

}
