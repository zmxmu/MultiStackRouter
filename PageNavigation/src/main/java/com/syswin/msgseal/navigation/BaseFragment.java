package com.syswin.msgseal.navigation;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.syswin.msgseal.navigation.entity.FragmentItem;
import com.syswin.msgseal.navigation.entity.PageItem;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

/**
 * Fragment基类，所有新建的Fragment类都得继承此基类
 */
public class BaseFragment extends Fragment implements PageLifeCycle {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeakReference<BaseFragment> weakReference = new WeakReference<BaseFragment>(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String routerPath = bundle.getString(PageNavigation.BUNDLE_KEY_FRAGMENT);
            int animatorType = bundle.getInt(PageNavigation.BUNDLE_KEY_ANIMATOR_TYPE);
            PageNavigation.getInstance().getStack().push(
                    new FragmentItem(PageItem.ROUTER_TYPE_FRAGMENT, weakReference, routerPath, animatorType));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Stack<PageItem> stack = PageNavigation.getInstance().getStack();
        Iterator<PageItem> itemIterator = stack.iterator();
        while (itemIterator.hasNext()) {
            PageItem currentItem = itemIterator.next();
            if (currentItem instanceof FragmentItem) {
                if (this == (BaseFragment) ((FragmentItem) currentItem).getFragmentWR().get()) {
                    itemIterator.remove();
                    break;
                }
            }
        }
    }

    @Override
    public void onShow() {
        Log.e(this.getClass().getSimpleName(), "onShow");
    }

    @Override
    public void onHide() {
        Log.e(this.getClass().getSimpleName(), "onHide");
    }

    @Override
    public void onResume() {
        super.onResume();
        //onShow();
        //Log.e(this.getClass().getSimpleName(),"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        //onHide();
        //Log.e(this.getClass().getSimpleName(),"onPause");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if(hidden){
//            onHide();
//        }
//        else{
//            onShow();
//        }
//        Log.e(this.getClass().getSimpleName(),"onHiddenChanged"+hidden);
    }
}
