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
 * 页面路由管理器
 * Created by zhengmin on 2019/2/21.
 */

public class RouterManager {

    public static final String BUNDLE_KEY_ANIMATOR_TYPE = "animator_type";
    public static final String BUNDLE_KEY_FRAGMENT = "fragment_class";
    public static final String BUNDLE_KEY_PATH = "route_path";

    //跳转类型normal,跳转时不查询混合栈,直接栈顶新建页面
    public static final int GOTO_ACTION_NORMAL = 0;
    //跳转类型single,跳转时查询混合栈,如果有则重新打开该页面，并清空其上的页面
    public static final int GOTO_ACTION_SINGLE = 1;

    private RouterManager(){};
    private static volatile RouterManager instance;

    /**
     * 页面路由管理器
     * @return 管理器的懒加载单例
     */
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

    /**
     * 获得页面路由map表
     * @return 页面路由map表
     */
    public HashMap<String, Class> getRouterMap() {
        return mRouterMap;
    }

    /**
     * 获得页面混合栈
     * @return 页面混合栈
     */
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

    /**
     * 根据path路由进行页面跳转，支持activity、fragment，支持跳转动画及跳转参数传递
     * @param activity 发起跳转的activity
     * @param path 要跳转到的页面路由参数
     * @param actionType 跳转类型，支持normal和single两种模式
     * @param bundle 跳转参数
     * @param animatorType 跳转切换动效
     * @return 跳转成功与否的返回值
     */
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

    /**
     * 回退到指定path路由的页面，如果页面栈未找到该path，则不作跳转
     * @param activity 发起跳转的activity
     * @param path 要跳转到的页面路由参数
     * @param bundle 跳转参数
     * @return 跳转成功与否的返回值
     */
    public boolean goBack(Activity activity, String path,Bundle bundle){
        if(mRouterMap.containsKey(path)){
            return goTo(activity,path,GOTO_ACTION_SINGLE,bundle);
        }
        else{
            return false;
        }
    }

    /**
     * 启动一个新的activity
     * @param activity 发起该启动的activity
     * @param path 要启动的activity路径
     * @param bundle 启动参数
     * @param animatorType 跳转切换动效
     */
    public void startNewActivity(Activity activity,String path,Bundle bundle,int animatorType){
        Intent intent = new Intent(activity,mRouterMap.get(path));
        intent.putExtras(bundle);
        activity.startActivity(intent);
        int animatorArray[]=NavigationHelper.ANIMATOR_ARRAY[animatorType][ANIMATOR_START];
        activity.overridePendingTransition(animatorArray[0], animatorArray[1]);
    }

    /**
     * 根据路由信息查询在混合栈的序号
     * @param path
     * @return
     */
    public int getPathIndex(String path){
        for(int i = mStack.size()-1;i>=0;i--){
            if(TextUtils.equals(path,mStack.get(i).getRouterPath())){
                return i;
            }
        }
        return -1;
    }

    /**
     * 从混合栈栈顶开始查找，第一个找到的fragment容器
     * @return 第一个找到的fragment容器
     */
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

    /**
     * 栈顶如果是fragment，则返回该fragment
     * @return 栈顶fragment
     */
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

    /**
     *
     * @return 混合栈栈顶第一个元素
     */
    public PageItem getTopItem(){
        if(!mStack.isEmpty()){
            return (PageItem) mStack.peek();
        }
        else{
            return null;
        }
    }

    /**
     * 从栈顶开始查找，所能找到的第二个fragment
     * @return 栈顶第二个fragment
     */
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
