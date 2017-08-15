package com.example.zhengjin.funsettingsuitest.testuiobjects;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;

/**
 * Created by zhengjin on 2017/2/28.
 * <p>
 * Include UI objects for about info page.
 */
public final class UiObjectsAboutInfo {

    private static UiObjectsAboutInfo ourInstance;

    private UiObjectsAboutInfo() {
    }

    public static UiObjectsAboutInfo getInstance() {
        if (ourInstance == null) {
            synchronized (UiObjectsAboutInfo.class) {
                if (ourInstance == null) {
                    ourInstance = new UiObjectsAboutInfo();
                }
            }
        }
        return ourInstance;
    }

    public static synchronized void destroyInstance() {
        if (ourInstance != null) {
            ourInstance = null;
        }
    }

    public BySelector getSettingsAboutInfoPageTitleSelector() {
        return By.res("tv.fun.settings:id/setting_title");
    }

    public BySelector getSettingsItemsContainerOnAboutSelector() {
        return By.res("tv.fun.settings:id/settings_container");
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

    public BySelector getPlayControllerItemOnAboutSelector() {
        return By.res("tv.fun.settings:id/about_item_programflatform");
    }

    public BySelector getAppsVersionItemOnAboutSelector() {
        return By.res("tv.fun.settings:id/about_item_app_version");
    }

    public BySelector getItemTitleOnAboutInfoPageSelector() {
        return By.res("tv.fun.settings:id/item_title");
    }

    public BySelector getItemValueOnAboutInfoPageSelector() {
        return By.res("tv.fun.settings:id/item_value");
    }

    public BySelector getQrCodeContentOnProductInfoSelector() {
        return By.res("tv.fun.settings:id/prompt");
    }

    public BySelector getTvNameItemOnProductInfoSelector() {
        return By.res("tv.fun.settings:id/activity_about_productinfo_tvname");
    }

    public BySelector getTvModelItemOnProductInfoSelector() {
        return By.res("tv.fun.settings:id/activity_about_productinfo_model");
    }

    public BySelector getRomSizeItemOnProductInfoSelector() {
        return By.res("tv.fun.settings:id/activity_about_productinfo_space");
    }

    public BySelector getSeriesIdItemOnProductInfoSelector() {
        return By.res("tv.fun.settings:id/activity_about_productinfo_code");
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

    public BySelector getItemTitleOnAboutInfoSubPageSelector() {
        return By.res("tv.fun.settings:id/display_item_title");
    }

    public BySelector getItemValueOnAboutInfoSubPageSelector() {
        return By.res("tv.fun.settings:id/display_item_edit");
    }

    public BySelector getQuestionFeedbackItemOnAboutSelector() {
        return By.res("tv.fun.settings:id/about_item_feedback");
    }

    public BySelector getServiceTelNumberOnFeedbackSubPageSelector() {
        return By.res("tv.fun.settings:id/feedback_tips_3");
    }

    public BySelector getServiceQQNumberOnFeedbackSubPageSelector() {
        return By.res("tv.fun.settings:id/feedback_tips_4");
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

    public BySelector getCopyRightProtectItemOnLawInfoSelector() {
        return By.res("tv.fun.settings:id/about_item_sub_complaint");
    }

    public BySelector getPrivacyPolicyItemOnLawInfoSelector() {
        return By.res("tv.fun.settings:id/about_item_sub_policy");
    }

    public BySelector getUserAgreementItemOnLawInfoSelector() {
        return By.res("tv.fun.settings:id/about_item_sub_protocol");
    }

    public BySelector getOsNameContainterOnHiddenSystemInfoSelector() {
        return By.res("tv.fun.settings:id/part_one_left");
    }

    public BySelector getDeviceModelContainerOnHiddenSystemInfoSelector() {
        return By.res("tv.fun.settings:id/part_one_right");
    }

    public BySelector getSystemItemKeyOnHiddenSystemInfoSelector() {
        return By.res("tv.fun.settings:id/sysytem_item_label");
    }

    public BySelector getSystemItemValueOnHiddenSystemInfoSelector() {
        return By.res("tv.fun.settings:id/sysytem_item_text");
    }

}
