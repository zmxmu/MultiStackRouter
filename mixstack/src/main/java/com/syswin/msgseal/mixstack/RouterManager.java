package com.syswin.msgseal.mixstack;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.syswin.msgseal.mixstack.action.GotoAction;
import com.syswin.msgseal.mixstack.model.ActivityItem;
import com.syswin.msgseal.mixstack.model.FragmentItem;
import com.syswin.msgseal.mixstack.model.RouterItem;

import java.util.Stack;

/**
 * Created by zhengmin on 2019/2/21.
 */

public class RouterManager {

    public static final String BUNDLE_KEY_FRAGMENT = "fragmentCls";
    public static final String BUNDLE_KEY_PATH = "route_path";
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
            if(TextUtils.equals(clz.getSimpleName(),"BaseFragment")){
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
    public boolean goTo(Context context, String path, GotoAction action, Bundle bundle){
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
        if(bundle == null){
            bundle = new Bundle();
        }
        bundle.putString(BUNDLE_KEY_PATH,path);
        return action.gotoPage(context,path,bundle,itemType);
    }
    public void startNewActivity(Context context,String path,Bundle bundle){
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(context.getPackageName(),path);
        intent.setComponent(componentName);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    public int getPathIndex(String path){
        for(int i = mStack.size()-1;i>=0;i--){
            if(TextUtils.equals(path,mStack.get(i).getRouterPath())){
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
            else if(mStack.get(i).getType()== RouterItem.ROUTER_TYPE_ACTIVITY){
                return null;
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
