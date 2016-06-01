package com.example.zhengjin.funsettingsuitest.testsuites;

import com.example.zhengjin.funsettingsuitest.testcategory.DemoTests;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;

/**
 * Created by zhengjin on 2016/6/1.
 */

@RunWith(Categories.class)
@Categories.IncludeCategory(DemoTests.class)
public class DemoTestsSuite {
}
