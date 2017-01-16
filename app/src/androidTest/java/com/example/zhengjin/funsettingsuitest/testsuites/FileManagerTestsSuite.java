package com.example.zhengjin.funsettingsuitest.testsuites;

import com.example.zhengjin.funsettingsuitest.testcases.TestFileManager;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by zhengjin on 2016/6/17.
 *
 * Run test cases for testing file manager module.
 */
@RunWith(Categories.class)
@Suite.SuiteClasses(TestFileManager.class)
@Categories.IncludeCategory(CategoryDemoTests.class)
public final class FileManagerTestsSuite {
}
