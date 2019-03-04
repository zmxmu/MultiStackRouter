package com.syswin.msgseal.navigation;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.syswin.msgseal.navigation.action.NormalGotoAction;
import com.syswin.msgseal.navigation.action.SingleGotoAction;
import com.syswin.msgseal.navigation.animator.PageTransferAnimator;
import com.syswin.msgseal.navigation.entity.ActivityItem;
import com.syswin.msgseal.navigation.entity.FragmentItem;
import com.syswin.msgseal.navigation.entity.PageItem;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static com.syswin.msgseal.navigation.NavigationFlags.GOBACK_NORMAL;
import static com.syswin.msgseal.navigation.NavigationFlags.GOBACK_SPECIFY;
import static com.syswin.msgseal.navigation.NavigationFlags.GOTO_NORMAL;
import static com.syswin.msgseal.navigation.NavigationFlags.GOTO_SINGLE;
import static com.syswin.msgseal.navigation.NavigationHelper.ANIMATOR_FINISH;
import static com.syswin.msgseal.navigation.NavigationHelper.ANIMATOR_SLIDE_LEFT_RIGHT;
import static com.syswin.msgseal.navigation.NavigationHelper.ANIMATOR_START;

/**
 * 页面路由管理器 Created by zhengmin on 2019/2/21.
 */

public class PageNavigation {

    public static final String BUNDLE_KEY_ANIMATOR_TYPE = "animator_type";
    public static final String BUNDLE_KEY_FRAGMENT = "fragment_class";
    public static final String BUNDLE_KEY_PATH = "route_path";

    private static volatile PageNavigation instance;

    private Map<String, Class> mRouterMap = new HashMap<>();

    private PageNavigation() {
    }

    /**
     * 页面路由管理器
     *
     * @return 管理器的懒加载单例
     */
    public static PageNavigation getInstance() {
        if (instance == null) {
            synchronized (PageNavigation.class) {
                if (instance == null) {
                    instance = new PageNavigation();
                }
            }
        }
        return instance;
    }

    public void init(Application app) {
        app.registerActivityLifecycleCallbacks(new SwitchBackgroundCallbacks());
    }

    /**
     * 获得页面混合栈
     *
     * @return 页面混合栈
     */
    public Stack<PageItem> getStack() {
        return mStack;
    }

    private Stack<PageItem> mStack = new Stack<>();

    private int getItemType(String path) {
        int result = PageItem.ROUTER_TYPE_NONE;
        Class clz = getPathClass(path);
        if (TextUtils.equals(clz.getSimpleName(), "FragmentContainerActivity")) {
            return PageItem.ROUTER_TYPE_CONTAINER;
        } else if ((Activity.class).isAssignableFrom(clz)) {
            return PageItem.ROUTER_TYPE_ACTIVITY;
        } else if ((BaseFragment.class).isAssignableFrom(clz)) {
            return PageItem.ROUTER_TYPE_FRAGMENT;
        }
        return result;
    }

