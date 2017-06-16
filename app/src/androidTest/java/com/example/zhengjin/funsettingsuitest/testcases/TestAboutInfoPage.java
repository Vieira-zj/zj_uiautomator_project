package com.example.zhengjin.funsettingsuitest.testcases;

import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryAboutInfoTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryImageAndSoundSettingsTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryVersion30;
import com.example.zhengjin.funsettingsuitest.testrunner.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceAction;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMenu;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveLeft;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuiobjects.UiObjectsAboutInfo;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskAboutInfo;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2017/1/5.
 * <p>
 * Include test cases for system information page.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestAboutInfoPage {

    private final String[] ABOUT_ITEM_TITLES_ARR =
            {"产品信息", "网络信息", "系统版本", "问题反馈", "法律信息", "播控方", "应用版本"};
    private final String START_CATCH_LOG_MENU_BTN_TEXT = "开始抓日志";
    private final String DUMP_LOG_MENU_BTN_TEXT = "导入U盘";
    private final String[] LAW_ITEM_TITLES_ARR = {"版权保护投诉指引", "隐私政策", "用户协议"};

    private UiDevice mDevice;
    private UiActionsManager mAction;
    private UiObjectsAboutInfo mFunUiObjects;
    private TaskAboutInfo mTask;

    private String mMessage;

    @BeforeClass
    public static void classSetUp() {
        ShellUtils.stopProcessByPackageName(SETTINGS_PKG_NAME);
    }

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mFunUiObjects = UiObjectsAboutInfo.getInstance();
        mTask = TaskAboutInfo.getInstance();

        TaskLauncher.backToLauncher();
        mTask.openAboutInfoHomePage();
    }

    @After
    public void clearUp() {
        ShellUtils.stopProcessByPackageName(SETTINGS_PKG_NAME);
    }

    @Test
    @Category({CategoryAboutInfoTests.class})
    public void test01_01AboutInfoPageTitle() {
        UiObject2 pageTitle =
                mDevice.findObject(mFunUiObjects.getSettingsAboutInfoPageTitleSelector());

        mMessage = "Verify the About Info page title is enabled.";
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(pageTitle));
        mMessage = "Verify the title text on About Info page.";
        Assert.assertEquals(mMessage, mTask.ABOUT_INFO_PAGE_TEXT, pageTitle.getText());
    }

    @Test
    @Category({CategoryAboutInfoTests.class, CategoryVersion30.class})
    public void test01_02HiddenAppsVersionItemOnAboutInfo() {
        mMessage = "Verify the product info item is default focused on about info page.";
        UiObject2 productInfoItem =
                mDevice.findObject(mFunUiObjects.getProductInfoItemOnAboutSelector());
        Assert.assertTrue(mMessage, productInfoItem.isFocused());

        mMessage = "Verify the setting items count before hidden is show.";
        UiObject2 itemsContainer =
                mDevice.findObject(mFunUiObjects.getSettingsItemsContainerOnAboutSelector());
        List<UiObject2> settingItems = itemsContainer.findObjects(
                By.clazz(TestConstants.CLASS_RELATIVE_LAYOUT).maxDepth(1));
        final int initSize = 6;
        Assert.assertEquals(mMessage, initSize, settingItems.size());

        mAction.doMultipleDeviceActionsAndWait(new DeviceAction[]{
                new DeviceActionMoveLeft(), new DeviceActionMoveLeft(),
                new DeviceActionMoveUp(), new DeviceActionMoveLeft()}, 500L);
        ShellUtils.systemWaitByMillis(WAIT);

        mMessage = "Verify the setting items count after hidden is show.";
        settingItems = itemsContainer.findObjects(
                By.clazz(TestConstants.CLASS_RELATIVE_LAYOUT).maxDepth(1));
        Assert.assertEquals(mMessage, (initSize + 1), settingItems.size());
    }

    @Test
    @Category({CategoryAboutInfoTests.class, CategoryVersion30.class})
    public void test01_03OpenHiddenAppsVersionSubPage() {
        mAction.doMultipleDeviceActionsAndWait(new DeviceAction[]{
                new DeviceActionMoveLeft(), new DeviceActionMoveLeft(),
                new DeviceActionMoveUp(), new DeviceActionMoveLeft()}, 500L);
        ShellUtils.systemWaitByMillis(SHORT_WAIT);

        mMessage = "Verify the hidden apps version item is shown after short keys.";
        UiObject2 appsVersionItem =
                mDevice.findObject(mFunUiObjects.getAppsVersionItemOnAboutSelector());
        UiObject2 itemTitle =
                appsVersionItem.findObject(mFunUiObjects.getItemTitleOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[6], itemTitle.getText());

        mMessage = "Verify the title of apps version sub page.";
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[6]);
        UiObject2 title = mDevice.findObject(
                mFunUiObjects.getSettingsAboutInfoSubPageTitleSelector());
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[6], title.getText());
    }

    @Test
    @Category({CategoryAboutInfoTests.class})
    public void test02ProductInfoItemOnAboutInfo() {
        mMessage = "Verify the product info item on About Info page is enabled.";
        UiObject2 productInfoItem =
                mDevice.findObject(mFunUiObjects.getProductInfoItemOnAboutSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(productInfoItem));

        mMessage = "Verify the title of product info item.";
        UiObject2 itemTitle = mTask.getTitleInAboutInfoItem(productInfoItem);
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[0], itemTitle.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test03TitleAndQrCodeOnProductInfoSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[0]);

        mMessage = "Verify the tile on Product Info is enabled.";
        UiObject2 title =
                mDevice.findObject(mFunUiObjects.getSettingsAboutInfoSubPageTitleSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(title));

        mMessage = "Verify the text of title on Product Info.";
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[0], title.getText());

        mMessage = "Verify the QR code content.";
        UiObject2 qrCodeText =
                mDevice.findObject(mFunUiObjects.getQrCodeContentOnProductInfoSelector());
        Assert.assertEquals(mMessage, "扫描微信二维码，关注公众号", qrCodeText.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test04_01TvNameOnProductInfoSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[0]);

        mMessage = "Verify the device name item is enabled.";
        UiObject2 tvNameItem =
                mDevice.findObject(mFunUiObjects.getTvNameItemOnProductInfoSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(tvNameItem));

        mMessage = "Verify the device name item title.";
        UiObject2 itemTitle =
                tvNameItem.findObject(mFunUiObjects.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "设备名称：", itemTitle.getText());

        mMessage = "Verify the device name item value.";
        UiObject2 itemValue = tvNameItem.findObject(
                mFunUiObjects.getItemValueOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, RunnerProfile.deviceName, itemValue.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test04_02TvModelOnProductInfoSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[0]);

        mMessage = "Verify the device model item is enabled.";
        UiObject2 tvModelItem =
                mDevice.findObject(mFunUiObjects.getTvModelItemOnProductInfoSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(tvModelItem));

        mMessage = "Verify the device model item title.";
        UiObject2 itemTitle =
                tvModelItem.findObject(mFunUiObjects.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "产品型号：", itemTitle.getText());

        mMessage = "Verify the device model item value.";
        UiObject2 itemValue =
                tvModelItem.findObject(mFunUiObjects.getItemValueOnAboutInfoSubPageSelector());
        Pattern pattern = Pattern.compile(mTask.getRegExpDeviceModelInfo());
        Matcher matcher = pattern.matcher(itemValue.getText());
        Assert.assertTrue(mMessage, matcher.find());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test04_03TvRomSizeOnProductInfoSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[0]);

        mMessage = "Verify the device rom size item is enabled.";
        UiObject2 romSize = mDevice.findObject(mFunUiObjects.getRomSizeItemOnProductInfoSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(romSize));

        mMessage = "Verify the device rom size item title.";
        UiObject2 itemTitle =
                romSize.findObject(mFunUiObjects.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "本机容量：", itemTitle.getText());

        mMessage = "Verify the device rom item value.";
        UiObject2 itemValue =
                romSize.findObject(mFunUiObjects.getItemValueOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, mTask.getDeviceRomSizeInfo(), itemValue.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test04_04TvSeriesIdOnProductInfoSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[0]);

        mMessage = "Verify the device series id item is enabled.";
        UiObject2 seriesId =
                mDevice.findObject(mFunUiObjects.getSeriesIdItemOnProductInfoSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(seriesId));

        mMessage = "Verify the device series id item title.";
        UiObject2 itemTitle =
                seriesId.findObject(mFunUiObjects.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "设备序列号：", itemTitle.getText());

        mMessage = "Verify the device series id item value.";
        UiObject2 itemValue =
                seriesId.findObject(mFunUiObjects.getItemValueOnAboutInfoSubPageSelector());
        Assert.assertTrue(mMessage, mTask.isDeviceSeriesIdValid(itemValue.getText()));
    }

    @Test
    @Category({CategoryAboutInfoTests.class, CategoryVersion30.class})
    public void test04_05HiddenFullInfoOnProductInfo() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[0]);
        TestHelper.waitForUiObjectExist(mFunUiObjects.getSettingsAboutInfoSubPageTitleSelector());

        mMessage = "Verify hidden product information page is open after shorten keys.";
        mAction.doMultipleDeviceActionsAndWait(new DeviceAction[]{
                new DeviceActionMoveRight(), new DeviceActionMoveRight(),
                new DeviceActionMoveDown(), new DeviceActionMoveLeft()}, 500L);
        Assert.assertTrue(mMessage, TestHelper.waitForActivityOpenedByShellCmd(
                TestConstants.SETTINGS_PKG_NAME, ".about.SystemInfoActivity"));

        // verification 1
        mMessage = "Verify OS name title on hidden product information page.";
        UiObject2 osNameContainer = mDevice.findObject(
                mFunUiObjects.getOsNameContainterOnHiddenSystemInfoSelector());
        UiObject2 itemKey = osNameContainer.findObject(
                mFunUiObjects.getSystemItemKeyOnHiddenSystemInfoSelector());
        Assert.assertEquals(mMessage, "操作系统:", itemKey.getText());

        mMessage = "Verify OS name value on hidden product information page.";
        UiObject2 itemValue = osNameContainer.findObject(
                mFunUiObjects.getSystemItemValueOnHiddenSystemInfoSelector());
        Assert.assertEquals(mMessage, this.buildOsNameOnHiddenFullInfoPanel(), itemValue.getText());

        // verification 2
        mMessage = "Verify device model title on hidden product information page.";
        UiObject2 modelContainer = mDevice.findObject(
                mFunUiObjects.getDeviceModelContainerOnHiddenSystemInfoSelector());
        itemKey = modelContainer.findObject(
                mFunUiObjects.getSystemItemKeyOnHiddenSystemInfoSelector());
        Assert.assertEquals(mMessage, "设备型号:", itemKey.getText());

        mMessage = "Verify device model value on hidden product information page.";
        itemValue = modelContainer.findObject(
                mFunUiObjects.getSystemItemValueOnHiddenSystemInfoSelector());
        if (RunnerProfile.isPlatform938) {
            Assert.assertTrue(mMessage,
                    mTask.getFullDeviceModelInfo().contains(itemValue.getText()));
        } else {
            Pattern pattern = Pattern.compile(mTask.getRegExpDeviceModelInfo());
            Matcher matcher = pattern.matcher(itemValue.getText());
            Assert.assertTrue(mMessage, matcher.find());
        }
    }

    private String buildOsNameOnHiddenFullInfoPanel() {
        final String osNameTemplate = "FunUI 智能电视系统（安卓%s）";
        final String TextAndroidVersion4 = "4.4";
        final String TextAndroidVersion6 = "6.0";

        return String.format(osNameTemplate,
                Build.VERSION.RELEASE.startsWith("4") ? TextAndroidVersion4 : TextAndroidVersion6);
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test06NetworkInfoItemOnAboutInfo() {
        mMessage = "Verify the network info item on About Info page is enabled.";
        UiObject2 productItem =
                mDevice.findObject(mFunUiObjects.getNetworkInfoItemOnAboutSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(productItem));

        mMessage = "Verify the title of network info item.";
        UiObject2 itemText = mTask.getTitleInAboutInfoItem(productItem);
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[1], itemText.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test07NetworkStatusInfoOnSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[1]);

        // verification 1
        mMessage = "Verify the network info page title is enabled.";
        UiObject2 title =
                mDevice.findObject(mFunUiObjects.getSettingsAboutInfoSubPageTitleSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(title));

        mMessage = "Verify the text of title on network info.";
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[1], title.getText());

        // verification 2
        mMessage = "Verify the title of network status item.";
        UiObject2 statusItem =
                mDevice.findObject(mFunUiObjects.getNetworkStatusItemOnNetworkInfoSelector());
        UiObject2 itemTitle =
                statusItem.findObject(mFunUiObjects.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "已连接", itemTitle.getText());

        mMessage = "Verify the value of network status item.";
        UiObject2 itemValue =
                statusItem.findObject(mFunUiObjects.getItemValueOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "有线网络", itemValue.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test08NetworkWiredInfoOnSubPage() {
        TaskAboutInfo.NetworkInfo wiredInfo =
                mTask.getSysNetworkInfo(TaskAboutInfo.NetworkType.Wired);
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[1]);

        mMessage = "Verify the title of network IP address.";
        UiObject2 ipAddrItem =
                mDevice.findObject(mFunUiObjects.getNetworkIpAddrItemOnNetworkInfoSelector());
        UiObject2 ipItemTitle =
                ipAddrItem.findObject(mFunUiObjects.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "IP地址", ipItemTitle.getText());

        mMessage = "Verify the value of network IP address.";
        UiObject2 ipItemValue =
                ipAddrItem.findObject(mFunUiObjects.getItemValueOnAboutInfoSubPageSelector());
        Assert.assertTrue(mMessage, wiredInfo.getIpAddr().equalsIgnoreCase(ipItemValue.getText()));

        mMessage = "Verify the title of wired mac.";
        UiObject2 macIdItem =
                mDevice.findObject(mFunUiObjects.getNetworkWiredMacItemOnNetworkInfoSelector());
        UiObject2 macItemTitle =
                macIdItem.findObject(mFunUiObjects.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "有线网络mac地址", macItemTitle.getText());

        mMessage = "Verify the value of wired mac.";
        UiObject2 macItemValue =
                macIdItem.findObject(mFunUiObjects.getItemValueOnAboutInfoSubPageSelector());
        Assert.assertTrue(mMessage, wiredInfo.getMacId().equalsIgnoreCase(macItemValue.getText()));
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test09NetworkWirelessInfoMacOnSubPage() {
        TaskAboutInfo.NetworkInfo wiredInfo =
                mTask.getSysNetworkInfo(TaskAboutInfo.NetworkType.Wireless);
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[1]);

        mMessage = "Verify the title of wireless mac.";
        UiObject2 macIdItem =
                mDevice.findObject(mFunUiObjects.getNetworkWirelessMacItemOnNetworkInfoSelector());
        UiObject2 macItemTitle =
                macIdItem.findObject(mFunUiObjects.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "无线网络mac地址", macItemTitle.getText());

        mMessage = "Verify the value of wireless mac.";
        String macAddrWireless = macIdItem.findObject(
                mFunUiObjects.getItemValueOnAboutInfoSubPageSelector()).getText();
        Assert.assertTrue(mMessage, (wiredInfo.getMacId().equalsIgnoreCase(macAddrWireless)
                || "00:00:00:00:00:00".equals(macAddrWireless)));
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test11QuestionFeedbackItem() {
        mMessage = "Verify the feedback item on About Info page is enabled.";
        UiObject2 feedback =
                mDevice.findObject(mFunUiObjects.getQuestionFeedbackItemOnAboutSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(feedback));

        mMessage = "Verify the title of feedback item.";
        UiObject2 title = mTask.getTitleInAboutInfoItem(feedback);
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[3], title.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test12ContentAndMenuInQuestionFeedbackSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[3]);

        mMessage = "Verify the title on question feedback sub page.";
        UiObject2 subPageTitle =
                mDevice.findObject(mFunUiObjects.getSettingsAboutInfoSubPageTitleSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(subPageTitle));
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[3], subPageTitle.getText());

        mMessage = "Verify the content on question feedback sub page.";
        UiObject2 subPageContent =
                mDevice.findObject(mFunUiObjects.getQuestionFeedbackSubPageContentSelector());
        Assert.assertEquals(mMessage, "如需上传日志，请按 “菜单” 键", subPageContent.getText());

        mMessage = "Verify the bottom feedback menu is exist on sub page!";
        mAction.doDeviceActionAndWait(new DeviceActionMenu());
        Assert.assertTrue(mMessage,
                TestHelper.waitForUiObjectExist(mFunUiObjects.getBottomFeedbackMenuSelector()));
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test13ButtonsInFeedbackMenuOfSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[3]);
        mTask.openBottomFeedBackMenu();

        // verification 1
        mMessage = "Verify the catch log button in bottom menu is enabled.";
        UiObject2 catchLogBtn =
                mDevice.findObject(mFunUiObjects.getCatchLogButtonInFeedbackMenuSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(catchLogBtn));

        mMessage = "Verify the catch log button is default focused.";
        Assert.assertTrue(mMessage, catchLogBtn.isFocused());

        mMessage = "Verify the text of catch log button.";
        UiObject2 catchBtnText =
                catchLogBtn.findObject(mFunUiObjects.getFeedbackMenuButtonTextSelector());
        Assert.assertEquals(mMessage, START_CATCH_LOG_MENU_BTN_TEXT, catchBtnText.getText());

        // verification 2
        mMessage = "Verify the dump log to U disk button in bottom menu is enabled.";
        UiObject2 dumpLogBtn =
                mDevice.findObject(mFunUiObjects.getDumpLogToDiskButtonInFeedbackMenuSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(dumpLogBtn));

        mMessage = "Verify the text of dump log button.";
        UiObject2 dumpBtnText =
                dumpLogBtn.findObject(mFunUiObjects.getFeedbackMenuButtonTextSelector());
        Assert.assertEquals(mMessage, DUMP_LOG_MENU_BTN_TEXT, dumpBtnText.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test14StartAndStopCatchLogOnFeedbackSubPage() {
        final String STOP_CATCH_LOG_MENU_BTN_TEXT = "停止抓日志";

        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[3]);

        mMessage = "Verify feedback menu disappear after start catch log on feedback page.";
        mTask.openBottomFeedBackMenu();
        mTask.enterOnSpecifiedButtonInFeedbackMenu(START_CATCH_LOG_MENU_BTN_TEXT);
        Assert.assertFalse(mMessage,
                TestHelper.waitForUiObjectExist(mFunUiObjects.getBottomFeedbackMenuSelector()));

        mMessage = "Verify the stop catch log button in feedback menu.";
        mTask.openBottomFeedBackMenu();
        Assert.assertEquals(mMessage,
                STOP_CATCH_LOG_MENU_BTN_TEXT, this.getCatchLogBtnTextInFeedbackMenu());

        mMessage = "Verify feedback menu disappear after stop catch log on feedback page.";
        mTask.enterOnSpecifiedButtonInFeedbackMenu(STOP_CATCH_LOG_MENU_BTN_TEXT);
        Assert.assertFalse(mMessage,
                TestHelper.waitForUiObjectExist(mFunUiObjects.getBottomFeedbackMenuSelector()));

        mMessage = "Verify the start catch log button in feedback menu.";
        mTask.openBottomFeedBackMenu();
        Assert.assertEquals(mMessage,
                START_CATCH_LOG_MENU_BTN_TEXT, this.getCatchLogBtnTextInFeedbackMenu());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test15DumpLogToDiskOnFeedbackSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[3]);

        mMessage = "Verify feedback menu disappear after dump log to disk on feedback page.";
        mTask.openBottomFeedBackMenu();
        mTask.enterOnSpecifiedButtonInFeedbackMenu(DUMP_LOG_MENU_BTN_TEXT);
        Assert.assertFalse(mMessage,
                TestHelper.waitForUiObjectExist(mFunUiObjects.getBottomFeedbackMenuSelector()));

        mMessage = "Verify back to question feedback home page.";
        UiObject2 title = mDevice.findObject(mFunUiObjects.getSettingsAboutInfoPageTitleSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(title));
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test17SystemVersionItemOnAboutInfo() {
        mMessage = "Verify the System Version item on about info page.";
        UiObject2 sysVersion =
                mDevice.findObject(mFunUiObjects.getSystemVersionInfoItemOnAboutSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(sysVersion));

        mMessage = "Verify the system version title text.";
        UiObject2 itemTitle =
                sysVersion.findObject(mFunUiObjects.getItemTitleOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, "系统版本", itemTitle.getText());

        mMessage = "Verify the system version number value.";
        UiObject2 itemValue =
                sysVersion.findObject(mFunUiObjects.getItemValueOnAboutInfoPageSelector());
        Assert.assertTrue(mMessage, itemValue.getText().contains(mTask.getSystemVersionInfo()));
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test18PlayControllerItemOnAboutInfo() {
        mMessage = "Verify the play controller item on about info page.";
        UiObject2 controller =
                mDevice.findObject(mFunUiObjects.getPlayControllerItemOnAboutSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(controller));

        mMessage = "Verify the play controller title text.";
        UiObject2 itemTitle =
                controller.findObject(mFunUiObjects.getItemTitleOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, "播控方", itemTitle.getText());

        mMessage = "Verify the play controller value.";
        UiObject2 itemValue =
                controller.findObject(mFunUiObjects.getItemValueOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, "BesTV", itemValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test21LawInfoItemOnAboutInfo() {
        mMessage = "Verify the law info item on about info page.";
        UiObject2 law = mDevice.findObject(mFunUiObjects.getLawInfoItemOnAboutSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(law));

        mMessage = "Verify the law info title text.";
        UiObject2 title = law.findObject(mFunUiObjects.getItemTitleOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[4], title.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test22TitleAndItemsOnLawInfoSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[4]);

        // verification 1
        mMessage = "Verify the title on Law Info sub page is enabled.";
        UiObject2 title =
                mDevice.findObject(mFunUiObjects.getSettingsAboutInfoSubPageTitleSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(title));

        mMessage = "Verify the title text on Law Info sub page.";
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[4], title.getText());

        // verification 2
        mMessage = "Verify the Copy Right Protect item title text.";
        UiObject2 copyRightItem =
                mDevice.findObject(mFunUiObjects.getCopyRightProtectItemOnLawInfoSelector());
        UiObject2 titleCopyRight =
                copyRightItem.findObject(mFunUiObjects.getItemTitleOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, LAW_ITEM_TITLES_ARR[0], titleCopyRight.getText());

        mMessage = "Verify the Privacy Policy item title text.";
        UiObject2 privacyItem =
                mDevice.findObject(mFunUiObjects.getPrivacyPolicyItemOnLawInfoSelector());
        UiObject2 titlePrivacy =
                privacyItem.findObject(mFunUiObjects.getItemTitleOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, LAW_ITEM_TITLES_ARR[1], titlePrivacy.getText());

        mMessage = "Verify the user agreement item title text.";
        UiObject2 agreementItem =
                mDevice.findObject(mFunUiObjects.getUserAgreementItemOnLawInfoSelector());
        UiObject2 titleAgreement =
                agreementItem.findObject(mFunUiObjects.getItemTitleOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, LAW_ITEM_TITLES_ARR[2], titleAgreement.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test23TitleAndContentOnCopyRightProtectSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[4]);
        mTask.openSpecifiedAboutInfoItemSubPage(LAW_ITEM_TITLES_ARR[0]);

        mMessage = "Verify the title is enabled on Copy Right Protect sub page.";
        UiObject2 title =
                mDevice.findObject(mFunUiObjects.getSettingsAboutInfoSubPageTitleSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(title));

        mMessage = "Verify the title text on Copy Right Protect sub page.";
        Assert.assertEquals(mMessage, LAW_ITEM_TITLES_ARR[0], title.getText());

        mMessage = "Verify the content web view is exist.";
        UiObject2 contentWebView =
                mDevice.findObject(mTask.getContentWebViewOnLawItemSubPageSelector());
        Assert.assertNotNull(mMessage, contentWebView);
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test24TitleAndContentOnPrivacyPolicySubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[4]);
        mTask.openSpecifiedAboutInfoItemSubPage(LAW_ITEM_TITLES_ARR[1]);

        mMessage = "Verify the title is enabled on Privacy Policy sub page.";
        UiObject2 title =
                mDevice.findObject(mFunUiObjects.getSettingsAboutInfoSubPageTitleSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(title));

        mMessage = "Verify the title text on Privacy Policy sub page.";
        Assert.assertEquals(mMessage, LAW_ITEM_TITLES_ARR[1], title.getText());

        mMessage = "Verify the content web view is exist.";
        UiObject2 contentWebView =
                mDevice.findObject(mTask.getContentWebViewOnLawItemSubPageSelector());
        Assert.assertNotNull(mMessage, contentWebView);
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test25TitleAndContentOnUserAgreementSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[4]);
        mTask.openSpecifiedAboutInfoItemSubPage(LAW_ITEM_TITLES_ARR[2]);

        mMessage = "Verify the title is enabled on User Agreement sub page.";
        UiObject2 title =
                mDevice.findObject(mFunUiObjects.getSettingsAboutInfoSubPageTitleSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(title));

        mMessage = "Verify the title text on User Agreement sub page.";
        Assert.assertEquals(mMessage, LAW_ITEM_TITLES_ARR[2], title.getText());

        mMessage = "Verify the content web view is exist.";
        UiObject2 contentWebView =
                mDevice.findObject(mTask.getContentWebViewOnLawItemSubPageSelector());
        Assert.assertNotNull(mMessage, contentWebView);
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test99ClearUpAfterAllTestCasesDone() {
        mTask.destroyInstance();
        mFunUiObjects.destroyInstance();
    }

    private String getCatchLogBtnTextInFeedbackMenu() {
        UiObject2 CatchBtn =
                mDevice.findObject(mFunUiObjects.getCatchLogButtonInFeedbackMenuSelector());
        return CatchBtn.findObject(mFunUiObjects.getFeedbackMenuButtonTextSelector()).getText();
    }

}
