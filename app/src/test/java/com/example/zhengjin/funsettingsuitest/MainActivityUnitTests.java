package com.example.zhengjin.funsettingsuitest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by zhengjin on 2016/5/31.
 */
public final class MainActivityUnitTests {

    MainActivity ma = null;

    @Before
    public void setUp() {
        System.out.println("In MainActivityUnitTest setup.");

        ma = new MainActivity();
    }

    @After
    public void clearUp() {
        System.out.println("In MainActivityUnitTest tearDown.");
    }

    @Test
    public void testBuildHelloMessageNull() {
        final String input = null;
        final String results = "Pls enter your name in edit text.";

        Assert.assertEquals(results, ma.BuildHelloMessage(input));
    }

    @Test
    public void testBuildHelloMessageEmpty() {
        final String input = "";
        final String results = "Pls enter your name in edit text.";

        Assert.assertEquals(results, ma.BuildHelloMessage(input));
    }

    @Test
    public void testBuildHelloMessageStr() {
        final String input = "ZJ";
        final String results = "Hello, ZJ";

        Assert.assertEquals(results, ma.BuildHelloMessage(input));
    }

}