    /**
     * 根据path路由进行页面跳转，支持activity、fragment，支持跳转动画及跳转参数传递
     *
     * @param activity     发起跳转的activity
     * @param actionType   跳转类型
     * @param path         要跳转到的页面路由参数
     * @param bundle       跳转参数
     * @param animatorType 跳转切换动效
     * @return 跳转成功与否的返回值
     */
    public boolean go(Activity activity, @NavigationFlags int actionType, String path, Bundle bundle, int animatorType) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(BUNDLE_KEY_PATH, path);
        switch (actionType) {
            case GOTO_NORMAL:
                return new NormalGotoAction(activity, path, bundle, getItemType(path)).gotoPage(animatorType);
            case GOTO_SINGLE:
                return new SingleGotoAction(activity, path, bundle, getItemType(path)).gotoPage(animatorType);
            case GOBACK_NORMAL:
                return go(activity, GOBACK_NORMAL);
            case GOBACK_SPECIFY:
                if (mRouterMap.containsKey(path))
                    return go(activity, GOTO_SINGLE, path, bundle);
                else
                    break;
        }
        return false;
    }

    public boolean go(Activity activity, @NavigationFlags int actionType, String path, Bundle bundle) {
        return go(activity, actionType, path, bundle, ANIMATOR_SLIDE_LEFT_RIGHT);
    }

    public boolean go(Activity activity, @NavigationFlags int actionType) {
        if (actionType == GOBACK_NORMAL) {
            if(activity instanceof FragmentContainerActivity){
                PageItem topItem = PageNavigation.getInstance().getTopItem();
                int animatorArray[] = NavigationHelper.ANIMATOR_ARRAY[topItem.getAnimatorType()][ANIMATOR_FINISH];
                final BaseFragment exitFragment;
                if (topItem.getType() == PageItem.ROUTER_TYPE_FRAGMENT) {
                    exitFragment = ((FragmentItem) topItem).getFragmentWR().get();
                } else {
                    exitFragment = null;
                    activity.finish();
                    activity.overridePendingTransition(animatorArray[0], animatorArray[1]);
                }
                final BaseFragment enterFragment = PageNavigation.getInstance().getSubTopFragment();
                if (enterFragment == null) {
                    exitFragment.onHide();
                    activity.finish();
                    activity.overridePendingTransition(animatorArray[0], animatorArray[1]);
                } else {
                    activity.getFragmentManager().beginTransaction().show(enterFragment).commit();
                    enterFragment.onShow();
                    PageTransferAnimator animator = NavigationHelper.initAnimator(activity, topItem.getAnimatorType());
                    if (animator != null) {
                        animator.animatorExit(exitFragment, enterFragment);
                    }
                }
            }
            else{
                NavigationHelper.executeExitAnimator(activity);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得页面路由对应的page 类
     * @param path     要跳转到的页面路由参数
     * @return 页面路由map表
     */
    public Class getPathClass(String path){
        Class result = mRouterMap.get(path);
        if (result == null) {
            try {
                Class cls = Class.forName("com.syswin.msgseal.routeprocessor."+"Navigation_"+path.replace('.','_'));
                Method method = cls.getMethod("getPageClass", null);
                result = (Class)method.invoke(null, null);
                if(result != null ){
                    mRouterMap.put(path,result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    /**
     * 启动一个新的activity
     *
     * @param activity     发起该启动的activity
     * @param path         要启动的activity路径
     * @param bundle       启动参数
     * @param animatorType 跳转切换动效
     */
    public void startNewActivity(Activity activity, String path, Bundle bundle, int animatorType) {
        Intent intent = new Intent(activity, getPathClass(path));
        intent.putExtras(bundle);
        activity.startActivity(intent);
        int animatorArray[] = NavigationHelper.ANIMATOR_ARRAY[animatorType][ANIMATOR_START];
        activity.overridePendingTransition(animatorArray[0], animatorArray[1]);
    }

    /**
     * 根据路由信息查询在混合栈的序号
     */
    public int getPathIndex(String path) {
        for (int i = mStack.size() - 1; i >= 0; i--) {
            if (TextUtils.equals(path, mStack.get(i).getRouterPath())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 从混合栈栈顶开始查找，第一个找到的fragment容器
     *
     * @return 第一个找到的fragment容器
     */
    public FragmentContainerActivity getLastContainer() {
        for (int i = mStack.size() - 1; i >= 0; i--) {
            if (mStack.get(i).getType() == PageItem.ROUTER_TYPE_CONTAINER) {
                return (FragmentContainerActivity) ((ActivityItem) mStack.get(i)).getActivityWR().get();
            } else if (mStack.get(i).getType() == PageItem.ROUTER_TYPE_ACTIVITY) {
                return null;
            }
        }
        return null;
    }

    /**
     * 栈顶如果是fragment，则返回该fragment
     *
     * @return 栈顶fragment
     */
    public BaseFragment getTopFragment() {
        PageItem currentItem;
        if (!mStack.isEmpty()) {
            currentItem = (PageItem) mStack.peek();
            if (currentItem.getType() == PageItem.ROUTER_TYPE_FRAGMENT) {
                return ((FragmentItem) currentItem).getFragmentWR().get();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @return 混合栈栈顶第一个元素
     */
    public PageItem getTopItem() {
        if (!mStack.isEmpty()) {
            return mStack.peek();
        } else {
            return null;
        }
    }

    /**
     * 从栈顶开始查找，所能找到的第二个fragment
     *
     * @return 栈顶第二个fragment
     */
    public BaseFragment getSubTopFragment() {
        PageItem currentItem;
        if (mStack.size() > 2) {
            currentItem = mStack.get(mStack.size() - 1);
            if (currentItem != null && currentItem.getType() == PageItem.ROUTER_TYPE_FRAGMENT) {
                currentItem = mStack.get(mStack.size() - 2);
                if (currentItem != null && currentItem.getType() == PageItem.ROUTER_TYPE_FRAGMENT) {
                    return ((FragmentItem) currentItem).getFragmentWR().get();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
