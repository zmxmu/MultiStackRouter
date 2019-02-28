package com.syswin.msgseal.navigation.model;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by zhengmin on 2019/2/21.
 */

public class ActivityItem extends PageItem {

    WeakReference<Activity> mActivityWR;
    public ActivityItem(int type,WeakReference<Activity> activityWeakReference,String routerPath){
        super(type,routerPath);
        mActivityWR = activityWeakReference;
    }
    @Override
    public Activity getItem() {
        if(mActivityWR!=null){
            return mActivityWR.get();
        }
        return null;
    }


    public WeakReference<Activity> getActivityWR() {
        return mActivityWR;
    }

}
