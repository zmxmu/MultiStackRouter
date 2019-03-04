package com.syswin.msgseal.navigation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.syswin.msgseal.navigation.NavigationFlags.GOBACK_NORMAL;
import static com.syswin.msgseal.navigation.NavigationFlags.GOBACK_SPECIFY;
import static com.syswin.msgseal.navigation.NavigationFlags.GOTO_NORMAL;
import static com.syswin.msgseal.navigation.NavigationFlags.GOTO_SINGLE;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        GOTO_NORMAL,
        GOTO_SINGLE,
        GOBACK_SPECIFY,
        GOBACK_NORMAL
})
public @interface NavigationFlags {

    //跳转类型normal,跳转时不查询混合栈,直接栈顶新建页面
    int GOTO_NORMAL = 0;

    //跳转类型single,跳转时查询混合栈,如果有则重新打开该页面，并清空其上的页面
    int GOTO_SINGLE = 1;

    int GOBACK_SPECIFY = 2;

    int GOBACK_NORMAL = 3;
}
