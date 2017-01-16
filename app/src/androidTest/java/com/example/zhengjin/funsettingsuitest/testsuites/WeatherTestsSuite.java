package com.example.zhengjin.funsettingsuitest.testsuites;

import com.example.zhengjin.funsettingsuitest.testcases.TestWeather;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by zhengjin on 2017/1/16.
 *
 * Run test cases for Weather apk.
 */

@RunWith(Categories.class)
@Suite.SuiteClasses(TestWeather.class)
@Categories.IncludeCategory(CategoryDemoTests.class)
public final class WeatherTestsSuite {
}
