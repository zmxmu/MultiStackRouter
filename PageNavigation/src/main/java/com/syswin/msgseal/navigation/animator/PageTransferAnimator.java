package com.syswin.msgseal.navigation.animator;

import android.app.Activity;

import com.syswin.msgseal.navigation.BaseFragment;

public abstract class PageTransferAnimator {
    protected final int ANIMATION_DURATION = 300;
    protected Activity mActivity;
    protected int mValue;
    public PageTransferAnimator(Activity activity,int value){
        mActivity = activity;
        mValue = value;
    }
    public abstract void animatorEnter(BaseFragment exitFragment, BaseFragment enterFragment);
    public abstract void animatorExit( BaseFragment exitFragment,BaseFragment enterFragment);
}
