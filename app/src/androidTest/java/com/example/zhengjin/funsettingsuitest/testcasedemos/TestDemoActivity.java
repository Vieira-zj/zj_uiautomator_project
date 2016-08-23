package com.example.zhengjin.funsettingsuitest.testcasedemos;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.example.zhengjin.funsettingsuitest.Activity.ActivityDemo;
import com.example.zhengjin.funsettingsuitest.R;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;

/**
 * Created by zhengjin on 2016/6/1.
 *
 * Include UI test cases for demo activity by using Espresso.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestDemoActivity {

//    private final static String TAG = TestDemoActivity.class.getSimpleName();

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule(ActivityDemo.class);

    @Before
    public void setUp() {
    }

    @After
    public void clearUp() {
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test1ActDemoEditWithEmpty() {

        ViewInteraction edit = Espresso.onView(ViewMatchers.withId(R.id.editor_user_name));
        edit.perform(ViewActions.clearText());

        ViewInteraction btnOk = Espresso.onView(ViewMatchers.withId(R.id.button_ok));
        btnOk.perform(ViewActions.click());

        String result = "Please enter your name.";
        ViewInteraction text = Espresso.onView(ViewMatchers.withId(R.id.text_hello_msg));
        text.check(ViewAssertions.matches(ViewMatchers.withText(result)));
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test2ActDemoEditWithString() {

        String input = "ZhengJin";
        String result = "Hello, ZhengJin";

        ViewInteraction edit = Espresso.onView(ViewMatchers.withId(R.id.editor_user_name));
        edit.perform(ViewActions.clearText());
        edit.perform(ViewActions.typeText(input));

        ViewInteraction btnOk = Espresso.onView(ViewMatchers.withId(R.id.button_ok));
        btnOk.perform(ViewActions.click());

        ViewInteraction text = Espresso.onView(ViewMatchers.withId(R.id.text_hello_msg));
        text.check(ViewAssertions.matches(ViewMatchers.withText(result)));
    }
}
