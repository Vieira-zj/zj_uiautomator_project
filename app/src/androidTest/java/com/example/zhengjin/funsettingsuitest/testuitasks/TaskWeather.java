package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;

import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;

/**
 * Created by zhengjin on 2016/9/30.
 *
 * Include the UI selectors and tasks for weather app.
 */

public final class TaskWeather {

    private static TaskWeather instance;
    private UiDevice device;
    private UiActionsManager action;

    private TaskWeather() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        action = UiActionsManager.getInstance();
    }

    public static synchronized TaskWeather getInstance() {
        if (instance == null) {
            instance = new TaskWeather();
        }
        return instance;
    }

    public void destroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public BySelector getLocationOfWeatherHomePageSelector() {
        return By.res("tv.fun.weather:id/tv_weather_day_addr");
    }

}
