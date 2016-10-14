package com.example.zhengjin.funsettingsuitest.testsuites;

import com.example.zhengjin.funsettingsuitest.testcases.TestCommonSettings;
import com.example.zhengjin.funsettingsuitest.testcases.TestFileManager;
import com.example.zhengjin.funsettingsuitest.testcases.TestHomeVideoTab;
import com.example.zhengjin.funsettingsuitest.testcases.TestPlayingFilm;
import com.example.zhengjin.funsettingsuitest.testcases.TestWeather;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryFileManagerTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategorySettingsTests;

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
        CategoryFileManagerTests.class})
//@Categories.ExcludeCategory({CategoryHomeVideoTabTests.class})
@Suite.SuiteClasses({
        TestCommonSettings.class,
        TestFileManager.class,
        TestWeather.class,
        TestPlayingFilm.class,
        TestHomeVideoTab.class})
public class AllTestsSuite {
}
