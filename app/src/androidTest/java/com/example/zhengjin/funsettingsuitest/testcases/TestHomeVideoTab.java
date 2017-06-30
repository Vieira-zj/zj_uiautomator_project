package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryHomeVideoTabTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryVersion30;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceAction;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveLeft;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuiobjects.UiObjectsVideoHomeTab;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskVideoHomeTab;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;
import com.example.zhengjin.funsettingsuitest.utils.StringUtils;

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

/**
 * Created by Vieira on 2016/7/4.
 * <p>
 * Include the test cases to test the tabs of launcher home.
 * Test cases for video 2nd level sub-page are unstable.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestHomeVideoTab {

    private final UiDevice mDevice = TestConstants.GetUiDeviceInstance();
    private final UiActionsManager mAction = UiActionsManager.getInstance();
    private final UiObjectsVideoHomeTab mFunUiObjects = UiObjectsVideoHomeTab.getInstance();
    private final TaskVideoHomeTab mTask = TaskVideoHomeTab.getInstance();

    private final String[] HOME_VIDEO_TABS = {"电影", "电视剧", "少儿", "综艺", "跟播", "7日新增"};

    @Before
    public void setUp() {
        TaskLauncher.backToLauncher();
    }

    @After
    public void clearUp() {
        ShellUtils.takeScreenCapture(mDevice);
    }

    @Test
    @Ignore
    @Category({CategoryHomeVideoTabTests.class})
    // unstable case
    public void test11OpenFilmCardOfLeftArea() {
        UiObject2 filmCard =
                mTask.getSpecifiedCardFromHomeLeftAreaByText(HOME_VIDEO_TABS[0]);
        mAction.doClickActionAndWait(filmCard);
        mAction.doDeviceActionAndWaitForIdle(new DeviceActionEnter());

        // verify tab text
        List<UiObject2> listTabText = TestHelper.waitForUiObjectsExistAndReturn(
                mFunUiObjects.getTopTabTextOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listTabText);

        // verify card title
        UiObject2 tmpContainer = TestHelper.waitForUiObjectExistAndReturn(
                mFunUiObjects.getCardContainerOfVideoRecommendPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiContainer(tmpContainer);
    }

    @Test
    @Ignore
    @Category({CategoryHomeVideoTabTests.class})
    // unstable case
    public void test12OpenTvSerialCardOfLeftArea() {
        UiObject2 tvSerialCard =
                mTask.getSpecifiedCardFromHomeLeftAreaByText(HOME_VIDEO_TABS[1]);
        mAction.doClickActionAndWait(tvSerialCard);
        mAction.doDeviceActionAndWaitForIdle(new DeviceActionEnter());

        // verify tab text
        List<UiObject2> listTabText = TestHelper.waitForUiObjectsExistAndReturn(
                mFunUiObjects.getTopTabTextOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listTabText);

        // verify card main title
        List<UiObject2> listMainTitles = TestHelper.waitForUiObjectsExistAndReturn(
                mFunUiObjects.getCardMainTitleOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listMainTitles);

        // verify card sub title
        List<UiObject2> listSubTitles = mDevice.findObjects(
                mFunUiObjects.getCardSubTitleOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listSubTitles);
    }

    @Test
    @Ignore
    @Category({CategoryHomeVideoTabTests.class})
    // unstable case
    public void test13OpenChildrenCardOfLeftArea() {
        UiObject2 childrenCard =
                mTask.getSpecifiedCardFromHomeLeftAreaByText(HOME_VIDEO_TABS[2]);
        mAction.doClickActionAndWait(childrenCard);
        mAction.doDeviceActionAndWaitForIdle(new DeviceActionEnter());

        // verify tab text
        List<UiObject2> tabTextList = TestHelper.waitForUiObjectsExistAndReturn(
                mFunUiObjects.getTopTabTextOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(tabTextList);

        // verify card main title
        List<UiObject2> cardTitleList = TestHelper.waitForUiObjectsExistAndReturn(
                mFunUiObjects.getCardMainTitleOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(cardTitleList);
    }

    @Test
    @Ignore
    @Category({CategoryHomeVideoTabTests.class})
    // unstable case
    public void test14OpenVarietyCardOfLeftArea() {
        UiObject2 varietyCard =
                mTask.getSpecifiedCardFromHomeLeftAreaByText(HOME_VIDEO_TABS[3]);
        mAction.doClickActionAndWait(varietyCard);
        mAction.doDeviceActionAndWaitForIdle(new DeviceActionEnter());

        // verify tab text
        List<UiObject2> listTabText = TestHelper.waitForUiObjectsExistAndReturn(
                mFunUiObjects.getTopTabTextOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listTabText);

        // verify card title
        UiObject2 tmpContainer = TestHelper.waitForUiObjectExistAndReturn(
                mFunUiObjects.getCardContainerOfVideoRecommendPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiContainer(tmpContainer);
    }

    @Test
    @Ignore
    @Category({CategoryHomeVideoTabTests.class})
    // UI changes
    public void test21OpenFollowingLatestTvSerialOfRightArea() {
        UiObject2 card = mTask.getSpecifiedCardFromHomeRightAreaByText(HOME_VIDEO_TABS[4]);
        mAction.doClickActionAndWait(card);
        mAction.doDeviceActionAndWaitForIdle(new DeviceActionEnter());

        // verify title
        UiObject2 title = TestHelper.waitForUiObjectExistAndReturn(
                mFunUiObjects.getTitleTextOfVideoDetailsPageSelector());
        Assert.assertFalse("Verify the title of video details page is NOT empty.",
                StringUtils.isBlank(title.getText()));

        // verify each card of bottom related video list
        UiObject2 relatedVideoList = TestHelper.waitForUiObjectExistAndReturn(
                mFunUiObjects.getRelatedVideoListOfVideoDetailsPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiContainer(relatedVideoList);
    }

    @Test
    @Ignore
    @Category({CategoryHomeVideoTabTests.class})
    public void test22OpenDailyNewsOfRightArea() {
        // it's a dynamic(not idle) page, and UI elements cannot be dumped by uiautomator
    }

    @Test
    @Ignore
    @Category({CategoryHomeVideoTabTests.class})
    // UI changes
    public void test23OpenNewlyUpdatesIn7DaysOfRightArea() {
        UiObject2 card = mTask.getSpecifiedCardFromHomeRightAreaByText(HOME_VIDEO_TABS[5]);
        mAction.doClickActionAndWait(card);
        mAction.doDeviceActionAndWaitForIdle(new DeviceActionEnter());

        // verify tab text
        List<UiObject2> listTabText = TestHelper.waitForUiObjectsExistAndReturn(
                mFunUiObjects.getTopTabTextOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listTabText);

        // verify card main title
        List<UiObject2> listCardTitle = TestHelper.waitForUiObjectsExistAndReturn(
                mFunUiObjects.getCardMainTitleOfVideoSubPageSelector());
        TestHelper.verifyEachTextViewHasTextInUiCollection(listCardTitle);
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class, CategoryVersion30.class})
    public void test31OpenFactoryMenuFromSignalSourceDialog() {
        mTask.openSignalSourceDialog();

        mAction.doChainedDeviceActionAndWait(new DeviceActionMoveDown())
                .doDeviceActionAndWait(new DeviceActionMoveLeft());
        UiObject2 hdmi1 = mDevice.findObject(
                mFunUiObjects.getHdmi1ItemFromSignalSourceDialogSelector());
        Assert.assertTrue("Focus on hdmi1 signal source.", hdmi1.isFocused());

        mAction.doMultipleDeviceActionsAndWait(new DeviceAction[]{
                new DeviceActionMoveLeft(), new DeviceActionMoveLeft(),
                new DeviceActionMoveUp(), new DeviceActionMoveRight()}, 500L);

        Assert.assertTrue("Verify factory mode activity is opened.",
                TestHelper.waitForActivityOpenedByShellCmd(
                        "mstar.factorymenu.ui",
                        "mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity"));

        ShellUtils.systemWaitByMillis(TestConstants.SHORT_WAIT);
        UiObject2 menu = mDevice.findObject(mFunUiObjects.getFactoryMenuFlipperSelector());
        Assert.assertTrue("Verify factory menu flipper is enabled.",
                TestHelper.waitForUiObjectEnabled(menu));
    }

    @Test
    @Category({CategoryHomeVideoTabTests.class})
    public void test99ClearUpAfterAllTestCasesDone() {
        TaskVideoHomeTab.destroyInstance();
        UiObjectsVideoHomeTab.destroyInstance();
    }

}
