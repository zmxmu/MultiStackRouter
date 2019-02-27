package com.syswin.msgseal.navigation.animator;

import android.animation.Animator;

import com.syswin.msgseal.navigation.BaseFragment;

public interface IAnimator {
    int ANIMATION_DURATION = 300;
    void animatorEnter( BaseFragment exitFragment,BaseFragment enterFragment);
    void animatorExit( BaseFragment exitFragment,BaseFragment enterFragment);
}
