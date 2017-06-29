package com.example.zhengjin.funsettingsuitest.testsuites;

import com.example.zhengjin.funsettingsuitest.testcases.TestAboutInfoPage;
import com.example.zhengjin.funsettingsuitest.testcases.TestGeneralSettings;
import com.example.zhengjin.funsettingsuitest.testcases.TestFileManager;
import com.example.zhengjin.funsettingsuitest.testcases.TestHomeVideoTab;
import com.example.zhengjin.funsettingsuitest.testcases.TestImageAndSoundConfigs;
import com.example.zhengjin.funsettingsuitest.testcases.TestWeather;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryAboutInfoTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryFileManagerTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryHomeVideoTabTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryImageAndSoundSettingsTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategorySettingsTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryVersion20;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryWeatherTests;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by zhengjin on 2016/6/17.
 * <p>
 * Run all test cases by include and exclude rules.
 * <p>
 * Run commands:
 * adb shell am instrument -w -r
 * -e listener com.example.zhengjin.funsettingsuitest.testrunner.RunnerListenerFunSettings
 * -e debug false
 * -e class com.example.zhengjin.funsettingsuitest.testsuites.AllTestsSuite
 * com.example.zhengjin.funsettingsuitest.test/android.support.test.runner.AndroidJUnitRunner
 */
@RunWith(Categories.class)
@Categories.IncludeCategory({
        CategorySettingsTests.class,
        CategoryImageAndSoundSettingsTests.class,
        CategoryAboutInfoTests.class,
        CategoryFileManagerTests.class,
        CategoryWeatherTests.class,
        CategoryHomeVideoTabTests.class})
//@Categories.IncludeCategory(CategoryDemoTests.class)
@Categories.ExcludeCategory({CategoryVersion20.class})
@Suite.SuiteClasses({
        TestGeneralSettings.class,
        TestFileManager.class,
        TestWeather.class,
        TestImageAndSoundConfigs.class,
        TestAboutInfoPage.class,
        TestHomeVideoTab.class})
public final class AllTestsSuite {
}
