package com.example.zhengjin.funsettingsuitest.testsuites;

import com.example.zhengjin.funsettingsuitest.testcasedemos.TestMainActivity;
import com.example.zhengjin.funsettingsuitest.testcasedemos.TestShellUtils;
import com.example.zhengjin.funsettingsuitest.testcasedemos.TestTaskLauncher;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by zhengjin on 2016/6/1.
 *
 * Run test cases for testing demos.
 */
@RunWith(Categories.class)
@Categories.IncludeCategory(CategoryDemoTests.class)
@Suite.SuiteClasses({TestTaskLauncher.class,
        TestMainActivity.class,
        TestShellUtils.class})
public class DemoTestsSuite {
}
