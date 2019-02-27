package com.syswin.msgseal.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.syswin.msgseal.navigation.animator.IAnimator;
import com.syswin.msgseal.navigation.animator.SlideLeftRightAnimator;

import java.lang.reflect.Constructor;

import static com.syswin.msgseal.navigation.RouterManager.ANIMATOR_SLIDE_LEFT_RIGHT;

public class FragmentContainerActivity extends Activity {

    private int mFragmentWidth;
    private IAnimator mAnimator;
    private int mAnimatorType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment);
        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            addFragment(bundle
                    , bundle.getString(RouterManager.BUNDLE_KEY_FRAGMENT)
                            , bundle.getInt(RouterManager.BUNDLE_KEY_ANIMATOR_TYPE));
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
    public void addFragment(Bundle bundle, String path,int animatorType) {
        final BaseFragment enterFragment = getFragment(path);
        final BaseFragment exitFragment = RouterManager.getInstance().getTopFragment();
        if (enterFragment == null) {
            return;
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        enterFragment.setArguments(bundle);
        if(mAnimator == null || animatorType != mAnimatorType){
            mAnimatorType = animatorType;
            switch (animatorType){
                case ANIMATOR_SLIDE_LEFT_RIGHT:
                    mAnimator = new SlideLeftRightAnimator(this);
                    break;
            }

        }
        getFragmentManager().beginTransaction().add(R.id.content, enterFragment).commitAllowingStateLoss();
        if(exitFragment!=null && mAnimator!=null){
            mAnimator.animatorEnter(exitFragment,enterFragment,mFragmentWidth);
        }
        enterFragment.onShow();
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
                getFragmentManager().beginTransaction().show(enterFragment).commit();
                if(mAnimator!=null){
                    mAnimator.animatorExit(exitFragment,enterFragment,mFragmentWidth);
                }
            }
        }
    }
}
