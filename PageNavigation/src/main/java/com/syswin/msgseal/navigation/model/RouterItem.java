package com.syswin.msgseal.navigation.model;

public class RouterItem {

    private Class mRouterClass;
    private int mRouterType;

    private Class getRouterClass(){
        return mRouterClass;
    }
    public int getRouterType() {
        return mRouterType;
    }

    public RouterItem(Class cls,int routerType){
        mRouterClass = cls;
        mRouterType = routerType;
    }

}
