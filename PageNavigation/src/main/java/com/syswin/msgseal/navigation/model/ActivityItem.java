package com.syswin.msgseal.navigation.model;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by zhengmin on 2019/2/21.
 */

public class ActivityItem extends RouterItem {

    WeakReference<Activity> mActivityWR;
    int mType;
    public ActivityItem(int type,WeakReference<Activity> activityWeakReference,String routerPath){
        super(routerPath);
        mType = type;
        mActivityWR = activityWeakReference;

    }
    @Override
    public Activity getItem() {
        if(mActivityWR!=null){
            return mActivityWR.get();
        }
        return null;
    }

    @Override
    public int getType() {
        return mType;
    }

    public WeakReference<Activity> getActivityWR() {
        return mActivityWR;
    }

}
