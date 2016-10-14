package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryWeatherTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionBack;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;
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

    private final String INIT_PROVINCE = "湖北";
    private final String INIT_CITY = "武汉";
    private final String ADD_PROVINCE = "山东";
    private final String ADD_CITY = "青岛";
    private final String ADD_DEFAULT_PROVINCE = "海南";
    private final String ADD_DEFAULT_CITY = "三亚";

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
        Assert.assertTrue("Open Weather app.",
                TestHelper.waitForAppOpenedByUntil(WEATHER_PKG_NAME));
    }

    @After
    public void clearUp() {
        ShellUtils.stopProcess(WEATHER_PKG_NAME);
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test11DefaultLocateCity() {
        String message;

        message = "Verify the default location on weather home.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertNotNull(location);
        Assert.assertEquals(message, String.format("%s(默认)", INIT_CITY), location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test12TipAtBottomOfWeatherHome() {
        String message;

        message = "Verify the tip at the bottom of weather home.";
        UiObject2 tip = mDevice.findObject(mTask.getTipTextAtBottomOfWeatherHomeSelector());
        Assert.assertNotNull(tip);
        Assert.assertTrue(message, tip.getText().contains("菜单键管理城市"));
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test13WeatherForecastDates() {
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
    public void test14FunctionButtonsInMenu() {
        String message;

        mTask.openBottomMenu();
        // verification 1
        message = "Verify the update button exist.";
        UiObject2 textUpdate = mDevice.findObject(By.text(mTask.WEATHER_MENU_BUTTON_TEXT_UPDATE));
        Assert.assertTrue(message, TestHelper.waitForUiObjectEnabled(textUpdate));

        message = "Verify the update is default focused.";
        UiObject2 btnUpdate = textUpdate.getParent();
        Assert.assertTrue(message, btnUpdate.isFocused());

        // verification 2
        message = "Verify the modify default city button exist.";
        UiObject2 textModifyDefault =
                mDevice.findObject(By.text(mTask.WEATHER_MENU_BUTTON_TEXT_MODIFY_DEFAULT));
        Assert.assertTrue(message, TestHelper.waitForUiObjectEnabled(textModifyDefault));

        message = "Verify the update button exist.";
        UiObject2 textAddCity =
                mDevice.findObject(By.text(mTask.WEATHER_MENU_BUTTON_TEXT_ADD_CITY));
        Assert.assertTrue(message, TestHelper.waitForUiObjectEnabled(textAddCity));

        // verification 3
        message = "Verify the update button NOT exist.";
        UiObject2 textDeleteCity =
                mDevice.findObject(By.text(mTask.WEATHER_MENU_BUTTON_TEXT_DELETE_CITY));
        Assert.assertNull(message, textDeleteCity);
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test15UpdateAndRefreshWeatherData() {
        String message;

        mTask.openBottomMenu();
        mTask.focusOnSpecifiedMenuButtonAndEnter(mTask.WEATHER_MENU_BUTTON_TEXT_UPDATE);

        message = "Verify the refresh time is updated after click Update menu button.";
        UiObject2 refreshTime = mDevice.findObject(mTask.getTopRefreshTimeOfWeatherHomeSelector());
        Assert.assertNotNull(refreshTime);
        Assert.assertEquals(message, "刚刚更新", refreshTime.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test21OpenAddCityAndCancel() {
        String message;

        // verification 1
        mTask.openBottomMenu();
        mTask.focusOnSpecifiedMenuButtonAndEnter(mTask.WEATHER_MENU_BUTTON_TEXT_ADD_CITY);
        message = "Verify the city manager page is opened.";
        Assert.assertTrue(message,
                TestHelper.waitForUiObjectExist(mTask.getCityManagerTitleSelector()));

        message = "Verify the default selected province.";
        Assert.assertEquals(message, INIT_PROVINCE, mTask.getSelectedLocationProvince());
        message = "Verify the default selected city.";
        Assert.assertEquals(message, INIT_CITY, mTask.getSelectedLocationCity());

        // verification 2
        mTask.selectSpecifiedLocationProvince(ADD_PROVINCE, false);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        mTask.selectSpecifiedLocationCity(ADD_CITY, false);
        mAction.doDeviceActionAndWait(new DeviceActionBack());

        message = "Verify the show city is not changed after cancel add city.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertNotNull(location);
        Assert.assertEquals(message, String.format("%s(默认)", INIT_CITY), location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test22AddNewCityWeather() {
        String message;

        mTask.openBottomMenu();
        mTask.focusOnSpecifiedMenuButtonAndEnter(mTask.WEATHER_MENU_BUTTON_TEXT_ADD_CITY);

        mTask.selectSpecifiedLocationProvince(ADD_PROVINCE, false);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        mTask.selectSpecifiedLocationCity(ADD_CITY, false);
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        message = "Verify the show city after add new city weather.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(message, ADD_CITY, location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test23AddNewDefaultCityWeather() {
        String message;

        mTask.openBottomMenu();
        mTask.focusOnSpecifiedMenuButtonAndEnter(mTask.WEATHER_MENU_BUTTON_TEXT_MODIFY_DEFAULT);

        mTask.selectSpecifiedLocationProvince(ADD_DEFAULT_PROVINCE, true);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        mTask.selectSpecifiedLocationCity(ADD_DEFAULT_CITY, false);
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        message = "Verify the show city after add new default city.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(message,
                String.format("%s(默认)", ADD_DEFAULT_CITY), location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test24FirstShowCityAfterAddNewDefaultCity() {
        String message;

        message = "Verify the 1st show city after add new default city.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(message,
                String.format("%s(默认)", ADD_DEFAULT_CITY), location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test25ModifyDefaultCity() {
        String message;

        mTask.openBottomMenu();
        mTask.focusOnSpecifiedMenuButtonAndEnter(mTask.WEATHER_MENU_BUTTON_TEXT_MODIFY_DEFAULT);

        mTask.selectSpecifiedLocationProvince(INIT_PROVINCE, false);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        mTask.selectSpecifiedLocationCity(INIT_CITY, false);
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        message = "Verify the show city after change default city.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(message,
                String.format("%s(默认)", INIT_CITY), location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test26FirstShowCityAfterModifyDefaultCity() {
        String message;

        message = "Verify the 1st show city after change default city.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(message,
                String.format("%s(默认)", INIT_CITY), location.getText());
    }

    @Ignore
    @Category(CategoryWeatherTests.class)
    public void test31AddDuplicatedCity() {
        // TODO: 2016/10/14
    }

    @Ignore
    @Category(CategoryWeatherTests.class)
    public void test32AddDuplicatedDefaultCity() {
        // TODO: 2016/10/14
    }

    @Ignore
    @Category(CategoryWeatherTests.class)
    public void test33SequenceOfAddedCities() {
        // TODO: 2016/10/14
    }

    @Ignore
    @Category(CategoryWeatherTests.class)
    public void test41DeleteCityAndCancel() {
        // TODO: 2016/10/14
    }

    @Ignore
    @Category(CategoryWeatherTests.class)
    public void test42DeleteCityAndConfirm() {
        // TODO: 2016/10/14
    }

    @Ignore
    @Category(CategoryWeatherTests.class)
    public void test43DeleteDefaultCityAndCancel() {
        // TODO: 2016/10/14
    }

    @Ignore
    @Category(CategoryWeatherTests.class)
    public void test44DeleteDefaultCityAndConfirm() {
        // TODO: 2016/10/14
    }

    @Ignore
    @Category(CategoryWeatherTests.class)
    public void test45DeleteWhenOnlyOneCity() {
        // TODO: 2016/10/14
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
