package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceAction;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMenu;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WEATHER_PKG_NAME;

/**
 * Created by zhengjin on 2016/9/30.
 * <p>
 * Include the UI selectors and tasks for weather app.
 */

public final class TaskWeather {

    private static TaskWeather instance;
    private UiDevice device;
    private UiActionsManager action;

    public final String WEATHER_MENU_BUTTON_TEXT_UPDATE = "更新";
    public final String WEATHER_MENU_BUTTON_TEXT_MODIFY_DEFAULT = "修改默认";
    public final String WEATHER_MENU_BUTTON_TEXT_ADD_CITY = "添加城市";
    public final String WEATHER_MENU_BUTTON_TEXT_DELETE_CITY = "删除当前";

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

    private BySelector getProvinceListSelector() {
        return By.res("tv.fun.weather:id/ws_province");
    }

    private BySelector getCityListSelector() {
        return By.res("tv.fun.weather:id/ws_city");
    }

    private BySelector getMiddleItemInProvinceCityListSelector() {
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

    public void openWeatherHomePage() {
        TaskLauncher.clickOnButtonFromTopQuickAccessBar(
                TaskLauncher.getQuickAccessBtnWeatherSelector());
        Assert.assertTrue("openWeatherHomePage, open failed!",
                TestHelper.waitForAppOpenedByUntil(WEATHER_PKG_NAME));
    }

    public UiObject2 getCurrentCityOnWeatherHomePage() {
        return device.findObject(this.getLocationOfWeatherHomeSelector());
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
        String[] items = date.split("-");
        Assert.assertTrue("formatForecastDate, the date items count is not 3.", items.length == 3);

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

    public void openBottomMenu() {
        action.doDeviceActionAndWait(new DeviceActionMenu());
        Assert.assertTrue(
                TestHelper.waitForUiObjectExist(By.text(WEATHER_MENU_BUTTON_TEXT_UPDATE)));
        action.doDeviceActionAndWait(new DeviceActionMoveUp());  // request focus
    }

    public void ClickOnSpecifiedMenuButtonByText(String btnText) {
        UiObject2 btn = device.findObject(By.text(btnText)).getParent();
        for (int i = 0, moveTimes = 4; i < moveTimes; i++) {
            if (btn.isFocused()) {
                action.doDeviceActionAndWait(new DeviceActionEnter(), TestConstants.LONG_WAIT);
                return;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveRight());
        }

        Assert.assertTrue(String.format("ClickOnSpecifiedMenuButtonByText, " +
                "failed to focus on menu button %s.", btnText), false);
    }

    public String getSelectedLocationProvince() {
        UiObject2 provinceList = device.findObject(this.getProvinceListSelector());
        UiObject2 middleProvince =
                provinceList.findObject(this.getMiddleItemInProvinceCityListSelector());
        return middleProvince.getText();
    }

    public String getSelectedLocationCity() {
        UiObject2 cityList = device.findObject(this.getCityListSelector());
        UiObject2 middleCity =
                cityList.findObject(this.getMiddleItemInProvinceCityListSelector());
        return middleCity.getText();
    }

    // location = {province, city}
    public void selectSpecifiedLocation(String[] location, boolean[] directionUp) {
        this.selectSpecifiedLocationProvince(location[0], directionUp[0]);
        action.doDeviceActionAndWait(new DeviceActionMoveRight());
        this.selectSpecifiedLocationCity(location[1], directionUp[1]);
    }

    private void selectSpecifiedLocationProvince(String provinceText, boolean directionUp) {
        DeviceAction moveAction =
                directionUp ? new DeviceActionMoveUp() : new DeviceActionMoveDown();
        UiObject2 provinceList = device.findObject(this.getProvinceListSelector());
        for (int i = 0, maxMoveTimes = 20; i < maxMoveTimes; i++) {
            UiObject2 middleProvince =
                    provinceList.findObject(this.getMiddleItemInProvinceCityListSelector());
            if (provinceText.equals(middleProvince.getText())) {
                ShellUtils.systemWaitByMillis(TestConstants.SHORT_WAIT);
                return;
            }
            action.doDeviceActionAndWait(moveAction);
        }

        Assert.assertTrue(String.format("selectSpecifiedLocationProvince, province %s is NOT found."
                , provinceText), false);
    }

    private void selectSpecifiedLocationCity(String cityText, boolean directionUp) {
        DeviceAction moveAction =
                directionUp ? new DeviceActionMoveUp() : new DeviceActionMoveDown();
        UiObject2 cityList = device.findObject(this.getCityListSelector());
        for (int i = 0, maxMoveTimes = 20; i < maxMoveTimes; i++) {
            UiObject2 middleCity =
                    cityList.findObject(this.getMiddleItemInProvinceCityListSelector());
            if (cityText.equals(middleCity.getText())) {
                return;
            }
            action.doDeviceActionAndWait(moveAction);
        }

        Assert.assertTrue(String.format("selectSpecifiedLocationCity, city %s is NOT found."
                , cityText), false);
    }

}
