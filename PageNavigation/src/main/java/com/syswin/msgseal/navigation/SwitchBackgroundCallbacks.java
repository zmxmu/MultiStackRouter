package com.syswin.msgseal.navigation;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.syswin.msgseal.navigation.model.ActivityItem;
import com.syswin.msgseal.navigation.model.RouterItem;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

public class SwitchBackgroundCallbacks implements Application.ActivityLifecycleCallbacks {
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
        if(TextUtils.equals(routerPath,BaseFragmentActivity.class.getName())){
            RouterManager.getInstance().getStack().push(
                    new ActivityItem(RouterItem.ROUTER_TYPE_CONTAINER,activityWeakReference,routerPath));
        }
        else{
            RouterManager.getInstance().getStack().push(
                    new ActivityItem(RouterItem.ROUTER_TYPE_ACTIVITY,activityWeakReference,routerPath));
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

    @Override
    public void onActivityDestroyed(Activity activity) {
        Stack<RouterItem> stack = RouterManager.getInstance().getStack();
        Iterator<RouterItem> itemIterator = stack.iterator();
        while(itemIterator.hasNext()){
            RouterItem currentItem = itemIterator.next();
            if(currentItem instanceof ActivityItem){
                if(activity == (Activity)((ActivityItem) currentItem).getActivityWR().get()){
                    itemIterator.remove();
                    break;
                }
            }
        }
    }
}
