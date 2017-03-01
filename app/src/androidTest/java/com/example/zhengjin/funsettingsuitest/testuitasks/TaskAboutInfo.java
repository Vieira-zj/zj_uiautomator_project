package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testrunner.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionCenter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMenu;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuiobjects.UiObjectsAboutInfo;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;
import com.example.zhengjin.funsettingsuitest.utils.StringUtils;

import junit.framework.Assert;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_ABOUT_INFO_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2017/1/5.
 * <p>
 * Include the UI selectors and tasks for about info page test cases.
 */
public final class TaskAboutInfo {

    public static final String TAG = TaskAboutInfo.class.getSimpleName();

    private static TaskAboutInfo instance;

    private UiDevice device;
    private UiActionsManager action;
    private UiObjectsAboutInfo funUiObjects;

    public final String ABOUT_INFO_PAGE_TEXT = "关于";
    private final String MAC_NULL = "00:00:00:00:00:00";

    private TaskAboutInfo() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        action = UiActionsManager.getInstance();
        funUiObjects = UiObjectsAboutInfo.getInstance();
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

    public BySelector getContentWebViewOnLawItemSubPageSelector() {
        return By.clazz(TestConstants.CLASS_WEB_VIEW);
    }

    public UiObject2 getTitleInAboutInfoItem(UiObject2 parent) {
        return parent.findObject(funUiObjects.getItemTitleOnAboutInfoPageSelector());
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
                TestHelper.waitForUiObjectExist(funUiObjects.getBottomFeedbackMenuSelector()));
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

    public String getDeviceModelInfo() {
        String productSeriesNo = this.getTvSeriesNoByShellCmd("getprop | grep product.model");
        String tvModel = this.getDeviceModel(productSeriesNo);
        String suffix = this.getDeviceModelSuffix(productSeriesNo);

        return String.format("%s%s%s", "G", tvModel, suffix);
    }

    private String getDeviceModel(String seriesNo) {
        final String DEFAULT_MODEL = "55";

        if (StringUtils.isEmpty(seriesNo)) {
            return DEFAULT_MODEL;
        }
        try {
            return seriesNo.substring(0, 2);
        } catch (Exception e) {
            return DEFAULT_MODEL;
        }
    }

    private String getDeviceModelSuffix(String seriesNo) {
        final String DEFAULT_MODEL_SUFFIX = "F";

        if (StringUtils.isEmpty(seriesNo)) {
            return DEFAULT_MODEL_SUFFIX;
        }
        if (RunnerProfile.isPlatform938) {
            return "S";
        }
        if (seriesNo.endsWith("51")) {
            return "F";
        }
        if (seriesNo.endsWith("52")) {
            return "Y";
        }

        return DEFAULT_MODEL_SUFFIX;
    }

    private String getTvSeriesNoByShellCmd(String cmd) {
        ShellUtils.CommandResult cr = ShellUtils.execCommand(cmd, false, true);
        if (cr.mResult == 0 && !StringUtils.isEmpty(cr.mSuccessMsg)) {
            Pattern pattern = Pattern.compile("[0-9]{4}");
            Matcher matcher = pattern.matcher(cr.mSuccessMsg);
            if (matcher.find()) {
                return matcher.group();
            }
        }

        return "";
    }

    public String getDeviceRomSizeInfo() {
        final String DEFAULT_ROM_SIZE = "8GB";

        String cmd = RunnerProfile.isPlatform938 ? "df | grep /data" : "df | grep /sdcard";
        ShellUtils.CommandResult cr = ShellUtils.execCommand(cmd, false, true);
        if (cr.mResult != 0 || StringUtils.isEmpty(cr.mSuccessMsg)) {
            return DEFAULT_ROM_SIZE;
        }

        if (this.filterRomSize(cr.mSuccessMsg) <= 8.0) {
            return DEFAULT_ROM_SIZE;
        }
        return "32GB";
    }

    private float filterRomSize(String results) {
        String size;
        try {
            String[] items = results.split("\\s+");
            size = items[1].substring(0, items[1].indexOf("G"));
        } catch (Exception e) {
            size = "0";
        }

        return Float.parseFloat(size);
    }

