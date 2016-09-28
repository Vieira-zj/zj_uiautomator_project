package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import junit.framework.Assert;

import java.util.List;

/**
 * Created by Vieira on 2016/7/4.
 *
 * Include UI selectors and tasks on video tab of home page.
 */
public final class TaskVideoHomeTab {

    public static BySelector getAllCardsTitleOfLauncherHomeLeftAreaSelector() {
        return By.res("com.bestv.ott:id/title");
    }

    public static BySelector getAllCardsMainTitleOfLauncherHomeRightAreaSelector() {
        return By.res("com.bestv.ott:id/maintitle");
    }

    public static BySelector getCardsContainerOfVideoRecommendPageSelector() {
        return By.res("com.bestv.ott:id/grid");
    }

    public static BySelector getSpecialSubjectContainerSelector() {
        return By.res("com.bestv.ott:id/special_listview");
    }

    public static BySelector getAllTabTextOfVideoSubPageSelector() {
        return By.res("com.bestv.ott:id/tab_title");
    }

    public static BySelector getAllCardsMainTitleOfVideoSubPageSelector() {
        return By.res("com.bestv.ott:id/maintitle");
    }

    public static BySelector getAllCardsSubTitleOfVideoSubPageSelector() {
        return By.res("com.bestv.ott:id/subtitle");
    }

    public static BySelector getTitleTextOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/detail_title");
    }

    public static BySelector getRelatedVideoListOfVideoDetailsPageSelector() {
        return By.res("com.bestv.ott:id/relate_list_view");
    }

    public static UiObject2 findSpecifiedCardFromLeftAreaByText(UiDevice device, String search) {
        List<UiObject2> textList =
                device.findObjects(getAllCardsTitleOfLauncherHomeLeftAreaSelector());
        return findSpecifiedTextViewFromUiCollection(textList, search);
    }

    public static UiObject2 findSpecifiedCardFromRightAreaByText(UiDevice device, String search) {
        List<UiObject2> textList =
                device.findObjects(getAllCardsMainTitleOfLauncherHomeRightAreaSelector());
        return findSpecifiedTextViewFromUiCollection(textList, search);
    }

    private static UiObject2 findSpecifiedTextViewFromUiCollection(
            List<UiObject2> list, String search) {
        Assert.assertFalse("Error, the UI collection size is zero.", (list.size() == 0));
        UiObject2 retObj = null;

        for (UiObject2 uiText : list) {
            if (search.equals(uiText.getText())) {
                retObj = uiText.getParent();
            }
        }
        Assert.assertNotNull(String.format(
                "The text(%s) is NOT found on launcher home page.", search), retObj);

        return retObj;
    }

}
