package com.example.zhengjin.funsettingsuitest.testsuites;

import com.example.zhengjin.funsettingsuitest.testcasedemos.TestMainActivity;
import com.example.zhengjin.funsettingsuitest.testcasedemos.TestShellUtils;
import com.example.zhengjin.funsettingsuitest.testcasedemos.TestTaskLauncher;
import com.example.zhengjin.funsettingsuitest.testcases.TestCommonSettings;
import com.example.zhengjin.funsettingsuitest.testcases.TestFileManager;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryFileManagerTests;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by zhengjin on 2016/6/17.
 *
 * Run all test cases by include and exclude rules.
 */
@RunWith(Categories.class)
@Categories.IncludeCategory(CategoryFileManagerTests.class)
//@Categories.ExcludeCategory({CategoryDemoTests.class, CategorySettingsTests.class})
@Suite.SuiteClasses({TestTaskLauncher.class,
        TestMainActivity.class,
        TestShellUtils.class,
        TestCommonSettings.class,
        TestFileManager.class})
public class AllTestsSuite {
}
