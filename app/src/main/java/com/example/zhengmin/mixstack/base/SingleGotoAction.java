package com.example.zhengmin.mixstack.base;

import android.content.Context;
import android.os.Bundle;

import java.util.Stack;

public class SingleGotoAction implements GotoAction {
    @Override
    public boolean gotoPage(Context context, String path, Bundle bundle, int itemType) {
        int index =  RouterManager.getInstance().getPathIndex(path);
        Stack<RouterItem> stack = RouterManager.getInstance().getStack();
        if(index == (stack.size()-1)){
            return false;
        }
        switch (itemType){
            case RouterItem.ROUTER_TYPE_ACTIVITY:
                if(index>=0){
                    for(int i = stack.size()-1;i>index;i--){
                        RouterItem currentItem = stack.get(i);
                        if(currentItem.getType() == RouterItem.ROUTER_TYPE_ACTIVITY
                                || currentItem.getType() == RouterItem.ROUTER_TYPE_CONTAINER){
                            ((ActivityItem)currentItem).getActivityWR().get().finish();
                        }
                    }
                }
                else{
                    RouterManager.getInstance().goTo(context,path,new NormalGotoAction(),bundle);
                }
                break;
            case RouterItem.ROUTER_TYPE_FRAGMENT:
                if(index>0){
                    BaseFragment fragment = ((FragmentItem)stack.get(index)).getFragmentWR().get();
                    BaseFragmentActivity container = (BaseFragmentActivity)fragment.getActivity();
                    for(int i = stack.size()-1;i>index;i--){
                        RouterItem currentItem = stack.get(i);
                        if(currentItem.getType() == RouterItem.ROUTER_TYPE_ACTIVITY
                                || currentItem.getType() == RouterItem.ROUTER_TYPE_CONTAINER){
                            ((ActivityItem)currentItem).getActivityWR().get().finish();
                        }
                        else if(currentItem.getType() == RouterItem.ROUTER_TYPE_FRAGMENT){
                            BaseFragment currentFragment = ((FragmentItem)currentItem).getFragmentWR().get();
                            BaseFragmentActivity currentContainer = (BaseFragmentActivity)currentFragment.getActivity();
                            currentContainer.getFragmentManager().beginTransaction().remove(currentFragment).commit();
                        }
                    }
                    container.getFragmentManager().beginTransaction().show(fragment).commit();
                    fragment.getView().setX(0);

                }
                else{
                    RouterManager.getInstance().goTo(context,path,new NormalGotoAction(),bundle);
                }
                break;
        }
        return true;
    }
}
