package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryVideoHomeTabTests;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskVideoHomeTab;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CHILDREN_CARD_TEXT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.FILM_CARD_TEXT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.FOLLOWING_TV_SERIAL_TEXT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.NEWLY_ADD_IN_7_DAYS_TEXT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.TV_SERIAL_CARD_TEXT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.VARIETY_CARD_TEXT;

/**
 * Created by Vieira on 2016/7/4.
 *
 * Include the test cases to test the tabs of launcher home.
 * These test cases are unstable (video 2nd level page).
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestVideoHomeTab {

    private UiDevice mDevice;
    private UiActionsManager mAction;
    private TaskVideoHomeTab mTask;

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mTask = TaskVideoHomeTab.getInstance();

        TaskLauncher.backToLauncher(mDevice);
    }

    @After
    public void clearUp() {
        ShellUtils.takeScreenCapture(mDevice);
    }

    @Test
    @Category({CategoryVideoHomeTabTests.class})
    public void test11OpenFilmCardOfLeftArea() {
        UiObject2 filmCard =
                mTask.findSpecifiedCardFromLeftAreaByText(mDevice, FILM_CARD_TEXT);
        mAction.doClickActionAndWait(filmCard);
        mAction.doDeviceActionAndWaitForIdle(new DeviceActionEnter());

        // verify tab text
        List<UiObject2> listTabText = TestHelper.waitForMultipleUiObjectsVisibleAndReturn(
                mDevice, mTask.getAllTabTextOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listTabText);

        // verify card title
        UiObject2 tmpContainer = TestHelper.waitForUiObjectVisibleAndReturn(
                mDevice, mTask.getCardsContainerOfVideoRecommendPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiContainer(tmpContainer);
    }

    @Test
    @Category({CategoryVideoHomeTabTests.class})
    public void test12OpenTvSerialCardOfLeftArea() {
        UiObject2 tvSerialCard =
                mTask.findSpecifiedCardFromLeftAreaByText(mDevice, TV_SERIAL_CARD_TEXT);
        mAction.doClickActionAndWait(tvSerialCard);
        mAction.doDeviceActionAndWaitForIdle(new DeviceActionEnter());

        // verify tab text
        List<UiObject2> listTabText = TestHelper.waitForMultipleUiObjectsVisibleAndReturn(
                mDevice, mTask.getAllTabTextOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listTabText);

        // verify card main title
        List<UiObject2> listMainTitles = TestHelper.waitForMultipleUiObjectsVisibleAndReturn(
                mDevice, mTask.getAllCardsMainTitleOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listMainTitles);

        // verify card sub title
        List<UiObject2> listSubTitles = mDevice.findObjects(
                mTask.getAllCardsSubTitleOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listSubTitles);
    }

    @Test
    @Category({CategoryVideoHomeTabTests.class})
    public void test13OpenChildrenCardOfLeftArea() {
        UiObject2 childrenCard =
                mTask.findSpecifiedCardFromLeftAreaByText(mDevice, CHILDREN_CARD_TEXT);
        mAction.doClickActionAndWait(childrenCard);
        mAction.doDeviceActionAndWaitForIdle(new DeviceActionEnter());

        // verify tab text
        List<UiObject2> tabTextList = TestHelper.waitForMultipleUiObjectsVisibleAndReturn(
                mDevice, mTask.getAllTabTextOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(tabTextList);

        // verify card main title
        List<UiObject2> cardTitleList = TestHelper.waitForMultipleUiObjectsVisibleAndReturn(
                mDevice, mTask.getAllCardsMainTitleOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(cardTitleList);
    }

    @Test
    @Category({CategoryVideoHomeTabTests.class})
    public void test14OpenVarietyCardOfLeftArea() {
        UiObject2 varietyCard =
                mTask.findSpecifiedCardFromLeftAreaByText(mDevice, VARIETY_CARD_TEXT);
        mAction.doClickActionAndWait(varietyCard);
        mAction.doDeviceActionAndWaitForIdle(new DeviceActionEnter());

        // verify tab text
        List<UiObject2> listTabText = TestHelper.waitForMultipleUiObjectsVisibleAndReturn(
                mDevice, mTask.getAllTabTextOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listTabText);

        // verify card title
        UiObject2 tmpContainer = TestHelper.waitForUiObjectVisibleAndReturn(
                mDevice, mTask.getCardsContainerOfVideoRecommendPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiContainer(tmpContainer);
    }

    @Test
    @Category({CategoryVideoHomeTabTests.class})
    public void test21OpenFollowingLatestTvSerialOfRightArea() {
        UiObject2 card = mTask.findSpecifiedCardFromRightAreaByText(
                mDevice, FOLLOWING_TV_SERIAL_TEXT);
        mAction.doClickActionAndWait(card);
        mAction.doDeviceActionAndWaitForIdle(new DeviceActionEnter());

        // verify title
        UiObject2 title = TestHelper.waitForUiObjectVisibleAndReturn(
                mDevice, mTask.getTitleTextOfVideoDetailsPageSelector());
        Assert.assertFalse("Verify the title of video details page is NOT empty.",
                "".equals(title.getText()));

        // verify each card of bottom related video list
        UiObject2 relatedVideoList = TestHelper.waitForUiObjectVisibleAndReturn(
                mDevice, mTask.getRelatedVideoListOfVideoDetailsPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiContainer(relatedVideoList);
    }

    @Ignore
    @Category({CategoryVideoHomeTabTests.class})
    public void test22OpenDailyNewsOfRightArea() {
        // it's a dynamic(not idle) page, and UI elements cannot be dumped by uiautomator
    }

    @Test
    @Category({CategoryVideoHomeTabTests.class})
    public void test23OpenNewlyUpdatesIn7DaysOfRightArea() {
        UiObject2 card = mTask.findSpecifiedCardFromRightAreaByText(
                mDevice, NEWLY_ADD_IN_7_DAYS_TEXT);
        mAction.doClickActionAndWait(card);
        mAction.doDeviceActionAndWaitForIdle(new DeviceActionEnter());

        // verify tab text
        List<UiObject2> listTabText = TestHelper.waitForMultipleUiObjectsVisibleAndReturn(
                mDevice, mTask.getAllTabTextOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listTabText);

        // verify card main title
        List<UiObject2> listCardTitle = TestHelper.waitForMultipleUiObjectsVisibleAndReturn(
                mDevice, mTask.getAllCardsMainTitleOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listCardTitle);
    }

    @Test
    @Category({CategoryVideoHomeTabTests.class})
    public void test99ClearUpAfterAllTestCasesDone() { mTask.destroyInstance(); }

}
