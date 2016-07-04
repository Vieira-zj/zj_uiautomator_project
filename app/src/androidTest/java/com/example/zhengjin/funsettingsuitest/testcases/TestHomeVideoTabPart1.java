package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;

import com.example.zhengjin.funsettingsuitest.testcategory.Category24x7LauncherTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryHomeVideoTabTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskHomeVideoTab;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;

/**
 * Created by Vieira on 2016/7/4.
 *
 * Include the test cases to test launcher home page basic functions for 24 x 7.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestHomeVideoTabPart1 {

    private final static String FILM_CARD_TEXT = "电影";
    private final static String TV_SERIAL_CARD_TEXT = "电视剧";

    private static UiActionsManager action;
    private static UiDevice device;

    private UiObject2 mContainer;

    @BeforeClass
    public static void beforeClass() {

        action = UiActionsManager.getInstance();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Before
    public void setUp() {

        TaskLauncher.backToLauncher(device);
        mContainer = device.findObject(TaskHomeVideoTab.getCardsContainerOfFilmDetailsSelector());
    }

    @After
    public void clearUp() {
//        TestHelper.doScreenCapture(device);
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class, Category24x7LauncherTests.class})
    public void test11OpenFilmCardBottom() throws UiObjectNotFoundException {

        List<UiObject2> filmCards = mContainer.findObjects(By.text(FILM_CARD_TEXT));
        Assert.assertEquals("Verify there are 2 film cards.", 2, filmCards.size());

        action.doClickActionAndWait(filmCards.get(1));
        action.doDeviceActionAndWait(new DeviceActionEnter(), LONG_WAIT);

        UiObject2 tempContainer = TestHelper.waitForUiObjectVisibleAndReturn(
                device, TaskHomeVideoTab.getCardsContainerOfFilmDetailsSelector());
        TaskHomeVideoTab.verifyAllTextViewHasTextOfContainer(tempContainer);
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class, Category24x7LauncherTests.class})
    public void test12OpenFilmCardLeft() throws UiObjectNotFoundException {

        List<UiObject2> filmCards = mContainer.findObjects(By.text(FILM_CARD_TEXT));
        Assert.assertEquals("Verify there are 2 film cards.", 2, filmCards.size());

        action.doClickActionAndWait(filmCards.get(0));
        action.doDeviceActionAndWait(new DeviceActionEnter());

        UiObject2 tempContainer = TestHelper.waitForUiObjectVisibleAndReturn(
                device, TaskHomeVideoTab.getCardsContainerOfSpeicialSubjectSelector());
        TaskHomeVideoTab.verifyAllTextViewHasTextOfContainer(tempContainer);
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class, Category24x7LauncherTests.class})
    public void test13OpenTvSerialCardBottom() throws UiObjectNotFoundException {

        List<UiObject2> tvSerialCards = mContainer.findObjects(By.text(TV_SERIAL_CARD_TEXT));
        Assert.assertEquals("Verify there are 2 tv serial cards.", 2, tvSerialCards.size());

        // TODO: 2016/7/4
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class, Category24x7LauncherTests.class})
    public void test14OpenTvSerialCardLeft() throws UiObjectNotFoundException {

        // TODO: 2016/7/4
    }
}
