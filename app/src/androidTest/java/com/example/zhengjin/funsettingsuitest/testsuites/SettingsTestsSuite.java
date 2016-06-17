package com.example.zhengjin.funsettingsuitest.testsuites;

import com.example.zhengjin.funsettingsuitest.testcases.TestCommonSettings;
import com.example.zhengjin.funsettingsuitest.testcategory.CategorySettingsTests;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by zhengjin on 2016/6/17.
 *
 * Runt est cases for testing common settings module.
 */
@RunWith(Categories.class)
@Categories.IncludeCategory(CategorySettingsTests.class)
@Suite.SuiteClasses(TestCommonSettings.class)
public class SettingsTestsSuite {
}
