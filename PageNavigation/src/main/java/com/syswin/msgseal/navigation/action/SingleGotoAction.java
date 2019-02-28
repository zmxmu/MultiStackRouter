package com.syswin.msgseal.navigation.action;

import android.content.Context;
import android.os.Bundle;

import com.syswin.msgseal.navigation.NavigationHelper;
import com.syswin.msgseal.navigation.model.ActivityItem;
import com.syswin.msgseal.navigation.BaseFragment;
import com.syswin.msgseal.navigation.FragmentContainerActivity;
import com.syswin.msgseal.navigation.model.FragmentItem;
import com.syswin.msgseal.navigation.model.PageItem;
import com.syswin.msgseal.navigation.RouterManager;

import java.util.Stack;

public class SingleGotoAction extends GotoAction {
    public SingleGotoAction(Context context, String path, Bundle bundle,int itemType) {
        super(context, path, bundle,itemType);
    }

    @Override
    public boolean gotoPage(int animatorType) {
        int index =  RouterManager.getInstance().getPathIndex(mPath);
        Stack<PageItem> stack = RouterManager.getInstance().getStack();
        if(index == (stack.size()-1)){
            return false;
        }
        switch (mItemType){
            case PageItem.ROUTER_TYPE_ACTIVITY:
                if(index>=0){
                    for(int i = stack.size()-1;i>index;i--){
                        PageItem currentItem = stack.get(i);
                        if(currentItem.getType() == PageItem.ROUTER_TYPE_ACTIVITY
                                || currentItem.getType() == PageItem.ROUTER_TYPE_CONTAINER){
                            ((ActivityItem)currentItem).getActivityWR().get().finish();
                        }
                    }
                }
                else{
                    RouterManager.getInstance().goTo(mContext,mPath,RouterManager.GOTO_ACTION_NORMAL,mBundle);
                }
                break;
            case PageItem.ROUTER_TYPE_FRAGMENT:
                if(index>0){
                    BaseFragment enterFragment = ((FragmentItem)stack.get(index)).getFragmentWR().get();
                    FragmentContainerActivity container = (FragmentContainerActivity)enterFragment.getActivity();
                    BaseFragment exitFragment = RouterManager.getInstance().getTopFragment();
                    if(exitFragment.getActivity()!= container){
                        exitFragment = null;
                    }
                    for(int i = stack.size()-1;i>index;i--){
                        PageItem currentItem = stack.get(i);
                        if(currentItem.getType() == PageItem.ROUTER_TYPE_ACTIVITY
                                || currentItem.getType() == PageItem.ROUTER_TYPE_CONTAINER){
                            ((ActivityItem)currentItem).getActivityWR().get().finish();
                        }
                        else if(currentItem.getType() == PageItem.ROUTER_TYPE_FRAGMENT){
                            if(i == stack.size()-1 && exitFragment!=null){
                                continue;
                            }
                            BaseFragment currentFragment = ((FragmentItem)currentItem).getFragmentWR().get();
                            FragmentContainerActivity currentContainer = (FragmentContainerActivity)currentFragment.getActivity();
                            currentContainer.getFragmentManager().beginTransaction().remove(currentFragment).commit();
                        }
                    }

                    container.getFragmentManager().beginTransaction().show(enterFragment).commit();
                    NavigationHelper.initAnimator(container,animatorType).animatorExit(exitFragment,enterFragment);
//                    enterFragment.getView().setX(0);

                }
                else{
                    RouterManager.getInstance().goTo(mContext,mPath,RouterManager.GOTO_ACTION_NORMAL,mBundle,animatorType);
                }
                break;
        }
        return true;
    }
}
