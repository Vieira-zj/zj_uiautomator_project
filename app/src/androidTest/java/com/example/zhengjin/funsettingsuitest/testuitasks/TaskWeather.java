package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;

import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;

import junit.framework.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public BySelector getWeatherForecastDateSelector() {
        return By.res("tv.fun.weather:id/tv_weather_date");
    }

    public String getWeatherForecastDateFromUiText(String source) {
        int start = source.indexOf("(") + 1;
        return source.substring(start, (source.length() - 1));
    }

    public List<String> getWeatherForecastDates() {
        final int length = 5;
        List<String> dates = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            dates.add(this.formatForecastDate(this.getNextFromCurrentDate(i)));
        }
        return dates;
    }

    // param date: yyyy-MM-dd
    private String formatForecastDate(String date) {
        String message;

        String[] items = date.split("-");
        message = "Error in formatForecastDate(), the date items length is not 3.";
        Assert.assertTrue(message, (items.length == 3));

        String month = items[1];
        String day = items[2];
        // format "01" to 1
        return String.format(Locale.getDefault(),
                "%d月%d日", Integer.parseInt(month), Integer.parseInt(day));
    }

    private String getNextFromCurrentDate(int next) {
        final long DayTimeMillis = 24 * 60 * 60 * 1000;
        long extraTime = next * DayTimeMillis;
        return this.getSpecifiedDate(System.currentTimeMillis() + extraTime);
    }

    private String getSpecifiedDate(long timeMillis) {
        SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date curDate = new Date(timeMillis);
        return formatter.format(curDate);
    }

}
