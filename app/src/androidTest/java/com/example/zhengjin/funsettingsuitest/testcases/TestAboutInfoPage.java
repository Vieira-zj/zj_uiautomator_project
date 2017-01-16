package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testcategory.CategoryAboutInfoTests;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryImageAndSoundSettingsTests;
import com.example.zhengjin.funsettingsuitest.testsuites.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMenu;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskAboutInfo;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskLauncher;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
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

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;

/**
 * Created by zhengjin on 2017/1/5.
 * <p>
 * Include test cases for system information page.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestAboutInfoPage {

    private UiDevice mDevice;
    private UiActionsManager mAction;
    private TaskAboutInfo mTask;
    private String mMessage;

    private final String[] ABOUT_ITEM_TITLES_ARR =
            {"产品信息", "网络信息", "系统版本", "问题反馈", "法律信息", "播控方"};
    private final String START_CATCH_LOG_MENU_BTN_TEXT = "开始抓日志";
    private final String DUMP_LOG_MENU_BTN_TEXT = "导入U盘";
    private final String[] LAW_ITEM_TITLES_ARR = {"版权保护投诉指引", "隐私政策", "用户协议"};

    @BeforeClass
    public static void classSetUp() {
        ShellUtils.stopProcess(SETTINGS_PKG_NAME);
    }

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mTask = TaskAboutInfo.getInstance();

        TaskLauncher.backToLauncher();
        mTask.openAboutInfoHomePage();
    }

    @After
    public void clearUp() {
        ShellUtils.stopProcess(SETTINGS_PKG_NAME);
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test01AboutInfoPageTitle() {
        UiObject2 pageTitle = mDevice.findObject(mTask.getSettingsAboutInfoPageTitleSelector());

        mMessage = "Verify the About Info page title is enabled.";
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(pageTitle));
        mMessage = "Verify the title text on About Info page.";
        Assert.assertEquals(mMessage, mTask.ABOUT_INFO_PAGE_TEXT, pageTitle.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test02ProductInfoItemOnAboutInfo() {
        mMessage = "Verify the product info item on About Info page is enabled.";
        UiObject2 productItem = mDevice.findObject(mTask.getProductInfoItemOnAboutSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(productItem));

        mMessage = "Verify the title of product info item.";
        UiObject2 itemTitle = mTask.getTitleInAboutInfoItem(productItem);
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[0], itemTitle.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test03TitleAndQrCodeOnProductInfoSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[0]);

        mMessage = "Verify the tile on Product Info is enabled.";
        UiObject2 title = mDevice.findObject(mTask.getSettingsAboutInfoSubPageTitleSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(title));

        mMessage = "Verify the text of title on Product Info.";
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[0], title.getText());

        mMessage = "Verify the QR code content.";
        UiObject2 qrCodeText = mDevice.findObject(mTask.getQrCodeContentOnProductInfoSelector());
        Assert.assertEquals(mMessage, "扫描微信二维码，关注公众号", qrCodeText.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test04_01TvNameOnProductInfoSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[0]);

        mMessage = "Verify the device name item is enabled.";
        UiObject2 tvNameItem = mDevice.findObject(mTask.getTvNameItemOnProductInfoSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(tvNameItem));

        mMessage = "Verify the device name item title.";
        UiObject2 itemTitle = tvNameItem.findObject(mTask.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "设备名称：", itemTitle.getText());

        mMessage = "Verify the device name item value.";
        UiObject2 itemValue = tvNameItem.findObject(mTask.getItemValueOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, RunnerProfile.deviceName, itemValue.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test04_02TvModelOnProductInfoSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[0]);

        mMessage = "Verify the device model item is enabled.";
        UiObject2 tvModelItem = mDevice.findObject(mTask.getTvModelItemOnProductInfoSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(tvModelItem));

        mMessage = "Verify the device model item title.";
        UiObject2 itemTitle =
                tvModelItem.findObject(mTask.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "产品型号：", itemTitle.getText());

        mMessage = "Verify the device model item value.";
        UiObject2 itemValue =
                tvModelItem.findObject(mTask.getItemValueOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, mTask.getDeviceModelInfo(), itemValue.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test04_03TvRomSizeOnProductInfoSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[0]);

        mMessage = "Verify the device rom size item is enabled.";
        UiObject2 romSize = mDevice.findObject(mTask.getRomSizeItemOnProductInfoSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(romSize));

        mMessage = "Verify the device rom size item title.";
        UiObject2 itemTitle = romSize.findObject(mTask.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "本机容量：", itemTitle.getText());

        mMessage = "Verify the device rom item value.";
        UiObject2 itemValue = romSize.findObject(mTask.getItemValueOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, mTask.getDeviceRomSizeInfo(), itemValue.getText());
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test04_04TvSeriesIdOnProductInfoSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[0]);

        mMessage = "Verify the device series id item is enabled.";
        UiObject2 seriesId = mDevice.findObject(mTask.getSeriesIdItemOnProductInfoSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(seriesId));

        mMessage = "Verify the device series id item title.";
        UiObject2 itemTitle = seriesId.findObject(mTask.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "设备序列号：", itemTitle.getText());

        mMessage = "Verify the device series id item value.";
        UiObject2 itemValue = seriesId.findObject(mTask.getItemValueOnAboutInfoSubPageSelector());
        Assert.assertTrue(mMessage, mTask.isDeviceSeriesIdValid(itemValue.getText()));
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test06NetworkInfoItemOnAboutInfo() {
        mMessage = "Verify the network info item on About Info page is enabled.";
        UiObject2 productItem = mDevice.findObject(mTask.getNetworkInfoItemOnAboutSelector());
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
        UiObject2 title = mDevice.findObject(mTask.getSettingsAboutInfoSubPageTitleSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(title));

        mMessage = "Verify the text of title on network info.";
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[1], title.getText());

        // verification 2
        mMessage = "Verify the title of network status item.";
        UiObject2 statusItem =
                mDevice.findObject(mTask.getNetworkStatusItemOnNetworkInfoSelector());
        UiObject2 itemTitle =
                statusItem.findObject(mTask.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "已连接", itemTitle.getText());

        mMessage = "Verify the value of network status item.";
        UiObject2 itemValue =
                statusItem.findObject(mTask.getItemValueOnAboutInfoSubPageSelector());
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
                mDevice.findObject(mTask.getNetworkIpAddrItemOnNetworkInfoSelector());
        UiObject2 ipItemTitle =
                ipAddrItem.findObject(mTask.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "IP地址", ipItemTitle.getText());

        mMessage = "Verify the value of network IP address.";
        UiObject2 ipItemValue =
                ipAddrItem.findObject(mTask.getItemValueOnAboutInfoSubPageSelector());
        Assert.assertTrue(mMessage, wiredInfo.getIpAddr().equalsIgnoreCase(ipItemValue.getText()));

        mMessage = "Verify the title of wired mac.";
        UiObject2 macIdItem =
                mDevice.findObject(mTask.getNetworkWiredMacItemOnNetworkInfoSelector());
        UiObject2 macItemTitle =
                macIdItem.findObject(mTask.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "有线网络mac地址", macItemTitle.getText());

        mMessage = "Verify the value of wired mac.";
        UiObject2 macItemValue =
                macIdItem.findObject(mTask.getItemValueOnAboutInfoSubPageSelector());
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
                mDevice.findObject(mTask.getNetworkWirelessMacItemOnNetworkInfoSelector());
        UiObject2 macItemTitle =
                macIdItem.findObject(mTask.getItemTitleOnAboutInfoSubPageSelector());
        Assert.assertEquals(mMessage, "无线网络mac地址", macItemTitle.getText());

        mMessage = "Verify the value of wireless mac.";
        UiObject2 macItemValue =
                macIdItem.findObject(mTask.getItemValueOnAboutInfoSubPageSelector());
        Assert.assertTrue(mMessage, wiredInfo.getMacId().equalsIgnoreCase(macItemValue.getText()));
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test11QuestionFeedbackItem() {
        mMessage = "Verify the feedback item on About Info page is enabled.";
        UiObject2 feedback = mDevice.findObject(mTask.getQuestionFeedbackItemOnAboutSelector());
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
                mDevice.findObject(mTask.getSettingsAboutInfoSubPageTitleSelector());
        Assert.assertTrue(TestHelper.waitForUiObjectEnabled(subPageTitle));
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[3], subPageTitle.getText());

        mMessage = "Verify the content on question feedback sub page.";
        UiObject2 subPageContent =
                mDevice.findObject(mTask.getQuestionFeedbackSubPageContentSelector());
        Assert.assertEquals(mMessage, "如需上传日志，请按 “菜单” 键", subPageContent.getText());

        mMessage = "Verify the bottom feedback menu is exist on sub page!";
        mAction.doDeviceActionAndWait(new DeviceActionMenu());
        Assert.assertTrue(mMessage,
                TestHelper.waitForUiObjectExist(mTask.getBottomFeedbackMenuSelector()));
    }

    @Test
    @Category(CategoryAboutInfoTests.class)
    public void test13ButtonsInFeedbackMenuOfSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[3]);
        mTask.openBottomFeedBackMenu();

        // verification 1
        mMessage = "Verify the catch log button in bottom menu is enabled.";
        UiObject2 catchLogBtn =
                mDevice.findObject(mTask.getCatchLogButtonInFeedbackMenuSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(catchLogBtn));

        mMessage = "Verify the catch log button is default focused.";
        Assert.assertTrue(mMessage, catchLogBtn.isFocused());

        mMessage = "Verify the text of catch log button.";
        UiObject2 catchBtnText = catchLogBtn.findObject(mTask.getFeedbackMenuButtonTextSelector());
        Assert.assertEquals(mMessage, START_CATCH_LOG_MENU_BTN_TEXT, catchBtnText.getText());

        // verification 2
        mMessage = "Verify the dump log to U disk button in bottom menu is enabled.";
        UiObject2 dumpLogBtn =
                mDevice.findObject(mTask.getDumpLogToDiskButtonInFeedbackMenuSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(dumpLogBtn));

        mMessage = "Verify the text of dump log button.";
        UiObject2 dumpBtnText = dumpLogBtn.findObject(mTask.getFeedbackMenuButtonTextSelector());
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
                TestHelper.waitForUiObjectExist(mTask.getBottomFeedbackMenuSelector()));

        mMessage = "Verify the stop catch log button in feedback menu.";
        mTask.openBottomFeedBackMenu();
        Assert.assertEquals(mMessage,
                STOP_CATCH_LOG_MENU_BTN_TEXT, this.getCatchLogBtnTextInFeedbackMenu());

        mMessage = "Verify feedback menu disappear after stop catch log on feedback page.";
        mTask.enterOnSpecifiedButtonInFeedbackMenu(STOP_CATCH_LOG_MENU_BTN_TEXT);
        Assert.assertFalse(mMessage,
                TestHelper.waitForUiObjectExist(mTask.getBottomFeedbackMenuSelector()));

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
                TestHelper.waitForUiObjectExist(mTask.getBottomFeedbackMenuSelector()));

        mMessage = "Verify back to question feedback home page.";
        UiObject2 title = mDevice.findObject(mTask.getSettingsAboutInfoPageTitleSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(title));
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test17SystemVersionItemOnAboutInfo() {
        mMessage = "Verify the System Version item on about info page.";
        UiObject2 sysVersion = mDevice.findObject(mTask.getSystemVersionInfoItemOnAboutSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(sysVersion));

        mMessage = "Verify the system version title text.";
        UiObject2 itemTitle = sysVersion.findObject(mTask.getItemTitleOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, "系统版本", itemTitle.getText());

        mMessage = "Verify the system version number value.";
        UiObject2 itemValue = sysVersion.findObject(mTask.getItemValueOnAboutInfoPageSelector());
        Assert.assertTrue(mMessage, itemValue.getText().contains(mTask.getSystemVersionInfo()));
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test18PlayControllerItemOnAboutInfo() {
        mMessage = "Verify the play controller item on about info page.";
        UiObject2 controller = mDevice.findObject(mTask.getPlayControllerItemOnAboutSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(controller));

        mMessage = "Verify the play controller title text.";
        UiObject2 itemTitle = controller.findObject(mTask.getItemTitleOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, "播控方", itemTitle.getText());

        mMessage = "Verify the play controller value.";
        UiObject2 itemValue = controller.findObject(mTask.getItemValueOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, "BesTV", itemValue.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test21LawInfoItemOnAboutInfo() {
        mMessage = "Verify the law info item on about info page.";
        UiObject2 law = mDevice.findObject(mTask.getLawInfoItemOnAboutSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(law));

        mMessage = "Verify the law info title text.";
        UiObject2 title = law.findObject(mTask.getItemTitleOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[4], title.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test22TitleAndItemsOnLawInfoSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[4]);

        // verification 1
        mMessage = "Verify the title on Law Info sub page is enabled.";
        UiObject2 title =
                mDevice.findObject(mTask.getSettingsAboutInfoSubPageTitleSelector());
        Assert.assertTrue(mMessage, TestHelper.waitForUiObjectEnabled(title));

        mMessage = "Verify the title text on Law Info sub page.";
        Assert.assertEquals(mMessage, ABOUT_ITEM_TITLES_ARR[4], title.getText());

        // verification 2
        mMessage = "Verify the Copy Right Protect item title text.";
        UiObject2 copyRightItem =
                mDevice.findObject(mTask.getCopyRightProtectItemOnLawInfoSelector());
        UiObject2 titleCopyRight =
                copyRightItem.findObject(mTask.getItemTitleOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, LAW_ITEM_TITLES_ARR[0], titleCopyRight.getText());

        mMessage = "Verify the Privacy Policy item title text.";
        UiObject2 privacyItem = mDevice.findObject(mTask.getPrivacyPolicyItemOnLawInfoSelector());
        UiObject2 titlePrivacy =
                privacyItem.findObject(mTask.getItemTitleOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, LAW_ITEM_TITLES_ARR[1], titlePrivacy.getText());

        mMessage = "Verify the user agreement item title text.";
        UiObject2 agreementItem = mDevice.findObject(mTask.getUserAgreementItemOnLawInfoSelector());
        UiObject2 titleAgreement =
                agreementItem.findObject(mTask.getItemTitleOnAboutInfoPageSelector());
        Assert.assertEquals(mMessage, LAW_ITEM_TITLES_ARR[2], titleAgreement.getText());
    }

    @Test
    @Category(CategoryImageAndSoundSettingsTests.class)
    public void test23TitleAndContentOnCopyRightProtectSubPage() {
        mTask.openSpecifiedAboutInfoItemSubPage(ABOUT_ITEM_TITLES_ARR[4]);
        mTask.openSpecifiedAboutInfoItemSubPage(LAW_ITEM_TITLES_ARR[0]);

        mMessage = "Verify the title is enabled on Copy Right Protect sub page.";
        UiObject2 title = mDevice.findObject(mTask.getSettingsAboutInfoSubPageTitleSelector());
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
        UiObject2 title = mDevice.findObject(mTask.getSettingsAboutInfoSubPageTitleSelector());
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
        UiObject2 title = mDevice.findObject(mTask.getSettingsAboutInfoSubPageTitleSelector());
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
    }

    private String getCatchLogBtnTextInFeedbackMenu() {
        UiObject2 CatchBtn =
                mDevice.findObject(mTask.getCatchLogButtonInFeedbackMenuSelector());
        return CatchBtn.findObject(mTask.getFeedbackMenuButtonTextSelector()).getText();
    }
}
