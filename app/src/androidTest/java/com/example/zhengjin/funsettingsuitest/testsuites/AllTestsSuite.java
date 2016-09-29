package com.example.zhengjin.funsettingsuitest.testsuites;

import com.example.zhengjin.funsettingsuitest.testcases.TestCommonSettings;
import com.example.zhengjin.funsettingsuitest.testcases.TestFileManager;
import com.example.zhengjin.funsettingsuitest.testcases.TestVideoHomeTab;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryVideoHomeTabTests;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by zhengjin on 2016/6/17.
 * <p>
 * Run all test cases by include and exclude rules.
 */
@RunWith(Categories.class)
//@Categories.IncludeCategory({CategorySettingsTests.class, CategoryFileManagerTests.class})
@Categories.ExcludeCategory({CategoryVideoHomeTabTests.class})
@Suite.SuiteClasses({TestCommonSettings.class,
        TestFileManager.class,
        TestVideoHomeTab.class})
public class AllTestsSuite {
}
