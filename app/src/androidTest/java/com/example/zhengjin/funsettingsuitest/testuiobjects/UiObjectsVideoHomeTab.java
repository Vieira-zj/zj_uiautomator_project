package com.example.zhengjin.funsettingsuitest.testuiobjects;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;

/**
 * Created by zhengjin on 2017/3/20.
 * <p>
 * Include the UI objects for video home tab.
 */

public final class UiObjectsVideoHomeTab {

    private static UiObjectsVideoHomeTab ourInstance = new UiObjectsVideoHomeTab();

    public static UiObjectsVideoHomeTab getInstance() {
        return ourInstance;
    }

    public void destroyInstance() {
        if (ourInstance != null) {
            ourInstance = null;
        }
    }

    private UiObjectsVideoHomeTab() {
    }

    public BySelector getCardTitleOfLauncherHomeLeftAreaSelector() {
        return By.res("com.bestv.ott:id/title");
    }

    public BySelector getCardMainTitleOfLauncherHomeRightAreaSelector() {
        return By.res("com.bestv.ott:id/maintitle");
    }

    public BySelector getAllImagesOfMainCateOnCateDetailsSelector() {
        return By.res("com.bestv.ott:id/poster");
    }

    public BySelector getCardContainerOfVideoRecommendPageSelector() {
        return By.res("com.bestv.ott:id/grid");
    }

    public BySelector getTopTabTextOfVideoSubPageSelector() {
        return By.res("com.bestv.ott:id/tab_title");
    }

    public BySelector getCardMainTitleOfVideoSubPageSelector() {
        return By.res("com.bestv.ott:id/maintitle");
    }

    public BySelector getCardSubTitleOfVideoSubPageSelector() {
        return By.res("com.bestv.ott:id/subtitle");
    }

    @SuppressWarnings("unused")
    public BySelector getVideoTitleOfVideoSubPageSelector() {
        return By.res("com.bestv.ott:id/title");
    }

    public BySelector getTitleTextOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/detail_title");
    }

    public BySelector getPlayButtonOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/detail_play_button");
    }

    public BySelector getSelectButtonOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/detail_select_button");
    }

    public BySelector getTryWatchButtonOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/detail_try_button");
    }

    public BySelector getTvNumberTipsOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/detail_tip_button");
    }

    public BySelector getRelatedVideoListOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/relate_list_view");
    }

    public BySelector getAllTvCellsOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/tv_cell");
    }

    public BySelector getBottomTabContainerOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/episode_view");
    }

    public BySelector getSignalSourceCardOnHomeTvTabSelector() {
        return By.res("com.bestv.ott:id/home_tv");
    }

    public BySelector getHdmi1ItemFromSignalSourceDialogSelector() {
        return By.res("tv.fun.settings:id/source_item_hdmi1");
    }

    public BySelector getFactoryMenuFlipperSelector() {
        return By.res("mstar.factorymenu.ui:id/factory_view_flipper");
    }

}
