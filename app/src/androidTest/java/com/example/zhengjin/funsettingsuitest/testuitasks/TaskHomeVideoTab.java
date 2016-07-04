package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiObject2;

import junit.framework.Assert;

import java.util.List;

/**
 * Created by Vieira on 2016/7/4.
 *
 * Include UI selectors and tasks on video tab of home page.
 */
public final class TaskHomeVideoTab {

    public static BySelector getCardsContainerOfFilmDetailsSelector() {
        return By.res("com.bestv.ott:id/grid");
    }

    public static BySelector getCardsContainerOfSpeicialSubjectSelector() {
        return By.res("com.bestv.ott:id/special_listview");
    }

    public static void verifyAllTextViewHasTextOfContainer(UiObject2 container) {

        List<UiObject2> listObj = container.findObjects(By.clazz("android.widget.TextView"));
        for (UiObject2 uiObj : listObj) {
            Assert.assertNotNull("Verify the card is not null.", uiObj);
            Assert.assertFalse("Verify the text of card title is not empty.",
                    "".equals(uiObj.getText()));
        }
    }

}
