package com.example.zhengmin.mixstack;

import android.app.Activity;
import android.app.Application;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.zhengmin.mixstack.activity.BaseFragmentActivity;
import com.example.zhengmin.mixstack.base.ActivityItem;
import com.example.zhengmin.mixstack.base.RouterItem;
import com.example.zhengmin.mixstack.base.RouterManager;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by zhengmin on 2019/2/21.
 */

public class MixStackApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //注册监听器
        registerActivityLifecycleCallbacks(new SwitchBackgroundCallbacks());
    }


    private class SwitchBackgroundCallbacks implements ActivityLifecycleCallbacks {

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
}
