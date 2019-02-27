package com.syswin.msgseal.navigation;

import android.app.Activity;
import android.os.Bundle;

import com.syswin.msgseal.navigation.animator.PageTransferAnimator;
import com.syswin.msgseal.navigation.animator.SlideLeftRightAnimator;
import com.syswin.msgseal.navigation.animator.SlideUpDownAnimator;

import java.lang.reflect.Constructor;

import static com.syswin.msgseal.navigation.RouterManager.ANIMATOR_SLIDE_LEFT_RIGHT;
import static com.syswin.msgseal.navigation.RouterManager.ANIMATOR_SLIDE_UP_DOWN;

public class FragmentContainerActivity extends Activity {
    private PageTransferAnimator mAnimator;
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
    private void initAnimator(int animatorType){
        if(mAnimator == null || animatorType != mAnimatorType){
            mAnimatorType = animatorType;
            switch (animatorType){
                case ANIMATOR_SLIDE_LEFT_RIGHT:
                    mAnimator = new SlideLeftRightAnimator(this,Utils.getFragmentWidth(this));
                    break;
                case ANIMATOR_SLIDE_UP_DOWN:
                    mAnimator = new SlideUpDownAnimator(this,Utils.getFragmentHeight(this));
                    break;
            }
        }
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
        initAnimator(animatorType);
        getFragmentManager().beginTransaction().add(R.id.content, enterFragment).commitAllowingStateLoss();
        if(exitFragment!=null && mAnimator!=null){
            mAnimator.animatorEnter(exitFragment,enterFragment);
        }
        enterFragment.onShow();
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
                    mAnimator.animatorExit(exitFragment,enterFragment);
                }
            }
        }
    }
}
