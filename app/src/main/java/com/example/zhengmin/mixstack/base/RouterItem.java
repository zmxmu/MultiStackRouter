package com.example.zhengmin.mixstack.base;

public abstract class RouterItem {
    public static final int ROUTER_TYPE_NONE = 0;
    public static final int ROUTER_TYPE_ACTIVITY = 1;
    public static final int ROUTER_TYPE_FRAGMENT = 2;
    public static final int ROUTER_TYPE_CONTAINER = 3;
    private String mRouterKey;
    abstract Object getItem();
    abstract int getType();
    public String getRouterKey(){
        return mRouterKey;
    }
    public void setRouterKey(String key){
        mRouterKey = key;
    }

}
