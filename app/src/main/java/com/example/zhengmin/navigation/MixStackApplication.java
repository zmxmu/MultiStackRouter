package com.example.zhengmin.navigation;

import android.app.Application;

import com.syswin.msgseal.navigation.RouterManager;
import com.syswin.msgseal.navigation.SwitchBackgroundCallbacks;
import com.syswin.msgseal.routeprocessor.RouteMap;

/**
 * Created by zhengmin on 2019/2/21.
 */

public class MixStackApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //注册监听器
        registerActivityLifecycleCallbacks(new SwitchBackgroundCallbacks());
        RouteMap.initPageMap(RouterManager.getInstance().getRouterMap());
    }

}
