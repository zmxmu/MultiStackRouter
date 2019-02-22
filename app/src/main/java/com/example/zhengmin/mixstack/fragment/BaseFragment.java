package com.example.zhengmin.mixstack.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.example.zhengmin.mixstack.base.ActivityItem;
import com.example.zhengmin.mixstack.base.FragmentItem;
import com.example.zhengmin.mixstack.base.RouterItem;
import com.example.zhengmin.mixstack.base.RouterManager;

import java.lang.ref.WeakReference;
import java.util.Stack;

public class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        WeakReference<BaseFragment> weakReference = new WeakReference<BaseFragment>(this);
        RouterManager.getInstance().getStack().push(
                new FragmentItem(RouterItem.ROUTER_TYPE_FRAGMENT,weakReference));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Stack<RouterItem> stack = RouterManager.getInstance().getStack();
        while(stack.size()>0){
            RouterItem currentItem = stack.pop();
            if(currentItem instanceof FragmentItem){
                if(this == (BaseFragment)((FragmentItem) currentItem).getFragmentWR().get()){
                    break;
                }
            }
        }
    }
    public void onShow() {

    }

    public void onHide() {

    }
}