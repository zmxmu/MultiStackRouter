package com.example.zhengmin.mixstack;

import android.app.Application;

import com.syswin.msgseal.mixstack.RouterManager;
import com.syswin.msgseal.mixstack.SwitchBackgroundCallbacks;
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
