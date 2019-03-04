package com.syswin.msgseal.navigation.entity;

/**
 * 混合栈元素 基类
 */
public abstract class PageItem {
    public static final int ROUTER_TYPE_NONE = 0;
    public static final int ROUTER_TYPE_ACTIVITY = 1;
    public static final int ROUTER_TYPE_FRAGMENT = 2;
    public static final int ROUTER_TYPE_CONTAINER = 3;
    private String mRouterPath;
    int mType;
    private int mAnimatorType;

    abstract Object getItem();

    public String getRouterPath() {
        return mRouterPath;
    }

    public void setRouterPath(String Path) {
        mRouterPath = Path;
    }

    public PageItem(int type, String routerPath) {
        mType = type;
        mRouterPath = routerPath;
    }

    public PageItem(int type, String routerPath, int animatorType) {
        mType = type;
        mRouterPath = routerPath;
        mAnimatorType = animatorType;
    }

    public int getAnimatorType() {
        return mAnimatorType;
    }

    public int getType() {
        return mType;
    }
}
