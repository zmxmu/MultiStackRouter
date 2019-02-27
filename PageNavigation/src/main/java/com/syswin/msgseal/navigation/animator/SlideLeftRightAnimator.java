package com.syswin.msgseal.navigation.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;

import com.syswin.msgseal.navigation.BaseFragment;

public class SlideLeftRightAnimator implements IAnimator {
    private Activity mActivity;
    public SlideLeftRightAnimator(Activity activity){
        mActivity = activity;
    }
    @Override
    public void animatorEnter(final BaseFragment exitFragment, final BaseFragment enterFragment
            ,final int startValue) {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(startValue, 0).setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (exitFragment != null && exitFragment.getView() != null) {
                    exitFragment.getView().setX(value - startValue);
                }
                if (enterFragment != null && enterFragment.getView() != null) {
                    enterFragment.getView().setX(value);
                }
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                valueAnimator.removeListener(this);
                valueAnimator.removeAllUpdateListeners();
                if (exitFragment != null) {
                    exitFragment.onHide();
                    mActivity.getFragmentManager().beginTransaction().hide(exitFragment).commitAllowingStateLoss();
                }
            }
        });

        valueAnimator.start();

    }

    @Override
    public void animatorExit(final BaseFragment exitFragment,final BaseFragment enterFragment,final int startValue) {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0,startValue).setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (exitFragment != null && exitFragment.getView() != null) {
                    exitFragment.getView().setX(value);
                }
                if (enterFragment != null && enterFragment.getView() != null) {
                    enterFragment.getView().setX(value - startValue);
                }
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                valueAnimator.removeListener(this);
                valueAnimator.removeAllUpdateListeners();
                mActivity.getFragmentManager().beginTransaction().remove(exitFragment).commit();
            }
        });

        valueAnimator.start();
    }
}
