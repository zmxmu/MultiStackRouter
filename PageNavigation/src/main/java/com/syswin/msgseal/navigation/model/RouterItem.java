package com.syswin.msgseal.navigation.model;

public abstract class RouterItem {
    public static final int ROUTER_TYPE_NONE = 0;
    public static final int ROUTER_TYPE_ACTIVITY = 1;
    public static final int ROUTER_TYPE_FRAGMENT = 2;
    public static final int ROUTER_TYPE_CONTAINER = 3;
    private String mRouterPath;
    abstract Object getItem();
    public abstract int getType();
    public String getRouterPath(){
        return mRouterPath;
    }
    public void setRouterPath(String Path){
        mRouterPath = Path;
    }
    public RouterItem(String routerPath){
        mRouterPath = routerPath;
    }

}
