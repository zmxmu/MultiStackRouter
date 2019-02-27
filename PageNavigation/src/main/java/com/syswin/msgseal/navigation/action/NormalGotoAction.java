package com.syswin.msgseal.navigation.action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.syswin.msgseal.navigation.BaseFragment;
import com.syswin.msgseal.navigation.FragmentContainerActivity;
import com.syswin.msgseal.navigation.model.PageItem;
import com.syswin.msgseal.navigation.RouterManager;

import static com.syswin.msgseal.navigation.RouterManager.BUNDLE_KEY_FRAGMENT;
import static com.syswin.msgseal.navigation.RouterManager.BUNDLE_KEY_PATH;

public class NormalGotoAction extends GotoAction {

    public NormalGotoAction(Context context, String path, Bundle bundle,int itemType) {
        super(context, path, bundle,itemType);
    }

    @Override
    public boolean gotoPage() {
        switch (mItemType){
            case PageItem.ROUTER_TYPE_ACTIVITY:
                RouterManager.getInstance().startNewActivity(mContext,mPath,mBundle);
                break;
            case PageItem.ROUTER_TYPE_FRAGMENT:
                FragmentContainerActivity baseFragmentActivity = RouterManager.getInstance().getLastContainer();
                if(baseFragmentActivity!=null){
                    mBundle.putString(BUNDLE_KEY_FRAGMENT,mPath);
                    baseFragmentActivity.addFragment(mBundle,mPath);
                }
                else{
                    Intent intent = new Intent(mContext,FragmentContainerActivity.class);
                    mBundle.putString(BUNDLE_KEY_PATH,FragmentContainerActivity.class.getName());
                    mBundle.putString(BUNDLE_KEY_FRAGMENT,mPath);
                    intent.putExtras(mBundle);
                    mContext.startActivity(intent,mBundle);
                }
                break;
        }
        return true;
    }
}
