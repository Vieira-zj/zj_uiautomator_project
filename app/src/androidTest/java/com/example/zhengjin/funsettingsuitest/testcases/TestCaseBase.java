package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.uiautomator.UiDevice;

import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;

/**
 * Created by zhengjin on 2017/7/13.
 * <p>
 * Parent for UI test case class.
 */

class TestCaseBase {

    final String TAG = this.getClass().getSimpleName();

    final UiDevice mDevice = TestConstants.GetUiDeviceInstance();
    final UiActionsManager mAction = UiActionsManager.getInstance();

}
