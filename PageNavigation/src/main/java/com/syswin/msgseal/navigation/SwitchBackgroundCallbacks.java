package com.syswin.msgseal.navigation;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.syswin.msgseal.navigation.model.ActivityItem;
import com.syswin.msgseal.navigation.model.PageItem;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

/**
 * activity生命周期回调callback
 */
public class SwitchBackgroundCallbacks implements Application.ActivityLifecycleCallbacks {
    /**
     * activity创建时的回调
     * @param activity
     * @param bundle
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        String routerPath;
        Bundle paraBundle = activity.getIntent().getExtras();
        if(paraBundle!=null){
            routerPath=paraBundle.getString(RouterManager.BUNDLE_KEY_PATH);
        }
        else{
            routerPath = activity.getClass().getName();
        }
        WeakReference<Activity> activityWeakReference = new WeakReference<Activity>(activity);
        if(TextUtils.equals(routerPath,FragmentContainerActivity.class.getName())){
            //判断属于fragment容器activity，进行压栈处理
            RouterManager.getInstance().getStack().push(
                    new ActivityItem(PageItem.ROUTER_TYPE_CONTAINER,activityWeakReference,routerPath));
        }
        else{
            //普通activity压栈处理
            RouterManager.getInstance().getStack().push(
                    new ActivityItem(PageItem.ROUTER_TYPE_ACTIVITY,activityWeakReference,routerPath));
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    /**
     * activity销毁时的回调
     * @param activity
     */
    @Override
    public void onActivityDestroyed(Activity activity) {
        Stack<PageItem> stack = RouterManager.getInstance().getStack();
        Iterator<PageItem> itemIterator = stack.iterator();
        while(itemIterator.hasNext()){
            PageItem currentItem = itemIterator.next();
            if(currentItem instanceof ActivityItem){
                if(activity == (Activity)((ActivityItem) currentItem).getActivityWR().get()){
                    itemIterator.remove();
                    break;
                }
            }
        }
    }
}
