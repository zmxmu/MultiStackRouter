package com.syswin.msgseal.navigation;

import android.app.Activity;
import android.os.Bundle;

import com.syswin.msgseal.navigation.animator.PageTransferAnimator;
import com.syswin.msgseal.navigation.model.FragmentItem;
import com.syswin.msgseal.navigation.model.PageItem;

import java.lang.reflect.Constructor;

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
        if(mAnimator == null || animatorType!=mAnimatorType){
            mAnimatorType = animatorType;
            mAnimator = NavigationHelper.initAnimator(this,animatorType);
        }
        getFragmentManager().beginTransaction().add(R.id.content, enterFragment).commitAllowingStateLoss();
        if(exitFragment!=null && mAnimator!=null){
            mAnimator.animatorEnter(exitFragment,enterFragment);
        }
        enterFragment.onShow();
    }


    @Override
    public void onBackPressed() {
        PageItem topItem = RouterManager.getInstance().getTopItem();
        final BaseFragment exitFragment;
        if(topItem.getType() == PageItem.ROUTER_TYPE_FRAGMENT){
            exitFragment =  ((FragmentItem)topItem).getFragmentWR().get();
        }
        else{
            exitFragment =null;
            finish();
        }
        final BaseFragment enterFragment = RouterManager.getInstance().getSubTopFragment();
        if(enterFragment == null){
            finish();
        }
        else{
            getFragmentManager().beginTransaction().show(enterFragment).commit();
            mAnimator = NavigationHelper.initAnimator(this,topItem.getAnimatorType());
            if(mAnimator!=null){
                mAnimator.animatorExit(exitFragment,enterFragment);
            }
        }
    }
}
