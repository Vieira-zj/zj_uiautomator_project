package com.example.zhengjin.funsettingsuitest.testsuites;

import com.example.zhengjin.funsettingsuitest.testcases.TestAboutInfoPage;
import com.example.zhengjin.funsettingsuitest.testcases.TestCommonSettings;
import com.example.zhengjin.funsettingsuitest.testcases.TestFileManager;
import com.example.zhengjin.funsettingsuitest.testcases.TestImageAndSoundSettings;
import com.example.zhengjin.funsettingsuitest.testcases.TestWeather;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryAboutInfoTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryFileManagerTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryImageAndSoundSettingsTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategorySettingsTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryWeatherTests;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by zhengjin on 2016/6/17.
 * <p>
 * Run all test cases by include and exclude rules.
 */
@RunWith(Categories.class)
@Categories.IncludeCategory({
        CategorySettingsTests.class,
        CategoryFileManagerTests.class,
        CategoryWeatherTests.class,
        CategoryImageAndSoundSettingsTests.class,
        CategoryAboutInfoTests.class})
//@Categories.IncludeCategory(CategoryDemoTests.class)
//@Categories.ExcludeCategory({CategoryDemoTests.class})
@Suite.SuiteClasses({
        TestCommonSettings.class,
        TestFileManager.class,
        TestWeather.class,
        TestImageAndSoundSettings.class,
        TestAboutInfoPage.class})
public final class AllTestsSuite {
}
