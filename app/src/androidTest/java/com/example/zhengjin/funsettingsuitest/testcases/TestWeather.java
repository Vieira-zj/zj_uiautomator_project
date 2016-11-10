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
    private String mMessage;

    private final String INIT_PROVINCE = "湖北";
    private final String INIT_CITY = "武汉";
    private final String ADD_PROVINCE_1 = "山东";
    private final String ADD_CITY_1 = "青岛";
    private final String ADD_DEFAULT_PROVINCE_2 = "海南";
    private final String ADD_DEFAULT_CITY_2 = "三亚";

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
    public void test11DefaultLocatedCity() {
        mMessage = "Verify the default location on weather home.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(mMessage, String.format("%s(默认)", INIT_CITY), location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test12BottomTipOnWeatherHome() {
        mMessage = "Verify the tip at the bottom of weather home.";
        UiObject2 tip = mDevice.findObject(mTask.getTipTextAtBottomOfWeatherHomeSelector());
        Assert.assertNotNull(tip);
        Assert.assertTrue(mMessage, tip.getText().contains("菜单键管理城市"));
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test13WeatherForecastDates() {
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

        mMessage = "Verify the length of weather forecast dates is 5.";
        Assert.assertEquals(mMessage, actualDates.size(), expectedDates.size());

        for (int i = 0; i < size; i++) {
            mMessage = String.format(Locale.getDefault(),
                    "Verify the forecast date at position %d", i);
            Assert.assertEquals(mMessage, expectedDates.get(i), actualDates.get(i));
        }
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test14FunctionButtonsInMenu() {
        mTask.openBottomMenu();

        mMessage = "Verify update button exist.";
        UiObject2 btnUpdate =
                mDevice.findObject(By.text(mTask.WEATHER_MENU_BUTTON_TEXT_UPDATE)).getParent();
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(btnUpdate));
        mMessage = "Verify update button is default focused.";
        Assert.assertTrue(mMessage, btnUpdate.isFocused());

        mMessage = "Verify modify default city button exist.";
        UiObject2 btnModifyDefault =
                mDevice.findObject(By.text(mTask.WEATHER_MENU_BUTTON_TEXT_MODIFY_DEFAULT))
                        .getParent();
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(btnModifyDefault));

        mMessage = "Verify add city button exist.";
        UiObject2 btnAddCity =
                mDevice.findObject(By.text(mTask.WEATHER_MENU_BUTTON_TEXT_ADD_CITY)).getParent();
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(btnAddCity));

        mMessage = "Verify the delete city button NOT exist.";
        UiObject2 textDeleteCity =
                mDevice.findObject(By.text(mTask.WEATHER_MENU_BUTTON_TEXT_DELETE_CITY));
        Assert.assertNull(mMessage, textDeleteCity);
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test15UpdateAndRefreshWeatherData() {
        mTask.openBottomMenu();
        mTask.focusOnSpecifiedMenuButtonAndEnter(mTask.WEATHER_MENU_BUTTON_TEXT_UPDATE);

        mMessage = "Verify the refresh time is updated after click Update menu button.";
        UiObject2 refreshTime = mDevice.findObject(mTask.getTopRefreshTimeOfWeatherHomeSelector());
        Assert.assertNotNull(refreshTime);
        Assert.assertEquals(mMessage, "刚刚更新", refreshTime.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test21AddCityAndCancel() {
        mTask.openBottomMenu();
        mTask.focusOnSpecifiedMenuButtonAndEnter(mTask.WEATHER_MENU_BUTTON_TEXT_ADD_CITY);

        // verification 1
        mMessage = "Verify the title of add city page.";
        UiObject2 title = mDevice.findObject(mTask.getCityManagerTitleSelector());
        Assert.assertEquals(mMessage, "添加城市", title.getText());
        mMessage = "Verify the default located province.";
        Assert.assertEquals(mMessage, INIT_PROVINCE, mTask.getSelectedLocationProvince());
        mMessage = "Verify the default located city.";
        Assert.assertEquals(mMessage, INIT_CITY, mTask.getSelectedLocationCity());

        // select 山东 青岛
        mTask.selectSpecifiedLocationProvince(ADD_PROVINCE_1, false);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        mTask.selectSpecifiedLocationCity(ADD_CITY_1, false);
        mAction.doDeviceActionAndWait(new DeviceActionBack(), WAIT);

        // verification 2
        mMessage = "Verify the shown city is not changed after cancel add city.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertNotNull(location);
        Assert.assertEquals(mMessage, String.format("%s(默认)", INIT_CITY), location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test22AddNewCityWeather() {
        mTask.openBottomMenu();
        mTask.focusOnSpecifiedMenuButtonAndEnter(mTask.WEATHER_MENU_BUTTON_TEXT_ADD_CITY);

        // select 山东 青岛
        mTask.selectSpecifiedLocationProvince(ADD_PROVINCE_1, false);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        mTask.selectSpecifiedLocationCity(ADD_CITY_1, false);
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mMessage = "Verify the shown city after add new city weather.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(mMessage, ADD_CITY_1, location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test23AddDefaultCityAndCancel() {
        mTask.openBottomMenu();
        mTask.focusOnSpecifiedMenuButtonAndEnter(mTask.WEATHER_MENU_BUTTON_TEXT_MODIFY_DEFAULT);

        // verification 1
        mMessage = "Verify the title of modify default city page.";
        UiObject2 title = mDevice.findObject(mTask.getCityManagerTitleSelector());
        Assert.assertEquals(mMessage, "修改默认城市", title.getText());
        mMessage = "Verify the default located province.";
        Assert.assertEquals(mMessage, INIT_PROVINCE, mTask.getSelectedLocationProvince());
        mMessage = "Verify the default located city.";
        Assert.assertEquals(mMessage, INIT_CITY, mTask.getSelectedLocationCity());

        // select 海南 三亚
        mTask.selectSpecifiedLocationProvince(ADD_DEFAULT_PROVINCE_2, true);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        mTask.selectSpecifiedLocationCity(ADD_DEFAULT_CITY_2, false);
        mAction.doDeviceActionAndWait(new DeviceActionBack(), WAIT);

        // verification 2
        mMessage = "Verify the shown city is not changed after cancel add new default city.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(mMessage, String.format("%s(默认)", INIT_CITY), location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test24AddDefaultCityWeather() {
        mTask.openBottomMenu();
        mTask.focusOnSpecifiedMenuButtonAndEnter(mTask.WEATHER_MENU_BUTTON_TEXT_MODIFY_DEFAULT);

        // select 海南 三亚
        mTask.selectSpecifiedLocationProvince(ADD_DEFAULT_PROVINCE_2, true);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        mTask.selectSpecifiedLocationCity(ADD_DEFAULT_CITY_2, false);
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mMessage = "Verify the shown city after add new default city.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(mMessage,
                String.format("%s(默认)", ADD_DEFAULT_CITY_2), location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test25FirstCityAfterAddDefaultCity() {
        mMessage = "Verify the 1st shown city after add new default city.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(mMessage,
                String.format("%s(默认)", ADD_DEFAULT_CITY_2), location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test26ModifyDefaultCity() {
        mTask.openBottomMenu();
        mTask.focusOnSpecifiedMenuButtonAndEnter(mTask.WEATHER_MENU_BUTTON_TEXT_MODIFY_DEFAULT);

        // select 湖北 武汉
        mTask.selectSpecifiedLocationProvince(INIT_PROVINCE, false);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        mTask.selectSpecifiedLocationCity(INIT_CITY, false);
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mMessage = "Verify the shown city after change default city.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(mMessage, String.format("%s(默认)", INIT_CITY), location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test27FirstCityAfterModifyDefaultCity() {
        mMessage = "Verify the 1st shown city after change default city.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(mMessage, String.format("%s(默认)", INIT_CITY), location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test28ReAddDefaultCity() {
        mTask.openBottomMenu();
        mTask.focusOnSpecifiedMenuButtonAndEnter(mTask.WEATHER_MENU_BUTTON_TEXT_MODIFY_DEFAULT);

        // select 湖北 武汉
        mTask.selectSpecifiedLocationProvince(INIT_PROVINCE, false);
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight());
        mTask.selectSpecifiedLocationCity(INIT_CITY, false);
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mMessage = "Verify the shown city after re-add same default city.";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertEquals(mMessage, String.format("%s(默认)", INIT_CITY), location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test29SequenceAfterAddedCities() {
        mMessage = "Verify the 1st default city";
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertEquals(mMessage, String.format("%s(默认)", INIT_CITY), location.getText());

        mMessage = "Verify the 2nd added city.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertEquals(mMessage, ADD_CITY_1, location.getText());

        mMessage = "Verify the 3rd added city.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertEquals(mMessage, ADD_DEFAULT_CITY_2, location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test31DeleteMenuButton() {
        mTask.openBottomMenu();

        mMessage = "Verify delete button exist.";
        UiObject2 btnDeleteCity =
                mDevice.findObject(By.text(mTask.WEATHER_MENU_BUTTON_TEXT_DELETE_CITY)).getParent();
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(btnDeleteCity));
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test32DeleteCityAndCancel() {
        // select 青岛
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        mTask.openBottomMenu();
        mTask.focusOnSpecifiedMenuButtonAndEnter(mTask.WEATHER_MENU_BUTTON_TEXT_DELETE_CITY);

        mMessage = "Verify the remove dialog title text.";
        UiObject2 title = mDevice.findObject(mTask.getDialogTitleSelector());
        Assert.assertEquals(String.format("删除城市\"%s\"?", ADD_CITY_1), title.getText());

        mMessage = "Verify the cancel button of dialog.";
        UiObject2 cancelBtn = mDevice.findObject(mTask.getDialogCancelButtonSelector());
        Assert.assertTrue(mMessage, cancelBtn.isClickable());

        mMessage = "Verify click cancel button of dialog.";
        mAction.doClickActionAndWait(cancelBtn);
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertEquals(mMessage, ADD_CITY_1, location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test33DeleteCityAndConfirm() {
        // select 青岛
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        mTask.openBottomMenu();
        mTask.focusOnSpecifiedMenuButtonAndEnter(mTask.WEATHER_MENU_BUTTON_TEXT_DELETE_CITY);

        mMessage = "Verify the confirm button of dialog.";
        UiObject2 confirmBtn = mDevice.findObject(mTask.getDialogConfirmButtonSelector());
        Assert.assertTrue(mMessage, confirmBtn.isClickable());

        mMessage = "Verify delete a city.";
        mAction.doClickActionAndWait(confirmBtn, WAIT);
        UiObject2 location = mDevice.findObject(mTask.getLocationOfWeatherHomeSelector());
        Assert.assertEquals(mMessage, ADD_DEFAULT_CITY_2, location.getText());
    }

    @Ignore
    @Category(CategoryWeatherTests.class)
    public void test34DeleteDefaultCityAndCancel() {
        // TODO: 2016/10/14
    }

    @Ignore
    @Category(CategoryWeatherTests.class)
    public void test35DeleteDefaultCityAndConfirm() {
        // TODO: 2016/10/14
    }

    @Ignore
    @Category(CategoryWeatherTests.class)
    public void test36DeleteWhenOnlyOneCity() {
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
