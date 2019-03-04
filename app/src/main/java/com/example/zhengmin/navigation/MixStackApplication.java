package com.example.zhengmin.navigation;

import android.app.Application;

import com.syswin.msgseal.navigation.PageNavigation;
import com.syswin.msgseal.routeprocessor.RouteMap;

/**
 * Created by zhengmin on 2019/2/21.
 */

public class MixStackApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //注册监听器
        PageNavigation.getInstance().init(this);
        RouteMap.initPageMap(PageNavigation.getInstance().getRouterMap());
    }
}
