package com.example.zhengmin.mixstack;

import android.app.Activity;
import android.app.Application;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;

import com.example.zhengmin.mixstack.activity.BaseFragmentActivity;
import com.example.zhengmin.mixstack.base.ActivityItem;
import com.example.zhengmin.mixstack.base.RouterItem;
import com.example.zhengmin.mixstack.base.RouterManager;

import java.lang.ref.WeakReference;
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
            WeakReference<Activity> activityWeakReference = new WeakReference<Activity>(activity);
            if(activity instanceof BaseFragmentActivity){
                RouterManager.getInstance().getStack().push(
                        new ActivityItem(RouterItem.ROUTER_TYPE_CONTAINER,activityWeakReference));
            }
            else{
                RouterManager.getInstance().getStack().push(
                        new ActivityItem(RouterItem.ROUTER_TYPE_ACTIVITY,activityWeakReference));
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
            while(stack.size()>0){
                RouterItem currentItem = stack.pop();
                if(currentItem instanceof ActivityItem){
                    if(activity == (Activity)((ActivityItem) currentItem).getActivityWR().get()){
                        break;
                    }
                }
            }
        }
    }
}
