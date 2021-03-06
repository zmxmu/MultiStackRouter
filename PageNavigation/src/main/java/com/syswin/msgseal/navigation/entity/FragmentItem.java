package com.syswin.msgseal.navigation.entity;

import com.syswin.msgseal.navigation.BaseFragment;

import java.lang.ref.WeakReference;

/**
 * Created by zhengmin on 2019/2/21.
 */

public class FragmentItem extends PageItem {

    WeakReference<BaseFragment> mFragmentWR;

    public FragmentItem(int type, WeakReference<BaseFragment> fragmentWeakReference,String routerKey,int animatorType){
        super(type,routerKey,animatorType);

        mFragmentWR = fragmentWeakReference;
    }
    @Override
    public BaseFragment getItem() {
        if(mFragmentWR!=null){
            return mFragmentWR.get();
        }
        return null;
    }

    @Override
    public int getType() {
        return mType;
    }

    public WeakReference<BaseFragment> getFragmentWR() {
        return mFragmentWR;
    }

}
