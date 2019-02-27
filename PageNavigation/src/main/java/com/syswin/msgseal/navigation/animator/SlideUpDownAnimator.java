package com.syswin.msgseal.navigation.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;

import com.syswin.msgseal.navigation.BaseFragment;

public class SlideUpDownAnimator extends PageTransferAnimator {

    public SlideUpDownAnimator(Activity activity, int value) {
        super(activity, value);
    }

    @Override
    public void animatorEnter(final BaseFragment exitFragment, final BaseFragment enterFragment) {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(mValue, 0).setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (exitFragment != null && exitFragment.getView() != null) {
                    exitFragment.getView().setY(value - mValue);
                }
                if (enterFragment != null && enterFragment.getView() != null) {
                    enterFragment.getView().setY(value);
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
    public void animatorExit(final BaseFragment exitFragment,final BaseFragment enterFragment) {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0,mValue).setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (exitFragment != null && exitFragment.getView() != null) {
                    exitFragment.getView().setY(value);
                }
                if (enterFragment != null && enterFragment.getView() != null) {
                    enterFragment.getView().setY(value - mValue);
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
                    mActivity.getFragmentManager().beginTransaction().remove(exitFragment).commit();
                }
            }
        });

        valueAnimator.start();
    }
}
