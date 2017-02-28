package com.example.zhengjin.funsettingsuitest.testuiobjects;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;

/**
 * Created by zhengjin on 2017/2/28.
 * <p>
 * Include UI objects for weather apk.
 */
public final class UiObjectsWeather {

    private static UiObjectsWeather ourInstance = new UiObjectsWeather();

    public static UiObjectsWeather getInstance() {
        return ourInstance;
    }

    private UiObjectsWeather() {
    }

    public void destroyInstance() {
        if (ourInstance != null) {
            ourInstance = null;
        }
    }

    public BySelector getLocationOfWeatherHomeSelector() {
        return By.res("tv.fun.weather:id/tv_weather_day_addr");
    }

    public BySelector getTipTextAtBottomOfWeatherHomeSelector() {
        return By.res("tv.fun.weather:id/tv_tip");
    }

    public BySelector getTopRefreshTimeOfWeatherHomeSelector() {
        return By.res("tv.fun.weather:id/tv_weather_refresh_time");
    }

    public BySelector getWeatherForecastDateSelector() {
        return By.res("tv.fun.weather:id/tv_weather_date");
    }

    public BySelector getCityManagerTitleSelector() {
        return By.res("tv.fun.weather:id/setting_title");
    }

    public BySelector getProvinceListSelector() {
        return By.res("tv.fun.weather:id/ws_province");
    }

    public BySelector getCityListSelector() {
        return By.res("tv.fun.weather:id/ws_city");
    }

    public BySelector getMiddleItemInProvinceCityListSelector() {
        return By.res("tv.fun.weather:id/wheel_view_tx3");
    }

    public BySelector getDialogTitleSelector() {
        return By.res("tv.fun.weather:id/tv_title");
    }

    public BySelector getDialogConfirmButtonSelector() {
        return By.res("tv.fun.weather:id/btn_confirm");
    }

    public BySelector getDialogCancelButtonSelector() {
        return By.res("tv.fun.weather:id/btn_cancel");
    }

}
