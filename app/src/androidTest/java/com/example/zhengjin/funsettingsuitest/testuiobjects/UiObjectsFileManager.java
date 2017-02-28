package com.example.zhengjin.funsettingsuitest.testuiobjects;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;

/**
 * Created by zhengjin on 2017/2/28.
 * <p>
 * Include the UI obejcts for file manager apk.
 */
public final class UiObjectsFileManager {

    private static UiObjectsFileManager ourInstance = new UiObjectsFileManager();

    public static UiObjectsFileManager getInstance() {
        return ourInstance;
    }

    public void destroyInstance() {
        if (ourInstance != null) {
            ourInstance = null;
        }
    }

    private UiObjectsFileManager() {
    }

    public BySelector getFileManagerHomeTabSelector() {
        return By.res("tv.fun.filemanager:id/activity_fun_fm_tab");
    }

    public BySelector getVideoCategorySelector() {
        return By.res("tv.fun.filemanager:id/category_video");
    }

    public BySelector getCategoryTitleSelector() {
        return By.res("tv.fun.filemanager:id/entity_name");
    }

    public BySelector getCategoryEntiesCountSelector() {
        return By.res("tv.fun.filemanager:id/entity_count");
    }

    public BySelector getMainTitleSelector() {
        return By.res("tv.fun.filemanager:id/activity_sub_title_main");
    }

    public BySelector getSubTitleSelector() {
        return By.res("tv.fun.filemanager:id/activity_sub_title_sub");
    }

    public BySelector getMenuTipsSelector() {
        return By.res("tv.fun.filemanager:id/activity_sub_menu_tips");
    }

    public BySelector getMenuContainerSelector() {
        return By.res("android:id/tv_fun_menu");
    }

    public BySelector getMenuRemoveBtnContainerSelector() {
        return By.res("tv.fun.filemanager:id/menu_item_del_id");
    }

    public BySelector getMenuHideBtnContainerSelector() {
        return By.res("tv.fun.filemanager:id/menu_item_hide_id");
    }

    public BySelector getMenuShowAllBtnContainerSelector() {
        return By.res("tv.fun.filemanager:id/menu_item_show_id");
    }

    public BySelector getMenuBtnTextSelector() {
        return By.res("android:id/tv_fun_menu_text");
    }

    public BySelector getYesBtnOfConfirmDialogSelector() {
        return By.res("tv.fun.filemanager:id/confirm_dialog_btn_confirm");
    }

    public BySelector getCancelBtnOfConfirmDialogSelector() {
        return By.res("tv.fun.filemanager:id/confirm_dialog_btn_cancel");
    }

    public BySelector getTipsOfEmptyDirFromLocalFilesCardSelector() {
        return By.res("tv.fun.filemanager:id/sub_blank_tips");
    }

}
