package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.uiautomator.UiDevice;

import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;

/**
 * Created by zhengjin on 2017/8/2.
 * <p>
 * Base class for test UI tasks.
 */

class TaskBase {

    public final String TAG = this.getClass().getSimpleName();

    public final UiDevice device = TestConstants.GetUiDeviceInstance();
    public final UiActionsManager action = UiActionsManager.getInstance();

}
