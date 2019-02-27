package com.syswin.msgseal.navigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;


import java.lang.reflect.Constructor;

public class FragmentContainerActivity extends Activity {
    public static final int ANIMATION_DURATION = 300;
    private int mFragmentWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment);
        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            addFragment(bundle
                    , bundle.getString(RouterManager.BUNDLE_KEY_FRAGMENT));
        }
        mFragmentWidth = getFragmentWidth();
    }
    private BaseFragment getFragment(String path) {
        try {
            Class fragmentClazz = RouterManager.getInstance().getRouterMap().get(path);
            Constructor<?> constructor = fragmentClazz.getConstructor(new Class[0]);
            return (BaseFragment) constructor.newInstance(new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 新加页面
     */
    public void addFragment(Bundle bundle, String path) {
        final BaseFragment enterFragment = getFragment(path);
        final BaseFragment exitFragment = RouterManager.getInstance().getTopFragment();
        if (enterFragment == null) {
            return;
        }
        if (bundle == null) {
            bundle = new Bundle();
        }

        enterFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().add(R.id.content, enterFragment).commitAllowingStateLoss();
        if(exitFragment!=null){
            final ValueAnimator valueAnimator = ValueAnimator.ofInt(mFragmentWidth, 0).setDuration(ANIMATION_DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    if (exitFragment != null && exitFragment.getView() != null) {
                        exitFragment.getView().setX(value - mFragmentWidth);
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
                        getFragmentManager().beginTransaction().hide(exitFragment).commitAllowingStateLoss();
                    }
                    if (enterFragment != null) {
                        enterFragment.onShow();
                    }
                }
            });

            valueAnimator.start();
        }
    }

    private int getFragmentWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @Override
    public void onBackPressed() {
        final BaseFragment exitFragment = RouterManager.getInstance().getTopFragment();
        if (exitFragment == null) {
            finish();
        }
        else{
            final BaseFragment enterFragment = RouterManager.getInstance().getSubTopFragment();
            if(enterFragment == null){
                finish();
            }
            else{
                fragmentTransfer(exitFragment,enterFragment);
            }
        }
    }
    public void fragmentTransfer(final BaseFragment exitFragment,final BaseFragment enterFragment){
        getFragmentManager().beginTransaction().show(enterFragment).commit();

        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0,mFragmentWidth).setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (exitFragment != null && exitFragment.getView() != null) {
                    exitFragment.getView().setX(value);
                }
                if (enterFragment != null && enterFragment.getView() != null) {
                    enterFragment.getView().setX(value - mFragmentWidth);
                }
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                valueAnimator.removeListener(this);
                valueAnimator.removeAllUpdateListeners();
                getFragmentManager().beginTransaction().remove(exitFragment).commit();
            }
        });

        valueAnimator.start();
    }
}