    public boolean isDeviceSeriesIdValid(String seriesId) {
        Pattern pattern = Pattern.compile("FUN[0-9]{20,21}");
        Matcher matcher = pattern.matcher(seriesId);
        return matcher.find();
    }

    public String getSystemVersionInfo() {
        String ret = "0.0.0.0";

        try {
            String results = this.runShellCommandAndCheck("getprop | grep version.incremental");
            String version = results.split("\\s+")[1];
            ret = version.substring(1, (version.length() - 1));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Assert.assertTrue("getSystemVersionInfo, failed to get system version!", false);
        }

        return ret;
    }

    public NetworkInfo getSysNetworkInfo(NetworkType type) {
        return RunnerProfile.isPlatform938 ?
                this.getSysNetworkInfoForPlatform938(type) :
                this.getSysNetworkInfoForPlatform638(type);
    }

    private NetworkInfo getSysNetworkInfoForPlatform638(NetworkType type) {
        String cmd = this.buildCommandByNetworkType(type, "netcfg | grep %s");

        try {
            String[] valuesArr = this.runShellCommandAndCheck(cmd).split("\\s+");
            return new NetworkInfo(valuesArr[2].split("/")[0], valuesArr[4]);
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
            return new NetworkInfo("null", MAC_NULL);
        }
    }

    private NetworkInfo getSysNetworkInfoForPlatform938(NetworkType type) {
        String ipAddr = this.getNetworkIpForPlatform938(type);
        String macId = this.getNetworkMacForPlatform938(type);
        return new NetworkInfo(ipAddr, macId);
    }

    private String getNetworkIpForPlatform938(NetworkType type) {
        final String DEFAULT_IP = "0.0.0.0";

        String cmd = this.buildCommandByNetworkType(type, "ifconfig %s | grep \"inet addr\"");
        ShellUtils.CommandResult cr = ShellUtils.execCommand(cmd, false, true);
        if (cr.mResult != 0 || StringUtils.isEmpty(cr.mSuccessMsg)) {
            return DEFAULT_IP;
        }

        String[] items = cr.mSuccessMsg.split("\\s+");
        for (String item : items) {
            if (item.startsWith("addr")) {
                return item.split(":")[1];
            }
        }

        return DEFAULT_IP;
    }

    private String getNetworkMacForPlatform938(NetworkType type) {
        String cmd = this.buildCommandByNetworkType(type, "ifconfig %s | grep HWaddr");
        Pattern pattern = Pattern.compile("(([0-9]|[A-Z]){2}:){5}([0-9]|[A-Z]){2}");
        Matcher matcher;

        try {
            matcher = pattern.matcher(this.runShellCommandAndCheck(cmd));
        } catch (Exception e) {
            Log.w(TAG, e.getMessage());
            return MAC_NULL;
        }
        if (matcher.find()) {
            return matcher.group();
        }

        Assert.assertTrue("No mac found in results for command " + cmd, false);
        return "";  // not go here
    }

    private String buildCommandByNetworkType(NetworkType type, String baseCmd) {
        final String NETWORK_TYPE_WIRED = "eth0";
        final String NETWORK_TYPE_WIRELESS = "wlan0";

        return type == NetworkType.Wireless ?
                String.format(baseCmd, NETWORK_TYPE_WIRELESS) :
                String.format(baseCmd, NETWORK_TYPE_WIRED);
    }

    private String runShellCommandAndCheck(String cmd) throws Exception {
        ShellUtils.CommandResult cr = ShellUtils.execCommand(cmd, false, true);
        if (cr.mResult != 0) {
            throw new Exception(String.format(Locale.getDefault(),
                    "The return code is (%d) for command (%s)", cr.mResult, cmd));
        }
        if (StringUtils.isEmpty(cr.mSuccessMsg)) {
            Log.e(TAG, "The return info is empty by command: " + cmd);
            Assert.assertTrue("Return empty for command: " + cmd, false);
        }

        return cr.mSuccessMsg;
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
