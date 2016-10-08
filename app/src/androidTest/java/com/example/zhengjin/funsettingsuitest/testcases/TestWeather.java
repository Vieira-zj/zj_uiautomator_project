package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryWeatherTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskWeather;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WEATHER_PKG_NAME;

/**
 * Created by zhengjin on 2016/9/30.
 * <p>
 * Include test cases for Weather app.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestWeather {

    private UiDevice mDevice;
    private UiActionsManager mAction;
    private TaskWeather mTask;

    @BeforeClass
    public static void classSetUp() {
        ShellUtils.stopAndClearPackage(WEATHER_PKG_NAME);
    }

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mTask = TaskWeather.getInstance();

        TaskLauncher.backToLauncher();
        TaskLauncher.clickOnButtonFromTopQuickAccessBar(
                TaskLauncher.getQuickAccessBtnWeatherSelector());
        Assert.assertTrue(TestHelper.waitForAppOpened(WEATHER_PKG_NAME));
    }

    @After
    public void clearUp() {
        ShellUtils.stopProcess(WEATHER_PKG_NAME);
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test11DefaultLocateCity() {
        String message;

        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomePageSelector());
        Assert.assertNotNull(location);
        message = "Verify the default location of weather.";
        Assert.assertEquals(message, "武汉(默认)", location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test12WeatherForecastDate() {
        String message;

        List<UiObject2> dates = mDevice.findObjects(mTask.getWeatherForecastDateSelector());
        final int size = dates.size();
        List<String> actualDates = new ArrayList<>(size);
        for (UiObject2 date : dates) {
            actualDates.add(mTask.getWeatherForecastDateFromUiText(date.getText()));
        }
        Collections.sort(actualDates, new dateComparator());
//        for (String date : actualDates) {
//            Log.d("ZJ", "Date test: " + date);
//        }

        List<String> expectedDates = mTask.getWeatherForecastDates();
        Collections.sort(expectedDates, new dateComparator());

        message = "Verify the length of weather forecast dates is 5.";
        Assert.assertEquals(message, actualDates.size(), expectedDates.size());

        for (int i = 0; i < size; i++) {
            message = String.format(Locale.getDefault(),
                    "Verify the forecast date at position %d", i);
            Assert.assertEquals(message, expectedDates.get(i), actualDates.get(i));
        }
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test13FunctionButtonsInMenu() {
        // TODO: 2016/9/30
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test14UpdateWeatherData() {
        // TODO: 2016/9/30  
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test99ClearUpAfterAllTestCasesDone() {
        mTask.destroyInstance();
    }

    private class dateComparator implements Comparator<String> {
        @Override
        public int compare(String arg1, String arg2) {
            int tmpInt1 =
                    Integer.parseInt(arg1.substring((arg1.indexOf("月") + 1), (arg1.length() - 1)));
            int tmpInt2 =
                    Integer.parseInt(arg2.substring((arg2.indexOf("月") + 1), (arg2.length() - 1)));
            return tmpInt1 - tmpInt2;
        }
    }

}
