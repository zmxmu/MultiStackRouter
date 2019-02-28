package com.syswin.msgseal.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.syswin.msgseal.navigation.action.NormalGotoAction;
import com.syswin.msgseal.navigation.action.SingleGotoAction;
import com.syswin.msgseal.navigation.model.ActivityItem;
import com.syswin.msgseal.navigation.model.FragmentItem;
import com.syswin.msgseal.navigation.model.PageItem;

import java.util.HashMap;
import java.util.Stack;

import static com.syswin.msgseal.navigation.NavigationHelper.ANIMATOR_SLIDE_LEFT_RIGHT;
import static com.syswin.msgseal.navigation.NavigationHelper.ANIMATOR_START;

/**
 * Created by zhengmin on 2019/2/21.
 */

public class RouterManager {
    public static final String BUNDLE_KEY_ANIMATOR_TYPE = "animator_type";
    public static final String BUNDLE_KEY_FRAGMENT = "fragment_class";
    public static final String BUNDLE_KEY_PATH = "route_path";
    public static final int GOTO_ACTION_NORMAL = 0;
    public static final int GOTO_ACTION_SINGLE = 1;

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

    private HashMap<String, Class> mRouterMap = new HashMap<>();

    public HashMap<String, Class> getRouterMap() {
        return mRouterMap;
    }
    public Stack<PageItem> getStack() {
        return mStack;
    }

    private Stack<PageItem> mStack = new Stack<>();

    private int getItemType(String path){
        int result  = PageItem.ROUTER_TYPE_NONE;
        Class clz = mRouterMap.get(path);
        if(TextUtils.equals(clz.getSimpleName(),"FragmentContainerActivity")){
            return PageItem.ROUTER_TYPE_CONTAINER;
        }
        else if((Activity.class).isAssignableFrom(clz)){
            return PageItem.ROUTER_TYPE_ACTIVITY;
        }
        else if((BaseFragment.class).isAssignableFrom(clz)){
            return PageItem.ROUTER_TYPE_FRAGMENT;
        }
        return result;
    }
    public boolean goTo(Activity activity, String path, int actionType, Bundle bundle,int animatorType){
        if(bundle == null){
            bundle = new Bundle();
        }
        bundle.putString(BUNDLE_KEY_PATH,path);
        switch (actionType){
            case GOTO_ACTION_NORMAL:
                return new NormalGotoAction(activity,path,bundle,getItemType(path)).gotoPage(animatorType);
            case GOTO_ACTION_SINGLE:
                return new SingleGotoAction(activity,path,bundle,getItemType(path)).gotoPage(animatorType);
        }
        return false;
    }
    public boolean goTo(Activity activity, String path, int actionType, Bundle bundle){
        return goTo(activity,path,actionType,bundle,ANIMATOR_SLIDE_LEFT_RIGHT);
    }

    public boolean goBack(Activity activity, String path,Bundle bundle){
        if(mRouterMap.containsKey(path)){
            return goTo(activity,path,GOTO_ACTION_SINGLE,bundle);
        }
        else{
            return false;
        }
    }

    public void startNewActivity(Activity activity,String path,Bundle bundle,int animatorType){
        Intent intent = new Intent(activity,mRouterMap.get(path));
        intent.putExtras(bundle);
        activity.startActivity(intent);
        int animatorArray[]=NavigationHelper.ANIMATOR_ARRAY[animatorType][ANIMATOR_START];
        activity.overridePendingTransition(animatorArray[0], animatorArray[1]);
    }
    public int getPathIndex(String path){
        for(int i = mStack.size()-1;i>=0;i--){
            if(TextUtils.equals(path,mStack.get(i).getRouterPath())){
                return i;
            }
        }
        return -1;
    }
    public FragmentContainerActivity getLastContainer(){
        for(int i = mStack.size()-1;i>=0;i--){
            if(mStack.get(i).getType()== PageItem.ROUTER_TYPE_CONTAINER){
                return (FragmentContainerActivity)((ActivityItem)mStack.get(i)).getActivityWR().get();
            }
            else if(mStack.get(i).getType()== PageItem.ROUTER_TYPE_ACTIVITY){
                return null;
            }
        }
        return null;
    }
    public BaseFragment getTopFragment(){
        PageItem currentItem;
        if(!mStack.isEmpty()){
            currentItem = (PageItem) mStack.peek();
            if(currentItem.getType() == PageItem.ROUTER_TYPE_FRAGMENT){
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
    public PageItem getTopItem(){
        if(!mStack.isEmpty()){
            return (PageItem) mStack.peek();
        }
        else{
            return null;
        }
    }
    public BaseFragment getSubTopFragment(){
        PageItem currentItem;
        if(mStack.size()>2){
            currentItem = mStack.get(mStack.size()-1);
            if(currentItem!=null && currentItem.getType() == PageItem.ROUTER_TYPE_FRAGMENT){
                currentItem = mStack.get(mStack.size()-2);
                if(currentItem!=null && currentItem.getType() == PageItem.ROUTER_TYPE_FRAGMENT){
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
