package com.example.zhengjin.funsettingsuitest.testcases;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuitasks.TaskImageAndSound;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * Created by zhengjin on 2016/12/14.
 * <p>
 * Include test cases for image and sound settings.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestImageAndSoundSettings {

    private UiDevice mDevice;
    private UiActionsManager mAction;
    private TaskImageAndSound mTask;
    private String mMessage;

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mAction = UiActionsManager.getInstance();
        mTask = TaskImageAndSound.getInstance();
    }

    @After
    public void clearUp() {
        ShellUtils.stopProcess(TestConstants.SETTINGS_PKG_NAME);
    }

}
