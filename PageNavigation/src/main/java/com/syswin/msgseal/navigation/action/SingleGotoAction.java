package com.syswin.msgseal.navigation.action;

import android.app.Activity;
import android.os.Bundle;

import com.syswin.msgseal.navigation.BaseFragment;
import com.syswin.msgseal.navigation.FragmentContainerActivity;
import com.syswin.msgseal.navigation.NavigationHelper;
import com.syswin.msgseal.navigation.PageNavigation;
import com.syswin.msgseal.navigation.entity.ActivityItem;
import com.syswin.msgseal.navigation.entity.FragmentItem;
import com.syswin.msgseal.navigation.entity.PageItem;

import java.util.Stack;

import static com.syswin.msgseal.navigation.NavigationFlags.GOTO_NORMAL;

/*
single模式，从栈顶开始查找栈中是否有该页面，有的话激活该页面，清空其上元素
 */
public class SingleGotoAction extends GotoAction {
    public SingleGotoAction(Activity activity, String path, Bundle bundle, int itemType) {
        super(activity, path, bundle, itemType);
    }

    @Override
    public boolean gotoPage(int animatorType) {
        int index = PageNavigation.getInstance().getPathIndex(mPath);
        Stack<PageItem> stack = PageNavigation.getInstance().getStack();
        if (index == (stack.size() - 1)) {
            return false;
        }
        switch (mItemType) {
            case PageItem.ROUTER_TYPE_ACTIVITY:
                if (index >= 0) {
                    for (int i = stack.size() - 1; i > index; i--) {
                        PageItem currentItem = stack.get(i);
                        if (currentItem.getType() == PageItem.ROUTER_TYPE_ACTIVITY
                                || currentItem.getType() == PageItem.ROUTER_TYPE_CONTAINER) {
                            Activity activity = ((ActivityItem) currentItem).getActivityWR().get();
                            activity.finish();
                            if (i == stack.size() - 1) {
                                NavigationHelper.executeExitAnimator(activity);
                            }
                        }
                    }
                } else {
                    PageNavigation.getInstance().goTo(mActivity, mPath, GOTO_NORMAL, mBundle);
                }
                break;
            case PageItem.ROUTER_TYPE_FRAGMENT:
                if (index > 0) {
                    BaseFragment enterFragment = ((FragmentItem) stack.get(index)).getFragmentWR().get();
                    FragmentContainerActivity container = (FragmentContainerActivity) enterFragment.getActivity();
                    BaseFragment exitFragment = PageNavigation.getInstance().getTopFragment();
                    if (exitFragment.getActivity() != container) {
                        exitFragment = null;
                    }
                    for (int i = stack.size() - 1; i > index; i--) {
                        PageItem currentItem = stack.get(i);
                        if (currentItem.getType() == PageItem.ROUTER_TYPE_ACTIVITY
                                || currentItem.getType() == PageItem.ROUTER_TYPE_CONTAINER) {
                            ((ActivityItem) currentItem).getActivityWR().get().finish();
                        } else if (currentItem.getType() == PageItem.ROUTER_TYPE_FRAGMENT) {
                            if (i == stack.size() - 1 && exitFragment != null) {
                                continue;
                            }
                            BaseFragment currentFragment = ((FragmentItem) currentItem).getFragmentWR().get();
                            FragmentContainerActivity currentContainer = (FragmentContainerActivity) currentFragment.getActivity();
                            currentContainer.getFragmentManager().beginTransaction().remove(currentFragment).commit();
                        }
                    }

                    container.getFragmentManager().beginTransaction().show(enterFragment).commit();
                    enterFragment.onShow();
                    NavigationHelper.initAnimator(container, animatorType).animatorExit(exitFragment, enterFragment);
//                    enterFragment.getView().setX(0);

                } else {
                    PageNavigation.getInstance().goTo(mActivity, mPath, GOTO_NORMAL, mBundle, animatorType);
                }
                break;
        }
        return true;
    }
}
