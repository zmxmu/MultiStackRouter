package com.syswin.msgseal.navigation.action;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.syswin.msgseal.navigation.BaseFragment;
import com.syswin.msgseal.navigation.FragmentContainerActivity;
import com.syswin.msgseal.navigation.NavigationHelper;
import com.syswin.msgseal.navigation.RouterManager;
import com.syswin.msgseal.navigation.model.PageItem;

import static com.syswin.msgseal.navigation.NavigationHelper.ANIMATOR_START;
import static com.syswin.msgseal.navigation.RouterManager.BUNDLE_KEY_ANIMATOR_TYPE;
import static com.syswin.msgseal.navigation.RouterManager.BUNDLE_KEY_FRAGMENT;
import static com.syswin.msgseal.navigation.RouterManager.BUNDLE_KEY_PATH;

/*
普通跳转模式，直接在混合栈顶部新建页面item，进行跳转
 */
public class NormalGotoAction extends GotoAction {

    public NormalGotoAction(Activity activity, String path, Bundle bundle, int itemType) {
        super(activity, path, bundle,itemType);
    }

    @Override
    public boolean gotoPage(int animatorType) {
        switch (mItemType){
            case PageItem.ROUTER_TYPE_ACTIVITY:
                RouterManager.getInstance().startNewActivity(mActivity,mPath,mBundle,animatorType);
                BaseFragment exitFragment = RouterManager.getInstance().getTopFragment();
                if(exitFragment!= null){
                    exitFragment.onHide();
                }
                break;
            case PageItem.ROUTER_TYPE_FRAGMENT:
                FragmentContainerActivity baseFragmentActivity = RouterManager.getInstance().getLastContainer();
                mBundle.putInt(BUNDLE_KEY_ANIMATOR_TYPE,animatorType);
                mBundle.putString(BUNDLE_KEY_FRAGMENT,mPath);
                if(baseFragmentActivity!=null){
                    baseFragmentActivity.addFragment(mBundle,mPath,animatorType);
                }
                else{
                    Intent intent = new Intent(mActivity,FragmentContainerActivity.class);
                    mBundle.putString(BUNDLE_KEY_PATH,FragmentContainerActivity.class.getName());
                    intent.putExtras(mBundle);
                    mActivity.startActivity(intent,mBundle);
                    int animatorArray[]=NavigationHelper.ANIMATOR_ARRAY[animatorType][ANIMATOR_START];
                    mActivity.overridePendingTransition(animatorArray[0], animatorArray[1]);
                }
                break;
        }
        return true;
    }
}
