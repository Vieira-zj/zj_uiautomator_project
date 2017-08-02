package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceAction;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMenu;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiobjects.UiObjectsWeather;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhengjin on 2016/9/30.
 * <p>
 * Include the UI selectors and tasks for weather app.
 */
public final class TaskWeather extends TaskBase {

    private static TaskWeather instance;

    private UiObjectsWeather funUiObjects;

    public final String MENU_BUTTON_TEXT_UPDATE = "更新";
    public final String MENU_BUTTON_TEXT_MODIFY_DEFAULT = "修改默认";
    public final String MENU_BUTTON_TEXT_ADD_CITY = "添加城市";
    public final String MENU_BUTTON_TEXT_DELETE_CITY = "删除当前";

    private TaskWeather() {
        funUiObjects = UiObjectsWeather.getInstance();
    }

    public static TaskWeather getInstance() {
        if (instance == null) {
            synchronized (TaskWeather.class) {
                if (instance == null) {
                    instance = new TaskWeather();
                }
            }
        }
        return instance;
    }

    public static synchronized void destroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public void validateWeatherHomeDefaultCityName(String cityName) {
        UiObject2 cityOnHome = this.getCurrentCityOnWeatherHomePage();
        TestHelper.waitForUiObjectEnabled(cityOnHome);
        Assert.assertEquals("validateWeatherHomeDefaultCityName, failed!"
                , this.formatCityNameWithSuffixDefault(cityName), cityOnHome.getText());
    }

    public UiObject2 getCurrentCityOnWeatherHomePage() {
        return device.findObject(funUiObjects.getLocationOfWeatherHomeSelector());
    }

    public String formatCityNameWithSuffixDefault(String cityName) {
        return String.format("%s(默认)", cityName);
    }

    public String getWeatherForecastDateFromUiText(String source) {
        String retDate = "";
        int start = source.indexOf("(") + 1;
        try {
            retDate = source.substring(start, (source.length() - 1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retDate;
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
                TestHelper.waitForUiObjectExist(By.text(MENU_BUTTON_TEXT_UPDATE)));
        action.doDeviceActionAndWait(new DeviceActionMoveUp());  // request focus
    }

    private void EnterOnSpecifiedMenuButtonByText(String btnText) {
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

    public void openMenuAndEnterOnButtonByText(String btnText) {
        this.openBottomMenu();
        this.EnterOnSpecifiedMenuButtonByText(btnText);
    }

    public String getSelectedLocationProvince() {
        UiObject2 provinceList = device.findObject(funUiObjects.getProvinceListSelector());
        UiObject2 middleProvince =
                provinceList.findObject(funUiObjects.getMiddleItemInProvinceCityListSelector());
        return middleProvince.getText();
    }

    public String getSelectedLocationCity() {
        UiObject2 cityList = device.findObject(funUiObjects.getCityListSelector());
        UiObject2 middleCity =
                cityList.findObject(funUiObjects.getMiddleItemInProvinceCityListSelector());
        return middleCity.getText();
    }

    // locations = {province, city}
    public void selectSpecifiedLocation(String[] locationsArr, boolean[] directionUpArr) {
        this.selectSpecifiedLocationProvince(locationsArr[0], directionUpArr[0]);
        action.doDeviceActionAndWait(new DeviceActionMoveRight());
        this.selectSpecifiedLocationCity(locationsArr[1], directionUpArr[1]);
    }

    private void selectSpecifiedLocationProvince(String provinceText, boolean directionUp) {
        DeviceAction moveAction =
                directionUp ? new DeviceActionMoveUp() : new DeviceActionMoveDown();
        UiObject2 provinceList = device.findObject(funUiObjects.getProvinceListSelector());
        for (int i = 0, maxMoveTimes = 20; i < maxMoveTimes; i++) {
            UiObject2 middleProvince = provinceList.findObject(
                    funUiObjects.getMiddleItemInProvinceCityListSelector());
            if (provinceText.equals(middleProvince.getText())) {
                ShellUtils.systemWaitByMillis(TestConstants.SHORT_WAIT);
                return;
            }
            action.doDeviceActionAndWait(moveAction);
        }

        Assert.assertTrue(String.format(
                "selectSpecifiedLocationProvince, province %s is NOT found.", provinceText), false);
    }

    private void selectSpecifiedLocationCity(String cityText, boolean directionUp) {
        DeviceAction moveAction =
                directionUp ? new DeviceActionMoveUp() : new DeviceActionMoveDown();
        UiObject2 cityList = device.findObject(funUiObjects.getCityListSelector());
        for (int i = 0, maxMoveTimes = 20; i < maxMoveTimes; i++) {
            UiObject2 middleCity =
                    cityList.findObject(funUiObjects.getMiddleItemInProvinceCityListSelector());
            if (cityText.equals(middleCity.getText())) {
                return;
            }
            action.doDeviceActionAndWait(moveAction);
        }

        Assert.assertTrue(String.format(
                "selectSpecifiedLocationCity, city %s is NOT found.", cityText), false);
    }

}
