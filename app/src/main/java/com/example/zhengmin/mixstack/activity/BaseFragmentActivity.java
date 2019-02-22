package com.example.zhengmin.mixstack.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.zhengmin.mixstack.R;
import com.example.zhengmin.mixstack.base.FragmentItem;
import com.example.zhengmin.mixstack.base.RouterManager;
import com.example.zhengmin.mixstack.fragment.BaseFragment;

import java.lang.reflect.Constructor;

public class BaseFragmentActivity extends Activity {
    public static final int ANIMATION_DURATION = 300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment);
        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            addFragment(bundle
                    , bundle.getString(RouterManager.BUNDLE_KEY_FRAGMENT));
        }
    }
    private BaseFragment getFragment(String className) {
        try {
            Class fragmentClazz = Class.forName(className);
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
    public void addFragment(Bundle bundle, String className) {
        BaseFragment newFragment = getFragment(className);
        BaseFragment finalExitFragment = RouterManager.getInstance().getTopFragment();
        if (newFragment == null) {
            return;
        }
        if (bundle == null) {
            bundle = new Bundle();
        }

        newFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().add(R.id.content, newFragment).commitAllowingStateLoss();
        if(finalExitFragment!=null){
            transformAnimation(finalExitFragment,newFragment);
        }
    }
    private void transformAnimation(final BaseFragment exitFragment, final BaseFragment enterFragment ){
        final int width = getFragmentWidth();

        final ValueAnimator valueAnimator = ValueAnimator.ofInt(width, 0).setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (exitFragment != null && exitFragment.getView() != null) {
                    exitFragment.getView().setX(value - width);
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
    private int getFragmentWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @Override
    public void onBackPressed() {
        BaseFragment currentFragment = RouterManager.getInstance().getTopFragment();

        if (currentFragment == null) {
            finish();
        }
        else{
            removeTopFragment();
        }
    }
    private void removeTopFragment(){
        BaseFragment exitFragment = ((FragmentItem)(RouterManager.getInstance().getStack().pop()))
                .getFragmentWR().get();
        BaseFragment enterFragment = RouterManager.getInstance().getTopFragment();
        if(enterFragment == null){
            finish();
        }
        else{
            getFragmentManager().beginTransaction().show(enterFragment)
                    .commit();
            enterFragment.getView().setX(0);
            getFragmentManager().beginTransaction().replace(R.id.content,
                    enterFragment).commit();
            //transformAnimation(exitFragment,enterFragment);
        }
    }
}
