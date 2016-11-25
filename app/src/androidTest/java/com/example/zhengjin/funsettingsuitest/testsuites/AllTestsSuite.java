package com.example.zhengjin.funsettingsuitest.testsuites;

import com.example.zhengjin.funsettingsuitest.testcases.TestCommonSettings;
import com.example.zhengjin.funsettingsuitest.testcases.TestFileManager;
import com.example.zhengjin.funsettingsuitest.testcases.TestHomeVideoTab;
import com.example.zhengjin.funsettingsuitest.testcases.TestPlayingVideos;
import com.example.zhengjin.funsettingsuitest.testcases.TestWeather;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryFileManagerTests;
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
        CategoryWeatherTests.class})
@Categories.ExcludeCategory({CategoryDemoTests.class})
@Suite.SuiteClasses({
        TestCommonSettings.class,
        TestFileManager.class,
        TestWeather.class,
        TestPlayingVideos.class,
        TestHomeVideoTab.class})
public class AllTestsSuite {
}
