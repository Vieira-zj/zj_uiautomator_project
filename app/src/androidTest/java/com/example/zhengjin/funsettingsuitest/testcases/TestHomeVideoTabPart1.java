package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

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

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CHILDREN_CARD_TEXT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.FILM_CARD_TEXT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.TV_SERIAL_CARD_TEXT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.VARIETY_CARD_TEXT;

/**
 * Created by Vieira on 2016/7/4.
 *
 * Include the test cases to test launcher home page basic functions for 24 x 7.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestHomeVideoTabPart1 {

    private static UiActionsManager action;
    private UiDevice mDevice;


    @BeforeClass
    public static void beforeClass() {

        action = UiActionsManager.getInstance();
    }

    @Before
    public void setUp() {

        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        TaskLauncher.backToLauncher(mDevice);
    }

    @After
    public void clearUp() {
        TestHelper.doScreenCapture(mDevice);
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class, Category24x7LauncherTests.class})
    public void test11OpenFilmCardOfLeftArea() {

        UiObject2 filmCard =
                TaskHomeVideoTab.findSpecifiedCardFromLeftAreaByText(mDevice, FILM_CARD_TEXT);
        action.doClickActionAndWait(filmCard);
        action.doDeviceActionAndWait(new DeviceActionEnter());

        // verify tab text
        List<UiObject2> listTabText = TestHelper.waitForMultipleUiObjectsVisibleAndReturn(
                mDevice, TaskHomeVideoTab.getAllTabTextOfVideoRecommendPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listTabText);

        // verify card title
        UiObject2 tmpContainer = TestHelper.waitForUiObjectVisibleAndReturn(
                mDevice, TaskHomeVideoTab.getCardsContainerOfVideoRecommendPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiContainer(tmpContainer);
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class, Category24x7LauncherTests.class})
    public void test12OpenTvSerialCardOfLeftArea() {

        UiObject2 tvSerialCard =
                TaskHomeVideoTab.findSpecifiedCardFromLeftAreaByText(mDevice, TV_SERIAL_CARD_TEXT);
        action.doClickActionAndWait(tvSerialCard);
        action.doDeviceActionAndWait(new DeviceActionEnter());

        // verify tab text
        List<UiObject2> listTabText = TestHelper.waitForMultipleUiObjectsVisibleAndReturn(
                mDevice, TaskHomeVideoTab.getAllTabTextOfVideoRecommendPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listTabText);

        // verify card main title
        List<UiObject2> listMainTitles = TestHelper.waitForMultipleUiObjectsVisibleAndReturn(
                mDevice, TaskHomeVideoTab.getAllCardsMainTitleOfVideoRecommendPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listMainTitles);

        // verify card sub title
        List<UiObject2> listSubTitles = mDevice.findObjects(
                TaskHomeVideoTab.getAllCardsSubTitleOfVideoRecommendPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listSubTitles);
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class, Category24x7LauncherTests.class})
    public void test13OpenChildrenCardOfLeftArea() {

        UiObject2 childrenCard =
                TaskHomeVideoTab.findSpecifiedCardFromLeftAreaByText(mDevice, CHILDREN_CARD_TEXT);
        action.doClickActionAndWait(childrenCard);
        action.doDeviceActionAndWait(new DeviceActionEnter());

        // verify tab text
        List<UiObject2> tabTextList = TestHelper.waitForMultipleUiObjectsVisibleAndReturn(
                mDevice, TaskHomeVideoTab.getAllTabTextOfVideoRecommendPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(tabTextList);

        // verify card title
        List<UiObject2> cardTitleList = TestHelper.waitForMultipleUiObjectsVisibleAndReturn(
                mDevice, TaskHomeVideoTab.getAllCardsMainTitleOfVideoRecommendPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(cardTitleList);
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class, Category24x7LauncherTests.class})
    public void test14OpenVarietyCardOfLeftArea() {

        UiObject2 varietyCard =
                TaskHomeVideoTab.findSpecifiedCardFromLeftAreaByText(mDevice, VARIETY_CARD_TEXT);
        action.doClickActionAndWait(varietyCard);
        action.doDeviceActionAndWait(new DeviceActionEnter());

        // verify tab text
        List<UiObject2> listTabText = TestHelper.waitForMultipleUiObjectsVisibleAndReturn(
                mDevice, TaskHomeVideoTab.getAllTabTextOfVideoRecommendPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listTabText);

        // verify card title
        UiObject2 tmpContainer = TestHelper.waitForUiObjectVisibleAndReturn(
                mDevice, TaskHomeVideoTab.getCardsContainerOfVideoRecommendPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiContainer(tmpContainer);
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class, Category24x7LauncherTests.class})
    public void test21OpenLatestTvSerialOfRightArea() {

        UiObject2 card = TaskHomeVideoTab.findSpecifiedCardFromRightAreaByText(mDevice, "跟播");
        action.doClickActionAndWait(card);
        action.doDeviceActionAndWait(new DeviceActionEnter());

        // verify title
        UiObject2 title = TestHelper.waitForUiObjectVisibleAndReturn(
                mDevice, TaskHomeVideoTab.getTitleTextOfVideoDetailsPageSelector());
        Assert.assertFalse("Verify the title of video details page is NOT empty.",
                "".equals(title.getText()));

        // verify each card of bottom related video list
        UiObject2 relatedVideoList = TestHelper.waitForUiObjectVisibleAndReturn(
                mDevice, TaskHomeVideoTab.getRelatedVideoListOfVideoDetailsPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiContainer(relatedVideoList);
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class, Category24x7LauncherTests.class})
    public void test22OpenDailyNewsOfRightArea() {
        // TODO: 2016/7/5     
    }
    

}
