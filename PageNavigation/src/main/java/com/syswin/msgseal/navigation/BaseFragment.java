package com.syswin.msgseal.navigation;

import android.app.Fragment;
import android.os.Bundle;

import com.syswin.msgseal.navigation.model.FragmentItem;
import com.syswin.msgseal.navigation.model.RouterItem;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

public class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        WeakReference<BaseFragment> weakReference = new WeakReference<BaseFragment>(this);
        Bundle bundle = getArguments();
        if(bundle!=null){
            String routerPath=bundle.getString(RouterManager.BUNDLE_KEY_FRAGMENT);
            RouterManager.getInstance().getStack().push(
                    new FragmentItem(RouterItem.ROUTER_TYPE_FRAGMENT,weakReference,routerPath));
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Stack<RouterItem> stack = RouterManager.getInstance().getStack();
        Iterator<RouterItem> itemIterator = stack.iterator();
        while(itemIterator.hasNext()){
            RouterItem currentItem = itemIterator.next();
            if(currentItem instanceof FragmentItem){
                if(this == (BaseFragment)((FragmentItem) currentItem).getFragmentWR().get()){
                    itemIterator.remove();
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
