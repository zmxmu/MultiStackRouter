package com.example.zhengmin.mixstack.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.zhengmin.mixstack.activity.BaseFragmentActivity;
import com.example.zhengmin.mixstack.fragment.BaseFragment;

import java.util.Stack;

/**
 * Created by zhengmin on 2019/2/21.
 */

public class RouterManager {

    public static final String BUNDLE_KEY_FRAGMENT = "fragmentCls";
    public static final String BUNDLE_KEY_PARAMS = "params";
    private RouterManager(){};
    private static volatile RouterManager instance;
    public  static  RouterManager  getInstance(){
        if(instance == null ){
            synchronized (RouterManager.class){
                if(instance == null){
                    instance =  new RouterManager();
                }
            }
        }
        return instance;
    }

    public Stack<RouterItem> getStack() {
        return mStack;
    }

    private Stack<RouterItem> mStack = new Stack<>();
    private int getItemType(Class clz){
        int result  = RouterItem.ROUTER_TYPE_NONE;

        while(!TextUtils.equals(clz.getSimpleName(),"Object")){
            if(TextUtils.equals(clz.getSimpleName(),"Fragment")){
                return RouterItem.ROUTER_TYPE_FRAGMENT;
            }
            else if(TextUtils.equals(clz.getSimpleName(),"BaseFragmentActivity")){
                return RouterItem.ROUTER_TYPE_CONTAINER;
            }
            else if(TextUtils.equals(clz.getSimpleName(),"Activity")){
                return RouterItem.ROUTER_TYPE_ACTIVITY;
            }
            else{
                clz = clz.getSuperclass();
            }
        }
        return result;
    }
    public boolean jumpTo(Context context,String path, JumpAction action, Bundle bundle){
        Class clz;
        try {
            clz = Class.forName(path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        int itemType = getItemType(clz);

        if(itemType == RouterItem.ROUTER_TYPE_NONE){
            return false;
        }
        return action.gotoNewPage(context,path,bundle,itemType);
    }
    public void startNewActivity(Context context,String path,Bundle bundle){
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(context.getClass().getName(),path);
        intent.setComponent(componentName);
        context.startActivity(intent,bundle);
    }
    private int getPathIndex(String path){
        for(int i = mStack.size()-1;i>=0;i--){
            if(TextUtils.equals(path,mStack.get(i).getRouterKey())){
                return i;
            }
        }
        return -1;
    }
    public BaseFragmentActivity getLastContainer(){
        for(int i = mStack.size()-1;i>=0;i--){
            if(mStack.get(i).getType()== RouterItem.ROUTER_TYPE_CONTAINER){
                return (BaseFragmentActivity)((ActivityItem)mStack.get(i)).getActivityWR().get();
            }
        }
        return null;
    }
    public BaseFragment getTopFragment(){
        RouterItem currentItem;
        if(!mStack.isEmpty()){
            currentItem = (RouterItem) mStack.peek();
            if(currentItem.getType() == RouterItem.ROUTER_TYPE_FRAGMENT){
                return ((FragmentItem)currentItem).getFragmentWR().get();
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }
    public BaseFragment getSubTopFragment(){
        RouterItem currentItem;
        if(mStack.size()>2){
            currentItem = mStack.get(mStack.size()-1);
            if(currentItem!=null && currentItem.getType() == RouterItem.ROUTER_TYPE_FRAGMENT){
                currentItem = mStack.get(mStack.size()-2);
                if(currentItem!=null && currentItem.getType() == RouterItem.ROUTER_TYPE_FRAGMENT){
                    return ((FragmentItem)currentItem).getFragmentWR().get();
                }
                else{
                    return null;
                }
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }
}
