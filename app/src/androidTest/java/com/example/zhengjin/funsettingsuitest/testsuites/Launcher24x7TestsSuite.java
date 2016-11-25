package com.example.zhengjin.funsettingsuitest.testsuites;

import com.example.zhengjin.funsettingsuitest.testcases.TestPlayingVideos;
import com.example.zhengjin.funsettingsuitest.testcategory.Category24x7LauncherTests;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Vieira on 2016/7/7.
 *
 * Include test cases for 24 x 7 launcher home test.
 */
@RunWith(Categories.class)
@Categories.IncludeCategory(Category24x7LauncherTests.class)
@Suite.SuiteClasses(TestPlayingVideos.class)
public class Launcher24x7TestsSuite {
}
