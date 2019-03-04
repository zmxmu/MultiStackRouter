package com.syswin.msgseal.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.syswin.msgseal.navigation.animator.PageTransferAnimator;

import java.lang.reflect.Constructor;

/**
 * Fragment容器activity
 */
public class FragmentContainerActivity extends Activity {
    private PageTransferAnimator mAnimator;
    private int mAnimatorType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            addFragment(bundle, bundle.getString(PageNavigation.BUNDLE_KEY_FRAGMENT),
                    bundle.getInt(PageNavigation.BUNDLE_KEY_ANIMATOR_TYPE));
        }
    }

    /**
     * 根据页面路由获得fragment对象实例
     */
    private BaseFragment getFragment(String path) {
        try {
            Class fragmentClazz = PageNavigation.getInstance().getPathClass(path);
            Constructor<?> constructor = fragmentClazz.getConstructor(new Class[0]);
            return (BaseFragment) constructor.newInstance(new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 往容器添加新的fragment
     *
     * @param bundle       容器参数
     * @param path         页面路由地址
     * @param animatorType 切换动效类型
     */
    public void addFragment(Bundle bundle, String path, int animatorType) {
        final BaseFragment enterFragment = getFragment(path);
        final BaseFragment exitFragment = PageNavigation.getInstance().getTopFragment();
        if (enterFragment == null) {
            return;
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        enterFragment.setArguments(bundle);
        if (mAnimator == null || animatorType != mAnimatorType) {
            mAnimatorType = animatorType;
            mAnimator = NavigationHelper.initAnimator(this, animatorType);
        }
        getFragmentManager().beginTransaction().add(R.id.content, enterFragment).commitAllowingStateLoss();
        if (exitFragment != null && mAnimator != null) {
            mAnimator.animatorEnter(exitFragment, enterFragment);
        }
        enterFragment.onShow();
    }


    @Override
    public void onBackPressed() {
        PageNavigation.getInstance().go(this,NavigationFlags.GOBACK_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(this.getClass().getSimpleName(), "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(this.getClass().getSimpleName(), "onPause");
    }
}
