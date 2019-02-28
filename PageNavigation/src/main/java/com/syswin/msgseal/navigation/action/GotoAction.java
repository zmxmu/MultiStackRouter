package com.syswin.msgseal.navigation.action;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.syswin.msgseal.navigation.model.PageItem;

public abstract class GotoAction {
    Activity mActivity;
    String mPath;
    Bundle mBundle;
    int mItemType;
    public GotoAction(Activity activity, String path, Bundle bundle, int itemType){
        mActivity = activity;
        mPath = path;
        mBundle = bundle;
        mItemType = itemType;
    }
    abstract boolean gotoPage(int animatorType);

}
