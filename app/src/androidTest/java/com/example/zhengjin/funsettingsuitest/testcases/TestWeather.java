package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryWeatherTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionBack;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiobjects.UiObjectsWeather;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WEATHER_PKG_NAME;

/**
 * Created by zhengjin on 2016/9/30.
 * <p>
 * Include test cases for Weather app.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestWeather extends TestCaseBase {

    private final UiObjectsWeather mFunUiObjects = UiObjectsWeather.getInstance();
    private final TaskWeather mTask = TaskWeather.getInstance();

    private final String TEXT_ADD_CITY = "添加城市";

    private final String INIT_PROVINCE = "湖北";
    private final String INIT_CITY = "武汉";
    private final String ADD_PROVINCE_1 = "山东";
    private final String ADD_CITY_1 = "青岛";
    private final String ADD_DEFAULT_PROVINCE_2 = "海南";
    private final String ADD_DEFAULT_CITY_2 = "三亚";

    private String mMessage;

    @BeforeClass
    public static void classSetUp() {
        ShellUtils.stopAndClearPackage(WEATHER_PKG_NAME);
    }

    @Before
    public void setUp() {
        TaskLauncher.backToLauncher();
        TaskLauncher.openWeatherFromLauncherQuickAccessBar();
    }

    @After
    public void clearUp() {
        ShellUtils.stopProcessByPackageName(WEATHER_PKG_NAME);
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test11DefaultLocatedCity() {
        mMessage = "Verify the default city name on weather home.";
        UiObject2 cityOnHome = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(cityOnHome));

        if (!mTask.formatCityNameWithSuffixDefault(INIT_CITY).equals(cityOnHome.getText())) {
            // exit testing process if init default city is not WuHan
            System.exit(-1);
        }
        Assert.assertTrue(mMessage, true);
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test12BottomTipOnWeatherHome() {
        mMessage = "Verify the tip at the bottom of weather home.";
        UiObject2 tip = mDevice.findObject(mFunUiObjects.getTipTextAtBottomOfWeatherHomeSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(tip));
        Assert.assertTrue(mMessage, tip.getText().contains("菜单键管理城市"));
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test13WeatherForecastDates() {
        final int forecastDaysCount = 5;

        List<String> actualDates = new ArrayList<>(forecastDaysCount);
        List<UiObject2> dates = mDevice.findObjects(mFunUiObjects.getWeatherForecastDateSelector());
        for (UiObject2 date : dates) {
            actualDates.add(mTask.getWeatherForecastDateFromUiText(date.getText()));
        }
        Collections.sort(actualDates, new dateComparator());

        List<String> expectedDates = mTask.getWeatherForecastDates();
        Collections.sort(expectedDates, new dateComparator());

        mMessage = "Verify the size of forecast day is 5.";
        Assert.assertEquals(mMessage, actualDates.size(), expectedDates.size());

        for (int i = 0; i < forecastDaysCount; i++) {
            mMessage = String.format(Locale.getDefault(),
                    "Verify the forecast date at position %d", i);
            Assert.assertEquals(mMessage, expectedDates.get(i), actualDates.get(i));
        }
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test14FunctionButtonsInMenu() {
        mTask.openBottomMenu();

        mMessage = "Verify update button exist in bottom menu.";
        UiObject2 btnUpdate = mDevice.findObject(
                By.text(mTask.MENU_BUTTON_TEXT_UPDATE)).getParent();
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(btnUpdate));
        mMessage = "Verify update button is default focused.";
        Assert.assertTrue(mMessage, btnUpdate.isFocused());

        mMessage = "Verify modify default city button exist.";
        UiObject2 btnModifyDefault = mDevice.findObject(
                By.text(mTask.MENU_BUTTON_TEXT_MODIFY_DEFAULT)).getParent();
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(btnModifyDefault));

        mMessage = "Verify add city button exist.";
        UiObject2 btnAddCity = mDevice.findObject(
                By.text(mTask.MENU_BUTTON_TEXT_ADD_CITY)).getParent();
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(btnAddCity));

        mMessage = "Verify the delete city button NOT exist.";
        UiObject2 textDeleteCity = mDevice.findObject(By.text(mTask.MENU_BUTTON_TEXT_DELETE_CITY));
        Assert.assertNull(mMessage, textDeleteCity);
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test15UpdateWeatherData() {
        mTask.openMenuAndEnterOnButtonByText(mTask.MENU_BUTTON_TEXT_UPDATE);
        TestHelper.waitForLoadingComplete();

        mMessage = "Verify the refresh time is updated after click Update menu button.";
        UiObject2 refreshTime =
                mDevice.findObject(mFunUiObjects.getTopRefreshTimeOfWeatherHomeSelector());
        Assert.assertNotNull(refreshTime);
        Assert.assertEquals(mMessage, "刚刚更新", refreshTime.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test21_01TitleAndLocationListOnAddCityPage() {
        mTask.validateWeatherHomeDefaultCityName(INIT_CITY);
        mTask.openMenuAndEnterOnButtonByText(mTask.MENU_BUTTON_TEXT_ADD_CITY);

        mMessage = "Verify the title of add city page.";
        UiObject2 title = mDevice.findObject(mFunUiObjects.getCityManagerTitleSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(title));
        Assert.assertEquals(mMessage, TEXT_ADD_CITY, title.getText());

        mMessage = "Verify the default located province.";
        Assert.assertEquals(mMessage, INIT_PROVINCE, mTask.getSelectedLocationProvince());
        mMessage = "Verify the default located city.";
        Assert.assertEquals(mMessage, INIT_CITY, mTask.getSelectedLocationCity());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test21_02AddCityAndCancel() {
        mTask.validateWeatherHomeDefaultCityName(INIT_CITY);
        mTask.openMenuAndEnterOnButtonByText(mTask.MENU_BUTTON_TEXT_ADD_CITY);

        // select 山东 青岛
        mTask.selectSpecifiedLocation(
                new String[]{ADD_PROVINCE_1, ADD_CITY_1}, new boolean[]{true, false});
        mAction.doDeviceActionAndWait(new DeviceActionBack(), WAIT);

        mMessage = "Verify the shown city on home is not changed after cancel add city.";
        UiObject2 location = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(mMessage, mTask.formatCityNameWithSuffixDefault(INIT_CITY)
                , location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test21_03AddNewCityWeather() {
        // pre-data: 武汉 after-data: 武汉 - 青岛
        mTask.validateWeatherHomeDefaultCityName(INIT_CITY);
        mTask.openMenuAndEnterOnButtonByText(mTask.MENU_BUTTON_TEXT_ADD_CITY);

        // select 山东 青岛
        mTask.selectSpecifiedLocation(
                new String[]{ADD_PROVINCE_1, ADD_CITY_1}, new boolean[]{true, false});
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mMessage = "Verify the shown city on home after add new city weather.";
        UiObject2 location = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(mMessage, ADD_CITY_1, location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test21_04AddDuplicatedCity() {
        // pre-data: 武汉 - 青岛 after-data: 武汉 - 青岛
        mTask.validateWeatherHomeDefaultCityName(INIT_CITY);
        mTask.openMenuAndEnterOnButtonByText(mTask.MENU_BUTTON_TEXT_ADD_CITY);

        // select 山东 青岛
        mTask.selectSpecifiedLocation(
                new String[]{ADD_PROVINCE_1, ADD_CITY_1}, new boolean[]{true, false});
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mMessage = "Verify stay on the add city page after add duplicated city.";
        UiObject2 title = mDevice.findObject(mFunUiObjects.getCityManagerTitleSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(title));
        Assert.assertEquals(mMessage, TEXT_ADD_CITY, title.getText());

        mMessage = "Verify the selected located province.";
        Assert.assertEquals(mMessage, ADD_PROVINCE_1, mTask.getSelectedLocationProvince());
        mMessage = "Verify the selected located city.";
        Assert.assertEquals(mMessage, ADD_CITY_1, mTask.getSelectedLocationCity());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test23_01TitleAndLocationListOnAddDefaultPage() {
        mTask.validateWeatherHomeDefaultCityName(INIT_CITY);
        mTask.openMenuAndEnterOnButtonByText(mTask.MENU_BUTTON_TEXT_MODIFY_DEFAULT);

        mMessage = "Verify the title of modify default city page.";
        UiObject2 title = mDevice.findObject(mFunUiObjects.getCityManagerTitleSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(title));
        Assert.assertEquals(mMessage, "修改默认城市", title.getText());

        mMessage = "Verify the default located province.";
        Assert.assertEquals(mMessage, INIT_PROVINCE, mTask.getSelectedLocationProvince());
        mMessage = "Verify the default located city.";
        Assert.assertEquals(mMessage, INIT_CITY, mTask.getSelectedLocationCity());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test23_02AddDefaultCityAndCancel() {
        mTask.validateWeatherHomeDefaultCityName(INIT_CITY);
        mTask.openMenuAndEnterOnButtonByText(mTask.MENU_BUTTON_TEXT_MODIFY_DEFAULT);

        // select 海南 三亚
        mTask.selectSpecifiedLocation(
                new String[]{ADD_DEFAULT_PROVINCE_2, ADD_DEFAULT_CITY_2},
                new boolean[]{false, false});
        mAction.doDeviceActionAndWait(new DeviceActionBack(), WAIT);

        mMessage = "Verify the shown city is unchanged on home after cancel add default city.";
        UiObject2 location = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(mMessage, mTask.formatCityNameWithSuffixDefault(INIT_CITY)
                , location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test23_03AddDefaultCityWeather() {
        // pre-data: 武汉 - 青岛 after-data: 三亚 - 武汉 - 青岛
        mTask.validateWeatherHomeDefaultCityName(INIT_CITY);
        mTask.openMenuAndEnterOnButtonByText(mTask.MENU_BUTTON_TEXT_MODIFY_DEFAULT);

        // select 海南 三亚
        mTask.selectSpecifiedLocation(
                new String[]{ADD_DEFAULT_PROVINCE_2, ADD_DEFAULT_CITY_2},
                new boolean[]{false, false});
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mMessage = "Verify the shown city on home after add new default city.";
        UiObject2 location = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(mMessage, mTask.formatCityNameWithSuffixDefault(ADD_DEFAULT_CITY_2)
                , location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test23_04HomeCityAfterAddDefaultCity() {
        mMessage = "Verify the shown city on home after add new default city.";
        UiObject2 location = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(mMessage, mTask.formatCityNameWithSuffixDefault(ADD_DEFAULT_CITY_2),
                location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test25_01ModifyDefaultCity() {
        // pre-data: 三亚 - 武汉 - 青岛 after-data: 武汉 - 青岛 - 三亚
        mTask.validateWeatherHomeDefaultCityName(ADD_DEFAULT_CITY_2);
        mTask.openMenuAndEnterOnButtonByText(mTask.MENU_BUTTON_TEXT_MODIFY_DEFAULT);

        // select 湖北 武汉
        mTask.selectSpecifiedLocation(
                new String[]{INIT_PROVINCE, INIT_CITY}, new boolean[]{true, false});
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mMessage = "Verify the shown city on home after change default city.";
        UiObject2 location = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(mMessage, mTask.formatCityNameWithSuffixDefault(INIT_CITY)
                , location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test25_02HomeCityAfterModifyDefaultCity() {
        mMessage = "Verify the shown city on home after change default city.";
        UiObject2 location = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(location));
        Assert.assertEquals(mMessage, mTask.formatCityNameWithSuffixDefault(INIT_CITY)
                , location.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test25_03ReAddDefaultCity() {
        // pre-data: 武汉 - 青岛 - 三亚 after-data: 武汉 - 青岛 - 三亚
        mTask.validateWeatherHomeDefaultCityName(INIT_CITY);
        mTask.openMenuAndEnterOnButtonByText(mTask.MENU_BUTTON_TEXT_MODIFY_DEFAULT);

        // select 湖北 武汉
        mTask.selectSpecifiedLocation(
                new String[]{INIT_PROVINCE, INIT_CITY}, new boolean[]{true, false});
        mAction.doDeviceActionAndWait(new DeviceActionEnter(), WAIT);

        mMessage = "Verify the shown city on home after re-add same default city.";
        UiObject2 cityOnHome = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(cityOnHome));
        Assert.assertEquals(mMessage, mTask.formatCityNameWithSuffixDefault(INIT_CITY)
                , cityOnHome.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test27CityListSequenceAfterUpdates() {
        // 武汉 - 青岛 - 三亚
        mMessage = "Verify the default city on home at position 1st.";
        UiObject2 cityOnHome = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(cityOnHome));
        Assert.assertEquals(mMessage, mTask.formatCityNameWithSuffixDefault(INIT_CITY)
                , cityOnHome.getText());

        mMessage = "Verify the city at position 2nd.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        Assert.assertEquals(mMessage, ADD_CITY_1,
                mTask.getCurrentCityOnWeatherHomePage().getText());

        mMessage = "Verify the city at position 3rd.";
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        Assert.assertEquals(mMessage, ADD_DEFAULT_CITY_2,
                mTask.getCurrentCityOnWeatherHomePage().getText());
    }

    @Test
    @Category({CategoryWeatherTests.class})
    public void test31DeleteButtonOnMenu() {
        mTask.openBottomMenu();

        mMessage = "Verify delete button is clickable in bottom menu.";
        UiObject2 btnDeleteCity = mDevice.findObject(
                By.text(mTask.MENU_BUTTON_TEXT_DELETE_CITY)).getParent();
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectClickable(btnDeleteCity));
    }

    @Test
    @Category({CategoryWeatherTests.class})
    public void test32_01DeleteCityAndCancel() {
        mTask.validateWeatherHomeDefaultCityName(INIT_CITY);
        // select 青岛
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        mTask.openMenuAndEnterOnButtonByText(mTask.MENU_BUTTON_TEXT_DELETE_CITY);

        mMessage = "Verify the title text of remove dialog.";
        UiObject2 title = mDevice.findObject(mFunUiObjects.getDialogTitleSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(title));
        Assert.assertEquals(String.format("删除城市\"%s\"?", ADD_CITY_1), title.getText());

        mMessage = "Verify the cancel button of dialog.";
        UiObject2 cancelBtn = mDevice.findObject(mFunUiObjects.getDialogCancelButtonSelector());
        Assert.assertTrue(mMessage, cancelBtn.isClickable());

        mMessage = "Verify the city on home after cancel delete city.";
        mAction.doClickActionAndWait(cancelBtn);
        UiObject2 cityOnHome = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(cityOnHome));
        Assert.assertEquals(mMessage, ADD_CITY_1, cityOnHome.getText());
    }

    @Test
    @Category({CategoryWeatherTests.class})
    public void test32_02DeleteCityAndConfirm() {
        // pre-data: 武汉 - 青岛 - 三亚 after-data: 武汉 - 三亚
        mTask.validateWeatherHomeDefaultCityName(INIT_CITY);
        // select 青岛
        mAction.doDeviceActionAndWait(new DeviceActionMoveRight(), WAIT);
        mTask.openMenuAndEnterOnButtonByText(mTask.MENU_BUTTON_TEXT_DELETE_CITY);

        mMessage = "Verify the confirm button of dialog.";
        UiObject2 confirmBtn = mDevice.findObject(mFunUiObjects.getDialogConfirmButtonSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(confirmBtn));
        Assert.assertTrue(mMessage, confirmBtn.isClickable());

        mMessage = "Verify city on home and delete current city.";
        mAction.doClickActionAndWait(confirmBtn, WAIT);
        UiObject2 cityOnHome = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(cityOnHome));
        Assert.assertEquals(mMessage, ADD_DEFAULT_CITY_2, cityOnHome.getText());
    }

    @Test
    @Category({CategoryWeatherTests.class})
    public void test33_01DeleteDefaultCityAndCancel() {
        mTask.validateWeatherHomeDefaultCityName(INIT_CITY);
        // select default 武汉
        mTask.openMenuAndEnterOnButtonByText(mTask.MENU_BUTTON_TEXT_DELETE_CITY);

        mMessage = "Verify the title text of remove dialog.";
        UiObject2 title = mDevice.findObject(mFunUiObjects.getDialogTitleSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(title));
        Assert.assertEquals(String.format("删除城市\"%s\"?", INIT_CITY), title.getText());

        mMessage = "Verify the cancel button of dialog.";
        UiObject2 cancelBtn = mDevice.findObject(mFunUiObjects.getDialogCancelButtonSelector());
        Assert.assertTrue(mMessage, cancelBtn.isClickable());

        mMessage = "Verify home city after cancel delete default city.";
        mAction.doClickActionAndWait(cancelBtn);
        UiObject2 cityOnHome = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(cityOnHome));
        Assert.assertEquals(mMessage,
                mTask.formatCityNameWithSuffixDefault(INIT_CITY), cityOnHome.getText());
    }

    @Test
    @Category({CategoryWeatherTests.class})
    public void test33_02DeleteDefaultCityAndConfirm() {
        // pre-data: 武汉 - 三亚 after-data: 三亚
        mTask.validateWeatherHomeDefaultCityName(INIT_CITY);
        mTask.openMenuAndEnterOnButtonByText(mTask.MENU_BUTTON_TEXT_DELETE_CITY);

        mMessage = "Verify the confirm button of dialog.";
        UiObject2 confirmBtn = mDevice.findObject(mFunUiObjects.getDialogConfirmButtonSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(confirmBtn));
        Assert.assertTrue(mMessage, confirmBtn.isClickable());

        mMessage = "Verify confirm and delete default city.";
        mAction.doClickActionAndWait(confirmBtn, WAIT);
        UiObject2 cityOnHome = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(cityOnHome));
        Assert.assertEquals(mMessage, mTask.formatCityNameWithSuffixDefault(ADD_DEFAULT_CITY_2)
                , cityOnHome.getText());
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test36HomeCityAndDeleteBtnNotExistForOnlyOneCity() {
        mMessage = "Verify the shown home city after delete default city.";
        UiObject2 cityOnHome = mTask.getCurrentCityOnWeatherHomePage();
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(cityOnHome));
        Assert.assertEquals(mMessage, mTask.formatCityNameWithSuffixDefault(ADD_DEFAULT_CITY_2)
                , cityOnHome.getText());

        mMessage = "Verify delete button is not shown when only one city exist.";
        mTask.openBottomMenu();
        UiObject2 btnDeleteCity = mDevice.findObject(By.text(mTask.MENU_BUTTON_TEXT_DELETE_CITY));
        Assert.assertNull(mMessage, btnDeleteCity);
    }

    @Test
    @Category(CategoryWeatherTests.class)
    public void test99ClearUpAfterAllTestCasesDone() {
        TaskWeather.destroyInstance();
        UiObjectsWeather.destroyInstance();
    }

    private class dateComparator implements Comparator<String> {

        @Override
        public int compare(String arg1, String arg2) {
            Integer[] lstMonthAndDay1 = formatDateText(arg1);
            Integer[] lstMonthAndDay2 = formatDateText(arg2);

            if (lstMonthAndDay1[0].intValue() == lstMonthAndDay2[0].intValue()) {
                return lstMonthAndDay1[1] - lstMonthAndDay2[1];  // compare day
            }
            return lstMonthAndDay1[0] - lstMonthAndDay2[0];  // compare month
        }

        private Integer[] formatDateText(String dateText) {
            List<Integer> lstMonthAndDay = new ArrayList<>(5);

            // match month and day number
            Pattern pattern = Pattern.compile("\\d{1,2}");
            Matcher matcher = pattern.matcher(dateText);
            while (matcher.find()) {
                lstMonthAndDay.add(Integer.parseInt(matcher.group()));
            }

            if (lstMonthAndDay.size() < 2) {
                // default month 1, and day 1
                return new Integer[]{1, 1};
            }
            return lstMonthAndDay.toArray(new Integer[0]);
        }
    }

}
