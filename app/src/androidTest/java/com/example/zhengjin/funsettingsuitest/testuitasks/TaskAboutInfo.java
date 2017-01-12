package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testsuites.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionCenter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMenu;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;
import com.example.zhengjin.funsettingsuitest.utils.StringUtils;

import junit.framework.Assert;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_ABOUT_INFO_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2017/1/5.
 * <p>
 * Include the UI selectors and tasks for about info page test cases.
 */
public final class TaskAboutInfo {

    private static TaskAboutInfo instance;
    private UiDevice device;
    private UiActionsManager action;

    public static final String TAG = TaskAboutInfo.class.getSimpleName();

    public final String ABOUT_INFO_PAGE_TEXT = "关于";

    private TaskAboutInfo() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        action = UiActionsManager.getInstance();
    }

    public static synchronized TaskAboutInfo getInstance() {
        if (instance == null) {
            instance = new TaskAboutInfo();
        }

        return instance;
    }

    public void destroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public BySelector getSettingsAboutInfoPageTitleSelector() {
        return By.res("tv.fun.settings:id/setting_title");
    }

    public BySelector getSettingsAboutInfoSubPageTitleSelector() {
        return By.res("tv.fun.settings:id/setting_title");
    }

    public BySelector getProductInfoItemOnAboutSelector() {
        return By.res("tv.fun.settings:id/about_item_product_info");
    }

    public BySelector getNetworkInfoItemOnAboutSelector() {
        return By.res("tv.fun.settings:id/about_item_network");
    }

    public BySelector getSystemVersionInfoItemOnAboutSelector() {
        return By.res("tv.fun.settings:id/about_item_sysinfo");
    }

    public BySelector getLawInfoItemOnAboutSelector() {
        return By.res("tv.fun.settings:id/about_item_law");
    }

    public BySelector getPlayControlItemOnAboutSelector() {
        return By.res("tv.fun.settings:id/about_item_programflatform");
    }

    public BySelector getNetworkStatusItemOnNetworkInfoSelector() {
        return By.res("tv.fun.settings:id/activity_about_network_status");
    }

    public BySelector getNetworkIpAddrItemOnNetworkInfoSelector() {
        return By.res("tv.fun.settings:id/activity_about_network_ip");
    }

    public BySelector getNetworkWiredMacItemOnNetworkInfoSelector() {
        return By.res("tv.fun.settings:id/activity_about_network_mac_ethernet");
    }

    public BySelector getNetworkWirelessMacItemOnNetworkInfoSelector() {
        return By.res("tv.fun.settings:id/activity_about_network_mac_wifi");
    }

    public BySelector getItemTitleOnNetworkInfoSubPageSelector() {
        return By.res("tv.fun.settings:id/display_item_title");
    }

    public BySelector getItemValueOnNetworkInfoSubPageSelector() {
        return By.res("tv.fun.settings:id/display_item_edit");
    }

    public BySelector getQuestionFeedbackItemOnAboutSelector() {
        return By.res("tv.fun.settings:id/about_item_feedback");
    }

    public BySelector getQuestionFeedbackSubPageContentSelector() {
        return By.res("tv.fun.settings:id/feedback_tips_5");
    }

    public BySelector getBottomFeedbackMenuSelector() {
        return By.res("android:id/tv_fun_menu");
    }

    public BySelector getCatchLogButtonInFeedbackMenuSelector() {
        return By.res("tv.fun.settings:id/about_item_feedback_menu_tcpdump");
    }

    public BySelector getDumpLogToDiskButtonInFeedbackMenuSelector() {
        return By.res("tv.fun.settings:id/about_item_feedback_menu_dump_log");
    }

    public BySelector getFeedbackMenuButtonTextSelector() {
        return By.res("android:id/tv_fun_menu_text");
    }

    public UiObject2 getTextViewInAboutInfoItem(UiObject2 parent) {
        return parent.findObject(By.clazz(TestConstants.CLASS_TEXT_VIEW));
    }

    public void openAboutInfoHomePage() {
        if (RunnerProfile.isPlatform938) {
            ShellUtils.startSpecifiedActivity(SETTINGS_PKG_NAME, SETTINGS_ABOUT_INFO_ACT);
            ShellUtils.systemWaitByMillis(WAIT);
        } else {
            TaskLauncher.openSpecifiedCardFromSettingsTab(ABOUT_INFO_PAGE_TEXT);
        }

        Assert.assertTrue("openAboutInfoHomePage, open failed!",
                TestHelper.waitForActivityOpenedByShellCmd(
                        SETTINGS_PKG_NAME, SETTINGS_ABOUT_INFO_ACT));
        action.doDeviceActionAndWait(new DeviceActionMoveUp());  // request focus
    }

    public void openSpecifiedAboutInfoItemSubPage(String itemTitle) {
        focusOnSpecifiedAboutInfoItem(itemTitle);
        action.doDeviceActionAndWait(new DeviceActionCenter(), WAIT);
    }

    private void focusOnSpecifiedAboutInfoItem(String itemTitle) {
        UiObject2 title = device.findObject(By.text(itemTitle));
        for (int i = 0, moves = 7; i < moves; i++) {
            if (title.isSelected()) {
                return;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveDown());
        }

        Assert.assertTrue("focusOnSpecifiedAboutInfoItem, failed to focus on item " + itemTitle
                , false);
    }

    public void openBottomFeedBackMenu() {
        action.doDeviceActionAndWait(new DeviceActionMenu());
        Assert.assertTrue("openBottomFeedBackMenu, open bottom menu failed!",
                TestHelper.waitForUiObjectExist(this.getBottomFeedbackMenuSelector()));
    }

    private void focusOnSpecifiedButtonInFeedbackMenu(String btnText) {
        UiObject2 menuBtn = device.findObject(By.text(btnText)).getParent();
        for (int i = 0, moves = 3; i < moves; i++) {
            if (menuBtn.isFocused()) {
                return;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveRight());
        }

        Assert.assertTrue("focusOnSpecifiedButtonInFeedbackMenu, failed to focus on menu button"
                + btnText, false);
    }

    public void enterOnSpecifiedButtonInFeedbackMenu(String btnText) {
        this.focusOnSpecifiedButtonInFeedbackMenu(btnText);
        action.doDeviceActionAndWait(new DeviceActionCenter());
    }

    public NetworkInfo getSysNetworkInfoForPlatform638(NetworkType type) {
        String cmd = "netcfg | grep %s";
        if (type == NetworkType.Wired) {
            cmd = String.format(cmd, "eth0");
        }
        if (type == NetworkType.Wireless) {
            cmd = String.format(cmd, "wlan0");
        }

        ShellUtils.CommandResult cr = ShellUtils.execCommand(cmd, false, true);
        if (cr.mResult != 0) {
            Log.e(TAG, "Failed to get the wired network info by " + cmd);
            throw new RuntimeException("Error for command " + cmd);
        }
        if (StringUtils.isEmpty(cr.mSuccessMsg)) {
            Log.e(TAG, "The return info is empty by command " + cmd);
            throw new RuntimeException("Return empty for command " + cmd);
        }

        String[] valuesArr = cr.mSuccessMsg.split("\\s+");
        return new NetworkInfo(valuesArr[2].split("/")[0], valuesArr[4]);
    }

    public static class NetworkInfo {
        private String ipAddr;
        private String macId;

        NetworkInfo(String ipAddr, String macId) {
            this.ipAddr = ipAddr;
            this.macId = macId;
        }

        public String getIpAddr() {
            return this.ipAddr;
        }

        public String getMacId() {
            return this.macId;
        }
    }

    public enum NetworkType {
        Wired,
        Wireless
    }

}
