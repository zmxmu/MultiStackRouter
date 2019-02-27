package com.syswin.msgseal.navigation.action;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.syswin.msgseal.navigation.model.PageItem;

public abstract class GotoAction {
    Context mContext;
    String mPath;
    Bundle mBundle;
    int mItemType;
    public GotoAction(Context context, String path, Bundle bundle,int itemType){
        mContext = context;
        mPath = path;
        mBundle = bundle;
        mItemType = itemType;
    }
    abstract boolean gotoPage();

}
